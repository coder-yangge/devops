package com.devops.common.exception;

import lombok.Data;

/**
 * @author yangge
 * @version 1.0.0
 * @title: BizException
 * @description: 系统异常
 * @date 2020/4/16 9:55
 */
@Data
public class BizException extends RuntimeException {

    private Object code;

    private Object[] args;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BizException(String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.args = args;
    }

    public BizException(Object code, Object[] args) {
        this.code = code;
        this.args = args;
    }

    public BizException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
