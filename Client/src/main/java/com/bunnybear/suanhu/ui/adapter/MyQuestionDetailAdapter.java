package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Answer;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.MyQuestion;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyQuestionDetailAdapter extends BaseMultiItemQuickAdapter<MainBaseBean, BaseViewHolder> {

    AppActivity activity;

    public MyQuestionDetailAdapter(AppActivity activity, List<MainBaseBean> data) {
        super(data);
        this.activity = activity;

        addItemType(0, R.layout.item_question_detail);
        addItemType(1, R.layout.item_question_text);
        addItemType(2, R.layout.item_answer);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                if (item instanceof MyQuestion) {
                    MyQuestion question = (MyQuestion) item;
//                    GlideUtil.load(question.getAvatar(), helper.getView(R.id.iv_head));
//                    helper.setText(R.id.tv_name, answer.getMaster_name());
//                    helper.setText(R.id.tv_des, answer.getIntroduce());
                    helper.setText(R.id.tv_content, question.getQuestion());
                    RecyclerView recyclerView = helper.getView(R.id.rv);
                    GridLayoutManager manager = new GridLayoutManager(activity, 3);
                    recyclerView.setLayoutManager(manager);
                    if (question.getQuestion_image().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        MyQuestionImageAdapter adapter = new MyQuestionImageAdapter(activity, question.getQuestion_image());
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                break;
            case 2:
                if (item instanceof Answer) {
                    Answer answer = (Answer) item;
                    GlideUtil.load(answer.getAvatar(), helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_name, answer.getMaster_name());
                    helper.setText(R.id.tv_des, answer.getIntroduce());
                    helper.setText(R.id.tv_content, answer.getContent());

                    RecyclerView rv = helper.getView(R.id.rv);
                    GridLayoutManager manager1 = new GridLayoutManager(activity, 3);
                    rv.setLayoutManager(manager1);
                    if (answer.getAnswer_image().size() > 0) {
                        rv.setVisibility(View.VISIBLE);
                        MyQuestionImageAdapter adapter1 = new MyQuestionImageAdapter(activity, answer.getAnswer_image());
                        rv.setAdapter(adapter1);
                    } else {
                        rv.setVisibility(View.GONE);
                    }
                    TextView tvFollow = helper.getView(R.id.tv_follow);
                    if (answer.getFollow() == 0) {
                        tvFollow.setText("加关注");
                        tvFollow.setTextColor(Color.WHITE);
                        tvFollow.setBackgroundResource(R.drawable.bg_btn);
                    } else {
                        tvFollow.setText("已关注");
                        tvFollow.setTextColor(Color.parseColor("#A096DE"));
                        tvFollow.setBackgroundResource(R.drawable.bg_btn_white);
                    }

                    helper.addOnClickListener(R.id.tv_follow);
                }
                break;
        }
    }
}
