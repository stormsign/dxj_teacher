package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupNoticeActivity extends BaseActivity{

    private RecyclerView rv_notices;
    private SwipeRefreshLayout srl;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);
        initData();
        initTitle();
        initView();

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团公告");
        title.showActionOnly();
        title.setActionText("发布");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {}

            @Override
            public void onNavTwoClick() {}

            @Override
            public void onNavThreeClick() {}

            @Override
            public void onActionClick() {
                showLogD("?????????????????????????????????");
//                showToast("发布公告");
                startActivity(new Intent(activity, CreateNoticeActivity.class));
            }

            @Override
            public void onBackClick() {}
        });

    }

    @Override
    public void initView() {
        rv_notices = (RecyclerView) findViewById(R.id.rv_notices);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

//        srl.setColorScheme(R.color.color1, R.color.color2,
//                R.color.color3, R.color.color4);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        srl.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

//        rv_notices.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView,
//                                             int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    srl.setRefreshing(true);
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
//            }
//        });
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra("groupId");
    }
}
