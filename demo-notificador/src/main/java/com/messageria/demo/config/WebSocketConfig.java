// filepath: /home/bernardo/Documentos/Github/messageria/demo-notificador/src/main/java/com/messageria/demo/config/WebSocketConfig.java
package com.messageria.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Prefixo para os tópicos de mensagens
        config.setApplicationDestinationPrefixes("/app"); // Prefixo para mensagens enviadas do cliente
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-notifications").setAllowedOrigins("*").withSockJS(); // Endpoint WebSocket
    }
}