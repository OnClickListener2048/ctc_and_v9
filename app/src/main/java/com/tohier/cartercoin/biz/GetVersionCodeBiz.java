package com.tohier.cartercoin.biz;


import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.GetVersionCodeListener;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class GetVersionCodeBiz implements ILoadVersionCodeBiz {

    private Context context;

    public GetVersionCodeBiz(Context context) {
        this.context = context;
    }

    @Override
    public void getVersionCode(final GetVersionCodeListener getVersionCodeListener) {
          HttpConnect.post((IContext) context, "app_getversion", null,
					new Callback() {

						@Override
						public void onResponse(Response arg0)
								throws IOException {
							JSONObject data = JSONObject.fromObject(arg0.body()
									.string());
							if (data.get("status").equals("success")) {
                                String remark = data.optJSONArray("data")
										.optJSONObject(0).optString("remark");
								int androidVersion = Integer.valueOf(data
										.optJSONArray("data").optJSONObject(0)
										.optString("version"));
                                getVersionCodeListener.loadSuccess(remark,androidVersion);
							} else {
                                getVersionCodeListener.loadFail();
							}
						}

						@Override
						public void onFailure(Request arg0, IOException arg1) {
                            getVersionCodeListener.loadFail();
						}
					});
    }

	@Override
	public void getUrl(final GetVersionCodeListener getVersionCodeListener) {
		HttpConnect.post((IContext) context, "member_dandelion_down", null,
				new Callback() {

					@Override
					public void onResponse(Response arg0)
							throws IOException {
						JSONObject data = JSONObject.fromObject(arg0.body()
								.string());
						if (data.get("status").equals("success")) {
							String url = data.optJSONArray("data")
									.optJSONObject(0).optString("url");
							getVersionCodeListener.loadSuccess(url,0);
						} else {
							getVersionCodeListener.loadFail();
						}
					}

					@Override
					public void onFailure(Request arg0, IOException arg1) {
						getVersionCodeListener.loadFail();
					}
				});
	}
}
