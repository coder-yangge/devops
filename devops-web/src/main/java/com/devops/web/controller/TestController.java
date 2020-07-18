package com.devops.web.controller;

import com.devops.common.acount.Account;
import com.devops.common.acount.AccountContextHolder;
import com.devops.common.exception.BizException;
import com.devops.web.common.interceptor.AuthorizationInterceptor;
import com.devops.web.common.vo.ResponseVo;
import com.devops.web.form.SimpleMessageForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author yangge
 * @version 1.0.0
 * @title: TestController
 * @date 2020/4/15 17:56
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/account/login")
    public ResponseVo<String> login(String userName, String password) throws Exception {
        log.info("username:{}, password:{}", userName, password);
        if (StringUtils.isBlank(userName)) {
            throw new BizException("account.login.state.fail");
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        Account account = new Account();
        account.setUserName(userName);
        account.setPassWord(password);
        account.setUserId("1");
        session.setAttribute(AuthorizationInterceptor.SESSION_ACCOUNT, account);
        Cookie cookie = new Cookie("SESSION-ACCOUNT-TEST", UUID.randomUUID().toString().toUpperCase());
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseVo<>("success");
    }

    @Autowired
    HttpServletRequest httpServletRequest;

    @GetMapping("/get/account")
    public ResponseVo<Account> getAccount() {
        Account account = AccountContextHolder.getAccount();
        ResponseVo<Account> vo = new ResponseVo<>();
        vo.setData(account);
        return vo;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping(value = "/push/user", produces = {"application/json"})
    public ResponseVo pushUser(@RequestBody SimpleMessageForm messageForm) {
        log.info("发送消息user[{}] 内容[{}]", AccountContextHolder.getAccount().getUserName(), messageForm.getMessage());

        messagingTemplate.convertAndSendToUser(AccountContextHolder.getAccount().getUserName(), messageForm.getTopic(), messageForm);
        return new ResponseVo<>("success");
    }

}
