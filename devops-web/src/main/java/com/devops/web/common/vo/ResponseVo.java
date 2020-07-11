package com.devops.web.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ResponseVo
 * @description: TODO
 * @date 2020/4/15 17:45
 */
@Data
public class ResponseVo<T> {

    private int code;

    private boolean success;

    private String message;

    private T data;

    private List<FieldExceptionVO> fieldErrors;

    public ResponseVo(String message) {
        this.message = message;
    }

    public ResponseVo() {
    }

    public ResponseVo(T data) {
        this.data = data;
    }

    public static class ResponseBuilder {

        public static ResponseVo buildSuccess(int code, String msg, boolean success, Object data){
            ResponseVo responseVo = new ResponseVo();
            responseVo.setCode(code);
            responseVo.setSuccess(success);
            responseVo.setMessage(msg);
            responseVo.setData(data);
            return responseVo;
        }

        public static ResponseVo buildSuccess(int code, String msg, Object data){

            return buildSuccess(code, msg, true, data);
        }

        public static ResponseVo buildSuccess(int code, Object data){

            return buildSuccess(code, "", true, data);
        }

        public static ResponseVo buildSuccess(String msg, Object data){

            return buildSuccess(200, msg, true, data);
        }

        public static ResponseVo buildSuccess(Object data){

            return buildSuccess(200, "", true, data);
        }

        public static ResponseVo buildSuccess(String msg){

            return buildSuccess(200, msg, true, null);
        }

        public static ResponseVo buildSuccess(){

            return buildSuccess(200, "", true, null);
        }
    }
}
