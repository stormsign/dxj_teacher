package com.dxj.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.dialogplus.SimpleAdapter;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import github.jjobes.slidedatetimepicker.SlideDateTimePicker;

/**
 * Created by kings on 8/27/2015.
 */
public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final int NICE_NAME = 1;
    public static final int DIALECT = 2;
    public static final int NATIONALITY = 3;
    public static final int MAJOR = 4;
    public static final int SCHOOLAGE = 5;
    public static final int GRADES = 6;
    public static final int LIVING_CITY = 6;
    public static final int REMARK = 7;
    public static final int EXPERIENCE =8;
    public static final int RESULT = 9;
    public static final int SOLVELABEL =10;
    private RelativeLayout relativeNiceName;
    private RelativeLayout relativeSex;
    private RelativeLayout relativeDialect;
    private RelativeLayout relativeNationality;
    private RelativeLayout relativeMajor;
    private RelativeLayout relativeSchollAge;
    private RelativeLayout relativeGrades;
    private RelativeLayout relativeLivingCity;
    private RelativeLayout relativeRecommend;
    private RelativeLayout relativeBirthday;
    private RelativeLayout relativeConstellation;
    private RelativeLayout relativerLabel;
    private RelativeLayout relativerExperience;
    private RelativeLayout relativerResult;
    private RelativeLayout relativerSolvelabel;
    private RelativeLayout relativerUniversity;
    private TextView tvNicename;
    private TextView tvSex;
    private TextView tvNationality;
    private TextView tvDialect;
    private TextView tvMajor;
    private TextView tvSchollAge;
    private TextView tvGrades;
    private TextView tvLivingCity;
    private TextView tvRecommend;
    private TextView tvBirthday;
    private TextView tvConstellation;
    private TextView tvExperience;
    private TextView tvResult;
    private TextView tvSolveLabel;
    private TextView tvUniversity;
    private DialogPlus dialogPlus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        relativeNiceName = (RelativeLayout) findViewById(R.id.relative_nicename);
        relativeSex = (RelativeLayout) findViewById(R.id.relative_sex);
        relativeDialect = (RelativeLayout) findViewById(R.id.relative_dialect);
        relativeNationality = (RelativeLayout) findViewById(R.id.relative_nationality);
        relativeMajor = (RelativeLayout) findViewById(R.id.relative_major);
        relativeSchollAge = (RelativeLayout) findViewById(R.id.relative_schollage);
        relativeGrades = (RelativeLayout) findViewById(R.id.relative_grades);
        relativeLivingCity = (RelativeLayout) findViewById(R.id.relative_livingcity);
        relativeGrades = (RelativeLayout) findViewById(R.id.relative_livingcity);
        relativeRecommend = (RelativeLayout) findViewById(R.id.relative_recommend);
        relativeBirthday = (RelativeLayout) findViewById(R.id.relative_birthday);
        relativeConstellation = (RelativeLayout) findViewById(R.id.relative_constellation);
        relativerLabel = (RelativeLayout) findViewById(R.id.relative_label);
        relativerExperience = (RelativeLayout) findViewById(R.id.relative_experience);
        relativerResult = (RelativeLayout) findViewById(R.id.relative_result);
        relativerSolvelabel = (RelativeLayout) findViewById(R.id.relative_solvelabel);
        relativerSolvelabel = (RelativeLayout) findViewById(R.id.relative_university);
        tvNicename = (TextView) findViewById(R.id.tv_nicename);
        tvSchollAge = (TextView) findViewById(R.id.tv_schollage);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDialect = (TextView) findViewById(R.id.tv_dialect);
        tvNationality = (TextView) findViewById(R.id.tv_nationality);
        tvMajor = (TextView) findViewById(R.id.tv_major);
        tvGrades = (TextView) findViewById(R.id.tv_major);
        tvLivingCity = (TextView) findViewById(R.id.tv_livingcity);
        tvRecommend = (TextView) findViewById(R.id.tv_recomment);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        tvConstellation = (TextView) findViewById(R.id.tv_constellation);
        tvExperience = (TextView) findViewById(R.id.tv_experience);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvSolveLabel = (TextView) findViewById(R.id.tv_solvelabel);
        tvUniversity = (TextView) findViewById(R.id.tv_university);
        relativeNiceName.setOnClickListener(this);
        relativeSex.setOnClickListener(this);
        relativeDialect.setOnClickListener(this);
        relativeNationality.setOnClickListener(this);
        relativeMajor.setOnClickListener(this);
        relativeSchollAge.setOnClickListener(this);
        relativeGrades.setOnClickListener(this);
        relativeLivingCity.setOnClickListener(this);
        relativeRecommend.setOnClickListener(this);
        relativeBirthday.setOnClickListener(this);
        relativeConstellation.setOnClickListener(this);
        relativerLabel.setOnClickListener(this);
        relativerExperience.setOnClickListener(this);
        relativerResult.setOnClickListener(this);
        relativerSolvelabel.setOnClickListener(this);
        relativerUniversity.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relative_nicename:
                Intent intent = new Intent(this, UpdateNiceNameActivity.class);
                startActivityForResult(intent, NICE_NAME);
                break;
            case R.id.relative_sex:
                updateSex();
                break;
            case R.id.relative_dialect:
                Intent intentDialect = new Intent(this, UpdateDialectActivity.class);
                startActivityForResult(intentDialect, DIALECT);
                break;
            case R.id.relative_nationality:
                Intent intentNationality = new Intent(this, UpdatenationalityActivity.class);
                startActivityForResult(intentNationality, NATIONALITY);
                break;
            case R.id.relative_major:
                Intent intentMajor = new Intent(this, UpdateMajorActivity.class);
                startActivityForResult(intentMajor, MAJOR);
                break;
            case R.id.relative_schollage:
                Intent intentSchool = new Intent(this, UpdateSchoolAgeActivity.class);
                startActivityForResult(intentSchool, SCHOOLAGE);
                break;
            case R.id.relative_grades:
                Intent intentGrades = new Intent(this, UpdateGradesActivity.class);
                startActivityForResult(intentGrades, GRADES);
                break;
            case R.id.relative_livingcity:
                Intent intentLivingCity = new Intent(this, UpdateLivingCityActivity.class);
                startActivityForResult(intentLivingCity, LIVING_CITY);
                break;
            case R.id.relative_recommend:
                Intent intentRecommend = new Intent(this, UpdateRecommendActivity.class);
                startActivityForResult(intentRecommend, REMARK);
                break;
            case R.id.relative_birthday:
                updateBirthday();
                break;
            case R.id.relative_constellation:
                SimpleAdapter adapter = new SimpleAdapter(UpdateUserInfoActivity.this, false);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener, dismissListener, cancelListener, true);
                break;
            case R.id.relative_label:
                Intent intentLabel = new Intent(this, UpdateLabelActivity.class);
                startActivityForResult(intentLabel, LIVING_CITY);
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
                Intent intentSolveLabels = new Intent(this, UpdateSolveLabelActivity.class);
                startActivityForResult(intentSolveLabels, SOLVELABEL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NICE_NAME) {
            String niceName = data.getStringExtra("nicename");
            tvNicename.setText(niceName);
        } else if (requestCode == DIALECT) {
            String dialect = data.getStringExtra("dialect");
            tvDialect.setText(dialect);
        } else if (requestCode == NATIONALITY) {
            String nationality = data.getStringExtra("nationality");
            tvNationality.setText(nationality);
        } else if (requestCode == MAJOR) {
            String nationality = data.getStringExtra("major");
            tvMajor.setText(nationality);
        } else if (requestCode == SCHOOLAGE) {
            String strSchoolAge = data.getStringExtra("schoolAge");
            tvSchollAge.setText(strSchoolAge);
        } else if (requestCode == GRADES) {
            String strGrades = data.getStringExtra("grades");
            tvGrades.setText(strGrades);
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
                sendRequestData("女", 1);
            }
        });
        dialogPlus.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("男", 1);
            }
        });
    }

    private void sendRequestData(String strSex, int index) {
        String urlPath = null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        if (index == 2) {
            Log.i("TAG", "strSex=" + strSex);
            map.put("birthday", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.BIRTHDAY;
        } else if (index == 1) {
            map.put("sex", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.SEX;
        } else if (index == 3) {
            map.put("horoscope", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.HOROSCOPE;
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
                if (message.getCode() == 0) {
                    if (index == 1)
                        tvSex.setText(strSex);
                    else if (index == 2)
                        tvBirthday.setText(strSex);
                    else if (index == 3)
                        tvConstellation.setText(strSex);
                }
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
            tvBirthday.setText(mFormatter.format(date));
            sendRequestData(mFormatter.format(date), 2);
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
            //        switch (view.getId()) {
            //          case R.id.header_container:
            //            Toast.makeText(MainActivity.this, "Header clicked", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.like_it_button:
            //            Toast.makeText(MainActivity.this, "We're glad that you like it", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.love_it_button:
            //            Toast.makeText(MainActivity.this, "We're glad that you love it", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.footer_confirm_button:
            //            Toast.makeText(MainActivity.this, "Confirm button clicked", Toast.LENGTH_LONG).show();
            //            break;
            //          case R.id.footer_close_button:
            //            Toast.makeText(MainActivity.this, "Close button clicked", Toast.LENGTH_LONG).show();
            //            break;
            //        }
            //        dialog.dismiss();
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
            String str = SimpleAdapter.strings[position];
            sendRequestData(str, 3);
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
}
