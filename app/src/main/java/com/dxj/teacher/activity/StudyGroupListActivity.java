package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/8/31.
 */
public class StudyGroupListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studygruoplist);
        initTitle();
        initView();
        initData();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("交友学团");
        title.showSearchBar(true);
        title.setSearchHint("输入老师或学团名称");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {
                ToastUtils.showToast(context,"创建");
                startActivity(new Intent(context, CreateStudyGroupActivity.class));
            }

            @Override
            public void onBackClick() {

            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
