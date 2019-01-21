package com.xiaoxiong.library.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.xiaoxiong.library.R;


public class NormalDialog extends Dialog {

    public NormalDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    public NormalDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(true);
    }

    public static class Builder {
        private Context context;
        private NormalDialog mDialog;
        private OnClickListener cancelBtnClickListener;
        private OnClickListener confirmBtnClickListener;
        private String message, sureName,sureNameColor, cancelName,cancelNameColor;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setConfirmBtn(OnClickListener listener) {
            this.confirmBtnClickListener = listener;
            return this;
        }

        public Builder setConfirmBtn(String sureName, OnClickListener listener) {
            this.sureName = sureName;
            this.confirmBtnClickListener = listener;
            return this;
        }

        public Builder setConfirmBtn(String sureName, String sureNameColor, OnClickListener listener) {
            this.sureName = sureName;
            this.sureNameColor = sureNameColor;
            this.confirmBtnClickListener = listener;
            return this;
        }

        public Builder setCancelBtn(OnClickListener listener) {
            this.cancelBtnClickListener = listener;
            return this;
        }

        public Builder setCancelBtn(String cancelName, OnClickListener listener) {
            this.cancelName = cancelName;
            this.cancelBtnClickListener = listener;
            return this;
        }

        public Builder setCancelBtn(String cancelName, String cancelNameColor, OnClickListener listener) {
            this.cancelName = cancelName;
            this.cancelNameColor = cancelNameColor;
            this.cancelBtnClickListener = listener;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public NormalDialog create() {
            mDialog = new NormalDialog(context, R.style.NormalDialog);
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_login_out, null);
            mDialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            TextView tvSure = (TextView) layout.findViewById(R.id.tv_sure);
            tvSure.setText(!TextUtils.isEmpty(sureName) ? sureName : "确定");
            tvSure.setTextColor(!TextUtils.isEmpty(sureNameColor) ? Color.parseColor(sureNameColor) : Color.parseColor("#A096DE"));

            final TextView tvCancel = (TextView) layout.findViewById(R.id.tv_cancel);
            tvCancel.setText(!TextUtils.isEmpty(cancelName) ? cancelName : "取消");
            tvCancel.setTextColor(!TextUtils.isEmpty(cancelNameColor) ? Color.parseColor(cancelNameColor) : Color.parseColor("#909090"));

            layout.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmBtnClickListener.onClick(mDialog, Dialog.BUTTON1);
                }
            });
            layout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("取消".equals(tvCancel.getText().toString())){
                        mDialog.dismiss();
                    }else {
                        cancelBtnClickListener.onClick(mDialog, Dialog.BUTTON2);
                    }
                }
            });
            TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
            tv_message.setText(message);
            mDialog.setContentView(layout);
            return mDialog;
        }
    }


}
