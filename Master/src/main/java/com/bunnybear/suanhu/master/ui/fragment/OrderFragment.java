package com.bunnybear.suanhu.master.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppFragment;
import com.bunnybear.suanhu.master.bean.Order;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.ui.adapter.OrderAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderFragment extends AppFragment implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static OrderFragment newInstance(int type) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<Order> list = new ArrayList<>();
    OrderAdapter adapter;

    int type = 0;

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        type = getArguments().getInt("type");

        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new OrderAdapter(list,type);
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
        if (type == 0){
            Http.http.createApi(MineAPI.class)
                    .getSimpleTestOrders()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<List<Order>>() {
                        @Override
                        public void success(List<Order> result) {
                            list.clear();
                            list.addAll(result);
                            adapter.setNewData(list);
                        }

                        @Override
                        public void fail(int errCode, String errStr) {
                            showMessage(errStr);
                        }
                    }));
        }else {
            Http.http.createApi(MineAPI.class)
                    .getDetailTestOrders()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<List<Order>>() {
                        @Override
                        public void success(List<Order> result) {
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


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_responder:
                responderOrder(position);
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

    private void responderOrder(int position){
        Http.http.createApi(MineAPI.class)
                .responderOrder(list.get(position).getOrder_use_sn())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {

                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void showDialog(){

    }

}
