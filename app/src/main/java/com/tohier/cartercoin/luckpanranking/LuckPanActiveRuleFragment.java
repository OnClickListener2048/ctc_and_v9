package com.tohier.cartercoin.luckpanranking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;


@SuppressLint("SetJavaScriptEnabled")
public class LuckPanActiveRuleFragment extends BaseFragment {

	private WebView web_update_apk;
	private View view;
	private LoadingView loadingView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_content,container,false);

		init();
		return view;
	}

	private void init() {
		loadingView = (LoadingView) view.findViewById(R.id.cif_loading);
		HttpConnect.post(this, "member_roulette_introduce", null, new Callback() {
			@Override
			public void onResponse(Response arg0) throws IOException {
				String jsonStr = arg0.body().string();
				final JSONObject data = JSONObject.fromObject(jsonStr);
				if (data.get("status").equals("success")) {

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
								if (data.optJSONArray("data").optJSONObject(0).containsKey("Value")) {
									String banner_url = data.optJSONArray("data").optJSONObject(0).optString("Value");
									if (!TextUtils.isEmpty(banner_url) ){
										web_update_apk = (WebView)view.findViewById(R.id.web);
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
	}
	

	@Override
	public void initData() {

	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				loadingView.setVisibility(View.GONE);
			} else {
				if (loadingView.getVisibility() == View.GONE)
					loadingView.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

	}
}
