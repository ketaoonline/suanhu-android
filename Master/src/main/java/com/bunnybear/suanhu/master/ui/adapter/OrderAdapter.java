package com.bunnybear.suanhu.master.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.bean.Order;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    int type;

    public OrderAdapter(@Nullable List<Order> data, int type) {
        super(R.layout.item_order, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        GlideUtil.load(item.getAvatar(), (ImageView) helper.getView(R.id.iv));
        helper.setText(R.id.tv_start_time, "开始时间：" + DateUtil.getMillon(item.getStart_time() * 1000, DateUtil.FORMAT_YMDHMS));
        helper.setText(R.id.tv_type, type == 0 ? "类型：简测" : "类型：详测");
        helper.setText(R.id.tv_type_des, item.getName());
        helper.setText(R.id.tv_name, item.getUser_name());

        helper.setVisible(R.id.line, type == 0 ? false : true);
        helper.setVisible(R.id.tv_responder, type == 0 ? true : false);
        helper.addOnClickListener(R.id.tv_responder);
    }
}
