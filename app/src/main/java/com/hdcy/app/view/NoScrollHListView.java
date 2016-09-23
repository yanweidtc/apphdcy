package com.hdcy.app.view;

import android.content.Context;
import android.util.AttributeSet;

import it.sephiroth.android.library.widget.HListView;

/**
 * banner
 */

public class NoScrollHListView extends HListView{
    public NoScrollHListView(Context context) {
        super(context);
    }

    public NoScrollHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollHListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(expandSpec,heightMeasureSpec );
    }
}
