package com.dxj.teacher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dxj.teacher.R;

import java.util.List;

/**
 * Created by khb on 2015/8/27.
 */
public class ViewPagerIndicator extends LinearLayout {

    /**
     * 绘制三角形的画笔
     */
    private Paint mPaint;
    /**
     * path构成一个三角形
     */
    private Path mPath;
    /**
     * 三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 三角形高度
     */
    private int mTriangleHeight;
    /**
     * 三角形的宽度为单个tab的1/6
     */
    private static final float RATIO_TRIANGLE = 1.0f/6;
    /**
     * 三角形最大宽度
     */
    private int DIMENSION_TRIANGLE_WIDTH = (int) (getScreenWidth()/3*RATIO_TRIANGLE);
    /**
     * 初始时，三角形指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;

    /**
     * 默认的Tab数量
     */
    private static final int COUNT_DEFAULT_TAB = 4;
    /**
     * tab数量
     */
    private int mTabVisibleCount = COUNT_DEFAULT_TAB;
    /**
     * tab上的内容
     */
    private List<String> mTabTitles;
    /**
     * 与之绑定的ViewPager
     */
    public ViewPager mViewPager;
    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;

    public ViewPagerIndicator(Context context){
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount = mTypedArray.getInt(R.styleable.ViewPagerIndicator_item_count,
                COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0){
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }
        mTypedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count == 0){
            return ;
        }
        //设置每个tab的宽度
        for (int i = 0;i<count;i++){
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.width = getScreenWidth()/mTabVisibleCount;
            view.setLayoutParams(params);
        }
        //设置点击事件
        setItemClickEvent();
    }

    /**
     * 初始化三角形宽度
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w/mTabVisibleCount*RATIO_TRIANGLE);
        mTriangleWidth = Math.min(DIMENSION_TRIANGLE_WIDTH, mTriangleWidth);
        initTriangle();
//        初始化三角形的偏移量
        mInitTranslationX = getWidth()/mTabVisibleCount/2 - mTriangleWidth/2;
    }

    /**
     * 初始化三角形指示器
     */
    private void initTriangle() {
        mPath = new Path();
        mTriangleHeight = (int) (mTriangleWidth/2/Math.sqrt(2));    //三角形的高度
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.close();
    }

    //    点击切换pager
    private void setItemClickEvent() {
        int count = getChildCount();
        for (int i = 0; i<count; i++){
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

//    绘制指示器
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    public void setViewPager(ViewPager viewPager, int position){
        this.mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (myOnPageChangeListener != null) {
                    myOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                设置字体颜色高亮
                resetTextViewColor();
                highListTextView(position);
                if (myOnPageChangeListener != null) {
                    myOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int position) {
                if (myOnPageChangeListener != null) {
                    myOnPageChangeListener.onPageScrollStateChanged(position);
                }
            }
        });
        mViewPager.setCurrentItem(position);
        highListTextView(position);

    }
//    重置文本颜色
    private void resetTextViewColor() {
        for (int i = 0; i<getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof TextView){
                TextView tv = (TextView)view;
                tv.setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }
//    高亮文本颜色
    private void highListTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView){
            TextView tv = (TextView)view;
            tv.setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
        }
    }
//    指示器跟随手指滚动，以及容器滚动
    private void scroll(int position, float positionOffset) {
//        指针随着手指移动的偏移量
        mTranslationX = getWidth()/mTabVisibleCount*(position+positionOffset);
        int tabWidth = getScreenWidth()/mTabVisibleCount;
        if (positionOffset>0 //指针已经移动
                && position>=(mTabVisibleCount-2)   //指针移到倒数第二个
                && getChildCount()>mTabVisibleCount //tab总数量大于可显示的的tab数
                ){
            if (mTabVisibleCount > 1){
                this.scrollTo(
                        (position-(mTabVisibleCount-2))*tabWidth+(int)(tabWidth*positionOffset),    //容器是反方向移动
                        0
                );
            }else{  //tab数为1时
                this.scrollTo(
                        position*tabWidth+(int)(tabWidth*positionOffset),
                        0
                );
            }
        }
        invalidate();

    }

    public  interface MyOnPageChangeListener{
        public void onPageScrolled(int position, float positionOffset, int positinoOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int position);
    }
    private MyOnPageChangeListener myOnPageChangeListener;
    public void setMyOnPageChangeListener(MyOnPageChangeListener myOnPageChangeListener){
        this.myOnPageChangeListener = myOnPageChangeListener;
    }

    public  void setTabVisibleCount(int count){
        this.mTabVisibleCount = count;
    }

    public  void setTabItemTitles(List<String> list){
        if (!list.isEmpty()){
            this.removeAllViews();
            this.mTabTitles = list;
            for (String title : list){
                addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }

    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = getScreenWidth() / mTabVisibleCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(params);
        return tv;
    }


    /**
    * 获得屏幕的宽度
    *
            * @return
            */
    public int getScreenWidth()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}


































