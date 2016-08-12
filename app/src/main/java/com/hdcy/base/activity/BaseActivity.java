package com.hdcy.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hdcy.base.BaseData;
import com.hdcy.base.utils.LocalActivityMgr;
import com.hdcy.base.utils.SystemBarTintManager;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.hdcy.app.R;

import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends FragmentActivity implements BaseData {

    private LinearLayout layout_content, layout_custom_View;
    private View layout_bg_tran, layout_loading_error;
    private FrameLayout layout_title;

    private TextView tv_title, left_1, right_1, right_2, tv_loading_error;
    private ImageView img_loading_error;
    private View img_right_1_point;

    private final static long LOADING_WAITING_TIME = 2000;// 网络加载对话框出现等待时间
    private boolean isNeedShowProgress;// 是否需要显示加载对话框
    private Timer timer;
    private Toast mToast;
    private Dialog mDialog;
    private Dialog pwdDialog;
    private PopupWindow pop;

    private boolean isSetKeyBoard = true;// 是否设置软键盘高度

    private SystemBarTintManager mTintManager;// 系统通知栏管理器

    // Dialog相关
    private LinearLayout dialog_layout_content;
    private TextView dialog_tv_title;
    private TextView dialog_tv_content;
    private TextView dialog_tv_content_line;
    private TextView dialog_tv_line_left_center;
    private TextView dialog_tv_line_right_center;
    private Button dialog_btn_left;
    private Button dialog_btn_center;
    private Button dialog_btn_right;
    private int dialog_left_text_color = R.color.main_enable;// 对话框左按钮字体颜色
    private int dialog_center_text_color = R.color.main_enable;// 对话框中按钮字体颜色
    private int dialog_right_text_color = R.color.main_enable;// 对话框右按钮字体颜色
    private int dialog_left_bg = R.color.main_enable;// 对话框左按钮背景
    private int dialog_center_bg = R.color.main_enable;// 对话框中按钮背景
    private int dialog_right_bg = R.color.main_enable;// 对话框右按钮背景

    // 密码Dialog
    private TextView tv_pwd, tv_title_pwd, tv_content_pwd;
    private View layout_close_pwd, layout_content_pwd;

    private Random ran;
    private PopupWindow popKeyBoard;
    private List<Integer> numList;

    private String pageName;// 当前页面名称
    public BaseActivity activity;// 当前页面对象

    private boolean isNeedShowUploadTips = true;// 是否需要显示上传提示

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_base);
        activity = this;
        LocalActivityMgr.getInstance().pushActivity(activity);
        initView();
        setClick();
        pageName = getTitle().toString();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        // 退出Activity，然后通过IDE强制触发一次GC操作，有打印，则无内存泄漏，无打印则肯定有内存泄漏
        LogUtil.d("finalize：" + pageName + " has been recycled!");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }


    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }




    public void setContentView(int view) {
        View bodyView = View.inflate(activity, view, null);
        setContentView(bodyView);
    }

    public void setContentView(View bodyView) {
        if (layout_content != null) {
            layout_content.removeAllViews();
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout_content.addView(bodyView, layoutParams);
    }

    private void initView() {
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        layout_custom_View = (LinearLayout) findViewById(R.id.layout_custom_View);
        layout_bg_tran = findViewById(R.id.layout_bg_tran);
        layout_loading_error = findViewById(R.id.layout_loading_error);
        layout_title = (FrameLayout) findViewById(R.id.layout_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        left_1 = (TextView) findViewById(R.id.left_1);
        right_1 = (TextView) findViewById(R.id.right_1);
        right_2 = (TextView) findViewById(R.id.right_2);
        img_loading_error = (ImageView) findViewById(R.id.img_loading_error);
        tv_loading_error = (TextView) findViewById(R.id.tv_loading_error);

        Drawable leftDrawable = getResDrawable(R.mipmap.icon_back);

        left_1.setCompoundDrawables(leftDrawable, null, null, null);

    }




    private void setClick() {
        left_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
        // win.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 是否启动软键盘顶高View（需要在onCreat前调用）
     *
     * @param isSetKeyBoard
     */
    public void setKeyboard(boolean isSetKeyBoard) {
        this.isSetKeyBoard = isSetKeyBoard;
    }

    /**
     * 设置标题栏文字
     *
     * @param title
     */
    public void setTitleText(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置标题栏背景
     *
     * @param resourceId
     */
    public void setTitleBarBackground(int resourceId) {
        layout_title.setBackgroundResource(resourceId);
    }

    /**
     * 设置通知栏背景
     *
     * @param resourceId
     */
    public void setStatusBarBackground(int resourceId) {
        if (isOverKitKat) {
            mTintManager.setStatusBarTintResource(resourceId);
        }
    }

    /**
     * 设置TitleBar和ContentView重叠(TitleBar盖在ContentView之上)
     */
    public void setTitleBarContentOverlap() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout_content.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);

        params = (RelativeLayout.LayoutParams) layout_bg_tran.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);

        params = (RelativeLayout.LayoutParams) layout_loading_error.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);
    }

    /**
     * 获取内容View
     *
     * @return
     */
    public LinearLayout getContentView() {
        return layout_content;
    }

    /**
     * 获取左一View
     *
     * @return
     */
    public TextView getLeft1() {
        return left_1;
    }

    /**
     * 获取右一View
     *
     * @return
     */
    public TextView getRight1() {
        return right_1;
    }

    /**
     * 获取右一View小红点
     *
     * @return
     */
    public View getRight1Point() {
        return img_right_1_point;
    }

    /**
     * 获取右二View
     *
     * @return
     */
    public TextView getRight2() {
        return right_2;
    }

    /**
     * 设置左一是否显示
     *
     * @param isVisibility
     */
    public void setLeft1Visibility(boolean isVisibility) {
        left_1.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右一是否显示
     *
     * @param isVisibility
     */
    public void setRight1Visibility(boolean isVisibility) {
        right_1.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右一小红点是否显示
     *
     * @param isVisibility
     */
    public void setRight1PointVisibility(boolean isVisibility) {
        img_right_1_point.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右二是否显示
     *
     * @param isVisibility
     */
    public void setRight2Visibility(boolean isVisibility) {
        right_2.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题栏是否显示
     *
     * @param isVisibility
     */
    public void setTitleBarVisibility(boolean isVisibility) {
        layout_title.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置是否显示半透明背景
     *
     * @param isVisibility
     */
    public void setBgTranVisibility(boolean isVisibility) {
        layout_bg_tran.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 添加标题栏自定义View
     *
     * @param view
     */
    public void addTitleCustomView(View view) {
        layout_custom_View.addView(view);
    }

    /**
     * 添加标题栏自定义View
     *
     * @param view
     * @param params
     */
    public void addTitleCustomView(View view, ViewGroup.LayoutParams params) {
        layout_custom_View.addView(view, params);
    }

    /**
     * 设置标题栏自定义View是否显示
     *
     * @param isVisibility
     */
    public void setTitleCustomViewVisibility(boolean isVisibility) {
        layout_custom_View.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取标题栏自定义View
     *
     * @return
     */
    public LinearLayout getTitleCustomView() {
        return layout_custom_View;
    }

    /**
     * 隐藏网络错误
     */
    public void dismissNetError() {
        layout_loading_error.setVisibility(View.GONE);
    }


    /**
     * 隐藏空数据
     */
    public void dismissNullData() {
        layout_loading_error.setVisibility(View.GONE);
    }



    /**
     * 显示软键盘
     */
    public void showKeyboard() {
        showKeyboard(layout_content);
    }

    /**
     * 显示软键盘
     *
     * @param view
     */
    public void showKeyboard(final View view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 消失软键盘
     */
    public void dismissKeyboard() {
        dismissKeyboard(layout_content);
    }

    /**
     * 消失软键盘
     *
     * @param view
     */
    public void dismissKeyboard(final View view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
            }
        }, 20);
    }

    /**
     * 获取Toast
     *
     * @param context
     * @return
     */
    @SuppressLint("ShowToast")
    private Toast getToast(Context context) {
        if (mToast == null) {
            synchronized (activity) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }

            }
        }
        return mToast;
    }

    /**
     * 显示Toast_Short
     *
     * @param message
     */
    public void showToastShort(String message) {
        mToast = getToast(getApplicationContext());
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 显示Toast_Short
     *
     * @param messageId
     */
    public void showToastShort(int messageId) {
        mToast = getToast(getApplicationContext());
        mToast.setText(messageId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 显示Toast_Long
     *
     * @param message
     */
    public void showToastLong(String message) {
        mToast = getToast(getApplicationContext());
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 显示Toast_Long
     *
     * @param messageId
     */
    public void showToastLong(int messageId) {
        mToast = getToast(getApplicationContext());
        mToast.setText(messageId);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 取消Toast
     */
    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }



    /**
     * 获取颜色
     *
     * @param resId
     * @return
     */
    public int getResColor(int resId) {
        return ContextCompat.getColor(activity, resId);
    }

    /**
     * 获取Drawable
     *
     * @param resId
     * @return
     */
    public Drawable getResDrawable(int resId) {
        return ContextCompat.getDrawable(activity, resId);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Base Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}