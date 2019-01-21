package com.bunnybear.suanhu.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.ui.adapter.DialogTestBigTypeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.utils.DensityUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class TypeDialog extends Dialog {

    public TypeDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    public TypeDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(true);
    }

    public static class Builder {
        private Context context;
        private TypeDialog mDialog;
        private OnClickListener confirmBtnClickListener;

        List<String> types = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        String eventBusRequestCode;
        public Builder(Context context, List<String> types,String eventBusRequestCode) {
            this.context = context;
            this.eventBusRequestCode = eventBusRequestCode;
            for (String type : types) {
                String[] sts = type.split("@");
                ids.add(sts[0]);
                this.types.add(sts[1]);
            }
        }

        public Builder setConfirmBtn(OnClickListener listener) {
            this.confirmBtnClickListener = listener;
            return this;
        }

        public TypeDialog create() {
            mDialog = new TypeDialog(context, R.style.NormalDialog);
            View layout = LayoutInflater.from(context).inflate(R.layout.dialog_type_choose, null);
            mDialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            RecyclerView recyclerView = layout.findViewById(R.id.rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#ffffff"),2, DensityUtil.dp2px(context,10)));
            DialogTestBigTypeAdapter mAdapter = new DialogTestBigTypeAdapter(types);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mAdapter.setCheckPosition(position);
                }
            });

            layout.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    BusFactory.getBus().post(new IEvent(eventBusRequestCode, ids.get(mAdapter.checkPosition)));
                }
            });
            mDialog.setContentView(layout);
            return mDialog;
        }
    }


}
