package com.as.xiajue.picturebing.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.MaxPicItemData;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

/**
 * Created by Moing_Admin on 2017/8/8.
 */

public class MaxPicAdapter extends PagerAdapter {
    private List<MaxPicItemData> mDataList;
    private List<View> mViewList;

    public MaxPicAdapter(List mDataList, List<View> mViewList) {
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
        int i = position % 4;
        View view = mViewList.get(i);
        //添加到viewGroup
        container.addView(view);
        //设置图片
        MaxPicItemData data = mDataList.get(position);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById
                (R.id.item_maxP_imageView);
        //主要图片
        imageView.setImage(ImageSource.uri(data.getUri()));
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
    public View.OnClickListener mOnClickListener;
    public void setOnClickListener(View.OnClickListener onClickListener){
        this.mOnClickListener=onClickListener;
    }
//    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
//        this.mItemClickListener = itemClickListener;
//    }
//
//    private OnItemClickListener mItemClickListener;
//
//    /**
//     * 设置条目点击事件
//     */
//    public static interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }

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
