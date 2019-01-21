package com.bunnybear.suanhu.master.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.bunnybear.suanhu.master.R;
import com.bunnybear.suanhu.master.api.MineAPI;
import com.bunnybear.suanhu.master.base.AppActivity;
import com.bunnybear.suanhu.master.bean.SimpleTestDetail;
import com.bunnybear.suanhu.master.bean.User;
import com.bunnybear.suanhu.master.net.AppSubscriber;
import com.bunnybear.suanhu.master.net.Http;
import com.bunnybear.suanhu.master.oss.OssUtil;
import com.bunnybear.suanhu.master.ui.adapter.QuestionImageAdapter;
import com.bunnybear.suanhu.master.ui.adapter.SimpleTestGvAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EndSimpleTestDetailActivity extends AppActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gv)
    GridView gv;

    public static void open(AppActivity activity, int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderId", orderId);
        activity.startForResult(bundle,1001, EndSimpleTestDetailActivity.class);
    }

    SimpleTestDetail simpleTestDetail;
    int orderId;
    SimpleTestGvAdapter adapter;
    List<LocalMedia> mediaList = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_simpel_test_detail;
    }

    @Override
    protected String setTitleStr() {
        return "问答详情";
    }

    @Override
    protected void init() {
        orderId = getIntent().getIntExtra("orderId", -1);

        LocalMedia media = new LocalMedia();
        media.setPath("default");
        mediaList.add(media);
        adapter = new SimpleTestGvAdapter(this, mediaList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);


        getData();
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submitFirst();
                break;
        }
    }

    @Override
    protected void leftButtonclick(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    private void getData() {
        Http.http.createApi(MineAPI.class)
                .getSimpleTestDetail(orderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<SimpleTestDetail>() {
                    @Override
                    public void success(SimpleTestDetail result) {
                        simpleTestDetail = result;
                        GlideUtil.load(result.getUser_avatar(), ivHead);
                        tvName.setText(result.getUser_name());
                        tvContent.setText(result.getQuestion());
                        GridLayoutManager manager = new GridLayoutManager(EndSimpleTestDetailActivity.this, 3);
                        rv.setLayoutManager(manager);
                        if (result.getQuestion_image().size() > 0) {
                            rv.setVisibility(View.VISIBLE);
                            QuestionImageAdapter adapter = new QuestionImageAdapter(EndSimpleTestDetailActivity.this, result.getQuestion_image());
                            rv.setAdapter(adapter);
                        } else {
                            rv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
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
                .isCamera(true)
                .isZoomAnim(true)
                .synOrAsy(false)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    private void submitFirst() {
        if (simpleTestDetail != null) {
            final String content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                showMessage("回答不能为空");
                return;
            }
            List<LocalMedia> tempList = new ArrayList<>();
            for (LocalMedia media : mediaList) {
                if (!"default".equals(media.getPath())) {
                    tempList.add(media);
                }
            }
            if (tempList.size() > 0) {
                showProgressDialog("图片上传中...");
                OssUtil.uploadImages(this, tempList, tempList.size(), 0, new OssUtil.UploadCallBack() {
                    @Override
                    public void onCompleted(List<String> imageUrls) {
                        hideProgressDialog();
                        submitSecond(content, imageUrls);
                    }

                    @Override
                    public void onError(Exception e) {
                        showMessage(e.toString());
                    }
                });
            } else {
                submitSecond(content, null);
            }
        }
    }

    private void submitSecond(String content, List<String> results) {
        showProgressDialog("提交中...");
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("question_id", simpleTestDetail.getQuestion_id() + "");
        map.put("order_use_sn", simpleTestDetail.getOrder_use_sn() + "");
        if (results != null) {
            String result = "";
            for (String url : results) {
                result += url + ",";
            }
            map.put("more", result.substring(0, result.length() - 1));
        }
        Http.http.createApi(MineAPI.class)
                .answerSimpleTest(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        OssUtil.tempList.clear();
                        showMessage("回答成功");
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
