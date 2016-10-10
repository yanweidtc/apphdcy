package com.hdcy.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.model.Replys;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by WeiYanGeorge on 2016-09-28.
 */

public abstract class ReplysAdpterV2<E> extends BaseAdapter {
    public static final int DEFAULT_SHOW_COUNT = 2;

    protected Context mContext;
    protected ListView mListView;
    protected LayoutInflater inflater;
    protected LinearLayout headView;
    protected Button btn_loadmore;
    protected ArrayList<E> mShowObjects = new ArrayList<E>();
    protected List<E> mAllObjects;
    protected boolean shrink = true;

    @SuppressWarnings("unused")
    private ReplysAdpterV2() {
    }

    @SuppressLint("InflateParams")
    public ReplysAdpterV2( Context mContext, ListView mListView) {
        this.mContext = mContext;
        this.mListView = mListView;
        inflater = LayoutInflater.from(mContext);
        headView = (LinearLayout) inflater.inflate(R.layout.lv_footer_button, null);
        btn_loadmore = (Button) headView.findViewById(R.id.btn_loadmore);
        btn_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeShow();
            }
        });
        mListView.addFooterView(headView, null, false);

    }

    public void setAdapterData( List<E> mAllObjects ) {
        this.mAllObjects = mAllObjects;
        mShowObjects.clear();
        if( mAllObjects != null ) {
            if( mAllObjects.size() <= DEFAULT_SHOW_COUNT ) {
                headView.setVisibility(View.GONE);
                mShowObjects.addAll(mAllObjects);
            } else {
                headView.setVisibility(View.VISIBLE);
                for (int i = 0; i < DEFAULT_SHOW_COUNT; i++) {
                    mShowObjects.add(mAllObjects.get(i));
                }
            }
        }
        notifyDataSetChanged();
        setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public int getCount() {
        int showCount = 0;
        if( mShowObjects != null ) {
            showCount = mShowObjects.size();
        }
        return showCount;
    }

    @Override
    public E getItem(int position) {
        E object = null;
        if( mShowObjects != null ) {
            object = mShowObjects.get(position);
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void changeShow() {
        if( headView.getVisibility() == View.GONE ) {
            headView.setVisibility(View.VISIBLE);
        }
        mShowObjects.clear();
        if( shrink ) {
            shrink = false;
            mShowObjects.addAll(mAllObjects);
            btn_loadmore.setText("收起");
        } else {
            shrink = true;
            for (int i = 0; i < DEFAULT_SHOW_COUNT; i++) {
                mShowObjects.add(mAllObjects.get(i));
            }
            btn_loadmore.setText("更多");
        }
        notifyDataSetChanged();
        //setListViewHeightBasedOnChildren(mListView);
    }

    /**
     * 当ListView外层有ScrollView时，需要动态设置ListView高度
     * @param listView
     */
    protected void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
