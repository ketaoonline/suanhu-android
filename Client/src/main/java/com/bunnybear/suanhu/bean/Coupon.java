package com.bunnybear.suanhu.bean;

import java.io.Serializable;

public class Coupon implements Serializable{

    int id;//优惠券     id
    int coupon_type;//使用范围 0：课程 1：大师咨询 2：商城 3会员 4 全场通用
    double coupon_money;//优惠金额
    double coupon_price;//优惠门槛
    long begin_time;//起始时间
    long end_time;//终止时间
    int coupon_suanfen;//需要算分

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public double getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(double coupon_money) {
        this.coupon_money = coupon_money;
    }

    public double getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(double coupon_price) {
        this.coupon_price = coupon_price;
    }

    public long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(long begin_time) {
        this.begin_time = begin_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getCoupon_suanfen() {
        return coupon_suanfen;
    }

    public void setCoupon_suanfen(int coupon_suanfen) {
        this.coupon_suanfen = coupon_suanfen;
    }
}
