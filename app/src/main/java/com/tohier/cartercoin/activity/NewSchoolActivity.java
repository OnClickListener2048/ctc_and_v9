package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

public class NewSchoolActivity extends MyBackBaseActivity {

	private WebView web_update_apk;
	private ImageView iv_back;
	private TextView tv_share_wx,fragment_share_title_text;
	private ProgressBar pro;
	private LoadingView loadingView;
	private NoLinkView ivNoLink;
	private String url;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_gonggao_detail);
		
		init();

	}

	private void init() {
		fragment_share_title_text = (TextView) findViewById(R.id.fragment_share_title_text);

		url = getIntent().getStringExtra("url");

		loadingView = (LoadingView) findViewById(R.id.loading_view);
		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

		if (Tools.getAPNType(NewSchoolActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(url))
			{

				fragment_share_title_text.setText("活动详情");
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
				web_update_apk.loadUrl(url);
			}else
			{
				fragment_share_title_text.setText("新手学堂");
				getUrl();
			}
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		}


		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(NewSchoolActivity.this) == true) {
					ivNoLink.setVisibility(View.GONE);
					if(!TextUtils.isEmpty(url))
					{

						fragment_share_title_text.setText("活动详情");
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
						web_update_apk.loadUrl(url);
					}else
					{
						fragment_share_title_text.setText("新手学堂");
						getUrl();
					}

				}else{
					ivNoLink.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
				}

			}
		});


		pro = (ProgressBar) findViewById(R.id.pro);
		iv_back = (ImageView) findViewById(R.id.title_back);
		tv_share_wx = (TextView) findViewById(R.id.iv_share_wx);
		tv_share_wx.setVisibility(View.GONE);


		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

	private void getUrl()
	{
		//mZProgressHUD.show();
		HttpConnect.post(this, "member_novice_study", null, new Callback() {
			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					final String url = data.getJSONArray("data").getJSONObject(0).getString("value");
					Handler dataHandler = new Handler(getContext().getMainLooper()) {
						@Override
						public void handleMessage(final Message msg) {
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
							web_update_apk.loadUrl(url);
						}
					};
					dataHandler.sendEmptyMessage(0);
				} else {
					Handler dataHandler = new Handler(getContext().getMainLooper()) {
						@Override
						public void handleMessage(final Message msg) {
//						    NewSchoolActivity.this.sToast("暂无数据");
						}
					};
					dataHandler.sendEmptyMessage(0);
					//sToast(data.getString("msg"));
				}
				//mZProgressHUD.cancel();
			}
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				//mZProgressHUD.cancel();
				Handler dataHandler = new Handler(getContext().getMainLooper()) {
					@Override
					public void handleMessage(final Message msg) {
//						NewSchoolActivity.this.sToast("请检查您的网络链接");
					}
				};
				dataHandler.sendEmptyMessage(0);
			}
		});
	}
	
	  public class WebChromeClient extends android.webkit.WebChromeClient {
	        @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	                loadingView.setVisibility(View.GONE);
	            } else {
	                if (loadingView.getVisibility() == View.GONE)
						loadingView.setVisibility(View.VISIBLE
						);
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
