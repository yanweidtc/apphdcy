package com.hdcy.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.GiftContent;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.hdcy.base.utils.SizeUtils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-09-19.
 */

public class GiftFragmentAdapter extends BaseAdapter {

    private List<GiftContent> data = new ArrayList<>();
    private Context context;

    private int width;
    private int imgheight;

    public GiftFragmentAdapter(Context context, List<GiftContent> data){
        super();
        this.context = context;
        this.data = data;
        width = SizeUtils.getScreenWidth();
        imgheight = SizeUtils.dpToPx(200);
    }

    @Override
    public int getCount() {
        return data == null ? 0 :data.size();
    }

    @Override
    public GiftContent getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.item_gift_list, null);
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
        GiftContent item = data.get(position);
        if(!BaseUtils.isEmptyString(item.getImages().get(0))) {
            String cover = item.getImages().get(0) + "";
            Picasso.with(context).load(cover)
                    .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                    .resize(width, imgheight)
                    .tag(holder.getTag())
                    .config(Bitmap.Config.RGB_565)
                    .into(holder.iv_gift_bg);
        }
        holder.tv_gift_point.setText(item.getPoint()+"");
        holder.tv_gift_stock.setText(item.getStock()+"");
        holder.tv_gift_name.setText(item.getName()+"");
    }


    public class  ViewHolder {
        private Object tag;

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag){
            this.tag = tag;
        }

        private ImageView iv_gift_bg;
        private TextView tv_gift_point, tv_gift_stock,tv_gift_name;

        ViewHolder(View view){
            ButterKnife.bind(this,view);
            iv_gift_bg = (ImageView) view.findViewById(R.id.iv_gift_background);
            tv_gift_point = (TextView) view.findViewById(R.id.tv_gift_point);
            tv_gift_stock = (TextView) view.findViewById(R.id.tv_gift_stock);
            tv_gift_name = (TextView) view.findViewById(R.id.tv_gift_name);
        }

    }
}
