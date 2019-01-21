package com.bunnybear.suanhu.ui.activity;

import android.support.v4.app.Fragment;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.ui.adapter.ViewPagerAdapter;
import com.bunnybear.suanhu.ui.fragment.ArticleFragment;
import com.bunnybear.suanhu.ui.fragment.CollectArticleFragment;
import com.bunnybear.suanhu.ui.fragment.CollectClassFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class ArticleActivity extends AppActivity {
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity activity){
        activity.startActivity(null,ArticleActivity.class);
    }

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArticleFragment articleFragment1,articleFragment2,articleFragment3,articleFragment4;
    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_article;
    }

    @Override
    protected String setTitleStr() {
        return "文章";
    }

    @Override
    protected void init() {
        articleFragment1 = ArticleFragment.newInstance(2);
        articleFragment2 = ArticleFragment.newInstance(4);
        articleFragment3 = ArticleFragment.newInstance(3);
        articleFragment4 = ArticleFragment.newInstance(1);

        fragmentList.add(articleFragment1);
        fragmentList.add(articleFragment2);
        fragmentList.add(articleFragment3);
        fragmentList.add(articleFragment4);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragmentList));
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);

        String[] mTitles = {"预测","起名","择吉","风水"};
        for (int i = 0; i < mTitles.length; i++) {
            customTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(customTabEntities);
        commonTabLayout.setIndicatorWidth(20f);
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
