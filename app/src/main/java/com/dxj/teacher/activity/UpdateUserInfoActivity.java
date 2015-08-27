package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;

import org.w3c.dom.Text;

/**
 * Created by kings on 8/27/2015.
 */
public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final int NICE_NAME = 0x000001;
    public static final int CHOOSE_ALBUM = 0x000002;
    public static final int CHOOSE_ADDRESS = 0x000003;
    private RelativeLayout relativeNiceName;
    private RelativeLayout relativeSex;
    private TextView tvNicename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        initData();
        initView();
    }

    @Override
    public void initView() {
        relativeNiceName = (RelativeLayout) findViewById(R.id.relative_nicename);
        relativeSex = (RelativeLayout) findViewById(R.id.relative_sex);
        tvNicename = (TextView) findViewById(R.id.tv_nicename);
        relativeNiceName.setOnClickListener(this);
        relativeSex.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relative_nicename:
                Intent intent = new Intent(this,UpdateNiceNameActivity.class);
                startActivityForResult(intent,NICE_NAME);
                break;
            case R.id.relative_sex:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==NICE_NAME){
            String niceName = data.getStringExtra("nicename");
            tvNicename.setText(niceName);
        }
    }
}
