package com.as.xiajue.picturebing.ui.custom;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * xiaJue 2017/9/5创建
 */
public class BottomAnimationManager {
    private long BOTTOM_ANIMATION_DURATION = 300;//底部动画的播放时长

    /**
     * 点击消失或显示标题栏、底部textView相关操作
     */
    public boolean isShow = false;//flag--显示or隐藏
    private static final int HORIZONTAL = 998;
    private static final int VERTICAL = 999;

    /**
     * 触发动画效果
     */
    public void showOrHideAnimation(Toolbar toolbar, DateTranBackTextView dateTranBackTextView) {
        //标题栏的动画
        setViewAnimation(toolbar, toolbar.getHeight(), VERTICAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        //底部的动画
        setViewAnimation(dateTranBackTextView, dateTranBackTextView.getWidth(), HORIZONTAL, isShow,
                BOTTOM_ANIMATION_DURATION);
        isShow = !isShow;
    }

    /**
     * 只是播放底部的动画
     */
    public void bottomAnimation(DateTranBackTextView dateTranBackTextView) {
        setViewAnimation(dateTranBackTextView, dateTranBackTextView.getWidth(), HORIZONTAL,
                false, BOTTOM_ANIMATION_DURATION);
        setViewAnimation(dateTranBackTextView, dateTranBackTextView.getWidth(), HORIZONTAL, true,
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
}
