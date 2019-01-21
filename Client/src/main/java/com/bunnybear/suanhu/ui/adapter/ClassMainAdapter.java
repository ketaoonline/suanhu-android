package com.bunnybear.suanhu.ui.adapter;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ClassMainAdapter extends BaseMultiItemQuickAdapter<MainBaseBean,BaseViewHolder> {

    public ClassMainAdapter(List<MainBaseBean> data) {
        super(data);

        addItemType(0, R.layout.item_search);
        addItemType(1,R.layout.item_class_main_banner);
        addItemType(2,R.layout.item_banner);
        addItemType(3,R.layout.item_hot_topic);
        addItemType(4,R.layout.item_article);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {

    }
}
