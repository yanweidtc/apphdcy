package com.hdcy.app.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.Replys;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-08-24.
 */

public class ReplysAdapter extends BaseAdapter {
    private Context context;
    private List<Replys> data;

    public ReplysAdapter(Context context, List<Replys> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Replys getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_reply, null);
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

    private void setView(int position, ViewHolder holder) {
        Replys temp = getItem(position);
        String fromUser = temp.getCreaterName()+"";
        String toUser = temp.getReplyToName()+"";
        String content = fromUser +" 回复 " +toUser +": "+ temp.getContent();
        Log.e("fromUser",fromUser);
        holder.tv_content.setText(content);
    }

    public class ViewHolder {
        private Object tag;



        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            tv_content =(TextView) view.findViewById(R.id.tv_content);
        }

        //@ViewInject(R.id.tv_content)
        public TextView tv_content;

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

    }
}
