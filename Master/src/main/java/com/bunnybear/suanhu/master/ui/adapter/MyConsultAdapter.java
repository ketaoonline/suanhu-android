package com.bunnybear.suanhu.master.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.bean.DetailedTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class MyConsultAdapter extends BaseQuickAdapter<DetailedTest,BaseViewHolder> {
    public MyConsultAdapter(@Nullable List<DetailedTest> data) {
        super(R.layout.item_my_consult, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailedTest item) {
        helper.setText(R.id.tv_order_num, "订单号：" + item.getOrder_use_sn());
        helper.setText(R.id.tv_order_type, "订单类型：" + item.getCoursename());
        helper.setText(R.id.tv_name, item.getUser_name());
        helper.setText(R.id.tv_status, item.getEnd_time() == 0 ? "[未完成]" : "[已完成]");
        String content = item.getAnswer();
//        if (!TextUtils.isEmpty(content)&&content.contains("http:")){
//            content = "[图片]";
//        }
        helper.setText(R.id.tv_content, item.getSum_chat() > 0 ? "[" + item.getSum_chat() + "条]" + content : content);
        GlideUtil.load(item.getUser_avatar(), (ImageView) helper.getView(R.id.iv));
        helper.setVisible(R.id.iv_point, item.getNew_chat() == 0);
    }
}
