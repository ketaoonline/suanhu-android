package com.bunnybear.suanhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;


import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.view.ProgressWebView;

import butterknife.BindView;

public class WebViewActivity extends AppActivity {
    @BindView(R.id.web_view)
    ProgressWebView webView;
    public static final String EXTRA_PARAMS = "params";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_POST = "post";
    public static final String EXTRA_URL = "url";

    @Override
    protected int initLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected String setTitleStr() {
        return null;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String titleStr = intent.getStringExtra(EXTRA_TITLE);
        String url = intent.getStringExtra(EXTRA_URL);

        webView.defaultSetting();
        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new JsInterface(this), "android");

        if (TextUtils.isEmpty(url)) {
            webView.loadUrl("https://www.baidu.com/");
            setTitle("网页");
        } else {
            webView.loadUrl(url);
            if (titleStr.length() > 10) {
                setTitle(titleStr.substring(0, 10) + "...");
            } else {
                setTitle(titleStr);
            }
        }


    }

    public static void open(AppActivity activity, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.EXTRA_TITLE, title);
        bundle.putString(WebViewActivity.EXTRA_URL, url);
        activity.startActivity(bundle, WebViewActivity.class);
    }

    public class JsInterface {

        AppActivity activity;

        public JsInterface(AppActivity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void toComment(int id) {
            CommentActivity.open(activity,id,"post");
        }
    }


}
