package com.hdcy.app.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.hdcy.app.R;

/**
 * Created by WeiYanGeorge on 2016-09-22.
 */

public class SFProgressDialog extends Dialog {

    private SFProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static SFProgressDialog createProgressDialog(Context context) {
        SFProgressDialog m_progressDialog = new SFProgressDialog(context, R.style.SF_pressDialogCustom);
        m_progressDialog.setContentView(R.layout.sf_view_custom_progress_dialog);
        m_progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return m_progressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        ImageView loadingImageView = (ImageView) this.findViewById(R.id.sf_iv_progress_dialog_loading);
//        if (loadingImageView != null) {
//            AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
//            animationDrawable.start();
//        }
    }

    public void setMessage(String msg) {
        TextView loadingTextView = (TextView) this.findViewById(R.id.sf_tv_progress_dialog_loading);
        if (loadingTextView != null) {
            loadingTextView.setText(msg);
        }
    }
}
