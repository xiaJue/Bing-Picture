package com.as.xiajue.picturebing.view.activity.viewInterface;

/**
 * xiaJue 2017/9/8创建
 */
public interface IAboutView {
    /**
     * 设置版本号
     * @param version version string
     */
    void setVersion(String version);

    /**
     * 加载图片bitmap或file
     * @param tt bitmap or file
     * @param <T> Bitmap or File
     */
    <T> void setImage(T tt);
}
