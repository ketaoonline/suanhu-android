package com.bunnybear.suanhu.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ClassAssortmentAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassAssortmentActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static void open(AppActivity activity) {
        activity.startActivity(null, ClassAssortmentActivity.class);
    }

    ClassAssortmentAdapter assortmentAdapter;
    List<ClassType> list = new ArrayList<>();
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
        refreshLayout.setPureScrollModeOn();
        rv.setLayoutManager(new LinearLayoutManager(this));
        assortmentAdapter = new ClassAssortmentAdapter(list);
        assortmentAdapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(assortmentAdapter);
        assortmentAdapter.setOnItemClickListener(this);

        getData();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClassListActivity.open(this,list.get(position).getId(),list.get(position).getTag());
    }

    private void getData() {
        Http.http.createApi(ClassAPI.class)
                .getClassTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ClassType>>() {
                    @Override
                    public void success(List<ClassType> result) {
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

}
