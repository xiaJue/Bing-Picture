package com.as.xiajue.picturebing.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.model.utils.L;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by xiaJue on 2017/8/23.
 */

public class AboutActivity extends BaseActivity {
    private Toolbar mToolbar;
    private SubsamplingScaleImageView mImageView;
    private TextView mVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        setData();

    }

    private void setData() {
        Intent intent = getIntent();
        MaxPicItemData data = (MaxPicItemData) intent.getSerializableExtra("data");
        //set image
        if(ImageLoader.getInstance()!=null&&data!=null) {
            Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(data.getUrl());
            if (bitmap != null) {
                mImageView.setImage(ImageSource.bitmap(bitmap));
            } else {
                //disk cache image
                File file = ImageLoader.getInstance().getDiskCache().get(data.getUrl());
                if (file.exists()) {
                    mImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
                } else {
                    mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
                }
            }
        }else{
            mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
        }
        //设置版本号
        mVersion.setText(getString(R.string.version) + getVersionName());
    }

    private void bindView() {
        mToolbar = getView(R.id.about_toolbar);
        mImageView = getView(R.id.about_toolbar_image);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mVersion = getView(R.id.about_version);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        L.e(version+"----------");
        return version;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
