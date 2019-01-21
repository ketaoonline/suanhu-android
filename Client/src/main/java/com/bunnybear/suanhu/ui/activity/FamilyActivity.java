package com.bunnybear.suanhu.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Member;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.FamilyAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FamilyActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    public static void open(AppActivity activity) {
        activity.startActivity(null, FamilyActivity.class);
    }

    FamilyAdapter adapter;
    List<Member> list = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_family;
    }

    @Override
    protected String setTitleStr() {
        return "我的八字库";
    }

    @Override
    protected void init() {
        twRefreshLayout.setPureScrollModeOn();

        initRecyclerView();

        adapter = new FamilyAdapter(list);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f2f2f2")));
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem editItem = new SwipeMenuItem(FamilyActivity.this)
                        .setBackgroundColor(getResources().getColor(R.color.main_text_checked))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(DensityUtil.dp2px(FamilyActivity.this, 44))
                        .setText("编辑")
                        .setTextColor(Color.WHITE);
                SwipeMenuItem deleteItem = new SwipeMenuItem(FamilyActivity.this)
                        .setBackgroundColor(Color.parseColor("#F55A5A"))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(DensityUtil.dp2px(FamilyActivity.this, 44))
                        .setText("删除")
                        .setTextColor(Color.WHITE);
                rightMenu.addMenuItem(editItem);
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

                if (menuPosition == 0) {
                    EditMemberInfoActivity.open(FamilyActivity.this, list.get(adapterPosition));
                } else {
                    showDialog(adapterPosition, "确认删除" + list.get(adapterPosition).getF_name() + "？");
                }

            }
        };
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
    }

    private void showDialog(final int position, String message) {
        NormalDialog.Builder builder = new NormalDialog.Builder(this);
        builder.setMessage(message);
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteMember(list.get(position).getId());
            }

        }).setCancelBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Http.http.createApi(MineAPI.class)
                .getFamily()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Member>>() {
                    @Override
                    public void success(List<Member> result) {
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        if (list.size() > 0){
                            recyclerView.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }

    private void deleteMember(int id) {
        Http.http.createApi(MineAPI.class)
                .deleteMember(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        getData();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        EditMemberInfoActivity.open(FamilyActivity.this, null);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FriendFortuneActivity.open(this,list.get(position).getId());
    }
}
