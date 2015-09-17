package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.widget.TitleNavBar;

import org.w3c.dom.Text;

/**
 * Created by kings on 9/4/2015.
 */
public class CardSettingActivity extends BaseActivity implements View.OnClickListener {
    //    -1:审核未通过  0 ：未完善  1:审核中  2:通过审核
    public final static int CARD_ONE = -1;//审核未通过
    public final static int CARD_TWO = 0;//未完善
    public final static int CARD_THREE = 1;//审核中
    public final static int CARD_FOUR = 2;//通过审核
    private TextView tvPassCard;
    private TextView tvPassTeacher;
    private TextView tvPassAptitude;
    private TextView tvPassDegrees;
    private TextView tvPassChampion;
    private UserBean userBean;
    private RelativeLayout linearCard;//身份认证
    private RelativeLayout linearTeacherCard;//老师认证
    private RelativeLayout linearDegreesCard;//学历认证
    private RelativeLayout linearAptitudeCard;//资质认证
    private RelativeLayout linearChampionCard;//资质认证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_setting);
        initTitle();
        initData();
        initView();

    }

    @Override
    public void initTitle() {
        TitleNavBar titleNavBar = (TitleNavBar) findViewById(R.id.title);
        titleNavBar.setTitle("认证设置");
        titleNavBar.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        tvPassCard = (TextView) findViewById(R.id.tv_pass_card);
        tvPassTeacher = (TextView) findViewById(R.id.tv_pass_teacher);
        tvPassAptitude = (TextView) findViewById(R.id.tv_pass_aptitude);
        tvPassDegrees = (TextView) findViewById(R.id.tv_pass_degrees);
        tvPassChampion = (TextView) findViewById(R.id.tv_pass_champion);
        /*******设置认证状态********/


        linearCard=(RelativeLayout) findViewById(R.id.linear_card);
        linearTeacherCard=(RelativeLayout) findViewById(R.id.linear_teacher_card);
        linearDegreesCard=(RelativeLayout)findViewById(R.id.linear_degrees_card);
        linearAptitudeCard=(RelativeLayout)findViewById(R.id.linear_aptitude_card);
        linearChampionCard=(RelativeLayout)findViewById(R.id.linear_champion_card);
//        findViewById(R.id.linear_education_card).setOnClickListener(this);
        linearCard.setOnClickListener(this);
        linearTeacherCard.setOnClickListener(this);
        linearDegreesCard.setOnClickListener(this);
        linearAptitudeCard.setOnClickListener(this);
        linearChampionCard.setOnClickListener(this);
        tvSwitch(userBean.getUserInfo().getCard().getPassCard(), tvPassCard, linearCard);
        tvSwitch(userBean.getUserInfo().getPassJsz(), tvPassTeacher,linearTeacherCard);
        tvSwitch(userBean.getUserInfo().getPassAptitude(), tvPassAptitude,linearAptitudeCard);
        tvSwitch(userBean.getUserInfo().getPassDegrees(), tvPassDegrees,linearDegreesCard);
        tvSwitch(userBean.getUserInfo().getPassChampion(), tvPassChampion,linearChampionCard);
    }

    private void tvSwitch(int id, TextView mTextView,RelativeLayout mRelativeLayout) {
        switch (id) {
            case CARD_ONE:
                mTextView.setText("审核未通过");
                mTextView.setTextColor(getResources().getColor(R.color.text_label));
                mRelativeLayout.setClickable(true);
                break;
            case CARD_TWO:
                mTextView.setText("未完善");
                mTextView.setTextColor(getResources().getColor(R.color.text_label));
                mRelativeLayout.setClickable(true);
                break;
            case CARD_THREE:
                mTextView.setText("审核中");
                mTextView.setTextColor(getResources().getColor(R.color.text_price));
                mRelativeLayout.setClickable(false);
                break;
            case CARD_FOUR:
                mTextView.setText("通过审核");
                mTextView.setTextColor(getResources().getColor(R.color.tv_alread_card));
                mRelativeLayout.setClickable(false);
                break;
        }
    }

    @Override
    public void initData() {
        userBean = MyApplication.getInstance().getUserBean();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_card:
                startActivity(new Intent(this, CardActvity.class));
                break;
            case R.id.linear_degrees_card:
                Intent intent = new Intent(this, UpdateUniversityActivity.class);
                intent.putExtra("id", MyApplication.getInstance().getUserId());
                startActivity(intent);
                break;
//          case R.id.linear_education_card:
//              startActivity(new Intent(this,CardAptitudeActvity.class));
//              break;
            case R.id.linear_teacher_card:
                startActivity(new Intent(this, CardTeacherActvity.class));
                break;
            case R.id.linear_aptitude_card:
                startActivity(new Intent(this, CardDegreesActvity.class));
                break;
            case R.id.linear_champion_card:
                startActivity(new Intent(this, CardChampionActvity.class));

                break;
        }
    }
}
