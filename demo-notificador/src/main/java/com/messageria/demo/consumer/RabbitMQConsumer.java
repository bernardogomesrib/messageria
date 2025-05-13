package com.messageria.demo.consumer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RabbitMQConsumer {

    private static final int MAX_CACHE_SIZE = 3; // Limite máximo de mensagens no ackCache
    private final Map<Long, CompletableFuture<Boolean>> ackCache = new ConcurrentHashMap<>();
    private final Semaphore semaphore = new Semaphore(MAX_CACHE_SIZE); // Controla o número de mensagens simultâneas

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "${rabbitmq.queue.name}", ackMode = "MANUAL")
    public void consume(Pedido message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        if (!semaphore.tryAcquire()) {
            log.warn("ackCache está cheio. Mensagem rejeitada: {}", message);
            try {
                channel.basicReject(tag, true); // Rejeita a mensagem e reencaminha para a fila
            } catch (Exception e) {
                log.error("Erro ao rejeitar mensagem: {}", e.getMessage());
            }
            return;
        }

        try {
            log.info("Mensagem recebida: {}", message);
            log.info("Enviando notificação para o cliente");

            CompletableFuture<Boolean> ackFuture = new CompletableFuture<>();
            ackCache.put(message.getId(), ackFuture);

            // Envia a notificação para o front-end via WebSocket
            messagingTemplate.convertAndSend("/topic/notifications", message);

            // Aguarda o acknowledgment do cliente
            boolean ackReceived = ackFuture.get(10, TimeUnit.SECONDS);
            if (ackReceived) {
                channel.basicAck(tag, false);
                log.info("Mensagem processada com sucesso e confirmada.");
            } else {
                log.warn("Nenhum acknowledgment recebido do cliente. Mensagem será reprocessada.");
                channel.basicNack(tag, false, true); // Reencaminha a mensagem para a fila
            }
        } catch (Exception e) {
            log.error("Erro ao processar a mensagem: {}", e.getMessage());
            try {
                channel.basicNack(tag, false, true); // Reencaminha a mensagem para a fila
            } catch (Exception nackException) {
                log.error("Erro ao reencaminhar mensagem: {}", nackException.getMessage());
            }
        } finally {
            ackCache.remove(message.getId());
            semaphore.release(); // Libera o "permite" no Semaphore
        }
    }

    public void receiveAck(Long messageId) {
        CompletableFuture<Boolean> ackFuture = ackCache.get(messageId);
        if (ackFuture != null) {
            ackFuture.complete(true);
            log.info("Acknowledgment recebido para a mensagem: {}", messageId);
        } else {
            log.warn("Acknowledgment recebido para mensagem desconhecida: {}", messageId);
        }
    }
}
