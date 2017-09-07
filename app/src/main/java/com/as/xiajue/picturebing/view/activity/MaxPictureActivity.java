package com.as.xiajue.picturebing.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.view.activity.adapter.MaxPicAdapter;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.presenter.MaxPresenter;
import com.as.xiajue.picturebing.view.custom.DateTranBackTextView;
import com.as.xiajue.picturebing.view.activity.viewInterface.IMaxView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/5.
 */

public class MaxPictureActivity extends BaseActivity implements View.OnClickListener, IMaxView {
    private Toolbar mToolbar;//actionBar
    private TextView mToolbarTitle;//actionBar title text
    private ViewPager mViewPager;
    private DateTranBackTextView mDateTextView;//底部显示内容和时间的textView
    private List<MaxPicItemData> mDataList;
    private List<View> mViewList;
    private MaxPicAdapter mPicAdapter;
    private LayoutInflater mInflater;
    private String mUrl;
    private int mPosition, mSize;
    private String mCopyright, mEnddate;
    private MaxPresenter mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_picture);
        initIntentData(getIntent());
        mPresenter = new MaxPresenter(this);
        setViewPager();
        setTextView();
        setToolbar();
    }
    /**
     * 初始化从上一个activity传递过来的数据
     */
    private void initIntentData(Intent intent) {
        mPosition = intent.getIntExtra("_position", 1);
        mDataList = (List<MaxPicItemData>) intent.getSerializableExtra("_maxList");
        setSomeInfo(mPosition - 1);
    }
    /**
     * 拿到指定条目的数据并赋值到成员中
     */
    private void setSomeInfo(int position) {
        MaxPicItemData data = mDataList.get(position);
        mUrl = data.getUrl();//获得完整的url
        mCopyright = data.getCopyright();
        mEnddate = data.getEnddate();
        mSize = mDataList.size();
    }

    /**
     * 设置viewPager
     */
    private void setViewPager() {
        mInflater = LayoutInflater.from(this);
        /**
         * 模仿listView的条目复用，只加载出4个条目
         */
        mViewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = mInflater.inflate(R.layout.item_maxpic_viewpager, null);
            mViewList.add(view);
        }
        mPicAdapter = new MaxPicAdapter(this,mDataList, mViewList);
        mViewPager = getView(R.id.maxP_viewPager);
        mViewPager.setAdapter(mPicAdapter);
        mViewPager.setCurrentItem(mPosition - 1);
        mViewPager.addOnPageChangeListener(new MaxPicAdapter.MaxOnPagerChangeListener() {
            @Override
            public void onChange(int position) {
                setSomeInfo(position);
                //更新数据和播放动画
                mPresenter.onPageChange(mDataList.get(position),position);
            }
        });
        mPicAdapter.setOnClickListener(this);
    }

    /**
     * 设置actionBar
     */
    private void setToolbar() {
        mToolbarTitle = getView(R.id.max_toolbar_title);
        mToolbar = getView(R.id.max_toolbar);
        mToolbarTitle.setText(mPosition + "/" + mSize);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 设置底部textView
     */
    private void setTextView() {
        mDateTextView = getView(R.id.maxP_dateTextV);
        mDateTextView.setText(mCopyright);
        mDateTextView.setDateText(HomeItemData.getFormatDate(mEnddate));
    }

    @Override
    public void onClick(View v) {
        //显示或隐藏--移动渐变动画
        mPresenter.onClick(v,v.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.max_pic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       mPresenter.onMenuSelect(item,item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<MaxPicItemData> getDataList() {
        return mDataList;
    }

    @Override
    public TextView getMToolbarTitleText() {
        return mToolbarTitle;
    }

    @Override
    public Toolbar getMToolbar() {
        return mToolbar;
    }

    @Override
    public DateTranBackTextView getDateTextView() {
        return mDateTextView;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter.shareTempFile != null && mPresenter.shareTempFile.exists()) {
            mPresenter.shareTempFile.delete();//删除临时文件
        }
        super.onDestroy();
    }
}