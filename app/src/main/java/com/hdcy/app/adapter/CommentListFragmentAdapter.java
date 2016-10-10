package com.hdcy.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.PraiseResult;
import com.hdcy.app.model.Replys;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.ExpandableHListView;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */


public class CommentListFragmentAdapter extends RecyclerView.Adapter<CommentListFragmentAdapter.MyViewHolder>{

    private List<CommentsContent> mItems = new ArrayList<>();
    private List<Replys> replysList = new ArrayList<>();

    private List<CommentsContent> commentsList = new ArrayList<>();
    private CommentsContent commentsContent = new CommentsContent();

    private Replys replys;

    private LayoutInflater mInflater;
    private Context context;

    ReplysAdapter replysAdapter ;

    private boolean isPraised = false;

    private int actualcount;

    private boolean PraisedStatus;

    private PraiseResult praiseResult ;

    private OnItemClickListener itemClickListener;
    private OnPraiseClickListener onPraiseClickListener;

    private String replyid;
    private String targetid;
    private String target;

    TextView tv_comment_submit;
    TextView tv_comment_cancel;
    TextView tv_limit;
    EditText editText;
    AlertDialog alertDialog;
    Button sendButton;

    private String content;

    private boolean isEdit;//是否编辑过



    public CommentListFragmentAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setOnPraiseClickListener(OnPraiseClickListener onPraiseClickListener){
        this.onPraiseClickListener = onPraiseClickListener;
    }

    public void setDatas(List<CommentsContent> items){
        mItems.clear();
        mItems.addAll(items);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public CommentsContent getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comments, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, v, holder);
                }
            }
        });
        holder.getAdapterPosition();
        holder.setIsRecyclable(false);
        return holder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setTag(mItems.get(position).getId());
        final CommentsContent item = mItems.get(position);
        replysList = item.getReplys();
        holder.ly_sub_replys.setTag(position);
        if(BaseUtils.isEmptyList(replysList)){
            holder.ly_sub_replys.setVisibility(View.GONE);
        }else {
            holder.ly_sub_replys.setVisibility(View.VISIBLE);
            Log.e("Replyssize",replysList.size()+"");
            replysAdapter = new ReplysAdapter(context,replysList);
            holder.lv_replys.setAdapter(replysAdapter);
            holder.lv_replys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    replyid = item.getReplys().get(position).getId()+"";
                    target="article";
                    targetid = item.getReplys().get(position).getTargetId()+"";
                    ShowInputDialog();
                }
            });
            final int size = replysList.size();
            holder.tv_more.setTag(position);
            if(replysList.size() >2){
                holder.tv_more.setVisibility(View.VISIBLE);
                holder.tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = replysList.size();
                        replysAdapter.addItemNum(size);
                        replysAdapter.notifyDataSetChanged();
                        holder.tv_more.setVisibility(View.GONE);
                    }
                });
            }else {
                holder.tv_more.setVisibility(View.GONE);
            }
        }
        holder.tv_name.setText(item.getCreaterName()+"");
        Picasso.with(context).load(item.getCreaterHeadimgurl())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(50,50)
                .centerCrop()
                .into(holder.iv_avatar);
        SimpleDateFormat foramt = new SimpleDateFormat("MM-dd");
        String dateformat = foramt.format(item.getCreatedTime()).toString();
        holder.tv_time.setText(dateformat);
        Log.e("CommentContent",item.getContent()+"");
        holder.tv_comment_content.setText(item.getContent());
        holder.tv_praise_count.setText(item.getPraiseCount()+"");
        actualcount = item.getPraiseCount();
        Log.e("actualcount", actualcount+"");
        holder.iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) holder.getTag();
                if (tag == position) {
                    String targetId = item.getId() + "";
                    if (!isPraised) {
                        NetHelper.getInstance().DoPraise(targetId, new NetRequestCallBack() {
                            @Override
                            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                                praiseResult = responseInfo.getPraiseResult();
                                PraisedStatus = praiseResult.getContent();
                                if (PraisedStatus) {
                                    Log.e("点赞成功1", praiseResult.getContent() + "");
                                    isPraised = true;
                                    int count = actualcount+1;
                                    Log.e("actualcount", count + "");
                                    holder.iv_praise.setImageResource(R.drawable.content_icon_zambia_pressed);
                                    holder.tv_praise_count.setText(count + "");
                                } else {
                                    Toast.makeText(context, "你已经赞过", Toast.LENGTH_SHORT).show();
                                    holder.iv_praise.setImageResource(R.drawable.content_icon_zambia_pressed);
                                    isPraised = true;
                                    return;
                                }
                            }

                            @Override
                            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                            }

                            @Override
                            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                            }

                        });


                    } else {
                        UndoPraise(targetId);
                        int count = actualcount;
                        if(isPraised){
                        count = count -1;
                        holder.tv_praise_count.setText(actualcount + "");}
                        else {
                            holder.tv_praise_count.setText(actualcount);
                        }
                        holder.iv_praise.setImageResource(R.drawable.content_con_zambia_default);
                        Toast.makeText(context, "取消赞成功", Toast.LENGTH_SHORT).show();
                        isPraised = false;
                    }
                }
            }
        });


    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_avatar;
        private ImageView iv_praise;
        private TextView tv_name,tv_praise_count, tv_time,tv_comment_content;
        private ListView lv_replys;
        private LinearLayout ly_sub_replys;
        private TextView tv_more;

        private  Object tag;

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag){
            this.tag = tag;
        }

        public MyViewHolder(View itemView) {
            super(itemView);


            iv_avatar =(ImageView) itemView.findViewById(R.id.iv_avatar);
            iv_praise =(ImageView) itemView.findViewById(R.id.iv_praise);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_praise_count = (TextView) itemView.findViewById(R.id.tv_praise_count);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            ly_sub_replys = (LinearLayout) itemView.findViewById(R.id.ly_sub_reply);
            lv_replys = (ListView) itemView.findViewById(R.id.lv_replys);
            tv_more =(TextView) itemView.findViewById(R.id.tv_more);

            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);

        }
    }

    public interface OnPraiseClickListener{
        void onPraise(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public  void doPraise(final String targetId) {
            NetHelper.getInstance().DoPraise(targetId, new NetRequestCallBack() {
                @Override
                public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                }

                @Override
                public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                }

                @Override
                public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                }
            });
        }

        public  void UndoPraise(final String targetId) {
            NetHelper.getInstance().UnDoPraise(targetId, new NetRequestCallBack() {
                @Override
                public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                    Log.e("取消赞成功1", targetId);
                }

                @Override
                public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

                }

                @Override
                public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                    Log.e("取消赞失败", targetId);

                }
            });
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = mInflater.inflate(R.layout.fragment_edit_dialog, null);

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
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        alertDialog.show();
    }


    public void PublishComment() {
        NetHelper.getInstance().PublishComments(targetid, content, target, replyid, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("评论成功后的数据", responseInfo.toString());
                alertDialog.dismiss();
                replys = responseInfo.getReplys();
                replysList.add(0, replys);
                replysAdapter.notifyDataSetChanged();

                Toast.makeText(context, "评论发布成功", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {


            }
        });
    }




}