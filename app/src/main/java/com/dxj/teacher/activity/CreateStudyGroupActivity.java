package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/8/29.
 */
public class CreateStudyGroupActivity extends BaseActivity implements View.OnClickListener {

    private static final int RETURN_SUBJECT = 1;
    private ImageView groupHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createstudygroup);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("创建学团");
        title.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        groupHead = (ImageView) findViewById(R.id.iv_group_head);
        LinearLayout groupSubject = (LinearLayout) findViewById(R.id.ll_group_subject);
        groupSubject.setClickable(true);

        groupHead.setOnClickListener(this);
        groupSubject.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    public void createGroup(View view){
        showLogD(" ------- create group");
        create();
    }

    private void create() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == RETURN_SUBJECT){
                showToast("获取科目"+data.getStringExtra("subjectName"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_group_head:
                showLogD(" 上传群头像");
                uploadImg();
                break;
            case R.id.ll_group_subject:
                showLogD(" 科目");
                startActivityForResult(new Intent(context, SubjectListActivity.class), RETURN_SUBJECT);
                break;
        }

    }

    private void uploadImg() {

    }
}
