package com.as.xiajue.picturebing.view.activity.viewInterface;

import com.as.xiajue.picturebing.model.adapter.HomeRecyclerAdapter;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

/**
 * xiaJue 2017/9/5创建
 */
public interface IHomeView {
    TwinklingRefreshLayout getRefreshLayout();

    List<HomeItemData> getDataList();

    HomeRecyclerAdapter getAdapter();

    void showInternetFailure();
}
