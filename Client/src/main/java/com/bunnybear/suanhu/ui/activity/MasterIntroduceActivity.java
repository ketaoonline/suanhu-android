package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Comment;
import com.bunnybear.suanhu.bean.CommentResponse;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.MasterInfo;
import com.bunnybear.suanhu.bean.TestBigType;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.MasterIntroduceAdapter;
import com.bunnybear.suanhu.view.TypeDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MasterIntroduceActivity extends AppActivity {

    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_sample_survey)
    TextView tvSampleSurvey;
    @BindView(R.id.tv_detailed_survey)
    TextView tvDetailedSurvey;

    public static void open(AppActivity activity, int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        activity.startActivity(bundle, MasterIntroduceActivity.class);
    }

    MasterIntroduceAdapter adapter;
    List<MainBaseBean> list = new ArrayList<>();

    int id;
    int page = 1;
    int totalPage;
    boolean isCollected;
    MasterInfo masterInfo;
    List<String> stringList = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_master_introduce;
    }

    @Override
    protected String setTitleStr() {
        return "大师介绍";
    }

    @Override
    protected void init() {
        id = getIntent().getIntExtra("id", -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new MainBaseBean(4));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(1));
        list.add(new MainBaseBean(2));

        adapter = new MasterIntroduceAdapter(this, list);
        recyclerView.setAdapter(adapter);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                page++;
                getData(false);
            }
        });

        getMasterInfo();
    }

    private void getMasterInfo() {
        Http.http.createApi(CalcAPI.class)
                .getMasterInfo(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<MasterInfo>() {
                    @Override
                    public void success(MasterInfo result) {
                        masterInfo = result;
                        result.setViewType(4);
                        list.remove(0);
                        list.add(0, result);
                        adapter.setNewData(list);

                        setData();

                        getData(true);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    private void getData(boolean isRefresh) {
        Http.http.createApi(CalcAPI.class)
                .getComments(getIntent().getIntExtra("id", -1), "master", page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<CommentResponse>() {
                    @Override
                    public void success(CommentResponse result) {
                        totalPage = result.getTotal_page();
                        refreshLayout.setEnableLoadmore(page < totalPage);
                        if (isRefresh) {
                            if (result.getComment().size() > 0) {
                                list.add(new MainBaseBean(1));
                            }
                            for (Comment comment : result.getComment()) {
                                comment.setViewType(3);
                                list.add(comment);
                            }
                            refreshLayout.finishRefreshing();
                        } else {
                            for (Comment comment : result.getComment()) {
                                comment.setViewType(3);
                                list.add(comment);
                            }
                            refreshLayout.finishLoadmore();
                        }
                        adapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void setData() {
        int resId = R.mipmap.gray_star_big;
        String str = "收藏";
        if (masterInfo != null) {
            isCollected = masterInfo.getFollow() == 1;
            if (masterInfo.getFollow() == 1) {
                resId = R.mipmap.middle_yellow_star;
                str = "已收藏";
            }
            tvCollect.setText(str);
            tvCollect.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0);
            tvSampleSurvey.setText("简测\n¥" + masterInfo.getPrice());
//            tvDetailedSurvey.setText("咨询大师\n¥" + masterInfo.getX_price());
        }

    }


    @OnClick({R.id.ll_collect, R.id.tv_sample_survey, R.id.tv_detailed_survey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_collect:
                if (isCollected) {
                    cancelCollect();
                } else {
                    collect();
                }
                break;
            case R.id.tv_sample_survey:
                break;
            case R.id.tv_detailed_survey:
                Hawk.put("isFirstChooseMaster",true);
                getTestBigTypes();
                break;
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
            case "MasterChooseType":
                String idStr = (String) event.getObject();
                int typeId = Integer.valueOf(idStr);
                AssortmentActivity.open(this,typeId, id);
                break;
        }
    }

    private void getTestBigTypes() {
        Http.http.createApi(CalcAPI.class)
                .getTestBigTypes(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<TestBigType>>() {
                    @Override
                    public void success(List<TestBigType> result) {
                        stringList.clear();
                        for (TestBigType testBigType : result) {
                            stringList.add(testBigType.getQuestion_type_id() + "@" + testBigType.getName());
                        }
                        new TypeDialog.Builder(mActivity, stringList,"MasterChooseType").create().show();
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
        Http.http.createApi(MineAPI.class)
                .follow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("收藏成功");
                        tvCollect.setText("已收藏");
                        tvCollect.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.middle_yellow_star, 0, 0, 0);
                        isCollected = true;
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
        Http.http.createApi(MineAPI.class)
                .deleteFollow(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("取消收藏成功");
                        tvCollect.setText("收藏");
                        tvCollect.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.gray_star_big, 0, 0, 0);
                        isCollected = false;
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

//    /**
//     * 详测下单
//     */
//    private void putDetailedOrder() {
//        Http.http.createApi(CalcAPI.class)
//                .putDetailedTestOrder(id)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
//                    @Override
//                    public void success(String result) {
//                        MasterOrderDetailActivity.open(MasterIntroduceActivity.this, result);
//                    }
//
//                    @Override
//                    public void fail(int errCode, String errStr) {
//                        showMessage(errStr);
//                    }
//                }));
//    }


}
