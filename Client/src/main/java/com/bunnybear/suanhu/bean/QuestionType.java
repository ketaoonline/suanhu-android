package com.bunnybear.suanhu.bean;

public class QuestionType {
    int question_type_id;                //分类ID
    String name;                    //名称
    String image;        //图标
    double price;
    int master_price_id;
    int master_id;

    public int getMaster_price_id() {
        return master_price_id;
    }

    public void setMaster_price_id(int master_price_id) {
        this.master_price_id = master_price_id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuestion_type_id() {
        return question_type_id;
    }

    public void setQuestion_type_id(int question_type_id) {
        this.question_type_id = question_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
