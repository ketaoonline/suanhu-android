package com.bunnybear.suanhu.ui.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebViewFragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.AuthAPI;
import com.bunnybear.suanhu.api.CalcAPI;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppFragment;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.bean.Article;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.Notices;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.bean.SignInfo;
import com.bunnybear.suanhu.bean.TestBigType;
import com.bunnybear.suanhu.bean.TestBigTypeResponse;
import com.bunnybear.suanhu.bean.TopData;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.net.HttpConstData;
import com.bunnybear.suanhu.ui.activity.ArticleActivity;
import com.bunnybear.suanhu.ui.activity.AssortmentActivity;
import com.bunnybear.suanhu.ui.activity.ConversationListActivity;
import com.bunnybear.suanhu.ui.activity.FortuneDetailActivity;
import com.bunnybear.suanhu.ui.activity.LoginActivity;
import com.bunnybear.suanhu.ui.activity.MainActivity;
import com.bunnybear.suanhu.ui.activity.MasterIntroduceActivity;
import com.bunnybear.suanhu.ui.activity.MasterRecommendActivity;
import com.bunnybear.suanhu.ui.activity.PutQuestionActivityNew;
import com.bunnybear.suanhu.ui.activity.WebViewActivity;
import com.bunnybear.suanhu.ui.adapter.MainAdapter;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.bunnybear.suanhu.view.TypeDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.view.BGButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends AppFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    List<String> stringList = new ArrayList<>();
    List<MainBaseBean> list = new ArrayList<>();
    MainAdapter mainAdapter;

    int articlePosition = 6;
    boolean isSimple = false;

    @Override
    protected int initLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void lazyLoad() {
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(5));
        list.add(new MainBaseBean(1));
        list.add(new MainBaseBean(2));
        list.add(new MainBaseBean(6));
        list.add(new MainBaseBean(3));
        list.add(new MainBaseBean(4));
        mainAdapter = new MainAdapter(list);
        rv.setAdapter(mainAdapter);

        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                articlePosition = 6;
                getData();
            }

        });
        mainAdapter.setOnItemClickListener(this);
        mainAdapter.setOnItemChildClickListener(this);
        getData();
    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    private void getData() {
        list.clear();
        list.add(new MainBaseBean(0));
        list.add(new MainBaseBean(5));
        list.add(new MainBaseBean(1));
        list.add(new MainBaseBean(2));
        list.add(new MainBaseBean(6));
        list.add(new MainBaseBean(3));
        list.add(new MainBaseBean(4));

        getTopData();
        getNotices();
        getArticles();

    }


    private void getArticles() {
        Http.http.createApi(MainAPI.class)
                .getArticles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<Article>>() {
                    @Override
                    public void success(List<Article> result) {
                        for (Article article : result) {
                            article.setViewType(4);
                            if (articlePosition == 6) {
                                list.remove(articlePosition);
                            }
                            list.add(articlePosition, article);
                            articlePosition++;
                        }
                        mainAdapter.setNewData(list);
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void getTopData() {
        Http.http.createApi(MainAPI.class)
                .getTopData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<TopData>() {
                    @Override
                    public void success(TopData result) {
                        list.remove(0);
                        list.add(0, result);
                        mainAdapter.notifyItemChanged(0);
                        mainAdapter.notifyItemChanged(1);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void getNotices() {
        Http.http.createApi(MainAPI.class)
                .getNotices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<String>>() {
                    @Override
                    public void success(List<String> result) {
                        Notices notices = new Notices(6, result);
                        list.remove(4);
                        list.add(4, notices);
                        mainAdapter.notifyItemChanged(4);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void getSignInfo() {
        Http.http.createApi(MainAPI.class)
                .getSignInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<SignInfo>() {
                    @Override
                    public void success(SignInfo result) {
                        if (result == null) return;
                        showDialog(result);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


    @OnClick({R.id.tv_sign, R.id.iv_right})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.tv_sign:
//                showMessage("error...");
                getSignInfo();
                break;
            case R.id.iv_right:
                ConversationListActivity.open(mActivity, 0);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == 0) {
            FortuneDetailActivity.open(mActivity);
        }
        if (list.get(position) instanceof Article) {
            Article article = (Article) list.get(position);
            WebViewActivity.open(mActivity, article.getPost_title(), article.getNews_url());
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_all_article:
                ArticleActivity.open(mActivity);
                break;
            case R.id.rl_find_master:
                isSimple = false;
                getTestBigTypes(0);
                break;
            case R.id.rl_quick:
                isSimple = true;
                Hawk.put("isFirstChooseMaster", false);
                getTestBigTypes(5);
                break;
            case R.id.tv_to_detail:

                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMessageReceive(IEvent event) {
        String msgCode = event.getMsgCode();
        switch (msgCode) {
            case "MainChooseType":
                String idStr = (String) event.getObject();
                int id = Integer.valueOf(idStr);
                if (isSimple) {
                    PutQuestionActivityNew.open(mActivity, id);
                } else {
                    Hawk.put("isFirstChooseMaster", false);
                    AssortmentActivity.open(mActivity, id);
                }
                break;
        }
    }

    private void getTestBigTypes(int type) {
        Http.http.createApi(CalcAPI.class)
                .getTestBigTypes(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<TestBigType>>() {
                    @Override
                    public void success(List<TestBigType> result) {
                        stringList.clear();
                        for (TestBigType testBigType : result) {
                            String price = "<font color=\"#FF0000\">¥" + (double) testBigType.getPrice() / 100 + "</font>";
                            if (isSimple) {
                                stringList.add(testBigType.getQuestion_type_id() + "@" + testBigType.getName() + "        " + price);
                            } else {
                                stringList.add(testBigType.getQuestion_type_id() + "@" + testBigType.getName());
                            }
                        }
                        new TypeDialog.Builder(mActivity, stringList, "MainChooseType").create().show();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void showDialog(SignInfo signInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AlertDialog);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sign, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_sign_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                WebViewActivity.open(mActivity, "签到规则", HttpConstData.ROOT+"user/login/index4.html");
            }
        });
        view.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showPopWindow();
            }
        });

        TextView tvTotalPoint = view.findViewById(R.id.tv_total_point);
        tvTotalPoint.setText("  " + signInfo.getSuanfen() + "");

        BGButton bgButton = view.findViewById(R.id.btn_signed_daycount);
        bgButton.setText("已连续签到" + signInfo.getSign_sum() + "天");

        TextView tv_1 = view.findViewById(R.id.tv_1);
        TextView tv_2 = view.findViewById(R.id.tv_2);
        TextView tv_3 = view.findViewById(R.id.tv_3);
        TextView tv_4 = view.findViewById(R.id.tv_4);
        TextView tv_5 = view.findViewById(R.id.tv_5);
        TextView tv_6 = view.findViewById(R.id.tv_6);
        TextView tv_7 = view.findViewById(R.id.tv_7);

        ImageView iv_1 = view.findViewById(R.id.iv_1);
        ImageView iv_2 = view.findViewById(R.id.iv_2);
        ImageView iv_3 = view.findViewById(R.id.iv_3);
        ImageView iv_4 = view.findViewById(R.id.iv_4);
        ImageView iv_5 = view.findViewById(R.id.iv_5);
        ImageView iv_6 = view.findViewById(R.id.iv_6);
        ImageView iv_7 = view.findViewById(R.id.iv_7);

        switch (signInfo.getSign_sum()) {
            case 1:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 2:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 3:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                tv_3.setTextColor(Color.parseColor("#A096DE"));
                iv_3.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 4:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                tv_3.setTextColor(Color.parseColor("#A096DE"));
                iv_3.setImageResource(R.mipmap.sign_status_signed);
                tv_4.setTextColor(Color.parseColor("#A096DE"));
                iv_4.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 5:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                tv_3.setTextColor(Color.parseColor("#A096DE"));
                iv_3.setImageResource(R.mipmap.sign_status_signed);
                tv_4.setTextColor(Color.parseColor("#A096DE"));
                iv_4.setImageResource(R.mipmap.sign_status_signed);
                tv_5.setTextColor(Color.parseColor("#A096DE"));
                iv_5.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 6:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                tv_3.setTextColor(Color.parseColor("#A096DE"));
                iv_3.setImageResource(R.mipmap.sign_status_signed);
                tv_4.setTextColor(Color.parseColor("#A096DE"));
                iv_4.setImageResource(R.mipmap.sign_status_signed);
                tv_5.setTextColor(Color.parseColor("#A096DE"));
                iv_5.setImageResource(R.mipmap.sign_status_signed);
                tv_6.setTextColor(Color.parseColor("#A096DE"));
                iv_6.setImageResource(R.mipmap.sign_status_signed);
                break;
            case 7:
                tv_1.setTextColor(Color.parseColor("#A096DE"));
                iv_1.setImageResource(R.mipmap.sign_status_signed);
                tv_2.setTextColor(Color.parseColor("#A096DE"));
                iv_2.setImageResource(R.mipmap.sign_status_signed);
                tv_3.setTextColor(Color.parseColor("#A096DE"));
                iv_3.setImageResource(R.mipmap.sign_status_signed);
                tv_4.setTextColor(Color.parseColor("#A096DE"));
                iv_4.setImageResource(R.mipmap.sign_status_signed);
                tv_5.setTextColor(Color.parseColor("#A096DE"));
                iv_5.setImageResource(R.mipmap.sign_status_signed);
                tv_6.setTextColor(Color.parseColor("#A096DE"));
                iv_6.setImageResource(R.mipmap.sign_status_signed);
                tv_7.setTextColor(Color.parseColor("#A096DE"));
                iv_7.setImageResource(R.mipmap.sign_status_signed);
                break;
        }
        dialog.show();
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
                .getShareInfo(1)
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
