package com.as.xiajue.picturebing.presenter.presenterInterfece;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.model.cache.CacheUtils;
import com.as.xiajue.picturebing.utils.FileUtils;
import com.as.xiajue.picturebing.view.activity.viewInterface.IMaxView;

import java.io.File;

/**
 * xiaJue 2017/9/5创建
 */
public class MaxPresenter {
    private long BOTTOM_ANIMATION_DURATION = 300;//底部动画的播放时长
    private Context mContext;
    private IMaxView mIMaxView;

    private CacheUtils mCacheUtils;//缓存类

    public MaxPresenter(IMaxView iMaxView) {
        this.mContext = (Context) iMaxView;
        this.mIMaxView = iMaxView;
        mCacheUtils = new CacheUtils(mContext);
    }

    /**
     * 当点击页面
     */
    public void onClick(View view,int id){
        switch (id) {
            case R.id.item_maxP_imageView:
                showOrHideAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * 当viewPager滑动
     */
    public void onPageChange(MaxPicItemData data, int position){
        mIMaxView.getMToolbarTitleText().setText((position + 1) + "/" + mIMaxView.getDataList().size());
        mIMaxView.getDateTextView().setText(data.getCopyright());
        mIMaxView.getDateTextView().setDateText(HomeItemData.getFormatDate(data.getEnddate()));
        if (!isShow) {//isShow为false说明现在控件处于隐藏状态
            bottomAnimation();
        }
    }
    /**
     * 点击消失或显示标题栏、底部textView相关操作
     */
    private boolean isShow = false;//flag--显示or隐藏
    private static final int HORIZONTAL = 998;
    private static final int VERTICAL = 999;
    /**
     * 触发动画效果
     */
    private void showOrHideAnimation() {
        //标题栏的动画
        setViewAnimation(mIMaxView.getMToolbar(), mIMaxView.getMToolbar().getHeight(), VERTICAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        //底部的动画
        setViewAnimation(mIMaxView.getDateTextView(), mIMaxView.getDateTextView().getWidth(), HORIZONTAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        isShow = !isShow;
    }

    /**
     * 只是播放底部的动画
     */
    private void bottomAnimation() {
        setViewAnimation(mIMaxView.getDateTextView(), mIMaxView.getDateTextView().getWidth(), HORIZONTAL, false,
                BOTTOM_ANIMATION_DURATION);
        setViewAnimation(mIMaxView.getDateTextView(), mIMaxView.getDateTextView().getWidth(), HORIZONTAL, true,
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
    /**
     * 当菜单选择
     */
    public void onMenuSelect(MenuItem item, int id){
        switch (id) {
            case R.id.menu_max_save:
                /**
                 * 保存图片--因为图片在一开始已经被缓存到本地了
                 * 为了节省流量直接从缓存文件中获取图片
                 */
                saveImage(mIMaxView.getUrl());
                break;
            case R.id.menu_max_share:
                /**
                 * 分享图片
                 */
                shareImage(mIMaxView.getUrl());
                break;
            case android.R.id.home:
                /**
                 * 左上角返回按钮
                 */
                ((Activity)mContext).finish();
                break;
        }
    }
    public File shareTempFile;
    /**
     * 分享图片
     */
    private void shareImage(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        //获取临时文件的目录
        String path = Const.DOWNLOAD_IMAGE_DIR + "temp" + File.separator;
        //将缓存文件复制到临时文件夹
        File file = mCacheUtils.getCacheFileFromUrl(url);
        FileUtils.copyFile(file, path, new File[1]);
        //拿到新文件的路径
        shareTempFile = new File(path + FileUtils.changeSuff(file.getName(), ".jpg"));
        //分享
        Uri uri = Uri.fromFile(shareTempFile);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_how)));
    }

    /**
     * 保存图片
     */
    private void saveImage(String url) {
        File[] outFile = new File[1];
        int resultCode = FileUtils.copyFile(mCacheUtils.getCacheFileFromUrl(url),
                Const.DOWNLOAD_IMAGE_DIR, outFile);
        if (resultCode == 0) {
            Toast.makeText(mContext, R.string.save_success, Toast.LENGTH_SHORT).show();
            //通知系统图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                    ("file://" + outFile[0])));
        } else if (resultCode == 1) {
            Toast.makeText(mContext, R.string.save_failure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, R.string.save_exist, Toast.LENGTH_SHORT).show();
        }
    }
}
