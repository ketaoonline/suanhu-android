package com.bunnybear.suanhu.master.net;

public class JsonResult<T> {

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
