package com.hdcy.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hdcy.app.R;
import com.hdcy.base.BaseData;
import com.hdcy.base.utils.SizeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imageloader.core.ImageLoader;
import me.nereo.multi_image_selector.bean.Image;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by WeiYanGeorge on 2016-09-19.
 */

public class BootActivity extends AppCompatActivity implements BaseData {

    @BindView(R.id.img_boot)
    ImageView img_boot;
    private  final static int WAIT_TIME = 30; //启动页加载完成等待时间

    private int screenWidth, screenHeight;


    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){

        }
    };

    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        ButterKnife.bind(this);
        //initData();
        doFinish();
    }

    private void initView(){
    }

    public void initData(){
        screenWidth = SizeUtils.getScreenWidth();
        screenHeight = SizeUtils.getScreenHeight();
        imageLoader.displayImage("drawable://"+R.mipmap.bg_boot, img_boot);
    }
    private void doFinish() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        }, WAIT_TIME);
    }
}
