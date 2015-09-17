package com.dxj.teacher.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.widget.TitleNavBar;

/**
 * Created by khb on 2015/9/16.
 */
public class BrowserActivity extends BaseActivity {

    private WebView browser;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        browser = (WebView) findViewById(R.id.wv_browser);
        browser.loadUrl(url);
    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra("url");


    }
}
