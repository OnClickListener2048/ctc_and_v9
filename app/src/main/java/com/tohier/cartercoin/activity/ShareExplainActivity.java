package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

/**
 * Created by Administrator on 2016/12/31.
 */

public class ShareExplainActivity extends MyBaseActivity {

	private WebView web_update_apk;
	private ImageView iv_back;
	private TextView tv_target_more,fragment_share_title_text;
	private ProgressBar pro;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mining_raiders_layout);
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
		WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.60);   //高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.78);    //宽度设置为屏幕的0.8
		p.alpha = 1.0f;      //设置本身透明度
		p.dimAmount = 0.5f;      //设置黑暗度
		getWindow().setAttributes(p);

		init();
	}

	private void init() {
		fragment_share_title_text = (TextView) findViewById(R.id.fragment_share_title_text);
		tv_target_more = (TextView) findViewById(R.id.tv_target_more);
		fragment_share_title_text.setText("分享说明");
		tv_target_more.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
		pro = (ProgressBar) findViewById(R.id.pro);
		iv_back = (ImageView) findViewById(R.id.title_back);


		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_target_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShareExplainActivity.this,ShareActivity.class));
				finish();
			}
		});
		getUrl();

	}

	private void getUrl()
	{
		//mZProgressHUD.show();
		HttpConnect.post(this, "member_mining_share", null, new Callback() {
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
//						    AssetsExplainActivity.this.sToast("暂无数据");
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
//						AssetsExplainActivity.this.sToast("请检查您的网络链接");
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
					pro.setVisibility(View.GONE);
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
