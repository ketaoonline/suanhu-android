package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Master;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class SearchMasterAdapter extends BaseQuickAdapter<Master,BaseViewHolder> {
    AppActivity activity;
    public SearchMasterAdapter(AppActivity appActivity, @Nullable List<Master> data) {
        super(R.layout.item_calc_master_new, data);
        this.activity = appActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, Master master) {
        GlideUtil.load(master.getImag(), (ImageView) helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_name, master.getMaster_name());
        helper.setText(R.id.tv_star_count, master.getStars() + "分");
        helper.setText(R.id.tv_price, "¥" + master.getPrice() + "起");
//        helper.setText(R.id.tv_content, master.getIntroduce());
        helper.setText(R.id.tv_finish_count, master.getOrder_num() + "");
        helper.setText(R.id.tv_answer_count, master.getCallback_num() + "");
        helper.setText(R.id.tv_comment_count, master.getMark_num() + "");

        TextView tvStatus = helper.getView(R.id.tv_status);
        switch (master.getOnline()) {
            case 0:
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_green, 0, 0, 0);
                tvStatus.setText("在线");
                break;
            case 1:
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_red, 0, 0, 0);
                tvStatus.setText("忙碌");
                break;
            case 2:
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_gray, 0, 0, 0);
                tvStatus.setText("下线");
                break;
        }
        ScaleRatingBar scaleRatingBar = helper.getView(R.id.scaleRatingBar);
        scaleRatingBar.setRating((float) master.getStars());

        LinearLayout llBelongContainer = helper.getView(R.id.ll_belong_container);
        llBelongContainer.removeAllViews();
        for (String s : master.getBelong()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10,0,0,0);
            TextView tv = new TextView(activity);
            tv.setBackgroundResource(R.drawable.rect_red);
            tv.setText(s);
            tv.setTextSize(9f);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(10,3,10,3);
            llBelongContainer.addView(tv,lp);
        }

        LinearLayout llTagContainer = helper.getView(R.id.ll_tag_container);
        llTagContainer.removeAllViews();
        for (String s : master.getMake_well()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10,0,0,0);
            TextView tv = new TextView(activity);
            tv.setBackgroundResource(R.drawable.shape_tag_checked);
            tv.setText(s);
            tv.setTextSize(9f);
            tv.setTextColor(Color.parseColor("#A096DE"));
            tv.setPadding(8,2,8,2);
            llTagContainer.addView(tv,lp);
        }
    }
}
