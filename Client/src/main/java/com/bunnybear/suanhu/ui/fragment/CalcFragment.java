package com.bunnybear.suanhu.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.Article;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.MasterResponse;
import com.bunnybear.suanhu.bean.Masters;
import com.bunnybear.suanhu.bean.TestBigType;
import com.bunnybear.suanhu.bean.TestBigTypeResponse;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.AssortmentActivity;
import com.bunnybear.suanhu.ui.activity.MasterIntroduceActivity;
import com.bunnybear.suanhu.ui.activity.SearchMasterActivity;
import com.bunnybear.suanhu.ui.adapter.CalcAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CalcFragment extends AppFragment implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static CalcFragment newInstance() {
        CalcFragment fragment = new CalcFragment();
        return fragment;
    }

    List<MainBaseBean> list = new ArrayList<>();
    CalcAdapter adapter;

    int masterPosition = 11;

    @Override
    protected int initLayout() {
        return R.layout.fragment_calculation;
    }

    @Override
    protected void lazyLoad() {
        twRefreshLayout.setEnableLoadmore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new CalcAdapter(mActivity, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);

        twRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                masterPosition = 11;
                getData();
            }
        });

        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }


    private void getData(){
        list.clear();
        list.add(new MainBaseBean(4));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(1));
        list.add(new MainBaseBean(2));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(5));
        list.add(new MainBaseBean(2));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(5));
        list.add(new MainBaseBean(2));
        list.add(new MainBaseBean(0));
//        list.add(new MainBaseBean(3));
        getTestBigTypes();
        getHRecommendMasters();
        getRecommendMasters();
    }



    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.rl_search:
                SearchMasterActivity.open(mActivity);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (list.get(position) instanceof Master) {
            Master master = (Master) list.get(position);
            MasterIntroduceActivity.open(mActivity, master.getId());
        }
    }

    private void getHRecommendMasters() {
        Http.http.createApi(CalcAPI.class)
                .getHMasters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<MasterResponse>() {
                    @Override
                    public void success(MasterResponse result) {
                        Masters masters1 = new Masters(5, result.getNew_master());
                        Masters masters2 = new Masters(5, result.getHot_master());
                        list.remove(5);
                        list.add(5, masters1);
                        list.remove(8);
                        list.add(8, masters2);
                        adapter.notifyItemChanged(5);
                        adapter.notifyItemChanged(8);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    private void getRecommendMasters() {
        Http.http.createApi(CalcAPI.class)
                .getRecommendMaster()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Master>>() {
                    @Override
                    public void success(List<Master> result) {
                        for (Master master : result) {
                            master.setViewType(3);
//                            if (masterPosition == 11) {
//                                list.remove(masterPosition);
//                            }
                            list.add(masterPosition, master);
                            masterPosition++;
                        }
                        adapter.setNewData(list);
                        twRefreshLayout.finishRefreshing();

                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void getTestBigTypes() {
        Http.http.createApi(CalcAPI.class)
                .getTestBigTypes(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<TestBigType>>() {
                    @Override
                    public void success(List<TestBigType> result) {
                        TestBigTypeResponse response = new TestBigTypeResponse(1, result);
                        list.remove(2);
                        list.add(2, response);
                        adapter.notifyItemChanged(2);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


}
