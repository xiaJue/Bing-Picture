package com.as.xiajue.picturebing.view.activity;

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
}
