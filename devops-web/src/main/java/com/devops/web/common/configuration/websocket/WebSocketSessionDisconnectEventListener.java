package com.devops.web.common.configuration.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author yangge
 * @version 1.0.0
 * @title: WebSocketSessionDisconnectEventListener
 * @date 2020/7/17 17:38
 */
@Component
@Slf4j
public class WebSocketSessionDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent>{

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("WebSocket SessionId [{}]关闭连接", sessionId);
    }
}