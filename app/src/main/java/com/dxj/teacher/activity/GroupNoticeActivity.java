package com.dxj.teacher.activity;

import android.os.Bundle;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupNoticeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团公告");
        title.showActionOnly();
        title.setActionText("发布");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {}

            @Override
            public void onNavTwoClick() {}

            @Override
            public void onNavThreeClick() {}

            @Override
            public void onActionClick() {
                showToast("发布公告");
            }

            @Override
            public void onBackClick() {}
        });

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
