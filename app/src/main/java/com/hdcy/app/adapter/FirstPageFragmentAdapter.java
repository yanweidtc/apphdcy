package com.hdcy.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.Replys;
import com.hdcy.base.BaseInfo;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-08-30.
 */

public class FirstPageFragmentAdapter extends BaseAdapter {

    private Context context;
    private List<Content> data;

    public FirstPageFragmentAdapter(Context context, List<Content> data){
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Content getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setTag(position);
        setView(position, holder);
        return convertView;
    }

    private void setView(final int position, final ViewHolder holder) {
        Content item = data.get(position);
        holder.tvTitle.setText(item.getTitle()+"");
        holder.tv_info_update.setText(item.getCreatedTime().toLocaleString()+"");
        holder.tv_info_watched.setText(item.getReadCount()+"");
        String cover = item.getImage();
        Log.e("imageurl",cover);
        Picasso.with(context).load(cover)
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(240,240)
                .centerCrop()
                .into(holder.iv_info_cover);
        holder.tv_info_tag.setText(item.getTagInfos().get(0).getName()+"");
    }

    public class ViewHolder {
        private Object tag;


        private TextView tvTitle, tv_info_update,tv_info_watched, tv_info_tag;
        private ImageView iv_info_cover;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            tvTitle = (TextView) view.findViewById(R.id.tv_info_title);
            tv_info_update = (TextView)view.findViewById(R.id.tv_info_update);
            tv_info_watched = (TextView)view.findViewById(R.id.tv_info_watched);
            iv_info_cover = (ImageView) view.findViewById(R.id.iv_cover);
            tv_info_tag =(TextView) view.findViewById(R.id.tv_info_tag_name);
        }

        //@ViewInject(R.id.tv_content)

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

    }

}

