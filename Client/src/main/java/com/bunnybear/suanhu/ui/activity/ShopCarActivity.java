package com.bunnybear.suanhu.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Member;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.FamilyAdapter;
import com.bunnybear.suanhu.ui.adapter.ShopCarAdapter;
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

public class ShopCarActivity extends AppActivity implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_choose_all)
    TextView tvChooseAll;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_buy)
    TextView tvBuy;

    public static void open(AppActivity activity) {
        activity.startActivity(null, ShopCarActivity.class);
    }

    List<SClass> list = new ArrayList<>();
    ShopCarAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected String setTitleStr() {
        return "我的购物车";
    }

    @Override
    protected void init() {
        twRefreshLayout.setPureScrollModeOn();

        initRecyclerView();

        adapter = new ShopCarAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        getData();

    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5"), 2, DensityUtil.dp2px(this, 10)));
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(ShopCarActivity.this)
                        .setBackgroundColor(Color.parseColor("#F55A5A"))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(DensityUtil.dp2px(ShopCarActivity.this, 100))
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

    @OnClick({R.id.tv_choose_all, R.id.tv_buy})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.tv_choose_all:
                chooseAll();
                break;
            case R.id.tv_buy:
                if (isChooseClass()){
                    buy();
                }else {
                    showMessage("请选择要购买的课程");
                }
                break;
        }
    }

    private void showDialog(final int position, String message) {
        NormalDialog.Builder builder = new NormalDialog.Builder(this);
        builder.setMessage(message);
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(list.get(position).getId());
            }

        }).setCancelBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create().show();
    }

    private void calcPrice() {
        double totalMoney = 0;
        for (SClass sClass : list) {
            if (sClass.isChecked()) {
                totalMoney += sClass.getPrice();
            }
        }
        tvTotalMoney.setText("¥ " + totalMoney);
    }

    private boolean isChooseAll() {
        for (SClass sClass : list) {
            if (!sClass.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private void chooseAll(){
        if (isChooseAll()) {
            for (SClass sClass : list) {
                sClass.setChecked(false);
            }
            tvChooseAll.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shop_car_uncheck,0,0,0);
        }else {
            for (SClass sClass : list) {
                sClass.setChecked(true);
            }
            tvChooseAll.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shop_car_checked,0,0,0);
        }
        adapter.notifyDataSetChanged();
        calcPrice();
    }

    private boolean isChooseClass(){
        for (SClass sClass : list) {
            if (sClass.isChecked()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.iv_status:
                if (list.get(position).isChecked()){
                    list.get(position).setChecked(false);
                    tvChooseAll.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shop_car_uncheck,0,0,0);
                }else {
                    list.get(position).setChecked(true);
                    if (isChooseAll()) {
                        tvChooseAll.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shop_car_checked,0,0,0);
                    }
                }
                adapter.notifyItemChanged(position);
                calcPrice();
                break;
        }

    }


    private void getData() {
        Http.http.createApi(MineAPI.class)
                .myShopCar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SClass>>() {
                    @Override
                    public void success(List<SClass> result) {
                        tvChooseAll.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.shop_car_uncheck,0,0,0);
                        list.clear();
                        list.addAll(result);
                        adapter.setNewData(list);
                        tvCount.setText("共" + list.size() + "门课程");

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
                        showMessage(errStr);
                    }
                }));
    }

    /**
     * 删除课程
     *
     * @param id
     */
    private void delete(int id) {
        Http.http.createApi(MineAPI.class)
                .deleteShopCar(id)
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

    private void buy(){
        String ids = "";
        for (SClass sClass : list) {
            if (sClass.isChecked()) {
                ids += sClass.getId()+",";
            }
        }
        ids = ids.substring(0,ids.length()-1);
        Http.http.createApi(MineAPI.class)
                .buy(ids)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        OrderDetailActivity.open(ShopCarActivity.this,result);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
