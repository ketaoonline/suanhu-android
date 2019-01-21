package com.bunnybear.suanhu.util.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.bunnybear.suanhu.BuildConfig;
import com.bunnybear.suanhu.util.ToastUtil;
import com.xiaoxiong.library.utils.AppUtil;

import java.util.IllegalFormatCodePointException;
import java.util.Map;

/**
 * Created by xiaoxiong on 2016/12/8.
 */

public class AliPayUtil {
    private Activity activity;
    private Handler mhandler;
    public static final int SDK_PAY_FLAG = 1;

    public AliPayUtil(Activity activity, Handler mhandler, String message){
        this.activity = activity;
        this.mhandler = mhandler;
//        if (BuildConfig.DEBUG){
//            if (AppUtil.isAppInstalled(activity,"com.eg.android.AlipayGphoneRC")){
//                pay(message);
//            }else {
//                ToastUtil.show(activity,"您还未安装支付宝");
//            }
//        }else {
            if (AppUtil.isAppInstalled(activity,"com.eg.android.AlipayGphone")){
                pay(message);
            }else {
                ToastUtil.show(activity,"您还未安装支付宝");
            }
//        }

    }


    private void pay(final String message){
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                Map<String,String> result = payTask.payV2(message,true);
                Message message = new Message();
                message.what = SDK_PAY_FLAG;
                message.obj = result;
                mhandler.sendMessage(message);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
