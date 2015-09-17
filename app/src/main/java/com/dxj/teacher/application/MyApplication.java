package com.dxj.teacher.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.dxj.teacher.MainActivity;
import com.dxj.teacher.activity.NotificationsActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.Notification;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.dao.NoticeDao;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.manager.DBManager;
import com.dxj.teacher.utils.ExceptionHandler;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.LogUtils;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.StringUtils;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by khb on 2015/8/20.
 */
public class MyApplication extends Application {

    public static Context applicationContext;
    private static MyApplication instance;
    private UserBean mUserBean;
    private String id;
    private String locationdescribe;
    private String lontitude;
    private String latitude;
    // login user name
    public final String PREF_USERNAME = "username";

    public Vibrator mVibrator;
    public MyLocationListener mMyLocationListener;
    public LocationClient mLocationClient;
    public static List<Activity> storedActivities = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
//        applicationContext = getApplicationContext();
        applicationContext = this;
        instance = this;
        ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
        exceptionHandler.init(instance);
//        环信初始化
        hxSDKHelper.onInit(applicationContext);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        PushAgent.getInstance(getApplicationContext()).setMessageHandler(mUmengMessageHandler);
        PushAgent.getInstance(getApplicationContext()).setNotificationClickHandler(umengNotificationClickHandler);

    }

    UmengMessageHandler mUmengMessageHandler = new UmengMessageHandler(){
        @Override
        public void dealWithNotificationMessage(Context context, UMessage uMessage) {
//            父类方法是在通知栏显示消息
            super.dealWithNotificationMessage(context, uMessage);
//            以下是将消息存入数据库
            LogUtils.d("dealWithNotificationMessage got umeng message : " + uMessage.text);
            NoticeDao noticeDao = new NoticeDao(context);
            Date currentDate = new Date(System.currentTimeMillis());
//            currentDate.getTime();
            Notification notification = new Notification();
            notification.setTitle(uMessage.title);
            notification.setContent(uMessage.text);
            notification.setTime(currentDate.getTime());
            notification.setRead(false);
            notification.setActivity(uMessage.activity);
            if (null != uMessage.extra){
                notification.setExtra(uMessage.extra.get(MyUtils.UMENG_MESSAGE_EXTRA));
            }
            noticeDao.saveNotice(notification.getTitle(), notification.getContent(), DBManager.NOTICE_UNREAD, String.valueOf(notification.getTime()),
                    notification.getActivity(), notification.getExtra());
            List<Notification> allNotices = noticeDao.getAllNotices();
            LogUtils.d("allNotices "+ allNotices.size());
            if (MainActivity.mainActivity != null){
                MainActivity.mainActivity.refreshUI();
            }
//            系统通知activity已打开，并且在栈顶显示时,直接刷新
            if (NotificationsActivity.notificationsActivity != null
                    && (NotificationsActivity.class.getName()
                        .equals(CommonUtils.getTopActivity(context)))){
                NotificationsActivity.notificationsActivity.addNewNotice(notification);
            }
        }
    };

    UmengNotificationClickHandler umengNotificationClickHandler = new UmengNotificationClickHandler(){
        @Override
        public void openUrl(Context context, UMessage uMessage) {
            super.openUrl(context, uMessage);
        }

        @Override
        public void openActivity(Context context, UMessage uMessage) {
            LogUtils.d("openActivity " + uMessage.activity);
//            super.openActivity(context, uMessage);
            if(uMessage.activity != null && !TextUtils.isEmpty(uMessage.activity.trim())) {
                if (uMessage.extra != null) {
                    String extraString = uMessage.extra.get(MyUtils.UMENG_MESSAGE_EXTRA);
                    Map<String, String> mapExtra = MyUtils.parseUmengExtra(extraString);
                    MyUtils.openActivityFromUmengMessage(context, uMessage.activity, mapExtra);
                }
            }
        }

    };




    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                Log.i("TAG", "Gps");
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                Log.i("TAG", "address" + location.getAddrStr());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            Log.i("TAG", "address='" + sb.toString());
            setLocationdescribe(location.getLocationDescribe());
//            sendRequestData(location.getLatitude(), location.getLongitude());

        }

    }

    private void sendRequestData(double x, double y) {

        String urlPath = FinalData.URL_VALUE + HttpUtils.LOC;
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(getUserId())) {
            return;
        }
        map.put("id", getUserId());
        map.put("x", x);
        map.put("y", y);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0) {
//
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }

    public String getLocationdescribe() {
        return locationdescribe;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserBean getUserBean() {
//        if (mUserBean == null) {
        // 从文件获取

        mUserBean = AccountDBTask.getAccount();
//        }
        return mUserBean;
    }

    public String getUserId() {
        if (id == null) {
            // 从文件获取

            id = getUserBean().getUserInfo().getId();
        }
        return id;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param user
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM, final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM, emCallBack);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void setUserBean(UserBean userbean) {
        this.mUserBean = userbean;
    }

    //    把一个Activity加入到内存中
    public static void addActivity(Activity activity) {
        storedActivities.add(activity);
    }

    //    将内存记录的Activity全部退出
    public static void quitActivities() {
        for (Activity activity :
                storedActivities) {
            activity.finish();
        }
        storedActivities.clear();
    }

}
