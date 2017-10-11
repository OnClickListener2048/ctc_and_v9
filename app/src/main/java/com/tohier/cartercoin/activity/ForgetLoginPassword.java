package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/23.
 */

public class ForgetLoginPassword extends MyBackBaseActivity {

    private ImageView ivBack,iv_pwd_isShow;
    private EditText etPhoneNum,etVerificationCode,et_new_pwd;
    private Button btn_commit_update,btn_getyzm;
    private int count = 120;
    private GifImageView gif_loading;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btn_getyzm.setText("获取短信验证码   ");
                    btn_getyzm.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
                    btn_getyzm.setText("验证码已发送 （"+count+"s）   ");
                    btn_getyzm.setClickable(false);
                    postDelayed(thread,1000);
                }
            }
        }
    };
    Thread thread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x111);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
               switch (v.getId())
               {
                   case R.id.iv_back2:
                       finish();
                       break;
                   case R.id.btn_getyzm:
                       onValidateCode();
                       break;

                   case R.id.btn_commit_update:
                       confirmFindLoginPwd();
                       break;

                   case R.id.iv_pwd_isShow:
                       iv_pwd_isShow.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if(et_new_pwd.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                               {
                                   //显示
                                   et_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                   iv_pwd_isShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                               }else
                               {
                                   et_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                   iv_pwd_isShow.setImageResource(R.mipmap.iv_pwd_no_show);
                               }
                           }
                       });
                       break;
               }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_loginpwd);
        myProgressDialog.setTitle("修改中...");

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
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        etPhoneNum = (EditText) this.findViewById(R.id.et_phonenum);
        etVerificationCode = (EditText) this.findViewById(R.id.et_yzm);
        et_new_pwd = (EditText) this.findViewById(R.id.et_new_pwd);
        btn_commit_update = (Button) this.findViewById(R.id.btn_commit_update);
        btn_getyzm = (Button) this.findViewById(R.id.btn_getyzm);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);

//        if(!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum()))
//        {
//            etPhoneNum.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum());
//        }

    }

    private void setUpView() {
        ivBack.setOnClickListener(onClickListener);
        btn_getyzm.setOnClickListener(onClickListener);
        btn_commit_update.setOnClickListener(onClickListener);
        iv_pwd_isShow.setOnClickListener(onClickListener);
    }

    private void confirmFindLoginPwd()
    {
        if(TextUtils.isEmpty(etPhoneNum.getText().toString().trim()))
        {
            sToast("请输入手机号");
            return;
        }
        if(etPhoneNum.getText().toString().trim().length()!=11)
        {
            sToast("手机号输入不合法");
            return;
        }
        if(TextUtils.isEmpty(etVerificationCode.getText().toString().trim()))
        {
            sToast("请输入验证码");
            return;
        }
        if(TextUtils.isEmpty(et_new_pwd.getText().toString().trim()))
        {
            sToast("请输入新密码");
            return;
        }
        Map<String, String> par = new HashMap<String, String>();
        par.put("name", etPhoneNum.getText().toString().trim());
        par.put("password", et_new_pwd.getText().toString().trim());
        par.put("sms",etVerificationCode.getText().toString().trim());
        myProgressDialog.show();
        HttpConnect.post(this, "member_modify_password_byname", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    sToast("修改密码成功");
                    finish();
                } else {
                    sToast(data.getString("msg"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myProgressDialog.cancel();
                    }
                });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
//                sToast("链接超时！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       myProgressDialog.cancel();
                    }
                });
            }
        });

    }


    public void onValidateCode() {
        if(TextUtils.isEmpty(etPhoneNum.getText().toString().trim()))
        {
            sToast("请输入手机号");
            return;
        }
        if(etPhoneNum.getText().toString().trim().length()!=11)
        {
            sToast("手机号输入不合法");
            return;
        }
        String phone = ((EditText) findViewById(R.id.et_phonenum)).getText().toString();
        if (StringUtils.isNotBlank(phone) && NumberUtils.isNumber(phone) && phone.length() == 11) {
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", phone);
            par.put("code","member_modify_password_byname");
            gif_loading.setVisibility(View.VISIBLE);
            HttpConnect.post(this, "sms_member_code", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {
                        handler.post(thread);
                    } else {
                        sToast(data.getString("msg"));
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
//                    sToast("链接超时！");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gif_loading.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } else {
            sToast("请填入正确的手机号!");
        }
    }

}
