package com.dxj.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dxj.teacher.activity.EditCourseActivity;
import com.dxj.teacher.activity.LoginAndRightActivity;
import com.dxj.teacher.activity.StudyGroupListActivity;
import com.dxj.teacher.activity.UpdateUserInfoActivity;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.factory.FragmentFactory;
import com.dxj.teacher.utils.SPUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private Button bt_user, bt_message, bt_search, bt_home;
    private FragmentManager fm;
    private Context context = this;
    private PushAgent mPushAgent;

    private static final String DBNAME = "subject.db";
    private static final String SCHOOL = "t_city.db";
    private static final String TEACHER_TYPE = "t_teacher_role.db";

    private LocationClient mLocationClient;
    private Button startLocation;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

        fm = getSupportFragmentManager();
        bt_home = (Button) findViewById(R.id.bt_home);
        bt_search = (Button) findViewById(R.id.bt_search);
        bt_message = (Button) findViewById(R.id.bt_message);
        bt_user = (Button) findViewById(R.id.bt_user);

        fm.beginTransaction()
                .replace(R.id.rl_fragment_contanier, FragmentFactory.getFragment(0), "HOME")
                .commit();
        showLogD("==============================================");
    }
    @Override
    public void initData() {
        mPushAgent = PushAgent.getInstance(context);
        if (!mPushAgent.isEnabled()) {
            mPushAgent.enable(mRegisterCallback);
        }

        String device_token = UmengRegistrar.getRegistrationId(context);
        showLogD("device_token " + device_token);
        Log.i("TAG", "device_token " + device_token);
        SPUtils.saveSPData("token", device_token);
        mLocationClient = ((MyApplication) getApplication()).mLocationClient;


        //        环信账号登录


//        register(device_token);
        copyDB(DBNAME);
        copyDB(SCHOOL);
        copyDB(TEACHER_TYPE);
        initLocation();
        mLocationClient.start();
    }

    /**
     * 把apk中的数据库复制到手机内存
     *
     * @param dbName
     */
    private void copyDB(final String dbName) {
        new Thread() {
            public void run() {
                File file = new File(getFilesDir(), dbName);
                if (file.exists() && file.length() > 0) {
                    showLogI("已经拷贝过了数据库,无需重新拷贝");
                } else {
                    try {
                        InputStream is = getAssets().open(dbName);
                        byte[] buffer = new byte[1024];
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
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
            SPUtils.saveSPData("token", mPushAgent.getRegistrationId());
            Log.i("TAG", "updateStatus:" + info);
            Log.i("TAG", "=============================");
        }
    };

    /**
     * 首页底部四个按钮的事件
     *
     * @param v
     */
    public void showFragment(View v) {
        switch (v.getId()) {
            case R.id.bt_home:
                fm.beginTransaction()
                        .replace(R.id.rl_fragment_contanier, FragmentFactory.getFragment(0), "HOME")
                        .commit();
                break;
            case R.id.bt_search:
                break;
            case R.id.bt_user:
                Intent intent = new Intent(this, LoginAndRightActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_message:
                Intent intentMulti = new Intent(this, UpdateUserInfoActivity.class);
                startActivity(intentMulti);
                break;
        }
    }

    public void show(View v) {
        startActivity(new Intent(context, StudyGroupListActivity.class));
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();

        super.onStop();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，


        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }
}
