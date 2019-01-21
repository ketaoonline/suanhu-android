package com.bunnybear.suanhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.ui.fragment.MyConsultFragment;
import com.bunnybear.suanhu.ui.fragment.MyQuestionFragment;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.SegmentTabLayout;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ConversationListActivity extends AppActivity implements OnTabSelectListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.tabLayout)
    SegmentTabLayout tabLayout;

    public static void open(AppActivity activity,int currentPosition){
        Bundle bundle = new Bundle();
        bundle.putInt("currentPosition",currentPosition);
        activity.startActivity(bundle,ConversationListActivity.class);
    }

    public static void open(Context context, int currentPosition){
        Intent intent = new Intent(context,ConversationListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("currentPosition",currentPosition);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    private List<Fragment> mFragment = new ArrayList<>();
    String[] titles = {"我的问答","我的咨询"};
    MyQuestionFragment myQuestionFragment;
    MyConsultFragment myConsultFragment;

    int currentPosition = 0;
    @Override
    protected int initLayout() {
        return R.layout.activity_conversation_list;
    }

    @Override
    protected String setTitleStr() {
        return "我的咨询";
    }

    @Override
    protected void init() {
        currentPosition = getIntent().getIntExtra("currentPosition",0);

//        hideBaseTitleRelative();
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(this);

        initViewPager();

    }

    private void initViewPager() {
//        myQuestionFragment = MyQuestionFragment.newInstance();
        myConsultFragment = MyConsultFragment.newInstance();
//        mFragment.add(myQuestionFragment);
        mFragment.add(myConsultFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
//        viewPager.setCurrentItem(currentPosition);
//        tabLayout.setCurrentTab(currentPosition);
    }

    @OnClick({R.id.ivLeft})
    public void onViewClick(View view){
        if(antiShake.check(view.getId()))return;
        switch (view.getId()){
            case R.id.ivLeft:
                finish();
                break;
        }
    }


    @Override
    public void onTabSelect(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    myConsultFragment.onActivityResult(requestCode,resultCode,data);
                    break;
                case 1001:
//                    myQuestionFragment.onActivityResult(requestCode,resultCode,data);
                    break;
            }

        }
    }
}
