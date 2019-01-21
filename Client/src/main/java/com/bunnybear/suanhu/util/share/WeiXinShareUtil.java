package com.bunnybear.suanhu.util.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ShareBean;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class WeiXinShareUtil {
	private static final String APP_ID = "wx501f993a177e9239";
	public static IWXAPI api;
	/**
     * 会话
     */
    public static final int WEIXIN_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;
    /**
     * 朋友圈
     */
    public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;
	
	public static void req2WX(Context context){
		if (api == null) {
			api = WXAPIFactory.createWXAPI(context, APP_ID, true);
			api.registerApp(APP_ID);
		}
	}

	public static void shareToWX(Context context, ShareBean shareBean, final int type){
		req2WX(context);
		Glide.with(context).asBitmap().load(shareBean.getImage()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = shareBean.getUrl();
                WXMediaMessage mediaMessage = new WXMediaMessage();
                mediaMessage.mediaObject = webpageObject;
                mediaMessage.title = shareBean.getTitle();
                mediaMessage.description = shareBean.getContent();
                Bitmap bitmap = Bitmap.createScaledBitmap(resource,200,200,true);
                mediaMessage.setThumbImage(bitmap);
                bitmap.recycle();
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = mediaMessage;
                req.scene = type;
                api.sendReq(req);
            }
		});

	}

	public static void wxLogin(Context context){
		req2WX(context);
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "diandi_wx_login";
		api.sendReq(req);
	}

//	public static void shareToWX(Bitmap bmp){
//		Bitmap thumb = Bitmap.createScaledBitmap(bmp, 100, 150, true);
//		WXImageObject imageObject = new WXImageObject(bmp);
//		WXMediaMessage mediaMessage = new WXMediaMessage();
//		mediaMessage.mediaObject = imageObject;
//		mediaMessage.thumbData = compressImage(thumb);
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = String.valueOf(System.currentTimeMillis());
//		req.message = mediaMessage;
//		req.scene = WEIXIN_SHARE_TYPE_TALK;
//		api.sendReq(req);
//	}

	private static byte[] compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		return baos.toByteArray();
	}



}
