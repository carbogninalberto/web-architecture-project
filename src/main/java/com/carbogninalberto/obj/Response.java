package com.carbogninalberto.obj;

import java.io.Serializable;

public class Response implements Serializable {
    private String msg;

    public Response(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
