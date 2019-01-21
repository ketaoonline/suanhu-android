package com.xiaoxiong.library.http;

/**
 * 类描述:网络请求回调接口
 * 作者:xues
 * 时间:2017年04月15日
 */

public interface RequestCallBack<T> {
    void success(T t);
    void fail(int errCode, String errStr);
}
