package com.as.xiajue.picturebing.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.as.xiajue.picturebing.model.utils.ScreenUtils;

/**
 * Created by Moing_Admin on 2017/7/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 不用强制转换的findViewById
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果版本高于4.4[API19]则将toolbar向下移动一个状态栏的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getTopViewToBaseActivity() == null) {
                return;
            }
            int statusHeight = ScreenUtils.getStatusHeight(this);//状态栏高度
            ViewGroup.LayoutParams lp = getTopViewToBaseActivity().getLayoutParams();
            if (lp instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) lp).topMargin = statusHeight;
            } else if (lp instanceof CoordinatorLayout.LayoutParams) {
                ((CoordinatorLayout.LayoutParams) lp).topMargin = statusHeight;
            }
        }
    }

    public abstract View getTopViewToBaseActivity();
}
