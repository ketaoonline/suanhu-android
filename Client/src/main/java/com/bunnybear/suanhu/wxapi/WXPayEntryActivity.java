package com.bunnybear.suanhu.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bunnybear.suanhu.util.pay.WXPayUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;

/**
 * Created by xiaoxiong on 2016/12/8.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WXPayUtil.APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode){
                case 0:
                    Log.i(TAG, "onResp: success");
                    BusFactory.getBus().post(new IEvent("WX_SUCCESS_RESPONSE", null));
                    finish();
                    break;
                case -1:
                    Log.i(TAG, "onResp: failed");
                    Toast.makeText(this,"支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                    Log.i(TAG, "onResp: cancel");
                    Toast.makeText(this,"您已取消支付", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }




}
