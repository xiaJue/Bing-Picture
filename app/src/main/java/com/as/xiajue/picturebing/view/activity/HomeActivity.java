package com.as.xiajue.picturebing.view.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.adapter.HomeRecyclerAdapter;
import com.as.xiajue.picturebing.model.adapter.SpaceItemDecoration;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.manager.SnackbarManager;
import com.as.xiajue.picturebing.model.utils.DensityUtils;
import com.as.xiajue.picturebing.model.utils.FileUtils;
import com.as.xiajue.picturebing.model.utils.MenuUtils;
import com.as.xiajue.picturebing.model.utils.NetUtils;
import com.as.xiajue.picturebing.model.utils.SPUtils;
import com.as.xiajue.picturebing.presenter.HomePresenter;
import com.as.xiajue.picturebing.view.activity.viewInterface.IHomeView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import static com.as.xiajue.picturebing.model.utils.FileUtils.setApi23;

/**
 * Created by XiaJue on 2017/7/29.
 */

public class HomeActivity extends BaseActivity implements IHomeView {
    private static int itemSpace;//条目间的间距
    private static final int itemLinCount = Const.ITEM_LIN_COUNT;//一行显示的条目个数
    private TwinklingRefreshLayout mRefreshLayout;//refresh
    private RecyclerView mRecyclerView;//recyclerView(listView or GridView)
    private Toolbar mToolbar;//toolbar(actionBar)
    private List<HomeItemData> mDataList;//list view data
    private HomeRecyclerAdapter mAdapter;//list view adapter
    private HomePresenter mPresenter;//the activity data and biz handle
    private TextView mInternetFailureText;//internet connect failure show text
    private ProgressBar mProgressBar;//import data progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);
        //始终在右上角显示菜单
        MenuUtils.showRightTopMenu(this);
        //初始化数据
        initialize();
        //兼容api23以上版本
        if (((int) SPUtils.get(this, "get_permissions", 0)) == 0) {
            setApi23(this);
            SPUtils.put(this, "get_permissions", 1);
        }
    }

    @Override
    public View getTopViewToBaseActivity() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case FileUtils.PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    Toast.makeText(this, "权限获取成功！将用于保存或分享图片时使用!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限获取失败！将不能正常保存或分享图片!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        mInternetFailureText = getView(R.id.home_internet_failure_text);
        mProgressBar = getView(R.id.home_progress);
        //初始化数据
        Const.initialItemSpace(this);
        itemSpace = Const.itemSpace;

        mDataList = new ArrayList();
        mAdapter = new HomeRecyclerAdapter(this, mDataList);
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
                mPresenter.onItemClick(item, position);
            }
        });
    }

    public boolean mNetState;//网络状态

    @Override
    protected void onResume() {
        super.onResume();
        //如果是没网状态并在返回界面时发现有网络的话则刷新
        if (NetUtils.isNetworkConnected(this) && !mNetState) {
            mRefreshLayout.startRefresh();
        }
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
        mPresenter.onMenuSelect(item, item.getItemId());
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
    public TextView getInternetFialureText() {
        return mInternetFailureText;
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void showInternetFailure() {
        SnackbarManager.showInternetFialure(mRecyclerView, this);
    }

    @Override
    public void showToast(String text, int... lengths) {
        Toast.makeText(this, text, lengths.length > 0 ? lengths[0] : Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNetState(boolean b) {
        mNetState = b;
    }
}
