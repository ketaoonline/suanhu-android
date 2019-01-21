package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.ClassDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CollectClassFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static CollectClassFragment newInstance() {
        CollectClassFragment fragment = new CollectClassFragment();
        return fragment;
    }

    List<SClass> list = new ArrayList<>();
    CollectClassAdapter adapter;
    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new CollectClassAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClassDetailActivity.open(mActivity,list.get(position).getId());
    }


    class CollectClassAdapter extends BaseQuickAdapter<SClass,BaseViewHolder>{

        public CollectClassAdapter(@Nullable List<SClass> data) {
            super(R.layout.item_collect_class, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SClass item) {
            TextView tvOldPrice = helper.getView(R.id.tv_old_price);
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvOldPrice.setText("¥"+item.getOld_price());
            helper.setText(R.id.tv_title,item.getCoursename());
            helper.setText(R.id.tv_price,"¥"+item.getPrice());
            GlideUtil.load(item.getHead_banner(),helper.getView(R.id.iv));
        }
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .myCollectClasses()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SClass>>() {
                    @Override
                    public void success(List<SClass> result) {
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

}
