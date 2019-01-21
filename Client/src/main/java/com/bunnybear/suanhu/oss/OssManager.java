package com.bunnybear.suanhu.oss;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.utils.LogUtil;

import java.util.List;
import java.util.Random;


public class OssManager {

    /**
     * 图片上传的地址
     * 问后台要的
     */
    public static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    /**
     * 图片的访问地址的前缀
     * 其实就是： bucketName + endpoint
     */
    public static String prefix = "http://oss-cn-beijing.aliyuncs.com";
    /**
     * Bucket是OSS上的命名空间
     * 问后台要的
     */
    public static String bucketName = "suanhu-app";

    /**
     * 图片保存到OSS服务器的目录，问后台要
     */
    public static String dir = "/";

    private OSS mOSS;

    private static OssManager mInstance;

    public static OssManager getInstance() {
        if (mInstance == null) {
            synchronized (OssManager.class) {
                mInstance = new OssManager();
            }
        }
        return mInstance;
    }

    /**
     * 创建OSS对象
     */
    private OSS getOSS(Context context) {
        if (mOSS == null) {
            OSSCredentialProvider provider = OSSConfig.newCustomSignerCredentialProvider();
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            mOSS = new OSSClient(context, endpoint, provider, conf);
        }
        return mOSS;
    }


    public void uploadImages(final Context context, List<LocalMedia> localMediaList, final UploadCallBack callBack) {
        final Boolean[] flags = new Boolean[localMediaList.size()];//用观察每个文件是否成功
        final String[] returnJsons = new String[localMediaList.size()];
        int i = 0;
        for (LocalMedia media : localMediaList) {
            final int index = i++;
            upload(context, index, media.getPath(), new OssManager.OnUploadListener() {

                        @Override
                        public void onProgress(int position, long currentSize, long totalSize) {
                            LogUtil.i("position = " + position + " onProgress = " + currentSize);
                        }

                        @Override
                        public void onSuccess(int position, String uploadPath,final String imageUrl) {
                            LogUtil.i("position = " + position + " imageUrl = " + imageUrl
                                    + "\n uploadPath = " + uploadPath);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    flags[index] = true;
                                    returnJsons[index] = imageUrl;
                                    if (checkAllSuccess(flags)){
                                        callBack.onCompleted(flags, returnJsons);//检查全部文件是否都已经结束，这里要确保线程安全
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFailure(int position) {
                            LogUtil.i("position = " + position);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    flags[index] = false;
                                    if (checkAllSuccess(flags)){
                                        callBack.onCompleted(flags, returnJsons);//检查全部文件是否都已经结束
                                    }
                                }
                            });
                        }
                    }
            );
        }

    }


    /**
     * 图片上传
     *
     * @param context
     * @param uploadFilePath   图片的本地路径
     * @param onUploadListener 回调监听
     */
    private void upload(final Context context, final int position, final String uploadFilePath, final OnUploadListener onUploadListener) {
        PutObjectRequest put = new PutObjectRequest(bucketName, dir + getUUIDByRules32Image(), uploadFilePath);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request,final long currentSize,final long totalSize) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onUploadListener == null) {
                            return;
                        }
                        onUploadListener.onProgress(position, currentSize, totalSize);
                    }
                });

            }
        });
        getOSS(context).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onUploadListener == null) {
                            return;
                        }
                        String imageUrl = request.getObjectKey();
                        onUploadListener.onSuccess(position, uploadFilePath,
                                prefix + imageUrl);
                    }
                });

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onUploadListener == null) {
                            return;
                        }
                        onUploadListener.onFailure(position);
                    }
                });

            }
        });


    }

    public interface OnUploadListener {
        /**
         * 上传的进度
         */
        void onProgress(int position, long currentSize, long totalSize);

        /**
         * 成功上传
         */
        void onSuccess(int position, String uploadPath, String imageUrl);

        /**
         * 上传失败
         */
        void onFailure(int position);
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


    /**
     * 检查所有文件是否全部上传成功，包括有的失败了，这个方法要确保线程安全
     *
     * @param flags
     * @return
     */
    public synchronized static boolean checkAllSuccess(Boolean flags[]) {
        for (Boolean b : flags) {
            if (b == null) return false;//这个方法要确保线程安全
        }
        return true;
    }

    public interface UploadCallBack {
        void onCompleted(Boolean[] status, String[] results);

//        void onLoading(int index, short progress);
//
//        void onItemCompleted(int index, Object result);
//
//        void onItemFailed(int index);
    }


}
