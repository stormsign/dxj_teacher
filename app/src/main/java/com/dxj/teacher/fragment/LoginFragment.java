package com.dxj.teacher.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.AesUtil;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.SPUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText etPassword;
    private EditText etPhone;
    private Button btnLogin;
    private ProgressFragment progressFragment;

    @Override
    public void initData() {
//        List<String>

    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_login, null);
        etPhone = (EditText) view.findViewById(R.id.et_user);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:

                localLogin();

                break;
        }
    }

    private void localLogin() {
        progressFragment = ProgressFragment.newInstance();

        String strPassword = etPassword.getText().toString().trim();
        String strUser = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strUser)) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strUser)) {
            etPhone.setError("手机号码格式不对");
            etPhone.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(strPassword)) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }
        progressFragment.show(getFragmentManager(), "");
        String urlPath = FinalData.URL_VALUE + HttpUtils.LOGIN;
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", strUser);
        try {
            map.put("plainPassword", AesUtil.Encrypt(strPassword, AesUtil.cKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("TAG", "token=" + SPUtils.getSPData("token", "token"));
        map.put("deviceTokens", SPUtils.getSPData("token", "token"));
        initLogin(map, urlPath);
    }

    private void initLogin(Map<String, Object> map, String urlPath) {
        //();
        GsonRequest<UserBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, UserBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener getListener() {
        return new Response.Listener<UserBean>() {

            @Override
            public void onResponse(UserBean userBean) {
                Log.i("TAG", "userBean=" + userBean.toString());
                progressFragment.dismissAllowingStateLoss();
                if (userBean != null) {
                    if (userBean.getCode() == 0){
                        ToastUtils.showToast(getActivity(), userBean.getMsg());
                        AccountDBTask.saveUserBean(userBean);
                        MyApplication.getInstance().setUserBean(userBean);
                        loginHuanXin(userBean.getUserInfo().getMobile());
                    }
                    else
                        ToastUtils.showToast(getActivity(), userBean.getMsg());
                } else {
                    ToastUtils.showToast(getActivity(), "登录失败");
                }
            }
        };
    }
private  void loginHuanXin(final String mobile){
    EMChatManager.getInstance().login("teacher"+mobile, "1234456", new EMCallBack() {//回调
        @Override
        public void onSuccess() {
       Log.i("TAG","loginHuanXin");
            // 登陆成功，保存用户名密码
            MyApplication.getInstance().setUserName("teacher"+mobile);
            MyApplication.getInstance().setPassword("1234456");
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        // ** manually load all local groups and
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.d("main", "登陆聊天服务器成功！");
                        // 处理好友和群组
                        initializeContacts();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 取好友或者群聊失败，不让进入主页面
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                DemoHXSDKHelper.getInstance().logout(true, null);
                                Toast.makeText(getActivity().getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                }
            });
            // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
            boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                    MyApplication.currentUserNick.trim());
            if (!updatenick) {
                Log.e("LoginActivity", "update current user nick fail");
            }
        }

        @Override
        public void onProgress(int progress, String status) {

        }

        @Override
        public void onError(int code, String message) {
            Log.d("main", "登陆聊天服务器失败！");
        }
    });
}  //    环信账号登录后的处理
    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(getActivity());
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }
    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressFragment.dismissAllowingStateLoss();
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(getActivity(), "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(getActivity(), "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(getActivity(), "服务器异常 登录失败");
            }
        };
    }

    public static class ProgressFragment extends DialogFragment {


        public static ProgressFragment newInstance() {
            ProgressFragment frag = new ProgressFragment();
            frag.setRetainInstance(true);
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("登录中...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);

            return dialog;
        }

        @Override
        public void onCancel(DialogInterface dialog) {


            super.onCancel(dialog);
        }
    }
}

