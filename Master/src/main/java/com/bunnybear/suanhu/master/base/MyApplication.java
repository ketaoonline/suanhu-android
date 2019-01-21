package com.bunnybear.suanhu.master.base;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication{

    private static MyApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        InitializeService.start(this);

    }

    public static MyApplication getInstance() {
        return mApplication;
    }
}
