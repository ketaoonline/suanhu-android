package com.xiaoxiong.library.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;

/**
 * 获取手机相关信息的类
 *
 * @author zhangming
 * @time 2016/11/4 上午11:09
 */
public class AppUtil {
    /**
     * 获得设备的屏幕宽度
     *
     * @return 宽度的像素值
     */
    public static int getDeviceWidth(Context context) {
        return buildMetrics(context).widthPixels;
    }

    /**
     * 获得设备的屏幕高度
     *
     * @return 高度的像素值
     */
    public static int getDeviceHeight(Context context) {
        return buildMetrics(context).heightPixels;
    }

    /**
     * 得到屏幕密度
     *
     * @return 0.75/1/1.5...
     */
    public static float getDeviceDensity(Context context) {
        return buildMetrics(context).density;
    }

    /**
     * 得到屏幕密度的dpi单位
     *
     * @return 120/160/240...
     */
    public static int getDeviceDensityDPI(Context context) {
        return buildMetrics(context).densityDpi;
    }

    public static DisplayMetrics buildMetrics(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        Logs.e(metric.xdpi + ";" + metric.ydpi + ";" + metric.heightPixels + ";"
                + metric.widthPixels + ";" + metric.density + ";" + metric.densityDpi + ";"
                + metric.scaledDensity);
        return metric;
    }


    /**
     * 得到屏幕尺寸
     * 手机英寸计算方式:(widthPixels/xdpi)^2+(heightPixels/ydpi)^2开平方
     *
     * @return 例如MI4 :4.915699339870168,可见标注的5.0有水分
     */
    public static double getScreenSize(Context context) {
        DisplayMetrics metrics = buildMetrics(context);
        double w = Math.pow(ArithUtil.div(metrics.widthPixels, metrics.xdpi), 2);
        double h = Math.pow(ArithUtil.div(metrics.heightPixels, metrics.ydpi), 2);
        return Math.sqrt(w + h);
    }

    /**
     * 判断手机是否处理睡眠
     *
     * @return
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        Logs.e(isSleeping ? "手机睡眠中.." : "手机未睡眠...");
        return isSleeping;
    }

    /**
     * 检查网络状态
     *
     * @return 0：未开启网路；1：开启wifi；2：开启mobile；1-：获取失败
     */
    public static int checkNet(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) {
            return 0;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return 1;
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return 2;
        }
        return -1;
    }

    /**
     * 安装apk
     *
     * @param file
     */
    public static void installApk(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 获取设备id（IMEI）
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephony.getDeviceId();
        if (deviceId == null) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        Logs.e("当前设备IMEI码: " + deviceId);
        return deviceId != null ? deviceId : "Unknown";
    }

    /**
     * 获取当前运行的进程名称
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取当前应用程序的版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Logs.e("getAppVersion", e.getMessage());
        }
        Logs.e("该应用的版本名称: " + version);
        return version;
    }

    /**
     * 获取当前应用程序的版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Logs.e("getAppVersion", e.getMessage());
        }
        Logs.e("该应用的版本号: " + version);
        return version;
    }

    /****************一下方法需要特殊权限********************/

    /**
     * 获取设备mac地址
     *
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        Logs.e("当前mac地址: " + (null == macAddress ? "null" : macAddress));
        if (null == macAddress) {
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    public static String getIp(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if (!wm.isWifiEnabled())
            wm.setWifiEnabled(true);
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        return intToIp(ipAdd);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取手机剩余电量
     *
     * @param context
     * @return
     */
    public static int getPhoneResidueBattery(Context context) {
        Intent batteryInfoIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryInfoIntent.getIntExtra("level", 0);
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }


    /**
     * 是否安装某个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
