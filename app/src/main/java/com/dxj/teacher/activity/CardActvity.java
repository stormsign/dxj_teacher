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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.CourseSubjectList;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.GsonRequest;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyAsyn;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.utils.UpdatePhotoUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 9/4/2015.
 */
public class CardActvity extends BaseActivity {
    private CardView imgCard;
    private ImageView imgPic;
    private ImageView imgUpage;
    private TextView tvUpage;
    private RadioGroup radioGroupCard;
    private EditText etName;
    private EditText etCardNumber;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initTitle();
        initData();
        initView();

    }

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
//                if (headUrl.getCode()==0)
//                    sendRequestData(headUrl.getUrl(), 4);
            }
        };
    }

    @Override
    public void initTitle() {
        TitleNavBar titel = (TitleNavBar) findViewById(R.id.title);
        titel.setTitle("身份认证");
        titel.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        imgCard = (CardView) findViewById(R.id.img_card);
        imgPic = (ImageView) findViewById(R.id.img_pic);
        tvUpage = (TextView) findViewById(R.id.tv_update);
        imgUpage = (ImageView) findViewById(R.id.img_update);
        radioGroupCard = (RadioGroup) findViewById(R.id.choice_mode);
        update = (TextView) findViewById(R.id.creategroup);
        etName = (EditText) findViewById(R.id.et_input_name);
        etCardNumber = (EditText) findViewById(R.id.et_input_card_number);
        update.setOnClickListener(updateCardListener);
        imgCard.setOnClickListener(updateImageListener);

    }

    View.OnClickListener updateImageListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //获取照片
            UpdatePhotoUtils.startPhotoZoom(CardActvity.this);

        }
    };
    View.OnClickListener updateCardListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendRequestData();
        }
    };

    @Override
    public void initData() {

    }

    private void sendRequestData() {
        String strName = etName.getText().toString().trim();
        String strCardNumber = etCardNumber.getText().toString().trim();
        if (StringUtils.isEmpty(strName)) {
            etName.setError("请输入姓名");
            etName.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(imageUrl)) {
            showToast("请上传图片");
            return;
        }
        switch (radioGroupCard.getCheckedRadioButtonId()) {
            case R.id.rb_card:
                cardType = 0;
                break;
            case R.id.rb_passport:
                cardType = 1;
                break;
        }
        ;
        String urlPath = FinalData.URL_VALUE + HttpUtils.CARD;
        Map<String, Object> map = new HashMap<>();
        map.put("id", MyApplication.getInstance().getUserId());
        map.put("cardType", cardType);
        map.put("name", strName);
        map.put("img", imageUrl);
        map.put("number", strCardNumber);
        GsonRequest<BaseBean> custom = new GsonRequest(Request.Method.POST, urlPath, BaseBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<BaseBean> getListener() {
        return new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean str) {
                Log.i("TAG", "str=" + str);
                if (str.getCode() == 0) {
                    showToast(str.getMsg());
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(context, "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(context, "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(context, "服务器异常 登录失败");
            }
        };
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UpdateUserInfoActivity.RESULT_LOAD_IMAGE || requestCode == UpdateUserInfoActivity.TAKE_PICTURE || requestCode == UpdateUserInfoActivity.CUT_PHOTO_REQUEST_CODE) {
            new Thread() {
                @Override
                public void run() {
                    Log.i("TAG", "onActivityResult");
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
//                        imagePath = UpdatePhotoUtils.getImagePath();
//                        UpdatePhotoUtils.startPhotoZoomOne(selectedImageUri, CardActvity.this);
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

    public void photo() {
        //获取文件
        File out = UpdatePhotoUtils.photo(this);
        photoUri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, UpdateUserInfoActivity.TAKE_PICTURE);
    }
}
