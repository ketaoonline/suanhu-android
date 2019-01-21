package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ClassType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class TagClassAdapter extends BaseQuickAdapter<ClassType,BaseViewHolder> {
    public TagClassAdapter(@Nullable List<ClassType> data) {
        super(R.layout.item_tag_class, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassType item) {
        if (helper.getLayoutPosition() == 3){
            helper.setImageResource(R.id.iv,R.mipmap.class_icon_4);
            helper.setText(R.id.tv_name,"更多课程");
        }else {
            GlideUtil.load(item.getImage(),helper.getView(R.id.iv));
            helper.setText(R.id.tv_name,item.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() > 4 ? 4 : mData.size();
    }
}
