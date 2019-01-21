package com.bunnybear.suanhu.bean;

import java.util.List;

public class SimpleTest {

    int order_use_sn;                     //唯一子订单号
    int user_id;                                    //用户ID
    int type;                                            //订单类型：4：简测
    int pay_stuats;                                  //支付状态：1：支付成功
    int question_id;                              //问题Id
    int for_type;                                     //问题对应类型ID
    String content;      //问题内容
    String name;                         //问答类型
    List<String> img;                                          //问题图片
    int answer;                                       //已有回答数量
    int new_answer;//1 有  0 没有

    public int getNew_answer() {
        return new_answer;
    }

    public void setNew_answer(int new_answer) {
        this.new_answer = new_answer;
    }

    public int getOrder_use_sn() {
        return order_use_sn;
    }

    public void setOrder_use_sn(int order_use_sn) {
        this.order_use_sn = order_use_sn;
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getFor_type() {
        return for_type;
    }

    public void setFor_type(int for_type) {
        this.for_type = for_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
