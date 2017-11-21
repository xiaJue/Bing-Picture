package com.as.xiajue.picturebing.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.as.xiajue.picturebing.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        //打开HomeActivity
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public View getTopViewToBaseActivity() {
        return null;
    }


//    @Override
//    protected PicActionBar getToolbar() {
//        return null;
//    }
}
