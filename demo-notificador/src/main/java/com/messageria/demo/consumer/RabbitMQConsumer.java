// filepath: /home/bernardo/Documentos/Github/messageria/demo-notificador/src/main/java/com/messageria/demo/consumer/RabbitMQConsumer.java
package com.messageria.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RabbitMQConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(Pedido message) {
        log.info("Mensagem recebida: {}", message);
        log.info("Enviando notificação para o cliente");

        // Envia a notificação para o front-end via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}