package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class SmallImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public int checkPosition = 0;

    public SmallImageAdapter(@Nullable List<String> data) {
        super(R.layout.item_small_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv = helper.getView(R.id.iv_small);
        ImageView ivBig = helper.getView(R.id.iv_big);

        if (checkPosition == helper.getLayoutPosition()) {
            ivBig.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
            GlideUtil.load(item, ivBig);
        }else {
            ivBig.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            GlideUtil.load(item, iv);
        }
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
