package com.as.xiajue.picturebing.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * xiaJue 2018/4/15创建
 */
public class IntentUtils {

    /**
     * 调用分享文件
     */
    public static Intent getShareFileIntent(Context context, File file) {
        L.e("get share intent...");
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        //分享intent
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上必须这样
            L.e("7.0");
            String authority = context.getPackageName();
            contentUri = FileProvider.getUriForFile(context, authority, file);
        } else {
            contentUri = Uri.fromFile(file);
        }
        imageIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        return imageIntent;
    }
}
