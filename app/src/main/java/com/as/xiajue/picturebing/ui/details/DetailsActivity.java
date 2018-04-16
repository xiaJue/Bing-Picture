package com.as.xiajue.picturebing.ui.details;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.base.BaseActivity;
import com.as.xiajue.picturebing.bean.DetailsItemData;
import com.as.xiajue.picturebing.bean.HomeItemData;
import com.as.xiajue.picturebing.ui.custom.BottomAnimationManager;
import com.as.xiajue.picturebing.ui.custom.DateTranBackTextView;
import com.as.xiajue.picturebing.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/5.
 */

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements View.OnClickListener,
        DetailsContract.View {
    private Toolbar mToolbar;//actionBar
    private TextView mToolbarTitle;//actionBar title text
    private ViewPager mViewPager;
    private DateTranBackTextView mDateTextView;//底部显示内容和时间的textView
    private List<DetailsItemData> mDataList;
    private List<View> mViewList;
    private DetailsAdapter mPicAdapter;
    private LayoutInflater mInflater;
    private String mUrl;
    private int mPosition, mSize;
    private String mCopyright, mEnddate;
    private BottomAnimationManager mAnimationManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_max_picture;
    }

    @Override
    protected void bindView() {
        mViewPager = (ViewPager) findViewById(R.id.maxP_viewPager);
        mToolbarTitle = (TextView) findViewById(R.id.max_toolbar_title);
        mToolbar = (Toolbar) findViewById(R.id.max_toolbar);
        mDateTextView = (DateTranBackTextView) findViewById(R.id.maxP_dateTextV);
    }

    @Override
    protected void initialize() {
        //presenter
        mPresenter = new DetailsPresenter();
        mPresenter.attachView(this);
        mAnimationManager = new BottomAnimationManager();
        //init data
        mPosition = getIntent().getIntExtra("_position", 1);
        mDataList = (List<DetailsItemData>) getIntent().getSerializableExtra("_maxList");
        setSomeInfo(mPosition - 1);
        setViewPager();
        //bottom text
        mDateTextView.setText(mCopyright);
        mDateTextView.setDateText(HomeItemData.getFormatDate(mEnddate));
        //toolbar
        mToolbarTitle.setText(mPosition + "/" + mSize);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 拿到指定条目的数据并赋值到成员中
     */
    private void setSomeInfo(int position) {
        DetailsItemData data = mDataList.get(position);
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
        //只加载出4个条目
        mViewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = mInflater.inflate(R.layout.item_maxpic_viewpager, null);
            mViewList.add(view);
        }
        mPicAdapter = new DetailsAdapter(this, mDataList, mViewList);
        mViewPager.setAdapter(mPicAdapter);
        mViewPager.setCurrentItem(mPosition - 1);
        mPicAdapter.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new DetailsAdapter.MaxOnPagerChangeListener() {
            @Override
            public void onChange(int position) {
                setSomeInfo(position);
                //更新数据和播放动画
                DetailsItemData data = mDataList.get(position);
                mToolbarTitle.setText((position + 1) + "/" + mDataList.size());
                mDateTextView.setText(data.getCopyright());
                mDateTextView.setDateText(HomeItemData.getFormatDate(data.getEnddate()));
                if (!mAnimationManager.isShow) {//isShow为false说明现在控件处于隐藏状态
                    mAnimationManager.bottomAnimation(mDateTextView);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        //显示或隐藏--移动渐变动画
        mAnimationManager.showOrHideAnimation(mToolbar, mDateTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_max_save:
                mPresenter.saveImage(this, mUrl);
                break;
            case R.id.menu_max_share:
                L.e("share>>>>>>>>>>>>>>>>");
                mPresenter.shareImage(this, mUrl);
                break;
//            case R.id.menu_max_original:
//                //todo 查看原图
//                mPicAdapter.showOriginal(mViewPager.getCurrentItem());
//                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveError() {
        Toast.makeText(this, R.string.save_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveExist() {
        Toast.makeText(this, R.string.save_exist, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareError() {
    }

    @Override
    public void shareSuccess() {
    }

    @Override
    protected void onDestroy() {
        mPresenter.clearTempFile();
        super.onDestroy();
    }
}