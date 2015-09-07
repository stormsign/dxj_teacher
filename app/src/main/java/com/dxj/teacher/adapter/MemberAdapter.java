package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseRecyclerViewAdapter;
import com.dxj.teacher.bean.UserBean;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class MemberAdapter extends BaseRecyclerViewAdapter<UserBean.UserInfo> {

    private Activity mContext;
    private List<UserBean.UserInfo> mList;
    private LayoutInflater mLayoutInflater;
    private MemberItemHolder mHolder;
    private boolean isCheckable = false;

    public MemberAdapter(Activity mContext, List mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnMemberClickListener {
        void onMemberClick(View view, int position);

        void onMemberDeleteClick(View view, int position);
    }

    private OnMemberClickListener mOnMemberClickListener;

    public void setOnMemberClickListener(OnMemberClickListener mOnMemberClickListener) {
        this.mOnMemberClickListener = mOnMemberClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberItemHolder(mLayoutInflater.inflate(R.layout.item_member, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserBean.UserInfo userInfo = mList.get(position);
        if (null == userInfo) {
            return;
        }
        UserBean.UserInfo member = mList.get(position);
        mHolder = (MemberItemHolder) holder;
        if (isCheckable) {
            mHolder.check.setVisibility(View.VISIBLE);
        } else {
            mHolder.check.setVisibility(View.GONE);
        }
//        HorizontalScrollView抢占了触摸事件，外层的cardview拿不到触摸事件，只能把事件传给给scrollview内层，
//        所以在item_member里加了一个member_wraper用来触发点击团成员的事件
//        此处让scrollview不拦截事件，屏蔽scrollview的触摸滑动功能
        mHolder.hsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mHolder.member_name.setText(member.getNickName());
//        mHolder.member_desc.setText(member.getRemark());
        if (member.getHeadUrl() != null) {
            Glide.with(mContext).load(member.getHeadUrl()).placeholder(R.mipmap.default_avatar).into(mHolder.member_head);
        }
    }

    public boolean isCheckable() {
        return isCheckable;
    }

    public void setIsCheckable(boolean isCheckable) {
        this.isCheckable = isCheckable;
    }

    public class MemberItemHolder extends RecyclerView.ViewHolder {

        private RelativeLayout check;
        private ImageView iv_check;
        private ImageView member_head;
        private TextView member_name;
        private TextView member_info;
        private TextView member_desc;
        private CardView card_member;
        private HorizontalScrollView hsv;
        private RelativeLayout wraper;

        private boolean isChecked = false;

        public MemberItemHolder(View itemView) {
            super(itemView);
            wraper = (RelativeLayout) itemView.findViewById(R.id.member_wraper);
            hsv = (HorizontalScrollView) itemView.findViewById(R.id.hsv);
            card_member = (CardView) itemView.findViewById(R.id.cv_member);
            check = (RelativeLayout) itemView.findViewById(R.id.check);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);
            member_head = (ImageView) itemView.findViewById(R.id.member_head);
            member_name = (TextView) itemView.findViewById(R.id.member_name);
            member_info = (TextView) itemView.findViewById(R.id.member_info);
            member_desc = (TextView) itemView.findViewById(R.id.member_desc);

//            团成员点击事件
            if (mOnMemberClickListener != null) {
                wraper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnMemberClickListener.onMemberClick(v, getLayoutPosition());
                    }
                });
            }

//            选择删除图标的点击事件
            if (mOnMemberClickListener != null) {
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        控制选择删除图标的点击状态
                        if (isChecked){
                            iv_check.setImageResource(R.mipmap.btn_unselected);
                            isChecked = false;
                        }else{
                            iv_check.setImageResource(R.mipmap.btn_selected);
                            isChecked = true;
                        }
                        mOnMemberClickListener.onMemberDeleteClick(v, getLayoutPosition());
                    }
                });
            }

        }

    }
}
