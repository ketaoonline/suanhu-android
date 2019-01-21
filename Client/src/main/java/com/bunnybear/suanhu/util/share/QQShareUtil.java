package com.bunnybear.suanhu.util.share;

import android.app.Activity;
import android.widget.Toast;

import com.bunnybear.suanhu.bean.ShareBean;
import com.xiaoxiong.library.utils.CommonUtils;

import java.util.Map;


/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class QQShareUtil {

    public static void shareToQQ(final Activity activity,  ShareBean shareBean, final OnShareSuccessListener onShareSuccessListener){
        if (CommonUtils.isQQClientAvailable(activity)){
            QQShareManager qqShareManager = new QQShareManager(activity);
            qqShareManager.setOnQQShareResponse(new QQShareManager.QQShareResponse() {
                @Override
                public void respCode(int code) {

                }
            });
            QQShareManager.ShareContentWebpage webpage = new QQShareManager.ShareContentWebpage(shareBean.getTitle(),shareBean.getContent(),shareBean.getUrl(),shareBean.getImage());
            qqShareManager.shareWebPageQQ(activity,webpage);
        }else {
            Toast.makeText(activity,"QQ不可用",Toast.LENGTH_SHORT).show();
        }

    }

    public static void shareToQzone(final Activity activity, ShareBean shareBean, final OnShareSuccessListener onShareSuccessListener){
        if (CommonUtils.isQQClientAvailable(activity)){
            QQShareManager qqShareManager = new QQShareManager(activity);
            qqShareManager.setOnQQShareResponse(new QQShareManager.QQShareResponse() {
                @Override
                public void respCode(int code) {

                }
            });
            QQShareManager.ShareContentWebpage webpage = new QQShareManager.ShareContentWebpage(shareBean.getTitle(),shareBean.getContent(),shareBean.getUrl(),shareBean.getImage());
            qqShareManager.shareWebPageQzone(activity,webpage);
        }else {
            Toast.makeText(activity,"QQ不可用",Toast.LENGTH_SHORT).show();
        }

    }

}
