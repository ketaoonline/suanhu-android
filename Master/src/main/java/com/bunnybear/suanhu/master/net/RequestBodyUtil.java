package com.bunnybear.suanhu.master.net;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class RequestBodyUtil {
    public static final String TEXT = "text/plain";
    public static final String JSON = "application/json;charset=utf-8";


    private static RequestBody creatTextRequestBody(String content){
        return RequestBody.create(MediaType.parse(TEXT),content);
    }

    private static RequestBody creatJsonRequestBody(Map<String,String> map){
        return RequestBody.create(MediaType.parse(JSON), JsonUtil.toJson(map));
    }

    private static RequestBody creatJsonRequestBody2(Map<String,Object> map){
        return RequestBody.create(MediaType.parse(JSON), JsonUtil.toJson(map));
    }


    public static Map<String,RequestBody> constructParam(Map<String,Object> data){
        Map<String,RequestBody> map = new HashMap<>();
        if (data == null){
            data = new HashMap<>();
        }
        map.put("data",creatJsonRequestBody2(data));
        return map;
    }

}
