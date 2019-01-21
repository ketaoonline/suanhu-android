package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ClassListAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassListActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static void open(AppActivity activity, int type, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("title", title);
        activity.startActivity(bundle, ClassListActivity.class);
    }

    List<SClass> list = new ArrayList<>();
    ClassListAdapter adapter;
    int type;
    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected String setTitleStr() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type",-1);
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassListAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//            }
//
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//            }
//        });

        getData();

    }

    private void getData() {
        Http.http.createApi(ClassAPI.class)
                .getTagClasses(type)
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClassDetailActivity.open(this,list.get(position).getId());
    }
}
