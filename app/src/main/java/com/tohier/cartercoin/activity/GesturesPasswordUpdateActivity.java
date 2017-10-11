package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LocusPassWordView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.gesturespasswordconfig.SharedPreferencesHelper;
import com.tohier.cartercoin.presenter.SetGesturesPasswordPresenter;
import com.tohier.cartercoin.ui.GesturesPwdVerifyPassView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GesturesPasswordUpdateActivity extends BaseActivity{

	private LocusPassWordView mPwdView;
	private TextView multi_tv_token_time_hint;
	/**
	 * 验证手势密码的presenter
	 */
	private SetGesturesPasswordPresenter setGesturesPasswordPresenter;
	private boolean verificationAdopt = true;
	private String gesturesPwd;
	private Button btnReSet,btnCommit,btn_change_gesturespwd_start_state;
	private boolean isStart = false;
	private ImageView iv_back2;

	private GesturesPwdVerifyPassView gesturesPwdVerifyPassView = new GesturesPwdVerifyPassView() {
		@Override
		public void VerifyPassSuccess() {
			verificationAdopt = true;
			btnReSet.setVisibility(View.VISIBLE);
			btnCommit.setVisibility(View.VISIBLE);
			mPwdView.clearPassword();
			multi_tv_token_time_hint.setText("请设置手势密码");
		}

		@Override
		public void VerifyPassFail() {
			 sToast("链接超时");
		}

		@Override
		public void setSuccess() {
			SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
			sharedPreferences.edit().putString("gesturepassword","有").commit();
			SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(getApplicationContext());
			sph.clear();
               finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_gesturespassword_layout);


		mPwdView = (LocusPassWordView) this.findViewById(R.id.mPassWordView);
		multi_tv_token_time_hint = (TextView) this.findViewById(R.id.multi_tv_token_time_hint);
		iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
		btnReSet = (Button) this.findViewById(R.id.btn_reset);
		btnCommit = (Button) this.findViewById(R.id.btn_commit);
		btn_change_gesturespwd_start_state = (Button) this.findViewById(R.id.btn_change_gesturespwd_start_state);

		SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
		String opengestures = sharedPreferences.getString("opengestures","");
		if(!TextUtils.isEmpty(opengestures)&&opengestures.equals("True"))   //开启状态
		{
			btn_change_gesturespwd_start_state.setBackgroundResource(R.drawable.switch_no);
			isStart = true;
		}else                                                            //未开启状态
		{
			btn_change_gesturespwd_start_state.setBackgroundResource(R.drawable.switch_off);
			isStart = false;
		}

		iv_back2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		setGesturesPasswordPresenter = new SetGesturesPasswordPresenter(this,gesturesPwdVerifyPassView,mPwdView);

		mPwdView.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {

				if(verificationAdopt==false)
				{
					if(!TextUtils.isEmpty(mPassword))
					{
						setGesturesPasswordPresenter.updateCheckGesturesPassword(mPassword);
					}
				}else
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
							setGesturesPasswordPresenter.verifyPassSetGesturesPassword(mPassword);
//							multi_tv_token_time_hint.setText("手势一致");
//							gesturesPwd = mPassword;
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
				}
			}
		});


		btnCommit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(gesturesPwd))
				{
					setGesturesPasswordPresenter.verifyPassSetGesturesPassword(gesturesPwd);
				}else
				{
					sToast("请设定手势密码");
				}
			}
		});

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

		btn_change_gesturespwd_start_state.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
				String gesturepassword = sharedPreferences.getString("gesturepassword","");
				if(!TextUtils.isEmpty(gesturepassword)&&gesturepassword.equals("有"))
				{
					updateMemberInfo();
				}else
				{
					sToast("请设置手势密码");
				}
			}
		});

	}

	public void updateMemberInfo()
	{
		Map<String, String> par = new HashMap<String, String>();
		if(isStart)
		{
			par.put("opengestures", "0");
		}else
		{
			par.put("opengestures", "1");
		}
		HttpConnect.post(this, "member_open_gestures", par, new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Handler dataHandler = new Handler(getContext()
						.getMainLooper()) {

					@Override
					public void handleMessage(final Message msg) {
						mZProgressHUD.hide();
						sToast("链接超时");
					}
				};
				dataHandler.sendEmptyMessage(0);
			}

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject object = JSONObject.fromObject(arg0.body().string());
				if (object.get("status").equals("success")) {
					Handler dataHandler = new Handler(getContext()
							.getMainLooper()) {

						@Override
						public void handleMessage(final Message msg) {
							mZProgressHUD.hide();
//							sToast("修改成功");

							if(isStart)
							{
								isStart = false;
								btn_change_gesturespwd_start_state.setBackgroundResource(R.drawable.switch_off);
								SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
								sharedPreferences.edit().putString("opengestures","False").commit();
							}else
							{
								isStart = true;
								btn_change_gesturespwd_start_state.setBackgroundResource(R.drawable.switch_no);
								SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword",Context.MODE_PRIVATE);
								sharedPreferences.edit().putString("opengestures","True").commit();
							}
						}
					};
					dataHandler.sendEmptyMessage(0);


				}else{
					Handler dataHandler = new Handler(getContext()
							.getMainLooper()) {

						@Override
						public void handleMessage(final Message msg) {
							mZProgressHUD.hide();
							sToast("修改失败");
						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}
		});
	}


	@Override
	public void initData() {

	}
}
