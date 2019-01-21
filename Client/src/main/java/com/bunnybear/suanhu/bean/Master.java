package com.bunnybear.suanhu.bean;

import java.util.List;

public class Master extends MainBaseBean {
    int id;//大师ID
    String master_name;//大师姓名
    List<String> belong;//大师标签
    double stars;//星级评价
    int online;//是否在线  0在线  1忙碌  2 下线
    String imag;//大师宣传照
    List<String> make_well;//大师擅长领域
    double price;//咨询价格

    String introduce;//大师介绍
    int order_num;//订单数：
    int mark_num;//评价数
    int callback_num;//回答数：

    boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getMark_num() {
        return mark_num;
    }

    public void setMark_num(int mark_num) {
        this.mark_num = mark_num;
    }

    public int getCallback_num() {
        return callback_num;
    }

    public void setCallback_num(int callback_num) {
        this.callback_num = callback_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public List<String> getBelong() {
        return belong;
    }

    public void setBelong(List<String> belong) {
        this.belong = belong;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }

    public List<String> getMake_well() {
        return make_well;
    }

    public void setMake_well(List<String> make_well) {
        this.make_well = make_well;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
