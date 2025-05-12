package com.messageria.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String pedidosQueueName;

    @Value("${rabbitmq.queue.notificacoes}")
    private String notificacoesQueueName;

    @Value("${rabbitmq.topic.exchange.name}")
    private String topicExchangeName;

    @Value("${rabbitmq.routing.key.pedidos}")
    private String pedidosRoutingKey;

    @Value("${rabbitmq.routing.key.notificacoes}")
    private String notificacoesRoutingKey;

    @Bean
    public Queue pedidosQueue() {
        return new Queue(pedidosQueueName, true);
    }

    @Bean
    public Queue notificacoesQueue() {
        return new Queue(notificacoesQueueName, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding pedidosBinding() {
        return BindingBuilder
                .bind(pedidosQueue())
                .to(exchange())
                .with(pedidosRoutingKey);
    }

    @Bean
    public Binding notificacoesBinding() {
        return BindingBuilder
                .bind(notificacoesQueue())
                .to(exchange())
                .with(notificacoesRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}