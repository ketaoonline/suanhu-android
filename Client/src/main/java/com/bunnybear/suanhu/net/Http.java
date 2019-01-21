package com.bunnybear.suanhu.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by longbh on 16/5/24.
 */
public class Http {

    public static Http http;
    private Retrofit mRetrofit;
    public static OkHttpClient client;
    public final static int CONNECT_TIMEOUT = 10;
    public final static int READ_TIMEOUT = 10;
    public final static int WRITE_TIMEOUT = 10;

    private Http(Context context, String baseURL) {
        File httpCacheDirectory = new File(context.getApplicationContext().getCacheDir(), context
                .getApplicationContext().getPackageName());
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        client = new OkHttpClient.Builder()
                .addInterceptor(new RequestHeaderInterceptor())
                .addInterceptor(new RequestLogInterceptor())
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .cache(cache)
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .registerTypeAdapterFactory(new NullbooleanTofalseAdapterFactory<>())
                .registerTypeAdapterFactory(new NulldoubleTo0AdapterFactory<>())
                .registerTypeAdapterFactory(new NullIntTo0AdapterFactory<>())
                .registerTypeAdapterFactory(new NullLongTo0AdapterFactory<>())
                .serializeNulls()
                .create();
        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

//    public static boolean isLogin() {
//        return !"".equals(Hawk.get(HawkContants.SESSION, ""));
//    }

    public static void initHttp(Context context, String baseURL) {
        http = new Http(context, baseURL);
    }

    public <T> T createApi(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }


    //自定义Strig适配器
    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) STRING;
        }
    }

    private static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                // 在这里处理null改为空字符串
                writer.value("");
                return;
            }
            writer.value(value);
        }
    };

    //自定义LONG适配器
    private static class NullLongTo0AdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != Long.class) {
                return null;
            }
            return (TypeAdapter<T>) LONG;
        }
    }

    private static final TypeAdapter<Long> LONG = new TypeAdapter<Long>() {
        public Long read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return 0L;
            }
            return reader.nextLong();
        }

        public void write(JsonWriter writer, Long value) throws IOException {
            if (value == null) {
                writer.value(0L);
                return;
            }
            writer.value(value);
        }
    };


    //自定义Int适配器
    private static class NullIntTo0AdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != Integer.class) {
                return null;
            }
            return (TypeAdapter<T>) INT;
        }
    }

    private static final TypeAdapter<Integer> INT = new TypeAdapter<Integer>() {
        public Integer read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return 0;
            }
            return reader.nextInt();
        }

        public void write(JsonWriter writer, Integer value) throws IOException {
            if (value == null) {
                writer.value(0);
                return;
            }
            writer.value(value);
        }
    };

    //自定义double适配器
    private static class NulldoubleTo0AdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != Double.class) {
                return null;
            }
            return (TypeAdapter<T>) DOUBLE;
        }
    }

    private static final TypeAdapter<Double> DOUBLE = new TypeAdapter<Double>() {
        public Double read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return 0D;
            }
            return reader.nextDouble();
        }

        public void write(JsonWriter writer, Double value) throws IOException {
            if (value == null) {
                writer.value(0D);
                return;
            }
            writer.value(value);
        }
    };

    //自定义boolean适配器
    private static class NullbooleanTofalseAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != Boolean.class) {
                return null;
            }
            return (TypeAdapter<T>) BOOLEAN;
        }
    }

    private static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        public Boolean read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return false;
            }
            return reader.nextBoolean();
        }

        public void write(JsonWriter writer, Boolean value) throws IOException {
            if (value == null) {
                writer.value(false);
                return;
            }
            writer.value(value);
        }
    };
}
