package com.dxj.teacher.activity;

import android.os.Bundle;

import com.dxj.teacher.R;
import com.dxj.teacher.base.CardBaseActvity;
import com.dxj.teacher.utils.HttpUtils;

/**
 * Created by kings on 9/4/2015.
 */
public class CardTeacherActvity  extends CardBaseActvity{

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_card_teacher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return HttpUtils.JSZ;
    }
}
