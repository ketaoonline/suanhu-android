package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.Question;
import com.bunnybear.suanhu.bean.SignInfo;
import com.bunnybear.suanhu.bean.SimpleTest;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.MyQuestionDetailActivity;
import com.bunnybear.suanhu.ui.adapter.MyQuestionAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DensityUtil;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyQuestionFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static MyQuestionFragment newInstance() {
        MyQuestionFragment fragment = new MyQuestionFragment();
        return fragment;
    }

    MyQuestionAdapter adapter;
    List<SimpleTest> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void normalLoad() {
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"),2, DensityUtil.dp2px(mActivity,10)));
        adapter = new MyQuestionAdapter(mActivity,list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Override
    protected boolean useLazyLoad() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyQuestionDetailActivity.open(mActivity,list.get(position).getQuestion_id());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_delete:
                deleteSimpleTest(list.get(position).getOrder_use_sn());
                break;
        }
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getSimpleTests()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SimpleTest>>() {
                    @Override
                    public void success(List<SimpleTest> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void deleteSimpleTest(int id){
        Http.http.createApi(MineAPI.class)
                .deleteTest(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        getData();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
