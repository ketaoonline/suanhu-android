package com.bunnybear.suanhu.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.ui.fragment.CalcFragment;
import com.bunnybear.suanhu.ui.fragment.ClassFragment;
import com.bunnybear.suanhu.ui.fragment.MainFragment;
import com.bunnybear.suanhu.ui.fragment.MineFragment;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.view.CustomViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppActivity {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.iv_calc)
    ImageView ivCalc;
    @BindView(R.id.tv_calc)
    TextView tvCalc;
    @BindView(R.id.iv_class)
    ImageView ivClass;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.tab_main)
    LinearLayout tabMain;
    @BindView(R.id.tab_calc)
    LinearLayout tabCalc;
    @BindView(R.id.tab_class)
    LinearLayout tabClass;
    @BindView(R.id.tab_mine)
    LinearLayout tabMine;

    public static void open(AppActivity activity) {
        activity.startActivity(null, MainActivity.class);
    }

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MainFragment mainFragment;
    private CalcFragment calculationFragment;
    private ClassFragment classFragment;
    private MineFragment mineFragment;

    private int currTab;
    int[] selecteds = {R.mipmap.main_checked, R.mipmap.calc_checked, R.mipmap.class_checked, R.mipmap.mine_checked};
    int[] unSelects = {R.mipmap.mine_normal, R.mipmap.calc_normal, R.mipmap.class_normal, R.mipmap.mine_normal};

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
        mainFragment = MainFragment.newInstance();
        calculationFragment = CalcFragment.newInstance();
        classFragment = ClassFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        mFragments.add(mainFragment);
        mFragments.add(calculationFragment);
        mFragments.add(classFragment);
        mFragments.add(mineFragment);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setScroll(false);
        tabMain.setSelected(true);
    }


    @OnClick({R.id.tab_main, R.id.tab_calc, R.id.tab_class, R.id.tab_mine, R.id.iv_conversation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_main:
                viewPager.setCurrentItem(0);
                updateTabShow(tabMain);
                break;
            case R.id.tab_calc:
                viewPager.setCurrentItem(1);
                updateTabShow(tabCalc);
                break;
            case R.id.tab_class:
                viewPager.setCurrentItem(2);
                updateTabShow(tabClass);
                break;
            case R.id.tab_mine:
                viewPager.setCurrentItem(3);
                updateTabShow(tabMine);
                break;
            case R.id.iv_conversation:
                ConversationListActivity.open(this,0);
                break;
        }
    }

    private void updateTabShow(LinearLayout linearLayout) {
        int textNormalColor = getResources().getColor(R.color.main_text_normal);
        int textCheckedColor = getResources().getColor(R.color.main_text_checked);
        ivMain.setImageResource(R.mipmap.main_normal);
        ivCalc.setImageResource(R.mipmap.calc_normal);
        ivClass.setImageResource(R.mipmap.class_normal);
        ivMine.setImageResource(R.mipmap.mine_normal);
        tvMain.setTextColor(textNormalColor);
        tvCalc.setTextColor(textNormalColor);
        tvClass.setTextColor(textNormalColor);
        tvMine.setTextColor(textNormalColor);

        switch (linearLayout.getId()) {
            case R.id.tab_main:
                ivMain.setImageResource(R.mipmap.main_checked);
                tvMain.setTextColor(textCheckedColor);
                break;
            case R.id.tab_calc:
                ivCalc.setImageResource(R.mipmap.calc_checked);
                tvCalc.setTextColor(textCheckedColor);
                break;
            case R.id.tab_class:
                ivClass.setImageResource(R.mipmap.class_checked);
                tvClass.setTextColor(textCheckedColor);
                break;
            case R.id.tab_mine:
                ivMine.setImageResource(R.mipmap.mine_checked);
                tvMine.setTextColor(textCheckedColor);
                break;
        }
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
        } else {
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
        switch (msgCode) {
            case ConstData.TOKEN_INVALID:
                showMessage("登录已失效，请重新登录");
            case ConstData.LOGIN_OUT:
                ActivityManager.getInstance().finishOtherActivity(MainActivity.class);
                Hawk.delete(ConstData.TOKEN);
                LoginActivity.open(this);
                finish();
                break;
            case "Conversation":
                ActivityManager.getInstance().finishActivity(MasterOrderDetailActivity.class);
                ActivityManager.getInstance().finishActivity(MasterRecommendActivity.class);
                ActivityManager.getInstance().finishActivity(PutQuestionActivityNew.class);
                ActivityManager.getInstance().finishActivity(AssortmentActivity.class);
                ActivityManager.getInstance().finishActivity(MasterIntroduceActivity.class);
                boolean isSample = Hawk.get("isSample");
                ConversationListActivity.open(this, isSample ? 0 : 1);
                break;
            case "Class":
                ActivityManager.getInstance().finishActivity(OrderDetailActivity.class);
                ActivityManager.getInstance().finishActivity(ShopCarActivity.class);
                ActivityManager.getInstance().finishActivity(ClassDetailActivity.class);
                tabClass.performClick();
                classFragment.tabLayout.setCurrentTab(1);
                classFragment.viewPager.setCurrentItem(1);
                break;
        }
    }




}
