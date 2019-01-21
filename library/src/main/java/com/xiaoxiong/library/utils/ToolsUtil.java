package com.xiaoxiong.library.utils;

import android.content.Context;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangming
 * @time 2017/4/21 上午11:41
 */
public class ToolsUtil {
    static Toast toast;

    public static void toast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);

        }
        toast.setText(s);
        toast.show();
    }

    public static boolean isEmpty(String string) {
        if (TextUtils.isEmpty(string) || string.equals("null") || string.equals("undefined")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        return false;
    }

    /**
     * 将byte转化为更大单位的字符串
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        DecimalFormat format = new DecimalFormat("#.00");
        if (size < 1024) {
            return format.format((double) size) + "B";
        } else if (size < 1048576) {
            return format.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            return format.format((double) size / 1048576) + "M";
        } else {
            return format.format((double) size / 1073741824) + "G";
        }
    }

    //计算两个坐标点之间的距离
    public static long distance(double lat, double lng, double targetLat, double targetLng) {

        double latD = lat * Math.PI / 180;
        double lngD = lng * Math.PI / 180;
        double targetLatD = targetLat * Math.PI / 180;
        double targetLngD = targetLng * Math.PI / 180;

        double latSin = (latD - targetLatD) / 2;
        double latPow = Math.pow(latSin, 2);

        double cosLat = Math.cos(latD);
        double cosTargetLat = Math.cos(targetLatD);

        double lngSin = (lngD - targetLngD) / 2;
        double lngPow = Math.pow(lngSin, 2);
        double sqrt = Math.sqrt(latPow + cosLat * cosTargetLat * lngPow);
        return Math.round(6378.138 * 2 * sqrt * 1000);
    }

    public static String distanceConversion(float m) {
        if (m < 1000) {
            return m + "m";
        } else {
            return ArithUtil.div(m, 1000, 2) + "km";
        }
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public static int getResource(Context context, String imageName) {
        int resId = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
        return resId;
    }
    /**
     * @param pageIndx  页码，初始1，默认1
     * @param lat       纬度
     * @param lng       经度
     * @param province  省
     * @param city      市
     *                  下面为可选项
     * @param district  区
     * @param choose    排序：（10.智能，20.距离，30.浏览量）
     * @param cat_id    分类ID
     * @param scopekm   距离：千米
     * @param shoptype  1.全部商家，2.优惠商家，3.翼支付商家
     * @param manjian
     * @param miaosha
     * @param zhekou
     * @param youhui
     * @param paytype   在线支付
     * @param paytype_2 到店付
     * @return
     */
    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            Logs.e(e.getMessage());
            return null;
        }
    }


    /**
     * 禁止EditText输入特殊字符
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return getNewStringNoNum(source.toString());
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    private static String getNewStringNoNum(String str) {
        char[] chars = str.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                buffer.append(chars[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        if (num <= 0.01 && num > 0.00) {
            num = 0.01;
        }
        num = ArithUtil.round(num, 2);
        return new DecimalFormat("0.00").format(num + 0.0);//+0.0：解决-0.0的问题
    }

    static int level = 0;

    public static int getEmuiLeval() {
        // Finals 2016-6-14 如果获取过了就不用再获取了，因为读取配置文件很慢
        if (level > 0) {
            return level;
        }
        Properties properties = new Properties();
        File propFile = new File(Environment.getRootDirectory(), "build.prop");
        FileInputStream fis = null;
        if (propFile != null && propFile.exists()) {
            try {
                fis = new FileInputStream(propFile);
                properties.load(fis);
                fis.close();
                fis = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        if (properties.containsKey("ro.build.hw_emui_api_level")) {
            String valueString = properties.getProperty("ro.build.hw_emui_api_level");
            try {
                level = Integer.parseInt(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return level;
    }
}