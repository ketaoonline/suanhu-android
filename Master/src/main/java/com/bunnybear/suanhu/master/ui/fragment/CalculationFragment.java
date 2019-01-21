package com.bunnybear.suanhu.master.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppFragment;
import com.bunnybear.suanhu.master.bean.Consult;
import com.bunnybear.suanhu.master.bean.DetailedTest;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.ui.activity.ChatActivity;
import com.bunnybear.suanhu.master.ui.adapter.MyConsultAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DensityUtil;
import com.xiaoxiong.library.utils.EmptyViewUtil;
import com.xiaoxiong.library.view.NormalDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CalculationFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;


    public static CalculationFragment newInstance() {
        CalculationFragment fragment = new CalculationFragment();
        return fragment;
    }

    MyConsultAdapter adapter;
    List<DetailedTest> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.fragment_calc;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void normalLoad() {
        initRecyclerView();

        adapter = new MyConsultAdapter(list);
//        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        twRefreshLayout.setEnableLoadmore(false);
        twRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Override
    protected boolean useLazyLoad() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"),2, DensityUtil.dp2px(mActivity,10)));
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity)
                        .setBackgroundColor(Color.parseColor("#F55A5A"))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(DensityUtil.dp2px(mActivity, 70))
                        .setText("删除")
                        .setTextColor(Color.WHITE);
                rightMenu.addMenuItem(deleteItem);
            }
        };
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

                showDialog(adapterPosition, "确认删除？");

            }
        };
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
    }

    private void showDialog(final int position, String message) {
        NormalDialog.Builder builder = new NormalDialog.Builder(mActivity);
        builder.setMessage(message);
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteDetailedTest(list.get(position).getOrder_use_sn());
            }

        }).setCancelBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create().show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ChatActivity.open(mActivity,list.get(position));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK){
            getData();
        }
    }

    private void getData(){
        Http.http.createApi(MineAPI.class)
                .getDetailedTests()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<DetailedTest>>() {
                    @Override
                    public void success(List<DetailedTest> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        if (list.size() > 0){
//                            recyclerView.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        }else {
//                            recyclerView.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                        if (twRefreshLayout != null){
                            twRefreshLayout.finishRefreshing();
                        }

                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    private void deleteDetailedTest(int id){
        Http.http.createApi(MineAPI.class)
                .deleteTest(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        getData();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        switch (msgCode) {
            case "CHAT_LIST_REFRESH":
                getData();
                break;
        }
    }
}
