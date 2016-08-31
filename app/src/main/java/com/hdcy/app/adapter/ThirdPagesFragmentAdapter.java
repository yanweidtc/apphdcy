package com.hdcy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.Content;

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

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public  ActivityContent getItem(int position) {
        return mItems.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView ){
            super(itemView);
        }
    }
}
