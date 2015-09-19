package com.dxj.teacher.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupCategoryAdapter extends BaseRecyclerViewAdapter<String> {
    private int[] imgList = {R.mipmap.faxian_thfc_ico_yinyue, R.mipmap.faxian_thfc_ico_meishu, R.mipmap.faxian_thfc_ico_zxxue, R.mipmap.faxian_thfc_ico_waiyu
            , R.mipmap.faxian_thfc_ico_wudao, R.mipmap.faxian_thfc_ico_tiyu, R.mipmap.faxian_thfc_ico_xingqu, R.mipmap.faxian_thfc_ico_zgkao, R.mipmap.faxian_thfc_ico_waiyu, R.mipmap.faxian_thfc_ico_zgkao, R.mipmap.faxian_thfc_ico_qilei};
    private Activity mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater;
    private FirstItemHolder mHolder;

    public GroupCategoryAdapter(Activity mContext, List mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnFirstClickListener {
        void onFirstClick(View view, int position);
    }

    private OnFirstClickListener mOnFirstClickListener;

    public void setOnFirstClickListener(OnFirstClickListener mOnNoticeClickListener) {
        this.mOnFirstClickListener = mOnNoticeClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FirstItemHolder(mLayoutInflater.inflate(R.layout.simple_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String firstName = mList.get(position);
        if (null == firstName) {
            return;
        }
        mHolder = (FirstItemHolder) holder;
        mHolder.tv_firstname.setText(firstName);
        mHolder.imageView.setBackgroundResource(imgList[position]);
//        if (position == 0){
//            mHolder.isSelected = true;
//        }

    }

//    public void setItemSelected(boolean isSelected){
//        mHolder.isSelected = isSelected;
//    }

    public class FirstItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_firstname;
        public ImageView imageView;
//        public boolean isSelected = false;

        public FirstItemHolder(View itemView) {
            super(itemView);

            tv_firstname = (TextView) itemView.findViewById(R.id.text_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);

            if (mOnFirstClickListener != null) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnFirstClickListener.onFirstClick(v, getLayoutPosition());
                    }
                });
            }
        }

    }
}
