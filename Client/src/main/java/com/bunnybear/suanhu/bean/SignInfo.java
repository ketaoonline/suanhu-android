package com.bunnybear.suanhu.bean;

public class SignInfo {
    int suanfen;//总算分
    int sign_sum;//连续签到天数
    int today_get;//今日获得算分

    public int getSuanfen() {
        return suanfen;
    }

    public void setSuanfen(int suanfen) {
        this.suanfen = suanfen;
    }

    public int getSign_sum() {
        return sign_sum;
    }

    public void setSign_sum(int sign_sum) {
        this.sign_sum = sign_sum;
    }

    public int getToday_get() {
        return today_get;
    }

    public void setToday_get(int today_get) {
        this.today_get = today_get;
    }
}
