package com.dxj.student.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dxj.student.R;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.utils.StringUtils;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class RightFragment extends BaseFragment implements View.OnClickListener {
    private EditText etPhone;
    private EditText etPassword;
    private Button btnRight;
    private EditText etTwoPassword;
    private EditText etCode;
    private Button btnSendCode;

    @Override
    public void initData() {
//        List<String>
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_right, null);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        etTwoPassword = (EditText) view.findViewById(R.id.et_two_password);
        etCode = (EditText) view.findViewById(R.id.et_code);
        btnSendCode = (Button) view.findViewById(R.id.btn_send_code);
        btnSendCode.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_right:
                right();
                break;
            case R.id.btn_send_code:
                sendCode();
                break;
        }

    }

    private void sendCode() {
        String strPhone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strPhone)){
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
             return;
        }
        if (!StringUtils.isMobile(strPhone)){
            etPhone.setError("手机格式不对");
            etPhone.requestFocus();
            return;
        }

    }

    private void right() {

    }
}
