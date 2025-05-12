package com.messageria.demo.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    @Value("${rabbitmq.topic.exchange.name}")
    private String topicExchangeName;

    @Value("${rabbitmq.routing.key.notificacoes}")
    private String notificacoesRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Pedido message) {
        // Envia a mensagem para o exchange com a routing key apropriada
        rabbitTemplate.convertAndSend(topicExchangeName, notificacoesRoutingKey, message);
        log.info("Mensagem enviada para notificações: {}", message);
    }
}