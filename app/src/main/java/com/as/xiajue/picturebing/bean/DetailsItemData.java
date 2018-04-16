package com.as.xiajue.picturebing.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/8.
 */

public class DetailsItemData implements Serializable {
    private String copyright;
    private String enddate;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 注意！这是一个完整的url
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @NonNull
    public static DetailsItemData getMaxPicItemData(HomeItemData data) {
        DetailsItemData maxData = new DetailsItemData();
        maxData.setCopyright(data.getCopyright());
        maxData.setEnddate(data.getEnddate());
        maxData.setUrl(data.getAbsUrl());
        return maxData;
    }

    public static List Home2MaxPic(List<HomeItemData> dataList) {
        List list = new ArrayList();
        for (HomeItemData data :
                dataList) {
            DetailsItemData maxData = getMaxPicItemData(data);
            list.add(maxData);
        }
        return list;
    }
}
