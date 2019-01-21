package com.bunnybear.suanhu.bean;

import java.util.List;

public class FortuneDetail {

    String caifu;//财富断语
    String ganqing;//感情断语
    String renji;//人际关系断语
    String shensha;//今日神煞断语
    String yanse;//开运颜色
    int shuzi;//开运数字
    String fangxiang;//开运方向
    int mingge;//所属命格 0:木 1：水 2：火 3：金 4：土
    String zouyu;//转运咒语
    List<String> chengyu;
    int yunfen;//运势分数
    String yi;//今日宜做
    String ji;//今日忌
    String yangli;//阳历
    String yinli;//阴历
    String rizhu;

    public String getRizhu() {
        return rizhu;
    }

    public void setRizhu(String rizhu) {
        this.rizhu = rizhu;
    }

    public String getCaifu() {
        return caifu;
    }

    public void setCaifu(String caifu) {
        this.caifu = caifu;
    }

    public String getGanqing() {
        return ganqing;
    }

    public void setGanqing(String ganqing) {
        this.ganqing = ganqing;
    }

    public String getRenji() {
        return renji;
    }

    public void setRenji(String renji) {
        this.renji = renji;
    }

    public String getShensha() {
        return shensha;
    }

    public void setShensha(String shensha) {
        this.shensha = shensha;
    }

    public String getYanse() {
        return yanse;
    }

    public void setYanse(String yanse) {
        this.yanse = yanse;
    }

    public int getShuzi() {
        return shuzi;
    }

    public void setShuzi(int shuzi) {
        this.shuzi = shuzi;
    }

    public String getFangxiang() {
        return fangxiang;
    }

    public void setFangxiang(String fangxiang) {
        this.fangxiang = fangxiang;
    }

    public int getMingge() {
        return mingge;
    }

    public void setMingge(int mingge) {
        this.mingge = mingge;
    }

    public String getZouyu() {
        return zouyu;
    }

    public void setZouyu(String zouyu) {
        this.zouyu = zouyu;
    }

    public List<String> getChengyu() {
        return chengyu;
    }

    public void setChengyu(List<String> chengyu) {
        this.chengyu = chengyu;
    }

    public int getYunfen() {
        return yunfen;
    }

    public void setYunfen(int yunfen) {
        this.yunfen = yunfen;
    }

    public String getYi() {
        return yi;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getYangli() {
        return yangli;
    }

    public void setYangli(String yangli) {
        this.yangli = yangli;
    }

    public String getYinli() {
        return yinli;
    }

    public void setYinli(String yinli) {
        this.yinli = yinli;
    }
}
