package com.shengzhe.disan.xuetangteacher.http;

/**
 * service-下载接口
 * Created by hy on 2017/10/18.
 */
public class BaseHttpResult<T> {


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    private T data;
    private int error_code;
    private String error_message;
}
