package com.as.xiajue.picturebing.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.adapter.MaxPicAdapter;
import com.as.xiajue.picturebing.cache.CacheUtils;
import com.as.xiajue.picturebing.model.HomeItemData;
import com.as.xiajue.picturebing.model.MaxPicItemData;
import com.as.xiajue.picturebing.utils.FileUtils;
import com.as.xiajue.picturebing.view.DateTranBackTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/5.
 */

public class MaxPictureActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar mToolbar;//actionBar
    private TextView mToolbarTitle;//actionBar title text
    private ViewPager mViewPager;
    private DateTranBackTextView mDateTextView;//底部显示内容和时间的textView
    private CacheUtils mCacheUtils;//缓存类
    private File shareTempFile;//分享图片的临时文件
    private long BOTTOM_ANIMATION_DURATION = 300;//底部动画的播放时长

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_picture);
        mCacheUtils = new CacheUtils(this);
        initIntentData();
        setViewPager();
        setTextView();
        setToolbar();
    }

    private String mUrl;
    private String mUri;
    private int mPosition, mSize;
    private String mCopyright, mEnddate;

    /**
     * 初始化从上一个activity传递过来的数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("_position", 1);
        mDataList = (List<MaxPicItemData>) intent.getSerializableExtra("_maxList");
        setSomeInfo(mPosition - 1);
    }

    /**
     * 拿到指定条目的数据并赋值到成员中
     *
     * @param position
     */
    private void setSomeInfo(int position) {
        MaxPicItemData data = mDataList.get(position);
        mUri = data.getUri();
        mUrl = data.getUrl();//获得完整的url
        mCopyright = data.getCopyright();
        mEnddate = data.getEnddate();
        mSize = mDataList.size();
    }

    private List<MaxPicItemData> mDataList;
    private List<View> mViewList;
    private MaxPicAdapter mPicAdapter;
    private LayoutInflater mInflater;

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
        mPicAdapter = new MaxPicAdapter(mDataList, mViewList);
        mViewPager = getView(R.id.maxP_viewPager);
        mViewPager.setAdapter(mPicAdapter);
        mViewPager.setCurrentItem(mPosition - 1);
        mViewPager.addOnPageChangeListener(new MaxPicAdapter.MaxOnPagerChangeListener() {
            @Override
            public void onChange(int position) {
                /**
                 * 更新下一些数据
                 */
                setSomeInfo(position);
                mToolbarTitle.setText((position + 1) + "/" + mSize);
                mDateTextView.setText(mCopyright);
                mDateTextView.setDateText(HomeItemData.getFormatDate(mEnddate));
                /**
                 * 底部动画效果
                 */
                if (!isShow) {//isShow为false说明现在控件处于隐藏状态
                    bottomAnimation();
                }
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
        switch (v.getId()) {
            /**
             * 接近整个页面的点击事件
             */
            case R.id.item_maxP_imageView:
                //显示或隐藏--移动渐变动画
                showOrHideAnimation();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.max_pic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_max_save:
                /**
                 * 保存图片--因为图片在一开始已经被缓存到本地了
                 * 为了节省流量直接从缓存文件中获取图片
                 */
                saveImage();
                break;
            case R.id.menu_max_share:
                /**
                 * 分享图片
                 */
                shareImage();
                break;
            case android.R.id.home:
                /**
                 * 左上角返回按钮
                 */
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享图片
     */
    private void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        //获取临时文件的目录
        String path = Const.DOWNLOAD_IMAGE_DIR + "temp" + File.separator;
        //将缓存文件复制到临时文件夹
        File file = mCacheUtils.getCacheFileFromUrl(mUrl);
        FileUtils.copyFile(file, path, new File[1]);
        //拿到新文件的路径
        shareTempFile = new File(path + FileUtils.changeSuff(file.getName(), ".jpg"));
        //分享
        Uri uri = Uri.fromFile(shareTempFile);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share_how)));
    }

    /**
     * 保存图片
     */
    private void saveImage() {
        File[] outFile = new File[1];
        int resultCode = FileUtils.copyFile(mCacheUtils.getCacheFileFromUrl(mUrl),
                Const.DOWNLOAD_IMAGE_DIR, outFile);
        if (resultCode == 0) {
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
            //通知系统图库更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                    ("file://" + outFile[0])));
        } else if (resultCode == 1) {
            Toast.makeText(this, R.string.save_failure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.save_exist, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 触发动画效果
     */
    private void showOrHideAnimation() {
        //标题栏的动画
        setViewAnimation(mToolbar, mToolbar.getHeight(), VERTICAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        //底部的动画
        setViewAnimation(mDateTextView, mDateTextView.getWidth(), HORIZONTAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        isShow = !isShow;
    }

    /**
     * 点击消失或显示标题栏、底部textView相关操作
     */
    private boolean isShow = false;//flag--显示or隐藏
    private static final int HORIZONTAL = 998;
    private static final int VERTICAL = 999;

    /**
     * 只是播放底部的动画
     */
    private void bottomAnimation() {
        setViewAnimation(mDateTextView, mDateTextView.getWidth(), HORIZONTAL, false,
                BOTTOM_ANIMATION_DURATION);
        setViewAnimation(mDateTextView, mDateTextView.getWidth(), HORIZONTAL, true,
                BOTTOM_ANIMATION_DURATION);
    }

    /**
     * 设置并播放动画
     *
     * @param view            控件
     * @param translateLength 移动的距离
     * @param orientation     移动的方向
     * @param isShow          是隐藏还是显示
     * @param yanShi          播放的时长
     */
    private void setViewAnimation(final View view, int translateLength, int orientation, final
    boolean isShow, long yanShi) {
        TranslateAnimation translate = null;
        AlphaAnimation alpha;
        if (isShow) {
            if (orientation == HORIZONTAL) {
                translate = new TranslateAnimation(-translateLength, 0, 0, 0);
            } else if (orientation == VERTICAL) {
                translate = new TranslateAnimation(0, 0, -translateLength, 0);
            }
            alpha = new AlphaAnimation(0.0f, 1.0f);
        } else {
            if (orientation == HORIZONTAL) {
                translate = new TranslateAnimation(0, -translateLength, 0, 0);
            } else if (orientation == VERTICAL) {
                translate = new TranslateAnimation(0, 0, 0, -translateLength);
            }
            alpha = new AlphaAnimation(1.0f, 0.0f);
        }
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setDuration(BOTTOM_ANIMATION_DURATION);
        set.setStartTime(yanShi);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(set);
    }

    @Override
    protected void onDestroy() {
        if (shareTempFile != null && shareTempFile.exists()) {
            shareTempFile.delete();//删除临时文件
        }
        super.onDestroy();
    }
}
