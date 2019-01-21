package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.CalcOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class CalcOrderAdapter extends BaseQuickAdapter<CalcOrder,BaseViewHolder> {
    public CalcOrderAdapter(@Nullable List<CalcOrder> data) {
        super(R.layout.item_calc_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CalcOrder item) {
        helper.setText(R.id.tv_date,DateUtil.getMillon(item.getTime()*1000,DateUtil.FORMAT_YMDHMS));
        helper.setText(R.id.tv_order_num,"订单号："+item.getOrder_id());
        helper.setText(R.id.tv_order_type,item.getOrder_type());
        helper.setText(R.id.tv_name,item.getMaster_name());
        helper.setText(R.id.tv_price,"¥"+item.getPay_money());
        helper.setText(R.id.tv_pay_money,"实付：¥"+item.getMoney());
        GlideUtil.load(item.getImag(),helper.getView(R.id.iv));

        LinearLayout llWaitPay = helper.getView(R.id.ll_wait_pay);
        LinearLayout llSuccess = helper.getView(R.id.ll_success);
        TextView tvPayMoney = helper.getView(R.id.tv_pay_money);
        TextView tvStatus = helper.getView(R.id.tv_status);
        switch (item.getIs_pay()){
            case 0:
                llWaitPay.setVisibility(View.VISIBLE);
                llSuccess.setVisibility(View.GONE);
                tvPayMoney.setVisibility(View.GONE);
                tvStatus.setText("待支付");
                break;
            case 1:
                tvStatus.setText("交易成功");
                llWaitPay.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                tvPayMoney.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvStatus.setText("申请退款中");
                llWaitPay.setVisibility(View.GONE);
                llSuccess.setVisibility(View.GONE);
                tvPayMoney.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvStatus.setText("退款成功");
                llWaitPay.setVisibility(View.GONE);
                llSuccess.setVisibility(View.GONE);
                tvPayMoney.setVisibility(View.GONE);
                break;
            case 4:
                tvStatus.setText("拒绝退款");
                llWaitPay.setVisibility(View.GONE);
                llSuccess.setVisibility(View.GONE);
                tvPayMoney.setVisibility(View.GONE);
                break;
        }
        helper.addOnClickListener(R.id.tv_cancel);
        helper.addOnClickListener(R.id.tv_to_pay);
        helper.addOnClickListener(R.id.tv_apply_refund);

    }
}
