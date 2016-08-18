package com.hdcy.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;

import com.hdcy.app.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-08-18.
 */

public class WebViewBrowserActivity extends AppCompatActivity{

    @BindView(R.id.web)
    private String url, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superbrowser);
        //初始化组件
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("Articleid");

    }




}