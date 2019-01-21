package com.bunnybear.suanhu.bean;

public class Question {
    int question_id;                //问题ID
    String content;//内容

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
