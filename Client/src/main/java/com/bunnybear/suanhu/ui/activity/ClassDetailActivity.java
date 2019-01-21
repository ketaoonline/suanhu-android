package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.fragment.ClassDetailCatalogueFragment;
import com.bunnybear.suanhu.ui.fragment.ClassDetailCommentFragment;
import com.bunnybear.suanhu.ui.fragment.ClassDetailIntroduceFragment;
import com.bunnybear.suanhu.ui.fragment.ClassMainFragment;
import com.bunnybear.suanhu.ui.fragment.ClassMainItemFragment;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.CustomViewPager;
import com.xiaoxiong.library.view.headerviewpager.HeaderViewPager;
import com.xiaoxiong.library.view.scrollablelayoutlib.ScrollableLayout;
import com.xiaoxiong.library.view.tablayout.CommonTabLayout;
import com.xiaoxiong.library.view.tablayout.TabEntity;
import com.xiaoxiong.library.view.tablayout.listener.CustomTabEntity;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.FieldMap;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassDetailActivity extends AppActivity {
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    public static void open(AppActivity activity, int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        activity.startActivity(bundle, ClassDetailActivity.class);
    }

    ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
    ArrayList<ScrollContainerFragment> fragments = new ArrayList<>();
    String[] titles = {"介绍", "目录", "评价"};
    int id,type = -1;
    boolean isCollected;

    @Override
    protected int initLayout() {
        return R.layout.activity_class_detail;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {

        initView();
    }

    private void initView() {
        id = getIntent().getIntExtra("id", 1);
        fragments.add(ClassDetailIntroduceFragment.newInstance(id));
        fragments.add(ClassDetailCatalogueFragment.newInstance(id));
        fragments.add(ClassDetailCommentFragment.newInstance(id));
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setScroll(false);
        viewPager.setCurrentItem(0);
        for (int i = 0; i < 3; i++) {
            customTabEntities.add(new TabEntity(titles[i]));
        }
        commonTabLayout.setTabData(customTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
    }


    @OnClick({R.id.rl_shopcar, R.id.rl_collect, R.id.tv_add_shopcar, R.id.tv_to_study})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.rl_shopcar:
                ShopCarActivity.open(this);
                break;
            case R.id.rl_collect:
                if (isCollected) {
                    cancelCollect();
                } else {
                    collect();
                }
                break;
            case R.id.tv_add_shopcar:
                addShopCar();
//                AudioActivity.open(-1, this);
                break;
            case R.id.tv_to_study:
                switch (type){
                    case 0://视频
                        PlayActivity.open(true,-1,this,id);
                        break;
                    case 1://音频
                        AudioActivity.open(-1,this,id);
                        break;
                    case 2://图文
                        PlayActivity.open(false,-1,this,id);
                        break;
                }
                break;
        }
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();

        switch (msgCode) {
            case "ClassIntroduce":
                ClassDetailIntroduce detailIntroduce = (ClassDetailIntroduce) event.getObject();
                GlideUtil.load(detailIntroduce.getHead_banner(), iv);
                isCollected = detailIntroduce.getCollection() == 1;
                ivCollect.setImageResource(isCollected ? R.mipmap.middle_yellow_star : R.mipmap.gray_star_big);
                type = detailIntroduce.getType();
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    /**
     * 收藏
     */
    private void collect() {
        if (id > 0) {
            Map<String,String> map = new HashMap<>();
            map.put("course_id",id+"");
            Http.http.createApi(MineAPI.class)
                    .collect(map)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                        @Override
                        public void success(String result) {
                            showMessage("收藏成功");
                            ivCollect.setImageResource(R.mipmap.middle_yellow_star);
                            isCollected = true;
                        }

                        @Override
                        public void fail(int errCode, String errStr) {
                            showMessage(errStr);
                        }
                    }));
        }

    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        if (id > 0) {
            Map<String,String> map = new HashMap<>();
            map.put("course_id",id+"");
            Http.http.createApi(MineAPI.class)
                    .cancelCollect(map)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                        @Override
                        public void success(String result) {
                            showMessage("取消收藏成功");
                            ivCollect.setImageResource(R.mipmap.gray_star_big);
                            isCollected = false;
                        }

                        @Override
                        public void fail(int errCode, String errStr) {
                            showMessage(errStr);
                        }
                    }));
        }
    }

    private void addShopCar(){
        if (id > 0) {
            Http.http.createApi(MineAPI.class)
                    .addShopCar(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                        @Override
                        public void success(String result) {
                            showMessage("加入购物车成功");
                        }

                        @Override
                        public void fail(int errCode, String errStr) {
                            showMessage(errStr);
                        }
                    }));
        }
    }


}
