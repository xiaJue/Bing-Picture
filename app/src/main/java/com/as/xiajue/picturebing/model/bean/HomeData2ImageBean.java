package com.as.xiajue.picturebing.model.bean;

import com.as.xiajue.picturebing.model.database.bean.ImageBean;

/**
 * xiaJue 2017/9/12创建
 */
public class HomeData2ImageBean {
    public static ImageBean home2bean(HomeItemData data){
        ImageBean bean = new ImageBean();
        bean.setUrl(data.getUrl());
        bean.setCopyright(data.getCopyright());
        bean.setEnddate(data.getEnddate());
        return bean;
    }
    public static HomeItemData bean2home(ImageBean bean){
        HomeItemData data = new HomeItemData();
        data.setUrl(bean.getUrl());
        data.setCopyright(bean.getCopyright());
        data.setEnddate(bean.getEnddate());
        return data;
    }
}
