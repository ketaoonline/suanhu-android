package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.VIP;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class VIPAdapter extends BaseQuickAdapter<VIP, BaseViewHolder> {

    public int checkPosition = 0;

    public VIPAdapter(@Nullable List<VIP> data) {
        super(R.layout.item_vip, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VIP item) {
        helper.setBackgroundRes(R.id.rl, checkPosition == helper.getLayoutPosition() ? R.mipmap.bg_checked : R.mipmap.bg_uncheck);
        helper.setVisible(R.id.tv, !TextUtils.isEmpty(item.getBest_order()) ? true : false);
        helper.setText(R.id.tv_type, item.getName());
        helper.setText(R.id.tv_des, item.getIntroduce());
        helper.setText(R.id.tv_price, "¥ " + item.getPrice());
    }


    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
