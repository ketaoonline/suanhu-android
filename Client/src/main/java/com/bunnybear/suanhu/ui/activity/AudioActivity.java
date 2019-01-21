package com.bunnybear.suanhu.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Lesson;
import com.bunnybear.suanhu.bean.PlayDetail;
import com.bunnybear.suanhu.bean.ShareBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.AudioAdapter;
import com.bunnybear.suanhu.util.share.QQShareUtil;
import com.bunnybear.suanhu.util.share.WeiXinShareUtil;
import com.bunnybear.suanhu.view.SharePopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ybq.android.spinkit.style.Circle;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.LogUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.ywl5320.bean.TimeBean;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.libenum.SampleRateEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnPauseResumeListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.util.WlTimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener, OnPreparedListener, OnInfoListener, OnCompleteListener, OnPauseResumeListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_audio_info)
    TextView tvAudioInfo;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_collect)
    TextView tvCollect;

    public static void open(int lessonId, AppActivity activity, int courseId) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", lessonId);
        bundle.putInt("courseId", courseId);
        activity.startActivity(bundle, AudioActivity.class);
    }

    private Circle mCircleDrawable;
    List<Lesson> list = new ArrayList<>();
    AudioAdapter audioAdapter;
    boolean isPause = false;
    Lesson currentLesson = null;
    int currentPosition = 0;
    int lessonId, courseId;

    WlMusic wlMusic;
    PlayDetail playDetail;

    @Override
    protected int initLayout() {
        return R.layout.activity_audio;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {
        lessonId = getIntent().getIntExtra("id", -1);
        courseId = getIntent().getIntExtra("courseId", -1);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        audioAdapter = new AudioAdapter(list);
        recyclerView.setAdapter(audioAdapter);
        audioAdapter.setOnItemClickListener(this);

        initAudio();

        getPlayDetail();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == audioAdapter.checkPosition) return;
        audioAdapter.setCheckPosition(position);
        setCurrentLesson(position);
    }

    private void initAudio() {
        wlMusic = WlMusic.getInstance();
        wlMusic.setCallBackPcmData(true);//是否返回音频PCM数据
        wlMusic.setShowPCMDB(true);//是否返回音频分贝大小
        wlMusic.setPlayCircle(true); //设置不间断循环播放音频
        wlMusic.setVolume(65); //设置音量 65%
        wlMusic.setPlaySpeed(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setPlayPitch(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setMute(MuteEnum.MUTE_CENTER); //设置立体声（左声道、右声道和立体声）
        wlMusic.setConvertSampleRate(SampleRateEnum.RATE_44100);//设定恒定采样率（null为取消）
        wlMusic.setOnCompleteListener(this);
        wlMusic.setOnPauseResumeListener(this);
        wlMusic.setOnInfoListener(this);
        wlMusic.setOnPreparedListener(this);
    }


    @OnClick({R.id.iv_last, R.id.iv_play, R.id.iv_next, R.id.tv_share, R.id.tv_collect})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.iv_last:
                prev();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.tv_share:
                showPopWindow();
                break;
            case R.id.tv_collect:
                if (playDetail != null) {
                    if (playDetail.getCollection() == 0) {
                        collect();
                    } else {
                        cancelCollect();
                    }
                }
                break;
        }
    }

    @Override
    protected void leftButtonclick(View view) {
        submitStudyProgress();
    }

    @Override
    public void onBackPressed() {
        submitStudyProgress();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        wlMusic.stop();
        super.onDestroy();
    }

    /**
     * 播放
     */
    private void play() {
        if (wlMusic.isPlaying()) {
            if (isPause) {
                ivPlay.setImageResource(R.mipmap.audio_pause);
                wlMusic.resume();
            } else {
                ivPlay.setImageResource(R.mipmap.audio_play);
                wlMusic.pause();
            }
        } else {
            if (currentLesson != null) {
                wlMusic.setSource(currentLesson.getUrl());
                wlMusic.prePared();
                showLoading();
            }
        }
    }

    /**
     * 下一首
     */
    private void next() {
        if (list.size() - 1 > currentPosition) {
            currentPosition++;
            setCurrentLesson(currentPosition);
            audioAdapter.setCheckPosition(currentPosition);
        } else {
            showMessage("没有下一首了");
        }
    }

    /**
     * 上一首
     */
    private void prev() {
        if (currentPosition > 0) {
            currentPosition--;
            setCurrentLesson(currentPosition);
            audioAdapter.setCheckPosition(currentPosition);
        } else {
            showMessage("没有上一首了");
        }
    }


    private void showLoading() {
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 0, 0);
        mCircleDrawable.setColor(Color.WHITE);
        mCircleDrawable.start();
        ivPlay.setImageDrawable(mCircleDrawable);
    }

    private void hideLoading() {
        if (mCircleDrawable != null) {
            mCircleDrawable.stop();
            mCircleDrawable = null;
        }
        ivPlay.setImageResource(R.mipmap.audio_pause);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initAudio();
    }

    @Override
    public void onPrepared() {//准备完毕
        isPause = false;
        wlMusic.start();
        Message message = Message.obtain();
        message.what = 0;
        handler.sendMessage(message);

    }

    @Override
    public void onPause(boolean pause) {//播放暂停
        isPause = pause;
    }

    @Override
    public void onComplete() {//播放完成
        Message message = Message.obtain();
        message.what = 2;
        handler.sendMessage(message);

    }

    @Override
    public void onInfo(TimeBean timeBean) {//播放中的信息
        Message message = Message.obtain();
        message.obj = timeBean;
        message.what = 1;
        handler.sendMessage(message);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://准备好了
                    hideLoading();
                    break;
                case 1://播放中
                    TimeBean timeBean = (TimeBean) msg.obj;
                    int progress = timeBean.getCurrSecs() * 100 / timeBean.getTotalSecs();
                    if (progressBar != null) {
                        progressBar.setProgress(progress);
                        tvTime.setText(WlTimeUtil.secdsToDateFormat(timeBean.getCurrSecs(), timeBean.getTotalSecs()) + "/" + WlTimeUtil.secdsToDateFormat(timeBean.getTotalSecs(), timeBean.getTotalSecs()));
                        if (progress == 100) {
                            wlMusic.pause();
                            ivPlay.setImageResource(R.mipmap.audio_play);
                            onComplete();
                        }
                    }
                    break;
                case 2://播放完
                    if (list.size() - 1 > currentPosition) {
                        currentPosition++;
                        setCurrentLesson(currentPosition);
                        audioAdapter.setCheckPosition(currentPosition);
                    } else {
                        wlMusic.stop();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private void getPlayDetail() {
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(ClassAPI.class)
                .getPlayDetail(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<PlayDetail>() {
                    @Override
                    public void success(PlayDetail result) {
                        list.clear();
                        list.addAll(result.getCatalog_info());
                        audioAdapter.setNewData(list);
                        playDetail = result;
                        tvCollect.setCompoundDrawablesWithIntrinsicBounds(playDetail.getCollection() == 1 ? R.mipmap.middle_yellow_star : R.mipmap.white_star, 0, 0, 0);
                        GlideUtil.load(result.getHead_banner(), iv);
                        tvName.setText(result.getName() + "【" + list.size() + "集】");
                        tvTotalCount.setText("共" + list.size() + "课时");
                        for (int i = 0; i < result.getCatalog_info().size(); i++) {
                            if (courseId > 0) {
                                audioAdapter.setCheckPosition(0);
                                currentLesson = list.get(0);
                                currentPosition = 0;
                                tvAudioInfo.setText(currentLesson.getSension() + "  " + currentLesson.getName());
                                tvContent.setText(currentLesson.getText_url());
                                wlMusic.setSource(currentLesson.getUrl());
                                wlMusic.prePared();
                                showLoading();
                                break;
                            } else {
                                if (result.getCatalog_info().get(i).getId() == lessonId) {
                                    audioAdapter.setCheckPosition(i);
                                    currentLesson = list.get(i);
                                    currentPosition = i;
                                    tvAudioInfo.setText(currentLesson.getSension() + "  " + currentLesson.getName());
                                    tvContent.setText(currentLesson.getText_url());
                                    wlMusic.setSource(currentLesson.getUrl());
                                    wlMusic.prePared();
                                    showLoading();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void setCurrentLesson(int position) {
        this.currentLesson = list.get(position);
        this.currentPosition = position;
        tvAudioInfo.setText(currentLesson.getSension() + "  " + currentLesson.getName());
        wlMusic.playNext(currentLesson.getUrl());
        showLoading();
        progressBar.setProgress(0);
        tvTime.setText("00:00/00:00");
        tvContent.setText(currentLesson.getText_url());
    }


    private void submitStudyProgress() {
        int checkPosition = audioAdapter.checkPosition;
        Map<String, String> map = new HashMap<>();
        if (courseId > 0) {
            map.put("course_id", courseId + "");
        } else {
            map.put("course_info_id", lessonId + "");
        }
        map.put("couese_much", checkPosition + 1 + "");
        Http.http.createApi(ClassAPI.class)
                .submitStudyProgress(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        finish();
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
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(MineAPI.class)
                .collect(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("收藏成功");
                        if (playDetail != null) {
                            playDetail.setCollection(1);
                            tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.middle_yellow_star, 0, 0, 0);
                        }

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
        Map<String, String> map = new HashMap<>();
        map.put(courseId > 0 ? "course_id" : "course_info_id", courseId > 0 ? courseId + "" : lessonId + "");
        Http.http.createApi(MineAPI.class)
                .cancelCollect(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("取消收藏成功");
                        if (playDetail != null) {
                            playDetail.setCollection(0);
                            tvCollect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gray_star_big, 0, 0, 0);
                        }

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
                .getShareInfo(5)
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
