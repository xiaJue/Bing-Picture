package com.as.xiajue.picturebing.ui.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.bean.DetailsItemData;
import com.as.xiajue.picturebing.utils.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

/**
 * Created by xiaJue on 2017/8/8.
 */

public class DetailsAdapter extends PagerAdapter {
    private List<DetailsItemData> mDataList;
    private List<View> mViewList;
    private Context mContext;

    public DetailsAdapter(Context context, List mDataList, List<View> mViewList) {
        L.e("initial  adapter...");
        this.mContext = context;
        this.mDataList = mDataList;
        this.mViewList = mViewList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //复用view
        final int i = position % 4;
        View view = mViewList.get(i);
        //添加到viewGroup
        container.addView(view);
        //设置图片
        final DetailsItemData data = mDataList.get(position);
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById
                (R.id.item_maxP_imageView);
        //加载图片
//        imageView.setImage(ImageSource.resource(R.drawable.anim_loading_view));
        imageView.setVisibility(View.GONE);
        Glide.with(mContext).load(data.getUrl()).asBitmap().skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                    glideAnimation) {
                imageView.setImage(ImageSource.bitmap(resource));
                imageView.setVisibility(View.VISIBLE);
            }
        });
        if (mOnClickListener != null) {
            imageView.setOnClickListener(mOnClickListener);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int i = position % 4;
        View view = mViewList.get(i);
        container.removeView(view);
    }

    /**
     * listener
     */
    public View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public static abstract class MaxOnPagerChangeListener implements ViewPager
            .OnPageChangeListener {
        public abstract void onChange(int position);

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            onChange(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
