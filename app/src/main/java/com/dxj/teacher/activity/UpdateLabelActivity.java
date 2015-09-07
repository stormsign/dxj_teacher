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
import com.dxj.teacher.widget.CheckableButton;
import com.dxj.teacher.widget.FlowLayout;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 个人标签
 */
public class UpdateLabelActivity extends BaseActivity implements View.OnClickListener, CheckableButton.OnCheckedChangeListener {
    private FlowLayout alreadtFlowLayout;
    private FlowLayout selectFlowLayout;
    private String[] strings = {"成绩好", "品学兼优", "讲解详细", "认真负责", "成绩优异"};
    private Map<Integer, CheckableButton> store = new HashMap<>();
    private ArrayList<String> list;
    private ArrayList<String> lists=new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_label);
        initTitle();
        initData();
        initView();
    }


    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("个人标签");
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
        alreadtFlowLayout = (FlowLayout) this.findViewById(R.id.already_flowlayout);
        selectFlowLayout = (FlowLayout) this.findViewById(R.id.select_flowlayout);
        addChildTo(selectFlowLayout);
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        lists = getIntent().getStringArrayListExtra("label");
    }

    private void addChildTo(FlowLayout flowLayout) {
        for (int i = 0; i < strings.length; i++) {
//            if (strings[i].equals())

            CheckableButton btn = new CheckableButton(this);
            btn.setHeight(dp2px(22));
            btn.setTextSize(12);
            btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            StringBuilder sb = new StringBuilder();
            sb.append(strings[i]);
            btn.setText(sb.toString());
            flowLayout.addView(btn);
            btn.setTag(i);
            btn.setOnCheckedChangeWidgetListener(this);
            Log.i("TAG", "setChecked=sss" + strings[i]);
            for (int n=0;n<lists.size();n++){
                if (strings[i].equals(lists.get(n))){
                    addChildTo(alreadtFlowLayout, i);
                    btn.setChecked(true);
                    break;
                }
            }
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
        if (store.size() == 0) {
            finish();
            return;
        }
        Iterator iter = store.entrySet().iterator();

        list = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry<Integer, CheckableButton> entry = (Map.Entry<Integer, CheckableButton>) iter.next();
            CheckableButton checkableButton = entry.getValue();
            Log.i("TAG", "text=" + checkableButton.getText());
            list.add(checkableButton.getText().toString());
        }

        String urlPath = FinalData.URL_VALUE + HttpUtils.LABEL;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("label", list);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                StringBuffer strBuffer = new StringBuffer();
                if (message.getCode() == 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i != list.size() - 1) {
                            strBuffer.append(list.get(i)).append(",");
                        } else {
                            strBuffer.append(list.get(i));
                        }
                    }
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strBuffer.toString(), AccountTable.LABEL);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("label", list);
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
        Log.i("TAG", "isChecked=" + isChecked);
        if (isChecked) {
            addChildTo(alreadtFlowLayout, index);
        } else {
            CheckableButton checkableButton = store.get(index);
            alreadtFlowLayout.removeView(checkableButton);
            store.remove(index);
        }
    }

    private void addChildTo(FlowLayout flowLayout, int i) {
        if (store.containsKey(i)){
            return;
        }
        CheckableButton btn = new CheckableButton(this);
        btn.setHeight(dp2px(22));
        btn.setTextSize(12);
        btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
        btn.setBackgroundResource(R.drawable.checkable_background);
        StringBuilder sb = new StringBuilder();
        sb.append(strings[i]);
        btn.setText(sb.toString());
        btn.setChecked(true);
        flowLayout.addView(btn);
        store.put(i, btn);

    }

}
