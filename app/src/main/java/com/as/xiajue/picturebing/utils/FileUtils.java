package com.as.xiajue.picturebing.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by XiaJue on 2017/8/7.
 */

public class FileUtils {
    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;
    private static final int EXIST = 2;

    public static int copyFile(File file, String dir, String name, File[] outFile) {
        if (file.exists()) {
            outFile[0] = new File(dir, name);
            return toCopy(file, outFile[0]);
        }
        return FAILURE;
    }

    public static int toCopy(File srcFile, File destFile) {

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return FAILURE;
        } else if (!srcFile.isFile()) {
            return FAILURE;
        }

        // 判断目标文件是否存在
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            return EXIST;
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return FAILURE;
                }
            }
        }

        // 复制文件

        try {
            FileInputStream fi = new FileInputStream(srcFile);
            FileOutputStream fo = new FileOutputStream(destFile);

            FileChannel in = fi.getChannel();

            FileChannel out = fo.getChannel();

            in.transferTo(0, in.size(), out);

            fi.close();
            fo.close();
            in.close();
            out.close();

            return SUCCESS;
        } catch (Exception e) {
            return FAILURE;
        }
    }

    public static File compressBitmap(Bitmap bitmap,String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            return new File(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final int PERMISSIONS_REQUEST_CODE = 1;

    /**
     * 发起一个写文件的权限请求
     */
    public static boolean setApi23(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * API23以上版本需要发起写文件权限请求
             */
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取应用专属缓存目录
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param context 上下文
     * @param type    文件夹类型 可以为空，为空则返回API得到的一级目录
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public static File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is mobile phone " +
                    "unknown exception !");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is make directory " +
                        "fail !");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param context 上下文
     * @param type    文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为
     *                .../data/app_package_name/files/Pictures
     *                {@link android.os.Environment#DIRECTORY_MUSIC},
     *                {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                {@link android.os.Environment#DIRECTORY_ALARMS},
     *                {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/"
                        + context.getPackageName() + "/cache/" + type);
            }

            if (appCacheDir == null) {
                Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard " +
                        "unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is make " +
                            "directory fail !");
                }
            }
        } else {
            Log.e("getExternalDirectory", "getExternalDirectory fail ,the reason is sdCard " +
                    "nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    /**
     * 获取内存缓存目录
     *
     * @param type 子目录，可以为空，为空直接返回一级目录
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);//
            // /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Log.e("getInternalDirectory", "getInternalDirectory fail ,the reason is make " +
                    "directory fail !");
        }
        return appCacheDir;
    }
}
