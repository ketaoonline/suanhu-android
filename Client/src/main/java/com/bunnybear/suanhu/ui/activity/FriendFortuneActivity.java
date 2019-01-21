package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.FriendFortune;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.xiaoxiong.library.http.RequestCallBack;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendFortuneActivity extends AppActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_god_content)
    TextView tvGodContent;
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

    public static void open(AppActivity activity,int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        activity.startActivity(bundle, FriendFortuneActivity.class);
    }



    @Override
    protected int initLayout() {
        return R.layout.activity_friend_fortune;
    }

    @Override
    protected String setTitleStr() {
        return "好友运势";
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
                .getFriendFortune(getIntent().getIntExtra("id",-1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<FriendFortune>() {
                    @Override
                    public void success(FriendFortune result) {
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
                .getShareInfo(4)
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


    private void setDataToView(FriendFortune detail) {
        tvScore.setText(detail.getFen() + "");
        tvGodContent.setText(detail.getDuanyu());
        tvColor.setText(detail.getYanse());
        tvNumber.setText(detail.getShuzi() + "");
        tvDirection.setText(detail.getFangxiang());
        tv.setText(detail.getYinli());
        String[] dateStrs = detail.getYangli().split("-");
        if (dateStrs != null && dateStrs.length > 0){
            tvYearMonth.setText(dateStrs[0]+"."+dateStrs[1]);
            tvDay.setText(dateStrs[2]);
        }
        tvRizhu.setText(detail.getRizhu());
        tvName.setText(detail.getName());
    }

}
