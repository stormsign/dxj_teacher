package com.dxj.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dxj.student.activity.CoursesActivity;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.factory.FragmentFactory;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class MainActivity extends BaseActivity {

    private Button bt_user, bt_message, bt_search, bt_home;
    private FragmentManager fm;
    private Context context = this;
    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    @Override
    public void initView() {

        fm = getSupportFragmentManager();
        bt_home = (Button) findViewById(R.id.bt_home);
        bt_search = (Button) findViewById(R.id.bt_search);
        bt_message = (Button) findViewById(R.id.bt_message);
        bt_user = (Button) findViewById(R.id.bt_user);

    }

    @Override
    public void initData() {
        mPushAgent = PushAgent.getInstance(context);
        if (!mPushAgent.isEnabled()) {
            mPushAgent.enable(mRegisterCallback);
        }

        String device_token = UmengRegistrar.getRegistrationId(context);
        showLogD("device_token "+device_token);
    }

    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            String pkgName = getApplicationContext().getPackageName();
            String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
                            "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
                    mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                    mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                    UmengMessageDeviceConfig.getAppVersionCode(MainActivity.this), UmengMessageDeviceConfig.getAppVersionName(MainActivity.this));

            Log.i("TAG", "updateStatus:" + info);
            Log.i("TAG", "=============================");
        }
    };


    public void showFragment(View v){
        switch (v.getId()){
            case R.id.bt_home:
                Toast.makeText(context, "HOME", Toast.LENGTH_LONG).show();
                fm.beginTransaction()
                        .replace(R.id.rl_fragment_contanier, FragmentFactory.getFragment(0), "HOME")
                        .commit();
                break;
            case R.id.bt_search:
                Toast.makeText(context, "SEARCH", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void show(View v){
        startActivity(new Intent(context, CoursesActivity.class));
    }

}
