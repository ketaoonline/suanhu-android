package com.xiaoxiong.library.event;

/**
 * Created by Administrator on 2018/8/31.
 */

public class IEvent {

    Object object;
    String msgCode;

    public IEvent(String msgCode, Object object) {
        this.object = object;
        this.msgCode = msgCode;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
