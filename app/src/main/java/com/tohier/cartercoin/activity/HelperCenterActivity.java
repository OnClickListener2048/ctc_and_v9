package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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


@SuppressLint("SetJavaScriptEnabled")
public class HelperCenterActivity extends MyBackBaseActivity {

	private WebView web_update_apk;
	private ImageView iv_back;

	private ProgressBar pro;
	private TextView fragment_share_title_text;

	private String code;

	private LoadingView loadingView;
	private NoLinkView ivNoLink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_helpercenter_layout);
		
		init();
		setUpView();

	}

		
	private void setUpView() {
		iv_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(HelperCenterActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					HttpConnect.post(HelperCenterActivity.this, code, null, new Callback() {
						@Override
						public void onResponse(Response arg0) throws IOException {
							final JSONObject data = JSONObject.fromObject(arg0.body().string());
							if (data.get("status").equals("success")) {
//					final String banner_pic = data.optJSONArray("data").optJSONObject(0).optString("pic");

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if(!TextUtils.isEmpty(getIntent().getStringExtra("mark")))  //关于卡特坊
										{
											if (data.optJSONArray("data").optJSONObject(0).containsKey("value")) {
												String banner_url = data.optJSONArray("data").optJSONObject(0).optString("value");
												if (!TextUtils.isEmpty(banner_url) ){
													web_update_apk = (WebView)findViewById(R.id.web);
													web_update_apk.getSettings().setJavaScriptEnabled(true);

													web_update_apk.setWebViewClient(new WebViewClient(){
														@Override
														public boolean shouldOverrideUrlLoading(WebView view, String url) {
															// TODO Auto-generated method stub
															view.loadUrl(url);
															return false;
														}
													});

													web_update_apk.setWebChromeClient(new WebChromeClient());
													web_update_apk.loadUrl(banner_url);
												}
											}
										}else
										{
											if (data.optJSONArray("data").optJSONObject(0).containsKey("url")) {
												String banner_url = data.optJSONArray("data").optJSONObject(0).optString("url");
												Log.e("banner_url",banner_url);
												if (!TextUtils.isEmpty(banner_url) ){

													web_update_apk = (WebView)findViewById(R.id.web);
													web_update_apk.getSettings().setJavaScriptEnabled(true);

													web_update_apk.setWebViewClient(new WebViewClient(){
														@Override
														public boolean shouldOverrideUrlLoading(WebView view, String url) {
															// TODO Auto-generated method stub
															view.loadUrl(url);
															return false;
														}
													});

													web_update_apk.setWebChromeClient(new WebChromeClient());
													web_update_apk.loadUrl(banner_url);
												}
											}
										}
									}
								});
							} else {
								//sToast(data.getString("msg"));
							}
							//mZProgressHUD.cancel();
						}
						@Override
						public void onFailure(Request arg0, IOException arg1) {
						}
					});
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});
	}
 
	private void init() {
		fragment_share_title_text = (TextView) findViewById(R.id.fragment_share_title_text);
		loadingView = (LoadingView) findViewById(R.id.loading_view);
		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		if(!TextUtils.isEmpty(getIntent().getStringExtra("mark")))
		{
			fragment_share_title_text.setText(getIntent().getStringExtra("mark"));
		}

		pro = (ProgressBar) findViewById(R.id.pro);
		iv_back = (ImageView) findViewById(R.id.title_back);

		if(!TextUtils.isEmpty(getIntent().getStringExtra("mark")))  //关于卡特坊
		{
           code = "ctc_about";
		}else  //帮助中心
		{
			code = "ad_rule";
		}


		if (Tools.getAPNType(HelperCenterActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			HttpConnect.post(this, code, null, new Callback() {
				@Override
				public void onResponse(Response arg0) throws IOException {
					final JSONObject data = JSONObject.fromObject(arg0.body().string());
					if (data.get("status").equals("success")) {
//					final String banner_pic = data.optJSONArray("data").optJSONObject(0).optString("pic");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(!TextUtils.isEmpty(getIntent().getStringExtra("mark")))  //关于卡特坊
								{
									if (data.optJSONArray("data").optJSONObject(0).containsKey("value")) {
										String banner_url = data.optJSONArray("data").optJSONObject(0).optString("value");
										if (!TextUtils.isEmpty(banner_url) ){
											web_update_apk = (WebView)findViewById(R.id.web);
											web_update_apk.getSettings().setJavaScriptEnabled(true);

											web_update_apk.setWebViewClient(new WebViewClient(){
												@Override
												public boolean shouldOverrideUrlLoading(WebView view, String url) {
													// TODO Auto-generated method stub
													view.loadUrl(url);
													return false;
												}
											});

											web_update_apk.setWebChromeClient(new WebChromeClient());
											web_update_apk.loadUrl(banner_url);
										}
									}
								}else
								{
									if (data.optJSONArray("data").optJSONObject(0).containsKey("url")) {
										String banner_url = data.optJSONArray("data").optJSONObject(0).optString("url");
										Log.e("banner_url",banner_url);
										if (!TextUtils.isEmpty(banner_url) ){

											web_update_apk = (WebView)findViewById(R.id.web);
											web_update_apk.getSettings().setJavaScriptEnabled(true);

											web_update_apk.setWebViewClient(new WebViewClient(){
												@Override
												public boolean shouldOverrideUrlLoading(WebView view, String url) {
													// TODO Auto-generated method stub
													view.loadUrl(url);
													return false;
												}
											});

											web_update_apk.setWebChromeClient(new WebChromeClient());
											web_update_apk.loadUrl(banner_url);
										}
									}
								}
							}
						});
					} else {
						//sToast(data.getString("msg"));
					}
					//mZProgressHUD.cancel();
				}
				@Override
				public void onFailure(Request arg0, IOException arg1) {
				}
			});
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
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

	}
}
