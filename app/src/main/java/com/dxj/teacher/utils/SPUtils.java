package com.dxj.teacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dxj.teacher.application.MyApplication;

/**
 * Created by khb on 2015/8/20.
 */
public class SPUtils {
    private static String CONFIG = "config";
    private static SharedPreferences sharedPreferences;
    //写入
    public static void saveSPData(String key,String value){
        Context context = MyApplication.getInstance();
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, value).commit();
    }
    //读出
    public static String getSPData(String key,String defValue){
        Context context = MyApplication.applicationContext;
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }
    //写入
    public static void saveSPData(String key,int value){
        Context context = MyApplication.applicationContext;
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).commit();
    }
    //读出
    public static int getSPData(String key,int defValue){
        Context context = MyApplication.applicationContext;
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defValue);
    }
    //写入
    public static void saveSPData(String key,boolean value){
        Context context = MyApplication.applicationContext;
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }
    //读出
    public static Boolean getSPData(String key,boolean defValue){
        Context context = MyApplication.applicationContext;
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }
}
