package com.dxj.student.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * uncaughtException处理类，当程序发生Uncaught异常时，由该类处理
 * 记录错误日志
 * @author zjb
 *
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler mDefaultHandler;

	private static ExceptionHandler INSTANCE;
	private Context mContext;
	private Map<String,String> infos = new HashMap<String,String>();
	//用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private ExceptionHandler(){
		
	}
	
	public static ExceptionHandler getInstance(){
		if (INSTANCE==null) {
			INSTANCE = new ExceptionHandler();
		}
		return INSTANCE;
	}
	
	public void init(Context context){
		mContext = context;
		//获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);  
	}
	
	
	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		
		if (handleException(ex)&&mDefaultHandler!=null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			Log.w("exceptionHandler", "程序退出");
			System.exit(0);
		}
	}
	
	private boolean handleException(Throwable ex){
		if (ex == null) {
			return false;
		}
		//Toast提示异常信息
		new Thread(){
			public void run() {
				Looper.prepare();
				ToastUtils.showToast(mContext, "很抱歉,程序出现异常,即将退出.");
				Looper.loop();
			};
		}.start();
		
		//收集设备信息
		collectDeviceInfo(mContext);
		//保存日志文件
		saveExceptionLog2File(ex);
		
		return true;
	}

	/**
	 * 收集设备信息
	 * @param context
	 */
	private void collectDeviceInfo(Context context) {
		
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null":pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("cersionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
//			Log.e("exceptionHandler", "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for(Field field : fields){
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d("exceptionHandler", field.getName() + " : " + field.get(null).toString());
				
			} catch (IllegalAccessException | IllegalArgumentException e) {
				
				e.printStackTrace();
//				Log.e("exceptionHandler", "an error occured when collect package info", e);
			}
		}
		
	}

	/**
	 * 保存日志到文件中
	 * @param ex
	 * @return
	 */
	private String saveExceptionLog2File(Throwable ex) {
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
//			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
	

}
