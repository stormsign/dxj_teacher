package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.CourseAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.base.SwipeRefreshBaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.CourseSubjectBean;
import com.dxj.teacher.bean.CourseSubjectList;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 编辑课程
 */
public class EditCourseActivity extends SwipeRefreshBaseActivity implements View.OnClickListener, CourseAdapter.OnCheckmarkClickListener {

    private Button btnAdd;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private List<CourseSubjectBean> courseBeanList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
        initData();
        initView();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            sendRequestData();
        }
    }
    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    @Override
    public void requestDataRefresh() {
        Log.i("TAG","mSwipeRefreshLayou="+mSwipeRefreshLayout.isRefreshing());
//        //mMeizhiList.clear();
//        //mPage = 1;
//        //getData(/* add from db */ false);
//        setRefreshing(false);

//        if (!mSwipeRefreshLayout.isRefreshing()) {
            sendRequestData();
//        }
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("课程设置");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {
                Log.i("TAG", "onNavOneClick");
            }

            @Override
            public void onNavTwoClick() {
                Log.i("TAG", "onNavTwoClick");
                if (courseAdapter.isShape()) {
                    courseAdapter.setShape(false);
                } else {
                    courseAdapter.setShape(true);
                }
            }

            @Override
            public void onNavThreeClick() {
                Log.i("TAG", "onNavThreeClick");

            }

            @Override
            public void onBackClick() {
//                setResult(RESULT_OK, new Intent().putExtra("subjectName", subjectName)
//                        .putExtra("subjectFirst", subjectFirst)
//                        .putExtra("subjectSecond", subjectSecond)
//                        .putExtra("subjectThree", subjectThree));
//                if (subjectList != null && subjectList.isShowing()) {
//                    subjectList.dismiss();
//                }
            }
        });
    }

    @Override
    public void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add_course);
        recyclerView = (RecyclerView) findViewById(R.id.search_result);
        courseAdapter = new CourseAdapter(this, courseBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnCheckmarkItemClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_course:
                Intent intent = new Intent(this, AddCourseActivity.class);
                startActivity(intent);
//                sendRequestData();
                break;
        }
    }

    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE_COMMON + HttpUtils.GOOD_SUBJECT_LIST;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
//        map.put("nickName", strNiceName);
        GsonRequest<CourseSubjectList> custom = new GsonRequest(Request.Method.POST, urlPath, CourseSubjectList.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<CourseSubjectList> getListener() {
        return new Response.Listener<CourseSubjectList>() {
            @Override
            public void onResponse(CourseSubjectList str) {
                Log.i("TAG", "str=" + str);
                if (str.getCode() == 0) {
                    courseBeanList.addAll(str.getList());
                    courseAdapter.notifyDataSetChanged();
                    setRefreshing(false);
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                ToastUtils.showToast(EditCourseActivity.this, "修改失败");
//                finish();
            }
        };
    }

    @Override
    public void onCheckItemClick(int position) {
        courseBeanList.remove(position);
        courseAdapter.notifyDataSetChanged();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_subject;
    }

}
