package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Content;
import com.hdcy.base.BaseInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */


public class CommentListFragmentAdapter extends RecyclerView.Adapter<CommentListFragmentAdapter.MyViewHolder>{

    private List<CommentsContent> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    private OnItemClickListener itemClickListener;


    public CommentListFragmentAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentsContent item = mItems.get(position);
        holder.tv_name.setText(item.getCreaterName()+"");
        Picasso.with(context).load(item.getCreaterHeadimgurl())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(50,50)
                .centerCrop()
                .into(holder.iv_avatar);
        holder.tv_time.setText(item.getCreatedTime().toLocaleString()+"");
        holder.tv_comment_content.setText(item.getContent());

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_avatar,iv_praise;
        private TextView tv_name,tv_praise_count, tv_time,tv_comment_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_avatar =(ImageView) itemView.findViewById(R.id.iv_avatar);
            iv_praise =(ImageView) itemView.findViewById(R.id.iv_praise);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_praise_count = (TextView) itemView.findViewById(R.id.tv_praise_count);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);

        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}