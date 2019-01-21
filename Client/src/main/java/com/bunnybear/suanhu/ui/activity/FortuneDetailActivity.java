package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.FortuneDetail;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.util.share.OnShareSuccessListener;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.xiaoxiong.library.http.RequestCallBack;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FortuneDetailActivity extends AppActivity {

    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_4)
    TextView tv4;
    //    @BindView(R.id.tv_tab_1)
//    TextView tvTab1;
//    @BindView(R.id.ll_1)
//    LinearLayout ll1;
//    @BindView(R.id.rl_1)
//    RelativeLayout rl1;
//    @BindView(R.id.tv_tab_2)
//    TextView tvTab2;
//    @BindView(R.id.ll_2)
//    LinearLayout ll2;
//    @BindView(R.id.rl_2)
//    RelativeLayout rl2;
//    @BindView(R.id.tv_tab_3)
//    TextView tvTab3;
//    @BindView(R.id.ll_3)
//    LinearLayout ll3;
//    @BindView(R.id.rl_3)
//    RelativeLayout rl3;
//    @BindView(R.id.tv_tab_4)
//    TextView tvTab4;
//    @BindView(R.id.ll_4)
//    LinearLayout ll4;
//    @BindView(R.id.rl_4)
//    RelativeLayout rl4;
    @BindView(R.id.tv_yi)
    TextView tvYi;
    @BindView(R.id.tv_ji)
    TextView tvJi;
    @BindView(R.id.tv_element)
    TextView tvElement;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_zouyun)
    TextView tvZouyun;
    @BindView(R.id.tv_money_content)
    TextView tvMoneyContent;
    @BindView(R.id.tv_love_content)
    TextView tvLoveContent;
    @BindView(R.id.tv_god_content)
    TextView tvGodContent;
    @BindView(R.id.tv_people_content)
    TextView tvPeopleContent;
    @BindView(R.id.tv_color)
    TextView tvColor;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_direction)
    TextView tvDirection;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_year_month)
    TextView tvYearMonth;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_rizhu)
    TextView tvRizhu;

    public static void open(AppActivity activity) {
        activity.startActivity(null, FortuneDetailActivity.class);
    }


    @Override
    protected int initLayout() {
        return R.layout.activity_fortune_detail;
    }

    @Override
    protected String setTitleStr() {
        return "运势详情";
    }

    @Override
    protected void init() {

        getImageRight().setImageResource(R.mipmap.white_share);

        getData();
    }


    @Override
    protected void rightButtonClick() {
        showPopWindow();
    }


    private void getData() {
        Http.http.createApi(MainAPI.class)
                .getFortuneDetail()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<FortuneDetail>() {
                    @Override
                    public void success(FortuneDetail result) {
                        setDataToView(result);
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
                .getShareInfo(3)
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


    private void setDataToView(FortuneDetail detail) {
        tv1.setText(detail.getChengyu().get(0));
        tv2.setText(detail.getChengyu().get(1));
        tv3.setText(detail.getChengyu().get(2));
        tv4.setText(detail.getChengyu().get(3));
        tvYi.setText(detail.getYi());
        tvJi.setText(detail.getJi());
        tvZouyun.setText(detail.getZouyu());
        tvScore.setText(detail.getYunfen() + "");
        switch (detail.getMingge()) {
            case 0:
                tvElement.setText("木命格");
                tvElement.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wood_icon, 0, 0, 0);
                break;
            case 1:
                tvElement.setText("水命格");
                tvElement.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.water_icon, 0, 0, 0);
                break;
            case 2:
                tvElement.setText("火命格");
                tvElement.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.fire_icon, 0, 0, 0);
                break;
            case 3:
                tvElement.setText("金命格");
                tvElement.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gold_icon, 0, 0, 0);
                break;
            case 4:
                tvElement.setText("土命格");
                tvElement.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.soil_icon, 0, 0, 0);
                break;
        }
        tvMoneyContent.setText(detail.getCaifu());
        tvLoveContent.setText(detail.getGanqing());
        tvPeopleContent.setText(detail.getRenji());
        tvGodContent.setText(TextUtils.isEmpty(detail.getShensha()) ? "无" : detail.getShensha());
        tvColor.setText(detail.getYanse());
        tvNumber.setText(detail.getShuzi() + "");
        tvDirection.setText(detail.getFangxiang());
        tv.setText(detail.getYinli());
        String[] dateStrs = detail.getYangli().split("-");
        if (dateStrs != null && dateStrs.length > 0) {
            tvYearMonth.setText(dateStrs[0] + "." + dateStrs[1]);
            tvDay.setText(dateStrs[2]);
        }
        tvRizhu.setText(detail.getRizhu());
    }

}
