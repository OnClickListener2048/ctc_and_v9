package com.tohier.cartercoin.biz;

import android.content.Context;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.SetGesturesPasswordListener;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class SetGesturesPasswordBiz implements ISetGesturesPasswordBiz {

    private Context context;

    public SetGesturesPasswordBiz(Context context) {
        this.context = context;
    }

	@Override
	public void setGesturesPassword(String gesturesPassword,final SetGesturesPasswordListener setGesturesPasswordListener) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("gesturespassword", gesturesPassword);
		HttpConnect.post((IContext) context, "member_gestures_password", par,
				new Callback() {

					@Override
					public void onResponse(Response arg0)
							throws IOException {
						JSONObject data = JSONObject.fromObject(arg0.body()
								.string());
						if (data.get("status").equals("success")) {
							setGesturesPasswordListener.loadSuccess(null);
						} else {
							setGesturesPasswordListener.loadFail();
						}
					}

					@Override
					public void onFailure(Request arg0, IOException arg1) {
						setGesturesPasswordListener.loadFail();
					}
				});
	}


	@Override
	public void checkGesturesPassword(String gesturesPassword,final SetGesturesPasswordListener setGesturesPasswordListener) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("gesturespassword", gesturesPassword);
		HttpConnect.post((IContext) context, "member_gestures_check", par,new Callback() {

					@Override
					public void onResponse(Response arg0)
							throws IOException {
						JSONObject data = JSONObject.fromObject(arg0.body().string());
						if (data.get("status").equals("success")) {
							String result = data.getJSONArray("data").getJSONObject(0).optString("value");
							setGesturesPasswordListener.loadSuccess(result);
						} else {
							setGesturesPasswordListener.loadFail();
						}
					}

					@Override
					public void onFailure(Request arg0, IOException arg1) {
						setGesturesPasswordListener.loadFail();
					}
				});
	}
}
