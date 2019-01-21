package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Question;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PutQuestionAdapter extends BaseQuickAdapter<Question,BaseViewHolder>{

    public int checkPosition = -1;

    public PutQuestionAdapter(@Nullable List<Question> data) {
        super(R.layout.item_question_put, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Question item) {
        int position = helper.getLayoutPosition();
        helper.setText(R.id.tv,item.getContent());
        helper.setTextColor(R.id.tv,checkPosition == position ? Color.parseColor("#A096DE") : Color.parseColor("#333333"));
        helper.setImageResource(R.id.iv,checkPosition == position ? R.mipmap.shop_car_checked : R.mipmap.shop_car_uncheck);
    }


    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
