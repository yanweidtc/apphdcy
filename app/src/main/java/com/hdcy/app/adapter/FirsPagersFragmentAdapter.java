package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.Article;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.NewsArticleInfo;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-17.
 */

public class FirsPagersFragmentAdapter extends RecyclerView.Adapter<FirsPagersFragmentAdapter.MyViewHolder> {
    private List<Content> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;


    private OnItemClickListener itemClickListener;

    public FirsPagersFragmentAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
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
                    int count = mItems.get(position).getReadCount()+1;
                    holder.tv_info_watched.setText(count+"");
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Content item = mItems.get(position);
        holder.tvTitle.setText(item.getTitle());
        SimpleDateFormat foramt = new SimpleDateFormat("MM-dd");
        String dateformat = foramt.format(item.getCreatedTime()).toString();
        holder.tv_info_update.setText(dateformat);
        holder.tv_info_watched.setText(item.getReadCount()+"");
       if(!BaseUtils.isEmptyList(item.getTagInfos()))
       if (!BaseUtils.isEmptyString(item.getImage())) {
           String cover = item.getImage();
           // Log.e("imageurl",cover);
       Picasso.with(context).load(cover)
                    .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                    .error(BaseInfo.PICASSO_ERROR)
                    .resize(240,240)
                    .centerCrop()
                    .into(holder.iv_info_cover);
       }
        if(!BaseUtils.isEmptyList(item.getTagInfos())){
            if(!BaseUtils.isEmptyString(item.getTagInfos().get(0).getName())){
               holder.tv_info_tag.setText(item.getTagInfos().get(0).getName()+"");
            }
        }
        if(item.getBusiness() ==true){
            holder.tv_info_tag.setText("置顶");
            holder.tv_info_update.setVisibility(View.GONE);
        }

        // holder.tv_info_tag.setText(item.getTagInfos().get(0).getName());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Content getItem(int position) {
        return mItems.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tv_info_update,tv_info_watched, tv_info_tag;
        private ImageView iv_info_cover;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_info_title);
            tv_info_update = (TextView)itemView.findViewById(R.id.tv_info_update);
           tv_info_watched = (TextView)itemView.findViewById(R.id.tv_info_watched);
            iv_info_cover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tv_info_tag =(TextView) itemView.findViewById(R.id.tv_info_tag_name);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
