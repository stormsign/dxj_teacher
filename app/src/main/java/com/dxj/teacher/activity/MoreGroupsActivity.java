package com.dxj.teacher.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/9/6.
 */
public class MoreGroupsActivity extends BaseActivity{

    private int secondId;
    private static final String DBNAME = "subject.db";
    private SQLiteDatabase db;
    private String categoryName;
    private SwipeRefreshLayout refresh;
    private RecyclerView rv_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_groups);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle(categoryName);
        title.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        rv_groups = (RecyclerView) findViewById(R.id.rv_groups);
    }

    @Override
    public void initData() {
        secondId = (int) getIntent().getLongExtra("secondId", -1);
        db = SQLiteDatabase.openDatabase(context.getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        categoryName = SubjectDao.getCategoryNameById(db, secondId);



    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
