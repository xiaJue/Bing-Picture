package com.as.xiajue.picturebing.model.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.as.xiajue.picturebing.utils.L;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/8.
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
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById
                (R.id.item_maxP_imageView);
        //memory cache image
        L.e("load image");
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(data.getUrl());
        if (bitmap != null) {
            imageView.setImage(ImageSource.bitmap(bitmap));
            L.e("memory");
        }else{
            //disk cache image
            File file = ImageLoader.getInstance().getDiskCache().get(data.getUrl());
            if (file.exists()) {
                L.e("disk");
                imageView.setImage(ImageSource.uri(Uri.fromFile(file)));
            }
        }
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
