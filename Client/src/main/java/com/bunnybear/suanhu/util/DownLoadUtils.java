package com.bunnybear.suanhu.util;

import android.os.Environment;
import android.util.Log;

import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.net.Http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DownLoadUtils {

    private static final String TAG = "DownLoadUtils";
    private static final String PATH_FILE = Environment.getExternalStorageDirectory() + "/DownloadFileTwo";
    private static String filePath;
    private static File mFile;



    public static void downloadFile(String url,final DownloadListener downloadListener) {
        Log.d(TAG,"downloadFile");
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
            Log.d(TAG,"filePath=" + filePath);
        }
        mFile = new File(filePath);

        Http.http.createApi(MineAPI.class)
                .downloadFile(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onNext(final ResponseBody responseBody) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                writeFileSDcard(responseBody, mFile, downloadListener);
                            }
                        }.start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"onError=" + e.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"onComplete");
                    }
                });

    }

    private static void writeFileSDcard(ResponseBody responseBody, File mFile, DownloadListener downloadListener) {
        downloadListener.onStart();
        Log.d(TAG,"writeFileSDcard");
        long currentLength = 0;
        OutputStream os = null;
        InputStream is = responseBody.byteStream();
        long totalLength = responseBody.contentLength();
        Log.d(TAG,"totalLength=" + totalLength);
        try {
            os = new FileOutputStream(mFile);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                Log.d(TAG,"当前长度: " + currentLength);
                int progress = (int) (100 * currentLength / totalLength);
                Log.d(TAG,"当前进度: " + progress);
                downloadListener.onProgress(progress);
                if (progress == 100) {
                    downloadListener.onFinish(filePath);
                }
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG,"Exception=" + e.getMessage());
            downloadListener.onFailure("未找到文件！");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG,"Exception=" + e.getMessage());
            downloadListener.onFailure("IO错误！");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public interface DownloadListener{
        void onStart();
        void onProgress(int progress);
        void onFinish(String filePath);
        void onFailure(String failureMsg);
    }

}
