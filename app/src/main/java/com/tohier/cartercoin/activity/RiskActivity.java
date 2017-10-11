package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;


@SuppressLint("SetJavaScriptEnabled")
public class RiskActivity extends MyBaseActivity {

	private WebView web_update_apk;
	private ImageView iv_back;
	private TextView tv_share_wx,fragment_share_title_text;
	private ProgressBar pro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_gonggao_detail);
		
		init();

//		SliderConfig mConfig = new SliderConfig.Builder()
//				.primaryColor(Color.TRANSPARENT)
//				.secondaryColor(Color.TRANSPARENT)
//				.position(SliderPosition.LEFT)
//				.edge(false)
//				.build();
//
//		ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//		mConfig.setPosition(SliderPosition.LEFT);
//		iSlider.setConfig(mConfig);
	}

	private void init() {

		fragment_share_title_text = (TextView) findViewById(R.id.fragment_share_title_text);
		fragment_share_title_text.setText("风险提示");
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
		getUrl();

	}

	private void getUrl()
	{
		//mZProgressHUD.show();
		HttpConnect.post(this, "member_risk_introduce", null, new Callback() {
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
						    RiskActivity.this.sToast("暂无数据");
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
//						RiskActivity.this.sToast("请检查您的网络链接");
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
	                pro.setVisibility(View.GONE);
	            } else {
	                if (pro.getVisibility() == View.GONE)
	                	pro.setVisibility(View.VISIBLE);
	                pro.setProgress(newProgress);
	            }
	            super.onProgressChanged(view, newProgress);
	        }

	    }
	
	
	
	
	
	
	
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}
