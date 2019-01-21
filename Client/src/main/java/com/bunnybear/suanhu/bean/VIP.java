package com.bunnybear.suanhu.bean;

public class VIP {

    int id;
    String name;
    String introduce;
    double price;
    String best_order;

    public String getBest_order() {
        return best_order;
    }

    public void setBest_order(String best_order) {
        this.best_order = best_order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
