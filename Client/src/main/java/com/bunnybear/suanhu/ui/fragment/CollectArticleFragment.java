package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.CollectArticle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectArticleFragment extends AppFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static CollectArticleFragment newInstance() {
        CollectArticleFragment fragment = new CollectArticleFragment();
        return fragment;
    }

    List<CollectArticle> list = new ArrayList<>();
    CollectArticleAdapter articleAdapter;
    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected void lazyLoad() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        list.add(new CollectArticle());
        list.add(new CollectArticle());
        list.add(new CollectArticle());
        articleAdapter = new CollectArticleAdapter(list);
        articleAdapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(articleAdapter);
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }


    class CollectArticleAdapter extends BaseQuickAdapter<CollectArticle, BaseViewHolder> {

        public CollectArticleAdapter(@Nullable List<CollectArticle> data) {
            super(R.layout.item_collect_class, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CollectArticle item) {
            TextView tvOldPrice = helper.getView(R.id.tv_old_price);
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
