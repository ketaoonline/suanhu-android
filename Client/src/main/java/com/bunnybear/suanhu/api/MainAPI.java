package com.bunnybear.suanhu.api;

import com.bunnybear.suanhu.bean.Article;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.bean.FortuneDetail;
import com.bunnybear.suanhu.bean.FriendFortune;
import com.bunnybear.suanhu.bean.SignInfo;
import com.bunnybear.suanhu.bean.TopData;
import com.bunnybear.suanhu.net.JsonResult;

import java.security.SignedObject;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MainAPI {

    /**
     * 获取首页顶部数据
     *
     * @return
     */
    @POST("suanhu/fortune/today_totle")
    Observable<JsonResult<TopData>> getTopData();


    /**
     * 获取首页热门文章
     *
     * @return
     */
    @POST("suanhu/index/index_data")
    Observable<JsonResult<List<Article>>> getArticles();

    /**
     * 滚动文字
     * @return
     */
    @POST("suanhu/fortune/couts")
    Observable<JsonResult<List<String>>> getNotices();

    /**
     * 分类获取文章列表
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/news_list")
    Observable<JsonResult<ArticleResponse>> getArticlesOfType(
            @Field("type") int type,
            @Field("page") int page
    );

    /**
     * 获取签到信息
     * @return
     */
    @POST("suanhu/index/sign")
    Observable<JsonResult<SignInfo>> getSignInfo();

    /**
     * 获取运势详情
     * @return
     */
    @POST("suanhu/fortune/today_like")
    Observable<JsonResult<FortuneDetail>> getFortuneDetail();

    /**
     * 获取朋友运势详情
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/fortune/today_friend")
    Observable<JsonResult<FriendFortune>> getFriendFortune(
            @Field("id")int id
    );


}
