package com.bunnybear.suanhu.ui.activity;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.base.ConstData;
import com.bunnybear.suanhu.bean.User;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.oss.OssUtil;
import com.bunnybear.suanhu.ui.adapter.PopAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.ScreenUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.BGButton;
import com.xiaoxiong.library.view.CustomPopWindow;
import com.xiaoxiong.library.view.NormalDialog;
import com.xiaoxiong.library.view.datepicker.CalendarSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends AppActivity implements CalendarSelector.ICalendarSelectorCallBack {


    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;

    public static void open(AppActivity activity) {
        activity.startActivity(null, SettingActivity.class);
    }

    PromptDialog promptDialog;
    PromptButton cancelBtn, cameraBtn, albumbtn, manBtn, womanBtn;
    List<LocalMedia> selectList = new ArrayList<>();

    CalendarSelector mCalendarSelector;

    User user;
    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected String setTitleStr() {
        return "个人设置";
    }

    @Override
    protected void init() {
        getBtnRight().setText("保存");

        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        initView();

        getUserInfo();
    }

    private void initView() {
        mCalendarSelector = new CalendarSelector(this, 0, this);
        MyClickListener listener = new MyClickListener();
        cancelBtn = new PromptButton("取消", listener);
        cancelBtn.setTextColor(getResources().getColor(R.color.main_color));
        cameraBtn = new PromptButton("拍照", listener);
        albumbtn = new PromptButton("从相册选择", listener);
        manBtn = new PromptButton("男", listener);
        womanBtn = new PromptButton("女", listener);
    }

    @Override
    protected void rightButtonClick() {
        update();
    }

    @OnClick({R.id.rl_head, R.id.rl_nickname, R.id.rl_born_date, R.id.rl_born_time,R.id.btn_login_out})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId()))return;
        switch (view.getId()) {
            case R.id.rl_head:
                promptDialog.showAlertSheet("", true, cancelBtn, albumbtn, cameraBtn);
                break;
            case R.id.rl_nickname:
                UpdateNicknameActivity.open(this,user.getUser_nickname());
                break;
            case R.id.rl_born_date:
                mCalendarSelector.show(tvDate);
                break;
            case R.id.rl_born_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                        String minuteStr = minute < 10 ? "0" + minute : minute + "";
                        tvTime.setText(hourStr+":"+minuteStr);
                    }
                }, 0, 0, true).show();
                break;
            case R.id.btn_login_out:
                showLoginOutDialog();
                break;
        }
    }

    @Override
    public void transmitPeriod(HashMap<String, Object> result) {
        String dateStr = (String) result.get("year") + result.get("month") + result.get("day");
        String type = (boolean) result.get("isLunar") ? "【农历】" : "【公历】";
        tvDate.setText(type + dateStr);
    }

    public class MyClickListener implements PromptButtonListener {
        @Override
        public void onClick(PromptButton promptButton) {
            if (promptButton == cameraBtn) {
                toCamera();
            } else if (promptButton == albumbtn) {
                toAlbum();
            } else if (promptButton == manBtn) {
//                tvSex.setText("男");
            } else if (promptButton == womanBtn) {
//                tvSex.setText("女");
            }
        }
    }


    /**
     * 拍照
     */
    private void toCamera() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)
                .compress(true)
                .cropCompressQuality(10)
                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .isCamera(true)
                .openClickSound(true)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 相册选择
     */
    private void toAlbum() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .enableCrop(true)
                .compress(true)
                .cropCompressQuality(10)
                .withAspectRatio(1, 1)
                .isCamera(false)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
                        GlideUtil.load(media.getCompressPath(),ivHead);
                    }
                    break;
                case UpdateNicknameActivity.REQUEST_CODE:
                    tvNickname.setText(data.getStringExtra("nickname"));
                    break;
            }
        }
    }

    private void getUserInfo() {
        Http.http.createApi(MineAPI.class)
                .getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<User>() {
                    @Override
                    public void success(User result) {
                        user = result;
                        GlideUtil.load(user.getAvatar(), ivHead);
                        tvNickname.setText(result.getUser_nickname());
                        String birthType = user.getLunar() == 0 ? "【公历】" : "【农历】";
                        String date = DateUtil.getMillon(user.getBirth() * 1000, DateUtil.FORMAT_YMD_CN);
                        if (user.getIs_run() == 1){
                            String year = date.substring(0,5);
                            String monthAndDay = date.substring(5);
                            date = year + "闰" + monthAndDay;
                        }
                        tvDate.setText(birthType + date);
                        tvTime.setText(user.getLast_birth());
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }

    private void update(){
        String name = tvNickname.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showMessage("姓名不能为空");
            return;
        }
        String dateStr = tvDate.getText().toString();
        if (TextUtils.isEmpty(dateStr)) {
            showMessage("请选择出生日期");
            return;
        }
        int lunar = "【公历】".equals(dateStr.substring(0, 4)) ? 0 : 1;
        String birthTime = tvTime.getText().toString();
        if (TextUtils.isEmpty(birthTime)) {
            showMessage("请选择出生时间");
            return;
        }
        boolean isRun = false;
        if (dateStr.substring(4).contains("闰")) {
            isRun = true;
            dateStr = dateStr.replace("闰", "");
        }
        String is_run = isRun ? 1 + "" : 0 + "";
        String birth = DateUtil.dateToString(dateStr.substring(4), DateUtil.FORMAT_YMD_CN).substring(0, 11) + birthTime;

        if (selectList.size() > 0){
            showProgressDialog("图片上传中...");
            OssUtil.uploadImages(this,selectList, selectList.size(), 0, new OssUtil.UploadCallBack() {
                @Override
                public void onCompleted(List<String> imageUrls) {
                    hideProgressDialog();
                    updateSecond(name,lunar,birth,is_run,imageUrls.get(0));
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    showMessage(e.toString());
                }
            });
        }else{
            updateSecond(name,lunar,birth,is_run,user.getAvatar());
        }

    }

    private void updateSecond(String name,int lunar,String birth,String is_run,String url){
        Map<String,String> map = new HashMap<>();
        map.put("user_nickname",name);
        map.put("lunar",lunar+"");
        map.put("birth",birth);
        map.put("is_run",is_run);
        map.put("avatar",url);
        showProgressDialog("修改中...");
        Http.http.createApi(MineAPI.class)
                .updateInfo(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        OssUtil.tempList.clear();
                        showMessage("修改成功");
                        BusFactory.getBus().post(new IEvent("UserInfoUpdated", null));
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }

    /**
     * 退出登录弹窗
     */
    private void showLoginOutDialog() {
        NormalDialog.Builder builder = new NormalDialog.Builder(this);
        builder.setMessage("确定退出登录？");
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JPushInterface.deleteAlias(SettingActivity.this,1);
                JPushInterface.stopPush(getApplicationContext());
                BusFactory.getBus().post(new IEvent(ConstData.LOGIN_OUT, null));
            }

        }).create().show();
    }

}
