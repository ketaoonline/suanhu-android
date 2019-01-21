package com.bunnybear.suanhu.master.bean;

import java.util.List;

public class ChatMsgsResponse {
    int total_page;
    List<ChatMsg> list;
    int now_page;

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<ChatMsg> getList() {
        return list;
    }

    public void setList(List<ChatMsg> list) {
        this.list = list;
    }

    public int getNow_page() {
        return now_page;
    }

    public void setNow_page(int now_page) {
        this.now_page = now_page;
    }
}
