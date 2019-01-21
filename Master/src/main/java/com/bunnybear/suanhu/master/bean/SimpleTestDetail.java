package com.bunnybear.suanhu.master.bean;

import java.util.List;

public class SimpleTestDetail {

    int user_id;//用户ID
    String user_name;//用户名字
    String user_avatar;//用户头像
    String question; //用户问题
    List<String> question_image;//问题图片
    String order_use_sn;//唯一订单号
    int question_id;//问题ID

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(List<String> question_image) {
        this.question_image = question_image;
    }

    public String getOrder_use_sn() {
        return order_use_sn;
    }

    public void setOrder_use_sn(String order_use_sn) {
        this.order_use_sn = order_use_sn;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
