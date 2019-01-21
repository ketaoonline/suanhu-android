package com.bunnybear.suanhu.bean;

import java.util.List;

public class MasterInfo extends MainBaseBean{

    int id;//大师id
    String master_name;//大师姓名
    List<String> belong;//大师标签
    int experience;//工作年限
    List<String> make_well;//擅长领域
    int order_num;//完成订单数
    int mark_num;//评价数
    List<String> tag_word;
    String imag;//大师宣传照
    List<Experience> experience_info;//工作经历
    int follow;
    double price;
    double x_price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getX_price() {
        return x_price;
    }

    public void setX_price(double x_price) {
        this.x_price = x_price;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public List<Experience> getExperience_info() {
        return experience_info;
    }

    public void setExperience_info(List<Experience> experience_info) {
        this.experience_info = experience_info;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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

    public List<String> getMake_well() {
        return make_well;
    }

    public void setMake_well(List<String> make_well) {
        this.make_well = make_well;
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

    public List<String> getTag_word() {
        return tag_word;
    }

    public void setTag_word(List<String> tag_word) {
        this.tag_word = tag_word;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }
}
