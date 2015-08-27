package com.easemob.chatuidemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.utils.LogUtils;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;

import java.util.Map;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
		DemoHXSDKHelper d = (DemoHXSDKHelper) HXSDKHelper.getInstance();
		LogUtils.i("demohxsdkhelper:" + d + " getHXId:"+d.getHXId());
		Map<String, User> contactList = d.getContactList();
		LogUtils.i("contactlist:"+contactList.size());
		User user1 = contactList.get(username);
		LogUtils.i("user1:"+user1);
//		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
		User user = user1;
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
			Glide.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_avatar).into(imageView);
        }else{
			Glide.with(context).load(R.mipmap.default_avatar).into(imageView);
        }
    }
    
    /**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Glide.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_avatar).into(imageView);
		} else {
			Glide.with(context).load(R.mipmap.default_avatar).into(imageView);
		}
	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
		LogUtils.i("username:"+username+" textview:"+textView);
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    
    /**
     * 保存或更新某个用户
     * @param newUser
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}
    
}
