package com.bunnybear.suanhu.master.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.net.HttpConstData;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import cn.jpush.android.api.JPushInterface;


public class InitializeService extends IntentService {

    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.guesslive.caixiangji.service.action.app.create";
    public static final String EXTRA_PARAM = "com.guesslive.caixiangji.service.extra.PARAM";
    protected static Context mContext;

    public InitializeService() {
        super("InitializeService");
    }

    /**
     * 启动调用
     *
     * @param context
     */
    public static void start(Context context) {
        mContext = context;
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        performInit(EXTRA_PARAM);
    }

    /**
     * 启动初始化操作
     */
    private void performInit(String param) {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);//极光推送

        Hawk.init(mContext).setEncryption(new NoEncryption()).build(); //初始化Hawk
        Http.initHttp(mContext, HttpConstData.SERVER.BaseURL); //接口请求初始化
        GlideUtil.init(mContext); // 图片加载初始化
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); // android 7.0系统解决拍照的问题
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();



    }
}
