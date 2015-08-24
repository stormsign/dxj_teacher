package com.dxj.student.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by khb on 2015/8/19.
 */
public abstract class BaseListAdapter<T> extends android.widget.BaseAdapter {

    public List<T> list;
    public Context context;

    public BaseListAdapter(Context context, List<T> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent) ;
}
