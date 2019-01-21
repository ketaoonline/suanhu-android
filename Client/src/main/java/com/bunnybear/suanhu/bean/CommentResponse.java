package com.bunnybear.suanhu.bean;

import java.util.List;

public class CommentResponse extends MainBaseBean{

    List<Comment> comment;
    double star_average;
    int comment_sum;
    StarPeople star_people;
    int total_page;

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public double getStar_average() {
        return star_average;
    }

    public void setStar_average(double star_average) {
        this.star_average = star_average;
    }

    public int getComment_sum() {
        return comment_sum;
    }

    public void setComment_sum(int comment_sum) {
        this.comment_sum = comment_sum;
    }

    public StarPeople getStar_people() {
        return star_people;
    }

    public void setStar_people(StarPeople star_people) {
        this.star_people = star_people;
    }
}
