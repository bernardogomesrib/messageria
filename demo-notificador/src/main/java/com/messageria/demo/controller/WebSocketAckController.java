package com.messageria.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messageria.demo.consumer.RabbitMQConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WebSocketAckController {

    private final RabbitMQConsumer rabbitMQConsumer;

    @PostMapping("/ack")
    public void receiveAck(@RequestBody AckReq id) {
        rabbitMQConsumer.receiveAck(id.getId());
    }
}