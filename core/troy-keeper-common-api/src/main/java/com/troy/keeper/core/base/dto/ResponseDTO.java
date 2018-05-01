package com.troy.keeper.core.base.dto;

import java.io.Serializable;

/**
 * Created by yg on 2017/3/30.
 */
public class ResponseDTO<T> implements Serializable{
    private String code;
    private String msg;
    private T data;

    public ResponseDTO() {
    }

    public ResponseDTO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseDTO(String code, String msg,T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
