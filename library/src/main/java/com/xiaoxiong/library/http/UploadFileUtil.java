package com.xiaoxiong.library.http;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class UploadFileUtil {

    public static void uploadFileHead(String uploadAddress, String filePath, String fileKey, String headerKey, String header, final RequestCallBack<String> callBack){
        if (TextUtils.isEmpty(filePath))return ;
        try {
            File file = new File(filePath);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setURLEncodingEnabled(false);
            client.addHeader(headerKey, header);
            client.addHeader("ContentType","multipart/form-data");
            client.addHeader("XX-Device-Type","android");
            RequestParams params = new RequestParams();
            params.put(fileKey,file);
            client.setTimeout(10000);
            client.post(uploadAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String responseData = new String();
                    responseData = new String(bytes);
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        int code = jsonObject.getInt("code");
                        if (code == 1) {
                            String url = jsonObject.getString("data");
                            callBack.success(url);
                        }else {
                            callBack.fail(code,jsonObject.getString("msg"));
                        }
                    } catch (Exception e) {
                        callBack.fail(-1,e.toString());
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    callBack.fail(i,"上传失败:"+throwable.toString());
                }
            });
        } catch (FileNotFoundException e) {
            callBack.fail(-1,"文件未找到");
            e.printStackTrace();
        }

    }

    public static void uploadFilePay(String uploadAddress, String filePath, int orderNumber, String headerKey, String header, final RequestCallBack<String> callBack){
        if (TextUtils.isEmpty(filePath))return ;
        try {
            File file = new File(filePath);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setURLEncodingEnabled(false);
            client.addHeader(headerKey, header);
            client.addHeader("ContentType","multipart/form-data");
            client.addHeader("XX-Device-Type","android");
            RequestParams params = new RequestParams();
            params.put("image",file);
            params.put("order_sn",orderNumber+"");
            client.setTimeout(10000);
            client.post(uploadAddress, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String responseData = new String();
                    responseData = new String(bytes);
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        int code = jsonObject.getInt("code");
                        if (code == 1) {
                            String url = jsonObject.getString("data");
                            callBack.success(url);
                        }else {
                            callBack.fail(code,jsonObject.getString("msg"));
                        }
                    } catch (Exception e) {
                        callBack.fail(-1,e.toString());
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    callBack.fail(i,"上传失败:"+throwable.toString());
                }
            });
        } catch (FileNotFoundException e) {
            callBack.fail(-1,"文件未找到");
            e.printStackTrace();
        }

    }

}
