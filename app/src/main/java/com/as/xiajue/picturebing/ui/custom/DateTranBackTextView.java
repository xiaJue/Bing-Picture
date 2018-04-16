package com.as.xiajue.picturebing.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.utils.DensityUtils;

/**
 * Created by Moing_Admin on 2017/7/30.
 */

public class DateTranBackTextView extends TranBackTextView {
    private static final int DATE_TEXT_SIZE = 15;
    private int DATE_COLOR_INIT = Color.WHITE;
    private int mDateTextColor = DATE_COLOR_INIT;
    private String mDateText = "1997-11-10";
    private int mDateTextSize;
    private Paint mPaint;
    private int mDateTextWidth;
    private boolean mDateTextRight;
    private boolean mDateTextLeft;

    public DateTranBackTextView(Context context) {
        this(context, null);
    }

    public DateTranBackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTranBackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .DateTranBackTextView, defStyleAttr, 0);
        mDateText = ta.getString(R.styleable.DateTranBackTextView_dateText);
        mDateTextColor = ta.getColor(R.styleable.DateTranBackTextView_dateTextColor,
                DATE_COLOR_INIT);
        mDateTextSize = ta.getDimensionPixelSize(R.styleable.DateTranBackTextView_dateTextSize,
                DATE_TEXT_SIZE);
        mDateTextRight = ta.getBoolean(R.styleable.DateTranBackTextView_dateTextRight, true);
        mDateTextLeft = ta.getBoolean(R.styleable.DateTranBackTextView_dateTextLeft, false);
        //初始化一些工具
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(mDateTextSize);
        mPaint.setColor(mDateTextColor);
        mDateTextWidth = (int) mPaint.measureText(mDateText, 0, mDateText.length());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDateText(canvas);
    }

    /**
     * 绘制时间
     *
     * @param canvas
     */
    private void drawDateText(Canvas canvas) {
        canvas.save();
        int mbSize = DensityUtils.dp2px(getContext(), 5);
        if (mDateTextLeft) {
            canvas.translate(getPaddingLeft(), getMeasuredHeight() - mbSize);
        } else if (mDateTextRight) {
            canvas.translate(getMeasuredWidth() - mDateTextWidth - getPaddingRight(),
                    getMeasuredHeight() - mbSize);
        }
        canvas.drawText(mDateText, 0, 0, mPaint);
        canvas.restore();
    }

    /**
     * 设置时间文字
     *
     * @param mDateText
     */
    public void setDateText(String mDateText) {
        this.mDateText = mDateText;
        invalidate();
    }

    /**
     * 设置时间文字大小
     *
     * @param dateTextSize
     */
    public void setDateTextSize(int dateTextSize) {
        mDateTextSize = dateTextSize;
        invalidate();
    }

    /**
     * 设置时间文字颜色
     *
     * @param mDateTextColor
     */
    public void setDateTextColor(int mDateTextColor) {
        this.mDateTextColor = mDateTextColor;
        invalidate();
    }
}
