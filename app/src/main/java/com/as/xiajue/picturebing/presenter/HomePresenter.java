package com.as.xiajue.picturebing.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.adapter.OnRefreshFinish;
import com.as.xiajue.picturebing.model.adapter.HomeDataAdapter;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.utils.L;
import com.as.xiajue.picturebing.view.activity.AboutActivity;
import com.as.xiajue.picturebing.view.activity.MaxPictureActivity;
import com.as.xiajue.picturebing.view.activity.viewInterface.IHomeView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * xiaJue 2017/9/5创建
 */
public class HomePresenter implements OnRefreshFinish {
    private Context mContext;

    private HomeDataAdapter mHomeDataAdapter;
    private IHomeView mIHomeView;
    public final static int UP_REFRESH=223;
    public final static int DOWN_LOAD_MORE=224;

    public HomePresenter(IHomeView iHomeView) {
        this.mIHomeView=iHomeView;
        this.mContext= (Context) iHomeView;
        mHomeDataAdapter = new HomeDataAdapter(mContext);
        mHomeDataAdapter.setList(mIHomeView.getDataList());
    }

    /**
     * 当菜单选择
     */
    public void onMenuSelect(MenuItem item, int id) {
        switch (item.getItemId()) {
            case R.id.menu_home_about:
                //开启aboutActivity
                Intent intent = new Intent(mContext, AboutActivity.class);
                HomeItemData data;
                if (mIHomeView.getDataList().size() > 0 && (data = mIHomeView.getDataList().get(0)) != null) {
                    intent.putExtra("data", MaxPicItemData.getMaxPicItemData(data));
                }
                mContext.startActivity(intent);
                break;
        }
    }

    /**
     * 当条目点击
     */
    public void onItemClick(View item, int position) {
        Intent intent = new Intent(mContext, MaxPictureActivity.class);
        List maxList = MaxPicItemData.Home2MaxPic(mIHomeView.getDataList());
        intent.putExtra("_position", position + 1);
        intent.putExtra("_maxList", (Serializable) maxList);
        mContext.startActivity(intent);
    }

    /**
     * 当用户刷新
     */
    public void onLayoutRefresh(int orientation) {
        if (orientation == UP_REFRESH) {
            L.e("refresh");
            mHomeDataAdapter.load(0,this);
        } else if (orientation == DOWN_LOAD_MORE) {
            L.e("more");
            mHomeDataAdapter.load(mHomeDataAdapter.getLoadingIdx(),this);
        }
    }

    /**
     * 从网络解析到了数据
     */
    @Override
    public void success(List<HomeItemData> list) {
        putToList(list);
        sortList(mIHomeView.getDataList());
        mIHomeView.getAdapter().notifyDataSetChanged();
        mIHomeView.getRefreshLayout().finishRefreshing();
        mIHomeView.getRefreshLayout().finishLoadmore();
    }
    /**
     * 对元素进行升序排序
     */
    private void sortList(List<HomeItemData> tempList) {

        Collections.sort(tempList, new Comparator<HomeItemData>() {
            @Override
            public int compare(HomeItemData o1, HomeItemData o2) {
                return Integer.valueOf(o2.getEnddate()).compareTo(Integer.valueOf(o1.getEnddate()));
            }
        });
    }

    /**
     * 从网络解析数据失败！但可能从数据库获取到了数据
     * if(list!=null){
     * }
     */
    @Override
    public void fialure(List<HomeItemData> list) {
        putToList(list);
        sortList(mIHomeView.getDataList());
        mIHomeView.showInternetFailure();
        mIHomeView.getRefreshLayout().finishRefreshing();
        mIHomeView.getRefreshLayout().finishLoadmore();
    }

    /**
     * 将数据添加到activity的list中
     */
    private void putToList(List<HomeItemData> list) {
        for (HomeItemData data :
                list) {
            if (!HomeItemData.listContains(mIHomeView.getDataList(), data)) {
                mIHomeView.getDataList().add(data);
            }
        }
    }
}
