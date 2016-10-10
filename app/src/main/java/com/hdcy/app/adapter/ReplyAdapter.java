package com.hdcy.app.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.Replys;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-09-29.
 */

public class ReplyAdapter extends ReplysAdpterV2<Replys> {

    public OnItemsClickListeners onItemsClickListeners;

    public void setOnItemsClickListeners (OnItemsClickListeners onItemsClickListeners){
        this.onItemsClickListeners =onItemsClickListeners;
    }


    public ReplyAdapter(Context context, ListView mListView) {
        super(context,mListView);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_reply, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setView(position, holder);
        return convertView;
    }

    private void setView(final int position, final ViewHolder holder) {
        Replys temp = getItem(position);
        String fromUser = temp.getCreaterName()+"";
        String toUser = temp.getReplyToName()+"";
        String content =  temp.getContent();

        holder.tv_from.setText(fromUser);
        holder.tv_to.setText(toUser);
        holder.tv_content.setText(content);

        holder.fy_item_replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) holder.getTag();
                if (tag ==position){
                    if(onItemsClickListeners !=null){
                        onItemsClickListeners.onFrameLayout(position);
                    }
                }
            }
        });
    }

    public class ViewHolder {
        private Object tag;


        public TextView tv_content ,tv_from, tv_to;
        public LinearLayout fy_item_replys;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            fy_item_replys = (LinearLayout) view.findViewById(R.id.ry_item_reply);
            tv_content =(TextView) view.findViewById(R.id.tv_content);
            tv_from = (TextView) view.findViewById(R.id.tv_from);
            tv_to = (TextView) view.findViewById(R.id.tv_to);
        }

        //@ViewInject(R.id.tv_content)

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

    }

/*    public void addItemNum(int number){
        itemCount = number;
        notifyDataSetChanged();
    }

    public void addItem(Replys replys){
        data.add(replys);
        notifyDataSetChanged();
    }*/

    public interface OnItemsClickListeners{
        void onFrameLayout(int position);
    }

}
