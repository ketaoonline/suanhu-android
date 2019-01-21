package com.bunnybear.suanhu.master.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppFragment;
import com.bunnybear.suanhu.master.bean.Question;
import com.bunnybear.suanhu.master.bean.SimpleTest;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.ui.activity.EndSimpleTestDetailActivity;
import com.bunnybear.suanhu.master.ui.adapter.QuestionAdapter;
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

public class QuestionFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static QuestionFragment newInstance(boolean isEnd) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEnd",isEnd);
        fragment.setArguments(bundle);
        return fragment;
    }

    QuestionAdapter adapter;
    List<SimpleTest> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {}

    @Override
    protected void normalLoad() {
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"),2, DensityUtil.dp2px(mActivity,10)));
        adapter = new QuestionAdapter(mActivity,list,getArguments().getBoolean("isEnd"));
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                },1000);
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
        if (getArguments().getBoolean("isEnd"))return;
        EndSimpleTestDetailActivity.open(mActivity,list.get(position).getOrder_use_sn());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_delete:
                deleteSimpleTest(list.get(position).getOrder_use_sn());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK){
            getData();
        }
    }

    private void getData(){
        if (getArguments().getBoolean("isEnd")) {
            Http.http.createApi(MineAPI.class)
                    .getEndSimpleTests()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<List<SimpleTest>>() {
                        @Override
                        public void success(List<SimpleTest> result) {
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
                    .getSimpleTests()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<List<SimpleTest>>() {
                        @Override
                        public void success(List<SimpleTest> result) {
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
