package com.bunnybear.suanhu.ui.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.bunnybear.suanhu.bean.Member;
import com.bunnybear.suanhu.bean.User;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.adapter.PopAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.utils.ScreenUtil;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.ClearEditText;
import com.xiaoxiong.library.view.CustomPopWindow;
import com.xiaoxiong.library.view.datepicker.CalendarSelector;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditMemberInfoActivity extends AppActivity implements CalendarSelector.ICalendarSelectorCallBack, TagFlowLayout.OnTagClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tagFlowLayout)
    TagFlowLayout tagFlowLayout;

    public static void open(AppActivity activity, Member member) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("member", member);
        activity.startActivity(bundle, EditMemberInfoActivity.class);
    }

    PromptDialog promptDialog;
    PromptButton cancelBtn, manBtn, womanBtn;
    CustomPopWindow mListPopWindow;
    String[] times = {"早子时 00:00-01:00", "丑时 01:00-03:00", "寅时 03:00-05:00", "卯时 05:00-07:00",
            "辰时 07:00-09:00", "巳时 09:00-11:00", "午时 11:00-13:00", "未时 13:00-15:00",
            "申时 15:00-17:00", "酉时 17:00-19:00", "戌时 19:00-21:00", "亥时 21:00-23:00", "夜子时 23:00-24:00"};

    String[] types = {"我的", "亲人", "朋友", "其他"};
    MyTagAdapter tagAdapter;
    CalendarSelector mCalendarSelector;

    Member member;
    int tagPosition = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_edit_member_info;
    }

    @Override
    protected String setTitleStr() {
        return "生辰八字";
    }

    @Override
    protected void init() {
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        initView();

        member = (Member) getIntent().getSerializableExtra("member");
        if (member != null) {
            etName.setText(member.getF_name());
            tvSex.setText(member.getSex());
            String birthType = member.getLunar() == 0 ? "【公历】" : "【农历】";
            String date = DateUtil.getMillon(member.getBirth() * 1000, DateUtil.FORMAT_YMD_CN);
            if (member.getIs_run() == 1){
                String year = date.substring(0,5);
                String monthAndDay = date.substring(5);
                date = year + "闰" + monthAndDay;
            }
            tvDate.setText(birthType + date);
            tvTime.setText(member.getLast_birth());
            for (int i = 0; i < types.length; i++) {
                if (member.getTag().equals(types[i])) {
                    tagPosition = i;
                    tagAdapter.setSelectedList(i);
                }
            }

        }


    }

    private void initView() {
        mCalendarSelector = new CalendarSelector(this, 0, this);

        MyClickListener listener = new MyClickListener();
        cancelBtn = new PromptButton("取消", listener);
        cancelBtn.setTextColor(getResources().getColor(R.color.main_color));
        manBtn = new PromptButton("男", listener);
        womanBtn = new PromptButton("女", listener);

        tagAdapter = new MyTagAdapter(Arrays.asList(types));
        tagFlowLayout.setMaxSelectCount(1);
        tagFlowLayout.setOnTagClickListener(this);
        tagFlowLayout.setAdapter(tagAdapter);
        tagAdapter.setSelectedList(tagPosition);

    }

    @Override
    public void transmitPeriod(HashMap<String, Object> result) {
        String dateStr = (String) result.get("year") + result.get("month") + result.get("day");
        String type = (boolean) result.get("isLunar") ? "【农历】" : "【公历】";
        tvDate.setText(type + dateStr);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        tagPosition = position;
        return false;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        tvTime.setText(times[position].split(" ")[1]);
        mListPopWindow.dissmiss();
    }

    public class MyClickListener implements PromptButtonListener {
        @Override
        public void onClick(PromptButton promptButton) {
            if (promptButton == manBtn) {
                tvSex.setText("男");
            } else if (promptButton == womanBtn) {
                tvSex.setText("女");
            }
        }
    }

    @OnClick({R.id.ll_sex, R.id.ll_born_date, R.id.ll_born_time, R.id.btn_submit})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.ll_sex:
                promptDialog.showAlertSheet("", true, cancelBtn, womanBtn, manBtn);
                break;
            case R.id.ll_born_date:
                mCalendarSelector.show(tvDate);
                break;
            case R.id.ll_born_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                        String minuteStr = minute < 10 ? "0" + minute : minute + "";
                        tvTime.setText(hourStr+":"+minuteStr);
                    }
                }, 0, 0, true).show();
//                showPopListView();
                break;
            case R.id.btn_submit:
                save();
                break;
        }
    }

    private void showPopListView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(EditMemberInfoActivity.this) * 2 / 5)//显示大小
                .create()
                .showAtLocation(tagFlowLayout, Gravity.BOTTOM, 0, 0);
    }

    private void handleListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PopAdapter adapter = new PopAdapter(Arrays.asList(times));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }


    public class MyTagAdapter extends TagAdapter<String> {

        public MyTagAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, String o) {
            TextView tv = (TextView) EditMemberInfoActivity.this.getLayoutInflater().inflate(R.layout.item_tag, tagFlowLayout, false);
            tv.setText(o);
            return tv;
        }
    }


    private void save() {
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showMessage("姓名不能为空");
            return;
        }
        String sex = tvSex.getText().toString();
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
        String birth = DateUtil.dateToString(dateStr.substring(4), DateUtil.FORMAT_YMD_CN).substring(0, 11) + birthTime;
        final Map<String, String> map = new HashMap<>();
        if (member != null) {
            map.put("id", member.getId() + "");
        }
        map.put("is_run", isRun ? 1 + "" : 0 + "");
        map.put("name", name);
        map.put("sex", sex);
        map.put("lunar", lunar + "");
        map.put("birth", birth);
        map.put("tag", types[tagPosition]);

        Http.http.createApi(MineAPI.class)
                .editMember(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage(member != null ? "修改成功" : "新增成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

}
