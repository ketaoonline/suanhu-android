package com.bunnybear.suanhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.bean.CalcOrder;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.ApplyRefundActivity;
import com.bunnybear.suanhu.ui.activity.MasterOrderDetailActivity;
import com.bunnybear.suanhu.ui.adapter.CalcOrderAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CalcOrderFragment extends AppFragment implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static CalcOrderFragment newInstance(int type) {
        CalcOrderFragment fragment = new CalcOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<CalcOrder> list = new ArrayList<>();
    CalcOrderAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new CalcOrderAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener(this);

        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    private void getData() {
        Http.http.createApi(MineAPI.class)
                .calcOrders(getArguments().getInt("type"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<CalcOrder>>() {
                    @Override
                    public void success(List<CalcOrder> result) {
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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                deleteOrder(position);
                break;
            case R.id.tv_to_pay:
                MasterOrderDetailActivity.open(mActivity,list.get(position).getOrder_id()+"");
                break;
            case R.id.tv_apply_refund:
                ApplyRefundActivity.open(mActivity, list.get(position).getOrder_id());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK){
            switch (requestCode){
                case 1000:
                    getData();
                    break;
            }
        }
    }

    private void deleteOrder(int position){
        Http.http.createApi(MineAPI.class)
                .deleteOrder(list.get(position).getOrder_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("取消成功");
                        list.remove(position);
                        adapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
