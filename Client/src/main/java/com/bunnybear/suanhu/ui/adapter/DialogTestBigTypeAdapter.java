package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DialogTestBigTypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public int checkPosition = 0;

    public DialogTestBigTypeAdapter(@Nullable List<String> data) {
        super(R.layout.item_dialog_test_big_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv = helper.getView(R.id.tv);
        tv.setText(Html.fromHtml(item));
        tv.setBackgroundResource(checkPosition == helper.getLayoutPosition() ? R.drawable.bg_big_type_checked : R.drawable.bg_big_type_normal);

    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
