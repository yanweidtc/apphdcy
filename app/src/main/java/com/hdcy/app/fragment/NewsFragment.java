package com.hdcy.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.R;
import com.hdcy.base.activity.BaseActivity;

/**
 * Created by WeiYanGeorge on 2016-08-13.
 */

public class NewsFragment extends Fragment {

    private BaseActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        activity = (BaseActivity) getActivity();
        View view = View.inflate(activity, R.layout.fragment_news,null);
        return view;
    }
}
