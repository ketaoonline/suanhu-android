package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.CollectArticle;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.MasterIntroduceActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DensityUtil;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CollectMasterFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static CollectMasterFragment newInstance() {
        CollectMasterFragment fragment = new CollectMasterFragment();
        return fragment;
    }

    List<Master> list = new ArrayList<>();
    CollectMasterAdapter adapter;
    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"),2, DensityUtil.dp2px(mActivity,1)));
        adapter = new CollectMasterAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });

        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getCollectMasters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Master>>() {
                    @Override
                    public void success(List<Master> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MasterIntroduceActivity.open(mActivity,list.get(position).getId());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.iv_delete:
                cancelCollect(list.get(position).getId());
                break;
        }
    }


    /**
     * 取消收藏
     */
    private void cancelCollect(int id) {
        Http.http.createApi(MineAPI.class)
                .deleteFollow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("取消收藏成功");
                        getData();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    class CollectMasterAdapter extends BaseQuickAdapter<Master, BaseViewHolder> {

        public CollectMasterAdapter(@Nullable List<Master> data) {
            super(R.layout.item_collect_master, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Master master) {
            LinearLayout llBelongContainer = helper.getView(R.id.ll_belong_container);
            llBelongContainer.removeAllViews();
            for (String s : master.getBelong()) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10,0,0,0);
                TextView tv = new TextView(mActivity);
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
                TextView tv = new TextView(mActivity);
                tv.setBackgroundResource(R.drawable.shape_tag_checked);
                tv.setText(s);
                tv.setTextSize(9f);
                tv.setTextColor(Color.parseColor("#A096DE"));
                tv.setPadding(8,2,8,2);
                llTagContainer.addView(tv,lp);
            }
            GlideUtil.load(master.getImag(), helper.getView(R.id.iv));
            helper.setText(R.id.tv_name, master.getMaster_name());
            helper.setText(R.id.tv_content, master.getIntroduce());
            helper.addOnClickListener(R.id.iv_delete);
        }
    }
}
