package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.ResultListAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.UniversityBean;
import com.dxj.teacher.bean.UniversityListBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import github.jjobes.slidedatetimepicker.SlideDateTimePicker;

/**
 * Created by kings on 8/27/2015.
 */
public class UpdateUniversityActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton btnBack;
    private EditText etSearch;
    private EditText etMajor;
    private ListView lvSearchResult;
    private Button btnEntrancetime;
    private TextView tvNoresult;
    private String strGrades;
    private String strEntranceTime;
    private String strSearch;
    private ResultListAdapter resultListAdapter;
    private ArrayList<UniversityBean> universityResult=new ArrayList<>();
    private String strUniversity;
    private long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_university);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        etSearch = (EditText) findViewById(R.id.et_university);
        lvSearchResult = (ListView) findViewById(R.id.search_result);
        btnEntrancetime=(Button)findViewById(R.id.btn_entrancetime);
        tvNoresult=(TextView)findViewById(R.id.tv_noresult);
        etMajor=(EditText)findViewById(R.id.et_major);
        btnBack.setOnClickListener(this);
        btnEntrancetime.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() == null || "".equals(s.toString())) {

                    lvSearchResult.setVisibility(View.GONE);
                    tvNoresult.setVisibility(View.GONE);
                } else {
//                    letterListView.setVisibility(View.GONE);
//                    personList.setVisibility(View.GONE);
                    strSearch = s.toString();
                    sendRequestData(3);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resultListAdapter = new ResultListAdapter(this, universityResult);
        lvSearchResult.setAdapter(resultListAdapter);
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                strUniversity=universityResult.get(position).getName();
                id=universityResult.get(position).getId();
                etSearch.setText(strUniversity);
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                sendRequestData(1);
                break;
            case R.id.btn_entrancetime:
                updateBirthday();
                break;
        }
    }

    private void sendRequestData(int index) {

        String parameter=null;
        String urlPath =null;
        String key =null;

        if(index==1){
            parameter = etSearch.getText().toString().trim();

            if (StringUtils.isEmpty(parameter)){
                finish();
            }else{
                if (StringUtils.isEmpty(strEntranceTime)){
                    ToastUtils.showToast(this,"请输入开学时间");
                    return;
                }
            }
            urlPath = FinalData.URL_VALUE + HttpUtils.UNIVERSITY;
            if (StringUtils.isEmpty(parameter)) {
                finish();
                return;
            }
            key="university";
        }else if (index==3){
            parameter = strSearch;
            urlPath = FinalData.URL_VALUE + HttpUtils.FIND_UNIVERSITY;
            if (StringUtils.isEmpty(parameter)) {
                return;
            }
            key="name";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put(key, parameter);
        if(index==1){
            map.put("universityId",id);
            map.put("entranceTime",strEntranceTime);
            map.put("major",etMajor.getText().toString());
        }
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(index), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener(final int index) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                if (index==3){
                    UniversityListBean message = JSONObject.parseObject(str, UniversityListBean.class);
                    Log.i("TAG","message="+message.getList().size());
//                  if (message.getCode()==0){
                    universityResult.clear();
                    universityResult.addAll(message.getList());
                      if (universityResult.size() <= 0) {
                          Log.i("TAG","city_result="+universityResult.size());

                          tvNoresult.setVisibility(View.VISIBLE);
                          lvSearchResult.setVisibility(View.GONE);
                      } else {
                          Log.i("TAG","message="+message.getList().size());
                          Log.i("TAG","city_result="+universityResult.size());

                          tvNoresult.setVisibility(View.GONE);
                          lvSearchResult.setVisibility(View.VISIBLE);
                        resultListAdapter.notifyDataSetChanged();
//                      }
                  }
                }else{
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0) {
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strUniversity, AccountTable.UNIVERSITY);

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("university", strUniversity);
                        intent.putExtras(bundle);
                        UpdateUniversityActivity.this.setResult(RESULT_OK, intent);
                        finish();
                }}
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateUniversityActivity.this, "修改失败");
                finish();
            }
        };
    }
    private void updateBirthday() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                .build()
                .show();
    }
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
//            Toast.makeText(UpdateUserInfoActivity.this,
//                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
            btnEntrancetime.setText(mFormatter.format(date));
            strEntranceTime=mFormatter.format(date);

        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(UpdateUniversityActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };
}
