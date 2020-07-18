package com.devops.web.common.configuration.websocket;

import com.devops.common.acount.Account;
import com.devops.common.constants.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>用户建立连接时，判断用户是否已经登录，未登录则拒绝建立连接</p>
 * @author yangge
 * @version 1.0.0
 * @title: WebSocketInterceptor
 * @date 2020/7/17 16:33
 */
//@Component
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Account account = (Account)httpServletRequest.getSession().getAttribute(UserConstants.SESSION_ACCOUNT);
        if (account == null) {
            log.error("==============>>未登录<<==================");
            return false;
        }
        log.info("用户[{}]sessionId[{}]开始建立连接", account, account.getWebSocketSessionId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完毕");
    }
}
