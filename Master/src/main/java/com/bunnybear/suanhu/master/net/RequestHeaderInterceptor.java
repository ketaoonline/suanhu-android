package com.bunnybear.suanhu.master.net;


import com.bunnybear.suanhu.master.base.ConstData;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

public class RequestHeaderInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl originalHttpUrl = request.url();
        HttpUrl.Builder htbuilder = originalHttpUrl.newBuilder();
        HttpUrl url = htbuilder.build();

        Request.Builder builder = request.newBuilder().url(url);
        if (Hawk.contains(ConstData.TOKEN)) {
            String token = Hawk.get(ConstData.TOKEN);
            builder.addHeader("XX-Token", token);
        }
        builder.addHeader("XX-Device-Type","android");
        return chain.proceed(builder.build());
    }
}