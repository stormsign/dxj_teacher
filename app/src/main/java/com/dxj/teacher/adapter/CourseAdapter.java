package com.dxj.teacher.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseRecyclerViewAdapter;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.CourseSubjectBean;
import com.dxj.teacher.bean.CourseSubjectList;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/1.
 */
public class CourseAdapter extends BaseRecyclerViewAdapter<CourseSubjectBean> {
    private boolean shape;

    public CourseAdapter(Activity mContext, List<CourseSubjectBean> mList) {
        super(mContext, mList);
    }

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
        notifyDataSetChanged();
    }

    public interface OnSubjectItemClickListener {
        void onSubjectItemClick(View view, int position);
    }

    private OnSubjectItemClickListener mOnSubjectItemClickListener;

    public void setOnSubjectItemClickListener(OnSubjectItemClickListener mOnSubjectItemClickListener) {
        this.mOnSubjectItemClickListener = mOnSubjectItemClickListener;
    }

    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseAdapter.CourseHolder(mLayoutInflater.inflate(R.layout.item_course, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseSubjectBean courseBean = mList.get(position);
        if (null == courseBean) {
            return;
        }
        if (holder instanceof CourseAdapter.CourseHolder) {
            CourseHolder courseHolder = (CourseHolder) holder;
            courseHolder.name.setText(courseBean.getRemark());
            courseHolder.tvCourse.setText(courseBean.getSubjectName());
            if (shape) {
                courseHolder.checkmark.setVisibility(View.VISIBLE);
            } else {
                courseHolder.checkmark.setVisibility(View.GONE);
            }
//            if (subjectBean.isSelected()) {
//                subjectHolder.name.setTextColor(mContext.getResources().getColor(R.color.orange));
//            } else {
//                subjectHolder.name.setTextColor(mContext.getResources().getColor(R.color.abc_primary_text_material_light));

            courseHolder.checkmark.setOnClickListener(getCheckMarkListener(position, courseBean));
        }

    }

    private View.OnClickListener getCheckMarkListener(final int position, final CourseSubjectBean courseBean) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData(courseBean.getId(), position);
//                sendRequestData
            }
        };
    }

    public class CourseHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView tvCourse;
        public ImageView checkmark;

        public CourseHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            tvCourse = (TextView) itemView.findViewById(R.id.tv_course_name);
            checkmark = (ImageView) itemView.findViewById(R.id.checkmark);
            if (mOnSubjectItemClickListener != null) {
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    isSelected = true;
                        mOnSubjectItemClickListener.onSubjectItemClick(v, getLayoutPosition());
                    }
                });
            }
        }
    }

    private void sendRequestData(String id, int postion) {

        String urlPath = FinalData.URL_VALUE + HttpUtils.DELGOODSUBJECT;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
//        map.put("nickName", strNiceName);
        GsonRequest<BaseBean> custom = new GsonRequest(Request.Method.POST, urlPath, BaseBean.class, map, getListener(postion), getErrorListener());
        VolleySingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<BaseBean> getListener(final int position) {
        return new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean str) {
                Log.i("TAG", "str=" + str);
                if (str.getCode() == 0) {

                    if (mOnCheckmstkItemClickListener != null) {
                        mOnCheckmstkItemClickListener.onCheckItemClick(position);
                    }
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(mContext, "修改失败");
            }
        };
    }
}

