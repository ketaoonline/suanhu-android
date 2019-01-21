package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Question;
import com.bunnybear.suanhu.bean.QuestionType;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.AssortmentAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AssortmentActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.ll)
    LinearLayout linearLayout;

    public static void open(AppActivity activity,int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        activity.startActivity(bundle, AssortmentActivity.class);
    }

    public static void open(AppActivity activity,int type,int masterId) {
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putInt("masterId",masterId);
        activity.startActivity(bundle, AssortmentActivity.class);
    }

    AssortmentAdapter assortmentAdapter;
    List<QuestionType> list = new ArrayList<>();
    int type = 1;
    int masterId;
    @Override
    protected int initLayout() {
        return R.layout.layout_list;
    }

    @Override
    protected String setTitleStr() {
        return "分类";
    }

    @Override
    protected void init() {


        Hawk.put("isSample",false);
        boolean isFirstChooseMaster = Hawk.get("isFirstChooseMaster",false);
        type = getIntent().getIntExtra("type",-1);
        masterId = getIntent().getIntExtra("masterId",-1);


        int resBg = 0;
        switch (type){
            case 1:
                resBg = R.mipmap.bg_1;
                break;
            case 2:
                resBg = R.mipmap.bg_2;
                break;
            case 3:
                resBg = R.mipmap.bg_3;
                break;
            case 4:
                resBg = R.mipmap.bg_4;
                break;
        }
        linearLayout.setBackgroundResource(resBg);


        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(this));
        assortmentAdapter = new AssortmentAdapter(list,isFirstChooseMaster);
        assortmentAdapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(assortmentAdapter);
        assortmentAdapter.setOnItemClickListener(this);

        getData();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (masterId > 0){
            putDetailedOrder(masterId,list.get(position).getQuestion_type_id());
        }else {
            MasterRecommendActivity.open(this,list.get(position).getQuestion_type_id());
        }
    }

    private void getData() {
        Map<String,String> map = new HashMap<>();
        map.put("type",type+"");
        if (masterId > 0){
            map.put("master_id",masterId+"");
        }
        Http.http.createApi(CalcAPI.class)
                .getQuestionTypes(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<QuestionType>>() {
                    @Override
                    public void success(List<QuestionType> result) {
                        list.clear();
                        list.addAll(result);
                        assortmentAdapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    /**
     * 详测下单
     */
    private void putDetailedOrder(int masterId,int type) {
        Http.http.createApi(CalcAPI.class)
                .putDetailedTestOrder(masterId,type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        MasterOrderDetailActivity.open(AssortmentActivity.this, result);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
