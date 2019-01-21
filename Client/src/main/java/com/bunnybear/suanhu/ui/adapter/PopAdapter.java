package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PopAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public PopAdapter(@Nullable List<String> data) {
        super(R.layout.item_pop_text, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv,item);
    }
}
