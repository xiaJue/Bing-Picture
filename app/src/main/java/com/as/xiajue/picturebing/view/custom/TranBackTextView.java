package com.as.xiajue.picturebing.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;

/**
 * Created by Moing_Admin on 2017/7/30.
 */

public class TranBackTextView extends TextView {
    private int COLOR_INIT = Color.parseColor("#55D3918E");
    protected int mBgColor = COLOR_INIT;

    public TranBackTextView(Context context) {
        this(context, null);
    }

    public TranBackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranBackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .TranBackTextView, defStyleAttr, 0);
        mBgColor = ta.getColor(R.styleable.TranBackTextView_backColor, COLOR_INIT);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBgColor);
        super.onDraw(canvas);
    }

}
