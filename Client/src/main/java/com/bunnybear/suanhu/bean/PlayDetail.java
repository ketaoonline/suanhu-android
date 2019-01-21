package com.bunnybear.suanhu.bean;

import java.util.List;

public class PlayDetail extends MainBaseBean{
    String url;
    String text_url;
    String name;
    String introduce;
    int type;
    int count;
    String head_banner;
    List<Lesson> catalog_info;
    int collection;

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getHead_banner() {
        return head_banner;
    }

    public void setHead_banner(String head_banner) {
        this.head_banner = head_banner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText_url() {
        return text_url;
    }

    public void setText_url(String text_url) {
        this.text_url = text_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Lesson> getCatalog_info() {
        return catalog_info;
    }

    public void setCatalog_info(List<Lesson> catalog_info) {
        this.catalog_info = catalog_info;
    }
}
