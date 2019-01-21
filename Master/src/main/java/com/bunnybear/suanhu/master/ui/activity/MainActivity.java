package com.bunnybear.suanhu.master.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.base.ConstData;
import com.bunnybear.suanhu.master.ui.fragment.CalculationFragment;
import com.bunnybear.suanhu.master.ui.fragment.InterlocutionFragment;
import com.bunnybear.suanhu.master.ui.fragment.MineFragment;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppActivity implements OnTabSelectListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;

    public static void open(AppActivity activity) {
        activity.startActivity(null, MainActivity.class);
    }

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private CalculationFragment calculationFragment;
    private InterlocutionFragment interlocutionFragment;
    private MineFragment mineFragment;

    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
//    String[] titles = {"测算","问答","个人中心"};
    String[] titles = {"测算","个人中心"};
//    int[] selecteds = {R.mipmap.calc_checked,R.mipmap.interlocution_checked,R.mipmap.mine_checked};
//    int[] unSelects = {R.mipmap.calc_normal,R.mipmap.interlocution_normal,R.mipmap.mine_normal};

    int[] selecteds = {R.mipmap.calc_checked,R.mipmap.mine_checked};
    int[] unSelects = {R.mipmap.calc_normal,R.mipmap.mine_normal};

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected String setTitleStr() {
        return "算乎";
    }

    @Override
    protected void init() {
        hideBaseTitleRelative();
        initView();
    }

    private void initView() {
        calculationFragment = CalculationFragment.newInstance();
//        interlocutionFragment = InterlocutionFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        mFragments.add(calculationFragment);
//        mFragments.add(interlocutionFragment);
        mFragments.add(mineFragment);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setScroll(false);

        for (int i = 0; i < mFragments.size(); i++) {
            customTabEntities.add(new TabEntity(titles[i],selecteds[i],unSelects[i]));
        }
        commonTabLayout.setTabData(customTabEntities);
        commonTabLayout.setOnTabSelectListener(this);
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


    private long firstTime = 0;
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            showMessage("再按一次退出程序");
            firstTime = secondTime;
        } else{
            ActivityManager.getInstance().finishAllActivity();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        switch (msgCode){
            case ConstData.TOKEN_INVALID:
                showMessage("登录已失效，请重新登录");
                ActivityManager.getInstance().finishOtherActivity(MainActivity.class);
                Hawk.delete(ConstData.TOKEN);
                LoginActivity.open(this);
                finish();
                break;
            case ConstData.LOGIN_OUT:
                ActivityManager.getInstance().finishOtherActivity(MainActivity.class);
                Hawk.delete(ConstData.TOKEN);
                LoginActivity.open(this);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    calculationFragment.onActivityResult(requestCode,resultCode,data);
                    break;
                case 1001:
//                    interlocutionFragment.onActivityResult(requestCode,resultCode,data);
                    break;
            }
        }
    }
}
