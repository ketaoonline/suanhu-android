package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ClassOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;

import java.util.List;

public class ClassOrderAdapter extends BaseQuickAdapter<ClassOrder, BaseViewHolder> {
    public ClassOrderAdapter(@Nullable List<ClassOrder> data) {
        super(R.layout.item_class_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassOrder item) {
        RecyclerView rv = helper.getView(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        OrderClassAdapter adapter = new OrderClassAdapter(item.getInfo());
        rv.setAdapter(adapter);

        helper.setText(R.id.tv_order_num, "订单编号：" + item.getOrder_id());
        helper.setText(R.id.tv_date, DateUtil.getMillon(item.getTime() * 1000, DateUtil.FORMAT_YMDHMS));
        helper.setText(R.id.tv_price_des, item.getIs_pay() == 0 ? "需付款：" : "实付：");
        helper.setText(R.id.tv_price, "¥" + item.getMoney());

        helper.setImageResource(R.id.iv, item.getIs_pay() == 0 ? R.mipmap.class_order_wait_pay : R.mipmap.class_order_payed);
        helper.setText(R.id.tv_status, item.getIs_pay() == 0 ? "待支付" : "交易完成");
    }
}
