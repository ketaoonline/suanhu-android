package com.bunnybear.suanhu.master.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.AuthAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.base.ConstData;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppActivity {

    @BindView(R.id.et_account)
    ClearEditText etAccount;
    @BindView(R.id.et_pwd)
    ClearEditText etPwd;

    public static void open(AppActivity activity) {
        activity.startActivity(null, LoginActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {
        hideBaseTitleRelative();
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_pwd, R.id.tv_register, R.id.iv_wx, R.id.iv_qq})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.btn_login:
                login(etAccount.getText().toString(), etPwd.getText().toString());
                break;
            case R.id.tv_forgot_pwd:
                FindBackPwdActivity.open(this);
                break;
            case R.id.tv_register:
                RegisterActivity.open(this);
                break;
            case R.id.iv_wx:
                break;
            case R.id.iv_qq:
                break;
        }
    }

    /**
     * 登录
     */
    private void login(final String account, String pwd) {
        if (TextUtils.isEmpty(account)) {
            showMessage("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showMessage("密码不能为空");
            return;
        }
        if (account.length() != 11) {
            showMessage("手机号不正确");
            return;
        }
        showProgressDialog("登录中...");
        Http.http.createApi(AuthAPI.class)
                .login(account,pwd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        JPushInterface.resumePush(getApplicationContext());
                        JPushInterface.setAlias(LoginActivity.this, account, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                Log.d("set_alias", "set alias result is" + i);
                            }
                        });
                        Hawk.put(ConstData.TOKEN,result);
                        MainActivity.open(LoginActivity.this);
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));

    }
}
