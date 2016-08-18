package com.hdcy.app.fragment.second.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseBackFragment;

import static com.hdcy.base.BaseData.URL_BASE;

/**
 * Created by WeiYanGeorge on 2016-08-18.
 */


public  class InfoDetailFragment extends BaseBackFragment {

    WebView myWebView;
    private String targetId;
    private String Url = URL_BASE +"/index.html?id=";
    private String loadurl;
    private Toolbar mToolbar;


    public static InfoDetailFragment newInstance(String id) {
        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("targetid", id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infodetail,container,false);
        Bundle bundle = getArguments();
        if (bundle !=null){
            targetId = bundle.getString("targetid");
        }
        loadurl = Url+targetId;
        Log.e("访问地址",loadurl);
        myWebView = (WebView)view.findViewById(R.id.mywebview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(loadurl);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        initView(view);
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setTitle("咨询详情");

        initToolbarNav(mToolbar);
    }


}