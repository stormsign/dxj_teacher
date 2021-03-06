package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.SchoolAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.SchoolBean;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.db.dao.CityDao;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.dialogplus.SimpleAdapter;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 别名
 */
public class UpdateSchoolActivity extends BaseActivity implements View.OnClickListener {
    private static final String DBNAME = "t_city.db";
    private EditText etGrades;
    private EditText etSchool;
    private LinearLayout btnCity;
    private LinearLayout btnProvince;
    private TextView tvCity;
    private TextView tvProvince;
    private String strNiceName;
    private List<SchoolBean> list = new ArrayList<>();
    private List<SchoolBean> listCity = new ArrayList<>();
    private SQLiteDatabase db;
    private int index;
    private int provinceId;
    private int cityIndex=-1;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_school);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("就读高中");
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
        etGrades = (EditText) findViewById(R.id.et_grades);
        etSchool = (EditText) findViewById(R.id.et_school);
        etSchool.setText(strNiceName);
        btnCity = (LinearLayout) findViewById(R.id.btn_city);
        tvCity = (TextView) findViewById(R.id.tv_city);
        btnProvince = (LinearLayout) findViewById(R.id.btn_province);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        btnCity.setOnClickListener(this);
        btnProvince.setOnClickListener(this);

    }

    @Override
    public void initData() {
//        copyDB(DBNAME);
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        id = getIntent().getStringExtra("id");
        strNiceName=getIntent().getStringExtra("school");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.btn_province:
                list = CityDao.getFirstCity(db);
                SchoolAdapter adapter = new SchoolAdapter(this, list);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener(0), null, null, true);
                break;
            case R.id.btn_city:
                if (list.size() <= 0) {
                    return;
                }
                provinceId = list.get(index).getId();
                listCity = CityDao.getChildCityFromParent(db, provinceId);
                SchoolAdapter adapters = new SchoolAdapter(this, listCity);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapters, itemClickListener(1), null, null, true);
                break;
        }
    }

    private void sendRequestData() {
        strNiceName = etSchool.getText().toString().trim();
        if (StringUtils.isEmpty(strNiceName)) {
            finish();
            return;
        }
        if (cityIndex==-1){
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.SCHOOL;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("school", strNiceName);
        map.put("schoolProvince", provinceId);
        map.put("schoolCity", listCity.get(cityIndex).getId());
        if (!StringUtils.isEmpty(etGrades.getText().toString()))
            map.put("grades", etGrades.getText().toString());
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

                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strNiceName, AccountTable.SCHOOL);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("school", strNiceName);
                    intent.putExtras(bundle);
                    UpdateSchoolActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateSchoolActivity.this, "修改失败");
                finish();
            }
        };
    }

    private void showOnlyContentDialog(Holder holder, int gravity, BaseAdapter adapter,
                                       OnItemClickListener itemClickListener, OnDismissListener dismissListener,
                                       OnCancelListener cancelListener, boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .setCancelable(true)
                .create();
        dialog.show();
    }

    private OnItemClickListener itemClickListener(final int i) {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//        TextView textView = (TextView) view.findViewById(R.id.text_view);
//        String clickedAppName = textView.getText().toString();
                //        dialog.dismiss();
                //        Toast.makeText(MainActivity.this, clickedAppName + " clicked", Toast.LENGTH_LONG).show();
                Log.i("TAG", "position=" + position);
                if (i == 0) {
                    index = position;
                    tvProvince.setText(list.get(index).getName());
                } else {
                    tvCity.setText(listCity.get(position).getName());
                    cityIndex = position;
                }
                dialog.dismiss();
//            String str = strings[position];
//            sendRequestData(str, 3);
            }
        };
    }

}
