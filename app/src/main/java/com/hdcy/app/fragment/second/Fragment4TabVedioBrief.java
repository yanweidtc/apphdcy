package com.hdcy.app.fragment.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseFragment;


/**
 * 视频简介
 */
public class Fragment4TabVedioBrief extends BaseFragment {
    private static final String Params_VedioStr = "Arg_params";

    private String mStr =null;

    private TextView mTvTitle;

    private WebView mWebView;


    public static Fragment4TabVedioBrief newInstance(String str) {

        Bundle args = new Bundle();
        args.putString(Params_VedioStr, str);
        Fragment4TabVedioBrief fragment = new Fragment4TabVedioBrief();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStr = getArguments().getString(Params_VedioStr);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab_vedio_brief, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitle.setText("视频简介");
        mWebView= (WebView) view.findViewById(R.id.wv_vedio_desc);
//        mWebView.loadUrl("http://baidu.com");
        mWebView.loadDataWithBaseURL(null, mStr, "text/html", "utf-8", null);
        mWebView.getSettings().setJavaScriptEnabled(true);


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

}
