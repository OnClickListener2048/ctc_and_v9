package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.tohier.cartercoin.R;

public class AlipayActivity extends MyBackBaseActivity {

    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);

        web = (WebView) findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);

        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl(getIntent().getStringExtra("url"));
        finish();

    }
}
