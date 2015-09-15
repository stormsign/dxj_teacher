package com.dxj.teacher.activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.SubjectAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.ClassWayBean;
import com.dxj.teacher.bean.CourseSubjectBean;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
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
    //    课方式及价格 mode price   1:学生 2:老师 3协商场地 4:上午 5:下午 6:全天
    private CardView cardCourse;//科目选择
    public static final int MODE_STUDENT = 1;
    public static final int MODE_TEACHER = 2;
    public static final int MODE_ADDRESS = 3;
    public static final int MODE_AM = 4;
    public static final int MODE_PM = 5;
    public static final int MODE_ALL_DAY = 6;

    private PopupWindow subjectList;
    private static final String DBNAME = "subject.db";//数据库名字
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
    private CheckableButton checkBoxteacher;//老师上门
    private CheckableButton checkBoxSutdent;//学生上门
    private CheckableButton checkBoxAddress;//协商地址
    private RelativeLayout linearTeacher;
    private RelativeLayout linearstudent;
    private RelativeLayout linearAddress;
    private LinearLayout linearStudentDelete;//删除学生上门
    private LinearLayout linearTeacherDelete;//删除老师上门
    private LinearLayout linearAddressDelete;//删除协商地址
    private EditText etRemark;//专业背景
    private EditText etStudentPrice;//学生上门价格
    private EditText etTeacherPrice;//老师上门价格
    private EditText etAddressPrice;//协商价格
    private EditText etAmPrice;//上午价格
    private EditText etPmPrice;//下午价格
    private EditText etAllDayPrice;//全天价格
    private TextView tvSubjectName;//科目
    private SwitchCompat switchcompatMore;//判断是否显示更多的选择
    private RelativeLayout relativeMore;//上午下午全天
    private boolean isCheck;//判断是否显示更多的选择
    private boolean isSecond;//判断是否只有两级 true表示只有两级
    private CourseSubjectBean courseSubjectBean;
    /**
     * 一级科目id
     */
    private long subjectFirst;
    private String strSubjectFirst;
    /**
     * 二级科目id
     */
    private long subjectSecond;
    private String strsubjectSecond;
    /**
     * 三级科目id
     */
    private long subjectThree = -1;
    private String strSubjectThree;
    /**
     * 科目名称
     */
    private String subjectName;
    private StringBuffer stringBuffer;

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
        title.setTitle("擅长科目设置");
        title.setTitleNoRightButton();
        title.showAction(true);
        title.setActionText("完成");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {
                Log.i("TAG", "onNavThreeClick");

            }

            @Override
            public void onActionClick() {
                sendRequestData();
            }

            @Override
            public void onBackClick() {
//
            }
        });
    }

    @Override
    public void initView() {
        cardCourse = (CardView) findViewById(R.id.tv_subject_title);
        checkBoxteacher = (CheckableButton) findViewById(R.id.check_box_teacher);
        checkBoxSutdent = (CheckableButton) findViewById(R.id.check_box_student);
        checkBoxAddress = (CheckableButton) findViewById(R.id.check_box_address);
        linearstudent = (RelativeLayout) findViewById(R.id.linear_student);
        linearTeacher = (RelativeLayout) findViewById(R.id.linear_teacher);
        linearAddress = (RelativeLayout) findViewById(R.id.linear_address);
        relativeMore = (RelativeLayout) findViewById(R.id.relative_more);
        etRemark = (EditText) findViewById(R.id.et_remark);
        etStudentPrice = (EditText) findViewById(R.id.et_student_price);
        etTeacherPrice = (EditText) findViewById(R.id.et_teacher_price);
        etAddressPrice = (EditText) findViewById(R.id.et_address_price);
        etAmPrice = (EditText) findViewById(R.id.et_am_price);
        etPmPrice = (EditText) findViewById(R.id.et_pm_price);
        etAllDayPrice = (EditText) findViewById(R.id.et_allday_price);
        etAddressPrice = (EditText) findViewById(R.id.et_address_price);
        tvSubjectName = (TextView) findViewById(R.id.tv_subject_name);
        switchcompatMore = (SwitchCompat) findViewById(R.id.switchcompat_more);

        linearStudentDelete = (LinearLayout) findViewById(R.id.linear_student_delete);
        linearTeacherDelete = (LinearLayout) findViewById(R.id.linear_teacher_delete);
        linearAddressDelete = (LinearLayout) findViewById(R.id.linear_address_delete);

        linearStudentDelete.setOnClickListener(this);
        linearTeacherDelete.setOnClickListener(this);
        linearAddressDelete.setOnClickListener(this);
        checkBoxteacher.setOnCheckedChangeWidgetListener(this);
        checkBoxSutdent.setOnCheckedChangeWidgetListener(this);
        checkBoxAddress.setOnCheckedChangeWidgetListener(this);
        cardCourse.setOnClickListener(this);
        switchcompatMore.setOnCheckedChangeListener(onCheckedChangeListener);
        //假如courseSubjectBean不等于空 表示编辑课程
        if (courseSubjectBean != null) {
            if (!StringUtils.isEmpty(courseSubjectBean.getRemark())) {
                etRemark.setText(courseSubjectBean.getRemark());
            }
            if (!StringUtils.isEmpty(courseSubjectBean.getSubjectName())) {
                tvSubjectName.setText(courseSubjectBean.getSubjectName());
            }
            List<ClassWayBean> classWayList = courseSubjectBean.getClassWay();
            for (int i = 0; i < classWayList.size(); i++) {
                ClassWayBean classWayBean = classWayList.get(i);
                switch (classWayBean.getMode()) {
                    case AddCourseActivity.MODE_STUDENT:
                        checkBoxSutdent.setChecked(true);
                        linearstudent.setVisibility(View.VISIBLE);
                        etStudentPrice.setText(String.valueOf(classWayBean.getPrice()));
                        break;
                    case AddCourseActivity.MODE_TEACHER:
                        checkBoxteacher.setChecked(true);
                        linearTeacher.setVisibility(View.VISIBLE);
                        etTeacherPrice.setText(String.valueOf(classWayBean.getPrice()));

                        break;
                    case AddCourseActivity.MODE_ADDRESS:
                        checkBoxAddress.setChecked(true);
                        linearTeacher.setVisibility(View.VISIBLE);
                        etAddressPrice.setText(String.valueOf(classWayBean.getPrice()));

                        break;
                    case AddCourseActivity.MODE_AM:
                        isCheck = true;
                        etAmPrice.setText(String.valueOf(classWayBean.getPrice()));
                        break;
                    case AddCourseActivity.MODE_PM:
                        isCheck = true;
                        etPmPrice.setText(String.valueOf(classWayBean.getPrice()));
                        break;
                    case AddCourseActivity.MODE_ALL_DAY:
                        isCheck = true;
                        etAllDayPrice.setText(String.valueOf(classWayBean.getPrice()));
                        break;
                }
            }
            switchcompatMore.setChecked(isCheck);
            //科目id
            subjectThree = courseSubjectBean.getSubjectId();
            subjectName = courseSubjectBean.getSubjectName();
            stringBuffer = new StringBuffer(courseSubjectBean.getFullName());
        }
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isCheck = isChecked;
            //等于true显示更多选择
            if (isChecked) {
                relativeMore.setVisibility(View.VISIBLE);
            } else {
                relativeMore.setVisibility(View.GONE);
            }
        }
    };

    private PopupWindow getPopWindow() {
        PopupWindow popSubject = new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.popup_subject_list, null);
        popSubject.setWidth(cardCourse.getWidth());
        popSubject.setHeight(MyUtils.dip2px(this, 400));
        popSubject.setFocusable(true);
        popSubject.setTouchable(true);
        popSubject.setBackgroundDrawable(new BitmapDrawable());
        popSubject.setOutsideTouchable(false);
        popSubject.setContentView(popView);
        popSubject.setAnimationStyle(R.style.popwindow_anim_scale_slide);

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
                    isSecond = true;
                }
                secondAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {
                    @Override
                    public void onSubjectItemClick(View view, int position) {
                        setSecondAdapterClick(view, position, isSecond);
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
    private void setSecondAdapterClick(View view, int position, boolean isS) {
        SubjectBean secondSubject = secondList.get(position);
//        TextView name = (TextView) view;
        subjectSecond = secondSubject.getId();
        subjectName = secondSubject.getName();
        strsubjectSecond = secondSubject.getName();
        setSelectedItem(secondSubject, secondList, position);

        if (isS) {
            subjectList.dismiss();
            isSecond = false;
            stringBuffer = new StringBuffer();
            stringBuffer.append(strSubjectFirst);
            stringBuffer.append("-");
            stringBuffer.append(strsubjectSecond);
            tvSubjectName.setText(stringBuffer.toString());
        } else {
            thirdList.clear();
            thirdList.addAll(SubjectDao.getChildCategoryFromParent(db, secondSubject.getId()));
            secondAdapter.notifyDataSetChanged();
            thirdAdapter.notifyDataSetChanged();
        }


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
        strSubjectThree = thirdSubject.getName();
        setSelectedItem(thirdSubject, thirdList, position);
        thirdAdapter.notifyDataSetChanged();

        if (subjectList != null && subjectList.isShowing()) {
            subjectList.dismiss();
            stringBuffer = new StringBuffer();
            stringBuffer.append(strSubjectFirst);
            stringBuffer.append("-");
            stringBuffer.append(strsubjectSecond);
            stringBuffer.append("-");
            stringBuffer.append(strSubjectThree);
            tvSubjectName.setText(stringBuffer.toString());
        }
    }


    @Override
    public void initData() {
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        firstList = SubjectDao.getFirstCategory(db);
        courseSubjectBean = (CourseSubjectBean) getIntent().getSerializableExtra("course");
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
                    subjectList.showAsDropDown(cardCourse);
                }
                break;
            case R.id.linear_student_delete:
                if (checkBoxSutdent.isChecked()) {
                    checkBoxSutdent.setChecked(false);
                }
                break;
            case R.id.linear_teacher_delete:
                if (checkBoxteacher.isChecked()) {
                    checkBoxteacher.setChecked(false);
                }
                break;
            case R.id.linear_address_delete:
                if (checkBoxAddress.isChecked()) {
                    checkBoxAddress.setChecked(false);
                }
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

        if (StringUtils.isEmpty(strRemark)) {
            etRemark.setError("请输入专业背景介绍");
            etRemark.requestFocus();
            return;
        }
        if (subjectThree == -1) {
            showToast("请选择科目");
            return;
        }

        ArrayList<ClassWayBean> array = new ArrayList<>();
        String urlPath = FinalData.URL_VALUE + HttpUtils.GOODSUBJECT;
        if (checkBoxteacher.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            String price = etTeacherPrice.getText().toString();
            if (StringUtils.isEmpty(price)) {
                price = etTeacherPrice.getHint().toString();
            }
            classWayBean.setPrice(Integer.valueOf(price));
            classWayBean.setMode(MODE_TEACHER);
            array.add(classWayBean);
        }
        if (checkBoxSutdent.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            String price = etStudentPrice.getText().toString();
            if (StringUtils.isEmpty(price)) {
                price = etStudentPrice.getHint().toString();
            }
            classWayBean.setPrice(Integer.valueOf(price));
            classWayBean.setMode(MODE_STUDENT);
            array.add(classWayBean);
        }

        if (checkBoxAddress.isChecked()) {
            ClassWayBean classWayBean = new ClassWayBean();
            String price = etAddressPrice.getText().toString();
            if (StringUtils.isEmpty(price)) {
                price = etAddressPrice.getHint().toString();
            }
            classWayBean.setPrice(Integer.valueOf(price));
            classWayBean.setMode(MODE_ADDRESS);
            array.add(classWayBean);
        }
        if (isCheck) {
            String strAmPrice = etAmPrice.getText().toString().trim();
            String strPmPrice = etPmPrice.getText().toString().trim();
            String strAllDayPrice = etAllDayPrice.getText().toString().trim();
            if (!StringUtils.isEmpty(strAmPrice)) {
                ClassWayBean classWayBean = new ClassWayBean();
                classWayBean.setPrice(Integer.valueOf(strAmPrice));
                classWayBean.setMode(MODE_AM);
                array.add(classWayBean);
            }
            if (!StringUtils.isEmpty(strPmPrice)) {
                ClassWayBean classWayBean = new ClassWayBean();
                classWayBean.setPrice(Integer.valueOf(strPmPrice));
                classWayBean.setMode(MODE_PM);
                array.add(classWayBean);
            }
            if (!StringUtils.isEmpty(strAllDayPrice)) {
                ClassWayBean classWayBean = new ClassWayBean();
                classWayBean.setPrice(Integer.valueOf(strAllDayPrice));
                classWayBean.setMode(MODE_ALL_DAY);
                array.add(classWayBean);
            }
        }
        if (array.size() == 0) {
            showToast("请选择授课方式");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        map.put("subjectId", subjectThree);
        map.put("subjectName", subjectName);
        map.put("remark", strRemark);
        map.put("classWay", array);
        map.put("fullName", stringBuffer.toString());
        if (courseSubjectBean != null) {
            Log.i("TAG","courseSubjectBean");
            map.put("id", courseSubjectBean.getId());
        }

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
                } else
                    showToast(message.getMsg());
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(context, "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(context, "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(context, "服务器异常 登录失败");
            }
        };
    }
}
