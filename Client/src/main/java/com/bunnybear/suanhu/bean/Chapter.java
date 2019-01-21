package com.bunnybear.suanhu.bean;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Chapter extends ExpandableGroup<Lesson> {

    String chaption;
    List<Lesson> info;

    public Chapter(String title, List<Lesson> items) {
        super(title, items);
    }

    public String getChaption() {
        return chaption;
    }

    public void setChaption(String chaption) {
        this.chaption = chaption;
    }

    public List<Lesson> getInfo() {
        return info;
    }

    public void setInfo(List<Lesson> info) {
        this.info = info;
    }
}
