package com.bunnybear.suanhu.master.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.SegmentTabLayout;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InterlocutionFragment extends AppFragment implements OnTabSelectListener {

    @BindView(R.id.tabLayout)
    SegmentTabLayout tabLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static InterlocutionFragment newInstance() {
        InterlocutionFragment fragment = new InterlocutionFragment();

        return fragment;
    }
    QuestionFragment questionFragment1,questionFragment2;

    private List<Fragment> mFragment = new ArrayList<>();
    String[] titles = {"未处理","已处理"};

    @Override
    protected int initLayout() {
        return R.layout.fragment_interlocution;
    }

    @Override
    protected void lazyLoad() {
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(this);

        initViewPager();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    private void initViewPager() {
        questionFragment1 = QuestionFragment.newInstance(false);
        questionFragment2 = QuestionFragment.newInstance(true);
        mFragment.add(questionFragment1);
        mFragment.add(questionFragment2);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(mFragment.size());
        viewPager.setScroll(false);
    }


    @Override
    public void onTabSelect(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK){
            switch (requestCode){
                case 1001:
                    questionFragment1.onActivityResult(requestCode,resultCode,data);
                    questionFragment2.onActivityResult(requestCode,resultCode,data);
                    break;
            }
        }
    }
}
