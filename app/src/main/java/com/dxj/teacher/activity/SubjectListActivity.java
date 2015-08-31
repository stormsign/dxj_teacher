package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.widget.TitleNavBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class SubjectListActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow subjectList;
    private static final String DBNAME = "subject.db";
    private TextView subjectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("选择科目");
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
            public void onBackClick() {
                setResult(RESULT_OK, new Intent().putExtra("subjectName", "科目1").putExtra("subjcetId", 111));
                showToast("退出保存");
            }
        });
    }

    @Override
    public void initView() {
        subjectTitle = (TextView) findViewById(R.id.tv_subject_title);
        subjectTitle.setOnClickListener(this);
//        subjectList = getPopWindow();



    }

    private PopupWindow getPopWindow() {
        PopupWindow popSubject = new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.popup_subject_list, null);
        showLogD("subjectTitle.getWidth()"+subjectTitle.getWidth());
        popSubject.setWidth(subjectTitle.getWidth());
        popSubject.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popSubject.setFocusable(true);
        popSubject.setTouchable(true);
        popSubject.setBackgroundDrawable(new BitmapDrawable());
        popSubject.setOutsideTouchable(false);
        popSubject.setContentView(popView);

//        RecyclerView rv_first = (RecyclerView) popView.findViewById(R.id.rv_first_category);
//        RecyclerView rv_second = (RecyclerView) popView.findViewById(R.id.rv_second_category);
//        RecyclerView rv_thrid = (RecyclerView) popView.findViewById(R.id.rv_third_category);

        return popSubject;
    }


    @Override
    public void initData() {
//        copyDB(DBNAME);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir()+"/"+DBNAME,null,SQLiteDatabase.OPEN_READONLY);
        List<SubjectBean> list = SubjectDao.getFirstCategory(db);
        showLogD("subject list size" + list.size());
//        SQLiteDatabase db = SQ

    }

    private void copyDB(final String dbName) {
        new Thread(){
            public void run() {
                File file = new File(getFilesDir(), dbName);
                if (file.exists() && file.length() > 0) {
                    showLogI("已经拷贝过了数据库,无需重新拷贝");
                } else {
                    try {
                        InputStream is = getAssets().open(dbName);
                        byte[] buffer = new byte[1024];
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_subject_title:
                showLogD("subject");
//                subjectList.showAtLocation(subjectTitle, Gravity.BOTTOM, 0, 0);
//                subjectList.showAsDropDown(subjectTitle);
                if (subjectList != null && subjectList.isShowing()){
                    subjectList.dismiss();
                }else {
                    subjectList = getPopWindow();
                    subjectList.showAsDropDown(subjectTitle);
                }
                break;


        }
    }


}
