package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.adapter.NoticeAdapter;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.Notice;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupNoticeActivity extends BaseActivity{

    private RecyclerView rv_notices;
    private SwipeRefreshLayout srl;
    private String groupId;
    private List<Notice> list;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            srl.setRefreshing(false);
            super.handleMessage(msg);
        }
    };

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
        rv_notices.setLayoutManager(new LinearLayoutManager(this));
        NoticeAdapter adapter = new NoticeAdapter(this, list);
        rv_notices.setAdapter(adapter);
        adapter.setOnNoticeClickListener(new NoticeAdapter.OnNoticeClickListener() {
            @Override
            public void onNoticeClick(View view, int position) {
                showToast(position + " clicked");
            }
        });
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

        srl.setColorSchemeResources(R.color.azure, R.color.orange,
                R.color.text_orange2, R.color.text_orange);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showToast("刷新数据");
                handler.sendEmptyMessageDelayed(1, 8000);
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
        list = new ArrayList<>();
        for (int i = 0 ; i < 20; i++){
            Notice notice = new Notice();
            notice.setName(i+i+i+i+"");
            notice.setDescription("sdfsfsdfsfasdffsadfsafdsafssdfasfgdgds");
            list.add(notice);
        }
    }
}
