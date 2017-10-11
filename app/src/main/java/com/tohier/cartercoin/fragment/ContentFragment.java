package com.tohier.cartercoin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ContentFragment extends BaseFragment {

    private View view;
    private WebView webView;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content,container,false);
        webView = (WebView)view.findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return false;
            }
        });

        webView.loadUrl(url);
        return view;
    }

    @Override
    public void initData() {

    }
}
