package com.as.xiajue.picturebing.model.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaJue on 2017/7/31.
 * RecycleView 的条目间距
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView
            .State state) {
        outRect.top = space;
        outRect.right = space;
        outRect.left = space;
    }
}