package com.heking.hzz.Base;

/**
 * Created by Administrator on 2017/10/11.
 */

public class MessageEvent {
    public int code;
    public String message;
    public Object obj;

    public MessageEvent(int code) {
        this.code = code;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

    public MessageEvent(String message, Object obj) {
        this.message = message;
        this.obj = obj;
    }

    public MessageEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageEvent(int code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
}
