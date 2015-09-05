package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
 * 介绍
 */
public class UpdateRecommendActivity extends BaseActivity implements View.OnClickListener {
    private EditText etRemark;
    private String strRemark;
    private TextView tvCount;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recommend);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("介绍");
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
        etRemark = (EditText) findViewById(R.id.et);
        etRemark.setText(strRemark);
        tvCount = (TextView) findViewById(R.id.tv_count);
        etRemark.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tvCount.setText(s.length() + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        strRemark=getIntent().getStringExtra("recommend");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }

    }

    private void sendRequestData() {
        strRemark = etRemark.getText().toString().trim();
        if (StringUtils.isEmpty(strRemark)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.REMARk;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("remark", strRemark);
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
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strRemark, AccountTable.REMARK);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("remark", strRemark);
                    intent.putExtras(bundle);
                    UpdateRecommendActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateRecommendActivity.this, "修改失败");
                finish();
            }
        };
    }
}
