package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Coupon;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;

import java.util.List;

public class CouponAdatper extends BaseQuickAdapter<Coupon, BaseViewHolder> {
    public CouponAdatper(@Nullable List<Coupon> data) {
        super(R.layout.item_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {
        String type = "";
        switch (item.getCoupon_type()) {
            case 0:
                type = "课程";
                break;
            case 1:
                type = "大师咨询";
                break;
            case 2:
                type = "商城";
                break;
            case 3:
                type = "会员";
                break;
            case 4:
                type = "全场通用";
                break;
        }
        helper.setText(R.id.tv_type, "使用范围：" + type);
        helper.setText(R.id.tv_money, "¥ " + item.getCoupon_money());
        helper.setText(R.id.tv_price, "满" + (int) item.getCoupon_price() + "使用");
        helper.setText(R.id.tv_date, DateUtil.getMillon(item.getBegin_time() * 1000, DateUtil.FORMAT_YMDHM) + "至" + DateUtil.getMillon(item.getEnd_time() * 1000, DateUtil.FORMAT_YMDHM));
    }
}
