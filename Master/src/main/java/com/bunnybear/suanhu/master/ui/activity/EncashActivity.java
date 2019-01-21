package com.bunnybear.suanhu.master.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.AuthAPI;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.bean.Income;
import com.bunnybear.suanhu.master.bean.IncomeResponse;
import com.bunnybear.suanhu.master.bean.MainBaseBean;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;
import com.xiaoxiong.library.view.CountDownTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EncashActivity extends AppActivity {

    @BindView(R.id.tv_bank_card_num)
    TextView tvBankCardNum;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_code)
    ClearEditText etCode;
    @BindView(R.id.tv_get_code)
    CountDownTextView tvGetCode;

    public static void open(AppActivity activity, String bankCardNum) {
        Bundle bundle = new Bundle();
        bundle.putString("bankCardNum", bankCardNum);
        activity.startActivity(bundle, EncashActivity.class);
    }


    @Override
    protected int initLayout() {
        return R.layout.activity_encash;
    }

    @Override
    protected String setTitleStr() {
        return "收入";
    }

    @Override
    protected void init() {
        tvBankCardNum.setText(getIntent().getStringExtra("bankCardNum"));
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
                        sendMsg();
                    }
                });

    }


    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                encash();
                break;
        }
    }

    private void encash(){
        String money = etMoney.getText().toString();
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(money)){
            showMessage("提现金额不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)){
            showMessage("验证码不能为空");
            return;
        }
        Http.http.createApi(MineAPI.class)
                .applyEncash(money,code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("申请成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void sendMsg() {
        Http.http.createApi(MineAPI.class)
                .getCode()
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

}
