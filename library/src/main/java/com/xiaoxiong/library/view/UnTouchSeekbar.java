package com.xiaoxiong.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class UnTouchSeekbar extends android.support.v7.widget.AppCompatSeekBar {
    public UnTouchSeekbar(Context context) {
        super(context);
    }

    public UnTouchSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnTouchSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
