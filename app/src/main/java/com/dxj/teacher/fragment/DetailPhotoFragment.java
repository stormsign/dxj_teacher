package com.dxj.teacher.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.dialogplus.PhotoAdapter;

import java.util.ArrayList;

/**
 * Created by khb on 2015/8/19.
 */
public class DetailPhotoFragment extends BaseFragment {
    private GridView photo;
    private ArrayList<String> photos;
    private PhotoAdapter photoAdapter;

    @Override
    public void initData() {
        photos = getArguments().getStringArrayList("photos");
        photoAdapter = new PhotoAdapter(getActivity(), photos);
        photo.setAdapter(photoAdapter);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_photo, null);
        photo = (GridView) view.findViewById(R.id.gridview_photo);
        return view;
    }


}
