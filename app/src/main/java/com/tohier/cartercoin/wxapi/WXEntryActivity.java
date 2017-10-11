package com.tohier.cartercoin.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tohier.cartercoin.activity.GongGaoDetailActivity;
import com.tohier.cartercoin.activity.GuideActivity;
import com.tohier.cartercoin.activity.LoginMainActivity;
import com.tohier.cartercoin.activity.MainActivity;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.activity.NewShareActivity;
import com.tohier.cartercoin.activity.PhoneRegisterActivity;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends MyBaseActivity implements IWXAPIEventHandler,GetMemberInfoView {

	private IWXAPI api;
//	private static final String APP_ID = "wx38c96163385cf8b1";
//	private static final String APP_SECRET = "04c7e386a73d84d12e2db527a7f65fc7";

	private static final String APP_ID = "wx7ad749f6cba84064";
	private static final String APP_SECRET = "0cf4569cdc8f53c9a5dc38487881cdb9";
	private String openid;
	private String access_token;

	private GetMemberInfoPresenter getMemberInfoPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.flash_activity);
		api = WXAPIFactory.createWXAPI(this, APP_ID);
		api.handleIntent(getIntent(), this);

		myProgressDialog.setTitle("正在加载...");
		myProgressDialog.show();

//		// 添加有米的处理方法
//		OffersManager.getInstance(this).handleIntent(getIntent());

		// 分享有结果之后会打开这个activity，但是因为这个activity在这个demo中没有界面，因此将会是一片空白的，开发者请酌情判断是否需要finish掉
//		finish();
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {

		if("text".equals(arg0.transaction)){

			this.finish();
			if (arg0.errCode == 0) {
				GongGaoDetailActivity.myProgressDialog2.cancel();
//				IntroduceActivity.myProgressDialog2.cancel();
				HttpConnect.post(WXEntryActivity.this, "member_task_share", null, new Callback() {
					@Override
					public void onResponse(Response arg0) throws IOException {
					}
					@Override
					public void onFailure(Request arg0, IOException arg1) {

					}
				});

			} else if (arg0.errCode == -4) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
//					if (LoginMainActivity.myProgressDialog1.isShowing()){
						GongGaoDetailActivity.myProgressDialog2.cancel();
//						IntroduceActivity.myProgressDialog2.cancel();
//					}

					}
				});

			} else if (arg0.errCode == -2) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						GongGaoDetailActivity.myProgressDialog2.cancel();
//						IntroduceActivity.myProgressDialog2.cancel();
//
					}
				});

			}
			this.finish();
			return;
		}


//			sToast("分享成功");
		if("image".equals(arg0.transaction)){
			this.finish();
			if (arg0.errCode == 0) {
				NewShareActivity.myProgressDialog2.cancel();

					HttpConnect.post(WXEntryActivity.this, "member_task_share", null, new Callback() {
						@Override
						public void onResponse(Response arg0) throws IOException {
						}
						@Override
						public void onFailure(Request arg0, IOException arg1) {

						}
					});

			} else if (arg0.errCode == -4) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
//					if (LoginMainActivity.myProgressDialog1.isShowing()){
						NewShareActivity.myProgressDialog2.cancel();
//					}

					}
				});

			} else if (arg0.errCode == -2) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						NewShareActivity.myProgressDialog2.cancel();
//
					}
				});

			}
			return;
		}

		this.finish();
		
		if (arg0.errCode == 0) {
			if (LoginMainActivity.myProgressDialog1.isShowing()){
				LoginMainActivity.myProgressDialog1.cancel();
			}


			SendAuth.Resp resp = (SendAuth.Resp) arg0;
			getAccessTokenAndOpenId(resp.code);

		} else if (arg0.errCode == -4) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					sToast("您拒绝了微信登录授权");
					if (LoginMainActivity.myProgressDialog1.isShowing()){
						LoginMainActivity.myProgressDialog1.cancel();
					}
					NewShareActivity.myProgressDialog2.cancel();

					finish();
				}
			});

		} else if (arg0.errCode == -2) {
			runOnUiThread(new Runnable() {
			@Override
			public void run() {
				sToast("您取消了微信登录授权");
				if (LoginMainActivity.myProgressDialog1.isShowing()){
					LoginMainActivity.myProgressDialog1.cancel();
				}
				NewShareActivity.myProgressDialog2.cancel();
				finish();
			}
		});

		}else{
			sToast("微信授权失败");
			LoginMainActivity.myProgressDialog1.cancel();
			NewShareActivity.myProgressDialog2.cancel();
			finish();
		}

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub


	}

	//第一步
	private void getAccessTokenAndOpenId(String code) {
		Map<String, Object> par = new HashMap<String, Object>();
		par.put("appid", APP_ID);
		par.put("secret", APP_SECRET);
		par.put("code", code);
		par.put("grant_type", "authorization_code");

        MyNetworkConnection.getNetworkConnection(this).postValues("getAccessToken",
				"https://api.weixin.qq.com/sns/oauth2/access_token", par, new Callback() {

					@Override
					public void onResponse(Response arg0) throws IOException {
						// TODO Auto-generated method stub
						JSONObject data = JSONObject.fromObject(arg0.body().string());
						 access_token = data.getString("access_token");

						 openid = data.getString("unionid");

						String ss = getSharedPreferences("weixinlogin", Context.MODE_APPEND).getString("login","123");
						if(ss.equals("bandphone"))
						{
							getSharedPreferences("weixinlogin", Context.MODE_APPEND).edit().putString("login","456").commit();
							Map<String, String> par1 = new HashMap<String, String>();
							par1.put("id", LoginUser.getInstantiation(getApplicationContext())
									.getLoginUser().getUserId());
							par1.put("openid", openid);

							HttpConnect.post(WXEntryActivity.this, "member_modify_wechat", par1, new Callback() {

								@Override
								public void onFailure(Request arg0, IOException arg1) {
									Handler dataHandler = new Handler(
											getContext().getMainLooper()) {
										@Override
										public void handleMessage(
												final Message msg)
										{
											mZProgressHUD.cancel();
//											sToast("请检查你的网络状态");
											finish();
										}
									};
									dataHandler.sendEmptyMessage(0);
								}

								@Override
								public void onResponse(Response arg0) throws IOException {
									JSONObject object = JSONObject.fromObject(arg0.body().string());
									if (object.get("status").equals("success")) {
										Handler dataHandler = new Handler(
												getContext().getMainLooper()) {
											@Override
											public void handleMessage(
													final Message msg)
											{

												if(null!=bandSuccess)
												{
													bandSuccess.sueesss();
												}
												sToast("已绑定成功");
												finish();
											}
										};
										dataHandler.sendEmptyMessage(0);
									}else
									{
										Handler dataHandler = new Handler(
												getContext().getMainLooper()) {
											@Override
											public void handleMessage(
													final Message msg)
											{
												mZProgressHUD.cancel();
												sToast("该微信已有用户绑定");
												finish();
											}
										};
										dataHandler.sendEmptyMessage(0);
									}
								}
							});

					}else
						{
							isFirstLogin(openid,access_token);
						}
					}

					@Override
					public void onFailure(Request arg0, IOException arg1) {
						// TODO Auto-generated method stub
					}
				});
	}


	private void isFirstLogin(final String openId,final String access_token) {
		getUserInfo(openId,access_token);
	}


	private void getUserInfo(final String openId, String accessToken) {
		final Map<String, Object> par = new HashMap<String, Object>();
		par.put("access_token", accessToken);
		par.put("openid", openId);
        MyNetworkConnection.getNetworkConnection(this).postValues("getUserInfo",
				"https://api.weixin.qq.com/sns/userinfo", par, new Callback() {

					@Override
					public void onResponse(Response arg0) throws IOException {
						// TODO Auto-generated method stub
						JSONObject data = JSONObject.fromObject(arg0.body().string());
						final String nickname = data.getString("nickname");
						final String headimgurl = data.getString("headimgurl");
						Handler dataHandler = new Handler(
								WXEntryActivity.this.getMainLooper()) {

							@Override
							public void handleMessage(
									final Message msg) {
								Map<String, String> par = new HashMap<String, String>();

								par.put("code", openId);
								par.put("password", "");
								par.put("type", "wechat");
								par.put("pic", headimgurl);
								par.put("nickname", nickname);
                                par.put("uuid", GuideActivity.PHONE_ID);
                                par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
                                par.put("geographicBac",GuideActivity.ADDRESS );
                                par.put("browserSystem", GuideActivity.PHONE_TYPE);
								HttpConnect.post(WXEntryActivity.this, "member_login_v3", par, new com.squareup.okhttp.Callback() {

									@Override
									public void onResponse(Response arg0) throws IOException {
										final JSONObject data = JSONObject.fromObject(arg0.body().string());
										if (data.get("status").equals("success")) {

											getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
											final String token = data.optJSONArray("data").optJSONObject(0).optString("token");
											Handler dataHandler = new Handler(
													WXEntryActivity.this.getMainLooper()) {

												@Override
												public void handleMessage(
														final Message msg) {
													LoginUser.getInstantiation(WXEntryActivity.this.getApplicationContext()).setToken(token);
													getMemberInfoPresenter = new GetMemberInfoPresenter(WXEntryActivity.this,WXEntryActivity.this,"wxLogin");
													getMemberInfoPresenter.getMemberInfo();
//													ModifyMemberInfoPresenter modifyMemberInfoPresenter = new ModifyMemberInfoPresenter(WXEntryActivity.this,getMemberInfoPresenter);
//													modifyMemberInfoPresenter.modifyMemberInfo(headimgurl,nickname);
												}
											};
											dataHandler.sendEmptyMessage(0);
										}else
										{
											Handler dataHandler = new Handler(
													WXEntryActivity.this.getMainLooper()) {

												@Override
												public void handleMessage(
														final Message msg) {
													if(data.getString("msg")!=null)
													{
														boolean flag = Tools.isPhonticName(data.getString("msg"));
														if(!flag)
														{
															if(data.getString("msg").equals("0"))//跳注册
															{
																Intent intent = new Intent(WXEntryActivity.this,PhoneRegisterActivity.class);
																intent.putExtra("nickname",nickname);
																intent.putExtra("pic",headimgurl);
																intent.putExtra("type","wechat");
																intent.putExtra("openid",openId);
																startActivity(intent);
																finish();
															}else
															{
																sToast(data.getString("msg"));
															}
														}
													}
												}
											};
											dataHandler.sendEmptyMessage(0);
										}
									}

									@Override
									public void onFailure(Request arg0, IOException arg1) {
										Handler dataHandler = new Handler(
												WXEntryActivity.this.getMainLooper()) {

											@Override
											public void handleMessage(
													final Message msg) {
//												sToast("链接超时！");
											}
										};
										dataHandler.sendEmptyMessage(0);
									}
								});
							}
						};
						dataHandler.sendEmptyMessage(0);
					}

					@Override
					public void onFailure(Request arg0, IOException arg1) {
						// TODO Auto-generated method stub
						Handler dataHandler = new Handler(
								getContext().getMainLooper()) {

							@Override
							public void handleMessage(
									final Message msg) {
								sToast("获取头像信息出错");
							}
						};
						dataHandler.sendEmptyMessage(0);
					}
				});
	}



	/**
	 * 登录成功，关闭相应页面
	 */
	private void sendBroadcastRegisterDone() {
	    MyApplication.deleteActivity("LoginMainActivity");

//        String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
//		if(!TextUtils.isEmpty(phoneNum)&&!phoneNum.equals(""))
//		{
			startActivity(new Intent(this,MainActivity.class));
//		}else
//		{
//			Intent intent = new Intent(this,BandPhoneActivity.class);
//			intent.putExtra("loginBand",true);
//			startActivity(intent);
//		}
//		SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
//		String gesturepassword = sharedPreferences.getString("gesturepassword","");
//		if(!TextUtils.isEmpty(gesturepassword)&&gesturepassword.equals("有"))
//		{zA
//			startActivity(new Intent(this,IndexActivity.class));
//		}else
//		{
//			Intent intent =  new Intent(this,GesturesPasswordActivity.class);
//			intent.putExtra("isSetting","true");
//			startActivity(intent);
//		}
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

//		// 添加有米的处理方法
//		OffersManager.getInstance(this).handleIntent(getIntent());

		// 分享有结果之后会打开这个activity，但是因为这个activity在这个demo中没有界面，因此将会是一片空白的，开发者请酌情判断是否需要finish掉
		finish();

	}

	@Override
	public void loadMemberInfoSuccess() {
		Handler dataHandler = new Handler(
				WXEntryActivity.this.getMainLooper()) {

			@Override
			public void handleMessage(
					final Message msg) {
				sendBroadcastRegisterDone();
			}
		};
		dataHandler.sendEmptyMessage(0);
	}

	@Override
	public void loadMemberInfoFail() {
		Handler dataHandler = new Handler(
				WXEntryActivity.this.getMainLooper()) {

			@Override
			public void handleMessage(
					final Message msg) {
//				sToast("链接超时！");
			}
		};
		dataHandler.sendEmptyMessage(0);
	}

	@Override
	public void showMsg(String msg) {

	}

	@Override
	public void showProgress() {

	}

	@Override
	public void hideProgress() {

	}

	@Override
	public void setRMBTextView(String rmbTextView) {

	}

	@Override
	public void setXiaoFeiAccountTextView(String rmbTextView) {

	}

	@Override
	public void setJueZhanTextView(String rmbTextView) {

	}

	@Override
	public void setTerminalCtcTextView(String terminalCtcTextView) {

	}

	@Override
	public void setDemandTextView(String demandTextView) {

	}

	@Override
	public TextView setTongBanTextView(String rmbTextView) {
		return  null;
	}

	@Override
	public TextView setTongXiTextView(String terminalCtcTextView) {
		return  null;
	}

	@Override
	public TextView setTongXiaoTextView(String demandTextView) {
		return  null;
	}

	@Override
	public void setShareTextView(String demandTextView) {

	}

	@Override
	public void setCurrentCTCPriceTextView(String demandTextView) {

	}

	@Override
	public void setallSsetsTextView(String allssets) {

	}

	@Override
	public LinearLayout getTwoLevel() {
		return null;
	}

	@Override
	public LinearLayout getThreeLevel() {
		return null;
	}

	public String getOpenid() {
		return openid;
	}


	@Override
	protected void onStop() {
		super.onStop();
		myProgressDialog.cancel();
	}

	public interface BandSuccess
	{
		void sueesss();
	}

	public static BandSuccess bandSuccess;


	public static  void setBandSuccess(BandSuccess bandSuccess) {
		WXEntryActivity.bandSuccess = bandSuccess;
	}
}
