package com.bunnybear.suanhu.master.api;


import com.bunnybear.suanhu.master.bean.ChatMsg;
import com.bunnybear.suanhu.master.bean.ChatMsgsResponse;
import com.bunnybear.suanhu.master.bean.DetailedTest;
import com.bunnybear.suanhu.master.bean.IncomeResponse;
import com.bunnybear.suanhu.master.bean.Order;
import com.bunnybear.suanhu.master.bean.SimpleTest;
import com.bunnybear.suanhu.master.bean.SimpleTestDetail;
import com.bunnybear.suanhu.master.bean.User;
import com.bunnybear.suanhu.master.bean.CustomerInfo;
import com.bunnybear.suanhu.master.net.JsonResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MineAPI {

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("suanhu/master/index")
    Observable<JsonResult<User>> getUserInfo();

    /**
     * 修改用户资料
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/master/edit_master")
    Observable<JsonResult<String>> updateInfo(
            @FieldMap Map<String, String> map
    );

    /**
     * 设置接单数量
     * @param count
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/master/set_order_num")
    Observable<JsonResult<String>> setOrderCount(
            @Field("number")int count
    );


    /**
     * 简答列表（未处理）
     */
    @POST("suanhu/question/master_get_short")
    Observable<JsonResult<List<SimpleTest>>> getSimpleTests();

    /**
     * 简答列表（已处理）
     */
    @POST("suanhu/question/master_end_short")
    Observable<JsonResult<List<SimpleTest>>> getEndSimpleTests();

    /**
     * 简测详情
     */
    @FormUrlEncoded
    @POST("suanhu/question/master_short_info")
    Observable<JsonResult<SimpleTestDetail>> getSimpleTestDetail(
            @Field("order_use_sn") int orderId
    );

    /**
     * 回答简测
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/question/push_answer")
    Observable<JsonResult<String>> answerSimpleTest(
            @FieldMap Map<String,String> map
    );

    /**
     * 详测列表
     */
    @POST("suanhu/question/master_get_chat")
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
            @Field("order_sn") int orderId,
            @Field("page") int page
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
            @Field("order_sn") int orderId,
            @Field("last_msg") int lastMsgId
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
            @Field("order_sn") int orderId,
            @Field("to_user") int masterId,
            @Field("type") int type,
            @Field("content") String content,
            @Field("last_msg") int lastMsgId
    );


    /**
     * 删除测算
     */
    @FormUrlEncoded
    @POST("suanhu/question/master_delete_chat")
    Observable<JsonResult<String>> deleteTest(
            @Field("order_use_sn") int orderUseSn
    );


    /**
     * 申请大师获取擅长分类
     * @return
     */
    @POST("suanhu/master/get_master_type")
    Observable<JsonResult<List<String>>> getGoodAts();

    /**
     * 提交申请大师
     * @param realName
     * @param idCard
     * @param isFullTime
     * @param goodAt
     * @param images
     * @param introduce
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/master/be_master")
    Observable<JsonResult<String>> submitApply(
            @Field("true_name")String realName,
            @Field("id_card")String idCard,
            @Field("quanzhi")int isFullTime,
            @Field("type")String goodAt,
            @Field("image")String images,
            @Field("introduce")String introduce
    );

    /**
     * 详测订单
     * @return
     */
    @POST("suanhu/question/master_chat_order")
    Observable<JsonResult<List<Order>>> getDetailTestOrders();

    /**
     * 简测订单
     * @return
     */
    @POST("suanhu/question/master_order")
    Observable<JsonResult<List<Order>>> getSimpleTestOrders();

    /**
     * 获取客户信息
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/chat/get_chat_head")
    Observable<JsonResult<CustomerInfo>> getCustomerInfo(
            @Field("order_sn")int orderSn
    );

    /**
     * 抢单
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("")
    Observable<JsonResult<String>> responderOrder(
            @Field("order_id")int orderId
    );

    /**
     * 获取我的收入
     * @return
     */
    @POST("suanhu/master/my_income")
    Observable<JsonResult<IncomeResponse>> getIncomeData();


    /**
     * 申请提现
     * @param money
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/master/cash_money")
    Observable<JsonResult<String>> applyEncash(
            @Field("money")String money,
            @Field("code")String code
    );

    /**
     * 大师提现验证码
     * @return
     */
    @POST("suanhu/master/bank_code")
    Observable<JsonResult<String>> getCode();

    /**
     * 绑定银行卡
     * @param bankCardNum
     * @param address
     * @param people
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/master/bank_up")
    Observable<JsonResult<String>> bindBankCard(
            @Field("number")String bankCardNum,
            @Field("bank_name")String address,
            @Field("money_name")String people
    );
}
