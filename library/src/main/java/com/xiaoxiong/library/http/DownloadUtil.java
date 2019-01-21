package com.xiaoxiong.library.http;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class DownloadUtil {

    private static final String TAG = "DownloadUtil";
    
    private static final String PATH_FILE = Environment.getExternalStorageDirectory() + "/SuanHu";
    private static String filePath;
    private static File mFile;


    public static void downloadApk(String url,final DownloadListener downloadListener) {
        //建立一个文件夹
        mFile = new File(PATH_FILE);
        if (!mFile.exists() || !mFile.isDirectory()) {
            mFile.mkdirs();
        }
        //通过Url得到保存到本地的文件名
        String name = url;
        int index = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
        if (index != -1) {
            name = name.substring(index);
            filePath = PATH_FILE + name;
        }
        mFile = new File(filePath);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.d(TAG, "onStart: ");
                downloadListener.onStart();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                Log.d(TAG, "onProgress: bytesWritten="+bytesWritten+"---totalSize="+totalSize);
                int progress = (int) (100 * bytesWritten / totalSize);
                downloadListener.onProgress(progress);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(TAG, "onSuccess: "+i+"---");
                if (i == 200){
                    BufferedOutputStream bos = null;
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mFile);
                        bos = new BufferedOutputStream(fos);
                        bos.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (bos != null) {
                            try {
                                bos.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        downloadListener.onFinish(filePath);
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(TAG, "onFailure: ");
                downloadListener.onFailure("下载失败");
            }
        });

    }


    public interface DownloadListener {
        void onStart();

        void onProgress(int progress);

        void onFinish(String filePath);

        void onFailure(String failureMsg);
    }


}
