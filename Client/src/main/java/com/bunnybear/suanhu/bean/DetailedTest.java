package com.bunnybear.suanhu.bean;

import java.io.Serializable;

public class DetailedTest implements Serializable {

    int order_use_sn;//唯一订单ID
    String coursename;//订单类型
    int end_time;//是否完成，0：未完成，其他：已完成
    int user_id;//用户ID
    int type;//订单类型
    int pay_stuats;//支付状态0：未支付，1：已支付
    int sum_chat;//有多少条回复
    String answer;//最后一天回复
    int new_chat;//是否有新消息：0：有新消息，1：没有
    String master_avatar;
    String master_name;
    int key_id;

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public String getMaster_avatar() {
        return master_avatar;
    }

    public void setMaster_avatar(String master_avatar) {
        this.master_avatar = master_avatar;
    }

    public int getOrder_use_sn() {
        return order_use_sn;
    }

    public void setOrder_use_sn(int order_use_sn) {
        this.order_use_sn = order_use_sn;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPay_stuats() {
        return pay_stuats;
    }

    public void setPay_stuats(int pay_stuats) {
        this.pay_stuats = pay_stuats;
    }

    public int getSum_chat() {
        return sum_chat;
    }

    public void setSum_chat(int sum_chat) {
        this.sum_chat = sum_chat;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getNew_chat() {
        return new_chat;
    }

    public void setNew_chat(int new_chat) {
        this.new_chat = new_chat;
    }
}
