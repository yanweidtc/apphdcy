package com.hdcy.app.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.model.ArticleInfo;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;

import static com.hdcy.base.BaseData.URL_BASE;

/**
 * Created by WeiYanGeorge on 2016-08-18.
 */


public  class InfoDetailFragment extends BaseBackFragment {

    WebView myWebView;
    TextView tv_comment_count;
    EditText editText;
    Button sendButton;
    TextView title;
    private String targetId;
    private String Url = URL_BASE +"/articleDetails.html?id=";
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
        View view = inflater.inflate(R.layout.fragment_infodetail,container,false);
        Bundle bundle= getArguments();
        if (bundle !=null){
            targetId = bundle.getString("targetid");
        }
        loadurl = Url+targetId;
        myWebView = (WebView)view.findViewById(R.id.mywebview);
        Log.e("访问滴滴",loadurl);
        myWebView.loadUrl("http://www.ifeng.com");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tv_comment_count = (TextView) view.findViewById(R.id.tv_comment_count);
        editText = (EditText) view.findViewById(R.id.et_send);
        sendButton = (Button) view.findViewById(R.id.bt_send);
        title = (TextView) view.findViewById(R.id.toolbar_title);

        title.setText("资讯详情");

        initToolbarNav(mToolbar);

    }

    private boolean checkData(){
        content = editText.getText().toString();
        return true;

    }

    private void initData(){
        GetArticleInfo();

    }

    private void setData(){
        tv_comment_count.setText(articleInfo.getCommentCount()+"");
        tv_comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(CommentListFragment.newInstance(articleInfo.getId()+"")));
               // ((SupportFragment) getParentFragment()).start(CommentListFragment.newInstance(articleInfo.getId()+""));
            }
        });

    }

    private void setListener(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEdit = s.length() >0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    PublishComment();
                }
            }
        });

    }

    private void GetArticleInfo(){
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
    public void PublishComment(){
        NetHelper.getInstance().PublishComments(targetId, content,new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("发布成功",targetId);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("发布成功",targetId);

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("发布成功",targetId);

            }
        });
    }

}