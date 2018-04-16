package com.as.xiajue.picturebing.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * xiaJue 2017/9/12创建
 */
@Entity
public class ImageBean {

    @Unique
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String copyright;

    public String getCopyright() {
        return copyright;
    }
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Unique
    private String enddate;

    @Generated(hash = 1576409213)
    public ImageBean(String url, String copyright, String enddate) {
        this.url = url;
        this.copyright = copyright;
        this.enddate = enddate;
    }

    @Generated(hash = 645668394)
    public ImageBean() {
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
