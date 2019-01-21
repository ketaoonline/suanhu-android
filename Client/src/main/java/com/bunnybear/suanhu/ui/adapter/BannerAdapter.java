package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class BannerAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public BannerAdapter(@Nullable List<String> data) {
        super(R.layout.item_banner_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtil.load(item,helper.getView(R.id.image));
        helper.addOnClickListener(R.id.image);
    }
}
