package com.chen.common_service.dto;

import lombok.Data;

/**
 * @author cgh
 * @create 2023-03-20
 * 统一返回结果类
 */
@Data
public class Result<T> {

    private static final Integer CODE_200 = 200;
    private static final Integer NO_AUTH_510 = 510;
    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回数据
     */
    private T data;

    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setSuccess(true);
        r.setCode(CODE_200);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> OK(String msg, T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setMessage(msg);
        r.setData(data);
        r.setCode(CODE_200);
        return r;
    }

    public static <T> Result<T> OK() {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        return r;
    }

    public static <T> Result<T> error(String errorMsg) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(errorMsg);
        return r;
    }

    public static <T> Result<T> error(String errorMsg, Integer code) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setCode(code);
        return r;
    }

    public static <T> Result<T> noAuth(String msg) {
        return error(msg, NO_AUTH_510);
    }
}
