package com.bunnybear.suanhu.ui.activity;

import android.text.TextUtils;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.AuthAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;
import com.xiaoxiong.library.view.CountDownTextView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FindBackPwdActivity extends AppActivity {

    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.tv_get_code)
    CountDownTextView tvGetCode;
    @BindView(R.id.et_code)
    ClearEditText etCode;
    @BindView(R.id.et_pwd)
    ClearEditText etPwd;

    public static void open(AppActivity activity) {
        activity.startActivity(null, FindBackPwdActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_find_back_pwd;
    }

    @Override
    protected String setTitleStr() {
        return "找回密码";
    }

    @Override
    protected void init() {
        tvGetCode.setNormalText("获取验证码")
                .setCountDownText("", "s")
                .setCloseKeepCountDown(false)
                .setCountDownClickable(false)
                .setShowFormatTime(false)
                .setOnCountDownStartListener(new CountDownTextView.OnCountDownStartListener() {
                    @Override
                    public void onStart() {
                    }
                })
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        tvGetCode.setText("获取验证码");
                        tvGetCode.setClickable(true);
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMsg(etPhone.getText().toString());
                    }
                });
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        confirm(etPhone.getText().toString(),etCode.getText().toString(),etPwd.getText().toString());
    }


    private void sendMsg(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            showMessage("手机号不能为空");
            return;
        }
        if (mobile.length() != 11) {
            showMessage("手机号不正确");
            return;
        }
        Http.http.createApi(AuthAPI.class)
                .sendMsg(mobile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        tvGetCode.startCountDown(60);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }


    private void confirm(String mobile, String code, String pwd) {
        if (TextUtils.isEmpty(mobile)) {
            showMessage("手机号不能为空");
            return;
        }
        if (mobile.length() != 11) {
            showMessage("手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showMessage("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showMessage("新密码不能为空");
            return;
        }
        Http.http.createApi(AuthAPI.class)
                .findBackPwd(mobile, pwd, code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("密码重置成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
