package com.bunnybear.suanhu.master.ui.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.AuthAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.base.ConstData;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.orhanobut.hawk.Hawk;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.view.ClearEditText;
import com.xiaoxiong.library.view.CountDownTextView;
import com.xiaoxiong.library.view.CustomPopWindow;
import com.xiaoxiong.library.view.datepicker.CalendarSelector;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppActivity{

    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.tv_get_code)
    CountDownTextView tvGetCode;
    @BindView(R.id.et_code)
    ClearEditText etCode;
    @BindView(R.id.et_pwd)
    ClearEditText etPwd;
//    @BindView(R.id.tv_date)
//    TextView tvDate;
//    @BindView(R.id.tv_time)
//    TextView tvTime;
//    @BindView(R.id.iv_cb)
//    ImageView ivCb;

    public static void open(AppActivity activity) {
        activity.startActivity(null, RegisterActivity.class);
    }

    CustomPopWindow mListPopWindow;
    String[] times = {"早子时 00:00-01:00", "丑时 01:00-03:00", "寅时 03:00-05:00", "卯时 05:00-07:00",
            "辰时 07:00-09:00", "巳时 09:00-11:00", "午时 11:00-13:00", "未时 13:00-15:00",
            "申时 15:00-17:00", "酉时 17:00-19:00", "戌时 19:00-21:00", "亥时 21:00-23:00", "夜子时 23:00-24:00"};
    CalendarSelector mCalendarSelector;


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected String setTitleStr() {
        return "注册";
    }

    @Override
    protected void init() {
        tvGetCode.setNormalText("获取验证码")
                .setCountDownText("", "s")
                .setCloseKeepCountDown(false)
                .setCountDownClickable(false)
                .setShowFormatTime(false)
                .setOnCountDownStartListener(new CountDownTextView.OnCountDownStartListener() {
                    @Override
                    public void onStart() {
                    }
                })
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        tvGetCode.setText("获取验证码");
                        tvGetCode.setClickable(true);
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMsg(etPhone.getText().toString());
                    }
                });

//        mCalendarSelector = new CalendarSelector(this, 0, this);
    }


    @OnClick({R.id.btn_register})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
//            case R.id.rl_born_date:
//                mCalendarSelector.show(tvDate);
//                break;
//            case R.id.rl_born_time:
////                showPopListView();
//                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
//                        String minuteStr = minute < 10 ? "0" + minute : minute + "";
//                        tvTime.setText(hourStr+":"+minuteStr);
//                    }
//                }, 0, 0, true).show();
//                break;
//            case R.id.tv_agreement:
//                break;
//            case R.id.ll_agreement:
//                break;
            case R.id.btn_register:
                register(etPhone.getText().toString(), etCode.getText().toString(), etPwd.getText().toString());
                break;
        }
    }

//    @Override
//    public void transmitPeriod(HashMap<String, Object> result) {
//        String dateStr = (String) result.get("year") + result.get("month") + result.get("day");
//        String type = (boolean) result.get("isLunar") ? "【农历】" : "【公历】";
//        tvDate.setText(type + dateStr);
//    }
//
//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        tvTime.setText(times[position].split(" ")[1]);
//        mListPopWindow.dissmiss();
//    }

//    private void showPopListView() {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
//        //处理popWindow 显示内容
//        handleListView(contentView);
//        //创建并显示popWindow
//        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
//                .setView(contentView)
//                .enableBackgroundDark(true)
//                .setBgDarkAlpha(0.7f)
//                .size(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(RegisterActivity.this) * 2 / 5)//显示大小
//                .create()
//                .showAtLocation(tvDate, Gravity.BOTTOM, 0, 0);
//    }
//
//    private void handleListView(View contentView) {
//        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        PopAdapter adapter = new PopAdapter(Arrays.asList(times));
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(this);
//    }


    private void sendMsg(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            showMessage("手机号不能为空");
            return;
        }
        if (mobile.length() != 11) {
            showMessage("手机号不正确");
            return;
        }
        Http.http.createApi(AuthAPI.class)
                .sendMsg(mobile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        tvGetCode.startCountDown(60);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));


    }

    private void register(final String mobile, String code, String pwd) {
        if (TextUtils.isEmpty(mobile)) {
            showMessage("手机号不能为空");
            return;
        }
        if (mobile.length() != 11) {
            showMessage("手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showMessage("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showMessage("密码不能为空");
            return;
        }
        Http.http.createApi(AuthAPI.class)
                .register(mobile, code, pwd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        Hawk.put(ConstData.TOKEN, result);
                        JPushInterface.resumePush(getApplicationContext());
                        JPushInterface.setAlias(RegisterActivity.this, mobile, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                Log.d("set_alias", "set alias result is" + i);
                            }
                        });
                        showProgressDialog("登录中...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                                MainActivity.open(RegisterActivity.this);
                                finish();
                                ActivityManager.getInstance().finishActivity(LoginActivity.class);
                            }
                        }, 2000);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }
}
