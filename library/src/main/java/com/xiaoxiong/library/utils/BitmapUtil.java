package com.xiaoxiong.library.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * bitmap处理工具类
 * Created by oracleen on 2016/7/5.
 */
public class BitmapUtil {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;
    }

//    public static boolean saveBitmap(Bitmap bitmap, String dir, String name, boolean isShowPhotos) {
//        File path = new File(dir);
//        if (!path.exists()) {
//            path.mkdirs();
//        }
//        File file = new File(path + "/" + name);
//        if (file.exists()) {
//            file.delete();
//        }
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        } else {
//            return true;
//        }
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
//                    fileOutputStream);
//            fileOutputStream.flush();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                fileOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 其次把文件插入到系统图库
//        if (isShowPhotos) {
//            try {
//                MediaStore.Images.Media.insertImage(MyApplication.getIntstance().getContentResolver(),
//                        file.getAbsolutePath(), name, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            // 最后通知图库更新
//            MyApplication.getIntstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
//        }
//
//        return true;
//    }
}
