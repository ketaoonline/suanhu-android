package com.bunnybear.suanhu.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.PlayDetail;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.bean.User;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.PlayAdapter;
import com.bunnybear.suanhu.ui.adapter.SmallImageAdapter;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.LogUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.CustomViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.jz_video)
    JzvdStd jzVideo;
    @BindView(R.id.customViewPager)
    CustomViewPager customViewPager;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivRecyclerView)
    RecyclerView ivRecyclerView;

    public static void open(boolean isVideo, int lessonId, AppActivity activity, int courseId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isVideo", isVideo);
        bundle.putInt("lessonId", lessonId);
        bundle.putInt("courseId", courseId);
        activity.startActivity(bundle, PlayActivity.class);
    }

    PlayAdapter adapter;
    List<MainBaseBean> list = new ArrayList<>();
    MyAdapter viewPagerAdapter;
    List<String> urls = new ArrayList<>();
    SmallImageAdapter smallImageAdapter;
    int lessonId, courseId;
    boolean isVideo = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {
        lessonId = getIntent().getIntExtra("lessonId", -1);
        courseId = getIntent().getIntExtra("courseId", -1);
        isVideo = getIntent().getBooleanExtra("isVideo", false);
        if (isVideo) {
            jzVideo.setVisibility(View.VISIBLE);
            rlImage.setVisibility(View.GONE);
        } else {
            jzVideo.setVisibility(View.GONE);
            rlImage.setVisibility(View.VISIBLE);
        }
        initInfo();

        getPlayDetail();

    }

    private void initInfo() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(1));
        adapter = new PlayAdapter(this, list, isVideo);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
    }

    private void initVideo(String url) {
        jzVideo.setUp(url, "", JzvdStd.SCREEN_WINDOW_NORMAL);
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        jzVideo.startVideo();
    }

    private void initImage() {
        setTitle(1 + "/" + urls.size());
        customViewPager.setOffscreenPageLimit(list.size());
        viewPagerAdapter = new MyAdapter(urls);
        customViewPager.setAdapter(viewPagerAdapter);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                smallImageAdapter.setCheckPosition(position);
                ivRecyclerView.smoothScrollToPosition(position);
                int currentPosition = 0;
                if (position == urls.size()) {
                    currentPosition = 1;
                } else {
                    currentPosition = position + 1;
                }
                setTitle(currentPosition + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LinearLayoutManager ivLinearLayoutManager = new LinearLayoutManager(this);
        ivLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ivRecyclerView.setLayoutManager(ivLinearLayoutManager);
        smallImageAdapter = new SmallImageAdapter(urls);
        ivRecyclerView.setAdapter(smallImageAdapter);
        smallImageAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        submitStudyProgress();
        super.onBackPressed();
    }

    @Override
    protected void leftButtonclick(View view) {
        submitStudyProgress();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                submitStudyProgress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (smallImageAdapter.checkPosition == position) return;
        smallImageAdapter.setCheckPosition(position);
        customViewPager.setCurrentItem(position);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        String url = (String) event.getObject();
        switch (msgCode) {
            case "VideoUrl":
                initVideo(url);
                break;
            case "ImageUrl":
                urls = Arrays.asList(url.split(","));
                setTitle(1 + "/" + urls.size());
                viewPagerAdapter.setList(urls);
                customViewPager.setCurrentItem(0);
                smallImageAdapter.setCheckPosition(0);
                smallImageAdapter.setNewData(urls);
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_collect:
                if (((PlayDetail) list.get(0)).getCollection() == 0) {
                    collect();
                } else {
                    cancelCollect();
                }
                break;
            case R.id.iv_share:
                showPopWindow();
                break;
        }
    }


    private class MyAdapter extends PagerAdapter {

        List<String> list;
        private int mChildCount = 0;

        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = new ImageView(PlayActivity.this);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUtil.load(list.get(position), view);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setList(List<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    private void getPlayDetail() {
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(ClassAPI.class)
                .getPlayDetail(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<PlayDetail>() {
                    @Override
                    public void success(PlayDetail result) {
                        list.remove(0);
                        list.add(0, result);
                        adapter.setNewData(list);
                        for (int i = 0; i < result.getCatalog_info().size(); i++) {
                            LogUtil.i("id----", lessonId + "----" + result.getCatalog_info().get(i).getId());
                            if (result.getCatalog_info().get(i).getId() == lessonId) {
                                adapter.setCheckPosition(i);
                                break;
                            }
                        }

                        if (isVideo) {
                            initVideo(result.getUrl());
                        } else {
                            urls = Arrays.asList(result.getUrl().split(","));
                            initImage();
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void submitStudyProgress() {
        int checkPosition = adapter.checkPosition;
        Map<String, String> map = new HashMap<>();
        if (courseId > 0) {
            map.put("course_id", courseId + "");
        } else {
            map.put("course_info_id", lessonId + "");
        }
        map.put("couese_much", checkPosition + 1 + "");
        Http.http.createApi(ClassAPI.class)
                .submitStudyProgress(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    /**
     * 收藏
     */
    private void collect() {
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(MineAPI.class)
                .collect(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("收藏成功");
                        ((PlayDetail) list.get(0)).setCollection(1);
                        adapter.notifyItemChanged(0);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(MineAPI.class)
                .cancelCollect(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("取消收藏成功");
                        ((PlayDetail) list.get(0)).setCollection(0);
                        adapter.notifyItemChanged(0);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    SharePopWindow popWindow;
    private void showPopWindow() {
        popWindow = new SharePopWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShareInfo(view.getId());
                popWindow.dismiss();
            }
        });
        popWindow.showAtLocation(getWindow().getDecorView(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void getShareInfo(int viewId) {
        Http.http.createApi(MineAPI.class)
                .getShareInfo(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<ShareBean>() {
                    @Override
                    public void success(ShareBean result) {
                        switch (viewId) {
                            case R.id.ll_weixin:
                                WeiXinShareUtil.shareToWX(mActivity, result, WeiXinShareUtil.WEIXIN_SHARE_TYPE_TALK);
                                break;
                            case R.id.ll_friends_circle:
                                WeiXinShareUtil.shareToWX(mActivity, result, WeiXinShareUtil.WEIXIN_SHARE_TYPE_FRENDS);
                                break;
                            case R.id.ll_qq:
                                QQShareUtil.shareToQQ(mActivity, result, null);
                                break;
                            case R.id.ll_qzone:
                                QQShareUtil.shareToQzone(mActivity, result, null);
                                break;
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
