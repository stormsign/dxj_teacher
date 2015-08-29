package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.CheckableButton;
import com.dxj.teacher.widget.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 个人标签
 */
public class UpdateLabelActivity extends BaseActivity implements View.OnClickListener, CheckableButton.OnCheckedChangeListener {
    private String strMajor;
    private FlowLayout alreadtFlowLayout;
    private FlowLayout selectFlowLayout;
    private String[] strings = {"品学兼优", "品学兼优", "讲解详细", "认真负责", "成绩优异"};
    private Map<Integer, CheckableButton> store = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_label);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        alreadtFlowLayout = (FlowLayout) this.findViewById(R.id.already_flowlayout);
        selectFlowLayout = (FlowLayout) this.findViewById(R.id.select_flowlayout);
        addChildTo(selectFlowLayout);
    }

    @Override
    public void initData() {

    }

    private void addChildTo(FlowLayout flowLayout) {
        for (int i = 0; i < strings.length; i++) {
            CheckableButton btn = new CheckableButton(this);
            btn.setHeight(dp2px(32));
            btn.setTextSize(16);
            btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            StringBuilder sb = new StringBuilder();
            sb.append(strings[i]);
            btn.setText(sb.toString());
            flowLayout.addView(btn);
            btn.setTag(i);
            btn.setOnCheckedChangeWidgetListener(this);

        }
    }

    public int dp2px(int dpValue) {
        return (int) (dpValue * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                sendRequestData();
                break;
        }
    }

    private void sendRequestData() {
//        strMajor = etMajor.getText().toString().trim();
        if (StringUtils.isEmpty(strMajor)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.MAJOR;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("major", strMajor);
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
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("major", strMajor);
                    intent.putExtras(bundle);
                    UpdateLabelActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateLabelActivity.this, "修改失败");
                finish();
            }
        };
    }

    @Override
    public void onCheckedChanged(CheckableButton buttonView, boolean isChecked) {
        int index = (Integer) buttonView.getTag();
        int count = 0;
        Log.i("TAG", "index=" + index);
        if (isChecked) {
            addChildTo(alreadtFlowLayout, index);
        } else {
            CheckableButton checkableButton = store.get(index);
            alreadtFlowLayout.removeView(checkableButton);
        }
    }

    private void addChildTo(FlowLayout flowLayout, int i) {
        CheckableButton btn = new CheckableButton(this);
        btn.setHeight(dp2px(32));
        btn.setTextSize(16);
        btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
        btn.setBackgroundResource(R.drawable.checkable_background);
        StringBuilder sb = new StringBuilder();
        sb.append(strings[i]);
        btn.setText(sb.toString());
        flowLayout.addView(btn);
        store.put(i, btn);


//            btn.setTag(i);
//            btn.setOnCheckedChangeWidgetListener(this);

    }

}
