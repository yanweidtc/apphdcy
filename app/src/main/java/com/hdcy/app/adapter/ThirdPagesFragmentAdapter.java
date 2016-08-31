package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.Content;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-31.
 */

public class ThirdPagesFragmentAdapter extends RecyclerView.Adapter<ThirdPagesFragmentAdapter.MyViewHolder>{

    private List<ActivityContent> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;


    private OnItemClickListener itemClickListener;

    public ThirdPagesFragmentAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setDatas(List<ActivityContent> items){
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_activity_list, parent , false);
        final MyViewHolder holder = new MyViewHolder(view );
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActivityContent item = mItems.get(position);
        holder.tv_activity_title.setText(item.getName()+"");
        holder.tv_activity_subtitle.setText(item.getAddress()+"");
        if(!BaseUtils.isEmptyString(item.getImage())) {
            String cover = item.getImage();
            Picasso.with(context).load(cover)
                    .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                    .resize(400,240)
                    .centerCrop()
                    .into(holder.iv_activity_background);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public  ActivityContent getItem(int position) {
        return mItems.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private Object tag;

        public Object getTag(){
            return tag;
        }
        public void setTag(Object tag){
            this.tag = tag;
        }
        private TextView tv_activity_title, tv_activity_subtitle;
        private ImageView iv_activity_background;


        public MyViewHolder(View itemView ){
            super(itemView);
            tv_activity_title = (TextView) itemView.findViewById(R.id.tv_activity_title);
            tv_activity_subtitle = (TextView) itemView.findViewById(R.id.tv_activity_subtitle);
            iv_activity_background = (ImageView) itemView.findViewById(R.id.iv_activity_background);
        }
    }
}
