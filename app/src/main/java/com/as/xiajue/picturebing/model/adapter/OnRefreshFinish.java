package com.as.xiajue.picturebing.model.adapter;

import com.as.xiajue.picturebing.model.bean.HomeItemData;

import java.util.List;

/**
 * xiaJue 2017/9/5创建
 */
public interface OnRefreshFinish {
        void success(List<HomeItemData> list);
        void fialure(List<HomeItemData> list);
}
