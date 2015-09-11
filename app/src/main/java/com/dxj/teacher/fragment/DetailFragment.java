package com.dxj.teacher.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.bean.CourseSubjectBean;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.bean.TeacherInfoBean;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.widget.CheckableButton;
import com.dxj.teacher.widget.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/8/19.
 */
public class DetailFragment extends BaseFragment {
//    private TextView tvName;
//    private ImageView imgAvatar;
//    private TextView tvRecomment;//介绍
    private TextView tvAddressValue;//地址
    private TextView tvConstellation;//星座
    private TextView tvTeachAge;//教龄
    private TextView tvSchool;//学校
    private TextView tvMajor;//专业
    private TextView tvTeachCity;//授课区域
    private TextView tvTeachType;//授课方式
    private TextView tvExperience;//授课方式
    private TextView tvResult;//授课方式
    private FlowLayout alreadyFlowlayout;
    private FlowLayout flowlayoutSolveLabel;
    private LinearLayout linearPhoto;
    private LinearLayout linearStudy;
    private LinearLayout linearSubject;
    private TeacherInfoBean teacher;
    @Override
    public void initData() {
        teacher =(TeacherInfoBean)getArguments().getSerializable("teacherInfoBean");
        fillUi(teacher.getUserInfo());
        addStude(teacher.getGroup());
        addSubject(teacher.getGoodSubject());
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_home_details, null);
//        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvAddressValue = (TextView) view.findViewById(R.id.tv_address_value);
        tvConstellation = (TextView) view.findViewById(R.id.tv_constellation_value);
//        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
//        tvRecomment = (TextView) view.findViewById(R.id.tv_recomment);
        tvTeachAge = (TextView) view.findViewById(R.id.tv_teachage_value);
        tvMajor = (TextView) view.findViewById(R.id.tv_major_value);
        tvTeachType = (TextView) view.findViewById(R.id.tv_teach_type_value);
        tvTeachCity = (TextView) view.findViewById(R.id.tv_teach_city_value);
        tvSchool = (TextView) view.findViewById(R.id.tv_school);
        tvExperience = (TextView) view.findViewById(R.id.tv_experience);
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        linearPhoto = (LinearLayout) view.findViewById(R.id.linear_photo);
        linearStudy = (LinearLayout) view.findViewById(R.id.linear_study);
        linearSubject = (LinearLayout) view.findViewById(R.id.linear_subject);
        alreadyFlowlayout = (FlowLayout) view.findViewById(R.id.already_flowlayout);
        flowlayoutSolveLabel = (FlowLayout) view.findViewById(R.id.flowlayout_solve_label);
//        sendRequestData();
        return view;
    }

    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE_COMMON + HttpUtils.TEACHERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        GsonRequest<TeacherInfoBean> teacherinfo = new GsonRequest(Request.Method.POST, urlPath, TeacherInfoBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(teacherinfo);
    }

    private Response.Listener<TeacherInfoBean> getListener() {
        return new Response.Listener<TeacherInfoBean>() {
            @Override
            public void onResponse(TeacherInfoBean str) {
                Log.i("TAG", "str=" + str.getUserInfo().getHeadUrl());
                UserBean.UserInfo userInfo = str.getUserInfo();
                fillUi(userInfo);
                addStude(str.getGroup());
                addSubject(str.getGoodSubject());
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        };
    }

    private void fillUi(UserBean.UserInfo userInfo) {
//        tvName.setText(userInfo.getNickName());
//        Glide.with(getActivity()).load(userInfo.getHeadUrl()).centerCrop().into(imgAvatar);
//        tvRecomment.setText(userInfo.getRemark());
        tvAddressValue.setText(userInfo.getLivingCity());
        tvConstellation.setText(userInfo.getHoroscope());
        tvSchool.setText(userInfo.getSchool());
        tvTeachAge.setText(userInfo.getSchoolAge() + "年");
        tvMajor.setText(userInfo.getMajor());
        tvTeachCity.setText(userInfo.getLivingCity());
        tvExperience.setText(userInfo.getExperience());
        tvResult.setText(userInfo.getResult());
        addChildTo(userInfo.getLabel());
        addChildToTwo(userInfo.getSolveLabel());
        showPhoto(userInfo.getImages());
    }

    private void addStude(ArrayList<StudyGroup> list) {
        for (int i = 0; i < list.size(); i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_group, null);
            ImageView groupHead = (ImageView) view.findViewById(R.id.group_head);
            TextView groupName = (TextView) view.findViewById(R.id.group_name);
            groupName.setText(list.get(i).getSubjectName());
            Glide.with(getActivity()).load(list.get(i).getHeadUrl()).centerCrop().into(groupHead);
            linearStudy.addView(view);
        }
    }

    private void addSubject(ArrayList<CourseSubjectBean> list) {
        int size = list.size();
        if (size > 3)
            size = 3;

        for (int i = 0; i < size; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_detail_course, null);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            TextView price = (TextView) view.findViewById(R.id.tv_price);
            TextView describe = (TextView) view.findViewById(R.id.tv_describe);
            name.setText(list.get(i).getSubjectName());
            describe.setText(list.get(i).getRemark());
            linearSubject.addView(view);
        }
        if (list.size() > 3) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER);
            ;
            textView.setText("全部");
            textView.setTextSize(15);
            linearSubject.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void addChildTo(List<String> lists) {
        Log.i("TAG", "lists");
        for (int i = 0; i < lists.size(); i++) {
            CheckableButton btn = new CheckableButton(getActivity());
            btn.setHeight(MyUtils.dip2px(getActivity(), 22));
            btn.setTextSize(10);
            btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            StringBuilder sb = new StringBuilder();
            sb.append(lists.get(i));
            btn.setText(sb.toString());
            alreadyFlowlayout.addView(btn);
            btn.setTag(i);
//            btn.setOnCheckedChangeWidgetListener(this);


        }
    }

    private void addChildToTwo(List<String> lists) {
        Log.i("TAG", "lists");
        for (int i = 0; i < lists.size(); i++) {
            CheckableButton btn = new CheckableButton(getActivity());
            btn.setHeight(MyUtils.dip2px(getActivity(), 22));
            btn.setTextSize(10);
            btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            StringBuilder sb = new StringBuilder();
            sb.append(lists.get(i));
            btn.setText(sb.toString());
            flowlayoutSolveLabel.addView(btn);
            btn.setTag(i);
//            btn.setOnCheckedChangeWidgetListener(this);
        }
    }

    private void showPhoto(List<String> lists) {
        int count = lists.size();
        for (int i = 0; i < count; i++) {
            final ImageView pic = (ImageView) linearPhoto.getChildAt(i);
            if (lists.get(i) != null) {
                Glide.with(getActivity()).load(lists.get(i)).centerCrop().into(pic);
            }
        }
        if (count <= 4) {
            switch (count) {
                case 0:
                    linearPhoto.setVisibility(View.GONE);
                    break;
                case 1:
                    break;
            }
        }
    }
}