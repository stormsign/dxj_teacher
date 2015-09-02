package com.dxj.teacher.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.bean.StudyGroupBean;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2015/9/1.
 */
public class StudyGroupDetailActivity extends BaseActivity {

    private ImageView group_head;
    private TextView group_name;
    private TextView description;
    private TextView announce;
    private ImageView leader_head;
    private TextView leader_name;
    private TextView leader_school;
    private StudyGroup studyGroup;
    private TitleNavBar title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studygroupdetail);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        title = (TitleNavBar) findViewById(R.id.title);
        title.showNavOne(false);
        title.setNavTwoImageResource(R.mipmap.ic_settings_white_24dp);
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {
                showToast("设置");

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onBackClick() {

            }
        });

    }

    @Override
    public void initView() {
        group_head = (ImageView) findViewById(R.id.iv_group_head);
        group_name = (TextView) findViewById(R.id.tv_groupname);
        description = (TextView) findViewById(R.id.tv_description);
        announce = (TextView) findViewById(R.id.tv_announce);
        leader_head = (ImageView) findViewById(R.id.iv_leader_head);
        leader_name = (TextView) findViewById(R.id.tv_leader_name);
        leader_school = (TextView) findViewById(R.id.tv_leader_school);


    }

    @Override

    public void initData() {
        getIntent();
        getGroupDetail();

    }

    private void getGroupDetail() {
        String url = FinalData.URL_VALUE_COMMON+"getGroupDetail";
        Map<String, Object> map = new HashMap<String, Object>();
        String groupid = "e265954a-1927-49ff-a92c-ac8d2f83749c";
        map.put("id", groupid);

        GsonRequest<StudyGroupBean> gRequest = new GsonRequest<>(Request.Method.POST, url, StudyGroupBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(gRequest);
    }

    private Response.Listener<StudyGroupBean> getListener() {
        return new Response.Listener<StudyGroupBean>() {
            @Override
            public void onResponse(StudyGroupBean response) {
                showLogD("response "+response);
                if (response!= null && response.getCode() == 0){
                    studyGroup = response.getGroup();
                    processData(studyGroup);
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showLogE(volleyError.toString());
            }
        };
    }
    /**
     * 处理请求结果，显示到界面上
     */

    private void processData(StudyGroup studyGroup) {
        if (TextUtils.isEmpty(studyGroup.getHeadUrl())){
            Glide.with(this).load(studyGroup.getHeadUrl()).placeholder(R.mipmap.default_error).into(group_head);
        }
        group_name.setText(studyGroup.getGroupName());
        title.setTitle(studyGroup.getGroupName());
        description.setText(studyGroup.getDescription());



    }
}
