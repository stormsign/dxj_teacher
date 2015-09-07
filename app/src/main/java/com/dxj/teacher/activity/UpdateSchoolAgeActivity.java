package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 教龄
 */
public class UpdateSchoolAgeActivity extends BaseActivity implements View.OnClickListener {
    private EditText etSchoolAge;
    private String strSchoolAge;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nicename);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("教龄");
        title.setTitleNoRightButton();
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onActionClick() {

            }

            @Override
            public void onBackClick() {
                sendRequestData();
            }
        });
    }

    @Override
    public void initView() {
        etSchoolAge = (EditText) findViewById(R.id.et);
        etSchoolAge.setHint("教龄");
        etSchoolAge.setText(strSchoolAge);
    }

    @Override
    public void initData() {
        strSchoolAge = getIntent().getStringExtra("schoolAge");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }
    }

    private void sendRequestData() {
        strSchoolAge = etSchoolAge.getText().toString().trim();
        if (StringUtils.isEmpty(strSchoolAge)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.SCHOOLAGE;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("schoolAge", strSchoolAge);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0) {
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strSchoolAge, AccountTable.SCHOOLAGE);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("schoolAge", strSchoolAge);
                    intent.putExtras(bundle);
                    UpdateSchoolAgeActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateSchoolAgeActivity.this, "修改失败");
                finish();
            }
        };
    }
}
