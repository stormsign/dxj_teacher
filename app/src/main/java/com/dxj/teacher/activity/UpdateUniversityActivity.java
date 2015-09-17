package com.dxj.teacher.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.adapter.ResultListAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.bean.UniversityBean;
import com.dxj.teacher.bean.UniversityListBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyAsyn;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.utils.UpdatePhotoUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.google.gson.Gson;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import github.jjobes.slidedatetimepicker.SlideDateTimePicker;

/**
 * Created by kings on 8/27/2015.
 * 学历认证
 */
public class UpdateUniversityActivity extends BaseActivity implements View.OnClickListener {
    private EditText etSearch;
    private EditText etMajor;
    private TextView tvEntrancetime;
    private ListView lvSearchResult;
    private LinearLayout linearEntrancetime;
    private TextView tvNoresult;
    private String strGrades;
    private String strEntranceTime;
    private String strSearch;
    private ResultListAdapter resultListAdapter;
    private ArrayList<UniversityBean> universityResult = new ArrayList<>();
    private String strUniversity;
    private long id;
    private String userId;
    private boolean isShow;//假如EditText获取到值 第一次不搜取
    //---------------------//
    private CardView imgCard;
    private ImageView imgPic;
    private ImageView imgUpage;
    private TextView tvUpage;
    private TextView update;
    private int cardType;
    private String imageUrl;
    private Uri photoUri;
    private String picturePath;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
//                isBitmap = true;
                imgPic.setVisibility(View.VISIBLE);
                tvUpage.setVisibility(View.GONE);
                imgUpage.setVisibility(View.GONE);
                imgPic.setImageBitmap((Bitmap) msg.obj);
                new MyAsyn(context, getAsynResponse(), picturePath, HttpUtils.UPADTE_MULT_IMAGE).execute();
            }
        }
    };

    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Log.i("TAG", "Update+result=" + result);
//                tv.setVisibility(View.GONE);
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                Log.i("TAG", "headUrl=" + headUrl.getImages().get(0));
                imageUrl = headUrl.getImages().get(0);
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_university);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("学历认证");
        title.setTitleNoRightButton();
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

            }

            @Override
            public void onBackClick() {

            }
        });
    }

    @Override
    public void initView() {
        imgCard = (CardView) findViewById(R.id.img_card);
        imgPic = (ImageView) findViewById(R.id.img_pic);
        tvUpage = (TextView) findViewById(R.id.tv_update);
        imgUpage = (ImageView) findViewById(R.id.img_update);
        update = (TextView) findViewById(R.id.creategroup);
        update.setOnClickListener(updateCardListener);
        imgCard.setOnClickListener(updateImageListener);
        etSearch = (EditText) findViewById(R.id.et_university);
        lvSearchResult = (ListView) findViewById(R.id.search_result);
        linearEntrancetime = (LinearLayout) findViewById(R.id.linear_entrancetime);
        tvNoresult = (TextView) findViewById(R.id.tv_noresult);
        tvEntrancetime = (TextView) findViewById(R.id.tv_entrancetime);
        etMajor = (EditText) findViewById(R.id.et_major);

        linearEntrancetime.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() == null || "".equals(s.toString()) || isShow) {
                    lvSearchResult.setVisibility(View.GONE);
                    tvNoresult.setVisibility(View.GONE);
                    isShow = false;
                } else {
                    strSearch = s.toString();
                    sendRequestData(3);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resultListAdapter = new ResultListAdapter(this, universityResult);
        lvSearchResult.setAdapter(resultListAdapter);
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                strUniversity = universityResult.get(position).getName();
                id = universityResult.get(position).getId();
                etSearch.setText(strUniversity);
                lvSearchResult.setVisibility(View.GONE);
                isShow = true;
            }
        });
    }

    View.OnClickListener updateImageListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //获取照片
            UpdatePhotoUtils.startPhotoZoom(UpdateUniversityActivity.this);

        }
    };
    View.OnClickListener updateCardListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendRequestData(1);
        }
    };

    @Override
    public void initData() {
        userId = getIntent().getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.linear_entrancetime:
                updateBirthday();
                break;
        }
    }

    private void sendRequestData(int index) {

        String parameter = null;
        String urlPath = null;
        String key = null;

        if (index == 1) {
            parameter = etSearch.getText().toString().trim();

            if (StringUtils.isEmpty(parameter)) {
                finish();
            } else {
                if (StringUtils.isEmpty(strEntranceTime)) {
                    ToastUtils.showToast(this, "请输入开学时间");
                    return;
                }
            }
            urlPath = FinalData.URL_VALUE + HttpUtils.UNIVERSITY;
            if (StringUtils.isEmpty(parameter)) {
                finish();
                return;
            }
            key = "university";
        } else if (index == 3) {
            parameter = strSearch;
            urlPath = FinalData.URL_VALUE + HttpUtils.FIND_UNIVERSITY;
            if (StringUtils.isEmpty(parameter)) {
                return;
            }
            key = "name";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put(key, parameter);
        if (index == 1) {
            map.put("universityId", id);
            map.put("entranceTime", strEntranceTime);
            map.put("major", etMajor.getText().toString());
            map.put("degrees", imageUrl);
        }
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(index), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener(final int index) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                if (index == 3) {
                    UniversityListBean message = JSONObject.parseObject(str, UniversityListBean.class);
                    Log.i("TAG", "message=" + message.getList().size());
//                  if (message.getCode()==0){
                    universityResult.clear();
                    universityResult.addAll(message.getList());
                    if (universityResult.size() <= 0) {
                        Log.i("TAG", "city_result=" + universityResult.size());

                        tvNoresult.setVisibility(View.VISIBLE);
                        lvSearchResult.setVisibility(View.GONE);
                    } else {
                        Log.i("TAG", "message=" + message.getList().size());
                        Log.i("TAG", "city_result=" + universityResult.size());

                        tvNoresult.setVisibility(View.GONE);
                        lvSearchResult.setVisibility(View.VISIBLE);
                        resultListAdapter.notifyDataSetChanged();
//                      }
                    }
                } else {
                    BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                    if (message.getCode() == 0) {
                        AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strUniversity, AccountTable.UNIVERSITY);

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("university", strUniversity);
                        intent.putExtras(bundle);
                        UpdateUniversityActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateUniversityActivity.this, "修改失败");
                finish();
            }
        };
    }

    private void updateBirthday() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                .build()
                .show();
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
//            Toast.makeText(UpdateUserInfoActivity.this,
//                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
            tvEntrancetime.setText(mFormatter.format(date));
            strEntranceTime = mFormatter.format(date);

        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(UpdateUniversityActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UpdateUserInfoActivity.RESULT_LOAD_IMAGE || requestCode == UpdateUserInfoActivity.TAKE_PICTURE || requestCode == UpdateUserInfoActivity.CUT_PHOTO_REQUEST_CODE) {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    //获取图片路径
                    if (UpdateUserInfoActivity.RESULT_LOAD_IMAGE == requestCode) {
                        if (data == null) {
                            return;
                        }
                        //图片裁剪
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        if (bitmap == null && !StringUtils.isEmpty(picturePath)) {
                            try {
                                bitmap = MyUtils.createImageThumbnail(context, picturePath, 600);
                                Log.i("TAG", "bitmap=" + bitmap);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                ;
            }.start();
        }
    }

}
