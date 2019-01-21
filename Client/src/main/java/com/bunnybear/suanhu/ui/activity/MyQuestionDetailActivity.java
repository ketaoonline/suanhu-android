package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Answer;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.SimpleTest;
import com.bunnybear.suanhu.bean.SimpleTestDetail;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.MyQuestionDetailAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyQuestionDetailActivity extends AppActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static void open(AppActivity activity,int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        activity.startActivity(bundle, MyQuestionDetailActivity.class);
    }

    MyQuestionDetailAdapter adapter;
    List<MainBaseBean> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected String setTitleStr() {
        return "问答详情";
    }

    @Override
    protected void init() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyQuestionDetailAdapter(this,list);
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener(this);
        getData();
    }


    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getSimpleTestDetail(getIntent().getIntExtra("id",-1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<SimpleTestDetail>() {
                    @Override
                    public void success(SimpleTestDetail result) {
                        list.clear();
                        list.add(result.getQuestion());
                        list.add(new MainBaseBean(1));
                        for (Answer answer : result.getAnswer()) {
                            answer.setViewType(2);
                            list.add(answer);
                        }
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
        switch (view.getId()){
            case R.id.tv_follow:
                Answer answer = (Answer) list.get(position);
                if (answer.getFollow() == 0){
                    follow(position);
                }
                break;
        }
    }

    private void follow(int position){
        Http.http.createApi(MineAPI.class)
                .follow(((Answer)list.get(position)).getMaster_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        ((Answer)list.get(position)).setFollow(1);
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
