package com.bunnybear.suanhu.master.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.xiaoxiong.library.view.ClearEditText;

import butterknife.BindView;

public class UpdateNicknameActivity extends AppActivity {

    @BindView(R.id.et)
    ClearEditText et;

    public static final int REQUEST_CODE = 1000;

    public static void open(AppActivity activity,String nickname) {
        Bundle bundle = new Bundle();
        bundle.putString("nickname",nickname);
        activity.startForResult(bundle,REQUEST_CODE, UpdateNicknameActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_update_nickname;
    }

    @Override
    protected String setTitleStr() {
        return "修改昵称";
    }

    @Override
    protected void init() {
        getBtnRight().setText("确定");
        String name = getIntent().getStringExtra("nickname");
        et.setText(name);
        et.setSelection(name.length());
        et.requestFocus();
    }


    @Override
    protected void rightButtonClick() {
        String nickname = et.getText().toString();
        if (TextUtils.isEmpty(nickname)){
            showMessage("昵称不能为空");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("nickname",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }

}
