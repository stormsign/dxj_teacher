package com.dxj.teacher.activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.SubjectAdapter;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.ClassWayBean;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.widget.CheckableButton;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 别名
 */
public class AddCourseActivity extends BaseActivity implements View.OnClickListener, CheckableButton.OnCheckedChangeListener {

    private TextView btnCourse;
    private PopupWindow subjectList;
    private static final String DBNAME = "subject.db";
    private List<SubjectBean> firstList;
    private SQLiteDatabase db;
    private List<SubjectBean> secondList;
    private List<SubjectBean> thirdList;
    private RecyclerView rv_first;
    private RecyclerView rv_second;
    private RecyclerView rv_thrid;
    private SubjectAdapter firstAdapter;
    private SubjectAdapter secondAdapter;
    private SubjectAdapter thirdAdapter;
    private CheckableButton checkBoxteacher;
    private CheckableButton checkBoxSutdent;
    private CheckableButton checkBoxAddress;
    private LinearLayout linearTeacher;
    private LinearLayout linearstudent;
    private LinearLayout linearAddress;
    private EditText etRemark;
    /**
     * 一级科目id
     */
    private long subjectFirst;
    private String strSubjectFirst;
    /**
     * 二级科目id
     */
    private long subjectSecond;
    /**
     * 三级科目id
     */
    private long subjectThree = -1;
    private String strSubjectThree;
    /**
     * 科目名称
     */
    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("选择科目");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {
                Log.i("TAG", "onNavOneClick");
                sendRequestData();
            }

            @Override
            public void onNavTwoClick() {
                Log.i("TAG", "onNavTwoClick");

            }

            @Override
            public void onNavThreeClick() {
                Log.i("TAG", "onNavThreeClick");

            }

            @Override
            public void onActionClick() {

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
        btnCourse = (TextView) findViewById(R.id.tv_subject_title);
        checkBoxteacher = (CheckableButton) findViewById(R.id.check_box_teacher);
        checkBoxSutdent = (CheckableButton) findViewById(R.id.check_box_student);
        checkBoxAddress = (CheckableButton) findViewById(R.id.check_box_address);
        linearstudent = (LinearLayout) findViewById(R.id.linear_student);
        linearTeacher = (LinearLayout) findViewById(R.id.linear_teacher);
        linearAddress = (LinearLayout) findViewById(R.id.linear_address);
        etRemark = (EditText) findViewById(R.id.et_remark);
        checkBoxteacher.setOnCheckedChangeWidgetListener(this);
        checkBoxSutdent.setOnCheckedChangeWidgetListener(this);
        checkBoxAddress.setOnCheckedChangeWidgetListener(this);
        btnCourse.setOnClickListener(this);
    }


    private PopupWindow getPopWindow() {
        PopupWindow popSubject = new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.popup_subject_list, null);
        showLogD("subjectTitle.getWidth()" + btnCourse.getWidth());
        popSubject.setWidth(btnCourse.getWidth());
        popSubject.setHeight(800);
        popSubject.setFocusable(true);
        popSubject.setTouchable(true);
        popSubject.setBackgroundDrawable(new BitmapDrawable());
        popSubject.setOutsideTouchable(false);
        popSubject.setContentView(popView);
        popSubject.setAnimationStyle(R.style.popwindow_anim_slide);

        rv_first = (RecyclerView) popView.findViewById(R.id.rv_first_category);
        rv_second = (RecyclerView) popView.findViewById(R.id.rv_second_category);
        rv_thrid = (RecyclerView) popView.findViewById(R.id.rv_third_category);
        firstAdapter = new SubjectAdapter(this, firstList);
        rv_first.setLayoutManager(new LinearLayoutManager(this));
        rv_first.setAdapter(firstAdapter);
        firstAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {

            @Override
            public void onSubjectItemClick(View view, int position) {
                SubjectBean firstSubject = firstList.get(position);
//                TextView name = (TextView) view;
//                showLogD("firstList.get(position) " + firstSubject.getName() + " name " + name.getText());
                subjectFirst = firstSubject.getId();
                strSubjectFirst = firstSubject.getName();
                setSelectedItem(firstSubject, firstList, position);

                secondList = SubjectDao.getChildCategoryFromParent(db, firstSubject.getId());
                if (secondList.get(0) != null) {
                    thirdList = SubjectDao.getChildCategoryFromParent(db, secondList.get(0).getId());
                }
                secondAdapter = new SubjectAdapter(activity, secondList);
                thirdAdapter = new SubjectAdapter(activity, thirdList);
                rv_second.setAdapter(secondAdapter);
                rv_second.setLayoutManager(new LinearLayoutManager(activity));
                rv_thrid.setAdapter(thirdAdapter);
                rv_thrid.setLayoutManager(new LinearLayoutManager(activity));
//                rv_second.setVerticalScrollBarEnabled(true);
                if (secondList.size() == 1) {    //如果二级目录只有一个，直接设为选中
                    subjectSecond = secondList.get(0).getId();
                    subjectName = secondList.get(0).getName();
                    setSelectedItem(secondList.get(0), secondList, 0);
                }
                secondAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {
                    @Override
                    public void onSubjectItemClick(View view, int position) {
                        setSecondAdapterClick(view, position);
                    }
                });
                thirdAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {
                    @Override
                    public void onSubjectItemClick(View view, int position) {
                        setThirdAdapterClick(view, position);
                    }
                });
                firstAdapter.notifyDataSetChanged();
                secondAdapter.notifyDataSetChanged();
                thirdAdapter.notifyDataSetChanged();

            }
        });
        return popSubject;
    }

    /**
     * 设置该item是否被选中 ，其它item被选择状态重置为未选中
     *
     * @param subjectBean
     * @param list
     * @param position
     */
    private void setSelectedItem(SubjectBean subjectBean, List<SubjectBean> list, int position) {
        subjectBean.setIsSelected(true);
        for (int i = 0; i < list.size(); i++) {
            if (i != position) {
                list.get(i).setIsSelected(false);
            }
        }
    }

    /**
     * 设置二级目录item点击事件
     *
     * @param view
     * @param position
     */
    private void setSecondAdapterClick(View view, int position) {
        SubjectBean secondSubject = secondList.get(position);
//        TextView name = (TextView) view;
        subjectSecond = secondSubject.getId();
        subjectName = secondSubject.getName();
        setSelectedItem(secondSubject, secondList, position);
        thirdList.clear();
        thirdList.addAll(SubjectDao.getChildCategoryFromParent(db, secondSubject.getId()));
        secondAdapter.notifyDataSetChanged();
        thirdAdapter.notifyDataSetChanged();

    }

    /**
     * 设置三级目录item点击事件
     *
     * @param view
     * @param position
     */
    private void setThirdAdapterClick(View view, int position) {
        SubjectBean thirdSubject = thirdList.get(position);
//        TextView name = (TextView) view;
        subjectThree = thirdSubject.getId();
        subjectName = thirdSubject.getName();
        setSelectedItem(thirdSubject, thirdList, position);
        thirdAdapter.notifyDataSetChanged();

        if (subjectList != null && subjectList.isShowing()) {
            subjectList.dismiss();
        }
    }


    @Override
    public void initData() {
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        firstList = SubjectDao.getFirstCategory(db);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_subject_title:
                if (subjectList != null && subjectList.isShowing()) {
                    subjectList.dismiss();
                } else {
                    subjectList = getPopWindow();
                    subjectList.showAsDropDown(btnCourse);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CheckableButton buttonView, boolean isChecked) {
        if (buttonView == checkBoxteacher) {
            Log.i("TAG", "buttonView");
            if (isChecked) {
                linearTeacher.setVisibility(View.VISIBLE);
            } else {
                linearTeacher.setVisibility(View.GONE);

            }
        } else if (buttonView == checkBoxSutdent) {
            if (isChecked) {
                linearstudent.setVisibility(View.VISIBLE);
            } else {
                linearstudent.setVisibility(View.GONE);
            }

        } else if (buttonView == checkBoxAddress) {
            if (isChecked) {
                linearAddress.setVisibility(View.VISIBLE);
            } else {
                linearAddress.setVisibility(View.GONE);

            }
        }
    }

    private void sendRequestData() {
        String strRemark = etRemark.getText().toString().trim();
//        if (StringUtils.isEmpty(strNiceName)) {
//            finish();
//            return;
//        }
        if (StringUtils.isEmpty(strRemark)) {
            etRemark.setError("请输入专业背景介绍");
            etRemark.requestFocus();
            return;
        }
        if (subjectThree == -1) {
            showToast("请选择科目");
            return;
        }
//        if (){
//
//        }
        ArrayList<ClassWayBean> array = new ArrayList<>();
        String urlPath = FinalData.URL_VALUE + HttpUtils.GOODSUBJECT;
        if (checkBoxteacher.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            classWayBean.setPrice(45);
            classWayBean.setMode(1);
            array.add(classWayBean);
        }
        if (checkBoxSutdent.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            classWayBean.setPrice(45);
            classWayBean.setMode(2);
            array.add(classWayBean);
        }

        if (checkBoxAddress.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            classWayBean.setPrice(45);
            classWayBean.setMode(3);
            array.add(classWayBean);
        }
        if (array.size() == 0) {
            showToast("请选择授课方式");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("subjectId", subjectThree);
        map.put("subjectName", subjectName);
        map.put("remark", strRemark);
        map.put("classWay", array);
//        map.put("nickName", strNiceName);
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

//                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strNiceName, AccountTable.NICKNAME);
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("nicename", strNiceName);
//                    intent.putExtras(bundle);
//                    EditCourseActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.showToast(EditCourseActivity.this, "修改失败");
                finish();
            }
        };
    }
}
