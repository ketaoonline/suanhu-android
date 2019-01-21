package com.bunnybear.suanhu.master.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.AuthAPI;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BindBankCardActivity extends AppActivity {

    @BindView(R.id.et_bank_card_num)
    ClearEditText etBankCardNum;
    @BindView(R.id.et_address)
    ClearEditText etAddress;
    @BindView(R.id.et_people)
    ClearEditText etPeople;

    public static void open(AppActivity activity, String bankCardNum) {
        Bundle bundle = new Bundle();
        bundle.putString("bankCardNum", bankCardNum);
        activity.startActivity(bundle, BindBankCardActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_bind_bank_card;
    }

    @Override
    protected String setTitleStr() {
        return "绑定银行卡";
    }

    @Override
    protected void init() {
        String bankCardNum = getIntent().getStringExtra("bankCardNum");
        etBankCardNum.setText(TextUtils.isEmpty(bankCardNum) ? "" : bankCardNum);
    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                bind();
                break;
        }
    }

    private void bind() {
        String bankCardNum = etBankCardNum.getText().toString();
        String address = etAddress.getText().toString();
        String people = etPeople.getText().toString();
        if (TextUtils.isEmpty(bankCardNum)) {
            showMessage("银行卡号不能为空");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            showMessage("开户行不能为空");
            return;
        }
        if (TextUtils.isEmpty(people)) {
            showMessage("收款人不能为空");
            return;
        }
        Http.http.createApi(MineAPI.class)
                .bindBankCard(bankCardNum, address, people)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("绑定成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

}
