package com.bunnybear.suanhu.bean;

public class ImageBean extends MainBaseBean{

    String path;

    public ImageBean(int viewType, String path) {
        super(viewType);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
