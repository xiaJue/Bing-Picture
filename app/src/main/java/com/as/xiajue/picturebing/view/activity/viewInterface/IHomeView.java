package com.as.xiajue.picturebing.view.activity.viewInterface;

import android.widget.ProgressBar;
import android.widget.TextView;

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

    TextView getInternetFialureText();

    ProgressBar getProgressBar();

    void showInternetFailure();

    void  showToast(String text,int... lengths);

    void setNetState(boolean b);
}
