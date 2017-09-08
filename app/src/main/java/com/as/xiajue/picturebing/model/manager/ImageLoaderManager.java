package com.as.xiajue.picturebing.model.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * xiaJue 2017/9/8创建
 */
public class ImageLoaderManager {
    public static ImageLoaderManager mImageLoaderManager;
    public Context mContext;
    public static ImageLoaderManager getInstance(Context context) {
        if (mImageLoaderManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (mImageLoaderManager == null) {
                    mImageLoaderManager = new ImageLoaderManager(context);
                }
            }
        }
        return mImageLoaderManager;
    }

    public ImageLoaderManager(Context context) {
        mContext=context;
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.item_image_drawable).showImageOnFail(R.mipmap.image_load_failure).
                bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).
                displayer(new RoundedVignetteBitmapDisplayer(Const.ITEM_IMAGE_ROUND_SIZE, Const.ITEM_IMAGE_ROUND_SIZE)).build();
    }

    /**
     * 从内存中获取图片
     * @param url key
     * @return bitmap
     */
    public Bitmap getMemoryCache(String url) {
        return MemoryCacheManager.getInstance(mContext).getCache(url);
    }

    /**
     * 添加图片缓存到内存
     * @param url key
     * @param bitmap bitmap
     */
    public void addMemoryCache(String url, Bitmap bitmap) {
        MemoryCacheManager.getInstance(mContext).addCache(url,bitmap);
    }
    private DisplayImageOptions options;
    public void displayImage(ImageView imageView, String url, final ImageLoaderListener imageLoaderListener) {
        ImageLoader.getInstance().displayImage(url,imageView,options,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        imageLoaderListener.onLoadingStart(imageUri, view);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        imageLoaderListener.onLoadingComplete(imageUri,view,loadedImage);
                    }
                },
            new ImageLoadingProgressListener(){
                @Override
                public void onProgressUpdate(String s, View view, int i, int i1) {
                    imageLoaderListener.onProgressUpdate(s,view,i,i1);
                }
            });
    }

    /**
     * 加载图片
     */
    public void loadImage(String url, final View imageView) {
        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(imageView instanceof SubsamplingScaleImageView){
                    ((SubsamplingScaleImageView)imageView).setImage(ImageSource.bitmap(loadedImage));
                } else if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageBitmap(loadedImage);
                }

                if (loadedImage != null) {
                    FadeInBitmapDisplayer.animate(imageView, Const.ITEM_IMAGE_ANIMATE_DURATION_MILLIS);
                }
            }
        });
    }

    public interface ImageLoaderListener{
        void onLoadingStart(String imageUri, View view);

        void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

        void onProgressUpdate(String s, View view, int i, int max);
    }
}
