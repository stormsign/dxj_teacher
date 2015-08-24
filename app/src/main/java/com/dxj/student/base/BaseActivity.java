package com.dxj.student.base;

import android.content.Context;
import android.os.Bundle;

import com.umeng.message.PushAgent;

/**项目
 * Created by khb on 2015/8/19.
 */
public abstract class BaseActivity extends FinalActivity {

    protected BaseApplication mApplication;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = BaseApplication.getApplication();
        context = this;
        PushAgent.getInstance(context).onAppStart();
    }

//        初始化标题栏
//        处理消息栏

    /**
     * 处理页面展示
     */
    public abstract void initView();

    /**
     * 处理数据
     */
    public abstract void initData();

}
