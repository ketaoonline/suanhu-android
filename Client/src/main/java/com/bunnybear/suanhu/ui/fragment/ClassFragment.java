package com.bunnybear.suanhu.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.ui.activity.MainActivity;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.SegmentTabLayout;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.kingja.switchbutton.SwitchMultiButton;

public class ClassFragment extends AppFragment implements OnTabSelectListener {


    @BindView(R.id.tabLayout)
    public SegmentTabLayout tabLayout;
    @BindView(R.id.viewPager)
    public CustomViewPager viewPager;

    public static ClassFragment newInstance() {
        ClassFragment fragment = new ClassFragment();
        return fragment;
    }

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ClassMainFragment classMainFragment;
    private ClassMyLearnFragment classMyLearnFragment;

    String[] titles = {"首页","我的学习"};
    @Override
    protected int initLayout() {
        return R.layout.fragment_class;
    }

    @Override
    protected void lazyLoad() {
        initView();
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(this);

    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }


    private void initView() {
        classMainFragment = ClassMainFragment.newInstance();
        classMyLearnFragment = ClassMyLearnFragment.newInstance();
        mFragments.add(classMainFragment);
        mFragments.add(classMyLearnFragment);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onTabSelect(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
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


}
