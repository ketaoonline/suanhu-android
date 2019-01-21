package com.bunnybear.suanhu.bean;

import java.util.List;

public class MasterResponse {

    List<Master> new_master;
    List<Master> hot_master;

    public List<Master> getNew_master() {
        return new_master;
    }

    public void setNew_master(List<Master> new_master) {
        this.new_master = new_master;
    }

    public List<Master> getHot_master() {
        return hot_master;
    }

    public void setHot_master(List<Master> hot_master) {
        this.hot_master = hot_master;
    }
}
