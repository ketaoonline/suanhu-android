package com.bunnybear.suanhu.oss;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.utils.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * Created by zhaoya on 2017/1/5.
 */
public class OssUtil {

    private static final String TAG = OssUtil.class.getSimpleName();
    private static OSS oss;
    private Context context;

    // 运行sample前需要配置以下字段为有效的值, OSS是给你存文件的，相当于网盘，CDN是给你分发内容的，相当于缓存
    private static final String endpoint_oss = "http://oss-cn-beijing.aliyuncs.com"; // oss
    private static final String accessKeyId = OSSConfig.AK;
    private static final String accessKeySecret = OSSConfig.SK;

    public static final String testBucket = "suanhu-app";
    public static final String object_image = "client/"; // 上传到文件夹

    private static Handler handler;
    private static ExecutorService fixedThreadPool; // 线程池
    private static OSSAsyncTask task;

    public static List<String> tempList = new ArrayList<>();

    private static final Handler getMainHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public static final OSS getOssInstance(Context context) {
        if (oss == null) {
            OSSCredentialProvider credentialProvider = OSSConfig.newCustomSignerCredentialProvider();
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();
            oss = new OSSClient(context, endpoint_oss, credentialProvider, conf);
        }
        return oss;
    }

    /**
     * 上传文件
     * @param file
     * @param callBack
     */
    public static void upload(Context context,File file, final CallBack callBack) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(OssUtil.testBucket, object_image + getUUIDByRules32Image(), file.getPath());

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, final long currentSize, final long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if (callBack != null) {
                    getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onLoading(totalSize, currentSize);
                        }
                    });
                }
            }

        });
        task = getOssInstance(context).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, final PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                // 阿里oss上传成功之后调用之前上传接口的方法
                if (callBack != null) {
                    getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(getUrl(request.getObjectKey()));
                        }
                    });
                }

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Log.e(TAG, "oss error");
                getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onError(new Exception("图片上传失败"));
                        }
                    }
                });
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务
//        task.waitUntilFinished(); // 可以等待直到任务完成
    }


    public static void uploadImages(Context context,final List<LocalMedia> imageFiles, final int total, final int currentPosition,final UploadCallBack uploadCallBack){
        File imageFile = new File(imageFiles.get(currentPosition).getPath());
        OssUtil.upload(context,imageFile, new OssUtil.CallBack() {
            @Override
            public void onLoading(long totalSize, long currentSize) {

            }

            @Override
            public void onSuccess(String result) {
                tempList.add(result);
                if (currentPosition < total - 1){
                    uploadImages(context,imageFiles,total,currentPosition+1,uploadCallBack);
                }else{
                    uploadCallBack.onCompleted(tempList);
                }
            }

            @Override
            public void onError(Exception e) {
                uploadCallBack.onError(e);
            }
        });
    }

    public interface UploadCallBack {
        void onCompleted(List<String> imageUrls);
        void onError(Exception e);
    }

    public static String getUrl(String key) {
        try {
            // 设置URL过期时间为10年  3600l* 1000*24*365*10
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            // 生成URL
            String url = oss.presignConstrainedObjectURL(testBucket, key, DateUtil.getMillis(expiration));
            if (!TextUtils.isEmpty(url)) {
                return url;
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static void cancelTask(){
        task.cancel();
    }

    /**
     * 上传到后台的图片的名称
     */
    public static String getUUIDByRules32Image() {
        StringBuffer generateRandStr = null;
        try {
            String rules = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int rpoint = 0;
            generateRandStr = new StringBuffer();
            Random rand = new Random();
            int length = 32;
            for (int i = 0; i < length; i++) {
                if (rules != null) {
                    rpoint = rules.length();
                    int randNum = rand.nextInt(rpoint);
                    generateRandStr.append(rules.substring(randNum, randNum + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (generateRandStr == null) {
            return "getUUIDByRules32Image.png";
        }
        return generateRandStr + ".png";
    }


    public interface CallBack {
        void onLoading(long totalSize, long currentSize);
        void onSuccess(String result);
        void onError(Exception e);
    }
}
