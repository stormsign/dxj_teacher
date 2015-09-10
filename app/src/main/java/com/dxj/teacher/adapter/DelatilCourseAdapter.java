package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseRecyclerViewAdapter;
import com.dxj.teacher.bean.CourseSubjectBean;

import java.util.List;

/**
 * Created by khb on 2015/9/1.
 */
public class DelatilCourseAdapter extends BaseRecyclerViewAdapter<CourseSubjectBean> {


    public DelatilCourseAdapter(Activity mContext, List<CourseSubjectBean> mList) {
        super(mContext, mList);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DelatilCourseAdapter.DelatilCourseHolder(mLayoutInflater.inflate(R.layout.item_detail_course, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseSubjectBean courseBean = mList.get(position);
        if (null == courseBean) {
            return;
        }
        if (holder instanceof DelatilCourseAdapter.DelatilCourseHolder) {
            Log.i("TAG","CourseHolder="+courseBean.getRemark());
            DelatilCourseHolder courseHolder = (DelatilCourseHolder) holder;
            courseHolder.name.setText(courseBean.getSubjectName());
            courseHolder.tvCourse.setText(courseBean.getRemark());


        }

    }


    public class DelatilCourseHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView tvCourse;


        public DelatilCourseHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            tvCourse = (TextView) itemView.findViewById(R.id.tv_describe);

        }
    }

}

