package com.bunnybear.suanhu.ui.adapter;

import android.graphics.drawable.ClipDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Article;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.Notices;
import com.bunnybear.suanhu.bean.TopData;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.Arrays;
import java.util.List;

import ezy.ui.view.NoticeView;

public class MainAdapter extends BaseMultiItemQuickAdapter<MainBaseBean, BaseViewHolder> {

    boolean isOpen = false;
    String duanyu = "";
    public MainAdapter(List<MainBaseBean> data) {
        super(data);

        addItemType(0, R.layout.item_main_top_new);
        addItemType(1, R.layout.item_consult);
        addItemType(2, R.layout.item_banner);
        addItemType(3, R.layout.item_hot_topic);
        addItemType(4, R.layout.item_article);
        addItemType(5, R.layout.item_fortune_all);
        addItemType(6, R.layout.item_notice);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()) {
            case 0:
//                ImageView ivBall = helper.getView(R.id.iv_ball);
//                ClipDrawable clipBall = (ClipDrawable) ivBall.getBackground();
//                clipBall.setLevel(7900);
//                ImageView ivLove = helper.getView(R.id.iv_love);
//                ClipDrawable clipLove = (ClipDrawable) ivLove.getBackground();
//                clipLove.setLevel(2000);
//                ImageView ivBusiness = helper.getView(R.id.iv_business);
//                ClipDrawable clipBusiness = (ClipDrawable) ivBusiness.getBackground();
//                clipBusiness.setLevel(5000);
//                ImageView ivMoney = helper.getView(R.id.iv_money);
//                ClipDrawable clipMoney = (ClipDrawable) ivMoney.getBackground();
//                clipMoney.setLevel(8000);
                if (item instanceof TopData){
                    TopData topData = (TopData) item;
                    duanyu = topData.getDuanyu();
                    helper.setText(R.id.tv_point,topData.getYunfen()+"");
                    helper.setText(R.id.tv_zouyun,topData.getZouyu());
                    helper.addOnClickListener(R.id.tv_to_detail);
                }
                break;
            case 1:
                helper.addOnClickListener(R.id.rl_find_master);
                helper.addOnClickListener(R.id.rl_quick);
                break;
            case 2:
                break;
            case 3:
                helper.addOnClickListener(R.id.tv_all_article);
                break;
            case 4:
                if (item instanceof Article) {
                    Article article = (Article) item;
                    helper.setText(R.id.tv_type, article.getCategory_name());
                    helper.setText(R.id.tv_title, article.getPost_title());
                    helper.setText(R.id.tv_author, "作者：" + article.getPost_source());
                    helper.setText(R.id.tv_content, article.getPost_excerpt());
                    GlideUtil.load(article.getImage(), helper.getView(R.id.iv));
                }
                break;
            case 5:
                TextView tvContent = helper.getView(R.id.tv_content);
                tvContent.setText(duanyu);
                RelativeLayout rl = helper.getView(R.id.rl);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvContent.setMaxLines(isOpen ? 4 : 100);
                        if (isOpen){
                            tvContent.setMaxLines(4);
                            animateCollapse(helper.getView(R.id.iv));
                        }else {
                            tvContent.setMaxLines(100);
                            animateExpand(helper.getView(R.id.iv));
                        }
                        isOpen = !isOpen;
                    }
                });
                break;
            case 6:
                if (item instanceof Notices){
                    Notices notices = (Notices) item;
                    NoticeView noticeView = helper.getView(R.id.notice);
                    noticeView.start(notices.getList());
                }
                break;
        }
    }

    private void animateExpand(ImageView iv) {
        RotateAnimation rotate = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        iv.startAnimation(rotate);
    }

    private void animateCollapse(ImageView iv) {
        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        iv.startAnimation(rotate);
    }
}
