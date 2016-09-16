package com.hdcy.app.fragment.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseFragment;


/**
 * 视频简介
 */
public class Fragment4TabVedioBrief extends BaseFragment {
    private static final String Params_VedioStr = "Arg_params";

    private String mStr =null;

    private TextView mTvTitle;

    public static Fragment4TabVedioBrief newInstance(String str) {

        Bundle args = new Bundle();
        args.putString(Params_VedioStr, str);
        Fragment4TabVedioBrief fragment = new Fragment4TabVedioBrief();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStr = getArguments().getString(Params_VedioStr);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab_vedio_brief, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitle.setText(mStr);
    }

}
