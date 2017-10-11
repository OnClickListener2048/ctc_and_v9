package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LocusPassWordView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.gesturespasswordconfig.SharedPreferencesHelper;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.PhoneLoginPresenter;
import com.tohier.cartercoin.presenter.SetGesturesPasswordPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.LoadLoginPicView;
import com.tohier.cartercoin.ui.PhoneLoginView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GesturesPasswordActivity extends BaseActivity implements GetMemberInfoView,PhoneLoginView,LoadLoginPicView{

	private LocusPassWordView mPwdView;
	private TextView multi_tv_token_time_hint,tv_into_loginpage;
	private String username,password,gesturesPwd;
	private GetMemberInfoPresenter getMemberInfoPresenter;
	private SetGesturesPasswordPresenter setGesturesPasswordPresenter;
	private boolean isSetting = false;
	private Button btnReSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesturespassword_layout);

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

		mPwdView = (LocusPassWordView) this.findViewById(R.id.mPassWordView);
		multi_tv_token_time_hint = (TextView) this.findViewById(R.id.multi_tv_token_time_hint);
		tv_into_loginpage = (TextView) this.findViewById(R.id.tv_into_loginpage);
		btnReSet = (Button) this.findViewById(R.id.btn_reset);

		setGesturesPasswordPresenter = new SetGesturesPasswordPresenter(this, this, mPwdView);

		String into = getIntent().getStringExtra("into");
		if (!TextUtils.isEmpty(into) && into.equals("GuideActivity")) {
			String type = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("type", "");
			if (type.equals("phoneLogin")) {
				username = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("username", "");
				password = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("password", "");
				getMemberInfoPresenter = new GetMemberInfoPresenter(this, this);
				PhoneLoginPresenter phoneLoginPresenter = new PhoneLoginPresenter(this, this, getMemberInfoPresenter);
				phoneLoginPresenter.VerificationTokenPhoneLogin();
			} else if (type.equals("wxLogin")) {
				String openId = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("openId", "");
				wxLogin(openId);
			} else if (type.equals("qqLogin")) {
				String openId = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("openId", "");
				qqLogin(openId, null);
			}
		}

		String setting = getIntent().getStringExtra("isSetting");
		if (!TextUtils.isEmpty(setting) && setting.equals("true"))
		{
			multi_tv_token_time_hint.setText("请设置手势密码");
			btnReSet.setVisibility(View.VISIBLE);
			isSetting = true;
	    }

		mPwdView.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {   //设置与验证两种状态
			@Override
			public void onComplete(String mPassword)
			{
				if (isSetting)   //设置
				{
					SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(getApplicationContext());
					String pwd = sph.getString("password", "");

					if (pwd.length() == 0)
					{
						multi_tv_token_time_hint.setText("请重复手势密码");
						sph.putString("password",mPassword);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mPwdView.clearPassword();
						return;
					} else
					{
						if (mPassword.equals(pwd))   //两次一致可上传手势密码
						{
							multi_tv_token_time_hint.setText("手势一致");
							setGesturesPasswordPresenter.setGesturesPassword(mPassword);
					    }else            //两次不一致
						{
							multi_tv_token_time_hint.setText("与之前手势密码不符");
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mPwdView.clearPassword();
				    }
				} else     //验证
				{
					setGesturesPasswordPresenter.checkGesturesPassword(mPassword);
				}
			}
		});


//		btnCommit.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//                if(!TextUtils.isEmpty(gesturesPwd))
//				{
//					setGesturesPasswordPresenter.setGesturesPassword(gesturesPwd);
//				}else
//				{
//					sToast("请设定手势密码");
//				}
//			}
//		});

		btnReSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				   SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(getApplicationContext());
				   sph.clear();
				   multi_tv_token_time_hint.setText("请设置手势密码");
				   gesturesPwd = "";
				   mPwdView.clearPassword();
			}
		});
	}

	private void wxLogin(final String openId) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("code", openId);
		par.put("password", "");
		par.put("type", "wechat");
		par.put("pic", "");
		par.put("nickname", "");
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
		HttpConnect.post(this, "member_login_v3", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
					final String token = data.getJSONArray("data").getJSONObject(0).getString("token");
					LoginUser.getInstantiation(GesturesPasswordActivity.this.getApplicationContext()).setToken(token);

					Handler dataHandler = new Handler(
							GesturesPasswordActivity.this.getMainLooper()) {

						@Override
						public void handleMessage(
								final Message msg) {

							getMemberInfoPresenter = new GetMemberInfoPresenter(GesturesPasswordActivity.this,GesturesPasswordActivity.this);
							getMemberInfoPresenter.VerificationTokenGetMemberInfo();

						}
					};
					dataHandler.sendEmptyMessage(0);
				}else
				{
					Handler dataHandler = new Handler(
							GesturesPasswordActivity.this.getMainLooper()) {

						@Override
						public void handleMessage(
								final Message msg) {
//
						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Handler dataHandler = new Handler(
						GesturesPasswordActivity.this.getMainLooper()) {

					@Override
					public void handleMessage(
							final Message msg) {
						sToast("链接超时！");
					}
				};
				dataHandler.sendEmptyMessage(0);
			}
		});
	}


	private void qqLogin(final String openId, final String accessToken) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("code", openId);
		par.put("password", "");
		par.put("type", "qq");
		par.put("pic", "");
		par.put("nickname", "");
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
		HttpConnect.post(this, "member_login_v3", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				JSONObject data = JSONObject.fromObject(arg0.body().string());

				if (data.get("status").equals("success")) {
					getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
					final String token = data.getJSONArray("data").getJSONObject(0).getString("token");
					LoginUser.getInstantiation(GesturesPasswordActivity.this.getApplicationContext()).setToken(token);

					Handler dataHandler = new Handler(
							GesturesPasswordActivity.this.getMainLooper()) {

						@Override
						public void handleMessage(
								final Message msg) {
							getMemberInfoPresenter = new GetMemberInfoPresenter(GesturesPasswordActivity.this,GesturesPasswordActivity.this);
							getMemberInfoPresenter.VerificationTokenGetMemberInfo();
						}
					};
					dataHandler.sendEmptyMessage(0);

				}else
				{
					Handler dataHandler = new Handler(
							GesturesPasswordActivity.this.getMainLooper()) {

						@Override
						public void handleMessage(
								final Message msg) {
//							Toast.makeText(GesturesPasswordActivity.this, "数据接口请求失败", Toast.LENGTH_SHORT).show();
						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Handler dataHandler = new Handler(
						GesturesPasswordActivity.this.getMainLooper()) {

					@Override
					public void handleMessage(
							final Message msg) {
						Toast.makeText(GesturesPasswordActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
					}
				};
				dataHandler.sendEmptyMessage(0);
			}
		});
	}

	@Override
	public void loadMemberInfoSuccess() {
		SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
		final String gesturepassword = sharedPreferences.getString("gesturepassword","");
		if(!TextUtils.isEmpty(gesturepassword))
		{
			if(gesturepassword.equals("没有"))
			{
				multi_tv_token_time_hint.setText("请设置手势密码");
				btnReSet.setVisibility(View.VISIBLE);
				isSetting = true;
			}else
			{
			 	multi_tv_token_time_hint.setText("请验证手势密码");
				tv_into_loginpage.setVisibility(View.VISIBLE);
				tv_into_loginpage.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						LoginUser.getInstantiation(getApplicationContext()).loginOut();
						startActivity(new Intent(GesturesPasswordActivity.this,LoginMainActivity.class));
						finish();
					}
				});
			}
		}
	}

	@Override
	public void loadMemberInfoFail() {

	}

	@Override
	public void showMsg(String msg) {

	}

	@Override
	public String getusername() {
		return username;
	}

	@Override
	public String getpassword() {
		return password;
	}

	@Override
	public void loginSuccess() {

	}

	@Override
	public void loginFail() {

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
                   return null;
	}

	@Override
	public TextView setTongXiTextView(String terminalCtcTextView) {
		return null;
	}

	@Override
	public TextView setTongXiaoTextView(String demandTextView) {
		return null;
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

	@Override
	public void initData() {

	}

	@Override
	public void loadSuccess() {
	        	SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(getApplicationContext());
		        sph.clear();
                Intent intent = new Intent(GesturesPasswordActivity.this,MainActivity.class);
		        startActivity(intent);
		        finish();
	}

	@Override
	public void loadFail() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mPwdView.clearPassword();
//           sToast("链接失败");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			startActivity(new Intent(GesturesPasswordActivity.this,LoginMainActivity.class));
			return  true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
