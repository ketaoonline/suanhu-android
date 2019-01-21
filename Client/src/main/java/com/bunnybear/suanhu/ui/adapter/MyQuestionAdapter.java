package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.SimpleTest;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyQuestionAdapter extends BaseQuickAdapter<SimpleTest,BaseViewHolder>{

    AppActivity activity;
    public MyQuestionAdapter(AppActivity activity,@Nullable List<SimpleTest> data) {
        super(R.layout.item_question, data);
        this.activity = activity;

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
            MyQuestionImageAdapter adapter = new MyQuestionImageAdapter(activity,item.getImg());
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_order_sn,"订单号："+item.getOrder_use_sn());
        helper.setText(R.id.tv_order_type,"订单类型："+item.getName());
        helper.setText(R.id.tv_content,item.getContent());
        tvAnswerCount.setText(item.getAnswer()+"个回复");

        helper.addOnClickListener(R.id.tv_delete);
    }
}
