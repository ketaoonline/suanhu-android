package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Audio;
import com.bunnybear.suanhu.bean.Lesson;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AudioAdapter extends BaseQuickAdapter<Lesson,BaseViewHolder>{

    public int checkPosition = 0;

    public AudioAdapter(@Nullable List<Lesson> data) {
        super(R.layout.item_audio, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Lesson item) {
        int position = helper.getLayoutPosition();
        helper.setBackgroundRes(R.id.rl,checkPosition == position ? R.drawable.shape_tag_checked : R.drawable.shape_tag_uncheck);
        helper.setTextColor(R.id.tv_name_front,checkPosition == position ? Color.parseColor("#A096DE") : Color.parseColor("#666666"));
        helper.setTextColor(R.id.tv_name_back,checkPosition == position ? Color.parseColor("#A096DE") : Color.parseColor("#666666"));

        helper.setVisible(R.id.tv_temp,helper.getLayoutPosition() < 2 ? true : false);

        helper.setText(R.id.tv_name_front,item.getSension());
        helper.setText(R.id.tv_name_back,item.getName());

    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
