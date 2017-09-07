package com.as.xiajue.picturebing.model.utils;

import android.app.Activity;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by xiaJue on 2017/8/15.
 */

public class MenuUtils {
    /**
     * 始终在右上角显示一个省略按钮菜单。因为有些手机会默认在手机下方显示菜单
     */
    public static void showRightTopMenu(Activity activity) {
        try {
            ViewConfiguration config = ViewConfiguration.get(activity);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
        }
    }
}
