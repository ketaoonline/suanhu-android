package com.bunnybear.suanhu.bean;

public class SClass {

    int id;//课程id
    String coursename;//课程名称
    double price;//课程价格
    double stars;//星级评价
    String head_banner;//宣传照
    double old_price;//原价
    int total_people;//多少人学过
    String author;//作者
    boolean isChecked;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public int getTotal_people() {
        return total_people;
    }

    public void setTotal_people(int total_people) {
        this.total_people = total_people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getHead_banner() {
        return head_banner;
    }

    public void setHead_banner(String head_banner) {
        this.head_banner = head_banner;
    }
}
