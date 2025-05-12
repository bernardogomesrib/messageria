package com.messageria.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messageria.demo.entidade.Pedido;
import com.messageria.demo.producer.RabbitMQProducer;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
public class PedidoControler {
    private final RabbitMQProducer producer;

    @PostMapping("/send")
    public Pedido postPedido(@RequestBody Pedido entity) {
        producer.sendMessage(entity);
        return entity;
    } 
}
