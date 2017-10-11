package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


@SuppressLint("SetJavaScriptEnabled")
public class QuotationTuActivity extends MyBaseActivity implements View.OnClickListener{

	private WebView web_update_apk;
	private LoadingView loadingView;
	private NoLinkView ivNoLink;
	private String url;
	/**
	 * 0--比特币
	 * 1--以太坊
	 * 2--莱特币
	 */
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_quotation_tu);

		type = getIntent().getStringExtra("type");
		init();
		setUpView();

	}

		
	private void setUpView() {
		ivNoLink.setOnClickListener(this);
	}

	public void back(View v)
	{
		finish();
	}
 
	private void init() {
		web_update_apk = (WebView)findViewById(R.id.web);
		loadingView = (LoadingView) findViewById(R.id.loading_view);
		web_update_apk.getSettings().setJavaScriptEnabled(true);

		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		if (Tools.getAPNType(QuotationTuActivity.this) == true){
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


			HashMap<String,String> map = new HashMap<String, String>();
			map.put("type", type);
			HttpConnect.post(this, "member_quotation_chat", map, new Callback() {
				@Override
				public void onResponse(Response arg0) throws IOException {

					String str = arg0.body().string();
					JSONObject data = JSONObject.fromObject(str);
					if (data.getString( "status").equals("success")) {
						url = data.getJSONArray("data").getJSONObject(0).getString("value");

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
				if (Tools.getAPNType(QuotationTuActivity.this) == true){
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


					HashMap<String,String> map = new HashMap<String, String>();
					map.put("name", type);
					HttpConnect.post(this, "member_quotation_chat", map, new Callback() {
						@Override
						public void onResponse(Response arg0) throws IOException {

							String str = arg0.body().string();
							JSONObject data = JSONObject.fromObject(str);
							if (data.getString( "status").equals("success")) {
								url = data.getJSONArray("data").getJSONObject(0).getString("value");

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
}
