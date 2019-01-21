package com.bunnybear.suanhu.bean;

import java.util.List;

public class Notices extends MainBaseBean{

    List<String> list;

    public Notices(int viewType, List<String> list) {
        super(viewType);
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
