package com.bunnybear.suanhu.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.ui.adapter.ViewPagerAdapter;
import com.bunnybear.suanhu.ui.fragment.CalcOrderFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MyCalcOrderListActivity extends AppActivity {


    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity appActivity){
        appActivity.startActivity(null,MyCalcOrderListActivity.class);
    }

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    int currentPosition = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_calc_order_list;
    }

    @Override
    protected String setTitleStr() {
        return "我的测算订单";
    }

    @Override
    protected void init() {
        fragmentList.add(CalcOrderFragment.newInstance(0));
        fragmentList.add(CalcOrderFragment.newInstance(2));
        fragmentList.add(CalcOrderFragment.newInstance(1));

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
                currentPosition = position;
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentList.get(currentPosition) instanceof CalcOrderFragment){
            CalcOrderFragment fragment = (CalcOrderFragment) fragmentList.get(currentPosition);
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}
