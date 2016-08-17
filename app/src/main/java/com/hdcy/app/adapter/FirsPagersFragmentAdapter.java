package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.Article;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.NewsArticleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-17.
 */

public class FirsPagersFragmentAdapter extends RecyclerView.Adapter<FirsPagersFragmentAdapter.MyViewHolder> {
    private List<Content> mItems = new ArrayList<>();
    private LayoutInflater mInflater;

    private OnItemClickListener itemClickListener;

    public FirsPagersFragmentAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<Content> items){
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public FirsPagersFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home, parent, false);
        final FirsPagersFragmentAdapter.MyViewHolder holder = new FirsPagersFragmentAdapter.MyViewHolder(view);
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
        Content item = mItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Content getItem(int position) {
        return mItems.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
