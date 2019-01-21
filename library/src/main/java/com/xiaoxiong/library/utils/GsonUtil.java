package com.xiaoxiong.library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/10.
 * 封装的GSON解析工具类，提供泛型参数
 */
public class GsonUtil {

    private static Gson gson;
    public static final GsonBuilder gsonBuilder = new GsonBuilder();

    public static Gson gson() {
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

    /** 将Json数据解析成相应的映射对象 */
    public static <T> T fromJson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    /** 将Json数据解析成相应的映射对象列表, 这个是正确写法,下面是错误写法 */
    public static final <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for(final JsonElement elem : array){
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

    public static String ObjectToJson(Object o){
        Gson gson = new Gson();
        String jsonstr= gson.toJson(o);
        return jsonstr;

    }

    /** 这个是错误写法 */
    public static final <T> ArrayList<T> fromJsonList(String json) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Gson gson = new Gson();
        Type type = new TypeToken<T>(){}.getType();
        T t = (T) new Object();
        ParameterizedType parameterizedType = (ParameterizedType) t.getClass().getGenericSuperclass();

        Class<T> entityClass = (Class<T>)(parameterizedType.getActualTypeArguments()[0]);
        for(final JsonElement elem : array){
//            mList.add((T) gson.fromJson(elem, type));
            mList.add(gson.fromJson(elem, entityClass));
        }
        return mList;
    }
}
