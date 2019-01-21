package com.xiaoxiong.library.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

//import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by renlei on 2016/5/23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract boolean useEventBus();
//    protected ImmersionBar mImmersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        mImmersionBar = ImmersionBar.with(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mImmersionBar != null)
//            mImmersionBar.destroy();
    }

}
