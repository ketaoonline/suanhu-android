package com.bunnybear.suanhu.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.bean.PayBean;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.VIP;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.net.HttpConstData;
import com.bunnybear.suanhu.ui.adapter.VIPAdapter;
import com.bunnybear.suanhu.util.ToastUtil;
import com.bunnybear.suanhu.util.pay.AliPayResult;
import com.bunnybear.suanhu.util.pay.AliPayUtil;
import com.bunnybear.suanhu.util.pay.WXPayUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VIPActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_wxpay)
    ImageView ivWxpay;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    public static void open(AppActivity activity) {
        activity.startActivity(null, VIPActivity.class);
    }

    VIPAdapter mAdapter;
    List<VIP> list = new ArrayList<>();
    boolean isAliPay = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_vip;
    }

    @Override
    protected String setTitleStr() {
        return "开通会员";
    }

    @Override
    protected void init() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new VIPAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        getData();

    }

    @OnClick({R.id.tv_des, R.id.rl_wx, R.id.rl_alipay, R.id.tv_agreement_service, R.id.tv_agreement_fee, R.id.tv_pay})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId()))return;
        switch (view.getId()) {
            case R.id.tv_des:
                WebViewActivity.open(this,"会员权益说明",HttpConstData.ROOT+"user/login/index2.html");
                break;
            case R.id.rl_wx:
                updatePayType(false);
                break;
            case R.id.rl_alipay:
                updatePayType(true);
                break;
            case R.id.tv_agreement_service:
                WebViewActivity.open(this,"算乎会员服务协议",HttpConstData.ROOT+"user/login/index3.html");
                break;
            case R.id.tv_agreement_fee:
//                WebViewActivity.open(this,"《算乎会员自动续费协议》","http://www.baidu.com");
                break;
            case R.id.tv_pay:
                openVip();
                break;
        }
    }

    private void updatePayType(boolean mIsAliPay) {
        isAliPay = mIsAliPay ? true : false;
        ivAlipay.setImageResource(mIsAliPay ? R.mipmap.green_checked : R.mipmap.green_uncheck);
        ivWxpay.setImageResource(mIsAliPay ? R.mipmap.green_uncheck : R.mipmap.green_checked);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.checkPosition == position)return;
        mAdapter.setCheckPosition(position);
        tvPrice.setText("¥ "+list.get(position).getPrice());
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .vips()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<VIP>>() {
                    @Override
                    public void success(List<VIP> result) {
                        list.clear();
                        list.addAll(result);
                        mAdapter.setNewData(list);
                        if (list.size() > 0){
                            tvPrice.setText("¥ "+list.get(0).getPrice());
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void openVip(){
        Http.http.createApi(MineAPI.class)
                .openVip(list.get(mAdapter.checkPosition).getId(),isAliPay ? 1 : 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<PayBean>() {
                    @Override
                    public void success(PayBean result) {
                        if (isAliPay){
                            String alipayInfo = result.getAlipay_info().replaceAll("amp;","");
                            new AliPayUtil(VIPActivity.this,handler,alipayInfo);
                        }else {
                            WXPayUtil.newInstance(VIPActivity.this).pay(VIPActivity.this,result.getWxpay_info(),"before");
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
            switch (payResult.getResultStatus()) {
                case "9000":
                    showMessage("开通成功");
                    finish();
                    break;
                case "6001":
                    ToastUtil.show(VIPActivity.this,"已取消支付");
                    break;
                default:
                    ToastUtil.show(VIPActivity.this,payResult.getResultStatus()+":"+payResult.getMemo());
                    break;
            }
        }
    };

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        switch (msgCode){
            case "WX_SUCCESS_RESPONSE":
                showMessage("开通成功");
                finish();
                break;
        }
    }

}
