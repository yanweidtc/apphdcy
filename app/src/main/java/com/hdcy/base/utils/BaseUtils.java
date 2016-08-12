package com.hdcy.base.utils;

import android.text.TextUtils;

import com.hdcy.base.BaseData;

/**
 * Created by WeiYanGeorge on 2016-08-10.
 */

public class BaseUtils implements BaseData {

    private static long lastClickTime = 0;

    /**
     * String是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(CharSequence str) {
        return TextUtils.isEmpty(str) || "null".equals(str);
    }

    /**
     * 是否快速点击两次
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 300;
    }
}
