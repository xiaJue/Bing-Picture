package com.as.xiajue.picturebing.model.bean;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.as.xiajue.picturebing.model.cache.CacheUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/8.
 */

public class MaxPicItemData implements Serializable {
    private String uri;
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

    public String getUri() {
        return uri;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public static List<MaxPicItemData> Home2MaxPic(List<HomeItemData> homeList, CacheUtils
            cacheUtils) {
        List<MaxPicItemData> list = new ArrayList<>();
        for (HomeItemData data :
                homeList) {
            MaxPicItemData maxData = getMaxPicItemData(cacheUtils, data);
            list.add(maxData);
        }
        return list;
    }

    @NonNull
    public static MaxPicItemData getMaxPicItemData(CacheUtils cacheUtils, HomeItemData data) {
        MaxPicItemData maxData = new MaxPicItemData();
        maxData.setCopyright(data.getCopyright());
        maxData.setEnddate(data.getEnddate());
        maxData.setUri(Uri.fromFile(cacheUtils.getCacheFileFromUrl(data.getAbsUrl()))
                .toString());
        maxData.setUrl(data.getAbsUrl());
        return maxData;
    }
}
