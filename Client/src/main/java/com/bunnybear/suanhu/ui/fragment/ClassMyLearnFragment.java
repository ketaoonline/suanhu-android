package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.bean.CalcOrder;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.bean.StudyProgress;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.AudioActivity;
import com.bunnybear.suanhu.ui.activity.ConversationListActivity;
import com.bunnybear.suanhu.ui.activity.LoginActivity;
import com.bunnybear.suanhu.ui.activity.MainActivity;
import com.bunnybear.suanhu.ui.activity.PlayActivity;
import com.bunnybear.suanhu.ui.adapter.MyLearnAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DensityUtil;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassMyLearnFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static ClassMyLearnFragment newInstance() {
        ClassMyLearnFragment fragment = new ClassMyLearnFragment();
        return fragment;
    }

    MyLearnAdapter adapter;
    List<StudyProgress> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.fragment_class_my_learn;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void normalLoad() {
        twRefreshLayout.setEnableLoadmore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"), 2, DensityUtil.dp2px(mActivity, 1)));
        adapter = new MyLearnAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        twRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
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


    private void getData() {
        Http.http.createApi(ClassAPI.class)
                .getStudyProgress()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<StudyProgress>>() {
                    @Override
                    public void success(List<StudyProgress> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        twRefreshLayout.finishRefreshing();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StudyProgress studyProgress = list.get(position);
        switch (studyProgress.getType()) {
            case 0://视频
                PlayActivity.open(true, studyProgress.getCourse_info_id(), mActivity, -1);
                break;
            case 1://音频
                AudioActivity.open(studyProgress.getCourse_info_id(), mActivity,-1);
                break;
            case 2://图文
                PlayActivity.open(false, studyProgress.getCourse_info_id(), mActivity, -1);
                break;
        }
    }
}
