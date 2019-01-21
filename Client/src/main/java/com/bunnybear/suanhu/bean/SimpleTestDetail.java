package com.bunnybear.suanhu.bean;

import java.util.List;

public class SimpleTestDetail {

    MyQuestion question;

    List<Answer> answer;

    public MyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(MyQuestion question) {
        this.question = question;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }
}
