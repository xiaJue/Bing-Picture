package com.as.xiajue.picturebing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.NetUtils.HttpUtils;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.adapter.HomeAdapter;
import com.as.xiajue.picturebing.adapter.HomeNetDataAdapter;
import com.as.xiajue.picturebing.adapter.SpaceItemDecoration;
import com.as.xiajue.picturebing.cache.CacheUtils;
import com.as.xiajue.picturebing.dialog.DialogManager;
import com.as.xiajue.picturebing.model.HomeItemData;
import com.as.xiajue.picturebing.model.MaxPicItemData;
import com.as.xiajue.picturebing.utils.DensityUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaJue on 2017/7/29.
 */

public class HomeActivity extends BaseActivity {
    private static final int itemSpace = Const.itemSpace;//条目间的间距
    private static final int itemLinCount = Const.ITEM_LIN_COUNT;//一行显示的条目个数
    private TwinklingRefreshLayout mRefreshLayout;//刷新组件
    private RecyclerView mRecyclerView;
    private CacheUtils mCacheUtils;//缓存类
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //始终在右上角显示菜单
        showRightTopMenu();
        initial();//初始化数据
        setEvent();//设置各种事件
    }

    private HomeNetDataAdapter mHomeNetAdapter;//recycleView和json数据的控制器

    /**
     * 初始化数据
     */
    private void initial() {
        //初始化view
        mRefreshLayout = getView(R.id.refreshLayout);
        mRecyclerView = getView(R.id.home_recyclerView);
        mToolbar = getView(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //初始化数据
        mDataList = new ArrayList();
        mAdapter = new HomeAdapter(this, mDataList, itemLinCount, itemSpace);
        mHomeNetAdapter = new HomeNetDataAdapter(this, mDataList, mAdapter);
        mHomeNetAdapter.setRefreshLayout(mRefreshLayout);
        mCacheUtils = new CacheUtils(this);
        Const.initialItemSpace(this);
    }

    private HomeAdapter mAdapter;
    private List<HomeItemData> mDataList;

    /**
     * 设置事件
     */
    private void setEvent() {
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
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                /**
                 * 当条目点击，打开大图界面
                 */
                Intent intent = new Intent(HomeActivity.this, MaxPictureActivity.class);
                List maxList = MaxPicItemData.Home2MaxPic(mDataList, mCacheUtils);
                intent.putExtra("_position", position + 1);
                intent.putExtra("_maxList", (Serializable) maxList);
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(itemLinCount,
                StaggeredGridLayoutManager
                        .VERTICAL));//使用瀑布流
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
                mHomeNetAdapter.requestJsonData(0);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                //finishLoadMore();
                mHomeNetAdapter.requestJsonData(mHomeNetAdapter.getLoadingIdx());
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
        switch (item.getItemId()) {
            case R.id.menu_home_about:
                //显示一个dialog
                DialogManager.showAboutDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtils.getInstance(this).cacheFlush();
//        mHomeNetAdapter.addToDatabase();//将数据存储到数据库
    }

//    /**
//     * 他的父类BaseActivity需要得到actionBar的颜色来设置状态栏的颜色
//     * 所以需要在本类onCreate前得到toolbar的实例
//     *
//     * @return Toolbar
//     */
//    @Override
//    protected PicActionBar getToolbar() {
//        setContentView(R.layout.activity_home);
////        mActionBar = (PicActionBar) findViewById(R.id.home_actionBar);
//        return mActionBar;
//    }

    /**
     * 始终在右上角显示一个省略按钮菜单。因为有些手机会默认在手机下方显示菜单
     */
    private void showRightTopMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {

        }
    }
}
