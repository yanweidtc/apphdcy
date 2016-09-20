package com.hdcy.app.fragment.fourth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.fourth.childpagers.PersonalActivityFragment;
import com.hdcy.app.fragment.fourth.childpagers.PersonalGiftFragment;
import com.hdcy.app.fragment.fourth.childpagers.PersonalInfoFragment;
import com.hdcy.app.model.UserBaseInfo;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by WeiYanGeorge on 2016-09-08.
 */

public class FourthFragment extends BaseLazyMainFragment{

    private UserBaseInfo userBaseInfo;

    private ImageView iv_mine_top_bg;
    private ImageView iv_mine_top_avatar;

    private TextView tv_mine_level;
    private TextView tv_mine_credits;

    private LinearLayout ll_mine_person_info;
    private LinearLayout ll_mine_person_activity;
    private LinearLayout ll_mine_person_gift;

    int width;
    int bg_img_height;

    int avatarWidth;
    int avatarHeight;

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
        width = SizeUtils.getScreenWidth();
        bg_img_height = SizeUtils.dpToPx(232);
        avatarHeight = SizeUtils.dpToPx(100);
        avatarWidth = SizeUtils.dpToPx(100);
        setListener();
        return view;
    }

    private void setListener(){
        ll_mine_person_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userinfo",userBaseInfo);
                EventBus.getDefault().post(new StartBrotherEvent(PersonalInfoFragment.newInstance(bundle,userBaseInfo)));

            }
        });

        ll_mine_person_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(PersonalActivityFragment.newInstance()));
            }
        });

        ll_mine_person_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(PersonalGiftFragment.newInstance()));
            }
        });
    }

    private void initView(View view){
        iv_mine_top_bg = (ImageView) view.findViewById(R.id.iv_mine_top_bg);
        iv_mine_top_avatar = (ImageView) view.findViewById(R.id.iv_mine_top_avatar);
        tv_mine_level = (TextView) view.findViewById(R.id.tv_mine_top_level);
        tv_mine_credits = (TextView) view.findViewById(R.id.tv_mine_top_credits);
        ll_mine_person_info =(LinearLayout) view.findViewById(R.id.ll_mine_person_info);
        ll_mine_person_activity =(LinearLayout) view.findViewById(R.id.ll_mine_person_activity);
        ll_mine_person_gift = (LinearLayout) view.findViewById(R.id.ll_mine_person_gift);

    }

    private void setData(){
        tv_mine_level.setText("lv"+userBaseInfo.getLevel());
        tv_mine_credits.setText(userBaseInfo.getPoint()+"");

        Picasso.with(getContext()).load(userBaseInfo.getHeadimgurl())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(width,bg_img_height)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(iv_mine_top_bg);
        Picasso.with(getContext()).load(userBaseInfo.getHeadimgurl())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(avatarWidth,avatarHeight)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(iv_mine_top_avatar);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        initData();
    }

    private void initData(){
        GetUserCurrentInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void GetUserCurrentInfo(){
        NetHelper.getInstance().GetCurrentUserInfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                userBaseInfo = responseInfo.getUserBaseInfo();
                Log.e("userBaseInfo",userBaseInfo.getNickname()+"");
                setData();
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }
}
