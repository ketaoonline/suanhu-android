package com.bunnybear.suanhu.master.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunnybear.suanhu.master.R;
import com.xiaoxiong.library.base.ActivityManager;
import com.xiaoxiong.library.base.BaseActivity;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.utils.AntiShake;
import com.xiaoxiong.library.utils.statusbarcompat.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class AppActivity extends BaseActivity implements View.OnClickListener {
    public Activity mActivity;
    ImageView ivLeft;
    TextView tvTitle;
    TextView tvRight;
    ImageView ivRigth;
    RelativeLayout rlTitle;
    LinearLayout llContainer;

    private View contentView;
    private ProgressDialog dialog;
    private Unbinder unbinder;
    public AntiShake antiShake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.layout_app);

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setOnClickListener(this);
        ivRigth = (ImageView) findViewById(R.id.iv_right);
        ivRigth.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        setContent(initLayout());

        unbinder = ButterKnife.bind(mActivity);

        ActivityManager.getInstance().addActivity(this);

        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.main_text_checked));

        antiShake = new AntiShake();

        setTitle(setTitleStr());

        init();

    }

    protected abstract int initLayout();
    protected abstract String setTitleStr();
    protected abstract void init();

    protected void leftButtonclick(View view){
        this.finish();
    }

    private void setContent(int resId){
        contentView = LayoutInflater.from(this).inflate(resId, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        if (null != llContainer) {
            llContainer.addView(contentView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                leftButtonclick(view);
                break;
            case R.id.iv_right:
            case R.id.tv_right:
                rightButtonClick();
                break;
        }
    }

    protected void rightButtonClick(){

    }

    @Override
    protected boolean useEventBus() {
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        BusFactory.getBus().unregister(this);
        ActivityManager.getInstance().finishActivity(this);
    }

    public void showMessage(final Object message) {
        Toast.makeText(getApplicationContext(),message.toString(),Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String message) {
        dialog = ProgressDialog.show(mActivity, "", message + "");
    }

    public void showProgressDialogNoncancelability(String message) {
        dialog = ProgressDialog.show(mActivity, "", message + "");
        dialog.setCanceledOnTouchOutside(false);
    }

    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void setStatusBarIconDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {

        }
    }

    public LinearLayout getLlContainer() {
        return llContainer;
    }

    public View getContentView() {
        return contentView;
    }

    /**
     * 得到左边的按钮
     *
     * @return
     */
    public ImageView getBtnLeft() {
        return ivLeft;
    }

    /**
     * 得到右边的按钮
     *
     * @return
     */
    public TextView getBtnRight() {
        return tvRight;
    }

    public ImageView getImageRight() {
        return ivRigth;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (null != tvTitle) {
            tvTitle.setText(title);
        }
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(int resId) {
        tvTitle.setText(getString(resId));
    }

    /**
     * 设置左边按钮的图片资源
     *
     * @param resId
     */
    public void setBtnLeftRes(int resId) {
        if (null != ivLeft) {
            ivLeft.setImageResource(resId);
        }

    }

    /**
     * 隐藏标题栏
     */
    public void hideBaseTitleRelative() {
        if (null != rlTitle) {
            rlTitle.setVisibility(View.GONE);
        }
    }


    /**
     * 设置右边按钮的图片资源
     *
     * @param resId
     */
    public void setBtnRightRes(int resId) {
        if (null != ivRigth) {
            ivRigth.setImageResource(resId);
        }
    }

    /**
     * 隐藏上方的标题栏
     */
    public void hideTitleView() {
        if (null != tvTitle) {
            tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏左边的按钮
     */
    public void hideBtnLeft() {
        if (null != ivLeft) {
            ivLeft.setVisibility(View.GONE);
        }

    }

    /***
     * 隐藏右边的按钮
     */
    public void hideBtnRight() {
        if (null != tvRight) {
            tvRight.setVisibility(View.GONE);
        }
    }

    public void hideImageRight(){
        if (null != ivRigth) {
            ivRigth.setVisibility(View.GONE);
        }
    }


    /**
     * 展示左边的按钮
     */
    public void showBtnLeft() {
        if (null != ivLeft) {
            ivLeft.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 展示右边的按钮
     */
    public void showBtnRight() {
        if (null != tvRight) {
            tvRight.setVisibility(View.VISIBLE);
        }
    }

    public void startActivity(Bundle bundle, Class<?> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startForResult(Bundle bundle, int requestCode, Class<?> target) {
        Intent intent = new Intent(this, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
