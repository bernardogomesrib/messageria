package com.messageria.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.messageria.demo.entidade.Pedido;
import com.messageria.demo.entidade.PedidoRepository;
import com.messageria.demo.producer.RabbitMQProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final PedidoRepository pedidoRepository;
    private final RabbitMQProducer rabbitMQProducer;


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(Pedido message) {
        
        log.info("Mensagem recebida: {}", message);
        pedidoRepository.save(message);
        log.info("Pedido salvo: {}", message);
        //Enviando mensagem para o notificador
        rabbitMQProducer.sendMessage(message);
        
    }
}
