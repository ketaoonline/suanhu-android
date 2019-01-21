package com.bunnybear.suanhu.master.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class QuestionImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public QuestionImageAdapter(AppActivity activity, @Nullable List<String> data) {
        super(R.layout.item_question_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtil.load(item, (ImageView) helper.getView(R.id.iv));
    }

    @Override
    public int getItemCount() {
        return mData.size() > 3 ? 3 : mData.size();
    }
}
