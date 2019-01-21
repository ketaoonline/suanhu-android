package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Master;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class HRecommendMasterListAdapter extends BaseQuickAdapter<Master,BaseViewHolder> {
    public HRecommendMasterListAdapter(@Nullable List<Master> data) {
        super(R.layout.item_h_masters_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Master item) {
        GlideUtil.load(item.getImag(),helper.getView(R.id.iv));
        helper.setText(R.id.tv_name,item.getMaster_name());
    }
}
