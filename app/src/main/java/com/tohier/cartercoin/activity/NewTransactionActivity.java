package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;

@SuppressLint("SetJavaScriptEnabled")
public class NewTransactionActivity extends MyBaseActivity implements View.OnClickListener{

	private WebView web_update_apk;
	private LoadingView loadingView;
	private NoLinkView ivNoLink;
	private String url;
//	public static NewTransactionActivity activity1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_transaction_url);

		init();
		setUpView();

	}

	public void click(View v)
	{
		finish();
	}

	private void setUpView() {
		ivNoLink.setOnClickListener(this);
	}
 
	private void init() {
		web_update_apk = (WebView)findViewById(R.id.web);
		loadingView = (LoadingView) findViewById(R.id.loading_view);
		web_update_apk.getSettings().setJavaScriptEnabled(true);

		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

//		activity1 = NewTransactionActivity.this;

		if (Tools.getAPNType(NewTransactionActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			web_update_apk.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadUrl(url);
					return false;
				}
			});

			web_update_apk.setWebChromeClient(new WebChromeClient());



			HttpConnect.post(this, "member_transaction_url", null, new Callback() {
				@Override
				public void onResponse(Response arg0) throws IOException {

					String str = arg0.body().string();
					JSONObject data = JSONObject.fromObject(str);
					if (data.getString( "status").equals("success")) {
						url = data.getJSONArray("data").getJSONObject(0).getString("value")+ LoginUser.getInstantiation(NewTransactionActivity.this).getLoginUser().getToken();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								web_update_apk.loadUrl(url);
							}
						});

					}else{
//                    sToast(data.getString("msg"));
					}

				}

				@Override
				public void onFailure(Request arg0, IOException arg1) {

				}
			});
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_no_link:
				if (Tools.getAPNType(NewTransactionActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					web_update_apk.setWebViewClient(new WebViewClient(){
						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							// TODO Auto-generated method stub
							view.loadUrl(url);
							return false;
						}
					});

					web_update_apk.setWebChromeClient(new WebChromeClient());



					HttpConnect.post(this, "member_transaction_url", null, new Callback() {
						@Override
						public void onResponse(Response arg0) throws IOException {

							String str = arg0.body().string();
							JSONObject data = JSONObject.fromObject(str);
							if (data.getString( "status").equals("success")) {
								url = data.getJSONArray("data").getJSONObject(0).getString("value")+LoginUser.getInstantiation(NewTransactionActivity.this).getLoginUser().getToken();
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										web_update_apk.loadUrl(url);
									}
								});

							}else{
//                    sToast(data.getString("msg"));
							}

						}

						@Override
						public void onFailure(Request arg0, IOException arg1) {

						}
					});
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
	        @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	                loadingView.setVisibility(View.GONE);
	            } else {
	                if (loadingView.getVisibility() == View.GONE)
						loadingView.setVisibility(View.VISIBLE);
//	                pro.setProgress(newProgress);
	            }
	            super.onProgressChanged(view, newProgress);
	        }

	    }

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

//	public void refreshUrl(){
//		web_update_apk.loadUrl(url);
//	}
}
