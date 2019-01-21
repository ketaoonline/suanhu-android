package com.bunnybear.suanhu.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.net.HttpConstData;
import com.xiaoxiong.library.http.DownloadUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyMasterDesActivity extends AppActivity {

    String url = HttpConstData.ROOT+"upload/Master_v1.apk";
    @BindView(R.id.tv_download)
    TextView tvDownload;

    public static void open(AppActivity activity) {
        activity.startActivity(null, ApplyMasterDesActivity.class);
    }

    ProgressDialog progressDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_apply_master_des;
    }

    @Override
    protected String setTitleStr() {
        return "入驻流程说明";
    }

    @Override
    protected void init() {
        String str = "下载安装算乎大师端，<font color=\"#ff0000\">点击下载</font>";
        tvDownload.setText(Html.fromHtml(str));
    }

    @OnClick({R.id.tv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_download:
                downloadApk();
                break;
        }
    }

    private void downloadApk() {
        DownloadUtil.downloadApk(url, new DownloadUtil.DownloadListener() {
            @Override
            public void onStart() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(ApplyMasterDesActivity.this);
                        progressDialog.setTitle("安装包下载ing...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }
                });
            }

            @Override
            public void onProgress(int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress(progress);
                    }
                });
            }

            @Override
            public void onFinish(String filePath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        openFiles(new File(filePath));
                    }
                });
            }

            @Override
            public void onFailure(String failureMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        showMessage(failureMsg);
                    }
                });
            }
        });

    }

    private void openFiles(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(this, "com.bunnybear.suanhu.fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        startActivity(intent);

    }

}
