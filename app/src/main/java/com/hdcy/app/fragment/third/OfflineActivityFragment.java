package com.hdcy.app.fragment.third;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.adapter.ActivityCommentListAdapter;
import com.hdcy.app.adapter.CommentListViewFragmentAdapter;
import com.hdcy.app.adapter.ImageListViewAdapter;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.first.CommentListFragment;
import com.hdcy.app.fragment.third.child.PhotoScaleFragment;
import com.hdcy.app.model.ActivityDetails;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Result;
import com.hdcy.app.view.NoScrollHListView;
import com.hdcy.app.view.NoScrollListView;
import com.hdcy.base.utils.BaseUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

import static com.hdcy.base.BaseData.URL_BASE;

/**
 * Created by WeiYanGeorge on 2016-09-03.
 */

public class OfflineActivityFragment extends BaseBackFragment{

    private boolean mAtTop = true;

    private Toolbar mToolbar;
    private TextView title;
    private TextView tv_desc_html;
    private TextView tv_actvity_title;
    private TextView tv_attend_count;
    private TextView tv_activity_sponsor;
    private TextView tv_activity_starttime;
    private TextView tv_activity_register_end;
    private TextView tv_people_limits;
    private TextView tv_activity_fee;
    private TextView tv_activity_address;
    private ImageView iv_activity_phone;
    private ImageView iv_activity_comment;
    private TextView tv_more_comment;
    private TextView tv_activity_comment_status;


    //客服dialog
    private TextView tv_activity_waiter;
    private TextView tv_activity_waiter_phone;
    private ImageView iv_call_phone;
    private ImageView iv_cal_cancel;

    private AlertDialog alertDialogphone;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogoffregister;

    //输入评论dialog

    TextView tv_comment_submit;
    TextView tv_comment_cancel;
    TextView tv_limit;
    EditText editText;
    TextView tv_dialog_title;
    private String content;
    private boolean isEdit;
    private String targetId;
    private String message;
    private String target="activity";

    private Button button_submit;
    private Button bt_register;
    private Button bt_cancel;

    private HListView lv_imgs;
    private NoScrollListView lv_activity_comment;

    private ActivityCommentListAdapter mAdapter;


    ExpandableTextView expTv1;

    private ImageListViewAdapter imageListViewAdapter;

    private int pagecount = 0;

    private String activityid;
    private ActivityDetails activityDetails;
    private Result result;
    private List<CommentsContent> commentsList = new ArrayList<>();

    private List<String> imgurls = new ArrayList<String>();

    private String htmlcontent;
    private String Url = URL_BASE + "/activityDetails.html?id=";
    private String loadurl;


    public static OfflineActivityFragment newInstance(String ActivityId){
        OfflineActivityFragment fragment = new OfflineActivityFragment();
        Bundle args = new Bundle();
        args.putString("param",ActivityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_offline_activity, container, false);
        activityid = getArguments().getString("param");
        loadurl = Url+activityid;
        initView(view);
        GetCommentsList();
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("活动详情");
        initToolbarNav(mToolbar);
        mToolbar.inflateMenu(R.menu.hierarchy);
        lv_activity_comment = (NoScrollListView) view.findViewById(R.id.lv_activity_comment);
        lv_activity_comment.setFocusable(false);
        tv_actvity_title = (TextView) view.findViewById(R.id.tv_activity_detail_title);
        tv_desc_html = (TextView) view.findViewById(R.id.expandable_text);
        lv_imgs = (HListView) view.findViewById(R.id.lv_imgs_list);
        tv_attend_count = (TextView) view.findViewById(R.id.tv_attend_count);
        tv_activity_sponsor = (TextView) view.findViewById(R.id.tv_activity_sponsor);
        tv_activity_starttime = (TextView) view.findViewById(R.id.tv_activity_starttime);
        tv_activity_register_end = (TextView) view.findViewById(R.id.tv_activity_register_end);
        tv_people_limits =(TextView) view.findViewById(R.id.tv_people_limits);
        tv_activity_fee = (TextView) view.findViewById(R.id.tv_activity_fee);
        tv_activity_address =(TextView) view.findViewById(R.id.tv_activity_address);
        tv_activity_comment_status = (TextView) view.findViewById(R.id.tv_activity_comment_status);

        iv_activity_phone =(ImageView) view.findViewById(R.id.iv_activity_phone);
        iv_activity_comment = (ImageView) view.findViewById(R.id.iv_activity_comment);
        tv_more_comment =(TextView) view.findViewById(R.id.tv_more_comment);
        button_submit = (Button) view.findViewById(R.id.bt_send);


    }
    private void setListener() {

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withTitle(activityDetails.getName()+"")
                        .withText("好多车友")
                        .withTargetUrl(loadurl)
                        .withMedia(new UMImage(getContext(),activityDetails.getImage()))
                        .setListenerList(umShareListener)
                        .open();

                return false;
            }
        });

    }

    private void initData(){
        GetActivityDetails();
        GetActivityAttendStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setData(){
        htmlcontent = activityDetails.getDesc();
        ActivityDetails temp = activityDetails;
        org.jsoup.nodes.Document document = Jsoup.parse(htmlcontent);
        String htmlcontent = document.select("p").text();
        tv_actvity_title.setText(activityDetails.getName());
        ExpandableTextView expTv1 = (ExpandableTextView) getView().findViewById(R.id.expand_text_view);
        expTv1.setText(htmlcontent);
        imgurls = activityDetails.getImages();
        Log.e("imgurlssize", imgurls.size()+"");
        imageListViewAdapter = new ImageListViewAdapter(getContext(),imgurls);
        lv_imgs.setAdapter(imageListViewAdapter);

        lv_imgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String imgUrls[] =new String[imgurls.size()];
                for(int t =0 ; t< imgurls.size(); t++ ){
                    String str = imgurls.get(t);
                    imgUrls[t] = str;
                }
                EventBus.getDefault().post(new StartBrotherEvent(PhotoScaleFragment.newInstance(imgUrls, i)));

            }
        });
        iv_activity_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhoneAlertDialog();
            }
        });

        iv_activity_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetId = activityDetails.getId()+"";
                ShowInputDialog();
                tv_dialog_title.setText("写留言");

            }
        });

        tv_more_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(CommentListFragment.newInstance(activityDetails.getId()+"","activity")));
            }
        });



        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result.getContent() == false && activityDetails.getFinish() ==false) {
                    ShowRegisterAlertDialog();
                }else {
                    if(activityDetails.getFinish()==false){
                        Toast.makeText(getActivity(), "您已完成报名!", Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(getActivity(), "活动已结束!", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
        });

        //填充数据
        tv_attend_count.setText(activityDetails.getHot()+"");
        tv_activity_sponsor.setText(activityDetails.getSponsorName()+"");
        SimpleDateFormat foramt = new SimpleDateFormat("yyyy年MM月dd日");
        String dateformat1 = foramt.format(activityDetails.getStartTime()).toString();
        String dateformat2 = foramt.format(activityDetails.getSignEndTime()).toString();
        tv_activity_starttime.setText(dateformat1);
        tv_activity_register_end.setText(dateformat2);
        tv_people_limits.setText(activityDetails.getPeopleLimit()+"");
        tv_activity_fee.setText(activityDetails.getPrice()+"");
        tv_activity_address.setText(activityDetails.getAddress()+"");



        mAdapter = new ActivityCommentListAdapter(getContext(),commentsList);
        lv_activity_comment.setAdapter(mAdapter);

        if(commentsList.isEmpty()){
            tv_activity_comment_status.setVisibility(View.VISIBLE);
        }else {
            tv_activity_comment_status.setVisibility(View.GONE);
        }



    }

    private void GetActivityDetails(){
        NetHelper.getInstance().GetActivityDeatil(activityid, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                ActivityDetails temp = responseInfo.getActivityDetails();
                activityDetails = temp;
                Log.e("activityinfo", activityDetails.getId()+"");
                if(activityDetails.getFinish() == true){
                    button_submit.setBackgroundResource((R.color.main_font_gray_2));
                    button_submit.setText("已结束");
                }
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

    /**
     * 发布评论
     */

    public void PublishComment() {
        NetHelper.getInstance().PublishComments(activityid, content, "activity", null, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Toast.makeText(getActivity(), "留言发布成功", Toast.LENGTH_LONG).show();
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

    /**
     * 得到报名状态
     */

    public void GetActivityAttendStatus(){
        NetHelper.getInstance().GetCurrentPaticipationStatus(activityid, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                result =responseInfo.getResultinfo();
                if(result.getContent() == true){
                    button_submit.setBackgroundResource((R.color.main_font_gray_2));
                    button_submit.setText("已报名");
                }
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    public void GetCommentsList() {
        NetHelper.getInstance().GetCommentsList(activityid,target, pagecount, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                if (commentsList.isEmpty()) {
                    List<CommentsContent> commentListFragmentListtemp = responseInfo.getCommentsContentList();
                    commentsList.addAll(commentListFragmentListtemp);
                    Log.e("CommentListsize", commentsList.size() + "");
                }

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

    public void RegisterOfflineActivity(){
        NetHelper.getInstance().RegisterOfflineActivity(activityid, content, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                alertDialogoffregister.dismiss();
                Toast.makeText(getActivity(), "报名成功!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    private void ShowRegisterAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_register_offactivity,null);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEdit = s.length() >0;
                resetViewData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        tv_limit = (TextView) view.findViewById(R.id.tv_limit);
        bt_register = (Button) view.findViewById(R.id.bt_register);
        bt_cancel =(Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogoffregister.dismiss();
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()) {
                    RegisterOfflineActivity();
                }
            }
        });
        editText = (EditText) view.findViewById(R.id.edt_comment);
        editText.addTextChangedListener(textWatcher);
        editText.requestFocus();


        builder.setView(view);
        builder.create();
        alertDialogoffregister = builder.create();
        Window wm = alertDialogoffregister.getWindow();
        wm.setGravity(Gravity.BOTTOM);
        alertDialogoffregister.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        alertDialogoffregister.show();


    }

    private void ShowPhoneAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_phone,null);
        tv_activity_waiter = (TextView) view.findViewById(R.id.tv_activity_waiter);
        tv_activity_waiter_phone = (TextView) view.findViewById(R.id.tv_activity_waiter_phone);
        iv_call_phone = (ImageView) view.findViewById(R.id.iv_call_phone);
        iv_cal_cancel = (ImageView) view.findViewById(R.id.iv_call_cancel);

        tv_activity_waiter.setText(activityDetails.getWaiterName()+"");
        tv_activity_waiter_phone.setText(activityDetails.getWaiterPhone());
        builder.setView(view);
        builder.create();
        alertDialogphone = builder.create();
        Window wm = alertDialogphone.getWindow();
        wm.setGravity(Gravity.CENTER);

        iv_cal_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogphone.dismiss();
            }
        });
        iv_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCallPhone(activityDetails.getWaiterPhone());
            }
        });
        alertDialogphone.show();

    }

    public void doCallPhone(String phone) {
        LogUtil.i("doCallPhone：" + phone);
        final String finalPhone = phone.startsWith("tel:") ? phone : ("tel:" + phone);
        LogUtil.i("doCallPhone finalPhone：" + finalPhone);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(finalPhone));
        startActivity(intent);
    }
    /**
     * 弹出输入框Dialog
     */

    private void ShowInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_dialog,null);
        tv_dialog_title =(TextView) view.findViewById(R.id.tv_dialog_title);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEdit = s.length() >0;
                resetViewData1();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        tv_limit =(TextView) view.findViewById(R.id.tv_limit);
        tv_comment_submit = (TextView) view.findViewById(R.id.tv_submit_comment);
        tv_comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
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
        editText =(EditText) view.findViewById(R.id.edt_comment);
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

    private boolean checkData(){
        content = editText.getText().toString();
        if (BaseUtils.isEmptyString(content)||content.trim().isEmpty()) {
            Toast.makeText(getActivity(), "请输入你要发布的文字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    /**
     * 刷新控件数据
     */
    private void resetViewData(){
        int fontcount = 70 - editText.length();
        tv_limit.setText(fontcount+"");
    }

    private void resetViewData1(){
        int fontcount = 250 - editText.length();
        tv_limit.setText(fontcount+"");
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



}
