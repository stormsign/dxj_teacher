package com.dxj.teacher.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.adapter.NotificationAdapter;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.Notification;
import com.dxj.teacher.db.dao.NoticeDao;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.List;

/**
 * Created by khb on 2015/9/16.
 */
public class NotificationsActivity extends BaseActivity{

    public static NotificationsActivity notificationsActivity;

    private List<Notification> noticesList;
    private RecyclerView rv_notice;
    private SwipeRefreshLayout refresh;
    private NotificationAdapter mAdapter;
    private NoticeDao noticeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationsActivity = this;
        initTitle();
        initView();
        initData();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitleNoRightButton();
        title.setTitle("系统消息");
    }

    @Override
    public void initView() {
        rv_notice = (RecyclerView) findViewById(R.id.rv_noticeList);
        refresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void initData() {
        noticeDao = new NoticeDao(this);
        noticesList = noticeDao.getAllNotices();
        mAdapter = new NotificationAdapter(this, noticesList);
        rv_notice.setLayoutManager(new LinearLayoutManager(this));
        rv_notice.setAdapter(mAdapter);
        mAdapter.setOnNotificationItemClickListener(new NotificationAdapter.OnNotificationItemClickListener() {
            @Override
            public void onNotificationItemClick(View view, int position) {
                noticeDao.changeNoticeReadState(noticesList.get(position));
                showToast("假装进入通知详情页面");
            }
        });
    }

    @Override
    protected void onResume() {
        refreshAll();
        super.onResume();
    }

    private void refreshAll(){
        noticesList.clear();
        noticesList.addAll(noticeDao.getAllNotices());
        mAdapter.notifyDataSetChanged();
    }

    public void addNewNotice(Notification notification){
        noticesList.add(0, notification);
        mAdapter.notifyItemInserted(0);
        rv_notice.scrollToPosition(0);
    }
}
