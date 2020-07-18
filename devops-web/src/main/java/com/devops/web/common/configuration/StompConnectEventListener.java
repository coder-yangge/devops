package com.devops.web.common.configuration;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


/**
 * @author yangge
 * @version 1.0.0
 * @title: StompConnectEventListener
 * @date 2020/7/1 15:07
 */
@Component
@Slf4j
public class StompConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        log.info("webSocket session create id: {}", sessionId);
    }
}
