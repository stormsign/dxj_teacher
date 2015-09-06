package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseRecyclerViewAdapter;
import com.dxj.teacher.bean.Notice;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class NoticeAdapter extends BaseRecyclerViewAdapter<Notice> {

    private Activity mContext;
    private List<Notice> mList;
    private LayoutInflater mLayoutInflater;

    public NoticeAdapter(Activity mContext, List mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnNoticeClickListener {
        void onNoticeClick(View view, int position);
    }

    private OnNoticeClickListener mOnNoticeClickListener;

    public void setOnNoticeClickListener(OnNoticeClickListener mOnNoticeClickListener){
        this.mOnNoticeClickListener = mOnNoticeClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeItemHolder(mLayoutInflater.inflate(R.layout.item_notice, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Notice notice = mList.get(position);
        if (null == notice){
            return ;
        }
        NoticeItemHolder mHolder = (NoticeItemHolder) holder;
        mHolder.tv_title.setText(notice.getName());
        mHolder.tv_content.setText(notice.getDescription());
    }

    public class NoticeItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_title;
        public LinearLayout ll_notice;
        private TextView tv_content;


        public NoticeItemHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ll_notice = (LinearLayout) itemView.findViewById(R.id.lv_card);

            if (mOnNoticeClickListener != null){
                ll_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnNoticeClickListener.onNoticeClick(v, getLayoutPosition());
                    }
                });
            }
        }

    }
}
