package com.bunnybear.suanhu.bean;

public class StudyProgress {

    int course_id;//课程id
    int course_info_id;//小节id
    int much;//完成多少 成百分之10
    String head_banner;
    String course_name;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCourse_info_id() {
        return course_info_id;
    }

    public void setCourse_info_id(int course_info_id) {
        this.course_info_id = course_info_id;
    }

    public int getMuch() {
        return much;
    }

    public void setMuch(int much) {
        this.much = much;
    }

    public String getHead_banner() {
        return head_banner;
    }

    public void setHead_banner(String head_banner) {
        this.head_banner = head_banner;
    }
}
