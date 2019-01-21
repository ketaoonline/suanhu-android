package com.bunnybear.suanhu.bean;

import java.util.List;

public class TestBigTypeResponse extends MainBaseBean{

    List<TestBigType> list;

    public TestBigTypeResponse(int viewType, List<TestBigType> list) {
        super(viewType);
        this.list = list;
    }

    public List<TestBigType> getList() {
        return list;
    }

    public void setList(List<TestBigType> list) {
        this.list = list;
    }
}
