package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.bean.StudyGroupBean;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.SPUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

import java.util.HashMap;
import java.util.Map;

/** 学团详情
 * Created by khb on 2015/9/1.
 */
public class StudyGroupDetailActivity extends BaseActivity {

    private static final int SETTING = 1;
    private ImageView group_head;
    private TextView group_name;
    private TextView description;
    private TextView announce;
    private ImageView leader_head;
    private TextView leader_name;
    private TextView leader_school;
    private StudyGroup studyGroup;
    private TitleNavBar title;
    private EMGroup emGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studygroupdetail);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        title = (TitleNavBar) findViewById(R.id.title);
        title.showNavOne(false);
        title.setNavTwoImageResource(R.mipmap.ic_settings_white_24dp);
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {
                showToast("设置");
                Intent intent = new Intent(activity, GroupSettingActivity.class);
                intent.putExtra("studyGroup", studyGroup);
                startActivityForResult(intent, SETTING);
            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onBackClick() {

            }
        });

    }

    @Override
    public void initView() {
        group_head = (ImageView) findViewById(R.id.iv_group_head);
        group_name = (TextView) findViewById(R.id.tv_groupname);
        description = (TextView) findViewById(R.id.tv_description);
        announce = (TextView) findViewById(R.id.tv_announce);
        leader_head = (ImageView) findViewById(R.id.iv_leader_head);
        leader_name = (TextView) findViewById(R.id.tv_leader_name);
        leader_school = (TextView) findViewById(R.id.tv_leader_school);

    }

    @Override

    public void initData() {
        getIntent();
        getGroupDetail();

    }

    private void getGroupDetail() {
        String url = FinalData.URL_VALUE_COMMON+"getGroupDetail";
        Map<String, Object> map = new HashMap<String, Object>();
        String groupid = "abdefcbf-3e35-47d5-a4b0-563dd700a03a";
        map.put("id", groupid);

        GsonRequest<StudyGroupBean> gRequest = new GsonRequest<>(Request.Method.POST, url, StudyGroupBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(gRequest);
    }

    private Response.Listener<StudyGroupBean> getListener() {
        return new Response.Listener<StudyGroupBean>() {
            @Override
            public void onResponse(StudyGroupBean response) {
                showLogD("response " + response);
                if (response!= null && response.getCode() == 0){
                    studyGroup = response.getGroup();
//                    getEMGroup(studyGroup.getGroupId());
                    processData(studyGroup);
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showLogE(volleyError.toString());
            }
        };
    }

    /**
     * 获取环信EMGroup对象,
     * 如果在设置中获取，由于是异步向网络请求，在返回EMGroup对象前，用户就可能更改屏蔽消息
     * 可能会导致空指针
     */
    private void getEMGroup(final String groupId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    emGroup = EMGroupManager.getInstance().getGroupFromServer("101363032365466192");

                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 处理请求结果，显示到界面上
     */

    private void processData(StudyGroup studyGroup) {
        if (!TextUtils.isEmpty(studyGroup.getHeadUrl())){
            Glide.with(this).load(studyGroup.getHeadUrl()).placeholder(R.mipmap.default_error).into(group_head);
        }
        group_name.setText(studyGroup.getGroupName());
        title.setTitle(studyGroup.getGroupName());
        description.setText(studyGroup.getDescription());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTING && resultCode == RESULT_OK){
//            showToast(" name " + data.getStringExtra("groupname")
//                    + " desc " + data.getStringExtra("desc")
//                    + " headurl " + data.getStringExtra("headUrl")
//                    + " isMsgBlocked " + data.getBooleanExtra("isMsgBlocked", false));
//            SPUtils.saveSPData("group-" + studyGroup.getGroupId() + "-MsgBlocked", data.getBooleanExtra("isMsgBlocked", false));
            updateGroup(data);
            updateGroupMsgBlock(data.getBooleanExtra("isMsgBlocked", false));
        }
    }

    /**
     * 更新是否屏蔽学团消息,并存入本地
     */
    private void updateGroupMsgBlock(final boolean isMsgBlocked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    emGroup = EMGroupManager.getInstance().getGroupFromServer("101363032365466192");
                    getEMGroup(studyGroup.getGroupId());
                    emGroup.setMsgBlocked(isMsgBlocked);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        SPUtils.saveSPData("group-"+studyGroup.getGroupId()+"-MsgBlocked", isMsgBlocked);
    }

    private void updateGroup(Intent data) {
        String url = FinalData.URL_VALUE+"updateGroup";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", studyGroup.getId());
        map.put("groupName", data.getStringExtra("groupname"));
        map.put("description", data.getStringExtra("desc"));
        map.put("headUrl", data.getStringExtra("headUrl"));
        CustomStringRequest cRequest = new CustomStringRequest(Request.Method.POST, url, map, getUpdateListener(), getUpdateErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(cRequest);
    }

    private Response.ErrorListener getUpdateErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showLogE(volleyError.toString());
            }
        };
    }

    private Response.Listener<String> getUpdateListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                showToast(s);
                getGroupDetail();
            }
        };
    }


}
