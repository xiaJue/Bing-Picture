package com.as.xiajue.picturebing.ui.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.base.BasePresenter;
import com.as.xiajue.picturebing.utils.FileUtils;
import com.as.xiajue.picturebing.utils.IntentUtils;
import com.as.xiajue.picturebing.utils.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * xiaJue 2018/4/14创建
 */
public class DetailsPresenter extends BasePresenter<DetailsContract.View> implements
        DetailsContract.Presenter {
    public File mShareTempFile;

    @Override
    public void saveImage(final Context context, final String url) {
        L.e("save...");
        Glide.with(context).load(url).asBitmap().skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                    glideAnimation) {
                save(context, new File(url).getName(), FileUtils.compressBitmap(resource, Const
                        .DOWNLOAD_IMAGE_DIR + "temp.jpg"));
            }
        });
    }

    @Override
    public void shareImage(final Context context, final String url) {
        L.e("share....");
        Glide.with(context).load(url).asBitmap().skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                    glideAnimation) {
                share(context, FileUtils.compressBitmap(resource, Const.DOWNLOAD_IMAGE_DIR +
                        "temp.jpg"));
            }
        });
    }

    @Override
    public void clearTempFile() {
        if (mShareTempFile != null && mShareTempFile.exists()) {
            mShareTempFile.delete();
        }
    }

    /**
     * 分享图片
     */
    private void share(Context context, File file) {
        mShareTempFile = file;
        //分享
        Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
        Intent shareFileIntent = IntentUtils.getShareFileIntent(context, mShareTempFile);
        context.startActivity(Intent.createChooser(shareFileIntent, context
                .getString(R.string.share_how)));
    }


    /**
     * 保存图片
     */
    private void save(Context context, String name, File file) {
        File[] outFile = new File[1];
        int resultCode = FileUtils.copyFile(file,
                Const.DOWNLOAD_IMAGE_DIR, name, outFile);
        if (resultCode == 0) {
            mView.saveSuccess();
            //通知系统图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                    ("file://" + outFile[0])));
        } else if (resultCode == 1) {
            mView.saveError();
        } else {
            mView.saveExist();
        }
    }
}
