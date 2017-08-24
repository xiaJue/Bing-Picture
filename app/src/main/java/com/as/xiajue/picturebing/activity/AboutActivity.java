package com.as.xiajue.picturebing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.MaxPicItemData;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Moing_Admin on 2017/8/23.
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
        if (data == null) {
            mImageView.setImage(ImageSource.resource(R.mipmap.toolbar_bg));
            return;
        }
        String uri = data.getUri();
        mImageView.setImage(ImageSource.uri(uri));
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
