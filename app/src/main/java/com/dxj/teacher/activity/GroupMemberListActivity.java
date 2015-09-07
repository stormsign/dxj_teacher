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
    /**
     * 当前用户是否是团长
     */
    private boolean isOwner;

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
        final TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团员");
        title.setTitleNoRightButton();
        if (isOwner) {
            title.showAction(true);
            title.setActionText("编辑");
        }
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
                if (mAdapter.isCheckable()) {
                    title.setActionText("编辑");
                    mAdapter.setIsCheckable(false);
                }else{
                    title.setActionText("取消");
                    mAdapter.setIsCheckable(true);
                }
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
//                当显示选择删除图标时，即进入编辑状态，此时屏蔽点击团成员的事件
                if (!mAdapter.isCheckable()) {
                    ToastUtils.showToast(context, "member");
                }
            }

            @Override
            public void onMemberDeleteClick(View view, int position) {
                ToastUtils.showToast(context, "check");
//                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("删除团员");
//                builder.setMessage("确定要删除该团员吗？");

            }
        });

    }

    @Override
    public void initData() {
        members = (List<UserBean.UserInfo>)getIntent().getSerializableExtra("members");
        isOwner = getIntent().getBooleanExtra("isOwner", false);
    }
}
