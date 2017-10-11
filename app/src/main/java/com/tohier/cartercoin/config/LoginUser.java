package com.tohier.cartercoin.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tohier.cartercoin.activity.LoginMainActivity;

public class LoginUser {


	private final String SETTING = "Setting";
	private LoginUserEntity loginUserEntity = null;
	private static LoginUser loginUser = null;
	private SharedPreferences sp;

	public static LoginUser getInstantiation(Context myAppContext) {
		if (loginUser == null) {
			loginUser = new LoginUser(myAppContext);
		}

		return loginUser;
	}

	private LoginUser(Context context) {
		sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
	}

	public boolean isLogin() {
		return sp.contains("token");
	}

	public LoginUserEntity login(String userId, String headUrl,
								 String nickName, String phoneNum, String type, String token,String linkcode,String sex) {
		Editor sp = this.sp.edit();
		sp.putString("userId", userId);
		sp.putString("headUrl", headUrl);
		sp.putString("nickName", nickName);
		sp.putString("phoneNum", phoneNum);
		sp.putString("type", type);
		sp.putString("token", token);
		sp.putString("linkcode", linkcode);
		sp.putString("sex", sex);
		sp.commit();
		loginUserEntity = null;
		return getLoginUser();
	}

	public void setToken(String token)
	{
		Editor sp = this.sp.edit();
		sp.putString("token", token);
		sp.commit();
		loginUserEntity = null;
	}

	public void setSp(String superPad)
	{
		Editor sp = this.sp.edit();
		sp.putString("superPad", superPad);
		sp.commit();
		loginUserEntity = null;
	}

	public void setPhoneNum(String phoneNum)
	{
		Editor sp = this.sp.edit();
		sp.putString("phoneNum", phoneNum);
		sp.commit();
		loginUserEntity = null;
	}

	public void setType(String type)
	{
		Editor sp = this.sp.edit();
		sp.putString("type", type);
		sp.commit();
		loginUserEntity = null;
	}

	public void setHeadUrl(String headUrl)
	{
		Editor sp = this.sp.edit();
		sp.putString("headUrl", headUrl);
		sp.commit();
		loginUserEntity = null;
	}

	public void setType1(String type)
	{
		Editor sp = this.sp.edit();
		sp.putString("type", type);
		sp.commit();
		loginUserEntity = null;
	}

	public void setName1(String name)
	{
		Editor sp = this.sp.edit();
		sp.putString("nickName", name);
		sp.commit();
		loginUserEntity = null;
	}

	public void setSex(String sex)
	{
		Editor sp = this.sp.edit();
		sp.putString("sex", sex);
		sp.commit();
		loginUserEntity = null;
	}

	public LoginUserEntity getLoginUser() {
		if (this.isLogin() && loginUserEntity == null) {
			loginUserEntity = new LoginUserEntity();
			loginUserEntity.setUserId(sp.getString("userId", ""));
			loginUserEntity.setHeadUrl(sp.getString("headUrl", ""));
			loginUserEntity.setNickName(sp.getString("nickName", ""));
			loginUserEntity.setPhoneNum(sp.getString("phoneNum", ""));
			loginUserEntity.setType(sp.getString("type", ""));
			loginUserEntity.setToken(sp.getString("token",null));
			loginUserEntity.setLinkCode(sp.getString("linkcode",""));
			loginUserEntity.setSex(sp.getString("sex",""));
		}
		return loginUserEntity;
	}

	public void loginOut() {
		Editor sp = this.sp.edit();
		sp.clear();
		sp.commit();
		loginUserEntity = null;
	}

	public void startLoginActivity(Activity activity) {
		Intent intent = new Intent(activity, LoginMainActivity.class);
		intent.putExtra("canBack", false);
		activity.startActivity(intent);
	}

	public class LoginUserEntity {
		private String linkCode;
		private String userId;
		private String headUrl;
		private String nickName;
		private String phoneNum;
		private String type;
		private String token;
		private String sp;
		private String sex;

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getSp() {
			return sp;
		}

		public void setSp(String sp) {
			this.sp = sp;
		}

		public String getLinkCode() {
			return linkCode;
		}

		public void setLinkCode(String linkCode) {
			this.linkCode = linkCode;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getHeadUrl() {
			return headUrl;
		}

		public void setHeadUrl(String headUrl) {
			this.headUrl = headUrl;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getPhoneNum() {
			return phoneNum;
		}

		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}
}
