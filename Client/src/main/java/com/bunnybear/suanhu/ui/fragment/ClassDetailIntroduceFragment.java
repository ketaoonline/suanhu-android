package com.bunnybear.suanhu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ClassDetailIntroduceAdapter;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassDetailIntroduceFragment extends ScrollContainerFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static ClassDetailIntroduceFragment newInstance(int id) {
        ClassDetailIntroduceFragment fragment = new ClassDetailIntroduceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<MainBaseBean> list = new ArrayList<>();
    ClassDetailIntroduceAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_class_detail_introduce;
    }

    @Override
    protected void lazyLoad() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        adapter = new ClassDetailIntroduceAdapter(mActivity,list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);

        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }


    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    private void getData(){
        Http.http.createApi(ClassAPI.class)
                .getClassDetailIntroduce(getArguments().getInt("id"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<ClassDetailIntroduce>() {
                    @Override
                    public void success(ClassDetailIntroduce result) {
                        result.setViewType(0);
                        list.add(result);
                        if (result.getBanner() != null) {
                            for (String s : result.getBanner()) {
                                ImageBean imageBean = new ImageBean(1,s);
                                list.add(imageBean);
                            }
                        }
                        adapter.setNewData(list);
                        BusFactory.getBus().post(new IEvent("ClassIntroduce", result));
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
