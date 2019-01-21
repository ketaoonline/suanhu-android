package com.xiaoxiong.library.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoxiong.library.R;
import com.xiaoxiong.library.utils.LogUtil;


/**
 * Summary ：加载网络图片使用的工具类，基于Glide
 * Created by zhangdm on 2016/2/20.
 */
public class GlideUtil {
    public static final String TAG = "GlideUtil";

    /**
     * Glide的请求管理器类
     */
    private static RequestManager mRequestManager;
    private static Context mContext;

    /**
     * 初始化Glide工具
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        mRequestManager = Glide.with(context);
    }

    public static void loadNoDisk(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.loading);
        options.error(R.mipmap.loading);
        options.dontAnimate();
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void load(String url, int placeholder, int error, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeholder);
        options.error(error);
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }


    public static void load(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.loading);
        options.error(R.mipmap.loading);
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }


    public static void load(int res, ImageView imageView) {
        Glide.with(mContext.getApplicationContext())
                .load(res)
                .into(imageView);
    }


//    public static void load(String url, SimpleTarget<Bitmap> target) {
//        RequestOptions options = new RequestOptions();
//        options.error(R.mipmap.loading);
//        options.format(DecodeFormat.PREFER_ARGB_8888);
//        Glide.with(mContext.getApplicationContext())
//                .asBitmap()
//                .load(url)
//                .apply(options)
//                .into(target);
//    }

    public static void loadNoDisk(String url, SimpleTarget<Bitmap> target) {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.loading);
        options.format(DecodeFormat.PREFER_ARGB_8888);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext.getApplicationContext())
                .asBitmap()
                .load(url)
                .into(target);
    }

    //加载圆型的图片
    public static void loadCirclePicture(String url, int placeholder, int error, ImageView imageView) {
//        Glide.with(mContext).load(url).placeholder(placeholder).error(error).transform(new GlideCircleTransform(mContext)).into(imageView);
    }

    public static void loadCirclePicture(String url, ImageView imageView) {
//        Glide.with(mContext)
//                .load(url)
//                .placeholder(R.mipmap.loading)
//                .error(R.mipmap.loading)
//                .transform(new GlideCircleTransform(mContext)).into(imageView);
    }

    //加载圆角的图片
    public static void loadRoundPicture(String url, int placeholder, ImageView imageView, int dp) {
//        mRequestManager
//                .load(url)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, dp))
//                .placeholder(placeholder)
//                .error(placeholder)
//                .into(imageView);
    }

    //加载圆角的图片
    public static void loadRoundPicture(int url, int placeholder, ImageView imageView) {
//        mRequestManager
//                .load(url)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, 6))
//                .placeholder(placeholder)
//                .into(imageView);
    }


    public static void loadBlur(String url, int placeholder, int error, int radius,
                                ImageView imageView) {
//        Glide.with(mContext.getApplicationContext())
//                .load(url)
//                .animate(R.anim.image_fade_in)
//                .placeholder(placeholder)
//                .error(error)
//                .bitmapTransform(new BlurTransformation(mContext, radius))
//                .into(imageView);
    }

    //加载高斯模糊图片
    public static void loadBlurPicture(String url, ImageView imageView, int defaultImg) {
        if (!isInit()) {
            return;
        }
        if (imageView == null) {
            return;
        }
//        DrawableRequestBuilder builder = mRequestManager
//                .load(url)
//                .dontAnimate().bitmapTransform(new BlurTransformation(mContext, 20, 1));// “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//
//        if (defaultImg != -1) {
//            builder = builder.placeholder(defaultImg);
//        }
//        builder.into(imageView);
    }

//    public static void loadPicture(String url, GlideDrawableImageViewTarget listener, int defaultImg) {
//        if (!isInit()) {
//            return;
//        }
//        DrawableRequestBuilder builder = mRequestManager
//                .load(url)
//                .dontAnimate();
//        if (defaultImg != -1) {
//            builder = builder.placeholder(defaultImg);
//        }
//        builder.into(listener);
//    }

//    public static void loadPicture(String url, final Map<String, String> header, GlideDrawableImageViewTarget listener, int defaultImg) {
//        if (!isInit()) {
//            return;
//        }
//        Headers headers = new Headers() {
//            @Override
//            public Map<String, String> getHeaders() {
//                return header;
//            }
//        };
//        GlideUrl gliderUrl = new GlideUrl(url, headers);
//        DrawableRequestBuilder builder = mRequestManager
//                .load(gliderUrl)
//                .dontAnimate();
//        if (defaultImg != -1) {
//            builder = builder.placeholder(defaultImg);
//        }
//        builder.into(listener);
//    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    /**
     * Glide工具类是否已经初始化
     *
     * @return 已初始化则返回true
     */
    public static boolean isInit() {
        if (mContext == null || mRequestManager == null) {
            LogUtil.i(TAG, "not init");
            return false;
        }
        return true;
    }

    public static void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

}
