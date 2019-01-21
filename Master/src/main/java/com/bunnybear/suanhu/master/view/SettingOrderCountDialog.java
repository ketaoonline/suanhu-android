package com.bunnybear.suanhu.master.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.view.NormalDialog;

public class SettingOrderCountDialog extends Dialog {

    public SettingOrderCountDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    public SettingOrderCountDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(true);
    }

    public static class Builder {
        private Context context;
        private SettingOrderCountDialog mDialog;
        private OnClickListener cancelBtnClickListener;
        private OnClickListener confirmBtnClickListener;
        private String message, sureName,sureNameColor, cancelName,cancelNameColor;
        private int oldCount;
        public Builder(Context context,int oldCount) {
            this.context = context;
            this.oldCount = oldCount;
        }

        public SettingOrderCountDialog.Builder setConfirmBtn(OnClickListener listener) {
            this.confirmBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setConfirmBtn(String sureName, OnClickListener listener) {
            this.sureName = sureName;
            this.confirmBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setConfirmBtn(String sureName, String sureNameColor, OnClickListener listener) {
            this.sureName = sureName;
            this.sureNameColor = sureNameColor;
            this.confirmBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setCancelBtn(OnClickListener listener) {
            this.cancelBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setCancelBtn(String cancelName, OnClickListener listener) {
            this.cancelName = cancelName;
            this.cancelBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setCancelBtn(String cancelName, String cancelNameColor, OnClickListener listener) {
            this.cancelName = cancelName;
            this.cancelNameColor = cancelNameColor;
            this.cancelBtnClickListener = listener;
            return this;
        }

        public SettingOrderCountDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SettingOrderCountDialog create() {
            mDialog = new SettingOrderCountDialog(context, com.xiaoxiong.library.R.style.NormalDialog);
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_set_order_count, null);
            mDialog.addContentView(layout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            final TextView tvCount = layout.findViewById(R.id.tv_count);
            tvCount.setText(oldCount+"");
            TextView tvReduce = layout.findViewById(R.id.tv_reduce);
            TextView tvAdd = layout.findViewById(R.id.tv_add);
            tvReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.valueOf(tvCount.getText().toString());
                    if (count>0){
                        count--;
                        tvCount.setText(count+"");
                    }
                }
            });
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.valueOf(tvCount.getText().toString());
                    count++;
                    tvCount.setText(count+"");
                }
            });

            layout.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    int count = Integer.valueOf(tvCount.getText().toString());
                    BusFactory.getBus().post(new IEvent("SettingOrderCount",count));
                }
            });
            layout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mDialog.setContentView(layout);
            return mDialog;
        }
    }

}
