package com.devops.web.common.configuration.websocket;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import com.devops.web.common.interceptor.AuthorizationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author yangge
 * @version 1.0.0
 * @title: DevopsHandshakeHandler
 * @date 2020/7/17 14:27
 */
@Slf4j
@Component
public class DevOpsHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        List<String> cookies = request.getHeaders().get("cookie");
        cookies.forEach(cookie -> log.info(cookie));
        Account account = (Account)httpServletRequest.getSession().getAttribute(AuthorizationInterceptor.SESSION_ACCOUNT);
        if (account != null) {
            AccountContextHolder.putAccount(account);
            return () -> account.getUserName();
        }
        return null;
    }
}
