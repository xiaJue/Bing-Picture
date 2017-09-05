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

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.utils.L;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        setImage();

    }

    private void setImage() {
        Intent intent = getIntent();
        MaxPicItemData data = (MaxPicItemData) intent.getSerializableExtra("data");
        //set image
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(data.getUrl());
        if (bitmap != null) {
            mImageView.setImage(ImageSource.bitmap(bitmap));
            L.e("memory");
        }else{
            //disk cache image
            File file = ImageLoader.getInstance().getDiskCache().get(data.getUrl());
            if (file.exists()) {
                L.e("disk");
                mImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
            }else{
                mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
            }
        }
//        String uri = data.getUri();
//        mImageView.setImage(ImageSource.uri(uri));
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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(R.string.app_name);
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
