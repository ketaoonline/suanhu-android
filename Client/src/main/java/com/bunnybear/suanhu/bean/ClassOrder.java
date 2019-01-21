package com.bunnybear.suanhu.bean;

import java.util.List;

public class ClassOrder {

    double money;//付款价格
    int order_id;//这是订单id  付款的时候传这个
    String coursename;//课程姓名
    int order_use_sn;//这是订单编号
    double price;//红色价格
    double old_price;//灰色价格
    String head_banner;//
    long time;//时间
    int is_pay;//是否支付状态
    List<SClass> info;

    public List<SClass> getInfo() {
        return info;
    }

    public void setInfo(List<SClass> info) {
        this.info = info;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public int getOrder_use_sn() {
        return order_use_sn;
    }

    public void setOrder_use_sn(int order_use_sn) {
        this.order_use_sn = order_use_sn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public String getHead_banner() {
        return head_banner;
    }

    public void setHead_banner(String head_banner) {
        this.head_banner = head_banner;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }
}
