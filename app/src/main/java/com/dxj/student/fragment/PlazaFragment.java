package com.dxj.student.fragment;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import com.dxj.student.R;
import com.dxj.student.widget.PagerSlidingTabStrip;

/**
 * 广场
 * 
 * @author Administrator
 * 
 */
public class PlazaFragment extends FragmentActivity implements View.OnClickListener {

    private ArrayList<String> titleList;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapters adapter;
    private ViewPager mPage;
    private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);

	}

	@SuppressLint("ResourceAsColor")
    private void init() {

	titleList = new ArrayList<String>();
	titleList.add("登陆");
	titleList.add("注册");
	// tab
	mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	mPage = (ViewPager) findViewById(R.id.vPagerS);

	adapter = new MyPagerAdapters(getSupportFragmentManager(), new SparseArray<Fragment>());
	// 根据title添加界面
	addFragment();
	// 设置tab选中底部颜色
	// mTabs.setIndicatorColorResource(R.color.tabs);
	// 设置tab长度
	mTabs.setInfoDataNum(adapter.fragmentsList.size());
	// 设置Viewpage属性
	mPage.setAdapter(adapter);
	mPage.setCurrentItem(0);
	mPage.setOffscreenPageLimit(adapter.fragmentsList.size());
	// 关联ViewPage
	// mTabs.setPageChangeView(mActivity, multipleActions);
	mTabs.setPageChangeView(mActivity);
	mTabs.setViewPager(mPage);
	mTabs.setCheckTextTab(0);
    }



    @Override
    public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	// multipleActions.collapse();
    }

    private void addFragment() {
//	BbsFragment fragment = BbsFragment.getInstance(flag, userId);
//	// NeighboursFragment neighboursFragment = NeighboursFragment.getInstance(multipleActions, flag, userId);
//	NeighboursFragment neighboursFragment = NeighboursFragment.getInstance(flag, userId);
//	if (flag == 1) {
//	    actionFragmentOne = ActionFragment.getInstance(0, flag, userId);
//	    adapter.fragmentsList.append(ACTION_BY_ME_CHILD_POSITION, actionFragmentOne);
//
//	} else {
//	    actionFragment = PlazaActionFragment.getInstance();
//	    adapter.fragmentsList.append(ACTION_BY_ME_CHILD_POSITION, actionFragment);
//	}
//	adapter.fragmentsList.append(BBS_CHILD_POSITION, fragment);
//	adapter.fragmentsList.append(NEIGHBOURS_CHILD_POSITION, neighboursFragment);
    }

    /**
     * ViewPages Fragment Adapter
     */
    public class MyPagerAdapters extends FragmentStatePagerAdapter {

	private SparseArray<Fragment> fragmentsList;

	public MyPagerAdapters(FragmentManager fm) {
	    super(fm);
	}

	public MyPagerAdapters(FragmentManager fm, SparseArray<Fragment> fragments) {
	    super(fm);
	    this.fragmentsList = fragments;
	}

	public SparseArray<Fragment> getSparseFragments() {
	    return this.fragmentsList;
	}

	@Override
	public int getCount() {
	    return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
	    return fragmentsList.get(arg0);
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    return titleList.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
	    return POSITION_UNCHANGED;
	}
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub

    }

}
