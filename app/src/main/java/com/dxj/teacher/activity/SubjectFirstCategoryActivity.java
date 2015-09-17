package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.FirstGroupCategoryAdapter;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class SubjectFirstCategoryActivity extends BaseActivity {

    private SQLiteDatabase db;
    private List<SubjectBean> firstList;
    private static final String DBNAME = "subject.db";//科目表
    private RecyclerView rv_first;
    private FirstGroupCategoryAdapter firstAdapter;
    private List<String> titleList;
    private List<Boolean> titleSelectedList;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_subject);
        initData();
        initTitle();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("同行风采");
        title.showSearchBar(false);

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
            }
        });
    }

    @Override
    public void initView() {

        rv_first = (RecyclerView) findViewById(R.id.rv_first_category);
        rv_first.setLayoutManager(new GridLayoutManager(this, 4));
        rv_first.setAdapter(firstAdapter);

    }

    @Override
    public void initData() {
        titleList = new ArrayList<>();
        titleSelectedList = new ArrayList<>();
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
//        获取一级分类对象
        firstList = SubjectDao.getFirstCategory(db);
//        获取一级分类名称，和是否选中的记录表
        for (SubjectBean subject : firstList) {
            titleList.add(subject.getName());
            titleSelectedList.add(false);
        }
//        初始化时选中第一个
        titleSelectedList.set(0, true);
        init();

    }


    /**
     * 初始化适配器
     */
    private void init() {
        firstAdapter = new FirstGroupCategoryAdapter(this, titleList, titleSelectedList);
        firstAdapter.setOnFirstClickListener(new FirstGroupCategoryAdapter.OnFirstClickListener() {
            @Override
            public void onFirstClick(View view, int position) {
                if (index >= 0) {
                    titleSelectedList.set(index, false);
                } else {
                    titleSelectedList.set(0, false);
                }
                if (index == position) {
                    return;
                }
                titleSelectedList.set(position, true);
                firstAdapter.notifyDataSetChanged();
                index = position;
                //跳转到二级目录中
                Intent intent = new Intent(SubjectFirstCategoryActivity.this, SubjectCategoryActivity.class);
                intent.putExtra("category", firstList.get(position).getId());
                intent.putExtra("categoryTitle",firstList.get(position).getName());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
