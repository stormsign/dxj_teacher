package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dxj.teacher.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class NoticeAdapter extends BaseRecyclerViewAdapter {

    public NoticeAdapter(Activity mContext, List mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
