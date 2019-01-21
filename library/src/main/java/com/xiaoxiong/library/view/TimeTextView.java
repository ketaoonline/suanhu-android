package com.xiaoxiong.library.view;//package com.xiaoxiong.xwlibrary.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.widget.TextView;
//
//
///**
// * PackageName : com.ewhale.suibao.widgets
// * Author : Ziwen Lan
// * Date : 2018/2/9
// * Time : 11:23
// * Introduction : 倒计时TextView
// */
//
//public class TimeTextView extends TextView {
//    private CustomCountDownTimer mCustomCountDownTimer;
//    private OnCountdownEndListener mOnCountdownEndListener;
//    private OnTimeCallBackListener mOnTimeCallBackListener;
//
//
//    public TimeTextView(Context context) {
//        this(context, null);
//    }
//
//    public TimeTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        stop();
//    }
//
//
//    /**
//     * start countdown
//     *
//     * @param millisecond millisecond
//     */
//    public void start(long millisecond) {
//        if (millisecond <= 0) {
//            if (mOnTimeCallBackListener != null) {
//                mOnTimeCallBackListener.onTimeCallBack(this, "0", "0", "0");
//            }
//            return;
//        }
//
//
//        if (null != mCustomCountDownTimer) {
//            mCustomCountDownTimer.stop();
//            mCustomCountDownTimer = null;
//        }
//
//        int countDownInterval = 1000;
//
//        mCustomCountDownTimer = new CustomCountDownTimer(millisecond, countDownInterval) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                updateShow(millisUntilFinished);
//            }
//
//            @Override
//            public void onFinish() {
//                // callback
//                if (null != mOnCountdownEndListener) {
//                    mOnCountdownEndListener.onEnd(TimeTextView.this);
//                }
//            }
//        };
//        mCustomCountDownTimer.start();
//    }
//
//    /**
//     * stop countdown
//     */
//    public void stop() {
//        if (null != mCustomCountDownTimer) mCustomCountDownTimer.stop();
//    }
//
//    /**
//     * pause countdown
//     */
//    public void pause() {
//        if (null != mCustomCountDownTimer) mCustomCountDownTimer.pause();
//    }
//
//
//    public void updateShow(long ms) {
//
//        int day = (int) (ms / (1000 * 60 * 60 * 24));
//        int hour = (int) ((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
//        int minute = (int) ((ms % (1000 * 60 * 60)) / (1000 * 60));
//        int second = (int) ((ms % (1000 * 60)) / 1000);
////        int millisecond = (int) (ms % 1000);
//
////        LogUtil.simpleLog("小时=" + hour + ",分钟=" + minute + ",秒数=" + second);
//
//        hour = hour + day * 24;
//        String hourStr = hour >= 10 ? hour + "" : "0" + hour;
//        String minuteStr = minute >= 10 ? minute + "" : "0" + minute;
//        String secondStr = second >= 10 ? second + "" : "0" + second;
//        if (mOnTimeCallBackListener != null) {
//            mOnTimeCallBackListener.onTimeCallBack(this, hourStr, minuteStr, secondStr);
//        }
//    }
//
//
//    /**
//     * set countdown end callback listener
//     *
//     * @param onCountdownEndListener OnCountdownEndListener
//     */
//    public void setOnCountdownEndListener(OnCountdownEndListener onCountdownEndListener) {
//        mOnCountdownEndListener = onCountdownEndListener;
//    }
//
//
//    public interface OnCountdownEndListener {
//        void onEnd(TimeTextView cv);
//    }
//
//
//    public void setmOnTimeCallBackListener(OnTimeCallBackListener mOnTimeCallBackListener) {
//        this.mOnTimeCallBackListener = mOnTimeCallBackListener;
//    }
//
//    public interface OnTimeCallBackListener {
//        void onTimeCallBack(TimeTextView timeTextView, String hourStr, String minute, String second);
//    }
//
//}
