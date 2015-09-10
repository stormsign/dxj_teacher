package com.dxj.teacher.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kings on 9/10/2015.
 */
public class CircularProgressBar extends View {
    private int mDuration = 100;
    private int mProgress = 0;

    private Paint mPaint = new Paint();
    private Paint outPaint = new Paint();
    private RectF mRectF = new RectF();

    private int mBackgroundColor =Color.parseColor("#f0f0f0");
    private int mPrimaryColor = Color.parseColor("#ff71bdff");
    private int outPrimaryColor = Color.parseColor("#FF646464");
    private float mStrokeWidth = 10F;

    public interface OnProgressChangeListener {
        /**
         * @param duration 总长度
         * @param progress 当前进度
         * @param rate     //比
         */
        public void onChange(int duration, int progress, float rate);
    }

    /**
     * 设置进度条改变监听
     *
     * @param l
     */
    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        mOnChangeListener = l;
    }

    private OnProgressChangeListener mOnChangeListener;

    public CircularProgressBar(Context context) {
        super(context);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置进度条的最大值
     *
     * @param max
     */
    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        mDuration = max;
    }

    /**
     * 得到进度条的最大值
     *
     * @return
     */
    public int getMax() {
        return mDuration;
    }

    public void setProgress(int progress) {
        if (progress > mDuration) {
            progress = mDuration;
        }
        mProgress = progress;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange(mDuration, progress, getRateOfProgress());
        }
        invalidate();
    }

    /**
     * 得到进度条当前的值
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度条背景的颜色
     */
    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
    }

    /**
     * 设置进度条进度的颜色
     */
    public void setPrimaryColor(int color) {
        mPrimaryColor = color;
    }

    /**
     * 设置环形的宽度
     *
     * @param width
     */
    public void setCircleWidth(float width) {
        mStrokeWidth = width;

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        int radius = halfWidth < halfHeight ? halfWidth : halfHeight;
        float halfStrokeWidth = mStrokeWidth / 2;

        mPaint.setColor(mBackgroundColor);
        mPaint.setDither(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStyle(Paint.Style.STROKE);
        // 消除锯齿
        outPaint.setAntiAlias(true);
        // 设置画笔的颜色
        outPaint.setColor(outPrimaryColor);
        // 设置paint的外框宽度
        outPaint.setStrokeWidth(2);
        canvas.drawCircle(halfHeight, halfHeight, radius - halfStrokeWidth - 2, mPaint);
        canvas.drawCircle(halfHeight, halfHeight, (radius - halfStrokeWidth)-20, outPaint);
        mPaint.setColor(mPrimaryColor);
        mRectF.top = halfHeight - radius + halfStrokeWidth+2;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth-2;
        mRectF.left = halfWidth - radius + halfStrokeWidth+2;
        mRectF.right = halfWidth + radius - halfStrokeWidth-2;
        canvas.drawArc(mRectF, -90, getRateOfProgress() * 360, false, mPaint);
        canvas.save();
    }

    /**
     * 得到当前的进度的比率
     * 用进度条当前的值 与 进度条的最大值求商
     *
     * @return
     */
    private float getRateOfProgress() {
        return (float) mProgress / mDuration;
    }
}
