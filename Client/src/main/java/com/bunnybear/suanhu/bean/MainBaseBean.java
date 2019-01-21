package com.bunnybear.suanhu.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by zhaoya on 2018/1/23.
 */

public class MainBaseBean implements MultiItemEntity,Serializable {
    int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public MainBaseBean(){

    }

    public MainBaseBean(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemType() {
        return viewType;
    }
}
