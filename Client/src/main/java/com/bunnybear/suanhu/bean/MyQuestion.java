package com.bunnybear.suanhu.bean;

import java.util.List;

public class MyQuestion extends MainBaseBean{
    int question_id;                                   //问题ID
    String question;     //问题内容
    List<String> question_image;                                //问题图片
    int user_id;                                           //用户ID
    String user_avatar;                           //用户头像
    String question_type;                     //问题类型


    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
