package com.as.xiajue.picturebing.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.as.xiajue.picturebing.NetUtils.HttpUtils;
import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.cache.CacheUtils;
import com.as.xiajue.picturebing.model.HomeItemData;
import com.as.xiajue.picturebing.utils.ScreenUtils;
import com.as.xiajue.picturebing.view.DateTranBackTextView;

import java.util.List;

import static com.as.xiajue.picturebing.model.HomeItemData.getFormatDate;

/**
 * Created by xiaJue on 2017/7/30.
 */
public class HomeAdapter extends RecyclerView.Adapter {
    private List mList;
    private AppCompatActivity mContext;
    private LayoutInflater mInflater;
    private int mPinMuWidth;
    private int mItemCount;
    private int mSpace;
    private CacheUtils mCacheUtils;//缓存图片工具类
    private HttpUtils mHttpUtils;

    public HomeAdapter(Context context, List list, int itemCount, int Space) {
        this.mList = list;
        this.mContext = (AppCompatActivity) context;
        this.mItemCount = itemCount;
        this.mSpace = Space;
        mInflater = LayoutInflater.from(mContext);
        mPinMuWidth = ScreenUtils.getScreenWidth(mContext);
        mCacheUtils = new CacheUtils(mContext);
        mCacheUtils.initialMemoryCache();//初始化内存缓存
        mCacheUtils.initialSdCache();//初始化本地缓存
        mHttpUtils = HttpUtils.getInstance(mContext);//单例模式。所以无限new不用管资源问题
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
        h.countTv.setText(String.valueOf(position + 1));
        h.imageView.setImageResource(R.drawable.item_image_drawable);
        //对图片的相关操作
        loadImage(data, h);
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
     * 加载bitmap图片
     *
     * @param data
     * @param h
     */
    private void loadImage(final HomeItemData data, final Holder h) {
        mHttpUtils.getBitmap(data.getAbsUrl(), getItemWidth(), new HttpUtils
                .LoadCompressBitmapCallback() {
            @Override
            public void finish(boolean isSuccess, final Bitmap bitmap) {
                if (isSuccess) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 运行在UI线程
                             */
                            setViewHeight(h, bitmap);
                        }
                    });
                }
            }
        });
    }

    /**
     * 设置view的高度并设置到imageVew
     *
     * @param holder
     * @param imageBitmap
     */
    private void setViewHeight(Holder holder, Bitmap imageBitmap) {
        int bitWidth = imageBitmap.getWidth();
        int bitHeight = imageBitmap.getHeight();
        float viewHeight = bitHeight * ((float) getItemWidth() / bitWidth);
        holder.itemView.getLayoutParams().width = getItemWidth();
        holder.itemView.getLayoutParams().height = (int) viewHeight;
        holder.imageView.setImageBitmap(imageBitmap);
        holder.countTv.setVisibility(View.GONE);
    }

    /**
     * 获得一个Item的宽度
     *
     * @return
     */
    public int getItemWidth() {
        return mPinMuWidth / mItemCount - mSpace * 2;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        public DateTranBackTextView textView;
        public ImageView imageView;
        private TextView countTv;

        public Holder(View itemView) {
            super(itemView);
            textView = (DateTranBackTextView) itemView.findViewById(R.id.item_home_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_home_iv);
            countTv = (TextView) itemView.findViewById(R.id.item_home_countTv);
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
