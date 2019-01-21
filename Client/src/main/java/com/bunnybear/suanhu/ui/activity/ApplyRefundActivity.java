package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ClassOrder;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ReasonAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.http.RequestCallBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyRefundActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_remark)
    EditText etRemark;

    public static void open(AppActivity activity,int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderId",orderId);
        activity.startForResult(bundle, 1000, ApplyRefundActivity.class);
    }

    ReasonAdapter reasonAdapter;
    List<String> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_apply_refund;
    }

    @Override
    protected String setTitleStr() {
        return "退款申请";
    }

    @Override
    protected void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        reasonAdapter = new ReasonAdapter(list);
        recyclerView.setAdapter(reasonAdapter);

        reasonAdapter.setOnItemClickListener(this);

        getReasons();
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        reasonAdapter.setCheckPosition(position);
    }

    private void submit(){
        if (reasonAdapter.checkPosition == -1){
            showMessage("请选择退款原因");
            return;
        }
        String remark = etRemark.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("order_id",getIntent().getIntExtra("orderId",-1)+"");
        map.put("reson",list.get(reasonAdapter.checkPosition));
        if (!TextUtils.isEmpty(remark)){
            map.put("mark",remark);
        }
        Http.http.createApi(MineAPI.class)
                .applyRefund(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("您的申请提交成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void getReasons(){
        Http.http.createApi(MineAPI.class)
                .getReasons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<String>>() {
                    @Override
                    public void success(List<String> result) {
                        list.clear();
                        list.addAll(result);
                        reasonAdapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
