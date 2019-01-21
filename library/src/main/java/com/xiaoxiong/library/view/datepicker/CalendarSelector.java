package com.xiaoxiong.library.view.datepicker;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxiong.library.R;
import com.xiaoxiong.library.utils.DateUtil;
import com.xiaoxiong.library.view.tablayout.SegmentTabLayout;
import com.xiaoxiong.library.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cecil on 2017/8/22.
 * Email:guixixuan1120@outlook.com
 */

public class CalendarSelector implements OnTabSelectListener {

    private Activity mContext;
    private HashMap<String, Object> mMap;
    private ICalendarSelectorCallBack mCallBack;
    private StringScrollPicker mYearPicker;
    private StringScrollPicker mMonthPicker;
    private StringScrollPicker mDayPicker;
    private List<String> mYearList;
    private List<String> mMonthList;
    private List<String> mDayList;
    private TextView tvConfirm;
    private TextView tvCancel;
    private SegmentTabLayout segmentTabLayout;
    private PopupWindow mPopupWindow;
    private String mSelectedYear;
    private String mSelectedMonth;
    private String mSelectedDay;
    private int mYearPosition = 0;
    private int mMonthPosition = 0;
    private int mDayPosition = 0;
    private boolean isLunar = false;


    public interface ICalendarSelectorCallBack {
        void transmitPeriod(HashMap<String, Object> result);
    }

    public CalendarSelector(Activity context, int position,
                            ICalendarSelectorCallBack iCalendarSelectorCallBack) {
        this.mContext = context;
        this.mCallBack = iCalendarSelectorCallBack;
        this.mYearPosition = position;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mYearList = new ArrayList<>();
        mMonthList = new ArrayList<>();
        mDayList = new ArrayList<>();
        for (int i = 1901; i < 2100; i++) {
            mYearList.add(i + "年");
        }
        for (int i = 1; i < 13; i++) {
            mMonthList.add(i + "月");
        }
    }

    private void initView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.popup_window_calendar_selector,
                null);
        mYearPicker = v.findViewById(R.id.ssp_year);
        mMonthPicker = v.findViewById(R.id.ssp_month);
        mDayPicker = v.findViewById(R.id.ssp_day);
        tvConfirm = v.findViewById(R.id.tv_confirm);
        tvCancel = v.findViewById(R.id.tv_cancel);
        segmentTabLayout = v.findViewById(R.id.segmentTabLayout);
        segmentTabLayout.setTabData(new String[]{"公历", "农历"});
        segmentTabLayout.setOnTabSelectListener(this);
        initPicker();
        initPopup(v);
    }

    private void initPicker() {
        String dateStr = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
        String[] dates = dateStr.split("-");
        int year = Integer.valueOf(dates[0]);
        int month = Integer.valueOf(dates[1]);
        int day = Integer.valueOf(dates[2]);
        mYearPosition = year - 1901;
        mMonthPosition = month - 1;
        mDayPosition = day - 1;
        mYearPicker.setIsCirculation(false);
        mYearPicker.setData(mYearList);
        mYearPicker.setSelectedPosition(mYearPosition);
        mMap = Utils.parseAverageYear(1901 + mYearPosition + "年");
        setMonthList();
        mMonthPicker.setIsCirculation(false);
        mDayPicker.setIsCirculation(false);
        mSelectedYear = mYearList.get(mYearPosition);
        mSelectedMonth = mMonthList.get(mMonthPosition);
        mSelectedDay = mDayList.get(mDayPosition);
        mYearPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedYear = mYearList.get(position);
                mYearPosition = position;
                if (isLunar) {
                    mMap = Utils.parseLunarYear(mSelectedYear);
                } else {
                    mMap = Utils.parseAverageYear(mSelectedYear);
                }
                setMonthList();
            }
        });
        mMonthPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedMonth = mMonthList.get(position);
                mMonthPosition = position;
                setDayList();
            }
        });
        mDayPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedDay = mDayList.get(position);
                mDayPosition = position;
            }
        });
    }

    private void setMonthList() {
        mMonthList = (List<String>) mMap.get("month");
        mMonthPicker.setData(mMonthList);
        mMonthPosition = mMonthPosition >= mMonthList.size() ? mMonthList.size()
                - 1 :
                mMonthPosition;
        mMonthPicker.setSelectedPosition(mMonthPosition);
        setDayList();
    }

    private void setDayList() {
        mDayList = ((List<List<String>>) mMap.get("day")).get(mMonthPosition);
        mDayPicker.setData(mDayList);
        mDayPosition = mDayPosition >= mDayList.size() ? mDayList.size() - 1 :
                mDayPosition;
        mDayPicker.setSelectedPosition(mDayPosition);
    }

    private void initPopup(View v) {
        mPopupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(mContext, 1f);
            }
        });
    }

    private void initListener() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("year", mSelectedYear);
                map.put("month", mSelectedMonth);
                map.put("day", mSelectedDay);
                map.put("isLunar",isLunar);
                mCallBack.transmitPeriod(map);
                mPopupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0://国历
                if (!isLunar) {
                    return;
                }
                if ("1901年".equals(mYearPicker.getSelectedItem()) || "2099年".equals(mYearPicker
                        .getSelectedItem())) {
                    Toast.makeText(mContext, "因条件受限，本APP暂不提供1901年和2099年阴阳历数据的转换，感谢理解", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                isLunar = false;
                Log.d("Cecil", "lunar2Average");
                HashMap<String, Object> map = Utils.lunar2Average(mYearPicker.getSelectedItem(),
                        mMonthPosition, mDayPosition);
                mMonthPosition = (int) map.get("monthPosition");
                mDayPosition = (int) map.get("dayPosition");
                mYearPicker.setSelectedPosition((int) map.get("yearPosition"));
                mMap = Utils.parseAverageYear(mYearPicker.getSelectedItem());
                setMonthList();
                setDayList();
                break;
            case 1://农历
                if (isLunar) {
                    return;
                }
                if ("1901年".equals(mYearPicker.getSelectedItem()) || "2099年".equals(mYearPicker
                        .getSelectedItem())) {
                    Toast.makeText(mContext, "因条件受限，本APP暂不提供1901年和2099年阴阳历数据的转换，感谢理解", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                isLunar = true;
                Log.d("Cecil", "average2Lunar");
                mMap = Utils.average2Lunar(mYearPicker.getSelectedItem(),
                        mMonthPosition, mDayPosition);
                mMonthPosition = (int) mMap.get("monthPosition");
                mDayPosition = (int) mMap.get("dayPosition");
                mYearPicker.setSelectedPosition((int) mMap.get("yearPosition"));
                setMonthList();
                setDayList();
                break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    public void show(View v) {
        Utils.hideSoftInput(mContext);
        Utils.backgroundAlpha(mContext, 0.5f);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

}
