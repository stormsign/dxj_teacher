package com.dxj.teacher.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by kings on 9/18/2015.
 */
public class MyFragment extends BaseFragment {
    private UserBean userBean;
    private TextView tvName;
    private ImageView imgAvatar;

    @Override
    public void initData() {
        userBean = MyApplication.getInstance().getUserBean();
        tvName.setText(userBean.getUserInfo().getNickName());
        Glide.with(getActivity()).load(userBean.getUserInfo().getHeadUrl()).centerCrop().placeholder(R.mipmap.default_avatar).into(imgAvatar);
    }

    /**
     * 先执行initView 然后执行initData（）
     */
    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        TitleNavBar title = (TitleNavBar) view.findViewById(R.id.title);
        title.setTitle("我");
        title.setTitleNoRightButton();
        view.findViewById(R.id.back).setVisibility(View.GONE);
        return view;
    }
}
