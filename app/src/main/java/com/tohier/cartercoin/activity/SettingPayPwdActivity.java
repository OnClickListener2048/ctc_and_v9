package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/25.
 */

public class SettingPayPwdActivity extends MyBackBaseActivity{

    private ImageView iv_back2,iv_pwd_isShow,iv_pwd_isShow2;
    private EditText etPwd,etPwd2;
    private Button btn_commit_update;
    private GifImageView gif_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_paypwd);

        initData();
        setUpView();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    @Override
    public void initData() {
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        iv_pwd_isShow2 = (ImageView) this.findViewById(R.id.iv_pwd_isShow2);
        etPwd = (EditText) this.findViewById(R.id.et_new_pwd);
        etPwd2 = (EditText) this.findViewById(R.id.et_new_pwd2);
        btn_commit_update = (Button) this.findViewById(R.id.btn_commit_update);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);

        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void setUpView()
    {
        iv_pwd_isShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPwd.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                {
                    //显示
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_pwd_isShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                }else
                {
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_pwd_isShow.setImageResource(R.mipmap.iv_pwd_no_show);
                }
            }
        });

        iv_pwd_isShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPwd2.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                {
                    //显示
                    etPwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_pwd_isShow2.setImageResource(R.mipmap.iv_pay_pwd_show);
                }else
                {
                    etPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_pwd_isShow2.setImageResource(R.mipmap.iv_pwd_no_show);
                }
            }
        });



        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_commit_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPwd.getText().toString().trim())||TextUtils.isEmpty(etPwd2.getText().toString().trim()))
                {
                    sToast("密码不能为空");
                }else if(etPwd.getText().toString().trim().length()!=6||etPwd2.getText().toString().trim().length()!=6)
                {
                    sToast("请输入6位数交易密码");
                }else if(!etPwd.getText().toString().trim().equals(etPwd2.getText().toString().trim()))
                {
                    sToast("两次密码输入不一致");
                }else
                {
                    gif_loading.setVisibility(View.VISIBLE);
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("newpassword", etPwd.getText().toString().trim());
                    HttpConnect.post(SettingPayPwdActivity.this, "member_set_passwordpay", par, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            JSONObject data = JSONObject.fromObject(arg0.body().string());
                            if (data.get("status").equals("success")) {
                                sToast("交易密码设置成功");
                                finish();
                            } else {
                                boolean flag = Tools.isPhonticName(data.getString("msg"));
                                if(!flag)
                                {
                                    sToast(data.getString("msg"));
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gif_loading.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gif_loading.setVisibility(View.GONE);
                                }
                            });
//                            sToast("链接超时！");
                        }
                    });
                }
            }
        });
    };

}
