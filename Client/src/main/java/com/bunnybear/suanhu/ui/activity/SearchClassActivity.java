package com.bunnybear.suanhu.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Master;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.SearchClassAdapter;
import com.bunnybear.suanhu.ui.adapter.SearchMasterAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchClassActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener, TextWatcher {

    @BindView(R.id.et)
    ClearEditText et;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void open(AppActivity activity) {
        activity.startActivity(null, SearchClassActivity.class);
    }
    List<SClass> list = new ArrayList<>();
    SearchClassAdapter adapter;
    @Override
    protected int initLayout() {
        return R.layout.activity_search_class;
    }

    @Override
    protected String setTitleStr() {
        return "搜索";
    }

    @Override
    protected void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchClassAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        et.addTextChangedListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClassDetailActivity.open(this,list.get(position).getId());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0){
            String key = charSequence.toString();
            getData(key);
        }else {
            list.clear();
            adapter.setNewData(list);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void getData(String key){
        Http.http.createApi(CalcAPI.class)
                .searchClasses(key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SClass>>() {
                    @Override
                    public void success(List<SClass> result) {
                        list.clear();
                        list = result;
                        adapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
