package com.dxj.teacher.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;

/**
 * Created by khb on 2015/9/8.
 */
public class DrawerTestActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ListView leftDrawer;
    private LinearLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawertest);

        initData();
        initView();
        initTitle();

    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        drawer = (LinearLayout) findViewById(R.id.ll_drawer);
        leftDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerElevation(0.5f);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        initList();
    }


    private void initList() {
    }

    @Override
    public void initData() {

    }

//    public  void showMenu(View v){
//        showToast("action");
//    }

    public void action(View view){

        if (mDrawerLayout.isDrawerOpen(drawer)){
            mDrawerLayout.closeDrawer(drawer);
        }else{
            mDrawerLayout.openDrawer(drawer);
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(drawer)){
            mDrawerLayout.closeDrawer(drawer);
        }
        super.onBackPressed();
    }
}
