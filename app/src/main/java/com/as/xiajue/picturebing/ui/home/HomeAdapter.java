package com.as.xiajue.picturebing.ui.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.bean.HomeItemData;
import com.as.xiajue.picturebing.ui.custom.DateTranBackTextView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.as.xiajue.picturebing.bean.HomeItemData.getFormatDate;

/**
 * xiaJue 2018/4/14创建
 */
public class HomeAdapter extends BaseQuickAdapter<HomeItemData> {

    public HomeAdapter(Context context, int layoutResId, List<HomeItemData> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, HomeItemData homeItemData) {
        /**
         * h.textView.setText(data.getCopyright());
         h.textView.setDateText(getFormatDate(data.getEnddate()));
         h.imageView.setImageResource(R.drawable.item_image_drawable);
         */
        baseViewHolder.setText(R.id.item_home_tv, homeItemData.getCopyright());
        DateTranBackTextView textView = baseViewHolder.getView(R.id.item_home_tv);
        textView.setText(homeItemData.getCopyright());
        textView.setDateText(getFormatDate(homeItemData.getEnddate()));
        //image  wait
//        baseViewHolder.setImageResource(R.id.item_home_iv, R.drawable.item_image_drawable);
        final ImageView imageView = baseViewHolder.getView(R.id.item_home_iv);
        //set  image bitmap
//        setImage(homeItemData, imageView);
//        Picasso.with(mContext).setIndicatorsEnabled(true);//缓存调试
//        Picasso.with(mContext).load(homeItemData.getAbsUrl()).transform(new
//                CompressTransformation(3)).placeholder(R.drawable
//                .item_image_drawable).error(R.mipmap.fail).config(Bitmap.Config.RGB_565).into(imageView);
        Glide.with(mContext).load(homeItemData.getAbsUrl()).placeholder(R.drawable
                .item_image_drawable).error(R.mipmap.fail).into(imageView);
        //set click listener
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, baseViewHolder.getPosition());
            }
        });
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
