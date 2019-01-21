package com.bunnybear.suanhu.master.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class ChatMsg implements MultiItemEntity, Serializable {


    int chat_id;//聊天对话ID
    int send_user;//发送人ID
    int to_user;//接收人ID
    int type;//类型1:send_text 2:send_image 3:receive_text 4:receive_image
    String content;//图片地址或者内容
    int order_sn;//订单号
    long create_time;//时间
    String send_avatar;//发送者头像
    String to_avatar;//接收者头像

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getSend_user() {
        return send_user;
    }

    public void setSend_user(int send_user) {
        this.send_user = send_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(int order_sn) {
        this.order_sn = order_sn;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getSend_avatar() {
        return send_avatar;
    }

    public void setSend_avatar(String send_avatar) {
        this.send_avatar = send_avatar;
    }

    public String getTo_avatar() {
        return to_avatar;
    }

    public void setTo_avatar(String to_avatar) {
        this.to_avatar = to_avatar;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
