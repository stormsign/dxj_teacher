package com.dxj.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseListAdapter;
import com.dxj.teacher.bean.UniversityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 8/30/2015.
 */
public class ResultListAdapter extends BaseListAdapter {
    private LayoutInflater inflater;
    private List<UniversityBean> results = new ArrayList<>();
    public ResultListAdapter(Context context, List<UniversityBean> list) {
        super(context, list);
        inflater = LayoutInflater.from(context);
        this.results = list;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(results.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}

