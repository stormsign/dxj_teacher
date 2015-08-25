package com.dxj.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.dxj.student.R;
import com.dxj.student.adapter.UpdateImageAdapter;

import java.util.ArrayList;
import java.util.List;



public class UpdateImageActivity extends Activity {

    private static final int REQUEST_IMAGE = 3;

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera;
    private EditText mRequestNum;

    private ArrayList<String> mSelectPath;
    private GridView feedbackNoScrollgridview;
    private UpdateImageAdapter updateAdapter;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiimage);

        mResultText = (TextView) findViewById(R.id.result);
        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
        mRequestNum = (EditText) findViewById(R.id.request_num);
        feedbackNoScrollgridview = (GridView) findViewById(R.id.feedback_noScrollgridview);
        updateAdapter = new UpdateImageAdapter(UpdateImageActivity.this);
        feedbackNoScrollgridview.setAdapter(updateAdapter);
        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.multi) {
                    mRequestNum.setEnabled(true);
                } else {
                    mRequestNum.setEnabled(false);
                    mRequestNum.setText("");
                }
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

                if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                } else {
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                }

                boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;

                int maxNum = 9;
                if (!TextUtils.isEmpty(mRequestNum.getText())) {
                    maxNum = Integer.valueOf(mRequestNum.getText().toString());
                }

                Intent intent = new Intent(UpdateImageActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
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
}
