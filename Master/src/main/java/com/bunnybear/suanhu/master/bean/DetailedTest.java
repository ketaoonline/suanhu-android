package com.bunnybear.suanhu.master.bean;

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
    String user_avatar;
    String user_name;


    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
