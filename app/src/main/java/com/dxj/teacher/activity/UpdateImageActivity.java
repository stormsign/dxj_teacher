package com.dxj.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.UpdateImageAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateImageActivity extends Activity {

    private static final int REQUEST_IMAGE = 3;

    private TextView mResultText;
//    private RadioGroup mChoiceMode, mShowCamera;

    private ArrayList<String> mSelectPath;
    private GridView feedbackNoScrollgridview;
    private UpdateImageAdapter updateAdapter;
    private List<String> imageList = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiimage);

        mResultText = (TextView) findViewById(R.id.result);
//        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
//        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
        feedbackNoScrollgridview = (GridView) findViewById(R.id.feedback_noScrollgridview);
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        updateAdapter = new UpdateImageAdapter(UpdateImageActivity.this);
        feedbackNoScrollgridview.setAdapter(updateAdapter);
//        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                if (checkedId == R.id.multi) {
//                    mRequestNum.setEnabled(true);
//                } else {
//                    mRequestNum.setEnabled(false);
//                    mRequestNum.setText("");
//                }
//            }
//        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateAdapter.getImageUrls().size()>0){
                    imageUrls.addAll(updateAdapter.getImageUrls());
                    sendRequestData();
                }
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

//                if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
//                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
//                } else {
//                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
//                }

//                boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;

                int maxNum = 9;
//                if (!TextUtils.isEmpty(mRequestNum.getText())) {
//                    maxNum = Integer.valueOf(mRequestNum.getText().toString());
//                }

                Intent intent = new Intent(UpdateImageActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                // 默认选择
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> mSelectedImages = new ArrayList<String>();
                mSelectedImages.addAll(updateAdapter.getmSelectedImages());
               if(mSelectedImages.size()>0){
                   for(String list : mSelectedImages){
                       Log.i("TAG", "list=" + list);
                       if(imageList.contains(list)){
                           Log.i("TAG", "contains");
                           imageList.remove(list);

                       }
                   }
                   Log.i("TAG", "imageList=" + imageList.size());
                   updateAdapter.addData(imageList);
               }
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateAdapter.isShape()){
                    updateAdapter.setShape(false);
                }else{
                    updateAdapter.setShape(true);
                }
          }
        });
        feedbackNoScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAdapter.select(imageList.get(position));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
//                if (imageList.size() > 0) {
//                    Log.i("TAG","image");
//                    imageList.clear();
//                }
                for (String p : mSelectPath) {
                    Log.i("TAG", "mSelectPath=" + p);
                    imageList.add(p);
                    sb.append(p);
                    sb.append("\n");
                }
                mResultText.setText(sb.toString());
                updateAdapter.addData(imageList);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void sendRequestData() {
//        strNiceName = etNiceName.getText().toString().trim();
//        if (StringUtils.isEmpty(strNiceName)) {
//            finish();
//            return;
//        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.IMAGES;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("images", imageUrls);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0) {
//
//                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strNiceName, AccountTable.NICKNAME);
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("nicename", strNiceName);
//                    intent.putExtras(bundle);
//                    UpdateNiceNameActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateImageActivity.this, "修改失败");
                finish();
            }
        };
    }
}
