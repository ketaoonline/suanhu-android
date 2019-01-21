package com.bunnybear.suanhu.master.ui.activity;

import android.support.v4.app.Fragment;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.ui.adapter.ViewPagerAdapter;
import com.bunnybear.suanhu.master.ui.fragment.OrderFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class OrderActivity extends AppActivity {

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity activity){
        activity.startActivity(null,OrderActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_calc_order_list;
    }

    @Override
    protected String setTitleStr() {
        return "订单";
    }

    @Override
    protected void init() {
//        fragmentList.add(OrderFragment.newInstance(0));
        fragmentList.add(OrderFragment.newInstance(1));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);

        String[] mTitles = {"简测订单","详测订单"};
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
