package com.bunnybear.suanhu.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;

public class PaySuccessDialog extends Dialog {
    public PaySuccessDialog(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    public PaySuccessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
    }

    protected PaySuccessDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCanceledOnTouchOutside(false);
    }

    public static class Builder{
        private PaySuccessDialog dialog;
        private Context context;
        private String msg;
        public Builder(Context context,String msg) {
            this.context = context;
            this.msg = msg;
        }
        public Builder(Context context) {
            this.context = context;
        }

        public PaySuccessDialog create() {
            dialog = new PaySuccessDialog(context,com.xiaoxiong.library.R.style.NormalDialog);
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_pay_success, null);
            dialog.addContentView(layout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            TextView textView = layout.findViewById(R.id.tv);
            if (!TextUtils.isEmpty(msg)){
                textView.setText(msg);
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }

}
