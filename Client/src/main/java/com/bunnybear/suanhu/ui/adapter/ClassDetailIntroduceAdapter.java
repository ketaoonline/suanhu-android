package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class ClassDetailIntroduceAdapter extends BaseMultiItemQuickAdapter<MainBaseBean,BaseViewHolder> {
    AppActivity activity;
    public ClassDetailIntroduceAdapter(AppActivity activity,List<MainBaseBean> data) {
        super(data);
        this.activity = activity;
        addItemType(0, R.layout.item_introduce_detail);
        addItemType(1, R.layout.item_introduce_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()){
            case 0:
                if (item instanceof ClassDetailIntroduce){
                    ClassDetailIntroduce introduce = (ClassDetailIntroduce) item;
                    helper.setText(R.id.tv_title,introduce.getCoursename());
                    helper.setText(R.id.tv_star,introduce.getStars()+"分");
                    helper.setText(R.id.tv_learn_count,introduce.getTotal_people()+"人学过");
                    helper.setText(R.id.tv_price,introduce.getPrice()+"");
                    helper.setText(R.id.tv_old_price,"¥"+introduce.getOld_price());
                    ((TextView)helper.getView(R.id.tv_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                    ScaleRatingBar scaleRatingBar = helper.getView(R.id.scaleRatingBar);
                    scaleRatingBar.setRating((float) introduce.getStars());

                    LinearLayout llTagContainer = helper.getView(R.id.ll_tag_container);
                    llTagContainer.removeAllViews();
                    for (String s : introduce.getTag()) {
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

                    if (introduce.getPeople().size() > 0){
                        helper.getView(R.id.ll_people).setVisibility(View.VISIBLE);
                        LinearLayout llPeopleContainer = helper.getView(R.id.ll_people_container);
                        llPeopleContainer.removeAllViews();
                        for (String s : introduce.getPeople()) {
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(0,10,0,0);
                            TextView tv = new TextView(activity);
                            tv.setText(s);
                            tv.setTextSize(9f);
                            tv.setTextColor(Color.parseColor("#333333"));
                            tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.finger,0,0,0);
                            tv.setCompoundDrawablePadding(10);
                            llPeopleContainer.addView(tv,lp);
                        }
                    }else {
                        helper.getView(R.id.ll_people).setVisibility(View.GONE);
                    }



                }
                break;
            case 1:
                if (item instanceof ImageBean){
                    ImageBean imageBean = (ImageBean) item;
                    GlideUtil.load(imageBean.getPath(), (ImageView) helper.getView(R.id.iv));
                }
                break;
        }
    }
}
