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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 别名
 */
public class UpdateNiceNameActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton btnNiceName;
    private EditText etNiceName;
    private String strNiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nicename);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        btnNiceName = (ImageButton) findViewById(R.id.btn_back);
        etNiceName = (EditText) findViewById(R.id.et);
        btnNiceName.setOnClickListener(this);
    }

    @Override
    public void initData() {

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
        strNiceName = etNiceName.getText().toString().trim();
        if (StringUtils.isEmpty(strNiceName)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.NICE_NAME;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("nickName", strNiceName);
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

                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strNiceName, AccountTable.NICKNAME);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("nicename", strNiceName);
                    intent.putExtras(bundle);
                    UpdateNiceNameActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateNiceNameActivity.this, "修改失败");
                finish();
            }
        };
    }
}
