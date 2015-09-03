package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.utils.SPUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.easemob.chat.EMGroup;

/**
 * Created by khb on 2015/9/2.
 */
public class GroupSettingActivity extends BaseActivity{

    private RelativeLayout rl_head;
    private EditText groupname;
    private EditText description;
    private ImageView toggle;
    private final static int MAX_WORD_COUNT = 200;
    private TextView word_count;
    private SwitchCompat message_switch;
    private StudyGroup group;
    private EMGroup emGroup;
    private TextView tv_groupname;
    private TextView tv_description;
    private ImageView group_head;
    private String headUrl;
    private boolean isMsgBlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsetting);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("设置");
        title.setTitleNoRightButton();
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onBackClick() {
                setResult(RESULT_OK, new Intent().putExtra("groupname", groupname.getText().toString().trim())
                        .putExtra("desc", description.getText().toString().trim())
                        .putExtra("headUrl", headUrl)
                        .putExtra("isMsgBlocked", isMsgBlocked));
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra("groupname", groupname.getText().toString().trim())
                .putExtra("desc", description.getText().toString().trim())
                .putExtra("headUrl", headUrl)
                .putExtra("isMsgBlocked", isMsgBlocked));
        super.onBackPressed();
    }

    @Override
    public void initView() {
        rl_head = (RelativeLayout) findViewById(R.id.rl_group_head);
        group_head = (ImageView)findViewById(R.id.iv_group_head);
        groupname = (EditText) findViewById(R.id.et_groupname);
        description = (EditText) findViewById(R.id.et_description);
        tv_groupname = (TextView) findViewById(R.id.tv_groupname);
        tv_description = (TextView) findViewById(R.id.tv_description);
        word_count = (TextView) findViewById(R.id.tv_word_count);
        word_count.setText(description.getText().length() + "/" + MAX_WORD_COUNT);

        tv_description.setFocusable(true);
        tv_groupname.setFocusable(true);
        message_switch = (SwitchCompat) findViewById(R.id.sc_message_switch);
        isMsgBlocked = SPUtils.getSPData("group-"+group.getGroupId()+"-MsgBlocked", false);
        message_switch.setChecked(isMsgBlocked);
        message_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMsgBlocked = isChecked;
            }
        });

        // 监听输入的字数
        description.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;// 监听前的文本
            private int editStart;// 光标开始位置
            private int editEnd;// 光标结束位置
            private final int charMaxNum = MAX_WORD_COUNT;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                word_count.setText(s.length() + "/" + MAX_WORD_COUNT);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

                editStart = description.getSelectionStart();
                editEnd = description.getSelectionEnd();
                if (temp.length() > charMaxNum) {
                    showToast("最多输入" + MAX_WORD_COUNT + "个字！");
                    s.delete(editStart - (temp.length() - charMaxNum), editEnd);
                    description.setText(s);
                    description.setSelection(editStart);

                }
            }
        });

        tv_groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_groupname.setVisibility(View.GONE);
                groupname.setVisibility(View.VISIBLE);
            }
        });
        tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_description.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
            }
        });

        if (!TextUtils.isEmpty(group.getHeadUrl())){
            Glide.with(this).load(group.getHeadUrl()).placeholder(R.mipmap.default_error).into(group_head);
        }
        tv_groupname.setText(group.getGroupName());
        groupname.setText(group.getGroupName());
        tv_description.setText(group.getDescription());
        description.setText(group.getDescription());

        findViewById(R.id.quit);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        group = (StudyGroup) intent.getSerializableExtra("studyGroup");
        headUrl = group.getHeadUrl();
//        判断当前用户是学生还是老师
        Button quit = (Button) findViewById(R.id.quit);
        if(isLeader(mApplication.getUserId(), group)){
            quit.setText("解散学团");
        }else{
            quit.setText("退出学团");
        }
    }

    /**
     * 判断当前用户是否是团长
     * @return
     */
    private boolean isLeader(String id, StudyGroup group) {
        return (group.getTeacherId()).equals(id);
    }

    public void quit(View view){
        if (isLeader(mApplication.getUserId(), group)){

        }
    }
}
