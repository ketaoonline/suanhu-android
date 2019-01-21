package com.bunnybear.suanhu.ui.adapter;

import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Comment;
import com.bunnybear.suanhu.bean.CommentResponse;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

public class ClassDetailCommentAdapter extends BaseMultiItemQuickAdapter<MainBaseBean,BaseViewHolder> {

    public ClassDetailCommentAdapter(List<MainBaseBean> data) {
        super(data);

        addItemType(0, R.layout.item_comment_statistics_info);
        addItemType(1, R.layout.item_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()){
            case 0:
                if (item instanceof CommentResponse){
                    CommentResponse commentResponse = (CommentResponse) item;
                    helper.addOnClickListener(R.id.tv_to_comment);
                    helper.setText(R.id.tv_count,"（"+commentResponse.getComment_sum()+"人评价）");
                    helper.setText(R.id.tv_star_count,"综合评分\n"+commentResponse.getStar_average()+"分");
                    helper.setText(R.id.tv_1,commentResponse.getStar_people().getOne()+"人评分");
                    helper.setText(R.id.tv_2,commentResponse.getStar_people().getTwo()+"人评分");
                    helper.setText(R.id.tv_3,commentResponse.getStar_people().getThree()+"人评分");
                    helper.setText(R.id.tv_4,commentResponse.getStar_people().getFour()+"人评分");
                    helper.setText(R.id.tv_5,commentResponse.getStar_people().getFive()+"人评分");
                }
                break;
            case 1:
                if (item instanceof Comment){
                    Comment comment = (Comment) item;
                    GlideUtil.load(comment.getAvatar(),helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_nickname,comment.getUser_nickname());
                    helper.setText(R.id.tv_star_count,comment.getStar()/2+"分");
                    helper.setText(R.id.tv_content,comment.getContent());
                    helper.setVisible(R.id.ll_answer,TextUtils.isEmpty(comment.getRe_data()) ? false : true);
                    helper.setText(R.id.tv_re_content,comment.getRe_data());
                    helper.setText(R.id.tv_date, DateUtil.timeLogic(DateUtil.getMillon(comment.getCreate_time()*1000,DateUtil.FORMAT_YMDHMS),DateUtil.FORMAT_YMDHMS));
                    ScaleRatingBar scaleRatingBar = helper.getView(R.id.scaleRatingBar);
                    scaleRatingBar.setRating((float) comment.getStar()/2);
                }
                break;
        }
    }
}
