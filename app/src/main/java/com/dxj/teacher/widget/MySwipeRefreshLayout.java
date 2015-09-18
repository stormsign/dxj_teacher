package com.dxj.teacher.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;

import com.dxj.teacher.R;

/**
 * Created by khb on 2015/9/18.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    /**
     * 滑动到最下面时的上拉操作
     */
    private int mTouchSlop;
    /**
     * listview实例
     */
    private RecyclerView mListView;
    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;
    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;
    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    private int diffY;

    public MySwipeRefreshLayout(Context context) {
        super(context, null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.loadingview_footer, null,
                false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = null;
            for (int i = 0; i< childs; i++){
                childView = getChildAt(i);
                if (childView instanceof RecyclerView) {
                    break;
                }
            }
            if (childView instanceof RecyclerView) {
                mListView = (RecyclerView) childView;

                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        diffY = dy;
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        // 滚动时到了最底部也可以加载更多
                        if (canLoad()) {
                            loadData();
                        }
                    }
                });
                Log.d(VIEW_LOG_TAG, "### 找到listview");
            }
        }
    }


    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom()
                && !isLoading
                && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    public boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return (((LinearLayoutManager)mListView.getLayoutManager()).findLastVisibleItemPosition() == (mListView.getAdapter().getItemCount() - 1))
                    && (((LinearLayoutManager)mListView.getLayoutManager()).findLastVisibleItemPosition() - ((LinearLayoutManager)mListView.getLayoutManager()).findFirstVisibleItemPosition()
                        < mListView.getAdapter().getItemCount()-1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return diffY>0;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
//            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
//            mListView.getAdapter()addvFooterView(mListViewFooter);
        } else {
//            mListView.removeFooterView(mListViewFooter);
//            mYDown = 0;
//            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }

}
