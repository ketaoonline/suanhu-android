//package com.xiaoxiong.library.utils;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.widget.ImageView;
//
//import com.xiaoxiong.library.utils.glide.GlideUtil;
//
///**
// * Created by Administrator on 2017/8/29 0029.
// */
//
//public class GlideImageLoader extends ImageLoader {
//
//    public GlideImageLoader() {
//    }
//
//    @Override
//    public void displayImage(Context context, Object path, ImageView imageView) {
//        if (path instanceof Drawable) {
//            imageView.setImageDrawable((Drawable) path);
//        }else{
//            GlideUtil.load(path.toString(),imageView);
////            Glide.with(context).load(path).placeholder(R.mipmap.loading).into(imageView);
//        }
//    }
//}
