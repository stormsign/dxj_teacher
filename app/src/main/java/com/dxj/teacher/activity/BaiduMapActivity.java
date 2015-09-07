package com.dxj.teacher.activity;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dxj.teacher.R;
import com.easemob.util.LatLng;


public class BaiduMapActivity extends FragmentActivity  {
    /** 百度定位 */
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;private LatLng location;


    @Override
    protected void onCreate(Bundle arg0) {
	// TODO Auto-generated method stub
	super.onCreate(arg0);
//	SDKInitializer.initializeContacts(getApplicationContext());
//	setContentView(R.layout.activity_baidu_map);
	// address = (TextView) findViewById(R.id.address);
	/** 百度定位 */
	mLocationClient = new LocationClient(this.getApplicationContext());
	mMyLocationListener = new MyLocationListener();
	mLocationClient.registerLocationListener(mMyLocationListener);
	initLayout();
	// initListHead();
	InitLocation();
    }

    private void initLayout() {


    }

    /**
     * 初始化定位实例
     */
    private void InitLocation() {
	LocationClientOption option = new LocationClientOption();
	option.setLocationMode(tempMode);//
	option.setCoorType("gcj02");//
	int span = 100000;
	option.setScanSpan(span);//
	option.setIsNeedAddress(true);
	mLocationClient.setLocOption(option);
	mLocationClient.start();

    }

    @Override
    protected void onStop() {
	// TODO Auto-generated method stub
	mLocationClient.stop();
	super.onStop();
    }

    public class MyLocationListener implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
	    // Receive Location
	    StringBuffer sb = new StringBuffer(256);
	     sb.append("\nerror code : ");
	     sb.append(location.getLocType());
	     sb.append("\nlatitude : ");
	     sb.append(location.getLatitude());
	     sb.append("\nlontitude : ");
	     sb.append(location.getLongitude());
	     sb.append("\nradius : ");
	     sb.append(location.getRadius());
	    if (location.getLocType() == BDLocation.TypeGpsLocation) {
		 sb.append("\nspeed : ");
		 sb.append(location.getSpeed());
		 sb.append("\nsatellite : ");
		 sb.append(location.getSatelliteNumber());
		 sb.append("\ndirection : ");
		 sb.append("\naddr : ");
		 sb.append(location.getAddrStr() + location.getCity());
		 
		// sb.append(location.getDirection());
	    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
		 sb.append("\naddr : ");
		sb.append(location.getAddrStr());
		 sb.append("\noperationers : ");
		 sb.append(location.getOperators());
	    }
	    Log.i("TAG", "address="+sb.toString());
	}
    }


    @Override
    protected void onPause() {
	super.onPause();
    }

    @Override
    protected void onResume() {
	super.onResume();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	// 退出时销毁定位
	if (mLocationClient != null) {
	    mLocationClient.stop();
	}

    }




}
