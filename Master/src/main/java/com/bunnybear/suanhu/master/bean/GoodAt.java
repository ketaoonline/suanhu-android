package com.bunnybear.suanhu.master.bean;

public class GoodAt {
    String content;
    boolean isChecked;

    public GoodAt(String content, boolean isChecked) {
        this.content = content;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
