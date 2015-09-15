package com.dxj.teacher.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;

import com.dxj.teacher.bean.TeacherInfoBean;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.fragment.DetailCourseFragment;
import com.dxj.teacher.fragment.DetailFragment;
import com.dxj.teacher.fragment.DetailPhotoFragment;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kings on 9/8/2015.
 * 老师详情
 */
public class TeacherDetailsActivity extends AppCompatActivity implements DetailFragment.OnPhotoClick{
    private RadioGroup radioGroup;
    private Fragment[] fragments;
    private int currentTabIndex = 0;
    private TextView tvName;
    private ImageView imgAvatar;
    private TextView tvRecomment;//介绍
    private RadioButton radioButtonPhoto;//相册
    private RadioButton radioButtonHome;//主页
    private RadioButton radioButtonCourse;//课程
    private Toolbar mToolbar;//
    private CollapsingToolbarLayout mCollapsingToolbarLayout;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
        initData();
        initView();
//        initFragment();
        sendRequestData();

    }

//    @Override
    public void initTitle() {

    }

//    @Override
    public void initView() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mCollapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        mCollapsingToolbarLayout.setTitle("老师详情");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherDetailsActivity.this.onBackPressed();
            }
        });
        radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
        tvName = (TextView) findViewById(R.id.tv_name);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvRecomment = (TextView) findViewById(R.id.tv_recomment);
        radioButtonPhoto = (RadioButton) findViewById(R.id.list_holder_radio_button);
        radioButtonHome = (RadioButton) findViewById(R.id.basic_holder_radio_button);
        radioButtonCourse = (RadioButton) findViewById(R.id.grid_holder_radio_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.basic_holder_radio_button:
                        showFragment(0);
                        break;
                    case R.id.list_holder_radio_button:
                        showFragment(1);

                        break;
                    case R.id.grid_holder_radio_button:
                        showFragment(2);

                        break;
                }
            }
        });
    }



    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE_COMMON + HttpUtils.TEACHERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        GsonRequest<TeacherInfoBean> teacherinfo = new GsonRequest(Request.Method.POST, urlPath, TeacherInfoBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(teacherinfo);
    }

    private Response.Listener<TeacherInfoBean> getListener() {
        return new Response.Listener<TeacherInfoBean>() {
            @Override
            public void onResponse(TeacherInfoBean str) {
                Log.i("TAG", "str=" + str.getUserInfo().getHeadUrl());
                UserBean.UserInfo userInfo = str.getUserInfo();
                fillUi(userInfo);
//                addStude(str.getGroup());
//                addSubject(str.getGoodSubject());
                initFragment(str);
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
        mCollapsingToolbarLayout.setTitle(userInfo.getNickName());
        tvName.setText(userInfo.getNickName());
        Glide.with(this).load(userInfo.getHeadUrl()).centerCrop().into(imgAvatar);
        tvRecomment.setText(userInfo.getRemark());
    }

    private void showFragment(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;

    }

//    @Override
    public void initData() {
    }

    private void initFragment(TeacherInfoBean str) {
        DetailFragment homeFragment = getDetailFragment();
        homeFragment.setOnPhotoClick(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacherInfoBean", str);
        homeFragment.setArguments(bundle);

        DetailPhotoFragment meFragmetn = getDetailPhotoFragment();
        Bundle photoBundle = new Bundle();
        photoBundle.putStringArrayList("photos", str.getUserInfo().getImages());
        meFragmetn.setArguments(photoBundle);

        DetailCourseFragment plazaFragment = getDetailCourseFragment();

        Bundle courseBundle = new Bundle();
        courseBundle.putSerializable("course",str.getGoodSubject());
        plazaFragment.setArguments(courseBundle);

        fragments = new Fragment[]{homeFragment, meFragmetn, plazaFragment};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!plazaFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, plazaFragment, DetailCourseFragment.class.getName());
            fragmentTransaction.hide(plazaFragment);
        }
        if (!homeFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, homeFragment, DetailFragment.class.getName());
        }
        if (!meFragmetn.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, meFragmetn, DetailPhotoFragment.class.getName());
            fragmentTransaction.hide(meFragmetn);
        }
        if (!fragmentTransaction.isEmpty()) {
            fragmentTransaction.show(homeFragment).commit();
        }

    }

    public DetailFragment getDetailFragment() {
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailFragment();
        }
        return fragment;
    }

    public DetailPhotoFragment getDetailPhotoFragment() {
        DetailPhotoFragment fragment = (DetailPhotoFragment) getSupportFragmentManager().findFragmentByTag(DetailPhotoFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailPhotoFragment();
        }
        return fragment;
    }

    public DetailCourseFragment getDetailCourseFragment() {
        DetailCourseFragment fragment = (DetailCourseFragment) getSupportFragmentManager().findFragmentByTag(DetailCourseFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailCourseFragment();
        }
        return fragment;
    }


    @Override
    public void setOnPhotoClick(int index) {
        if (index==DetailFragment.COURSE){
            radioButtonCourse.setChecked(true);
            showFragment(2);
        }else if (index==DetailFragment.PHOTO){
            radioButtonPhoto.setChecked(true);
            showFragment(1);
        }
    }
}
