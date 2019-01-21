package com.bunnybear.suanhu.bean;

import java.io.Serializable;

public class Member implements Serializable{

    int id;//朋友id
    String f_name;//朋友姓名
    String sex;//性别
    long birth;//生日时间戳
    int lunar;//0：阳历 1：阴历
    String tag;
    String last_birth;
    int is_run;

    public int getIs_run() {
        return is_run;
    }

    public void setIs_run(int is_run) {
        this.is_run = is_run;
    }

    public String getLast_birth() {
        return last_birth;
    }

    public void setLast_birth(String last_birth) {
        this.last_birth = last_birth;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getLunar() {
        return lunar;
    }

    public void setLunar(int lunar) {
        this.lunar = lunar;
    }
}
