package com.dxj.teacher.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.activity.LoginAndRightActivity;
import com.dxj.teacher.activity.SubjectCategoryActivity;
import com.dxj.teacher.activity.SubjectFirstCategoryActivity;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by kings on 9/18/2015.
 * 发现
 */
public class FindFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void initData() {

    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_find, null);
        TitleNavBar title = (TitleNavBar) view.findViewById(R.id.title);
        title.setTitleNoRightButton();
        title.setTitle("发现");
        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.cv_study).setOnClickListener(this);
        view.findViewById(R.id.cv_peer).setOnClickListener(this);
        view.findViewById(R.id.cv_share).setOnClickListener(this);
        view.findViewById(R.id.btn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
     int id =v.getId();
        switch (id){
            case R.id.cv_study:
                //学团
                break;
            case R.id.cv_peer:
                //同行风采
                getActivity().startActivity(new Intent(getActivity(), SubjectFirstCategoryActivity.class));
                break;
            case R.id.cv_share:
                //分享
                break;
            case R.id.btn:
                startActivity(new Intent(getActivity(), LoginAndRightActivity.class));
                break;
        }
    }
}
