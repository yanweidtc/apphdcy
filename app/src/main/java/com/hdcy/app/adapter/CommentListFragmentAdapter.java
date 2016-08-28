package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.fragment.first.CommentPraiseApi;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.Replys;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */


public class CommentListFragmentAdapter extends RecyclerView.Adapter<CommentListFragmentAdapter.MyViewHolder>{

    private List<CommentsContent> mItems = new ArrayList<>();
    private List<Replys> replysList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    private boolean isPraised = false;

    private OnItemClickListener itemClickListener;
    private OnPraiseClickListener onPraiseClickListener;


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
        final CommentListFragmentAdapter.MyViewHolder holder = new CommentListFragmentAdapter.MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, v, holder);
                }
            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CommentsContent item = mItems.get(position);
        replysList = item.getReplys();
        if(BaseUtils.isEmptyList(replysList)){
            Log.e("replysLIst数据","kong");
        }else {
            Log.e("replysList数据不为空", replysList.get(0).getContent()+"");
        }
        if(BaseUtils.isEmptyList(replysList)){
            holder.ly_sub_replys.setVisibility(View.GONE);
        }else {
            holder.lv_replys.setAdapter(new ReplysAdapter(context,replysList));
        }
        holder.tv_name.setText(item.getCreaterName()+"");
        Picasso.with(context).load(item.getCreaterHeadimgurl())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(50,50)
                .centerCrop()
                .into(holder.iv_avatar);
        holder.tv_time.setText(item.getCreatedTime().toLocaleString()+"");
        Log.e("CommentContent",item.getContent()+"");
        holder.tv_comment_content.setText(item.getContent());
        holder.tv_praise_count.setText(item.getPraiseCount()+"");

        holder.iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetId = item.getId()+"";
                int count = item.getPraiseCount();
                if(!isPraised){
                    CommentPraiseApi.doPraise(targetId);
                    isPraised = true;
                    holder.iv_praise.setImageResource(R.drawable.content_icon_zambia_pressed);
                    count++;
                    holder.tv_praise_count.setText(count+"");
                }else {
                    holder.iv_praise.setImageResource(R.drawable.content_con_zambia_default);
                    CommentPraiseApi.UndoPraise(targetId);
                    isPraised = false;
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

            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);

        }
    }

    public interface OnPraiseClickListener{
        void onPraise(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}