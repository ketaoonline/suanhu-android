package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.bean.QuestionType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassAssortmentAdapter extends BaseQuickAdapter<ClassType, BaseViewHolder> {
    public ClassAssortmentAdapter(@Nullable List<ClassType> data) {
        super(R.layout.item_class_assortment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassType item) {
        GlideUtil.load(item.getImage(), (CircleImageView) helper.getView(R.id.iv));
        helper.setText(R.id.tv, item.getTag());
    }
}
