package com.bunnybear.suanhu.api;

import com.bunnybear.suanhu.bean.ChatMsgsResponse;
import com.bunnybear.suanhu.bean.CalcOrder;
import com.bunnybear.suanhu.bean.ChatMsg;
import com.bunnybear.suanhu.bean.ClassOrder;
import com.bunnybear.suanhu.bean.Coupon;
import com.bunnybear.suanhu.bean.DetailedTest;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.Member;
import com.bunnybear.suanhu.bean.PayBean;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.bean.SimpleTest;
import com.bunnybear.suanhu.bean.SimpleTestDetail;
import com.bunnybear.suanhu.bean.User;
import com.bunnybear.suanhu.bean.VIP;
import com.bunnybear.suanhu.net.JsonResult;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface MineAPI {

    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("suanhu/userinfo/user_info")
    Observable<JsonResult<User>> getUserInfo();


    /**
     * 意见反馈
     *
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/help/help")
    Observable<JsonResult<String>> feedback(
            @FieldMap Map<String, String> map
    );

    /**
     * 我的八字库
     *
     * @return
     */
    @POST("suanhu/userinfo/get_friend")
    Observable<JsonResult<List<Member>>> getFamily();

    /**
     * 增加朋友或编辑朋友八字库
     *
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/add_friends")
    Observable<JsonResult<String>> editMember(
            @FieldMap Map<String, String> map
    );

    /**
     * 删除八字
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/delet_friend")
    Observable<JsonResult<String>> deleteMember(
            @Field("id") int id
    );

    /**
     * 收藏课程
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/collection")
    Observable<JsonResult<String>> collect(
            @FieldMap Map<String,String> map
    );

    /**
     * 课程收藏列表
     *
     * @return
     */
    @POST("suanhu/order/my_collection")
    Observable<JsonResult<List<SClass>>> myCollectClasses();




    /**
     * 取消收藏
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/un_collection")
    Observable<JsonResult<String>> cancelCollect(
            @FieldMap Map<String,String> map
    );

    /**
     * 修改用户资料
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/update_info")
    Observable<JsonResult<String>> updateInfo(
            @FieldMap Map<String, String> map
    );

    /**
     * 加入购物车
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/shop_car")
    Observable<JsonResult<String>> addShopCar(
            @Field("course_id") int id
    );

    /**
     * 删除购物车
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/un_shop_car")
    Observable<JsonResult<String>> deleteShopCar(
            @Field("course_id") int id
    );

    /**
     * 我的购物车
     *
     * @return
     */
    @POST("suanhu/order/my_shop_car")
    Observable<JsonResult<List<SClass>>> myShopCar();


    /**
     * 优惠券列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/use_coupon")
    Observable<JsonResult<List<Coupon>>> getCoupons(
            @FieldMap Map<String, String> map
    );

    /**
     * 可以兑换优惠券列表
     *
     * @return
     */
    @POST("suanhu/userinfo/coupon_info")
    Observable<JsonResult<List<Coupon>>> getExchangeCoupons();

    /**
     * 兑换优惠券
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/userinfo/coupon_change")
    Observable<JsonResult<String>> exchangeCoupon(
            @Field("id") int id
    );


    /**
     * vip类型列表
     *
     * @return
     */
    @POST("suanhu/userinfo/vip_info")
    Observable<JsonResult<List<VIP>>> vips();


    /**
     * 开通vip
     *
     * @param id
     * @param payType
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/pay_vip")
    Observable<JsonResult<PayBean>> openVip(
            @Field("id") int id,
            @Field("pay_type") int payType
    );

    /**
     * 下单买课程
     *
     * @param ids
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/order")
    Observable<JsonResult<String>> buy(
            @Field("course_id") String ids
    );

    /**
     * 订单信息
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/pay_up")
    Observable<JsonResult<List<SClass>>> orderInfo(
            @Field("order_id") String orderId
    );

    /**
     * 订单信息
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/master_up")
    Observable<JsonResult<List<Master>>> masterOrderInfo(
            @Field("order_id") String orderId
    );

    /**
     * 结算
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/pay_to")
    Observable<JsonResult<PayBean>> payMoney(
            @FieldMap Map<String, String> map
    );

    /**
     * 完成对话
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/end_chat")
    Observable<JsonResult<String>> finishChat(
            @Field("order_use_sn")int orderId
    );

    /**
     * 评论
     *
     * @param content
     * @param id
     * @param tableName
     * @param star
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/comment")
    Observable<JsonResult<String>> comment(
            @Field("content") String content,
            @Field("object_id") int id,
            @Field("table_name") String tableName,
            @Field("star") int star
    );

    /**
     * 大师收藏列表
     * @return
     */
    @POST("suanhu/userinfo/collection_master")
    Observable<JsonResult<List<Master>>> getCollectMasters();


    /**
     * 关注大师
     *
     * @param masterId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/follow")
    Observable<JsonResult<String>> follow(
            @Field("master_id") int masterId
    );

    /**
     * 取消关注大师
     *
     * @param masterId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/delete_follow")
    Observable<JsonResult<String>> deleteFollow(
            @Field("master_id") int masterId
    );

    /**
     * 简答列表
     */
    @POST("suanhu/question/my_short")
    Observable<JsonResult<List<SimpleTest>>> getSimpleTests();

    /**
     * 简答详情
     */
    @FormUrlEncoded
    @POST("suanhu/question/answer")
    Observable<JsonResult<SimpleTestDetail>> getSimpleTestDetail(
            @Field("question_id") int questionId
    );

    /**
     * 详测列表
     */
    @POST("suanhu/question/chat_list")
    Observable<JsonResult<List<DetailedTest>>> getDetailedTests();

    /**
     * 详测聊天记录
     * @param orderId
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/chat/get_history")
    Observable<JsonResult<ChatMsgsResponse>> getChatRecord(
            @Field("order_sn")int orderId,
            @Field("page")int page
    );

    /**
     * 接收消息
     * @param orderId
     * @param lastMsgId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/chat/get_msg")
    Observable<JsonResult<List<ChatMsg>>> getReceiveMsgs(
            @Field("order_sn")int orderId,
            @Field("last_msg")int lastMsgId
    );

    /**
     * 发送消息
     * @param orderId
     * @param masterId
     * @param type
     * @param content
     * @param lastMsgId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/chat/send_msg")
    Observable<JsonResult<List<ChatMsg>>> sendMsg(
            @Field("order_sn")int orderId,
            @Field("to_user")int masterId,
            @Field("type")int type,
            @Field("content")String content,
            @Field("last_msg")int lastMsgId
    );


    /**
     * 删除测算
     */
    @FormUrlEncoded
    @POST("suanhu/question/delete_chat")
    Observable<JsonResult<String>> deleteTest(
            @Field("order_use_sn") int orderUseSn
    );

    /**
     * 我的测算订单列表
     * @param orderType
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/my_order")
    Observable<JsonResult<List<CalcOrder>>> calcOrders(
            @Field("order_type")int orderType
    );

    /**
     * 申请退款
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/refund_order")
    Observable<JsonResult<String>> applyRefund(
            @FieldMap Map<String,String> map
    );

    /**
     * 申请退款原因
     * @return
     */
    @POST("suanhu/order/refund_reson")
    Observable<JsonResult<List<String>>> getReasons();

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/delet_order")
    Observable<JsonResult<String>> deleteOrder(
            @Field("order_id")int orderId
    );

    /**
     * 我的课程订单列表
     * @param orderType
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/order/my_course_order")
    Observable<JsonResult<List<ClassOrder>>> classOrders(
            @Field("order_type")int orderType
    );

    /**
     * 获取分享信息
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/index/share_out")
    Observable<JsonResult<ShareBean>> getShareInfo(
            @Field("type")int type
    );

}
