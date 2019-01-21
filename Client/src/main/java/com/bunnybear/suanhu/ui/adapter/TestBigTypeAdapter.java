package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.bean.TestBigType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class TestBigTypeAdapter extends BaseQuickAdapter<TestBigType, BaseViewHolder> {
    public TestBigTypeAdapter(@Nullable List<TestBigType> data) {
        super(R.layout.item_tag_class, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBigType item) {
        GlideUtil.load(item.getImage(), helper.getView(R.id.iv));
        helper.setText(R.id.tv_name, item.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size() > 4 ? 4 : mData.size();
    }
}
