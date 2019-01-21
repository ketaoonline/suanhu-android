package com.bunnybear.suanhu.master.bean;

public class Order {

    int order_use_sn;//唯一订单号
    int user_id;//用户ID
    int type;//。。
    int pay_stuats; //支付状态
    long start_time;//开始时间
    String user_name;//用户名
    String avatar;//用户头像
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
