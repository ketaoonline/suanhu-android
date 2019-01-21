package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.RecommendAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

public class MasterRecommendActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rg)
    RadioGroup rg;

    public static void open(AppActivity activity, String questionId) {
        Bundle bundle = new Bundle();
        bundle.putString("questionId", questionId);
        activity.startActivity(bundle, MasterRecommendActivity.class);
    }

    public static void open(AppActivity activity,int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        activity.startActivity(bundle, MasterRecommendActivity.class);
    }

    RecommendAdapter adapter;
    List<Master> list = new ArrayList<>();
    String questionId = "";
    int type;
    @Override
    protected int initLayout() {
        return R.layout.activity_master_recommend;
    }

    @Override
    protected String setTitleStr() {
        return "大师推荐";
    }

    @Override
    protected void init() {
        questionId = getIntent().getStringExtra("questionId");
        type = getIntent().getIntExtra("type",-1);

        rg.check(R.id.rb_1);
        rg.setOnCheckedChangeListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecommendAdapter(this, list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        recommend(1);
    }


    @OnClick({R.id.btn})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.btn:
                if (adapter.checkPosition == -1) {
                    showMessage("请选择大师");
                    return;
                }
                int id = list.get(adapter.checkPosition).getId();
                if (TextUtils.isEmpty(questionId)) {
                    putDetailedOrder(id,type);
                } else {
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("master_id", id + "");
                    map1.put("question_id", questionId);
                    submit(map1);
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        MasterIntroduceActivity.open(this, list.get(position).getId());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter mAdapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_check_status:
                adapter.setCheckPosition(position);
                break;
        }
    }

    private void submit(Map<String, String> map) {
        Http.http.createApi(CalcAPI.class)
                .putQuestionOrder(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        MasterOrderDetailActivity.open(MasterRecommendActivity.this, result);
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
                        MasterOrderDetailActivity.open(MasterRecommendActivity.this, result);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    private void recommend(int order_type) {
        Map<String,String> map = new HashMap<>();
        map.put("order_type",order_type+"");
        map.put("type",type+"");
        Http.http.createApi(CalcAPI.class)
                .getRecommendMasters(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Master>>() {
                    @Override
                    public void success(List<Master> result) {
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_1:
                recommend(1);
                break;
            case R.id.rb_2:
                recommend(2);
                break;
            case R.id.rb_3:
                recommend(3);
                break;
        }
    }
}
