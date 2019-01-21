package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.SClass;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class OrderClassAdapter extends BaseQuickAdapter<SClass,BaseViewHolder> {

    public OrderClassAdapter(@Nullable List<SClass> data) {
        super(R.layout.item_order_class, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SClass item) {
        TextView tvOldPrice = helper.getView(R.id.tv_old_price);
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvOldPrice.setText("¥"+item.getOld_price());
        helper.setText(R.id.tv_title,item.getCoursename());
        helper.setText(R.id.tv_price,"¥"+item.getPrice());
        GlideUtil.load(item.getHead_banner(),helper.getView(R.id.iv));
    }

}
