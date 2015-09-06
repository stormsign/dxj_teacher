package com.dxj.teacher.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.adapter.MemberAdapter;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.List;

/**
 * Created by khb on 2015/9/6.
 */
public class GroupMemberListActivity extends BaseActivity{

    private List<UserBean.UserInfo> members;
    private SwipeRefreshLayout refresh;
    private RecyclerView rv_members;
    private MemberAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团员");
        title.setTitleNoRightButton();
        title.showAction(true);
        title.setActionText("编辑");
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
                mAdapter.setCheckVisible(true);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBackClick() {

            }
        });
    }

    @Override
    public void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        rv_members = (RecyclerView) findViewById(R.id.rv_members);
        mAdapter = new MemberAdapter(this, members);
        rv_members.setAdapter(mAdapter);
        rv_members.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnMemberClickListener(new MemberAdapter.OnMemberClickListener() {
            @Override
            public void onMemberClick(View view, int position) {
                ToastUtils.showToast(context, "member");
            }

            @Override
            public void onMemberDeleteClick(View view, int position) {
                ToastUtils.showToast(context, "check");
            }
        });

    }

    @Override
    public void initData() {
        members = (List<UserBean.UserInfo>)getIntent().getSerializableExtra("members");

    }
}
