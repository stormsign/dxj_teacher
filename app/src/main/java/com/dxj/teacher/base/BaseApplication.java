package com.dxj.teacher.base;

import android.app.Application;
import android.content.Context;

import com.dxj.teacher.utils.ExceptionHandler;


/**
 * Created by khb on 2015/8/20.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        baseApplication = this;
        ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
        exceptionHandler.init(baseApplication);
    }

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getApplication(){
        return baseApplication;
    }

}
