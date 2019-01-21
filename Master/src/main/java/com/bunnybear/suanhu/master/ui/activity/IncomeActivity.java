package com.bunnybear.suanhu.master.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.bean.DetailedTest;
import com.bunnybear.suanhu.master.bean.Income;
import com.bunnybear.suanhu.master.bean.IncomeResponse;
import com.bunnybear.suanhu.master.bean.MainBaseBean;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.ui.adapter.IncomeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IncomeActivity extends AppActivity implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static void open(AppActivity activity){
        activity.startActivity(null,IncomeActivity.class);
    }

    List<MainBaseBean> list = new ArrayList<>();
    IncomeAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected String setTitleStr() {
        return "收入";
    }

    @Override
    protected void init() {
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncomeAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.btn_encash:
                if (list.get(position) instanceof IncomeResponse){
                    IncomeResponse incomeResponse = (IncomeResponse) list.get(position);
                    if (TextUtils.isEmpty(incomeResponse.getBank_number())){
                        showMessage("请先绑定银行卡");
                        return;
                    }
                    EncashActivity.open(this,incomeResponse.getBank_number());
                }

                break;
            case R.id.rl:
                if (list.get(position) instanceof IncomeResponse){
                    IncomeResponse incomeResponse = (IncomeResponse) list.get(position);
                    BindBankCardActivity.open(this,incomeResponse.getBank_number());
                }
                break;
        }
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getIncomeData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<IncomeResponse>() {
                    @Override
                    public void success(IncomeResponse result) {
                        list.clear();
                        list.add(result);
                        if (result.getInfo().size() > 0){
                            list.add(new MainBaseBean(1));
                            for (Income income : result.getInfo()) {
                                income.setViewType(2);
                                list.add(income);
                            }
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
