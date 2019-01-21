package com.bunnybear.suanhu.bean;

public class MasterTag {

    boolean isChecked;
    String content;

    public MasterTag(boolean isChecked, String content) {
        this.isChecked = isChecked;
        this.content = content;
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
