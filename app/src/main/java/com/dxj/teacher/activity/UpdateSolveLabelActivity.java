package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.CheckableButton;
import com.dxj.teacher.widget.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 个人标签
 */
public class UpdateSolveLabelActivity extends BaseActivity implements View.OnClickListener, CheckableButton.OnCheckedChangeListener {
    private String strMajor;
    private FlowLayout alreadtFlowLayout;
    private FlowLayout selectFlowLayout;
    private ImageButton btnLabel;
    private String[] strings = {"提升学习方法", "陪伴学习", "心灵沟通的朋友", "学习自信的提升", "及时解决学习难点","各种数学难题"};
    private Map<Integer, CheckableButton> store = new HashMap<>();
    private ArrayList<String> list;
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
        btnLabel = (ImageButton) this.findViewById(R.id.btn_back);
        btnLabel.setOnClickListener(this);
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
        if (store.size()==0){
            finish();
            return;}
        Iterator iter  = store.entrySet().iterator();
       list   = new ArrayList<>();
        while (iter.hasNext()){
            Map.Entry<Integer,CheckableButton> entry = (Map.Entry<Integer,CheckableButton>)iter.next();
            CheckableButton checkableButton = entry.getValue();
            Log.i("TAG", "text=" + checkableButton.getText());
            list.add(checkableButton.getText().toString());
        }

        String urlPath = FinalData.URL_VALUE + HttpUtils.SOLVELABEL;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("solveLabel", list);
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
                    bundle.putStringArrayList("solveLabel", list);
                    intent.putExtras(bundle);
                    UpdateSolveLabelActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateSolveLabelActivity.this, "修改失败");
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
