package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Comment;
import com.bunnybear.suanhu.bean.Experience;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.MasterInfo;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.ListShowView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

public class MasterIntroduceAdapter extends BaseMultiItemQuickAdapter<MainBaseBean, BaseViewHolder> {

    AppActivity activity;
    List<Experience> experienceList;
    MasterInfo masterInfo;

    public MasterIntroduceAdapter(AppActivity activity, List<MainBaseBean> data) {
        super(data);
        this.activity = activity;
        addItemType(0, R.layout.item_master_info);
        addItemType(1, R.layout.item_master_text);
        addItemType(2, R.layout.item_master_learn_experience);
        addItemType(3, R.layout.item_master_comment);
        addItemType(4, R.layout.item_master_image);

        experienceList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()) {
            case 4:
                if (item instanceof MasterInfo) {
                    masterInfo = (MasterInfo) item;
                    experienceList = masterInfo.getExperience_info();
                    GlideUtil.load(masterInfo.getImag(), helper.getView(R.id.iv));
                }
                break;
            case 0:
                if (masterInfo != null) {
                    GlideUtil.load(masterInfo.getImag(), helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_name, masterInfo.getMaster_name());
                    helper.setText(R.id.tv_experience, masterInfo.getExperience() + "年");
                    helper.setText(R.id.tv_finish_count, "完成订单 " + masterInfo.getOrder_num());
                    helper.setText(R.id.tv_comment_count, "评价数 " + masterInfo.getMark_num());

                    LinearLayout llTagContainer = helper.getView(R.id.ll_tag_container);
                    llTagContainer.removeAllViews();
                    for (String s : masterInfo.getMake_well()) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10, 0, 0, 0);
                        TextView tv = new TextView(activity);
                        tv.setBackgroundResource(R.drawable.shape_tag_checked);
                        tv.setText(s);
                        tv.setTextSize(9f);
                        tv.setTextColor(Color.parseColor("#A096DE"));
                        tv.setPadding(8, 2, 8, 2);
                        llTagContainer.addView(tv, lp);
                    }

                    LinearLayout llBelongContainer = helper.getView(R.id.ll_belong_container);
                    llBelongContainer.removeAllViews();
                    for (String s : masterInfo.getBelong()) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10, 0, 0, 0);
                        TextView tv = new TextView(activity);
                        tv.setBackgroundResource(R.drawable.rect_red);
                        tv.setText(s);
                        tv.setTextSize(9f);
                        tv.setTextColor(Color.WHITE);
                        tv.setPadding(10, 3, 10, 3);
                        llBelongContainer.addView(tv, lp);
                    }

                    LinearLayout llTagWordContainer = helper.getView(R.id.ll_tag_word_container);
                    llTagWordContainer.removeAllViews();
                    for (String s : masterInfo.getTag_word()) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(30, 0, 0, 0);
                        TextView tv = new TextView(activity);
                        tv.setText(s);
                        tv.setTextSize(9f);
                        tv.setTextColor(Color.parseColor("#666666"));
                        tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.master_tag, 0, 0, 0);
                        tv.setCompoundDrawablePadding(6);
                        llTagWordContainer.addView(tv, lp);
                    }
                }
                break;
            case 1:
                int position = helper.getLayoutPosition();
                helper.setText(R.id.tv, position == 2 ? "学习经历" : "服务反馈");
                break;
            case 2:
                ListShowView listShowView = helper.getView(R.id.listShowView);
                ExperienceAdapter adapter = new ExperienceAdapter(activity, experienceList);
                listShowView.setAdapter(adapter);
                break;
            case 3:
                if (item instanceof Comment) {
                    Comment comment = (Comment) item;
                    GlideUtil.load(comment.getAvatar(), helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_nickname, comment.getUser_nickname());
                    helper.setText(R.id.tv_content, comment.getContent());
                    helper.setVisible(R.id.ll_answer, TextUtils.isEmpty(comment.getRe_data()) ? false : true);
                    helper.setText(R.id.tv_re_content, comment.getRe_data());
                    helper.setText(R.id.tv_date, DateUtil.timeLogic(DateUtil.getMillon(comment.getCreate_time() * 1000, DateUtil.FORMAT_YMDHMS), DateUtil.FORMAT_YMDHMS));
                }
                break;
        }
    }
}
