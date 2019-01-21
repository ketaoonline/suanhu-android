package com.bunnybear.suanhu.master.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.bean.GoodAt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class GoodAtAdapter extends BaseQuickAdapter<GoodAt,BaseViewHolder> {
    public GoodAtAdapter(@Nullable List<GoodAt> data) {
        super(R.layout.item_good_at, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodAt item) {
        ImageView iv = helper.getView(R.id.iv);
        TextView tv = helper.getView(R.id.tv);
        tv.setText(item.getContent());
        if (item.isChecked()){
            iv.setImageResource(R.mipmap.good_checked);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.parseColor("#A398E6"));
        }else {
            iv.setImageResource(R.mipmap.good_normal);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
    }
}
