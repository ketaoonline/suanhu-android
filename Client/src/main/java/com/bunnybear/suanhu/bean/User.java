package com.bunnybear.suanhu.bean;

public class User {

    int id;//用户id
    String user_nickname;//用户名称
    String avatar;//用户头像
    int suanfen;//用户算分
    int vip;//是否vip  0：否 1：是
    long birth;//生日时间戳
    int lunar;//0：阳历 1：阴历
    String last_birth;
    int is_run;

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

    public String getLast_birth() {
        return last_birth;
    }

    public void setLast_birth(String last_birth) {
        this.last_birth = last_birth;
    }

    public int getIs_run() {
        return is_run;
    }

    public void setIs_run(int is_run) {
        this.is_run = is_run;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSuanfen() {
        return suanfen;
    }

    public void setSuanfen(int suanfen) {
        this.suanfen = suanfen;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }
}
