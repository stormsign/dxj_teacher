package com.dxj.teacher.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.dxj.teacher.R;

/**
 * Created by kings on 8/26/2015.
 */
public class MyCount extends CountDownTimer {
    private Button btnCode;
    private Context context;

    public MyCount(long millisInFuture, long countDownInterval, Button btnCode, Context context) {
        super(millisInFuture, countDownInterval);
        this.btnCode = btnCode;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btnCode.setEnabled(false);
        btnCode.setTextColor(context.getResources().getColor(R.color.text_color));
        btnCode.setText(millisUntilFinished / 1000+"s");
    }

    @Override
    public void onFinish() {
        btnCode.setEnabled(true);
        btnCode.setTextColor(context.getResources().getColor(R.color.rbuttonNo));
        btnCode.setText("发送验证码");
    }
}
