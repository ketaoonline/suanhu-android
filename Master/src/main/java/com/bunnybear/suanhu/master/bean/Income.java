package com.bunnybear.suanhu.master.bean;

public class Income extends MainBaseBean {

    double money;//订单价格
    long creattime;//时间
    int type;//1：详测  5：简测
    int order_id;//订单编号

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getCreattime() {
        return creattime;
    }

    public void setCreattime(long creattime) {
        this.creattime = creattime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
