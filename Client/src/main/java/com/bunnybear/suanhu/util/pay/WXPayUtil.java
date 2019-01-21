package com.bunnybear.suanhu.util.pay;

import android.content.Context;

import com.bunnybear.suanhu.base.MyApplication;
import com.bunnybear.suanhu.util.ToastUtil;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaoxiong.library.utils.AppUtil;


/**
 * Created by xiaoxiong on 2016/12/8.
 */

public class WXPayUtil {

    private static final String TAG = WXPayUtil.class.getSimpleName();
    public static final String APP_ID = "wx501f993a177e9239";
    public static IWXAPI api;
    private static WXPayUtil instance;
    private Context context;

    private WXPayUtil(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
    }

    public static WXPayUtil newInstance(Context context) {
        if (instance == null) {
            instance = new WXPayUtil(context);
        }
        return instance;
    }

    public void pay(Context context, WXPayInfo wxPayInfo, String entrance) {
        if (AppUtil.isAppInstalled(context, "com.tencent.mm")) {
            if (check()) {
                if (null != wxPayInfo) {
                    PayReq req = new PayReq();  // 字段名	变量名	类型	必填	示例值	描述
                    req.appId = wxPayInfo.getAppid(); //json.getString("appid"); // 应用ID	appid	String(32)	是	wx8888888888888888	微信开放平台审核通过的应用APPID
                    req.partnerId = wxPayInfo.getPartnerid(); // 商户号	partnerid	String(32)	是	1900000109	微信支付分配的商户号
                    req.prepayId = wxPayInfo.getPrepayid(); // 预支付交易会话ID	 prepayid	String(32)	是	WX1217752501201407033233368018	微信返回的支付交易会话ID
                    req.nonceStr = wxPayInfo.getNoncestr(); // 随机字符串	noncestr	String(32)	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
                    req.timeStamp = wxPayInfo.getTimestamp() + ""; // 时间戳	timestamp	String(10)	是	1412000000	时间戳，请见接口规则-参数规定
                    req.packageValue = "Sign=WXPay"; // 扩展字段	package	String(128)	是	Sign=WXPay	暂填写固定值Sign=WXPay
                    req.sign = wxPayInfo.getSign(); // 签名	sign	String(32)	是	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
//                    req.signType = json.getString("MD5");
                    req.extData = entrance; // optional
//                    Toast.makeText(AppHelper.application(), "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    ToastUtil.show(context, "支付信息为空");
                }
            } else {
                ToastUtil.show(context, "请先打开微信后再支付");
            }
        } else {
            ToastUtil.show(context, "您还未安装微信");
        }

    }

    public void payTest(Context context) {
        if (AppUtil.isAppInstalled(context, "com.tencent.mm")) {
            if (check()) {
//                try {
//                    JSONObject json = new JSONObject(jsonStr);
//                    if(null != json){
                PayReq req = new PayReq();  // 字段名	变量名	类型	必填	示例值	描述
                req.appId = APP_ID; //json.getString("appid"); // 应用ID	appid	String(32)	是	wx8888888888888888	微信开放平台审核通过的应用APPID
                req.partnerId = "1504439731"; // 商户号	partnerid	String(32)	是	1900000109	微信支付分配的商户号
                req.prepayId = "wx1117465574837821891a6cac2970355133"; // 预支付交易会话ID	 prepayid	String(32)	是	WX1217752501201407033233368018	微信返回的支付交易会话ID
                req.nonceStr = "72a77ae0ab92478c865692f6eff40a89"; // 随机字符串	noncestr	String(32)	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
                req.timeStamp = "1528710415"; // 时间戳	timestamp	String(10)	是	1412000000	时间戳，请见接口规则-参数规定
                req.packageValue = "Sign=WXPay"; // 扩展字段	package	String(128)	是	Sign=WXPay	暂填写固定值Sign=WXPay
                req.sign = "34BF662628510B2259C0D19ADB700BF84E89188AF2678757496421A646524D7B"; // 签名	sign	String(32)	是	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
//                    req.signType = json.getString("MD5");
                req.extData = "before"; // optional
//                    Toast.makeText(AppHelper.application(), "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
//                    }else{
//                        Log.d("PAY_GET", "返回错误"+json.getString("message"));
//                        Toast.makeText(MyApplication.getInstance(), "返回错误"+json.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(MyApplication.getInstance(), "数据解析错误", Toast.LENGTH_SHORT).show();
//                }
            } else {
                ToastUtil.show(context, "请先打开微信后再支付");
            }
        } else {
            ToastUtil.show(context, "您还未安装微信");
        }

    }

    /**
     * 检测微信版本是否支持支付
     */
    public boolean check() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }


}
