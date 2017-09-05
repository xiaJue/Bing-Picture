package com.as.xiajue.picturebing.model.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.view.custom.DateTranBackTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

    public HomeRecyclerAdapter(Context context, List list) {
        this.mList = list;
        this.mContext = (AppCompatActivity) context;
        mInflater = LayoutInflater.from(mContext);
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        options=new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.item_image_drawable).showImageOnFail(R.mipmap.image_load_failure).
                bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).build();
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
        //获取图片--并缓存图片到内存和磁盘
        ImageLoader.getInstance().displayImage(data.getAbsUrl(),h.imageView,options);

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
