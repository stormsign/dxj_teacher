package com.dxj.student.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by khb on 2015/8/20.
 */
public class SystemUtils {

    /**
     * 获取手机里面正在运行的进程数量
     * @param context 上下文,要获取手机的状态信息,必须得到ActivityManager
     * @return
     */
    public static int getRunningProcessCount(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的应用程序进程的集合.
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }
    /**
     * 获取手机的剩余可用内存(RAM)空间
     * @param context
     * @return 可用内存的大小 单位 byte
     */
    public static long getAvailRam(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取手机的总的内存(RAM)空间大小
     * @param context
     * @return 可用内存的大小 单位 byte
     */
    public static long getTotalRam(Context context){
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
        try {
            File file = new File("/proc/meminfo");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br  = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            //MemTotal:         513000 kB
            StringBuffer sb = new StringBuffer();
            for(char c: line.toCharArray()){
                if(c>='0'&&c<='9'){
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString())*1024;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 获取手机IMEI号
     *
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


}
