package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ReasonAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public int checkPosition = -1;

    public ReasonAdapter(@Nullable List<String> data) {
        super(R.layout.item_reason, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv = helper.getView(R.id.tv);
        tv.setText(item);
        int resId = R.mipmap.green_uncheck;
        if (checkPosition == helper.getLayoutPosition()){
            resId = R.mipmap.green_checked;
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
