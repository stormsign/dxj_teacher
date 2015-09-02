package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.dxj.teacher.bean.TeacherBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.db.dao.CityDao;
import com.dxj.teacher.db.dao.TeacherTypeDao;
import com.dxj.teacher.dialogplus.SimpleAdapter;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 目前状态
 */
public class UpdateRoleNameActivity extends BaseActivity implements View.OnClickListener {
    public static final String[] strings ={"省级状元","市级状元","区级状元","校级状元"};
    public static final String DBNAME ="t_teacher_role.db";
    private SQLiteDatabase db;

    private ImageButton btnNiceName;
    private ListView lvTeacher;
    private String strRemark;
    private String str;
    private int typeID;
    private int childTypeID;
    private List<SchoolBean> list = new ArrayList<>();
    private List<SchoolBean> teacherList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rolename);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        btnNiceName = (ImageButton) findViewById(R.id.btn_back);
        lvTeacher = (ListView) findViewById(R.id.lv_teacher);
        btnNiceName.setOnClickListener(this);
        SchoolAdapter adapters = new SchoolAdapter(this, false,list);
        lvTeacher.setAdapter(adapters);
        lvTeacher.setOnItemClickListener(getItemClickListener());

    }
   private AdapterView.OnItemClickListener getItemClickListener(){
       return  new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int level = list.get(position).getId();
               teacherList = TeacherTypeDao.getChildTeacherFromParent(db, level);
                               SchoolAdapter adapter = new SchoolAdapter(UpdateRoleNameActivity.this, false,teacherList);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener, dismissListener, cancelListener, true);
//                break;

           }
       };
   }
    @Override
    public void initData() {
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        list = TeacherTypeDao.getFirstTeacher(db);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                sendRequestData();
                break;
//            case R.id.btn_1:
//                SimpleAdapter adapter = new SimpleAdapter(this, false,strings);
//                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener, dismissListener, cancelListener, true);
//                break;
        }

    }

    private void sendRequestData() {
//        strRemark = etRemark.getText().toString().trim();
        if (StringUtils.isEmpty(str)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.ROLENAME;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("childType", childTypeID);
        map.put("type", typeID);
        map.put("roleName", str);
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
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), str, AccountTable.ROLENAME);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("roleName", str);
                    intent.putExtras(bundle);
                    UpdateRoleNameActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateRoleNameActivity.this, "修改失败");
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

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            //        switch (view.getId()) {
            //          case R.id.header_container:
            //            Toast.makeText(MainActivity.this, "Header clicked", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.like_it_button:
            //            Toast.makeText(MainActivity.this, "We're glad that you like it", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.love_it_button:
            //            Toast.makeText(MainActivity.this, "We're glad that you love it", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.footer_confirm_button:
            //            Toast.makeText(MainActivity.this, "Confirm button clicked", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.footer_close_button:
            //            Toast.makeText(MainActivity.this, "Close button clicked", Toast.LENGTH_LONG).show();
            //            break;
            //        }
            //        dialog.dismiss();
        }
    };
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//        TextView textView = (TextView) view.findViewById(R.id.text_view);
//        String clickedAppName = textView.getText().toString();
            //        dialog.dismiss();
            //        Toast.makeText(MainActivity.this, clickedAppName + " clicked", Toast.LENGTH_LONG).show();
            Log.i("TAG", "position=" + position);
            dialog.dismiss();
            str= teacherList.get(position).getName();
            typeID= teacherList.get(position).getParentId();
            childTypeID= teacherList.get(position).getId();
//            sendRequestData(str, 3);

        }
    };
    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "dismiss listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };

    OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "cancel listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };
}
