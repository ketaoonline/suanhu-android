package com.bunnybear.suanhu.master.ui.adapter;

import android.text.TextUtils;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.bean.Income;
import com.bunnybear.suanhu.master.bean.IncomeResponse;
import com.bunnybear.suanhu.master.bean.MainBaseBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.DateUtil;

import java.util.List;

public class IncomeAdapter extends BaseMultiItemQuickAdapter<MainBaseBean,BaseViewHolder> {

    public IncomeAdapter(List<MainBaseBean> data) {
        super(data);
        addItemType(0, R.layout.item_income_top);
        addItemType(1, R.layout.item_income_text);
        addItemType(2, R.layout.item_income);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()){
            case 0:
                if (item instanceof IncomeResponse){
                    IncomeResponse incomeResponse = (IncomeResponse) item;
                    helper.setText(R.id.tv_income,"¥ "+incomeResponse.getIncome());
                    helper.setText(R.id.tv_bank_card_num,TextUtils.isEmpty(incomeResponse.getBank_number()) ? "绑定银行卡号":incomeResponse.getBank_number());
                    helper.addOnClickListener(R.id.btn_encash);
                    helper.addOnClickListener(R.id.rl);
                }
                break;
            case 1:
                break;
            case 2:
                if (item instanceof Income){
                    Income income = (Income) item;
                    helper.setText(R.id.tv_order_num,"订单编号："+income.getOrder_id());
                    helper.setText(R.id.tv_date,DateUtil.getMillon(income.getCreattime()*1000,DateUtil.FORMAT_YMDHMS));
                    helper.setText(R.id.tv_type,"订单类型："+income.getType());
                    helper.setText(R.id.tv_money,"订单金额："+income.getMoney());
                }
                break;
        }
    }
}
