package com.hdcy.app.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.RelativeTimeUtils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-09-04.
 */

public class ActivityCommentListAdapter extends BaseAdapter {

    private List<CommentsContent> data = new ArrayList<>();

    private LayoutInflater mInflater;
    private Context context;

    public ActivityCommentListAdapter(Context context, List<CommentsContent> data){
        super();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data.size() >3 ){
            return 3;
        }else {
           return data.size();
        }
    }

    @Override
    public CommentsContent getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_comments,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setTag(position);
        setView(position, holder);
        return convertView;
    }

    private void setView(final int position, final ViewHolder holder){
        CommentsContent item = getItem(position);

        holder.tv_name.setText(item.getCreaterName()+"");
        Picasso.with(context).load((item.getCreaterHeadimgurl()))
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(50,50)
                .into(holder.iv_avatar);
        Date time = item.getCreatedTime();
        String nowdate =RelativeTimeUtils.format(time);

        holder.tv_time.setText(nowdate);
        holder.tv_comment_content.setText(item.getContent());
        holder.tv_praise_count.setVisibility(View.GONE);
        holder.iv_praise.setVisibility(View.GONE);
        holder.lv_replys.setVisibility(View.GONE);


    }

    public class ViewHolder {
        private ImageView iv_avatar;
        private ImageView iv_praise;
        private TextView tv_name, tv_praise_count, tv_time, tv_comment_content;
        private ListView lv_replys;
        private LinearLayout ly_sub_replys;
        private TextView tv_more;

        private Object tag;

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public ViewHolder(View itemView) {

            ButterKnife.bind(this, itemView);


            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            iv_praise = (ImageView) itemView.findViewById(R.id.iv_praise);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_praise_count = (TextView) itemView.findViewById(R.id.tv_praise_count);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            ly_sub_replys = (LinearLayout) itemView.findViewById(R.id.ly_sub_reply);
            lv_replys = (ListView) itemView.findViewById(R.id.lv_replys);
            tv_more = (TextView) itemView.findViewById(R.id.tv_more);

            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);

        }
    }

}
