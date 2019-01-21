package com.bunnybear.suanhu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.CalcOrder;
import com.bunnybear.suanhu.bean.ClassOrder;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.OrderDetailActivity;
import com.bunnybear.suanhu.ui.adapter.ClassOrderAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassOrderFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static ClassOrderFragment newInstance(int type){
        ClassOrderFragment fragment = new ClassOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<ClassOrder> list = new ArrayList<>();
    ClassOrderAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new ClassOrderAdapter(list);
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

    private void getData() {
        Http.http.createApi(MineAPI.class)
                .classOrders(getArguments().getInt("type"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ClassOrder>>() {
                    @Override
                    public void success(List<ClassOrder> result) {
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
        if (list.get(position).getIs_pay() == 0){
            OrderDetailActivity.open(mActivity,list.get(position).getOrder_id()+"");
        }
    }
}
