package com.learn.websocket.learnwebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // End point "/my-app" is for user use for connect websocket with this sepecific app
        registry.addEndpoint("/my-app")
        .setAllowedOrigins("http://localhost:8080")
        .withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry registry){

        // The point and rule of stomjs to subscribe broker to send message like all for all messages and user for private message
        registry.enableSimpleBroker("/all", "/user");

        // This is app releated part if you want to send message to any user or public so you you have to send this "/app/anything here"
        registry.setApplicationDestinationPrefixes("/app");
        // registry.setUserDestinationPrefix("/user");
    }
}
