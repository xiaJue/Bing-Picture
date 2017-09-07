package com.as.xiajue.picturebing;

import android.content.Context;
import android.os.Environment;

import com.as.xiajue.picturebing.model.utils.DensityUtils;

import java.io.File;

/**
 * Created by XiaJue on 2017/8/3.
 * 一些全局性的配置
 */

public class Const {
    public static int itemSpace;//条目间的间距
    public static Context context;
    public static int ITEM_IMAGE_ROUND_SIZE =10;//图片圆角的角度
    public static int ITEM_IMAGE_ANIMATE_DURATION_MILLIS=800;//图片渐变显示的时长

    /**
     * 初始化条目边距
     * @param context
     */
    public static void initialItemSpace(Context context) {
        itemSpace = DensityUtils.dp2px(context, 5);//条目间的间距
    }

    public static final int LOAD_JSON_COUNT = 3;//一次显示的条目个数、最大为8
    public static final int ITEM_LIN_COUNT = 1;//一行显示的条目个数
    public static final String BASE_URL = "http://www.bing.com/";
    public static final String URL_ADDRESS = "http://www.bing.com/HPImageArchive" +
            ".aspx?format=js&idx=";
    public static final String DOWNLOAD_IMAGE_DIR = Environment.getExternalStorageDirectory() +
            File.separator + "bingDownload" + File.separator;
}
