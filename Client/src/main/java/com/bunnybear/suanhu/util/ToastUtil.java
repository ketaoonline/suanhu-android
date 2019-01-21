package com.bunnybear.suanhu.util;

import android.content.Context;
import android.widget.Toast;

import com.bunnybear.suanhu.base.MyApplication;


/**
 * Created by zhaoya on 2016/12/23.
 */
public class ToastUtil {
    private static Toast toast;

    public static final void show(Context context,String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static final void showLong(Context context,String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
