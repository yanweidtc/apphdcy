package com.hdcy.app.basefragment;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hdcy.app.R;

/**
 * Created by WeiYanGeorge on 2016-08-14.
 */

public class BaseBackFragment extends BaseFragment {
    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        initToolbarMenu(toolbar);
    }
}
