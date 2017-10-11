package com.tohier.cartercoin.config;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.tohier.cartercoin.R;

public class DataManagerTools {

	public static void TextVisiblePassword(ImageView me, EditText et){
		if(et.getTransformationMethod()== PasswordTransformationMethod.getInstance())
		{
			//显示
			et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			me.setImageResource(R.mipmap.iv_pay_pwd_show);
		}else
		{
			et.setTransformationMethod(PasswordTransformationMethod.getInstance());
			me.setImageResource(R.mipmap.iv_pwd_no_show);
		}
	}
}
