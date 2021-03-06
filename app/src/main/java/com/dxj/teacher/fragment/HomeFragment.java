package com.dxj.teacher.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.activity.CardSettingActivity;
import com.dxj.teacher.activity.ChatActivity;
import com.dxj.teacher.activity.EditCourseActivity;
import com.dxj.teacher.activity.TeacherDetailsActivity;
import com.dxj.teacher.activity.UpdateUserInfoActivity;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.MultiSwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private final static String SMSSDK_APP_KEY = "a38245d89da2";
    private final static String SMSSDK_APP_SECRET = "84db45a5b7dcde895fc72f47edf53447";
    private final static int IS_ACCEPT_TRUE = 0;//接受预约
    private final static int IS_ACCEPT_FALSE = 1;//不接受预约


    private RelativeLayout btnGoodSubject;//科目设置
    private RelativeLayout btnCard;//认证设置
    private RelativeLayout btnUserInfo;//个人信息
    private SwitchCompat switchcompatAppointment;//判断是否接受预约
    private MultiSwipeRefreshLayout swipeRefreshLayout;//下拉刷新

    private TextView tvStateOne;
    private ImageView imgStateSubject;

    private UserBean mUserBean;

    @Override
    public void initData() {
        mUserBean = MyApplication.getInstance().getUserBean();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        btnGoodSubject = (RelativeLayout) view.findViewById(R.id.relalative_good_subject);
        btnCard = (RelativeLayout) view.findViewById(R.id.relative_card);
        btnUserInfo = (RelativeLayout) view.findViewById(R.id.relateive_userinfo);
        /***********/
        tvStateOne = (TextView) view.findViewById(R.id.tv_state_one);
        imgStateSubject = (ImageView) view.findViewById(R.id.img_state_subject);
        /*******end********/
        switchcompatAppointment = (SwitchCompat) view.findViewById(R.id.switchcompat_appointment);
        swipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        btnGoodSubject.setOnClickListener(this);
        btnCard.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        view.findViewById(R.id.userinfo).setOnClickListener(this);
        view.findViewById(R.id.kefu).setOnClickListener(this);
        switchcompatAppointment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sendRequestData(IS_ACCEPT_TRUE);
                else
                    sendRequestData(IS_ACCEPT_FALSE);
            }
        });
        if (mUserBean!=null) {
            Log.i("TAG","size="+mUserBean.getUserInfo().getSubject().size());
            if (mUserBean.getUserInfo().getSubject().size() > 0) {
                setViewValue(true, tvStateOne, imgStateSubject);
            } else {
                setViewValue(false, tvStateOne, imgStateSubject);

            }
        }
        trySetupSwipeRefresh();
        return view;
    }

    /**
     * 根据资料的完成度 给控件设置属性
     *
     * @param is        为true表示 已经完成
     * @param textView
     * @param imageView
     */
    private void setViewValue(boolean is, TextView textView, ImageView imageView) {
        if (is) {
            textView.setText("已完成");
            textView.setTextColor(getResources().getColor(R.color.tv_alread_card));
            imageView.setBackgroundResource(R.mipmap.home_lie_icon_pass);
        } else {
            textView.setText("未完善");
            textView.setTextColor(getResources().getColor(R.color.text_label));
            imageView.setBackgroundResource(R.mipmap.home_lie_icon_nopass);
        }
    }

    /**
     *
     */
    void trySetupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2, R.color.refresh_progress_1);
            // do not use lambda!!
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i("TAG", "onRefresh");
                    requestDataRefresh();
                }
            });
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (swipeRefreshLayout == null) {
            return;
        }
        if (!refreshing) {
            // 防止刷新消失太快，
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private void requestDataRefresh() {
        String urlPath = FinalData.URL_VALUE + HttpUtils.TEACHERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        GsonRequest<UserBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, UserBean.class, map, getListenerRefresh(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener getListenerRefresh() {
        return new Response.Listener<UserBean>() {

            @Override
            public void onResponse(UserBean userBean) {
                Log.i("TAG", "userBean=" + userBean.toString());
                if (userBean != null) {
                    if (userBean.getCode() == 0) {
                        AccountDBTask.clear();
                        AccountDBTask.saveUserBean(userBean);
                        MyApplication.getInstance().setUserBean(userBean);
                    } else
                        ToastUtils.showToast(getActivity(), userBean.getMsg());
                } else {
                    ToastUtils.showToast(getActivity(), "刷新失败");
                }
                setRefreshing(false);
            }
        };
    }

    private void sendRequestData(int accept) {

        String urlPath = FinalData.URL_VALUE + HttpUtils.ACCEPT;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        map.put("accept", accept);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0)
                    ToastUtils.showToast(getActivity(), "设置成功");
                else
                    ToastUtils.showToast(getActivity(), "设置失败");
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(getActivity(), "设置失败");

            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relalative_good_subject:
                Log.i("TAG", "onClick");
                Intent intent = new Intent(getActivity(), EditCourseActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_card:
                startActivity(new Intent(getActivity(), CardSettingActivity.class));
                break;
            case R.id.userinfo:
                startActivity(new Intent(getActivity(), TeacherDetailsActivity.class));
                break;
//            case R.id.sms:
//                SMSSDK.getVerificationCode("86", "15068205384");
//                break;
//            case R.id.verifysms:
//                SMSSDK.submitVerificationCode("86", "15068205384", yzm.getText().toString());
//                break;
            case R.id.relateive_userinfo:
                startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                break;
            case R.id.kefu:
                startActivity(new Intent(getActivity(), ChatActivity.class)
                    .putExtra("chatType",ChatActivity.CHATTYPE_SINGLE)
                        .putExtra("userId", "800")
                );
                break;

        }
    }
}

