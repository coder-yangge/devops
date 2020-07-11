package com.devops.web.common.handler;

import com.devops.common.exception.BizException;
import com.devops.web.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author yangge
 * @version 1.0.0
 * @title: WebExceptionHandler
 * @description: 全局异常捕获
 * @date 2020/4/15 17:28
 */
@ControllerAdvice(value = "com.devops")
@Slf4j
public class WebExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVo globalException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ResponseVo vo = new ResponseVo();
        vo.setMessage(exception.getMessage());
        vo.setSuccess(false);
        vo.setCode(500);
        return vo;
    }

    @ExceptionHandler({BizException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseVo<Object> handleBizException(BizException e) {
        log.error(e.getMessage(), e);
        ResponseVo<Object> vo = new ResponseVo<>();
        vo.setSuccess(false);
        getMessage(e, e.getMessage(), e.getArgs(), vo);
        return vo;
    }

    private void getMessage(Exception e, String message2, Object[] args, ResponseVo vo) {
        if (message2 != null) {
            String message = e.getMessage();
            try {
                message = messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e1) {
                e1.printStackTrace();
            }
            vo.setMessage(message);
        }
    }

}
