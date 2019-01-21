package com.bunnybear.suanhu.ui.activity;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Coupon;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ExchangeCouponAdatper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.xiaoxiong.library.view.NormalDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExchangeCouponActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void open(AppActivity activity) {
        activity.startActivity(null, ExchangeCouponActivity.class);
    }

    ExchangeCouponAdatper adatper;
    List<Coupon> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_coupon;
    }

    @Override
    protected String setTitleStr() {
        return "兑换卡券";
    }

    @Override
    protected void init() {
        twRefreshLayout.setPureScrollModeOn();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adatper = new ExchangeCouponAdatper(list);
        adatper.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adatper);
        adatper.setOnItemClickListener(this);
        getData();

    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getExchangeCoupons()
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

    private void exchange(int id){
        Http.http.createApi(MineAPI.class)
                .exchangeCoupon(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("兑换成功");
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showDialog(position,"确定兑换该卡券？");
    }

    private void showDialog(final int position, String message) {
        NormalDialog.Builder builder = new NormalDialog.Builder(this);
        builder.setMessage(message);
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                exchange(list.get(position).getId());
            }

        }).setCancelBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create().show();
    }
}
