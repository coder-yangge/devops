package com.devops.web.common.interceptor;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import com.devops.common.exception.BizException;
import com.devops.web.common.annotation.NoLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author yangge
 * @version 1.0.0
 * @title: AuthorizationInterceptor
 * @date 2020/4/16 10:56
 */
@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    public final static String SESSION_ACCOUNT = "sessionAccount";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("开始拦截请求 request url:{}, time:{}", request.getRequestURI(), LocalDateTime.now());
        HttpSession session = request.getSession();
        try {
            Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
            if (ObjectUtils.isEmpty(account)) {
                if (handler instanceof ResourceHttpRequestHandler) {
                    // 未登录 跳转到登录界面
                    response.sendRedirect(request.getContextPath() + "/index.html");
                    return false;
                }
                throw new BizException("account.login.state.fail");
            }
            AccountContextHolder.putAccount(account);
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new BizException("account.login.state.fail");
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> beanType = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            if (beanType.isAnnotationPresent(NoLogin.class) || method.isAnnotationPresent(NoLogin.class)) {
                return true;
            }
        } else {

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // do nothing
    }
}
