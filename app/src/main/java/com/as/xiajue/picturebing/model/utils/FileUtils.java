package com.as.xiajue.picturebing.model.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

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

    public static int copyFile(File file, String dir, File[] outFile) {
        if (file.exists()) {
            outFile[0] = new File(dir, changeSuff(file.getName(), ".jpg"));
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

    /**
     * 更改字符串的后缀
     *
     * @param name
     * @param suff
     * @return
     */
    public static String changeSuff(String name, String suff) {
        int index = name.indexOf('.');
        L.e("index=" + index);
        return index != -1 ? name.substring(0, index) : name + suff;
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
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            return true;
        } else {
            return false;
        }
    }

}
