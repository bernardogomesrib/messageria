package com.messageria.demo.consumer;



import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;
import com.messageria.demo.entidade.PedidoRepository;
import com.messageria.demo.producer.RabbitMQProducer;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor

public class RabbitMQConsumer {
    private final PedidoRepository pedidoRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${rabbitmq.queue.name}", ackMode = "MANUAL")
    public void consume(Pedido message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {

        try {
            
            log.info("Mensagem recebida: {}", message);
            pedidoRepository.save(message);
            channel.basicAck(tag, false);
            log.info("Pedido salvo: {}", message);
            // Enviando mensagem para o notificador
            rabbitMQProducer.sendMessage(message);
        } catch (Exception ex) {
            log.error("Erro ao processar mensagem: {}", ex.getMessage());
            try {
                channel.basicNack(tag, false, true);
            } catch (Exception e) {
                log.error("Erro ao dar reject na mensagem");
            }
        }

    }
}
