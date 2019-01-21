package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.Chapter;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.bean.Lesson;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.ChatAdapter;
import com.bunnybear.suanhu.ui.adapter.ClassDetailCatalogueAdater;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DensityUtil;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassDetailCatalogueFragment extends ScrollContainerFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static ClassDetailCatalogueFragment newInstance(int id) {
        ClassDetailCatalogueFragment fragment = new ClassDetailCatalogueFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<Chapter> list = new ArrayList<>();
    ClassDetailCatalogueAdater adater;

    @Override
    protected int initLayout() {
        return R.layout.fragment_class_detail_catalogue;
    }

    @Override
    protected void lazyLoad() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"),2, DensityUtil.dp2px(mActivity,1)));

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
                .getClassDetailCatalogue(getArguments().getInt("id"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Chapter>>() {
                    @Override
                    public void success(List<Chapter> result) {
                        for (Chapter chapter : result) {
                            Chapter groupChapter = new Chapter(chapter.getChaption(),chapter.getInfo());
                            list.add(groupChapter);
                        }
                        adater = new ClassDetailCatalogueAdater(mActivity,list);
                        recyclerView.setAdapter(adater);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
