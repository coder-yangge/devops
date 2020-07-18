package com.devops.web.common.configuration;

import com.devops.web.common.configuration.websocket.DevOpsHandshakeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author yangge
 * @version 1.0.0
 * @title: WebSocketConfiguration
 * @date 2020/7/1 12:55
 */
@Slf4j
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private DevOpsHandshakeHandler devopsHandshakeHandler;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setHandshakeHandler(devopsHandshakeHandler)
                .setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker (MessageBrokerRegistry registry){
        //表明在topic、queue、user这三个域上可以向客户端发消息。
        registry.enableSimpleBroker("/topic", "/user", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

}
