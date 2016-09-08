package com.hdcy.app.fragment.fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;

/**
 * Created by WeiYanGeorge on 2016-09-08.
 */

public class FourthFragment extends BaseLazyMainFragment{
    private Toolbar mToolbar;

    private TextView title;

    public static FourthFragment newInstance(){
        Bundle args = new Bundle();
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth_pager,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("我的");
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }
}
