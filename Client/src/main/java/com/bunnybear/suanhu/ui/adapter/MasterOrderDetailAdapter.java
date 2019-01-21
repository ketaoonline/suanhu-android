package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.SClass;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class MasterOrderDetailAdapter extends BaseQuickAdapter<Master,BaseViewHolder>{
    public MasterOrderDetailAdapter(@Nullable List<Master> data) {
        super(R.layout.item_master_order_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Master item) {
        helper.setText(R.id.tv_title, item.getMaster_name());
        helper.setText(R.id.tv_price, "¥ " + item.getPrice());
        GlideUtil.load(item.getImag(), (ImageView) helper.getView(R.id.iv));
    }
}
