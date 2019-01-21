package com.bunnybear.suanhu.bean;

/**
 * Created by zhaoya on 2018/2/5.
 */

public class Star {
    boolean isLight;

    public Star(boolean isLight) {
        this.isLight = isLight;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }
}
