package com.as.xiajue.picturebing.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.base.BaseActivity;
import com.as.xiajue.picturebing.bean.DetailsItemData;
import com.as.xiajue.picturebing.bean.HomeItemData;
import com.as.xiajue.picturebing.manager.SnackbarManager;
import com.as.xiajue.picturebing.ui.about.AboutActivity;
import com.as.xiajue.picturebing.ui.custom.SpaceItemDecoration;
import com.as.xiajue.picturebing.ui.details.DetailsActivity;
import com.as.xiajue.picturebing.utils.DensityUtils;
import com.as.xiajue.picturebing.utils.FileUtils;
import com.as.xiajue.picturebing.utils.MenuUtils;
import com.as.xiajue.picturebing.utils.SPUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by XiaJue on 2017/7/29.
 */

public class HomeActivity extends BaseActivity implements HomeContract.View<List<HomeItemData>> {
    private static int itemSpace;//条目间的间距
    private static final int itemLinCount = Const.ITEM_LIN_COUNT;//一行显示的条目个数
    private TwinklingRefreshLayout mRefreshLayout;//refresh
    private RecyclerView mRecyclerView;//recyclerView(listView or GridView)
    private Toolbar mToolbar;//toolbar(actionBar)
    private List<HomeItemData> mDataList;//list view data
    private HomeAdapter mAdapter;//list view adapter
    private HomePresenter mPresenter;//the activity data and biz handle
//    private TextView mInternetFailureText;//internet connect failure show text
//    private ProgressBar mProgressBar;//import data progress

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void bindView() {
        //初始化view
        mRefreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recyclerView);
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        mInternetFailureText = (TextView) findViewById(R.id.home_internet_failure_text);
//        mProgressBar = (ProgressBar) findViewById(R.id.home_progress);
    }

    @Override
    public void initialize() {
        //始终在右上角显示菜单
        MenuUtils.showRightTopMenu(this);
        //...
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        //兼容api23以上版本
        if (((int) SPUtils.get(this, "get_permissions", 0)) == 0) {
            FileUtils.setApi23(this);
            SPUtils.put(this, "get_permissions", 1);
        }
        //初始化数据
        Const.initialItemSpace(this);
        itemSpace = Const.itemSpace;
        mDataList = new ArrayList();
        mAdapter = new HomeAdapter(this, R.layout.item_home, mDataList);
        mPresenter = new HomePresenter(this);
        //绑定
        mPresenter.attachView(this);
        //refreshLayout的一些配置
        SetRefreshLayout();
        //recycleView的一些配置
        setRecycleView();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case FileUtils.PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.permission_failure, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void setUiData(List<HomeItemData> data) {
        //处理排序和重复
        putToList(data);
        sortList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadError(Throwable e) {
        SnackbarManager.showInternetFialure(mRecyclerView, this);
    }

    /**
     * 设置refreshingLayout
     */
    private void SetRefreshLayout() {
        mRefreshLayout.setHeaderHeight(DensityUtils.dp2px(this, 20));
        mRefreshLayout.setFloatRefresh(true);//设置悬浮刷新
        mRefreshLayout.setHeaderView(new ProgressLayout(this));//设置刷新样式
        mRefreshLayout.startRefresh();//主动刷新
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                mPresenter.refresh();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                mPresenter.loadData(mDataList.size() / Const.LOAD_JSON_COUNT);
                refreshLayout.finishLoadmore();
            }
        });
    }

    /**
     * 设置recycleVew
     */
    private void setRecycleView() {
        //recycleView的边距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px
                (this, itemSpace)));
        //初始化数据
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(itemLinCount,
                StaggeredGridLayoutManager
                        .VERTICAL));//使用瀑布流
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                List maxList = DetailsItemData.Home2MaxPic(mDataList);
                intent.putExtra("_position", position + 1);
                intent.putExtra("_maxList", (Serializable) maxList);
                startActivity(intent);
            }
        });
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

    private int putToList(List<HomeItemData> list) {
        int putCount = 0;
        for (HomeItemData data :
                list) {
            if (!HomeItemData.listContains(mDataList, data)) {
                mDataList.add(data);
                putCount++;
            }
        }
        return putCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 如果数据没有加载成功，则每次执行onResume都加载一遍
         */
        if (!mPresenter.isLoad()) {
            mPresenter.loadData(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理菜单点击事件
        switch (item.getItemId()) {
            case R.id.menu_home_about:
                //开启aboutActivity
                Intent intent = new Intent(this, AboutActivity.class);
                HomeItemData data;
                if (mDataList.size() > 0 && (data = mDataList.get(0)) != null) {
                    intent.putExtra("data", DetailsItemData.getMaxPicItemData(data));
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 始终在右上角显示菜单-有些手机点击菜单键后在手机下方显示菜单
     */
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                mToolbar.showOverflowMenu();
                return true;
        }
        return super.onKeyUp(keycode, e);
    }
}
