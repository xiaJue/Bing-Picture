package com.as.xiajue.picturebing.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.as.xiajue.picturebing.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //打开HomeActivity
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

//    @Override
//    protected PicActionBar getToolbar() {
//        return null;
//    }
}
