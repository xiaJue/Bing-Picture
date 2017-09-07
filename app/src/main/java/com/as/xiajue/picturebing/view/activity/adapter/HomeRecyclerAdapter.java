package com.as.xiajue.picturebing.view.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.manager.MemoryCacheManager;
import com.as.xiajue.picturebing.model.utils.L;
import com.as.xiajue.picturebing.model.utils.ScreenUtils;
import com.as.xiajue.picturebing.view.custom.DateTranBackTextView;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import static com.as.xiajue.picturebing.model.bean.HomeItemData.getFormatDate;

/**
 * Created by xiaJue on 2017/7/30.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter {
    private List mList;
    private AppCompatActivity mContext;
    private LayoutInflater mInflater;
    private DisplayImageOptions options;
    private int mScreeWidth;
    private int mItemCount;
    private int mSpace;

    public HomeRecyclerAdapter(Context context, List list) {
        this.mList = list;
        this.mContext = (AppCompatActivity) context;
        mInflater = LayoutInflater.from(mContext);
        //条目宽高--
        mScreeWidth = ScreenUtils.getScreenWidth(mContext);
        mItemCount = Const.ITEM_LIN_COUNT;
        mSpace = Const.itemSpace;
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.item_image_drawable).showImageOnFail(R.mipmap.image_load_failure).
                bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true).
                displayer(new RoundedVignetteBitmapDisplayer(Const.ITEM_IMAGE_ROUND_SIZE,Const.ITEM_IMAGE_ROUND_SIZE)).build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home, parent, false);
        RecyclerView.ViewHolder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //条目的基本配置
        final HomeItemData data = (HomeItemData) mList.get(position);
        final Holder h = (Holder) holder;
        h.textView.setText(data.getCopyright());
        h.textView.setDateText(getFormatDate(data.getEnddate()));
        h.imageView.setImageResource(R.drawable.item_image_drawable);
        //获取图片--并缓存图片到内存和磁盘

        Bitmap bitmap = MemoryCacheManager.getInstance(mContext).getCache(data.getAbsUrl());
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage(data.getAbsUrl(), h.imageView, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            super.onLoadingStarted(imageUri, view);
                            h.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            //隐藏进度条
                            h.progressBar.setVisibility(View.GONE);
                            //设置图片宽高
                            setImageViewSize(loadedImage, h);
                            //渐变显示图片
                            if (loadedImage != null) {
                                    ImageView imageView = (ImageView) view;
                                    FadeInBitmapDisplayer.animate(imageView, Const.ITEM_IMAGE_ANIMATE_DURATION_MILLIS);
                                }
                            MemoryCacheManager.getInstance(mContext).addCache(data.getAbsUrl(),loadedImage);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String s, View view, int i, int max) {
                            //更新进度条
                            h.progressBar.setProgress((int) (i*1.0 / max * 100));
                        }
                    });
        }else{
            h.progressBar.setVisibility(View.GONE);
            h.imageView.setImageBitmap(bitmap);
        }

        /***设置条目点击事件**/
        if (itemClickListener != null) {
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    /**
     * 设置imageView的宽高
     */
    private void setImageViewSize(Bitmap loadedImage, Holder h) {
        int bitWidth = loadedImage.getWidth();
        int bitHeight = loadedImage.getHeight();
        float viewHeight = bitHeight * ((float) getItemWidth() / bitWidth);
        h.imageView.getLayoutParams().width = getItemWidth();
        h.imageView.getLayoutParams().height = (int) viewHeight;
    }

    /**
     * 获得一个item的宽度
     */
    public int getItemWidth() {
        return mScreeWidth / mItemCount - mSpace * 2;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        public DateTranBackTextView textView;
        public ImageView imageView;
        public CircleProgressBar progressBar;

        public Holder(View itemView) {
            super(itemView);
            textView = (DateTranBackTextView) itemView.findViewById(R.id.item_home_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_home_iv);
            progressBar = (CircleProgressBar) itemView.findViewById(R.id.item_home_progress);
        }
    }

    private OnItemClickListener itemClickListener;

    /**
     * 设置条目点击事件
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 条目点击事件的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View item, int position);
    }
}
