package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.SClass;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class SearchClassAdapter extends BaseQuickAdapter<SClass,BaseViewHolder> {
    AppActivity activity;
    public SearchClassAdapter(AppActivity appActivity, @Nullable List<SClass> data) {
        super(R.layout.item_search_class, data);
        this.activity = appActivity;
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
