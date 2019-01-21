package com.bunnybear.suanhu.api;

import com.bunnybear.suanhu.bean.CommentResponse;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.MasterInfo;
import com.bunnybear.suanhu.bean.MasterResponse;
import com.bunnybear.suanhu.bean.Question;
import com.bunnybear.suanhu.bean.QuestionType;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.TestBigType;
import com.bunnybear.suanhu.net.JsonResult;
import com.bunnybear.suanhu.ui.activity.MainActivity;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface CalcAPI {

    /**
     * 获取测算大类
     *
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/get_four_type")
    Observable<JsonResult<List<TestBigType>>> getTestBigTypes(
            @Field("type") int type
    );

    /**
     * 获取推荐大师
     * map
     *
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/master_list")
    Observable<JsonResult<List<Master>>> getRecommendMaster(
            @Field("type") int type
    );

    /**
     * 获取推荐大师
     *
     * @return
     */
    @POST("suanhu/userinfo/master_list")
    Observable<JsonResult<List<Master>>> getRecommendMaster();

    /**
     * 新进大师和热门大师列表
     *
     * @return
     */
    @POST("suanhu/userinfo/new_master")
    Observable<JsonResult<MasterResponse>> getHMasters();

    /**
     * 大师详细信息
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/master_info")
    Observable<JsonResult<MasterInfo>> getMasterInfo(
            @Field("master_id") int id
    );


    /**
     * 获取问答类型
     *
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/get_master_four")
    Observable<JsonResult<List<QuestionType>>> getQuestionTypes(
            @FieldMap Map<String, String> map
    );


    /**
     * 获取问题列表
     *
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/get_question")
    Observable<JsonResult<List<Question>>> getQuestions(
            @Field("type") int type
    );

    /**
     * 换一批问题
     *
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/get_other_question")
    Observable<JsonResult<List<Question>>> getOtherQuestions(
            @Field("type") int type
    );

    /**
     * 获取评论列表
     *
     * @param id
     * @param tableName
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/get_comment")
    Observable<JsonResult<CommentResponse>> getComments(
            @Field("object_id") int id,
            @Field("table_name") String tableName,
            @Field("page") int page
    );

    /**
     * 推荐大师列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/put_master")
    Observable<JsonResult<List<Master>>> getRecommendMasters(
            @FieldMap Map<String, String> map
    );

    /**
     * 提问下单
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/master_go")
    Observable<JsonResult<String>> putQuestionOrder(
            @FieldMap Map<String, String> map
    );


    /**
     * 简测提问
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/short_question")
    Observable<JsonResult<String>> sampleTestPutQuestion(
            @FieldMap Map<String, String> map
    );

    /**
     * 详测下单
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/chat_order")
    Observable<JsonResult<String>> putDetailedTestOrder(
            @Field("master_id") int id,
            @Field("type") int type
    );

    /**
     * 搜索大师
     *
     * @param masterName
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/search_master")
    Observable<JsonResult<List<Master>>> searchMasters(
            @Field("master_name") String masterName
    );

    /**
     * 搜索课程
     *
     * @param masterName
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/search_class")
    Observable<JsonResult<List<SClass>>> searchClasses(
            @Field("course_name") String masterName
    );

    /**
     * 分类课程列表
     *
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/search_class")
    Observable<JsonResult<List<SClass>>> getClasses(
            @Field("type") int type
    );

}
