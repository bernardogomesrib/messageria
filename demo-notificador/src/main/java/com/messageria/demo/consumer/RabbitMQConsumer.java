package com.messageria.demo.consumer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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
    
    private final Map<Long, CompletableFuture<Boolean>> ackCache = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "${rabbitmq.queue.name}", ackMode = "MANUAL")
    public void consume(Pedido message,  Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag) {
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
            }
            
        } catch (Exception e) {
            log.error("Erro ao processar a mensagem: {}", e.getMessage());
            // Não confirma a mensagem, ela permanecerá na fila
        }finally {
            // Remove o CompletableFuture do cache
            ackCache.remove(message.getId());
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


