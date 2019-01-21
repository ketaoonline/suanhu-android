package com.bunnybear.suanhu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.Comment;
import com.bunnybear.suanhu.bean.CommentResponse;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.CommentActivity;
import com.bunnybear.suanhu.ui.adapter.ClassDetailCommentAdapter;
import com.bunnybear.suanhu.ui.adapter.ClassDetailIntroduceAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassDetailCommentFragment extends ScrollContainerFragment implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;

    public static ClassDetailCommentFragment newInstance(int courseId){
        ClassDetailCommentFragment fragment = new ClassDetailCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",courseId);
        fragment.setArguments(bundle);
        return fragment;
    }
    List<MainBaseBean> list = new ArrayList<>();
    ClassDetailCommentAdapter adapter;

    int page = 1;
    int totalPage;

    @Override
    protected int initLayout() {
        return R.layout.fragment_class_detail_comment;
    }

    @Override
    protected void lazyLoad() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        list.add(new MainBaseBean(0));
        adapter = new ClassDetailCommentAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        recyclerView.useDefaultLoadMore();
        recyclerView.setLoadMoreListener(new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
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

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        CommentActivity.open(mActivity,getArguments().getInt("id"),"class");
    }

    private void getData(boolean isRefresh){
        Http.http.createApi(CalcAPI.class)
                .getComments(getArguments().getInt("id"),"class",page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<CommentResponse>() {
                    @Override
                    public void success(CommentResponse result) {
                        totalPage = result.getTotal_page();
                        if (isRefresh) {
                            list.clear();
                            list.add(result);
                            for (Comment comment : result.getComment()) {
                                comment.setViewType(1);
                                list.add(comment);
                            }
                            recyclerView.loadMoreFinish(list.size() == 1,page < totalPage);
                        } else {
                            for (Comment comment : result.getComment()) {
                                comment.setViewType(1);
                                list.add(comment);
                            }
                            recyclerView.loadMoreFinish(false,page < totalPage);
                        }
                        adapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


}
