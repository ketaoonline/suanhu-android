package com.bunnybear.suanhu.master.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.base.ConstData;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.utils.LogUtil;

public class WelcomeActivity extends AppActivity {
    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {
        hideBaseTitleRelative();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = Hawk.get(ConstData.TOKEN);
                if (!TextUtils.isEmpty(token)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goMain();
                        }
                    },1000);
                }else{
                    LoginActivity.open(WelcomeActivity.this);
                    finish();
                }
            }
        },2000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    private void goMain(){
        LogUtil.i(System.currentTimeMillis()+"");
        MainActivity.open(WelcomeActivity.this);
        finish();
    }

}
