package com.bunnybear.suanhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.Article;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ArticleAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticleFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static ArticleFragment newInstance(int type) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    ArticleAdapter articleAdapter;
    List<Article> list = new ArrayList<>();

    int page = 1;
    int totalPage;

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        articleAdapter = new ArticleAdapter(list);
        articleAdapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                page = 1;
                getData(true);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                page++;
                getData(false);
            }
        });

        getData(true);
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }


    private void getData(final boolean isRefresh) {
        Http.http.createApi(MainAPI.class)
                .getArticlesOfType(getArguments().getInt("type"), page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<ArticleResponse>() {
                    @Override
                    public void success(ArticleResponse result) {
                        totalPage = result.getTotal_page();
                        refreshLayout.setEnableLoadmore(page < totalPage);
                        if (isRefresh) {
                            list.clear();
                            list.addAll(result.getList());
                            refreshLayout.finishRefreshing();
                        } else {
                            list.addAll(result.getList());
                            refreshLayout.finishLoadmore();
                        }
                        articleAdapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
