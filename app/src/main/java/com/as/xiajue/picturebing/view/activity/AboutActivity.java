package com.as.xiajue.picturebing.view.activity;

import android.content.Intent;
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
import com.as.xiajue.picturebing.presenter.AboutPresenter;
import com.as.xiajue.picturebing.view.activity.viewInterface.IAboutView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

/**
 * Created by xiaJue on 2017/8/23.
 */

public class AboutActivity extends BaseActivity implements IAboutView {
    private Toolbar mToolbar;
    private SubsamplingScaleImageView mImageView;
    private TextView mVersion;
    private AboutPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);
        mPresenter = new AboutPresenter(this);
        bindView();
        setData();
    }

    @Override
    public View getTopViewToBaseActivity() {
        return  findViewById(R.id.about_appBar);
    }

    private void setData() {
        Intent intent = getIntent();
        MaxPicItemData data = (MaxPicItemData) intent.getSerializableExtra("data");
        if (data != null) {
            mPresenter.onSetImage(data.getUrl());
        } else {
            mPresenter.onSetImage("");
        }
        mPresenter.onSetVersion();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onMenuItemSelect(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setVersion(String version) {
        //设置版本号
        mVersion.setText(version);
    }

    @Override
    public <T> void setImage(T tt) {
        //设置图片
        if (tt != null) {
            if (tt instanceof Bitmap) {
                mImageView.setImage(ImageSource.bitmap((Bitmap) tt));
            } else if (tt instanceof File) {
                mImageView.setImage(ImageSource.uri(Uri.fromFile((File) tt)));
            }
        } else {
            mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
        }
    }

}
