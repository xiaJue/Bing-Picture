package com.as.xiajue.picturebing.ui.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.base.BaseActivity;
import com.as.xiajue.picturebing.bean.DetailsItemData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by xiaJue on 2017/8/23.
 */

public class AboutActivity extends BaseActivity implements AboutContract.View {
    private Toolbar mToolbar;
    private SubsamplingScaleImageView mImageView;
    private TextView mVersion;


    @Override
    protected void initialize() {
        setData();
        mVersion.setText(getString(R.string.version) + getVersionName());
    }

    private void setData() {
        Intent intent = getIntent();
        DetailsItemData data = (DetailsItemData) intent.getSerializableExtra("data");
        if (data != null) {
            Glide.with(this).load(data.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                        glideAnimation) {
                    mImageView.setImage(ImageSource.bitmap(resource));
                }
            });
        } else {
            mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
        }
        //默认的图片
//
    }

    @Override
    public void bindView() {
        mToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        mImageView = (SubsamplingScaleImageView) findViewById(R.id.about_toolbar_image);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mVersion = (TextView) findViewById(R.id.about_version);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        mPresenter.onMenuItemSelect(item);
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }
}
