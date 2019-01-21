package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Member;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;

import java.util.List;

public class FamilyAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    public FamilyAdapter(@Nullable List<Member> data) {
        super(R.layout.item_family_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setText(R.id.tv_name, item.getF_name());
        helper.setText(R.id.tv_sex, item.getSex());
        String birthType = item.getLunar() == 0 ? "【公历】" : "【农历】";

        String date = DateUtil.getMillon(item.getBirth() * 1000, DateUtil.FORMAT_YMD_CN);
        if (item.getIs_run() == 1){
            String year = date.substring(0,5);
            String monthAndDay = date.substring(5);
            date = year + "闰" + monthAndDay;
        }
        helper.setText(R.id.tv_birth, birthType + date + "  "+item.getLast_birth());
        helper.setText(R.id.tv_tag,item.getTag());
    }
}
