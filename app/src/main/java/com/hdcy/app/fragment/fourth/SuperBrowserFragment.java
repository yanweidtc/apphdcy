package com.hdcy.app.fragment.fourth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.fragment.first.InfoDetailFragment;

import static com.hdcy.base.BaseData.URL_BASE;

/**
 * Created by WeiYanGeorge on 2016-09-19.
 */

public class SuperBrowserFragment extends BaseBackFragment {

    private Toolbar mToolbar;
    private TextView title;

    WebView myWebView;

    private String Url;
    private String loadurl;
    private String targetId;
    private String titleinfo;
    private int type;

    public static SuperBrowserFragment newInstance(int type,String id, String title){
        SuperBrowserFragment fragment = new SuperBrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putString("targetid", id);
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_superbrowser, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            targetId = bundle.getString("targetid");
            titleinfo = bundle.getString("title");
            type = bundle.getInt("type");
        }
        if(type ==1){
           Url = URL_BASE + "/giftDetails.html?id=";
        }else if(type==2){
            Url =URL_BASE +"/dakaList.html?id=";
        }
        loadurl = Url + targetId;
        initView(view);
        initWebview(view);
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText(titleinfo);
        initToolbarNav(mToolbar);

    }

    private void initWebview(View view){
        myWebView = (WebView) view.findViewById(R.id.superwebview);
        Log.e("WebUrl",loadurl);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.canGoBack();
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl(loadurl);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myWebView.clearCache(true);
        myWebView.removeAllViews();
        myWebView.goBack();
        myWebView.destroy();
        myWebView =null;
    }

    @Override
    public void onResume() {
        myWebView.reload();
        super.onResume();
    }

    @Override
    public void onPause() {
        myWebView.reload();
        super.onPause();
    }



    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();

    private class WebViewClientBase extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url,
                                           boolean isReload) {
            // TODO Auto-generated method stub
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();

    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            getActivity().setProgress(newProgress * 1000);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url,
                                           boolean precomposed) {
            // TODO Auto-generated method stub
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            // TODO Auto-generated method stub
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

    }
}
