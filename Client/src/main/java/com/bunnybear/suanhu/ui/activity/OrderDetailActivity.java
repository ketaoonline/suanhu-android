package com.bunnybear.suanhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Coupon;
import com.bunnybear.suanhu.bean.PayBean;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.VIP;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.OrderDetailAdapter;
import com.bunnybear.suanhu.util.ToastUtil;
import com.bunnybear.suanhu.util.pay.AliPayResult;
import com.bunnybear.suanhu.util.pay.AliPayUtil;
import com.bunnybear.suanhu.util.pay.WXPayUtil;
import com.bunnybear.suanhu.view.PaySuccessDialog;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderDetailActivity extends AppActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_coupon_status)
    TextView tvCouponStatus;
    @BindView(R.id.tv_coupon_des)
    TextView tvCouponDes;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_wxpay)
    ImageView ivWxpay;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    public static void open(AppActivity activity,String orderId) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId",orderId);
        activity.startActivity(bundle, OrderDetailActivity.class);
    }

    List<SClass> list = new ArrayList<>();
    OrderDetailAdapter adapter;
    boolean isAliPay = false;
    String orderId = "";
    Coupon coupon;
    @Override
    protected int initLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected String setTitleStr() {
        return "订单结算";
    }

    @Override
    protected void init() {
        orderId = getIntent().getStringExtra("orderId");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(list);
        recyclerView.setAdapter(adapter);

        getData();

    }


    @OnClick({R.id.rl_coupon, R.id.tv_submit_order,R.id.rl_wx, R.id.rl_alipay})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId()))return;
        switch (view.getId()) {
            case R.id.rl_coupon:
                CouponActivity.openForResult(this,Integer.valueOf(orderId));
                break;
            case R.id.tv_submit_order:
                payMoney();
                break;
            case R.id.rl_wx:
                updatePayType(false);
                break;
            case R.id.rl_alipay:
                updatePayType(true);
                break;
        }
    }


    private void updatePayType(boolean mIsAliPay) {
        isAliPay = mIsAliPay ? true : false;
        ivAlipay.setImageResource(mIsAliPay ? R.mipmap.green_checked : R.mipmap.green_uncheck);
        ivWxpay.setImageResource(mIsAliPay ? R.mipmap.green_uncheck : R.mipmap.green_checked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    if (data != null){
                        coupon = (Coupon) data.getSerializableExtra("coupon");
                        tvCouponStatus.setVisibility(View.VISIBLE);
                        tvCouponDes.setText("已优惠"+coupon.getCoupon_money()+"元");
                        calcPrice(coupon.getCoupon_money());
                    }else {
                        coupon = null;
                        tvCouponStatus.setVisibility(View.GONE);
                        tvCouponDes.setText("");
                        calcPrice(0);
                    }
                    break;
            }
        }
    }

    private void calcPrice(double price) {
        double totalMoney = 0;
        for (SClass sClass : list) {
            totalMoney += sClass.getPrice();
        }
        tvTotal.setText("小计：¥ " + totalMoney);
        double realPrice = totalMoney - price;
        tvPrice.setText("¥ " + realPrice);
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .orderInfo(orderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SClass>>() {
                    @Override
                    public void success(List<SClass> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        calcPrice(0);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void payMoney(){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",orderId);
        map.put("pay_type",isAliPay ? 1 +"": 0+"");
        if (coupon != null){
            map.put("coupon_id",coupon.getId()+"");
        }
        Http.http.createApi(MineAPI.class)
                .payMoney(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<PayBean>() {
                    @Override
                    public void success(PayBean result) {
                        if (isAliPay){
                            new AliPayUtil(OrderDetailActivity.this,handler,result.getAlipay_info());
                        }else {
                            WXPayUtil.newInstance(OrderDetailActivity.this).pay(OrderDetailActivity.this,result.getWxpay_info(),"before");
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
                    showDialog();
                    break;
                case "6001":
                    ToastUtil.show(OrderDetailActivity.this,"已取消支付");
                    break;
                default:
                    ToastUtil.show(OrderDetailActivity.this,payResult.getResultStatus()+":"+payResult.getMemo());
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
                showDialog();
                break;
        }
    }

    private void showDialog(){
        PaySuccessDialog dialog = new PaySuccessDialog.Builder(this,"购买成功").create();
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                BusFactory.getBus().post(new IEvent("Class", null));
            }
        },3000);
    }

}
