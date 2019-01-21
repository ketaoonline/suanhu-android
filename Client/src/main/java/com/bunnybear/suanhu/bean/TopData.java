package com.bunnybear.suanhu.bean;

public class TopData extends MainBaseBean{
    int yunfen;
    String duanyu;
    String zouyu;

    public String getZouyu() {
        return zouyu;
    }

    public void setZouyu(String zouyu) {
        this.zouyu = zouyu;
    }

    public int getYunfen() {
        return yunfen;
    }

    public void setYunfen(int yunfen) {
        this.yunfen = yunfen;
    }

    public String getDuanyu() {
        return duanyu;
    }

    public void setDuanyu(String duanyu) {
        this.duanyu = duanyu;
    }
}
