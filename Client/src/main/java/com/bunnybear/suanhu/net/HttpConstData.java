package com.bunnybear.suanhu.net;

import android.text.TextUtils;

import com.bunnybear.suanhu.base.ConstData;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/30.
 */

public class HttpConstData {

    public static Server SERVER;
//    public static String ROOT = "http://df.luojiyong.com/";//old
    public static String ROOT = "http://www.qdske.cn/";//new

//    public static String headImageUpload = ROOT + "api/btb/userinfo/avatar_edit";
//    public static String feedbackImageUpload = ROOT + "api/btb/userinfo/avatar_edit";
//    public static String payImageUpload = ROOT + "api/btb/my_order/pay";
    public static String rootUrl = ROOT + "api/";//local

    private static List<String> urls = new ArrayList<>();

    static {
        urls.add(rootUrl);

        String rooturl = urls.get(0);
//        String rooturl = Hawk.get(ConstData.ROOT_URL);
//        if (TextUtils.isEmpty(rooturl)) {
//            rooturl = urls.get(0);
//            Hawk.put(ConstData.ROOT_URL, rooturl);
//        }
        SERVER = new Server(rooturl);
    }


}
