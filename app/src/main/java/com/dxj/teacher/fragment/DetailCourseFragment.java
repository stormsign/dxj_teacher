package com.dxj.teacher.fragment;

import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.adapter.CourseAdapter;
import com.dxj.teacher.adapter.DelatilAdapter;
import com.dxj.teacher.adapter.DelatilCourseAdapter;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.bean.CourseSubjectBean;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.dialogplus.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/19.
 */
public class DetailCourseFragment extends BaseFragment {
    private LinearLayout linearCourse;
    //    private DelatilCourseAdapter courseAdapter;
    private DelatilAdapter courseAdapter;
    private ArrayList<CourseSubjectBean> lists = new ArrayList<>();

    @Override
    public void initData() {
        lists = (ArrayList<CourseSubjectBean>) getArguments().getSerializable("course");
//        Log.i("TAG", "listsSSS=" + lists.size());
//        courseAdapter = new DelatilAdapter(getActivity(),lists);
//        recyclerViewCourse.setAdapter(courseAdapter);
        addSubject(lists);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_course, null);
        linearCourse = (LinearLayout) view.findViewById(R.id.linear_subject);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            recyclerViewCourse.setNestedScrollingEnabled(true);
//        }
        return view;
    }

    private void addSubject(ArrayList<CourseSubjectBean> list) {
        int size = list.size();


        for (int i = 0; i < size; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_detail_course, null);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            TextView price = (TextView) view.findViewById(R.id.tv_price);
            TextView describe = (TextView) view.findViewById(R.id.tv_describe);
            name.setText(list.get(i).getSubjectName());
            describe.setText(list.get(i).getRemark());
            linearCourse.addView(view);
        }

    }

}
