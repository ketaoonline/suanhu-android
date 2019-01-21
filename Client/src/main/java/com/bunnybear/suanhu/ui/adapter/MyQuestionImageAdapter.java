package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class MyQuestionImageAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public MyQuestionImageAdapter(AppActivity activity,@Nullable List<String> data) {
        super(R.layout.item_my_question_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtil.load(item, helper.getView(R.id.iv));
    }

    @Override
    public int getItemCount() {
        return mData.size() > 3 ? 3 : mData.size();
    }
}
