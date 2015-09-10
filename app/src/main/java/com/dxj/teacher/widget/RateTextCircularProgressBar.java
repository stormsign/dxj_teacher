package com.dxj.teacher.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.utils.MyUtils;

/**
 * Created by kings on 9/10/2015.
 */
public class RateTextCircularProgressBar extends FrameLayout implements CircularProgressBar.OnProgressChangeListener {

    private CircularProgressBar mCircularProgressBar;
    private TextView mRateText;
    private ImageView mRateImage;

    public RateTextCircularProgressBar(Context context) {
        super(context);
        init();
    }

    public RateTextCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCircularProgressBar = new CircularProgressBar(getContext());
        this.addView(mCircularProgressBar);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        mCircularProgressBar.setLayoutParams(lp);

        mRateText = new TextView(getContext());
        this.addView(mRateText);
        mRateText.setLayoutParams(lp);
        mRateText.setGravity(Gravity.CENTER);
        mRateText.setTextColor(Color.BLACK);
        mRateText.setTextSize(20);
        mRateImage = new ImageView(getContext());
        this.addView(mRateImage);
        mRateImage.setLayoutParams(lp);
        mRateText.setGravity(Gravity.CENTER);
        mRateImage.setPadding(20, 20, 20, 20);
        mRateImage.setMaxHeight(MyUtils.dip2px(getContext(),70));
        mRateImage.setMaxWidth(MyUtils.dip2px(getContext(),70));
        mRateImage.setImageResource(R.mipmap.geren_yuyin_kaishi);
        mCircularProgressBar.setOnProgressChangeListener(this);
    }
 public void isShow(){
     mRateImage.setVisibility(View.GONE);
 }
    public void isShowIMage(){
        mRateImage.setVisibility(View.VISIBLE);
    }
    /**
     * 设置最大值
     */
    public void setMax( int max ) {
        mCircularProgressBar.setMax(max);
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
        mCircularProgressBar.setProgress(progress);
    }

    /**
     * 得到 CircularProgressBar 对象，用来设置其他的一些属性
     * @return
     */
    public CircularProgressBar getCircularProgressBar() {
        return mCircularProgressBar;
    }

    /**
     * 设置中间进度百分比文字的尺寸
     * @param size
     */
    public void setTextSize(float size) {
        mRateText.setTextSize(size);
    }

    /**
     * 设置中间进度百分比文字的颜色
     * @param color
     */
    public void setTextColor( int color) {
        mRateText.setTextColor(color);
    }

    @Override
    public void onChange(int duration, int progress, float rate) {
        mRateText.setText(progress+ "s");
    }

}