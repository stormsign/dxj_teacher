package com.dxj.teacher.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.teacher.R;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.VoiceBean;
import com.dxj.teacher.db.AccountDBTask;
import com.dxj.teacher.db.AccountTable;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.HttpMethod;
import com.dxj.teacher.http.MyException;
import com.dxj.teacher.http.MyHttpConnection;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.Base64;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyAsyn;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.RateTextCircularProgressBar;
import com.dxj.teacher.widget.TitleNavBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MediaActivity extends BaseActivity {

    private String path;
    private String[] listFile;
    private MediaRecorder recorder;
//    private TextView btn1;
    private TextView btn2;
    private TextView tvReminder;
    private String savePath;
    private static final int RESULT_CAPTURE_RECORDER_SOUND = 3;// 录音的requestCode
    private String strRecorderPath;
    private File saveFile;
    private Map<String, Object> map;

    private RateTextCircularProgressBar mRateTextCircularProgressBar;
    private int progress = 1;
    private boolean isEnd;
    private String voiceUrl;
    private String id;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("TAG", "MSG=" + msg.what);
            if (msg.arg1 == -1) {
                String result = (String) msg.obj;
                VoiceBean voiceBean = JSONObject.parseObject(result, VoiceBean.class);
                if (voiceBean.getCode() == 0) {
                    ToastUtils.showToast(MediaActivity.this, "上传成功");
                    voiceUrl = voiceBean.getUrl();
                }
            } else {
                mRateTextCircularProgressBar.setProgress(msg.what);
                if (!isEnd) {
                    return;
                }
                if (progress == 61) {
                    sageder();
                    return;
//                progress = 0;
                }
                mHandler.sendEmptyMessageDelayed(progress++, 1000);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initTitle();
        initData();
        initView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CAPTURE_RECORDER_SOUND://录音
                if (resultCode == RESULT_OK) {
                    Uri uriRecorder = data.getData();
                    Log.i("tag", "urirecorder " + uriRecorder.toString());
                    Cursor cursor = this.getContentResolver().query(uriRecorder, null, null, null, null);
                    if (cursor.moveToNext()) {
                        /** _data：文件的绝对路径 ，_display_name：文件名 */
                        strRecorderPath = cursor.getString(cursor.getColumnIndex("_data"));
                        Toast.makeText(this, strRecorderPath, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void recorder() {
        savePath = path + "/" + new SimpleDateFormat(
                "yyyyMMddHHmmss").format(System
                .currentTimeMillis()) + ".amr";
        saveFile = new File(savePath);
        Log.d("TAG", saveFile.toString());
        recorder.setOutputFile(new File(savePath).getAbsolutePath());
        try {
            saveFile.createNewFile();
            recorder.prepare();
            recorder.start();
            mRateTextCircularProgressBar.setEnabled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        isEnd = true;
    }


    public void sageder() {
        if (saveFile.exists() && saveFile != null) {
            stopRecorder();
        }
        tvReminder.setText("录音结束");
//        btn1.setText("重新录制");
        btn2.setText("上传");
        isEnd = false;
        mRateTextCircularProgressBar.setEnabled(true);
    }

    public void stopRecorder() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            // 判断是否保存 如果不保存则删除
        }
    }

    private void upload() {
        Log.i("TAG", "update");
        map = new HashMap<String, Object>();
        try {
            if (savePath != null) {
                map.put("base64String", Base64.encode(getBytes(savePath)));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("fileName", new Date().getTime() + ".amr");
        map.put("folder", "head");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = getRequestUpload(HttpMethod.Post, "", map);
                    Message message = new Message();
                    message.obj = result;
                    message.arg1 = -1;
                    mHandler.sendMessage(message);
                } catch (MyException e) {


                }
            }
        }).start();

//        String url =
    }


    @Override
    protected void onDestroy() {
        if (recorder != null) {
            recorder.release();
        }
        super.onDestroy();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("语音");
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
                sendRequestData();
            }
        });
    }

    private void sendRequestData() {
//        strDialect = etDialect.getText().toString().trim();
        if (StringUtils.isEmpty(voiceUrl)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.VOICE;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("voice", voiceUrl);
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
//                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strDialect, AccountTable.DIALECT);
                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("dialect", strDialect);
//                    intent.putExtras(bundle);
                    MediaActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(MediaActivity.this, "修改失败");
                finish();
            }
        };
    }

    @Override
    public void initView() {

//        btn1 = (TextView) findViewById(R.id.btn1);
        btn2 = (TextView) findViewById(R.id.btn2);
        tvReminder = (TextView) findViewById(R.id.tv_reminder);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnd) {
                    sageder();
                } else {
                    upload();
                }
            }
        });
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopRecorder();
//                progress = 1;
//                mRateTextCircularProgressBar.isShow();
//                recorder();
//                tvReminder.setTextColor(getResources().getColor(R.color.text_black_voice));
//                tvReminder.setText("录音中");
//                mHandler.sendEmptyMessageDelayed(progress++, 1000);
//                btn1.setVisibility(View.VISIBLE);
//                btn2.setVisibility(View.VISIBLE);
//                btn1.setText("重新录制");
//                btn2.setText("录制结束");
//                mRateTextCircularProgressBar.isShowIMage();
//                mRateTextCircularProgressBar.setEnabled(true);
//                tvReminder.setText("说点什么吧");
//                tvReminder.setTextColor(getResources().getColor(R.color.text_black_64));
//                progress = 1;
//            }
//        });
        mRateTextCircularProgressBar = (RateTextCircularProgressBar) findViewById(R.id.rate_progress_bar);
        mRateTextCircularProgressBar.setMax(60);

        mRateTextCircularProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRateTextCircularProgressBar.isShow();
                recorder();
                tvReminder.setTextColor(getResources().getColor(R.color.text_black_voice));
                tvReminder.setText("录音中");
                mHandler.sendEmptyMessageDelayed(progress++, 1000);
//                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
//                btn1.setText("重新录制");
                btn2.setText("录制结束");
//
            }
        });
        mRateTextCircularProgressBar.getCircularProgressBar();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory()
                    .toString()
                    + "/ATEST_RECORDERS";
            Log.i("tag", "path" + path);
            File files = new File(path);
            if (!files.exists()) {
                files.mkdir();
            }
            listFile = files.list();
        }
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
    }

    public String getRequestUpload(HttpMethod httpMethod, String reqParams, Map<String, Object> paramsMap) throws MyException {
        JSONObject jsonObj = new JSONObject();
        String json = "";
        try {
            // 前四个参数为固定参数
            jsonObj.put(FinalData.PHONE_TYPE, FinalData.PHONE_TYPE_VALUE);
            jsonObj.put(FinalData.IMEI, FinalData.IMEI_VALUE);
            jsonObj.put(FinalData.VERSION_CODE, FinalData.VERSION_CODE_VALUE);
            // 判断是否还有其他参数需发送到服务器
            if (paramsMap != null) {
                Set<Map.Entry<String, Object>> set = paramsMap.entrySet();
                for (Map.Entry<String, Object> entry : set) {
                    jsonObj.put(entry.getKey(), entry.getValue());
                }
            }
            // 转为json格式String
            json = jsonObj.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 拼接请求地址
        // reqParams;
        String urlPath = FinalData.IMAGE_URL_UPLOAD + "uploadVoice";
        Log.i("TAG", "urlPath=" + urlPath);

        json = MyHttpConnection.httpPost(urlPath, json);
        // 判断是否返回为空，不为空缓存数据
        Log.i("TAG", "json=" + json);
        return json;
        // return getJson(json, cls);
    }


    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


}
