package com.dxj.teacher.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dxj.teacher.R;
import com.dxj.teacher.activity.EditCourseActivity;
import com.dxj.teacher.base.BaseFragment;


/**首页
 * Created by khb on 2015/8/19.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{
 private Button btnGoodSubject;
    @Override
    public void initData() {
//        List<String>
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
        btnGoodSubject =(Button)view.findViewById(R.id.btn_good_subject);
        btnGoodSubject.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case  R.id.btn_good_subject:
                Log.i("TAG","onClick");
                Intent intent  = new Intent(getActivity(), EditCourseActivity.class);
                startActivity(intent);
                break;
        }
    }
}

