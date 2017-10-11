package com.tohier.cartercoin.config;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.tohier.android.config.IContext;
import com.tohier.android.util.MD5;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpConnect {

//	public static final String host = "http://123.57.7.121:63/Api/";

	/**
	 * 正式
	 */
	public static final String host = "http://api.blacoin.cc/Api/";
	/**
	 * 测试
	 * */
//	public static final String host = "https://myapi.blacoin.cc/Api/";

	private static final String key = "f04260aa4b55c5a1991b64f43d540dcc";

	public static void post(IContext context, String code, Map<String, String> parameters, Callback responseCallback) {
		try{
			HttpConnect.post(context, code, parameters, responseCallback, true);
		}catch(Exception e){

		}

	}



	public static void post(IContext context, String code, Map<String, String> parameters, Callback responseCallback,
							boolean caniNetWork) {

		String token = "";
		try{
			token = LoginUser.getInstantiation(context.getContext().getApplicationContext()).getLoginUser().getToken();
		}catch(Exception e){
			token = "";
		}
		Log.e("token",token);

		Map<String, Object> par = new HashMap<String, Object>();
		String time = String.valueOf(new Date().getTime() / 1000);
		par.put("time", time);

		par.put("code", code);
		par.put("token", token);
		par.put("sign", MD5.stringToMD5(key + time + code));

		JSONObject objJson = new JSONObject();
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				objJson.put(key, parameters.get(key));
			}
			JSONArray parsJson = new JSONArray();
			parsJson.add(objJson);
			par.put("prm", parsJson.toString());
		} else {
			par.put("prm", "");
		}
		MyNetworkConnection.getNetworkConnection(context).postValues("post", host, par, responseCallback, caniNetWork);
	}




}
