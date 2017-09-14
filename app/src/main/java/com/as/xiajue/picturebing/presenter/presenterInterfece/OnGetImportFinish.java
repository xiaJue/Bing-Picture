package com.as.xiajue.picturebing.presenter.presenterInterfece;

import com.as.xiajue.picturebing.model.bean.HomeItemData;

import java.util.List;

/**
 * xiaJue 2017/9/13创建
 */
public interface OnGetImportFinish {
    void success(List<HomeItemData> list);
    void failure();
}
