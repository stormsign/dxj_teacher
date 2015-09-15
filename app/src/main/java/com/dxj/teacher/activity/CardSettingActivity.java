package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by kings on 9/4/2015.
 */
public class CardSettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_card_setting);
        initTitle();
        initData();
        initView();

    }

    @Override
    public void initTitle() {
        TitleNavBar titleNavBar =(TitleNavBar)findViewById(R.id.title);
        titleNavBar.setTitle("认证设置");
        titleNavBar.setTitleNoRightButton();
    }

    @Override
    public void initView() {
     findViewById(R.id.linear_card).setOnClickListener(this);
        findViewById(R.id.linear_teacher_card).setOnClickListener(this);
        findViewById(R.id.linear_degrees_card).setOnClickListener(this);
//        findViewById(R.id.linear_education_card).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.linear_card:
              startActivity(new Intent(this,CardActvity.class));
              break;
          case R.id.linear_degrees_card:
              startActivity(new Intent(this,CardDegreesActvity.class));
              break;
//          case R.id.linear_education_card:
//              startActivity(new Intent(this,CardAptitudeActvity.class));
//              break;
          case R.id.linear_teacher_card:
              startActivity(new Intent(this,CardTeacherActvity.class));
              break;
      }
    }
}
