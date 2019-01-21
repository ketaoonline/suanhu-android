package com.bunnybear.suanhu.ui.activity;

import android.support.v4.app.Fragment;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.ui.adapter.ViewPagerAdapter;
import com.bunnybear.suanhu.ui.fragment.CollectArticleFragment;
import com.bunnybear.suanhu.ui.fragment.CollectClassFragment;
import com.bunnybear.suanhu.ui.fragment.CollectMasterFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MyCollectActivity extends AppActivity {
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity activity){
        activity.startActivity(null,MyCollectActivity.class);
    }

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
    @Override
    protected int initLayout() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected String setTitleStr() {
        return "我的收藏";
    }

    @Override
    protected void init() {
        fragmentList.add(CollectClassFragment.newInstance());
        fragmentList.add(CollectMasterFragment.newInstance());

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragmentList));
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);

        String[] mTitles = {"课程","大师"};
        for (int i = 0; i < mTitles.length; i++) {
            customTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(customTabEntities);
        commonTabLayout.setIndicatorWidth(40f);
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
