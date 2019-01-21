package com.bunnybear.suanhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Coupon;
import com.bunnybear.suanhu.bean.QuestionType;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.CouponAdatper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void open(AppActivity activity) {
        activity.startActivity(null, CouponActivity.class);
    }

    public static void openForResult(AppActivity activity,int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderId",orderId);
        activity.startForResult(bundle,1000, CouponActivity.class);
    }

    CouponAdatper adatper;
    List<Coupon> list = new ArrayList<>();

    int orderId;

    @Override
    protected int initLayout() {
        return R.layout.activity_coupon;
    }

    @Override
    protected String setTitleStr() {
        orderId = getIntent().getIntExtra("orderId",0);
        String title = "";
        if(orderId > 0){
            title = "选择优惠券";
            getBtnRight().setText("暂不使用");
        }else {
            title = "我的卡券";
            getBtnRight().setText("去兑换");
        }
        return title;
    }

    @Override
    protected void init() {
        twRefreshLayout.setPureScrollModeOn();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adatper = new CouponAdatper(list);
        adatper.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adatper);
        adatper.setOnItemClickListener(this);

    }


    @Override
    protected void rightButtonClick() {
        if ("暂不使用".equals(getBtnRight().getText().toString())){
            setResult(RESULT_OK);
            finish();
        }else {
            ExchangeCouponActivity.open(this);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        Map<String,String> map = new HashMap<>();
        if (orderId > 0){
            map.put("order_id",orderId+"");
        }
        Http.http.createApi(MineAPI.class)
                .getCoupons(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Coupon>>() {
                    @Override
                    public void success(List<Coupon> result) {
                        list.clear();
                        list.addAll(result);
                        adatper.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (orderId > 0){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("coupon",list.get(position));
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
