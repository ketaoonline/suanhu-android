package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.StudyProgress;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class MyLearnAdapter extends BaseQuickAdapter<StudyProgress,BaseViewHolder>{
    public MyLearnAdapter(@Nullable List<StudyProgress> data) {
        super(R.layout.item_my_learn, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudyProgress item) {
        ProgressBar progressBar = helper.getView(R.id.progressBar);
        progressBar.setProgress(item.getMuch());
        helper.setText(R.id.tv_name,item.getCourse_name());
        helper.setText(R.id.tv_progress,"已学习"+item.getMuch()+"%");
        GlideUtil.load(item.getHead_banner(),helper.getView(R.id.iv));
    }
}
