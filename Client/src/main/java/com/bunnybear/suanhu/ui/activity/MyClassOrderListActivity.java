package com.bunnybear.suanhu.ui.activity;

import android.support.v4.app.Fragment;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.ui.adapter.ViewPagerAdapter;
import com.bunnybear.suanhu.ui.fragment.CalcOrderFragment;
import com.bunnybear.suanhu.ui.fragment.ClassOrderFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MyClassOrderListActivity extends AppActivity {


    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity appActivity){
        appActivity.startActivity(null,MyClassOrderListActivity.class);
    }

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_calc_order_list;
    }

    @Override
    protected String setTitleStr() {
        return "我的课程订单";
    }

    @Override
    protected void init() {
        fragmentList.add(ClassOrderFragment.newInstance(0));
        fragmentList.add(ClassOrderFragment.newInstance(2));
        fragmentList.add(ClassOrderFragment.newInstance(1));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);

        String[] mTitles = {"全部订单", "待支付","交易成功"};
        for (int i = 0; i < mTitles.length; i++) {
            customTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(customTabEntities);
        commonTabLayout.setIndicatorWidth(60f);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

}
