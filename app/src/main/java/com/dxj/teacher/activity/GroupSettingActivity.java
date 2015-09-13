package com.dxj.teacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.bean.BaseBean;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.bean.StudyGroup;
import com.dxj.teacher.http.CustomStringRequest;
import com.dxj.teacher.http.FinalData;
import com.dxj.teacher.http.VolleySingleton;
import com.dxj.teacher.utils.HttpUtils;
import com.dxj.teacher.utils.MyAsyn;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.PhotoFileUtils;
import com.dxj.teacher.utils.SPUtils;
import com.dxj.teacher.utils.StringUtils;
import com.dxj.teacher.utils.UpdatePhotoUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2015/9/2.
 */
public class GroupSettingActivity extends BaseActivity {

    private static final int EXIT = 3;
    private static final int DISMISS = 4;
    private static final int GET_CUT_PICTURE = 11;
    private EditText groupname;
    private EditText description;
    private final static int MAX_WORD_COUNT = 200;
    private TextView word_count;
    private SwitchCompat message_switch;
    private StudyGroup group;
    private TextView tv_groupname;
    private TextView tv_description;
    private ImageView group_head;
    private String headUrl;
    private boolean isMsgBlocked;

    private final static int BLOCK_SUCCESS = 1;
    private final static int UNBLOCK_SUCCESS = 2;
    private final static int BLOCK_FAILED = -1;
    private final static int BLOCK_FAILED_PERMISSION = -2;
    private final static int UNBLOCK_FAILED = -3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNBLOCK_SUCCESS:
                    System.out.println("已解除屏蔽该学团");
                    showToast("已解除屏蔽该学团");
                    break;
                case UNBLOCK_FAILED:
                    System.out.println("解除屏蔽该学团失败，请稍后再试");
                    showToast(
                            "解除屏蔽该学团失败，请稍后再试");
                    break;
                case BLOCK_SUCCESS:
                    System.out.println("已屏蔽该学团");
                    showToast("已屏蔽该学团");
                    break;
                case BLOCK_FAILED_PERMISSION:
                    int errorCode = (int) msg.obj;
                    if (errorCode == -1) {
                        System.out.println("屏蔽该学团失败，团长不能屏蔽群消息 " + errorCode);
                        showToast("屏蔽该学团失败，团长不能屏蔽群消息");
                    }
                    break;
                case GET_CUT_PICTURE:
//                    Glide.with(context).load((Bitmap)msg.obj).asBitmap().placeholder(R.mipmap.default_image).into(group_head);
                    group_head.setImageBitmap((Bitmap) msg.obj);
                    new MyAsyn(context, getAsynResponse(), imagePath, HttpUtils.UPLOAD_IMG).execute();
                default:
                    break;
            }
        }
    };
    private String imageAddress;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsetting);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("设置");
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
                setResult(RESULT_OK, new Intent().putExtra("groupname", groupname.getText().toString().trim())
                        .putExtra("desc", description.getText().toString().trim())
                        .putExtra("headUrl", headUrl)
                        .putExtra("isMsgBlocked", isMsgBlocked));
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra("groupname", groupname.getText().toString().trim())
                .putExtra("desc", description.getText().toString().trim())
                .putExtra("headUrl", headUrl)
                .putExtra("isMsgBlocked", isMsgBlocked));
        super.onBackPressed();
    }

    @Override
    public void initView() {
        group_head = (ImageView) findViewById(R.id.group_head);
        groupname = (EditText) findViewById(R.id.et_groupname);
        description = (EditText) findViewById(R.id.et_description);
        tv_groupname = (TextView) findViewById(R.id.tv_groupname);
        tv_description = (TextView) findViewById(R.id.tv_description);
        word_count = (TextView) findViewById(R.id.tv_word_count);
        word_count.setText(description.getText().length() + "/" + MAX_WORD_COUNT);

        tv_description.setFocusable(true);
        tv_groupname.setFocusable(true);
        message_switch = (SwitchCompat) findViewById(R.id.sc_message_switch);
        isMsgBlocked = SPUtils.getSPData("group-" + group.getGroupId() + "-MsgBlocked", false);
        message_switch.setChecked(isMsgBlocked);
        message_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMsgBlocked = isChecked;
                blockGroupMsg(isMsgBlocked);
            }
        });

        // 监听输入的字数
        description.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;// 监听前的文本
            private int editStart;// 光标开始位置
            private int editEnd;// 光标结束位置
            private final int charMaxNum = MAX_WORD_COUNT;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                word_count.setText(s.length() + "/" + MAX_WORD_COUNT);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = description.getSelectionStart();
                editEnd = description.getSelectionEnd();
                if (temp.length() > charMaxNum) {
                    showToast("最多输入" + MAX_WORD_COUNT + "个字！");
                    s.delete(editStart - (temp.length() - charMaxNum), editEnd);
                    description.setText(s);
                    description.setSelection(editStart);
                }
            }
        });

        findViewById(R.id.iv_group_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicOrGallery(v);
            }
        });

        tv_groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_groupname.setVisibility(View.GONE);
                groupname.setVisibility(View.VISIBLE);
            }
        });
        tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_description.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
            }
        });

        if (!TextUtils.isEmpty(group.getHeadUrl())) {
            Glide.with(this).load(group.getHeadUrl()).placeholder(R.mipmap.default_error).into(group_head);
        }
        tv_groupname.setText(group.getGroupName());
        groupname.setText(group.getGroupName());
        tv_description.setText(group.getDescription());
        description.setText(group.getDescription());

        findViewById(R.id.quit);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        group = (StudyGroup) intent.getSerializableExtra("studyGroup");
        headUrl = group.getHeadUrl();
//        判断当前用户是团员还是团长
        Button quit = (Button) findViewById(R.id.quit);
        if (isLeader(mApplication.getUserId(), group)) {
            quit.setText("解散学团");
        } else {
//            不是团长就不显示编辑头像、团名等
            findViewById(R.id.cv_group_head).setVisibility(View.GONE);
            findViewById(R.id.cv_group_name).setVisibility(View.GONE);
            findViewById(R.id.cv_group_desc).setVisibility(View.GONE);
            quit.setText("退出学团");
        }
    }

    //    屏蔽学团消息
    private void blockGroupMsg(final boolean isMsgBlocked) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (isMsgBlocked) {
                    try {
                        EMGroupManager.getInstance().blockGroupMessage(
                                group.getGroupId());
                        Message msg = new Message();
                        msg.what = BLOCK_SUCCESS;
                        handler.sendMessage(msg);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = BLOCK_FAILED_PERMISSION;
                        msg.obj = e.getErrorCode();
                        handler.sendMessage(msg);
                    }
                } else {  //解除屏蔽
                    try {
                        EMGroupManager.getInstance().unblockGroupMessage(
                                group.getGroupId());
                        handler.sendEmptyMessage(UNBLOCK_SUCCESS);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(UNBLOCK_FAILED);
                    }
                }
            }

        }).start();
    }


    /**
     * 判断当前用户是否是团长
     *
     * @return
     */
    private boolean isLeader(String id, StudyGroup group) {
        return (group.getTeacherId()).equals(id);
    }

    public void quit(View view) {
        if (isLeader(mApplication.getUserId(), group)) { //如果不是团长，执行退团
            exitOrDismiss(DISMISS, group.getTeacherId());
        } else {  //如果是团长，执行解散团
            exitOrDismiss(EXIT, group.getTeacherId());
        }
    }

    private void exitOrDismiss(int param, String teacherId) {
        String url = null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", group.getId());
        if (param == EXIT) {
            url = FinalData.URL_VALUE_COMMON + "quitGroup";
            map.put("imId", DemoHXSDKHelper.getInstance().getHXId());
        } else if (param == DISMISS) {
            url = FinalData.URL_VALUE + "delGroup";
        }
        CustomStringRequest cRequest = new CustomStringRequest(Request.Method.POST, url, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(cRequest);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                BaseBean msg = gson.fromJson(s, BaseBean.class);
                if (msg.getCode() == 0) {
                    showToast(msg.getMsg());
                    mApplication.quitActivities();
                    finish();

                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("操作出现问题，请稍后再试");
            }
        };
    }

    /**
     * 显示弹出框选择相册或相机
     *
     * @param parentView
     */
    private void showPicOrGallery(View parentView) {

        final PopupWindow portraitPop = new PopupWindow(this);
        final View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        final LinearLayout ll_popoup = (LinearLayout) view.findViewById(R.id.ll_popup);
        portraitPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        portraitPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        portraitPop.setBackgroundDrawable(new BitmapDrawable());
        portraitPop.setFocusable(true);
        portraitPop.setOutsideTouchable(true);
        portraitPop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        bt1.setText("相册");
        bt2.setText("拍照");
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                portraitPop.dismiss();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                photo();
                UpdatePhotoUtils.startPhotoZoom(activity);
                portraitPop.dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (isMulti) {
//                    Intent intent = new Intent(context, AlbumActivity.class);
//                    startActivityForResult(intent, CHOOSE_ALBUM);
//                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//                } else {
//                    startPhotoZoom();
//                }
                portraitPop.dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                portraitPop.dismiss();
            }
        });
        portraitPop.setAnimationStyle(R.style.popwindow_anim_slide);
        portraitPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == UpdatePhotoUtils.RESULT_LOAD_IMAGE
                || requestCode == UpdatePhotoUtils.TAKE_PICTURE
                || requestCode == UpdatePhotoUtils.CUT_PHOTO_REQUEST_CODE) {

            Uri photoUri = Uri.fromFile(UpdatePhotoUtils.photo(context));
            //获取图片路径
            if (UpdatePhotoUtils.RESULT_LOAD_IMAGE == requestCode) {
                if (data == null) {
                    return;
                }
                Uri selectedImageUri = data.getData();
                //图片裁剪
                try {
                    imageAddress = UpdatePhotoUtils.getImageAddress();
                    if (!PhotoFileUtils.isFileExist("")) {
                        PhotoFileUtils.createSDDir("");
                    }
                    imagePath = PhotoFileUtils.SDPATH + imageAddress + ".JPEG";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showLogD("IMAGEPATH " + imageAddress);
                UpdatePhotoUtils.startPhotoZoomOne(selectedImageUri, activity, imageAddress);

            } else if (requestCode == UpdatePhotoUtils.TAKE_PICTURE) {
                // 拍摄图片
                try {
                    imageAddress = UpdatePhotoUtils.getImageAddress();
                    if (!PhotoFileUtils.isFileExist("")) {
                        PhotoFileUtils.createSDDir("");
                    }
                    imagePath = PhotoFileUtils.SDPATH + imageAddress + ".JPEG";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showLogD("IMAGEPATH " + imageAddress);
                UpdatePhotoUtils.startPhotoZoomOne(photoUri, activity, imageAddress);
            } else if (requestCode == UpdatePhotoUtils.CUT_PHOTO_REQUEST_CODE) {
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    new Thread() {
                        @Override
                        public void run() {
                            Bitmap bitmap = null;
                            Log.i("TAG", "imagePath=" + imagePath);
                            if (bitmap == null && !StringUtils.isEmpty(imagePath)) {
                                bitmap = MyUtils.getBitmapByPath(imagePath);
                                Log.i("TAG", "bitmap=" + bitmap);
                                Message msg = new Message();
                                msg.what = GET_CUT_PICTURE;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            }
                        }

                        ;
                    }.start();
                }

            }
        }

    }

    public void photo() {
        //获取文件
        File out = UpdatePhotoUtils.photo(this);
        Uri photoUri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, UpdatePhotoUtils.TAKE_PICTURE);
    }

    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Log.i("TAG", "Update+result=" + result);
                Gson gson = new Gson();
                HeadUrl headUrlBean = gson.fromJson(result, HeadUrl.class);
                if (headUrlBean.getCode() == 0) {
                    showToast("头像上传成功");
                    headUrl = headUrlBean.getImages().get(0);
//                    sendRequestData(strHeadUrl, HEAD_URL);
                } else
                    showToast("头像上传失败");
            }
        };
    }


}
