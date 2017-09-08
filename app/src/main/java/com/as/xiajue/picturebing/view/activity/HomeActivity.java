package com.as.xiajue.picturebing.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.utils.L;
import com.as.xiajue.picturebing.model.adapter.HomeRecyclerAdapter;
import com.as.xiajue.picturebing.model.adapter.SpaceItemDecoration;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.manager.SnackbarManager;
import com.as.xiajue.picturebing.presenter.HomePresenter;
import com.as.xiajue.picturebing.model.utils.DensityUtils;
import com.as.xiajue.picturebing.model.utils.MenuUtils;
import com.as.xiajue.picturebing.view.activity.viewInterface.IHomeView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaJue on 2017/7/29.
 */

public class HomeActivity extends BaseActivity implements IHomeView{
    private static int itemSpace;//条目间的间距
    private static final int itemLinCount = Const.ITEM_LIN_COUNT;//一行显示的条目个数
    private TwinklingRefreshLayout mRefreshLayout;//refresh
    private RecyclerView mRecyclerView;//recyclerView(listView or GridView)
    private Toolbar mToolbar;//toolbar(actionBar)
    private List<HomeItemData> mDataList;//list view data
    private HomeRecyclerAdapter mAdapter;//list view adapter
    private HomePresenter mPresenter;//the activity data and biz handle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.e("log ok...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //始终在右上角显示菜单
        MenuUtils.showRightTopMenu(this);
        //初始化数据
        initialize();
    }


    /**
     * 初始化数据
     */
    private void initialize() {
        //初始化view
        mRefreshLayout = getView(R.id.refreshLayout);
        mRecyclerView = getView(R.id.home_recyclerView);
        mToolbar = getView(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //初始化数据
        Const.initialItemSpace(this);
        itemSpace = Const.itemSpace;

        mDataList = new ArrayList();
        mAdapter = new HomeRecyclerAdapter(this,mDataList);
        mPresenter = new HomePresenter(this);
        //设置
        //refreshLayout的一些配置
        SetRefreshLayout();
        //recycleView的一些配置
        setRecycleView();
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
        mAdapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                /**
                 * 当条目点击，打开大图界面
                 */
                mPresenter.onItemClick(item,position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.startRefresh();
    }

    /**
     * 设置refreshingLayout
     */
    private void SetRefreshLayout() {
        mRefreshLayout.setHeaderHeight(DensityUtils.dp2px(this, 20));
        mRefreshLayout.setFloatRefresh(true);//设置悬浮刷新
        mRefreshLayout.setHeaderView(new ProgressLayout(this));//设置刷新样式
        mRefreshLayout.startRefresh();//主动刷新
        /**设置结束刷新的条件**/
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //finishRefreshing();
                mPresenter.onLayoutRefresh(HomePresenter.UP_REFRESH);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                //finishLoadMore();
                mPresenter.onLayoutRefresh(HomePresenter.DOWN_LOAD_MORE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理菜单点击事件
        mPresenter.onMenuSelect(item,item.getItemId());
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

    @Override
    public TwinklingRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public List getDataList() {
        return mDataList;
    }

    @Override
    public HomeRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void showInternetFailure() {
        SnackbarManager.showInternetFialure(mRecyclerView,this);
    }
}
