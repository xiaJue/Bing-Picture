package com.as.xiajue.picturebing.view.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.MaxPicItemData;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by xiaJue on 2017/8/8.
 */

public class MaxPicAdapter extends PagerAdapter {
    private List<MaxPicItemData> mDataList;
    private List<View> mViewList;
    private Context mContext;

    public MaxPicAdapter(Context context,List mDataList, List<View> mViewList) {
        this.mContext=context;
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
        final MaxPicItemData data = mDataList.get(position);
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById
                (R.id.item_maxP_imageView);
            ImageLoader.getInstance().loadImage(data.getUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImage(ImageSource.bitmap(loadedImage));
                    if (loadedImage != null) {
                        FadeInBitmapDisplayer.animate(imageView, Const.ITEM_IMAGE_ANIMATE_DURATION_MILLIS);
                    }
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
