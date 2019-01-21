package com.bunnybear.suanhu.master.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.bean.GoodAt;
import com.bunnybear.suanhu.master.bean.User;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.oss.OssUtil;
import com.bunnybear.suanhu.master.ui.adapter.GoodAtAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.ClearEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyMasterActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_idcard)
    ClearEditText etIdcard;
    @BindView(R.id.tv_full_time)
    TextView tvFullTime;
    @BindView(R.id.tv_part_time_job)
    TextView tvPartTimeJob;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_no_face)
    TextView tvNoFace;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.tv_no_national_emblem)
    TextView tvNoNationalEmblem;
    @BindView(R.id.iv_national_emblem)
    ImageView ivNationalEmblem;
    @BindView(R.id.tv_introduce)
    EditText tvIntroduce;

    public static void open(AppActivity activity){
        activity.startActivity(null,ApplyMasterActivity.class);
    }
    GoodAtAdapter atAdapter;
    List<GoodAt> list = new ArrayList<>();
    int isFullTime = 1;
    String[] images = new String[2];
    List<LocalMedia> selectList = new ArrayList<>();
    @Override
    protected int initLayout() {
        return R.layout.activity_apply_master;
    }

    @Override
    protected String setTitleStr() {
        return "名师申请";
    }

    @Override
    protected void init() {
        rv.setLayoutManager(new GridLayoutManager(this,4));
        atAdapter = new GoodAtAdapter(list);
        rv.setAdapter(atAdapter);
        atAdapter.setOnItemClickListener(this);
        getGoodAt();
    }

    @OnClick({R.id.tv_full_time, R.id.tv_part_time_job, R.id.rl_face, R.id.rl_national_emblem, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_full_time:
                isFullTime = 1;
                tvFullTime.setTextColor(Color.parseColor("#ffffff"));
                tvFullTime.setBackgroundColor(Color.parseColor("#A398E6"));
                tvPartTimeJob.setTextColor(Color.parseColor("#333333"));
                tvPartTimeJob.setBackgroundColor(Color.parseColor("#eeeeee"));
                break;
            case R.id.tv_part_time_job:
                isFullTime = 0;
                tvFullTime.setTextColor(Color.parseColor("#333333"));
                tvFullTime.setBackgroundColor(Color.parseColor("#eeeeee"));
                tvPartTimeJob.setTextColor(Color.parseColor("#ffffff"));
                tvPartTimeJob.setBackgroundColor(Color.parseColor("#A398E6"));
                break;
            case R.id.rl_face:
                addPic(10000);
                break;
            case R.id.rl_national_emblem:
                addPic(10001);
                break;
            case R.id.btn_submit:
                submitFirst(etName.getText().toString(),etIdcard.getText().toString(),tvIntroduce.getText().toString());
                break;
        }
    }

    private void getGoodAt(){
        Http.http.createApi(MineAPI.class)
                .getGoodAts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<String>>() {
                    @Override
                    public void success(List<String> result) {
                        if (result.size() > 0){
                            tvPhone.setText(result.get(0));
                        }
                        for (String s : result) {
                            GoodAt goodAt = new GoodAt(s,false);
                            list.add(goodAt);
                        }
                        list.remove(0);
                        atAdapter.setNewData(list);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        list.get(position).setChecked(!list.get(position).isChecked());
        atAdapter.notifyItemChanged(position);
    }

    private void submitFirst(final String realName, final String idCard, final String introduce){
        if (TextUtils.isEmpty(realName)) {
            showMessage("真实姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            showMessage("身份证号不能为空");
            return;
        }
        if(idCard.length() != 18){
            showMessage("错误的身份证号");
            return;
        }
        String goodAtStr = "";
        for (GoodAt goodAt : list) {
            if (goodAt.isChecked()){
                goodAtStr += goodAt.getContent()+",";
            }
        }
        if (TextUtils.isEmpty(goodAtStr)) {
            showMessage("请选择您擅长的领域");
            return;
        }
        goodAtStr = goodAtStr.substring(0,goodAtStr.length()-1);
        if (TextUtils.isEmpty(images[0])){
            showMessage("请上传身份证人面像");
            return;
        }
        if (TextUtils.isEmpty(images[1])){
            showMessage("请上传身份证国徽像");
            return;
        }
        if (TextUtils.isEmpty(introduce)){
            showMessage("请填写个人介绍");
            return;
        }
        showProgressDialog("图片上传中...");
        final String finalGoodAtStr = goodAtStr;
        OssUtil.uploadImagesWithPath(this,Arrays.asList(images), selectList.size(), 0, new OssUtil.UploadCallBack() {
            @Override
            public void onCompleted(List<String> imageUrls) {
                hideProgressDialog();
                String urls = "";
                for (String imageUrl : imageUrls) {
                    urls += imageUrl +",";
                }
                submit(realName,idCard, finalGoodAtStr,urls.substring(0,urls.length()-1),introduce);
            }

            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                showMessage(e.toString());
            }
        });
    }

    private void submit(String realName,String idCard,String goodAt,String images,String introduce){
        Http.http.createApi(MineAPI.class)
                .submitApply(realName,idCard,isFullTime,goodAt,images,introduce)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showMessage("申请提交成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }

    private void addPic(int requestCode) {
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
                .isCamera(true)
                .imageFormat(PictureMimeType.JPEG)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10000://人面像
                    selectList = PictureSelector.obtainMultipleResult(data);
                    tvNoFace.setVisibility(View.GONE);
                    ivFace.setVisibility(View.VISIBLE);
                    GlideUtil.load(selectList.get(0).getCompressPath(),ivFace);
                    images[0] = selectList.get(0).getCompressPath();
                    break;
                case 10001://国徽面
                    selectList = PictureSelector.obtainMultipleResult(data);
                    tvNoNationalEmblem.setVisibility(View.GONE);
                    ivNationalEmblem.setVisibility(View.VISIBLE);
                    GlideUtil.load(selectList.get(0).getCompressPath(),ivNationalEmblem);
                    images[1] = selectList.get(0).getCompressPath();
                    break;
            }
        }
    }

}
