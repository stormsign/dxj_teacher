package com.dxj.teacher.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseFragment;


/**首页
 * Created by khb on 2015/8/19.
 */
public class HomeFragment extends BaseFragment {

    @Override
    public void initData() {
//        List<String>
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }



}

