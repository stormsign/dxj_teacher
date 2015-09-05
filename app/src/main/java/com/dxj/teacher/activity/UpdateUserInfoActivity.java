package com.dxj.teacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.dialogplus.SimpleAdapter;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyAsyn;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.PhotoFileUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.UpdatePhotoUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import github.jjobes.slidedatetimepicker.SlideDateTimePicker;

/**
 * Created by kings on 8/27/2015.
 */
public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {
    public String[] strings = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天坪座", "天蝎座", "射手座", "摩羯座", "水平座", "双鱼座"};
    public static final int TAKE_PICTURE = 0;// 拍照
    public static final int RESULT_LOAD_IMAGE = 1;// 从相册中选择
    public static final int CUT_PHOTO_REQUEST_CODE = 2;

    public static final int BIRTHDAY = 2;
    public static final int SEX = 1;
    public static final int HOROSCOPE = 3;
    public static final int HEAD_URL = 4;

    public static final int NICE_NAME = 1;
    public static final int DIALECT = 2;
    public static final int NATIONALITY = 3;
    public static final int LABEL = 4;
    public static final int SCHOOLAGE = 5;
    public static final int LIVING_CITY = 7;
    public static final int REMARK = 8;
    public static final int EXPERIENCE = 9;
    public static final int RESULT = 10;
    public static final int SOLVELABEL = 11;
    public static final int UNIVERSITY = 12;
    public static final int ROLENAME = 13;
    public static final int SCHOOL = 14;
    private RelativeLayout relativeNiceName;
    private RelativeLayout relativeSex;
    private RelativeLayout relativeDialect;
    private RelativeLayout relativeNationality;
    private RelativeLayout relativeSchollAge;
    private RelativeLayout relativeLivingCity;
    private RelativeLayout relativeRecommend;
    private RelativeLayout relativeBirthday;
    private RelativeLayout relativeConstellation;
    private RelativeLayout relativerLabel;
    private RelativeLayout relativerExperience;
    private RelativeLayout relativerResult;
    private RelativeLayout relativerSolvelabel;
    private RelativeLayout relativerUniversity;
    private RelativeLayout relativerRolename;
    private RelativeLayout relativerAvatar;
    private RelativeLayout relativerSchool;
    private RelativeLayout relativerImages;
    private TextView tvNicename;
    private TextView tvSex;
    private TextView tvNationality;
    private TextView tvDialect;
    private TextView tvSchollAge;
    private TextView tvLivingCity;
    private TextView tvRecommend;
    private TextView tvBirthday;
    private TextView tvConstellation;
    private TextView tvExperience;
    private TextView tvResult;
    private TextView tvSolveLabel;
    private TextView tvUniversity;
    private TextView tvRolename;
    private TextView tvSchool;
    private TextView tvImages;
    private TextView tvLabel;
    private ImageView avatar;
    private DialogPlus dialogPlus;
    private UserBean userBean;
    private Uri photoUri;
    private String imagePath;
    private String strHeadUrl;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
//                isBitmap = true;
                avatar.setImageBitmap((Bitmap) msg.obj);
                new MyAsyn(UpdateUserInfoActivity.this, getAsynResponse(), imagePath, HttpUtils.UPADTE_MULT_IMAGE).execute();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar titleNavBar = (TitleNavBar)findViewById(R.id.title);
        titleNavBar.setTitle("个人资料");
        titleNavBar.setTitleNoRightButton();
    }

    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Log.i("TAG", "Update+result=" + result);
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                if (headUrl.getCode()==0){
                    strHeadUrl=headUrl.getImages().get(0);
                    sendRequestData(strHeadUrl,HEAD_URL);
                }else
                    showToast("头像上传失败");
            }
        };
    }

    @Override
    public void initView() {
        avatar = (ImageView) findViewById(R.id.avatar);
        relativeNiceName = (RelativeLayout) findViewById(R.id.relative_nicename);
        relativeSex = (RelativeLayout) findViewById(R.id.relative_sex);
        relativeDialect = (RelativeLayout) findViewById(R.id.relative_dialect);
        relativeNationality = (RelativeLayout) findViewById(R.id.relative_nationality);
        relativeSchollAge = (RelativeLayout) findViewById(R.id.relative_schollage);
        relativeLivingCity = (RelativeLayout) findViewById(R.id.relative_livingcity);
        relativeRecommend = (RelativeLayout) findViewById(R.id.relative_recommend);
        relativeBirthday = (RelativeLayout) findViewById(R.id.relative_birthday);
        relativeConstellation = (RelativeLayout) findViewById(R.id.relative_constellation);
        relativerLabel = (RelativeLayout) findViewById(R.id.relative_label);
        relativerExperience = (RelativeLayout) findViewById(R.id.relative_experience);
        relativerResult = (RelativeLayout) findViewById(R.id.relative_result);
        relativerSolvelabel = (RelativeLayout) findViewById(R.id.relative_solvelabel);
        relativerUniversity = (RelativeLayout) findViewById(R.id.relative_university);
        relativerRolename = (RelativeLayout) findViewById(R.id.relative_rolename);
        relativerAvatar = (RelativeLayout) findViewById(R.id.relative_avatar);
        relativerSchool = (RelativeLayout) findViewById(R.id.relative_school);
        relativerImages = (RelativeLayout) findViewById(R.id.relative_images);
        //------------------------------------- 华丽分割线--------------------------------

        tvNicename = (TextView) findViewById(R.id.tv_nicename);
        tvSchollAge = (TextView) findViewById(R.id.tv_schollage);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDialect = (TextView) findViewById(R.id.tv_dialect);
        tvNationality = (TextView) findViewById(R.id.tv_nationality);
        tvLivingCity = (TextView) findViewById(R.id.tv_livingcity);
        tvRecommend = (TextView) findViewById(R.id.tv_recomment);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        tvConstellation = (TextView) findViewById(R.id.tv_constellation);
        tvExperience = (TextView) findViewById(R.id.tv_experience);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvSolveLabel = (TextView) findViewById(R.id.tv_solvelabel);
        tvUniversity = (TextView) findViewById(R.id.tv_university);
        tvRolename = (TextView) findViewById(R.id.tv_rolename);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvImages = (TextView) findViewById(R.id.tv_images);
        tvLabel = (TextView) findViewById(R.id.tv_label);
        //------------------------- 赋值--------------------------

        if (userBean != null) {
            showLogI(userBean.getUserInfo().getHeadUrl());
            if (userBean.getUserInfo().getHeadUrl() != null)
            /** 加载头像 */
                Glide.with(MyApplication.getInstance()).load(userBean.getUserInfo().getHeadUrl()).centerCrop().placeholder(R.mipmap.default_avatar).into(avatar);
            else
                Glide.with(MyApplication.getInstance()).load(R.mipmap.default_avatar).centerCrop().into(avatar);
            tvNicename.setText(userBean.getUserInfo().getNickName());
            tvSchollAge.setText(String.valueOf(userBean.getUserInfo().getSchoolAge()));
            tvSex.setText(userBean.getUserInfo().getSex());
            tvDialect.setText(userBean.getUserInfo().getDialect());
            tvNationality.setText(userBean.getUserInfo().getNationality());
            tvLivingCity.setText(userBean.getUserInfo().getLivingCity());
            tvRecommend.setText(userBean.getUserInfo().getRemark());
            tvBirthday.setText(userBean.getUserInfo().getBirthday());
            tvConstellation.setText(userBean.getUserInfo().getHoroscope());
            tvExperience.setText(userBean.getUserInfo().getExperience());
            tvResult.setText(userBean.getUserInfo().getResult());
            tvUniversity.setText(userBean.getUserInfo().getUniversity());
        }
        if (userBean.getUserInfo().getLabel().size() > 0) {
            StringBuffer sbf = new StringBuffer();
            for (int i = 0; i < userBean.getUserInfo().getLabel().size(); i++) {
                if (i == userBean.getUserInfo().getLabel().size() - 1)
                    sbf.append(userBean.getUserInfo().getLabel().get(i).toString());
                else
                    sbf.append(userBean.getUserInfo().getLabel().get(i).toString() + ",");

            }
            tvLabel.setText(sbf.toString());
        }
        if (userBean.getUserInfo().getSolveLabel().size() > 0) {
            StringBuffer sbf = new StringBuffer();
            for (int i = 0; i < userBean.getUserInfo().getSolveLabel().size(); i++) {
                if (i == userBean.getUserInfo().getSolveLabel().size() - 1)
                    sbf.append(userBean.getUserInfo().getSolveLabel().get(i).toString());
                else
                    sbf.append(userBean.getUserInfo().getSolveLabel().get(i).toString() + ",");

            }
            tvSolveLabel.setText(sbf.toString());

        }
        //------------------------添加点击事件-----------------------------
        relativeNiceName.setOnClickListener(this);
        relativeSex.setOnClickListener(this);
        relativeDialect.setOnClickListener(this);
        relativeNationality.setOnClickListener(this);
        relativeSchollAge.setOnClickListener(this);
        relativeLivingCity.setOnClickListener(this);
        relativeRecommend.setOnClickListener(this);
        relativeBirthday.setOnClickListener(this);
        relativeConstellation.setOnClickListener(this);
        relativerLabel.setOnClickListener(this);
        relativerExperience.setOnClickListener(this);
        relativerResult.setOnClickListener(this);
        relativerSolvelabel.setOnClickListener(this);
        relativerUniversity.setOnClickListener(this);
        relativerRolename.setOnClickListener(this);
        relativerAvatar.setOnClickListener(this);
        relativerSchool.setOnClickListener(this);
        relativerImages.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userBean = MyApplication.getInstance().getUserBean();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String userId =userBean.getUserInfo().getId();
        switch (id) {
            case R.id.relative_avatar:
                UpdatePhotoUtils.startPhotoZoom(this);
                break;
            case R.id.relative_nicename:
                Intent intent = new Intent(this, UpdateNiceNameActivity.class);
                intent.putExtra("niceName",userBean.getUserInfo().getNickName());
                intent.putExtra("id",userId);
                startActivityForResult(intent, NICE_NAME);
                break;
            case R.id.relative_sex:
                updateSex();
                break;
            case R.id.relative_dialect:
                Intent intentDialect = new Intent(this, UpdateDialectActivity.class);
                intentDialect.putExtra("dialect",userBean.getUserInfo().getDialect());
                intentDialect.putExtra("id",userId);
                startActivityForResult(intentDialect, DIALECT);
                break;
            case R.id.relative_nationality:
                Intent intentNationality = new Intent(this, UpdatenationalityActivity.class);
                intentNationality.putExtra("nationality",userBean.getUserInfo().getNationality());
                intentNationality.putExtra("id",userId);
                startActivityForResult(intentNationality, NATIONALITY);
                break;
            case R.id.relative_schollage:
                Intent intentSchool = new Intent(this, UpdateSchoolAgeActivity.class);
                intentSchool.putExtra("schoolAge",userBean.getUserInfo().getSchoolAge());
                intentSchool.putExtra("id",userId);
                startActivityForResult(intentSchool, SCHOOLAGE);
                break;
            case R.id.relative_livingcity:
                Intent intentLivingCity = new Intent(this, UpdateLivingCityActivity.class);
                intentLivingCity.putExtra("id",userId);
                startActivityForResult(intentLivingCity, LIVING_CITY);
                break;
            case R.id.relative_recommend:
                Intent intentRecommend = new Intent(this, UpdateRecommendActivity.class);
                intentRecommend.putExtra("recommend",userBean.getUserInfo().getRemark());
                intentRecommend.putExtra("id",userId);
                startActivityForResult(intentRecommend, REMARK);
                break;
            case R.id.relative_birthday:
                updateBirthday();
                break;
            case R.id.relative_constellation:
                SimpleAdapter adapter = new SimpleAdapter(UpdateUserInfoActivity.this, strings);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener, dismissListener, cancelListener, true);
                break;
            case R.id.relative_label:
                Intent intentLabel = new Intent(this, UpdateLabelActivity.class);
                startActivityForResult(intentLabel, LABEL);
                break;
            case R.id.relative_experience:
                Intent intentExperience = new Intent(this, UpdateExperienceActivity.class);
                startActivityForResult(intentExperience, EXPERIENCE);
                break;
            case R.id.relative_result:
                Intent intentResult = new Intent(this, UpdateResultActivity.class);
                startActivityForResult(intentResult, RESULT);
                break;
            case R.id.relative_solvelabel:
                Intent intentSolveLabel = new Intent(this, UpdateSolveLabelActivity.class);
                startActivityForResult(intentSolveLabel, SOLVELABEL);
                break;
            case R.id.relative_university:
                Intent intentSolveLabels = new Intent(this, UpdateUniversityActivity.class);
                startActivityForResult(intentSolveLabels, SOLVELABEL);
                break;
            case R.id.relative_rolename:
                Intent intentRoleName = new Intent(this, UpdateRoleNameActivity.class);
                startActivityForResult(intentRoleName, ROLENAME);
                break;
            case R.id.relative_school:
                Intent intentSchoolCity = new Intent(this, UpdateSchoolActivity.class);
                startActivityForResult(intentSchoolCity, SCHOOL);
                break;
            case R.id.relative_images:
                Intent intentImage = new Intent(this, UpdateImageActivity.class);
                startActivity(intentImage);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE || requestCode == TAKE_PICTURE || requestCode == CUT_PHOTO_REQUEST_CODE) {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    //获取图片路径
                    if (RESULT_LOAD_IMAGE == requestCode) {
                        if (data == null) {
                            return;
                        }
                        Uri selectedImageUri = data.getData();
                        //图片裁剪
                        imagePath = UpdatePhotoUtils.getImagePath();
                        UpdatePhotoUtils.startPhotoZoomOne(selectedImageUri, UpdateUserInfoActivity.this);

                    } else {
                        if (requestCode == TAKE_PICTURE) {
                            // 拍摄图片
                            imagePath = UpdatePhotoUtils.getImagePath();
                            UpdatePhotoUtils.startPhotoZoomOne(photoUri, UpdateUserInfoActivity.this);
                        } else if (requestCode == CUT_PHOTO_REQUEST_CODE) {
                            if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                                Log.i("TAG", "imagePath=" + imagePath);
                                if (bitmap == null && !StringUtils.isEmpty(imagePath)) {
                                    bitmap = MyUtils.getBitmapByPath(imagePath);
                                    Log.i("TAG", "bitmap=" + bitmap);
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = bitmap;
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    }
                }

                ;
            }.start();
        }
        if (data == null)
            return;
        if (requestCode == NICE_NAME) {
            String niceName = data.getStringExtra("nicename");
            tvNicename.setText(niceName);
        } else if (requestCode == DIALECT) {
            String dialect = data.getStringExtra("dialect");
            tvDialect.setText(dialect);
        } else if (requestCode == NATIONALITY) {
            String nationality = data.getStringExtra("nationality");
            tvNationality.setText(nationality);
        } else if (requestCode == LABEL) {
            List<String> labelList = data.getStringArrayListExtra("label");
            StringBuffer sbf = new StringBuffer();
            for (String str : labelList) {
                sbf.append(str + ",");
            }
            sbf.delete(sbf.length() - 1, sbf.length());
            tvLabel.setText(sbf.toString());
        } else if (requestCode == SCHOOLAGE) {
            String strSchoolAge = data.getStringExtra("schoolAge");
            tvSchollAge.setText(strSchoolAge);
        } else if (requestCode == SOLVELABEL) {
            List<String> labelList = data.getStringArrayListExtra("solveLabel");
            StringBuffer sbf = new StringBuffer();
            for (String str : labelList) {
                sbf.append(str + ",");
            }
            sbf.delete(sbf.length() - 1, sbf.length());
            tvSolveLabel.setText(sbf.toString());
        } else if (requestCode == LIVING_CITY) {
            String strLivingCity = data.getStringExtra("livingCity");
            tvLivingCity.setText(strLivingCity);
        } else if (requestCode == REMARK) {
            String strRemark = data.getStringExtra("remark");
            tvRecommend.setText(strRemark);
        } else if (requestCode == EXPERIENCE) {
            String strExperience = data.getStringExtra("experience");
            tvExperience.setText(strExperience);
        } else if (requestCode == RESULT) {
            String strResult = data.getStringExtra("result");
            tvResult.setText(strResult);
        } else if (requestCode == ROLENAME) {
            String strResult = data.getStringExtra("roleName");
            tvRolename.setText(strResult);
        } else if (requestCode == SCHOOL) {
            String strSchool = data.getStringExtra("school");
            tvSchool.setText(strSchool);
        }
    }

    private void updateSex() {


        dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.sex_item))
                .setCancelable(true)
                .create();

        dialogPlus.show();
        dialogPlus.findViewById(R.id.tv_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("女", SEX);
            }
        });
        dialogPlus.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("男", SEX);
            }
        });
    }

    private void sendRequestData(String strSex, int index) {
        String urlPath = null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        if (index == BIRTHDAY) {
            Log.i("TAG", "strSex=" + strSex);
            map.put("birthday", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.BIRTHDAY;
        } else if (index == SEX) {
            map.put("sex", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.SEX;
        } else if (index == HOROSCOPE) {
            map.put("horoscope", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.HOROSCOPE;
        } else if (index == HEAD_URL) {
            Log.i("TAG", "headUrl=" + strSex);
            map.put("headUrl", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.HEAD_URL;
        }


        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(strSex, index), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener(final String strSex, final int index) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                String accountTable = null;
                if (message.getCode() == 0) {
                    if (index == SEX) {
                        tvSex.setText(strSex);
                        accountTable = AccountTable.SET;
                    } else if (index == BIRTHDAY) {
                        tvBirthday.setText(strSex);
                        accountTable = AccountTable.BIRTHDAY;
                    } else if (index == HOROSCOPE) {
                        tvConstellation.setText(strSex);
                        accountTable = AccountTable.HOROSCOPE;
                    } else if (index == HEAD_URL) {
                        accountTable = AccountTable.HEADURL;
                        showToast("上传成功");
                    }
                }
                AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strSex, accountTable);

                if (index == 1)
                    dialogPlus.dismiss();
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialogPlus.dismiss();
            }
        };
    }

    private void updateBirthday() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())

                .build()
                .show();
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
//            Toast.makeText(UpdateUserInfoActivity.this,
//                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
            tvBirthday.setText(mFormatter.format(date));
            sendRequestData(mFormatter.format(date), BIRTHDAY);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(UpdateUserInfoActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private void showOnlyContentDialog(Holder holder, int gravity, BaseAdapter adapter,
                                       OnItemClickListener itemClickListener, OnDismissListener dismissListener,
                                       OnCancelListener cancelListener, boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .setCancelable(true)
                .create();
        dialog.show();
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {

        }
    };
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//        TextView textView = (TextView) view.findViewById(R.id.text_view);
//        String clickedAppName = textView.getText().toString();
            //        dialog.dismiss();
            //        Toast.makeText(MainActivity.this, clickedAppName + " clicked", Toast.LENGTH_LONG).show();
            Log.i("TAG", "position=" + position);
            dialog.dismiss();
            String str = strings[position];
            sendRequestData(str, HOROSCOPE);
        }
    };
    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "dismiss listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };

    OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "cancel listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };


    public void photo() {
        //获取文件
        File out = UpdatePhotoUtils.photo(this);
        photoUri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }
}
