package com.dxj.student.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxj.student.base.BaseApplication;
import com.dxj.student.utils.MyUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MyRequest {
    private static MyRequest myRequest = new MyRequest();

    private Context mContext;
    private String jsonString = "";

    private MyRequest() {
    }

    public static MyRequest getInstance() {
	return myRequest;
    }

    /**
     * 多张图片上传方法
     * 
     * @param reqParams 接口名称
     * @param cls 接受对象
     * @param paramsMap 多参数使用map传递
     * @return
     * @throws MyException
     */
    public String getRequest(HttpMethod httpMethod, String reqParams, com.dxj.student.bean.PhotoBean mPhotoBean) throws MyException {
	JSONObject jsonObj = new JSONObject();
	String json = "";
	try {
	    // 前四个参数为固定参数
	    jsonObj.put(com.dxj.student.http.FinalData.PHONE_TYPE,  FinalData.PHONE_TYPE_VALUE);
	    jsonObj.put( FinalData.IMEI,  FinalData.IMEI_VALUE);
	    jsonObj.put( FinalData.VERSION_CODE,  FinalData.VERSION_CODE_VALUE);
	    // jsonObj.put(FinalData.CMD, reqParams);
	    jsonObj.put("folder", mPhotoBean.getFolder());
	    JSONArray ja = new JSONArray();
	    // 判断是否还有其他参数需发送到服务器
	    if (mPhotoBean != null) {
		for (int i = 0; i < mPhotoBean.getImage().size(); i++) {
		    ja.add(mPhotoBean.getImage().get(i));
		}
		jsonObj.put("images", ja);
	    }
	    // 转为json格式String
	    json = jsonObj.toJSONString(jsonObj);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// 拼接请求地址
	String urlPath =  FinalData.IMAGE_URL_UPLOAD + "multiUploadImage";

	// 请求数据
	json = MyHttpConnection.httpPost(urlPath, json);
	// 判断是否返回为空，不为空缓存数据
	if (!MyUtils.isEmpty(json)) {
	    jsonString = json;
	    mContext = BaseApplication.getContext();
	    MyUtils.saveDataToFile(mContext, reqParams, new String(json));
	}
	// if (json == null)
	return jsonString;
	// return getJson(json, cls);
    }

    /**
     * 单张图片上传方法
     *
     * @param reqParams 接口名称
     * @param class1 接受对象
     * @param paramsMap 多参数使用map传递
     * @return
     * @throws MyException
     */
    public String getRequestUpload(HttpMethod httpMethod, String reqParams, Map<String, Object> paramsMap) throws MyException {
	JSONObject jsonObj = new JSONObject();
	String json = "";
	try {
	    // 前四个参数为固定参数
	    jsonObj.put( FinalData.PHONE_TYPE,  FinalData.PHONE_TYPE_VALUE);
	    jsonObj.put( FinalData.IMEI,  FinalData.IMEI_VALUE);
	    jsonObj.put( FinalData.VERSION_CODE,  FinalData.VERSION_CODE_VALUE);
	    // jsonObj.put(FinalData.CMD, reqParams);
	    // 判断是否还有其他参数需发送到服务器
	    if (paramsMap != null) {
		Set<Entry<String, Object>> set = paramsMap.entrySet();
		for (Entry<String, Object> entry : set) {
		    jsonObj.put(entry.getKey(), entry.getValue());
		}
	    }
	    // 转为json格式String
	    json = jsonObj.toJSONString();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// 拼接请求地址
	// reqParams;
	// String urlPath = FinalData.URL_VALUE + reqParams;
	String urlPath = FinalData.IMAGE_URL_UPLOAD + "uploadImg";
	Log.i("TAG", "urlPath=" + urlPath);

	json = MyHttpConnection.httpPost(urlPath, json);
	// 判断是否返回为空，不为空缓存数据
	Log.i("TAG", "json=" + json);
	return json;
	// return getJson(json, cls);
    }

    /**
     * 公共解析Json方法
     * 
     * @param buffer 服务器返回json
     * @param cls 接受对象
     * @return
     */
}
