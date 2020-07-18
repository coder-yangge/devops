package com.devops.web.common.interceptor;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import com.devops.common.constants.UserConstants;
import com.devops.common.exception.BizException;
import com.devops.web.common.annotation.NoLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * <p>用户建立连接时，判断用户是否已经登录，未登录则拒绝建立连接</p>
 * @author yangge
 * @version 1.0.0
 * @title: WebSocketInterceptor
 * @date 2020/7/17 16:33
 */
@Component
@Slf4j
public class WebSocketInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        try {
            Account account = (Account) session.getAttribute(UserConstants.SESSION_ACCOUNT);
            if (ObjectUtils.isEmpty(account)) {
                log.info("开始拦截请求 request url:{}, time:{}", request.getRequestURI(), LocalDateTime.now());
            }
            AccountContextHolder.putAccount(account);
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new BizException("account.login.state.fail");
        }
        return true;
    }


}
