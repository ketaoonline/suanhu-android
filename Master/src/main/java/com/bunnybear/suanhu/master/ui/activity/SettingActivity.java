package com.bunnybear.suanhu.master.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.base.ConstData;
import com.bunnybear.suanhu.master.bean.User;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.oss.OssUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.NormalDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends AppActivity {


    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_bank_card_num)
    TextView tvBankCardNum;

    public static void open(AppActivity activity) {
        activity.startActivity(null, SettingActivity.class);
    }

    PromptDialog promptDialog;
    PromptButton cancelBtn, cameraBtn, albumbtn, manBtn, womanBtn;
    List<LocalMedia> selectList = new ArrayList<>();

    User user;
    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected String setTitleStr() {
        return "个人设置";
    }

    @Override
    protected void init() {
        getBtnRight().setText("保存");

        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        initView();

    }

    private void initView() {
        MyClickListener listener = new MyClickListener();
        cancelBtn = new PromptButton("取消", listener);
        cancelBtn.setTextColor(getResources().getColor(R.color.main_text_checked));
        cameraBtn = new PromptButton("拍照", listener);
        albumbtn = new PromptButton("从相册选择", listener);
        manBtn = new PromptButton("男", listener);
        womanBtn = new PromptButton("女", listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    protected void rightButtonClick() {
        update();
    }

    @OnClick({R.id.rl_head, R.id.rl_nickname,R.id.btn_login_out,R.id.rl_bind_bank_card})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId()))return;
        switch (view.getId()) {
            case R.id.rl_head:
                promptDialog.showAlertSheet("", true, cancelBtn, albumbtn, cameraBtn);
                break;
            case R.id.rl_nickname:
                UpdateNicknameActivity.open(this,user.getMaster_name());
                break;
            case R.id.btn_login_out:
                showLoginOutDialog();
                break;
            case R.id.rl_bind_bank_card:
                BindBankCardActivity.open(this,user.getBank_number());
                break;
        }
    }

    public class MyClickListener implements PromptButtonListener {
        @Override
        public void onClick(PromptButton promptButton) {
            if (promptButton == cameraBtn) {
                toCamera();
            } else if (promptButton == albumbtn) {
                toAlbum();
            } else if (promptButton == manBtn) {
//                tvSex.setText("男");
            } else if (promptButton == womanBtn) {
//                tvSex.setText("女");
            }
        }
    }

    /**
     * 拍照
     */
    private void toCamera() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)
                .compress(true)
                .cropCompressQuality(10)
                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .isCamera(true)
                .openClickSound(true)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 相册选择
     */
    private void toAlbum() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .enableCrop(true)
                .compress(true)
                .cropCompressQuality(10)
                .withAspectRatio(1, 1)
                .isCamera(false)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
                        GlideUtil.load(media.getCompressPath(),ivHead);
                    }
                    break;
                case UpdateNicknameActivity.REQUEST_CODE:
                    tvNickname.setText(data.getStringExtra("nickname"));
                    break;
            }
        }
    }

    private void getUserInfo() {
        Http.http.createApi(MineAPI.class)
                .getUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<User>() {
                    @Override
                    public void success(User result) {
                        user = result;
                        GlideUtil.load(user.getAvatar(), ivHead);
                        tvNickname.setText(result.getMaster_name());
                        tvBankCardNum.setText(TextUtils.isEmpty(result.getBank_number())?"绑定银行卡":result.getBank_number());
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));

    }





    private void update(){
        final String name = tvNickname.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showMessage("姓名不能为空");
            return;
        }
        if (selectList.size() > 0){
            showProgressDialog("图片上传中...");
            OssUtil.uploadImages(this,selectList, selectList.size(), 0, new OssUtil.UploadCallBack() {
                @Override
                public void onCompleted(List<String> imageUrls) {
                    hideProgressDialog();
                    updateSecond(name,imageUrls.get(0));
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    showMessage(e.toString());
                }
            });
        }else{
            updateSecond(name,user.getAvatar());
        }

    }

    private void updateSecond(String name,String url){
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("avatar",url);
        showProgressDialog("修改中...");
        Http.http.createApi(MineAPI.class)
                .updateInfo(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        OssUtil.tempList.clear();
                        showMessage("修改成功");
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }

    /**
     * 退出登录弹窗
     */
    private void showLoginOutDialog() {
        NormalDialog.Builder builder = new NormalDialog.Builder(this);
        builder.setMessage("确定退出登录？");
        builder.setConfirmBtn(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JPushInterface.deleteAlias(SettingActivity.this,1);
                JPushInterface.stopPush(getApplicationContext());
                BusFactory.getBus().post(new IEvent(ConstData.LOGIN_OUT, null));
            }

        }).create().show();
    }

}
