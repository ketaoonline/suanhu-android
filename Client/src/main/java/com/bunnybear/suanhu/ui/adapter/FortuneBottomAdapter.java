package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.BottomFortune;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class FortuneBottomAdapter extends BaseQuickAdapter<BottomFortune,BaseViewHolder>{
    public FortuneBottomAdapter(@Nullable List<BottomFortune> data) {
        super(R.layout.item_fortune_bottom, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BottomFortune item) {

    }
}
