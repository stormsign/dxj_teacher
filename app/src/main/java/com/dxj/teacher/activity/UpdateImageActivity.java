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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.UpdateImageAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateImageActivity extends BaseActivity {

    private static final int REQUEST_IMAGE = 3;

//    private RadioGroup mChoiceMode, mShowCamera;

    private ArrayList<String> mSelectPath;
    private GridView feedbackNoScrollgridview;
    private UpdateImageAdapter updateAdapter;
    private List<String> imageList = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private String id;
    private LinearLayout linearAddPhone;
    private TitleNavBar title;
    private boolean isDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiimage);
        initTitle();
        initData();
        initView();

    }

    @Override
    public void initTitle() {
        title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("相册");
        title.setActionText("编辑");
        title.setTitleNoRightButton();
        title.showAction(true);
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
                                             @Override
                                             public void onNavOneClick() {

                                             }

                                             @Override
                                             public void onNavTwoClick() {

                                             }

                                             @Override
                                             public void onNavThreeClick() {

                                             }

                                             @Override
                                             public void onActionClick() {
                                                 if (isDelete) {
                                                     title.setActionText("删除");
                                                     isDelete = false;
                                                     List<String> mSelectedImages = new ArrayList<>();
                                                     mSelectedImages.addAll(updateAdapter.getmSelectedImages());
                                                     if (mSelectedImages.size() > 0) {
                                                         for (String list : mSelectedImages) {
                                                             Log.i("TAG", "list=" + list);
                                                             if (imageList.contains(list)) {
                                                                 Log.i("TAG", "contains");
                                                                 imageList.remove(list);

                                                             }
                                                             updateAdapter.addData(imageList);
                                                         }
                                                         Log.i("TAG", "imageList=" + imageList.size());
                                                         updateAdapter.addData(imageList);
                                                     }
                                                 } else {
                                                     title.setActionText("编辑");
                                                     isDelete = true;
                                                     if (updateAdapter.isShape()) {
                                                         updateAdapter.setShape(false);
                                                     } else {
                                                         updateAdapter.setShape(true);
                                                     }
                                                 }
                                             }

                                             @Override
                                             public void onBackClick() {
                                                 sendRequestData();
                                             }
                                         }

        );
    }

    @Override
    public void initView() {

        feedbackNoScrollgridview = (GridView) findViewById(R.id.feedback_noScrollgridview);
        linearAddPhone = (LinearLayout) findViewById(R.id.linear_add_phone);
        if (imageList.size() > 0)
            linearAddPhone.setVisibility(View.GONE);
        updateAdapter = new UpdateImageAdapter(UpdateImageActivity.this);
        updateAdapter.addData(imageList);
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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intetnMultiImageSelector();

            }
        });


        feedbackNoScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageList.size() == position) {
                    intetnMultiImageSelector();
                } else {
                    updateAdapter.select(imageList.get(position));
                }
            }
        });
    }

    private void intetnMultiImageSelector() {
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

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        imageList.addAll(getIntent().getStringArrayListExtra("photoList"));
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
                updateAdapter.addData(imageList);
                linearAddPhone.setVisibility(View.GONE);
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
        imageUrls.addAll(updateAdapter.getImageUrls());
        imageUrls.addAll(imageList);

        String urlPath = FinalData.URL_VALUE + HttpUtils.IMAGES;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
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
                    HeadUrl headUrl = new HeadUrl();
                    headUrl.setImages(updateAdapter.getImageUrls());
                    AccountDBTask.updatePhoto(MyApplication.getInstance().getUserId(), headUrl, AccountTable.PHOTO);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("photoCount", String.valueOf(updateAdapter.getImageUrls().size()));
                    intent.putExtras(bundle);
                    UpdateImageActivity.this.setResult(RESULT_OK, intent);
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
