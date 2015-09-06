package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public void setOnMemberClickListener(OnMemberClickListener mOnMemberClickListener){
        this.mOnMemberClickListener = mOnMemberClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberItemHolder(mLayoutInflater.inflate(R.layout.item_member, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserBean.UserInfo userInfo = mList.get(position);
        if (null == userInfo){
            return ;
        }
        UserBean.UserInfo member = mList.get(position);
        mHolder = (MemberItemHolder) holder;
        mHolder.member_name.setText(member.getNickName());
        mHolder.member_desc.setText(member.getRemark());
        if (member.getHeadUrl() != null){
            Glide.with(mContext).load(member.getHeadUrl()).placeholder(R.mipmap.default_avatar).into(mHolder.member_head);
        }
    }

    public void setCheckVisible(boolean flag){
        if (flag) {
            mHolder.check.setVisibility(View.VISIBLE);
        }else{
            mHolder.check.setVisibility(View.GONE);
        }
    }

    public class MemberItemHolder extends RecyclerView.ViewHolder {

        private RelativeLayout check;
        private ImageView iv_check;
        private ImageView member_head;
        private TextView member_name;
        private TextView member_info;
        private TextView member_desc;
        private CardView card_member;

        public MemberItemHolder(View itemView) {
            super(itemView);
            card_member = (CardView) itemView.findViewById(R.id.cv_member);
            check = (RelativeLayout) itemView.findViewById(R.id.check);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);
            member_head = (ImageView) itemView.findViewById(R.id.member_head);
            member_name = (TextView) itemView.findViewById(R.id.member_name);
            member_info = (TextView) itemView.findViewById(R.id.member_info);
            member_desc = (TextView) itemView.findViewById(R.id.member_desc);

            if (mOnMemberClickListener != null){
                card_member.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnMemberClickListener.onMemberClick(v, getLayoutPosition());
                    }
                });
            }
            if (mOnMemberClickListener != null){
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnMemberClickListener.onMemberDeleteClick(v,getLayoutPosition());
                    }
                });
            }

        }

    }
}
