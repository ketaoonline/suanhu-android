package com.bunnybear.suanhu.net;

import java.io.Serializable;

public class JsonResult<T> implements Serializable{

    public T data;
    public int code;
    public String msg;

    public boolean isOk() {
        if (code == 1) {
            return true;
        }
        return false;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }

}
