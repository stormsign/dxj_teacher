package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.bean.SubjectBean;
import com.dxj.teacher.db.dao.SubjectDao;
import com.dxj.teacher.fragment.GroupListFragment;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.dxj.teacher.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class StudyGroupListActivity extends BaseActivity {

    private int currentPage = 1;
    private final static int pageSize = 10;
    private SQLiteDatabase db;
    private List<SubjectBean> firstList;

    private static final String DBNAME = "subject.db";
    private List<StudyGroup> groupList;
    private ViewPagerIndicator indicator;
    private ViewPager vp_groups;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studygruoplist);
        initTitle();
        initView();
        initData();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("交友学团");
        title.showSearchBar(true);
        title.setSearchHint("输入老师或学团名称");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {
            }

            @Override
            public void onNavTwoClick() {
            }

            @Override
            public void onNavThreeClick() {
                ToastUtils.showToast(context, "创建");
                startActivity(new Intent(context, CreateStudyGroupActivity.class));
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
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        vp_groups = (ViewPager) findViewById(R.id.vp_groups);
    }

    @Override
    public void initData() {
        groupList = new ArrayList<StudyGroup>();

        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        firstList = SubjectDao.getFirstCategory(db);
        List<String> titleList = new ArrayList<>();
        for (SubjectBean subject:firstList) {
            titleList.add(subject.getName());
        }
        titleList.add(0, "推荐学团");
        indicator.setTabItemTitles(titleList);
        indicator.setViewPager(vp_groups, 0);
        List<GroupListFragment> fList = new ArrayList<>();
        for (int i = 0; i<titleList.size();i++){
//            i == 0时是推荐学团
            if (i == 0){    //-1表示fragment应该显示推荐学团
                GroupListFragment fragment = GroupListFragment.newInstance(-1);
                fList.add(fragment);
            }
//            i > 0时是一级目录，这时由于有推荐学团，i-1对应一级目录的记录
            else{
                GroupListFragment fragment = GroupListFragment.newInstance(firstList.get(i-1).getId());
                fList.add(fragment);
            }
        }
        adapter = getAdapter(fList);
        vp_groups.setAdapter(adapter);

    }

    /**
     * 获取FragmentPagerAdapter
     * @param fList
     * @return
     */
    private FragmentPagerAdapter getAdapter(final List<GroupListFragment> fList){
        return new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fList.get(i);
            }

            @Override
            public int getCount() {
                return fList.size();
            }
        };
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
