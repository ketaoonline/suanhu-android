package com.bunnybear.suanhu.bean;

public class Comment extends MainBaseBean {

    int id;//评论ID
    int user_id;//用户ID
    int object_id;//对象ID
    int floor;//初始楼层
    long create_time;//添加时间
    String table_name;//对象分类
    String full_name;//用户登录名
    String content;//评论内容
    double star;//星级
    String user_nickname;//用户昵称
    String avatar;//用户头像
    String re_data;//回复内容，没有时返回空字符串

    public Comment(int viewType) {
        super(viewType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRe_data() {
        return re_data;
    }

    public void setRe_data(String re_data) {
        this.re_data = re_data;
    }
}
