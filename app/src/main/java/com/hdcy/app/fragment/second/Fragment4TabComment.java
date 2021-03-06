package com.hdcy.app.fragment.second;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.adapter.CommentListViewFragmentAdapter;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Replys;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * 视频 评论
 */
public class Fragment4TabComment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private ListView mRecy;
    private BGARefreshLayout mRefreshLayout;
    private boolean mAtTop = true;

    TextView tv_comment_submit;
    TextView tv_comment_cancel;
    TextView tv_limit;
    EditText editText;
    AlertDialog alertDialog;
    Button sendButton;

    String targetid;
    String replyid;

    int globalposition;


    private Toolbar mToolbar;
    private TextView title;

    private boolean praise = false;


    private int mScrollTotal;

    private String content;

    private boolean isEdit;//是否编辑过


    private CommentListViewFragmentAdapter mAdapter;

    //加载更多页数,默认第一页为0
    private int pagecount = 0;

    private String tagId;
    private String target;


    private List<CommentsContent> commentsList = new ArrayList<>();
    private CommentsContent commentsContent = new CommentsContent();

    private List<Replys> replysList = new ArrayList<>();
    private Replys replys;

    private RootListInfo rootobjet = new RootListInfo();
    private boolean isLast;


    public static Fragment4TabComment newInstance(String tagId , String target) {
        Fragment4TabComment fragment = new Fragment4TabComment();
        Bundle bundle = new Bundle();
        bundle.putString("param", tagId);
        bundle.putString("param1", target);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab_vedio_comment, container, false);
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagId = bundle.getString("param");
            target = bundle.getString("param1");
        }
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view) {
        mRecy = (ListView) view.findViewById(R.id.recy);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));

        View headview = View.inflate(getContext(),R.layout.item_comment_top,null);
        mRecy.addHeaderView(headview);
        sendButton = (Button) view.findViewById(R.id.bt_send);

        mAdapter = new CommentListViewFragmentAdapter(getContext(),commentsList);
        mRecy.setAdapter(mAdapter);
        mAdapter.setOnAvatarClickListener(new CommentListViewFragmentAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatar(int position) {
                Log.e("replyid", position+"");
                replyid = commentsList.get(position).getId() + "";
                Log.e("replyid", replyid);
                targetid = tagId;
                globalposition = position;
                ShowInputDialog();
            }
        });
        mAdapter.setOnReplyClickListener(new CommentListViewFragmentAdapter.OnReplyClickListener() {
            @Override
            public void onReplyClick(int position, Replys reply) {
                Toast.makeText(getContext(), "点击的位置" + position, Toast.LENGTH_SHORT).show();
                replyid = reply.getId()+"";
                targetid = reply.getTargetId()+"";
                ShowInputDialog();

            }
        });

    }

    private void setListener() {


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyid = null;
                targetid = tagId;
                ShowInputDialog();
            }
        });


    }


    private void initData() {
        GetCommentsList();
    }

    private void setData() {
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.endLoadingMore();

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        commentsList.clear();
        pagecount = 0;
        initData();
        mRefreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pagecount++;
        if(isLast){
            mRefreshLayout.endLoadingMore();
            Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            initData();
            return true;
        }
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (mAtTop) {
            mRefreshLayout.beginRefreshing();
        } else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 检查数据是否为空
     *
     * @return
     */

    private boolean checkData() {
        content = editText.getText().toString();
        return true;
    }

    /**
     * 刷新控件数据
     */
    private void resetViewData() {
        int fontcount = 200 - editText.length();
        tv_limit.setText(fontcount + "");
    }

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

    public void GetCommentsList() {
        NetHelper.getInstance().GetCommentsList(tagId,target, pagecount, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<CommentsContent> Fragment4TabCommentListtemp = responseInfo.getCommentsContentList();
                commentsList.addAll(Fragment4TabCommentListtemp);
                rootobjet = responseInfo.getRootListInfo();
                isLast = rootobjet.isLast();
                Log.e("CommentisLast",isLast+""+tagId);
                Log.e("CommentListsize", commentsList.size() + "");
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
        NetHelper.getInstance().PublishComments(targetid, content, target, replyid, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                alertDialog.dismiss();
                if (replyid == null) {
                    commentsContent = responseInfo.getCommentsContent();
                    commentsList.add(0, commentsContent);
                } else {
                    Log.e("评论成功后的数据", commentsList.size()+"");
                    replys = responseInfo.getReplys();
                    replysList = commentsList.get(globalposition).getReplys();
                    replysList.add(0, replys);
                    commentsContent = commentsList.get(globalposition);
                    commentsContent.setReplys(replysList);
                    commentsList.set(globalposition,commentsContent);
                    Log.e("评论成功后的数据", commentsList.size()+"");

                }
                mAdapter.notifyDataSetChanged();
                setData();

                Toast.makeText(getActivity(), "评论发布成功", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("发布成功", targetid);

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Toast.makeText(getContext(), "评论发布失败", Toast.LENGTH_LONG).show();


            }
        });
    }
}
