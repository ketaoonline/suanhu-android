package com.bunnybear.suanhu.master.net;


import com.bunnybear.suanhu.master.base.ConstData;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.LogUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/6/29 0029.
 */
public class AppSubscriber<T> extends Subscriber<JsonResult<T>> {

    RequestCallBack<T> callBack;

    public AppSubscriber(RequestCallBack<T> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onNext(JsonResult<T> response) {
        try {
            if (response == null) {
                callBack.fail(-1, "网络请求失败");
            } else {
                if (response.code == 10001) {//token失效
                    BusFactory.getBus().post(new IEvent(ConstData.TOKEN_INVALID, null));
                    return;
                }
                if (response.isOk()) {
                    callBack.success(response.data);
                } else {
                    callBack.fail(response.code, response.msg);
                }
            }
        } catch (Exception e) {
            callBack.fail(-1, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.i("Throwable=" + e.toString());
        String errorMessage = "请求失败:" + e.toString();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ResponseBody body = httpException.response().errorBody();
            try {
                JsonResult result = JsonUtil.fromJson(body.string(), JsonResult.class);
                if (result != null) {
                    errorMessage = result.msg;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else if (e instanceof ConnectException) {
            errorMessage = "当前网络不可用，请检查网络";
        } else if (e instanceof UnknownHostException) {
            errorMessage = "当前网络不可用，请检查网络";
        } else if (e instanceof ConnectTimeoutException) {
            errorMessage = "网络连接超时";
        } else {
            e.printStackTrace();
        }
        callBack.fail(-1, errorMessage);
    }

}
