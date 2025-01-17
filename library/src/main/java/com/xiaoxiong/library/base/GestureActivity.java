package com.xiaoxiong.library.base;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * 支持手势的activity
 * Created by oracleen on 2016/6/14.
 */
public abstract class GestureActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 300;
    private int minVelocity = 0;

    protected abstract int initLayout();
    protected abstract void doFinish();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());


        ActivityManager.getInstance().addActivity(this);

        initGesture();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }

    private void initGesture() {
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            //向左手势

        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            //向右手势
            doFinish();
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
