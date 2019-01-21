package com.bunnybear.suanhu.bean;

import java.util.List;

public class Answer extends MainBaseBean{

    int answer_id;                                //回答ID
    int question_id;                           //问题ID
    String content;            //回复内容
    int master_id;                              //大师ID
    long create_time;                //回复时间
    String avatar;                    //大师头像
    String master_name;              //大师名字
    String introduce;       //大师简介
    List<String> answer_image;                       //回复图片
    int follow;                                       //是否关注：0未关注1已关注

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaster_id() {
        return master_id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getAnswer_image() {
        return answer_image;
    }

    public void setAnswer_image(List<String> answer_image) {
        this.answer_image = answer_image;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }
}
