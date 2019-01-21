package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.Masters;
import com.bunnybear.suanhu.bean.TestBigTypeResponse;
import com.bunnybear.suanhu.ui.activity.AssortmentActivity;
import com.bunnybear.suanhu.ui.activity.MasterIntroduceActivity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.hawk.Hawk;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class CalcAdapter extends BaseMultiItemQuickAdapter<MainBaseBean, BaseViewHolder> {

    AppActivity activity;

    public CalcAdapter(AppActivity activity, List<MainBaseBean> data) {
        super(data);
        this.activity = activity;
        addItemType(0, R.layout.item_calc_text);
        addItemType(1, R.layout.item_calc_quick);
        addItemType(2, R.layout.item_divider);
        addItemType(3, R.layout.item_calc_master_new);
        addItemType(4, R.layout.item_search);
        addItemType(5, R.layout.item_h_masters);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                String title = "";
                switch (helper.getLayoutPosition()){
                    case 1:
                        title = "     咨  询";
                        break;
                    case 4:
                        title = "新晋大师";
                        break;
                    case 7:
                        title = "热门大师";
                        break;
                    case 10:
                        title = "推荐大师";
                        break;
                }
                helper.setText(R.id.tv, title);
                break;
            case 1:
                if (item instanceof TestBigTypeResponse){
                    TestBigTypeResponse response = (TestBigTypeResponse) item;
                    RecyclerView rv = helper.getView(R.id.rv);
                    rv.setLayoutManager(new GridLayoutManager(activity,4));
                    TestBigTypeAdapter adapter = new TestBigTypeAdapter(response.getList());
                    rv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Hawk.put("isFirstChooseMaster",false);
                            AssortmentActivity.open(activity,response.getList().get(position).getQuestion_type_id());
                        }
                    });
                }
                break;
            case 3:
                if (item instanceof Master) {
                    Master master = (Master) item;
                    GlideUtil.load(master.getImag(), (ImageView) helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_name, master.getMaster_name());
                    helper.setText(R.id.tv_star_count, master.getStars() + "分");
                    helper.setText(R.id.tv_price, "¥" + master.getPrice() + "起");
//                    helper.setText(R.id.tv_content, master.getIntroduce());
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
                break;
            case 4:
                helper.addOnClickListener(R.id.rl_search);
                break;
            case 5:
                if (item instanceof Masters){
                    Masters response = (Masters) item;
                    RecyclerView rv = helper.getView(R.id.rv);
                    rv.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    HRecommendMasterListAdapter adapter = new HRecommendMasterListAdapter(response.getList());
                    rv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            MasterIntroduceActivity.open(activity,response.getList().get(position).getId());
                        }
                    });
                }
                break;
        }
    }
}
