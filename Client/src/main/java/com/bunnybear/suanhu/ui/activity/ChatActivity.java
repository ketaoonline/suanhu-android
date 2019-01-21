package com.bunnybear.suanhu.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ChatMsgsResponse;
import com.bunnybear.suanhu.bean.ChatMsg;
import com.bunnybear.suanhu.bean.DetailedTest;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.oss.OssUtil;
import com.bunnybear.suanhu.ui.adapter.ChatAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.base.ImageActivity;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.CommonUtils;
import com.xiaoxiong.library.utils.SoftKeyboardStateHelper;
import com.xiaoxiong.library.utils.animation.JMatrixUtil;
import com.xiaoxiong.library.view.ContainsEmojiEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatActivity extends AppActivity implements View.OnTouchListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.twRefreshLayout)
    TwinklingRefreshLayout twRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    //    @BindView(R.id.rv)
//    SwipeMenuRecyclerView rv;
    @BindView(R.id.et_content)
    ContainsEmojiEditText etContent;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    public static void open(AppActivity activity, DetailedTest detailedTest) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("DetailedTest", detailedTest);
        activity.startForResult(bundle, 1000, ChatActivity.class);
    }
    SoftKeyboardStateHelper softKeyboardStateHelper;

    List<ChatMsg> list = new ArrayList<>();
    ChatAdapter adapter;

    int page = 1;
    int totalPage;
    List<LocalMedia> selectList = new ArrayList<>();
    DetailedTest detailedTest;

    boolean isInit = false;
    Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    rv.scrollToPosition(list.size() - 1);
                    break;
                case 1://开始每间隔10秒获取一次最新消息
                    getMsg();
                    break;
            }
        }
    };


    @Override
    protected int initLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected String setTitleStr() {
        detailedTest = (DetailedTest) getIntent().getSerializableExtra("DetailedTest");
        return detailedTest.getMaster_name();
    }

    @Override
    protected void init() {
        if (detailedTest.getEnd_time() == 0) {
            getBtnRight().setText("完成对话");
            rlBottom.setVisibility(View.VISIBLE);
            getBtnRight().setVisibility(View.VISIBLE);
        }else {
            rlBottom.setVisibility(View.GONE);
            getBtnRight().setVisibility(View.GONE);
        }


        twRefreshLayout.setEnableLoadmore(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new ChatAdapter(list);
        rv.setAdapter(adapter);
        rv.setOnTouchListener(this);
        adapter.setOnItemChildClickListener(this);
        twRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page++;
                getHistory(true);
            }
        });

        softKeyboardStateHelper = new SoftKeyboardStateHelper(rlParent);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                handler.sendEmptyMessageDelayed(0, 250);
            }

            @Override
            public void onSoftKeyboardClosed() {

            }
        });

        getHistory(false);

    }

    @OnClick({R.id.iv_add_pic, R.id.iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_pic:
                addPic();
                break;
            case R.id.iv_send:
                sendMsg(null);
                break;
        }
    }

    @Override
    protected void leftButtonclick(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void rightButtonClick() {
        FinishChatActivity.open(this, detailedTest.getKey_id(), detailedTest.getMaster_avatar(), detailedTest.getOrder_use_sn());
    }


    /**
     * 发送消息
     */
    private void sendMsg(String imagUrl) {
        int type;
        String content;
        if (TextUtils.isEmpty(imagUrl)) {
            content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            type = 1;
        } else {
            content = imagUrl;
            type = 2;
        }
        int lastMsgId = list.size() > 0 ? list.get(list.size() - 1).getChat_id() : 0;
        Http.http.createApi(MineAPI.class)
                .sendMsg(detailedTest.getOrder_use_sn(), detailedTest.getKey_id(), type, content, lastMsgId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ChatMsg>>() {
                    @Override
                    public void success(List<ChatMsg> result) {
                        page = 1;
                        deleteHeadMsgs(result.size());
                        list.addAll(result);
                        adapter.setNewData(list);
                        if (list.size() > 0) {
                            rv.smoothScrollToPosition(list.size() - 1);
                        }
                        etContent.setText("");

                        if (!isInit) {
                            isInit = true;
                            startTiming();
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        CommonUtils.hideSoftInputFromWindow(this, etContent);
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_send_image:
            case R.id.iv_receive_image:
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(list.get(position).getContent());
                List<Rect> rects = new ArrayList<>();
                rects.add(JMatrixUtil.getDrawableBoundsInView((ImageView) view));
                ImageActivity.open(this, arrayList, 0, rects);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        sendImage();
                    }
                    break;
                case 1000:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    private void sendImage() {
        showProgressDialog("图片上传中...");
        OssUtil.uploadImages(this, selectList, selectList.size(), 0, new OssUtil.UploadCallBack() {
            @Override
            public void onCompleted(List<String> imageUrls) {
                hideProgressDialog();
                sendMsg(imageUrls.get(0));
                OssUtil.tempList.clear();
            }

            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                showMessage(e.toString());
            }
        });
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    private void addPic() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
//                .enableCrop(true)
//                .compress(true)
//                .cropCompressQuality(10)
                .withAspectRatio(1, 1)
                .isCamera(true)
                .imageFormat(PictureMimeType.JPEG)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    private void getHistory(boolean isLoadMore) {
        Http.http.createApi(MineAPI.class)
                .getChatRecord(detailedTest.getOrder_use_sn(), page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<ChatMsgsResponse>() {
                    @Override
                    public void success(ChatMsgsResponse result) {
                        totalPage = result.getTotal_page();
                        twRefreshLayout.setEnableRefresh(page < totalPage);
                        if (isLoadMore) {
                            list.addAll(0, result.getList());
                            twRefreshLayout.finishRefreshing();
                        } else {
                            list.clear();
                            list.addAll(result.getList());
                            if (list.size() > 0) {
                                rv.smoothScrollToPosition(list.size() - 1);
                            }
                        }
                        adapter.setNewData(list);
                        if (!isInit) {
                            isInit = true;
                            startTiming();
                        }

                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void startTiming() {
        if (list.size() > 0) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            }, 0, 10000);
        }
    }


    private void getMsg() {
        Http.http.createApi(MineAPI.class)
                .getReceiveMsgs(detailedTest.getOrder_use_sn(), list.get(list.size() - 1).getChat_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<ChatMsg>>() {
                    @Override
                    public void success(List<ChatMsg> result) {
                        if (result.size() > 0) {
                            page = 1;
                            deleteHeadMsgs(result.size());
                            list.addAll(result);
                            adapter.setNewData(list);
                            if (list.size() > 0) {
                                rv.smoothScrollToPosition(list.size() - 1);
                            }
                        }

                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void deleteHeadMsgs(int size) {
        int moreCount = list.size() + size - 20;
        if (moreCount > 0) {
            for (int i = 0; i < moreCount; i++) {
                list.remove(i);
            }
            twRefreshLayout.setEnableRefresh(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
