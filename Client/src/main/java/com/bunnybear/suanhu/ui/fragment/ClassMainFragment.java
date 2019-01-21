package com.bunnybear.suanhu.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.bean.ClassBanner;
import com.bunnybear.suanhu.bean.ClassType;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.ClassAssortmentActivity;
import com.bunnybear.suanhu.ui.activity.ClassDetailActivity;
import com.bunnybear.suanhu.ui.activity.ClassListActivity;
import com.bunnybear.suanhu.ui.activity.SearchClassActivity;
import com.bunnybear.suanhu.ui.activity.SearchMasterActivity;
import com.bunnybear.suanhu.ui.adapter.BannerAdapter;
import com.bunnybear.suanhu.ui.adapter.TagClassAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.banner.BannerLayout;
import com.xiaoxiong.library.view.headerviewpager.HeaderViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassMainFragment extends AppFragment implements RadioGroup.OnCheckedChangeListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.recycler)
    BannerLayout recyclerBanner;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;


    public static ClassMainFragment newInstance() {
        ClassMainFragment fragment = new ClassMainFragment();
        return fragment;
    }
    List<ClassType> list = new ArrayList<>();
    TagClassAdapter adapter;
    private ArrayList<ScrollContainerFragment> mFragments = new ArrayList<>();

    ClassMainItemFragment classMainItemFragment1, classMainItemFragment2, classMainItemFragment3;

    @Override
    protected int initLayout() {
        return R.layout.fragment_class_main;
    }

    @Override
    protected void lazyLoad() {
        rb1.setChecked(true);

        getData();
        rg.setOnCheckedChangeListener(this);
        initView();
        getClassTypes();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    private void initView() {
        rv.setLayoutManager(new GridLayoutManager(mActivity,4));
        adapter = new TagClassAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        classMainItemFragment1 = ClassMainItemFragment.newInstance("精选好课");
        classMainItemFragment2 = ClassMainItemFragment.newInstance("新增好课");
        classMainItemFragment3 = ClassMainItemFragment.newInstance("免费好课");
        mFragments.add(classMainItemFragment1);
        mFragments.add(classMainItemFragment2);
        mFragments.add(classMainItemFragment3);
        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb1.setChecked(true);
                        break;
                    case 1:
                        rb2.setChecked(true);
                        break;
                    case 3:
                        rb3.setChecked(true);
                        break;
                }
                scrollableLayout.setCurrentScrollableContainer(mFragments.get(position));
            }

        });
        scrollableLayout.setCurrentScrollableContainer(mFragments.get(0));


    }


    private void initBanner(List<ClassBanner> classBannerList){
        if (classBannerList != null && classBannerList.size() > 0) {
            List<String> list = new ArrayList<>();
            for (ClassBanner classBanner : classBannerList) {
                list.add(classBanner.getHead_banner());
            }
            BannerAdapter webBannerAdapter = new BannerAdapter(list);
            recyclerBanner.setAdapter(webBannerAdapter);
            webBannerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ClassDetailActivity.open(mActivity,classBannerList.get(position).getId());
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_3:
                viewPager.setCurrentItem(2);
                break;
        }

    }

    @OnClick({R.id.rl_search})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.rl_search:
                SearchClassActivity.open(mActivity);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == 3){
            ClassAssortmentActivity.open(mActivity);
        }else {
            ClassListActivity.open(mActivity,list.get(position).getId(),list.get(position).getTag());
        }
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private void getData(){
        Http.http.createApi(ClassAPI.class)
                .getClassBanner()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ClassBanner>>() {
                    @Override
                    public void success(List<ClassBanner> result) {
                        initBanner(result);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    private void getClassTypes() {
        Http.http.createApi(ClassAPI.class)
                .getClassTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ClassType>>() {
                    @Override
                    public void success(List<ClassType> result) {
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

}
