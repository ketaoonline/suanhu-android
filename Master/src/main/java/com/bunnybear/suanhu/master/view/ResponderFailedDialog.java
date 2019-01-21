package com.bunnybear.suanhu.master.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bunnybear.suanhu.master.R;

public class ResponderFailedDialog extends Dialog {
    public ResponderFailedDialog(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    public ResponderFailedDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
    }

    protected ResponderFailedDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCanceledOnTouchOutside(false);
    }

    public static class Builder{
        private ResponderFailedDialog dialog;
        private Context context;
        public Builder(Context context) {
            this.context = context;
        }

        public ResponderFailedDialog create() {
            dialog = new ResponderFailedDialog(context,com.xiaoxiong.library.R.style.NormalDialog);
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_responder_failed, null);
            dialog.addContentView(layout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            dialog.setContentView(layout);
            return dialog;
        }

    }

}
