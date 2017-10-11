package com.tohier.cartercoin.biz;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.listener.ModifyMemberInfoListener;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class ModifyMemberInfoBiz implements IModifyMemberInfoBiz {

    private Context context;

    public ModifyMemberInfoBiz(Context context) {
        this.context = context;
    }

	@Override
	public void modifyMemberInfo(final ModifyMemberInfoListener modifyMemberInfoListener, final String photo, final String nicheng) {

		HttpConnect.post(((IContext) context), "member_info", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				final JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {

					Handler dataHandler = new Handler(
							context.getMainLooper()) {

						@Override
						public void handleMessage(
								final Message msg) {

							Map<String, String> par1 = new HashMap<String, String>();

							final String birthday = data.getJSONArray("data").getJSONObject(0).optString("birthday");
							final String pic = data.getJSONArray("data").getJSONObject(0).optString("pic");
							final String nickname = data.getJSONArray("data").getJSONObject(0).optString("nickname");
							final String name = data.getJSONArray("data").getJSONObject(0).optString("name");
							final String sex = data.getJSONArray("data").getJSONObject(0).optString("sex");

							if(photo!=null)
							{
								par1.put("pic", photo);
							}else
							{
								par1.put("pic", pic);
							}

							if(nicheng!=null)
							{
								par1.put("nickname", nicheng);
							}else
							{
								par1.put("nickname",nickname );
							}

							if(sex!=null)
							{
								if(sex.equals("保密"))
								{
									par1.put("sex","-1");
								}else if(sex.equals("美女"))
								{
									par1.put("sex","0");
								}else if(sex.equals("帅哥"))
								{
									par1.put("sex","1");
								}

							}else
							{
								par1.put("sex", "");
							}

							if(birthday!=null)
							{
								par1.put("birthday", birthday);
							}else
							{
								par1.put("birthday", "");
							}

							if(name!=null)
							{
								par1.put("name", name);
							}else
							{
								par1.put("name", "");
							}
//
							HttpConnect.post(((IContext) context), "member_modify_me", par1, new com.squareup.okhttp.Callback() {

								@Override
								public void onFailure(Request arg0, IOException arg1) {
									Handler dataHandler = new Handler(
											context.getMainLooper()) {

										@Override
										public void handleMessage(
												final Message msg) {
											modifyMemberInfoListener.loadFail();
										}
									};
									dataHandler.sendEmptyMessage(0);
								}

								@Override
								public void onResponse(Response arg0) throws IOException {
									JSONObject object = JSONObject.fromObject(arg0.body().string());
									if (object.get("status").equals("success")) {
										Handler dataHandler = new Handler(
												context.getMainLooper()) {

											@Override
											public void handleMessage(
													final Message msg) {
												modifyMemberInfoListener.loadSuccess();
											}
										};
										dataHandler.sendEmptyMessage(0);
									}
								}
							});


						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public  void onFailure(Request arg0, IOException arg1) {
				modifyMemberInfoListener.loadSuccess();
			}
		});
	}
}
