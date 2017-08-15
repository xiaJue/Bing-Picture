package com.as.xiajue.picturebing.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Moing_Admin on 2017/7/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 不用强制转换的findViewById
     * @param id
     * @param <T>
     * @return
     */
    protected <T> T getView(int id) {
        return (T) findViewById(id);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        PicActionBar actionBar = getToolbar();
//        if (actionBar == null) {
//            return;
//        }
//        int color = actionBar.getBgColor();
//        ScreenUtils.setStatusColor(this, getWindow(),color);//设置状态栏颜色
//        setSupportActionBar(actionBar);
//    }
//    protected abstract PicActionBar getToolbar();
}
