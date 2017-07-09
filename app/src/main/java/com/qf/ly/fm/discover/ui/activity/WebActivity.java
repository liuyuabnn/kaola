package com.qf.ly.fm.discover.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;

public class WebActivity extends AppCompatActivity {
    private TuiJian2 tuiJian2;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        tuiJian2 = intent.getParcelableExtra("tuiJian2");

        /**
         * WebSettings
         */
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//让webview支持javascript
        settings.setDefaultTextEncodingName("utf-8");//设置字符集编码格式，用来解决网页乱码问题
        //下面两个属性一起使用
        settings.setLoadWithOverviewMode(true);//设置网页缩放至屏幕大小（pc端）
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        /**
         * WebChromeClient支持特殊的javascript
         */
        webView.setWebChromeClient(new WebChromeClient());

        /**
         * WebViewClient
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("print", "请求链接: " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("print", "开始加载链接: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("print", "加载链接结束: " + url);
                super.onPageFinished(view, url);
            }
        });//所有请求在本地webview打开

        //加载一个网址
        webView.loadUrl(tuiJian2.getWeburl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //系统返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                //能返回就返回到webview
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
