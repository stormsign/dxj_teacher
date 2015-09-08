package com.dxj.teacher.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dxj.teacher.R;
import com.dxj.teacher.activity.CardSettingActivity;
import com.dxj.teacher.activity.DrawerTestActivity;
import com.dxj.teacher.activity.EditCourseActivity;
import com.dxj.teacher.activity.UpdateUserInfoActivity;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.utils.LogUtils;
import com.dxj.teacher.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private final static String SMSSDK_APP_KEY = "a38245d89da2";
    private final static String SMSSDK_APP_SECRET = "84db45a5b7dcde895fc72f47edf53447";
    private EventHandler eventHandler;
    private EditText yzm;

    private Button btnGoodSubject;
    private Button btnCard;
    private Button btnUserInfo;

    @Override
    public void initData() {
//        List<String>

        SMSSDK.initSDK(context, MyUtils.SMSSDK_APP_KEY, MyUtils.SMSSDK_APP_SECRET);
        eventHandler = new EventHandler() {
            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        LogUtils.w("EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        LogUtils.w("get code success");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
                        LogUtils.d("countryList = " + countryList.toString());
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

        SMSSDK.getSupportedCountries();
    }

    @Override
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(eventHandler);
        super.onDestroy();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        btnGoodSubject = (Button) view.findViewById(R.id.btn_good_subject);
        btnCard = (Button) view.findViewById(R.id.btn_card);
        btnUserInfo = (Button) view.findViewById(R.id.btn_userinfo);
        btnGoodSubject.setOnClickListener(this);
        btnCard.setOnClickListener(this);
        btnCard.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        yzm = (EditText) view.findViewById(R.id.yzm);
        view.findViewById(R.id.sms).setOnClickListener(this);
        view.findViewById(R.id.verifysms).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_good_subject:
                Log.i("TAG", "onClick");
                Intent intent = new Intent(getActivity(), EditCourseActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_card:
                startActivity(new Intent(getActivity(), CardSettingActivity.class));
                break;
            case R.id.btn_userinfo:
                startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                break;
            case R.id.sms:
                SMSSDK.getVerificationCode("86", "15068205384");
                break;
            case R.id.verifysms:
                SMSSDK.submitVerificationCode("86", "15068205384", yzm.getText().toString());
                break;

        }
    }

    public void draw(View view){
        startActivity(new Intent(context, DrawerTestActivity.class));
    }
}

