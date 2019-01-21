package com.bunnybear.suanhu.ui.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.bean.User;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.ApplyMasterDesActivity;
import com.bunnybear.suanhu.ui.activity.CouponActivity;
import com.bunnybear.suanhu.ui.activity.FamilyActivity;
import com.bunnybear.suanhu.ui.activity.FeedbackActivity;
import com.bunnybear.suanhu.ui.activity.MyCalcOrderListActivity;
import com.bunnybear.suanhu.ui.activity.MyClassOrderListActivity;
import com.bunnybear.suanhu.ui.activity.MyCollectActivity;
import com.bunnybear.suanhu.ui.activity.SettingActivity;
import com.bunnybear.suanhu.ui.activity.ShopCarActivity;
import com.bunnybear.suanhu.ui.activity.VIPActivity;
import com.bunnybear.suanhu.ui.activity.WebViewActivity;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tencent.bugly.beta.Beta;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MineFragment extends AppFragment {


    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_vip_entrance)
    TextView tvVipEntrance;
    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.rl_vip)
    RelativeLayout rlVip;

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
        twRefreshLayout.setEnableLoadmore(false);
        twRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
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

    @OnClick({R.id.rl_vip, R.id.iv_family, R.id.iv_setting, R.id.ll_calc_order, R.id.ll_class_order,
            R.id.ll_coupon, R.id.ll_shopcar, R.id.ll_collect, R.id.ll_luck, R.id.rl_my_income,
            R.id.rl_invite_friend, R.id.rl_feedback, R.id.rl_bind_phone,R.id.rl_des})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.iv_family:
                FamilyActivity.open(mActivity);
                break;
            case R.id.iv_setting:
                SettingActivity.open(mActivity);
                break;
            case R.id.ll_calc_order:
                MyCalcOrderListActivity.open(mActivity);
                break;
            case R.id.ll_class_order:
                MyClassOrderListActivity.open(mActivity);
                break;
            case R.id.ll_coupon:
                CouponActivity.open(mActivity);
                break;
            case R.id.ll_shopcar:
                ShopCarActivity.open(mActivity);
                break;
            case R.id.ll_collect:
                MyCollectActivity.open(mActivity);
                break;
            case R.id.ll_luck:
                break;
            case R.id.rl_my_income:
                break;
            case R.id.rl_invite_friend:
                showPopWindow();
                break;
            case R.id.rl_feedback:
                FeedbackActivity.open(mActivity);
                break;
            case R.id.rl_bind_phone:
                Beta.checkUpgrade();
                break;
            case R.id.rl_vip:
                VIPActivity.open(mActivity);
                break;
            case R.id.rl_des:
                ApplyMasterDesActivity.open(mActivity);
                break;
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
                        tvNickname.setText(result.getUser_nickname());
                        tvPoint.setText("算分：" + result.getSuanfen());
                        tvVip.setVisibility(result.getVip() == 0 ? View.GONE : View.VISIBLE);
                        rlVip.setVisibility(result.getVip() == 0 ? View.VISIBLE : View.GONE);
                        twRefreshLayout.finishRefreshing();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    SharePopWindow popWindow;
    private void showPopWindow() {
        popWindow = new SharePopWindow(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShareInfo(view.getId());
                popWindow.dismiss();
            }
        });
        popWindow.showAtLocation(mActivity.getWindow().getDecorView(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void getShareInfo(int viewId) {
        Http.http.createApi(MineAPI.class)
                .getShareInfo(2)
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
