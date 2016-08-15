package com.hdcy.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WeiYanGeorge on 2016-08-15.
 */

public interface OnItemClickListener {
    void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
}
