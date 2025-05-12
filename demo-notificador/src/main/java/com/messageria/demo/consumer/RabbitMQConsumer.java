package com.messageria.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RabbitMQConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(Pedido message) {
        
        log.info("Mensagem recebida: {}", message);
        
    }
}
