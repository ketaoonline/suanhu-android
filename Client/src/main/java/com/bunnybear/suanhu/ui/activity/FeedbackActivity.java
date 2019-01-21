package com.bunnybear.suanhu.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.model.OSSResult;
import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.net.RequestBodyUtil;
import com.bunnybear.suanhu.oss.OssManager;
import com.bunnybear.suanhu.oss.OssUtil;
import com.bunnybear.suanhu.ui.adapter.FeedbackAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedbackActivity extends AppActivity implements TextWatcher, AdapterView.OnItemClickListener {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.gv)
    GridView gv;

    public static void open(AppActivity activity) {
        activity.startActivity(null, FeedbackActivity.class);
    }

    FeedbackAdapter adapter;
    List<LocalMedia> mediaList = new ArrayList<>();

    PromptDialog promptDialog;
    PromptButton cancelBtn, cameraBtn, albumbtn;

    @Override
    protected int initLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected String setTitleStr() {
        return "帮助与反馈";
    }

    @Override
    protected void init() {
        etContent.addTextChangedListener(this);
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        initView();

        LocalMedia media = new LocalMedia();
        media.setPath("default");
        mediaList.add(media);
        adapter = new FeedbackAdapter(this, mediaList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }

    private void initView() {
        MyClickListener listener = new MyClickListener();
        cancelBtn = new PromptButton("取消", listener);
        cancelBtn.setTextColor(getResources().getColor(R.color.main_color));
        cameraBtn = new PromptButton("拍照", listener);
        albumbtn = new PromptButton("从相册选择", listener);
    }

    public class MyClickListener implements PromptButtonListener {
        @Override
        public void onClick(PromptButton promptButton) {
            if (promptButton == cameraBtn) {
                toCamera();
            } else if (promptButton == albumbtn) {
//                toAlbum();
            }
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.btn_submit:
                submitFirst();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
            tvCount.setText("0/300");
        } else {
            tvCount.setText(charSequence.length() + "/300");
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        List<LocalMedia> tempList = new ArrayList<>();
        for (LocalMedia localMedia : mediaList) {
            if (!"default".equals(localMedia.getPath())) {
                tempList.add(localMedia);
            }
        }
        toAlbum(tempList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    mediaList.clear();
                    mediaList = PictureSelector.obtainMultipleResult(data);
                    if (mediaList.size() < 3) {
                        LocalMedia media = new LocalMedia();
                        media.setPath("default");
                        mediaList.add(mediaList.size(), media);
                    }
                    adapter.setData(mediaList);
                    break;
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
//                .enableCrop(true)
//                .compress(true)
//                .cropCompressQuality(10)
//                .minimumCompressSize(100)
                .withAspectRatio(1, 1)
                .isCamera(true)
                .openClickSound(true)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 相册选择
     */
    private void toAlbum(List<LocalMedia> list) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(3)
                .imageSpanCount(4)
                .previewImage(true)
                .selectionMedia(list)
//                .enableCrop(true)
//                .compress(true)
//                .cropCompressQuality(10)
//                .withAspectRatio(1, 1)
                .isCamera(false)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    private void submitFirst(){
        final String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)){
            showMessage("意见不能为空");
            return;
        }

        List<LocalMedia> tempList = new ArrayList<>();
        for (LocalMedia media : mediaList) {
            if (!"default".equals(media.getPath())){
                tempList.add(media);
            }
        }
        if (tempList.size() > 0){
            showProgressDialog("图片上传中...");
            OssUtil.uploadImages(this,tempList, tempList.size(), 0, new OssUtil.UploadCallBack() {
                @Override
                public void onCompleted(List<String> imageUrls) {
                    hideProgressDialog();
                    submitSecond(content,imageUrls);
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog();
                    showMessage(e.toString());
                }
            });
        }else {
            submitSecond(content,null);
        }

    }

    private void submitSecond(String content,List<String> results){
        showProgressDialog("提交中...");
        Map<String,String> map = new HashMap<>();
        map.put("content",content);
        if (results != null){
            String result = "";
            for (String url : results) {
                result += url + ",";
            }
            map.put("more", result.substring(0,result.length()-1));
        }
        Http.http.createApi(MineAPI.class)
                .feedback(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        OssUtil.tempList.clear();
                        showMessage("提交成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }


}
