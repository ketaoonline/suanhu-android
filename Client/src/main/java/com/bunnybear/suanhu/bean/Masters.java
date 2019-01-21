package com.bunnybear.suanhu.bean;

import java.util.List;

public class Masters extends MainBaseBean{
    List<Master> list;

    public Masters(int viewType, List<Master> list) {
        super(viewType);
        this.list = list;
    }

    public List<Master> getList() {
        return list;
    }

    public void setList(List<Master> list) {
        this.list = list;
    }

}
