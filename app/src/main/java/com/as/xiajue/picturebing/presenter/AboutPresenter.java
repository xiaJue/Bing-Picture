package com.as.xiajue.picturebing.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.MenuItem;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.manager.ImageLoaderManager;
import com.as.xiajue.picturebing.view.activity.viewInterface.IAboutView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * xiaJue 2017/9/8创建
 */
public class AboutPresenter {
    private Context mContext;
    private IAboutView mIAboutView;

    public AboutPresenter(IAboutView mIAboutView) {
        this.mIAboutView = mIAboutView;
        this.mContext= (Context) mIAboutView;
    }

    /**
     * 加载图片
     * @param url url
     */
    public void onSetImage(String url){
        //set image
            Bitmap bitmap = ImageLoaderManager.getInstance(mContext).getMemoryCache(url);
            if (bitmap != null) {
                mIAboutView.setImage(bitmap);
            } else {
                //disk cache image
                File file = ImageLoader.getInstance().getDiskCache().get(url);
                if (file.exists()) {
                    mIAboutView.setImage(file);
                } else {
                    mIAboutView.setImage(null);
                }
            }
        }

    /**
     * 获取应用版本号
     */
    public void onSetVersion(){
        mIAboutView.setVersion(mContext.getString(R.string.version) + getVersionName());
    }

    public void onMenuItemSelect(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                ((Activity)mContext).finish();
                break;
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName()
    {
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }
}
