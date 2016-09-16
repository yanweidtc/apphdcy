package com.hdcy.app.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.hdcy.app.R;
import com.hdcy.app.model.Bean4VedioBanner;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by  chiwenheng
 * 网络图片加载
 */
public class NetworkImageHolderView implements Holder<Bean4VedioBanner> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, Bean4VedioBanner data) {
        // 先加载默认图片
        imageView.setImageResource(R.drawable.ic_default_adimage);

        ImageLoader.getInstance().displayImage(data.image,imageView);
    }
}