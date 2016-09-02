package com.hdcy.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.Replys;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-08-30.
 */

public class ThirdPageFragmentAdapter extends BaseAdapter {

    private List<ActivityContent> data = new ArrayList<>();
    private Context context;

    //WindowManager wm = (WindowManager) context.get


    public ThirdPageFragmentAdapter(Context context, List<ActivityContent> data){
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ActivityContent getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_activity_list, null);
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
        ActivityContent item = data.get(position);
        holder.tv_activity_title.setText(item.getName()+"");
        holder.tv_activity_subtitle.setText(item.getAddress()+"");
        if(!BaseUtils.isEmptyString(item.getImage())) {
            String cover = item.getImage();
            Picasso.with(context).load(cover)
                    .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                    .resize(1150,550)
                    .centerCrop()
                    .into(holder.iv_activity_background);
        }
        if(item.getFinish()==false){
            holder.iv_activity_status.setVisibility(View.GONE);
        }
        holder.tv_activity_persons_count.setText(item.getHot()+"");
    }

    public class ViewHolder {
        private Object tag;


        private TextView tv_activity_title, tv_activity_subtitle,tv_activity_persons_count;
        private ImageView iv_activity_background,iv_activity_status;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            tv_activity_title = (TextView) view.findViewById(R.id.tv_activity_title);
            tv_activity_subtitle = (TextView) view.findViewById(R.id.tv_activity_subtitle);
            iv_activity_background = (ImageView) view.findViewById(R.id.iv_activity_background);
            iv_activity_status =(ImageView) view.findViewById(R.id.iv_activity_status);
            tv_activity_persons_count =(TextView) view.findViewById(R.id.tv_activity_persons_count);
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

