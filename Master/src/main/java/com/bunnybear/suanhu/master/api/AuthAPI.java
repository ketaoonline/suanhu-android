package com.bunnybear.suanhu.master.api;

import com.bunnybear.suanhu.master.net.JsonResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface AuthAPI {

    /**
     * 登陆
     *
     * @param mobile 账号
     * @param pwd    密码
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/login/login_verify")
    Observable<JsonResult<String>> login(@Field("mobile") String mobile,
                                         @Field("user_pass") String pwd);


    /**
     * 注册
     *
     * @param mobile
     * @param pwd
     * @param dateType
     * @param date
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/regismater/enroll")
    Observable<JsonResult<String>> register(@Field("mobile") String mobile,
                                            @Field("code") String code,
                                            @Field("user_pass") String pwd);

    /**
     * 找回密码
     * @param mobile
     * @param pwd
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/login/reset_password")
    Observable<JsonResult<String>> findBackPwd(
            @Field("mobile") String mobile,
            @Field("user_pass") String pwd,
            @Field("code") String code
    );


    /**
     * 发送验证码
     *
     * @param mobile 账号
     * @return
     */
    @FormUrlEncoded
    @POST("suanhu/regismater/reser_code")
    Observable<JsonResult<String>> sendMsg(@Field("mobile") String mobile);


}
