package com.hdcy.app.fragment.first;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.model.ArticleInfo;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import java.util.zip.Inflater;

import static com.hdcy.base.BaseData.URL_BASE;

/**
 * Created by WeiYanGeorge on 2016-08-18.
 */


public class InfoDetailFragment extends BaseBackFragment {

    WebView myWebView;
    TextView tv_comment_count;
    TextView tv_comment_submit;
    TextView tv_comment_cancel;
    TextView tv_limit;
    EditText editText;
    Button sendButton;
    TextView title;
    AlertDialog alertDialog;
    LinearLayout ly_comment_button;
    private String targetId;
    private String Url = URL_BASE + "/articleDetails.html?id=";
    private String loadurl;
    private Toolbar mToolbar;
    private boolean isEdit;

    private String content;

    private ArticleInfo articleInfo = new ArticleInfo();


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
        final View view = inflater.inflate(R.layout.fragment_infodetail, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            targetId = bundle.getString("targetid");
        }
        loadurl = Url + targetId ;
        initView(view);
        initData();
        setListener();
        initWebview(view);
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(getActivity(),mPermissionList, 100);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tv_comment_count = (TextView) view.findViewById(R.id.tv_comment_count);
        //editText = (EditText) view.findViewById(R.id.et_send);
        sendButton = (Button) view.findViewById(R.id.bt_send);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        ly_comment_button = (LinearLayout) view.findViewById(R.id.ly_comment_countimage);
        title.setText("资讯详情");
        initToolbarNav(mToolbar);
        mToolbar.inflateMenu(R.menu.hierarchy);
    }

    private void initWebview(View view) {
        myWebView = (WebView) view.findViewById(R.id.mywebview);
        Log.e("WebUrl", loadurl);
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

    private boolean checkData() {
        content = editText.getText().toString();
        return true;
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

    /**
     * 刷新控件数据
     */
    private void resetViewData() {
        int fontcount = 200 - editText.length();
        tv_limit.setText(fontcount + "");
    }

    private void initData() {
        GetArticleInfo();

    }

    private void setData() {
        tv_comment_count.setText(articleInfo.getCommentCount() + "");
        ly_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(CommentListFragment.newInstance(articleInfo.getId() + "", "article")));
                onResume();
            }
        });

    }

    private void setListener() {


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInputDialog();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withTitle(articleInfo.getTitle()+"")
                        .withText("好多车友")
                        .withTargetUrl(loadurl+"&show=YES")
                        .withMedia(new UMImage(getContext(),articleInfo.getImage()))
                        .setListenerList(umShareListener)
                        .open();

                return false;
            }
        });

    }

    /**
     * 弹出输入框Dialog
     */

    private void ShowInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_dialog, null);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEdit = s.length() > 0;
                resetViewData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        tv_limit = (TextView) view.findViewById(R.id.tv_limit);
        tv_comment_submit = (TextView) view.findViewById(R.id.tv_submit_comment);
        tv_comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    PublishComment();
                }
            }
        });
        tv_comment_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_comment_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        editText = (EditText) view.findViewById(R.id.edt_comment);
        editText.addTextChangedListener(textWatcher);
        editText.requestFocus();
        builder.setView(view);
        builder.create();
        alertDialog = builder.create();
        Window windowManager = alertDialog.getWindow();
        windowManager.setGravity(Gravity.BOTTOM);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        alertDialog.show();
    }

    private void GetArticleInfo() {
        NetHelper.getInstance().GetArticleInfo(targetId, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                articleInfo = responseInfo.getArticleInfo();
                setData();
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    public void PublishComment() {
        NetHelper.getInstance().PublishComments(targetId, content, "article", null, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Toast.makeText(getActivity(), "评论发布成功", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("发布成功", targetId);

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Toast.makeText(getContext(), "评论发布失败", Toast.LENGTH_LONG).show();


            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat","platform"+platform);
                Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
        com.umeng.socialize.utils.Log.d("result","onActivityResult");
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