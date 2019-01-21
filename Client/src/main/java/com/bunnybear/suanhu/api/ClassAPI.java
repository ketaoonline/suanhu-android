package com.bunnybear.suanhu.api;

import com.bunnybear.suanhu.bean.Chapter;
import com.bunnybear.suanhu.bean.ClassBanner;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.bean.PlayDetail;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.StudyProgress;
import com.bunnybear.suanhu.net.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ClassAPI {


    /**
     * 课堂首页课堂列表
     * @param label
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/classroom")
    Observable<JsonResult<List<SClass>>> getMainClasses(
            @Field("label")String label
    );

    /**
     * 课程详情介绍
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/course_info")
    Observable<JsonResult<ClassDetailIntroduce>> getClassDetailIntroduce(
            @Field("course_id")int id
    );


    /**
     * 课程详情目录
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/catalog_info")
    Observable<JsonResult<List<Chapter>>> getClassDetailCatalogue(
            @Field("course_id")int id
    );

    /**
     * 播放页信息
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/study_up")
    Observable<JsonResult<PlayDetail>> getPlayDetail(
            @FieldMap Map<String,String> map
    );

    /**
     * 课程banner
     * @return
     */
    @POST("suanhu/userinfo/banner_up")
    Observable<JsonResult<List<ClassBanner>>> getClassBanner();


    /**
     * 学习进度
     * @return
     */
    @POST("suanhu/userinfo/my_study_recall")
    Observable<JsonResult<List<StudyProgress>>> getStudyProgress();

    /**
     * 上传学习进度
     * @param courseId
     * @param lessonId
     * @param position
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/my_study")
    Observable<JsonResult<String>> submitStudyProgress(
//            @Field("course_id")int courseId,
//            @Field("course_info_id")int lessonId,
//            @Field("couese_much")int position
            @FieldMap Map<String,String> map
            );

    /**
     *  课程分类
     * @return
     */
    @POST("suanhu/userinfo/tags")
    Observable<JsonResult<List<ClassType>>> getClassTypes();

    /**
     * 分类课程列表
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/tag_calss")
    Observable<JsonResult<List<SClass>>> getTagClasses(
            @Field("tag_id")int type
    );

}
