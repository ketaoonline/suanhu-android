package com.bunnybear.suanhu.master.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.bean.Question;
import com.bunnybear.suanhu.master.bean.SimpleTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends BaseQuickAdapter<SimpleTest,BaseViewHolder>{

    AppActivity activity;
    boolean isEnd;
    public QuestionAdapter(AppActivity activity,@Nullable List<SimpleTest> data,boolean isEnd) {
        super(R.layout.item_question, data);
        this.activity = activity;
        this.isEnd = isEnd;
    }

    @Override
    protected void convert(BaseViewHolder helper, SimpleTest item) {
        TextView tvAnswerCount = helper.getView(R.id.tv_answer_count);
        if (item.getNew_answer() == 1){
            tvAnswerCount.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.tv_red_point,0);
        }

        RecyclerView recyclerView = helper.getView(R.id.rv);
        GridLayoutManager manager = new GridLayoutManager(activity,3);
        recyclerView.setLayoutManager(manager);
        if (item.getImg().size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            QuestionImageAdapter adapter = new QuestionImageAdapter(activity,item.getImg());
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_order_sn,"订单号："+item.getOrder_use_sn());
        helper.setText(R.id.tv_order_type,"订单类型："+item.getName());
        helper.setText(R.id.tv_content,item.getContent());
        tvAnswerCount.setText(item.getAnswer()+"个回复");
        helper.setVisible(R.id.tv_delete,!isEnd);
        helper.setVisible(R.id.iv,isEnd);
        helper.addOnClickListener(R.id.tv_delete);
    }
}
