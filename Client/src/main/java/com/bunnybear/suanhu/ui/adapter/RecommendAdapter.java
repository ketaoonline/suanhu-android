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

public class RecommendAdapter extends BaseQuickAdapter<Master,BaseViewHolder>{

    public int checkPosition = -1;
    AppActivity activity;
    public RecommendAdapter(AppActivity activity,@Nullable List<Master> data) {
        super(R.layout.item_recommend_master, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, Master master) {
        helper.setImageResource(R.id.iv_check_status,checkPosition == helper.getLayoutPosition() ? R.mipmap.shop_car_checked : R.mipmap.shop_car_uncheck);

        GlideUtil.load(master.getImag(),helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_name, master.getMaster_name());
        helper.setText(R.id.tv_star_count, master.getStars() + "分");
        helper.setText(R.id.tv_price, "¥" + master.getPrice() + "起");
        helper.setText(R.id.tv_content, master.getIntroduce());
        helper.setText(R.id.tv_finish_count, master.getOrder_num() + "");
        helper.setText(R.id.tv_answer_count, master.getCallback_num() + "");
        helper.setText(R.id.tv_comment_count, master.getMark_num() + "");

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

        helper.addOnClickListener(R.id.iv_check_status);

    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
