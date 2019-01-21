package com.bunnybear.suanhu.master.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppFragment;
import com.bunnybear.suanhu.master.base.ConstData;
import com.bunnybear.suanhu.master.bean.User;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.ui.activity.ApplyMasterActivity;
import com.bunnybear.suanhu.master.ui.activity.IncomeActivity;
import com.bunnybear.suanhu.master.ui.activity.LoginActivity;
import com.bunnybear.suanhu.master.ui.activity.MainActivity;
import com.bunnybear.suanhu.master.ui.activity.OrderActivity;
import com.bunnybear.suanhu.master.ui.activity.SettingActivity;
import com.bunnybear.suanhu.master.view.SettingOrderCountDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.BGButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MineFragment extends AppFragment {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.iv_5)
    ImageView iv5;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.btn_apply)
    BGButton btnApply;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    User user;

    @Override
    protected int initLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void normalLoad() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                refreshData();
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
        refreshData();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        switch (msgCode){
            case "SettingOrderCount":
                int count = (int) event.getObject();
                setOrderCount(count);
                break;
        }
    }

    @OnClick({R.id.rl,R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.btn_apply,R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                SettingActivity.open(mActivity);
                break;
            case R.id.rl:
//                SettingActivity.open(mActivity);
                break;
            case R.id.rl_1:
                if (user.getIs_master() == 0)return;
                OrderActivity.open(mActivity);
                break;
            case R.id.rl_2:
                if (user.getIs_master() == 0)return;
                IncomeActivity.open(mActivity);
                break;
            case R.id.rl_3:
                if (user.getIs_master() == 0)return;
                new SettingOrderCountDialog.Builder(mActivity,user.getOrder_number()).create().show();
                break;
            case R.id.rl_4:
                if (user.getIs_master() == 0)return;
                break;
            case R.id.rl_5:
                if (user.getIs_master() == 0)return;
                break;
            case R.id.btn_apply:
                ApplyMasterActivity.open(mActivity);
                break;
        }
    }

    private void update(){
        if (user != null) {
            btnApply.setVisibility(user.getIs_master() == 0? View.VISIBLE : View.GONE);
            iv1.setImageResource(user.getIs_master() == 1 ? R.mipmap.mine_c_icon_1 : R.mipmap.mine_icon_1);
            iv2.setImageResource(user.getIs_master() == 1 ? R.mipmap.mine_c_icon_2 : R.mipmap.mine_icon_2);
            iv3.setImageResource(user.getIs_master() == 1 ? R.mipmap.mine_c_icon_3 : R.mipmap.mine_icon_3);
            iv4.setImageResource(user.getIs_master() == 1 ? R.mipmap.mine_c_icon_4 : R.mipmap.mine_icon_4);
            iv5.setImageResource(user.getIs_master() == 1 ? R.mipmap.mine_c_icon_5 : R.mipmap.mine_icon_5);
        }

    }

    public void refreshData() {
        Http.http.createApi(MineAPI.class)
                .getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<User>() {
                    @Override
                    public void success(User result) {
                        user = result;
                        GlideUtil.load(result.getAvatar(), ivHead);
                        tvNickname.setText(result.getMaster_name());
                        tvPoint.setText("算龄：" + result.getAge()+"年");
                        tvVip.setText(result.getTag());
                        tv2.setText("¥"+result.getIncome());
                        tv3.setText(result.getOrder_number()+"单");
                        refreshLayout.finishRefreshing();
                        update();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }


    private void setOrderCount(final int count){
        Http.http.createApi(MineAPI.class)
                .setOrderCount(count)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        refreshData();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }
}
