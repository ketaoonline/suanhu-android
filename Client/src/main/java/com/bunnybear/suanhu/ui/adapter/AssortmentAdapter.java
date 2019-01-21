package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.QuestionType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssortmentAdapter extends BaseQuickAdapter<QuestionType, BaseViewHolder> {
    boolean isFirstChooseMaster;

    public AssortmentAdapter(@Nullable List<QuestionType> data, boolean isFirstChooseMaster) {
        super(R.layout.item_assortment, data);
        this.isFirstChooseMaster = isFirstChooseMaster;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionType item) {
        GlideUtil.load(item.getImage(), helper.getView(R.id.iv));
        helper.setText(R.id.tv, item.getName());
        helper.setText(R.id.tv_price, "¥" + (double) item.getPrice());
        helper.setVisible(R.id.tv_price, isFirstChooseMaster ? true : false);
    }
}
