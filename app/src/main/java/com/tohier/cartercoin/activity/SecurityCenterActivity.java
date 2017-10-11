package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/30.
 */

public class SecurityCenterActivity extends MyBackBaseActivity {

    private ImageView iv_back2,iv_right_arror,iv_right_arror2;
    private TextView tvSetting,tv_login_pwd_is_setting,tv_is_start_gesturespwd;
    private LinearLayout linearlayout_into_update_member_loginpwd,linearlayout_into_update_member_paypwd,linearlayout_into_update_member_gesturespwd;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId())
            {
                case R.id.iv_back2:
                       finish();
                    break;

                case R.id.linearlayout_into_update_member_gesturespwd:
                        intent = new Intent(SecurityCenterActivity.this,GesturesPasswordUpdateActivity.class);
                    break;

            }
            if(intent!=null)
            {
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securitycenter_layout);

        initData();

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
        tvSetting = (TextView) this.findViewById(R.id.tv_isSetting);
        tv_login_pwd_is_setting = (TextView) this.findViewById(R.id.tv_login_pwd_is_setting);
        tv_is_start_gesturespwd = (TextView) this.findViewById(R.id.tv_is_start_gesturespwd);
        iv_right_arror = (ImageView) this.findViewById(R.id.iv_right_arror);
        iv_right_arror2 = (ImageView) this.findViewById(R.id.iv_right_arror2);
        linearlayout_into_update_member_loginpwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_loginpwd);
        linearlayout_into_update_member_paypwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_paypwd);
        linearlayout_into_update_member_gesturespwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_gesturespwd);

        iv_back2.setOnClickListener(onClickListener);
        linearlayout_into_update_member_gesturespwd.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
        String opengestures = sharedPreferences.getString("opengestures","");
        if(!TextUtils.isEmpty(opengestures)&&opengestures.equals("True"))   //开启状态
        {
            tv_is_start_gesturespwd.setText("已开启");
        }else                                                            //未开启状态
        {
            tv_is_start_gesturespwd.setText("未开启");
        }
        isExitPayPwd();
    }


    /**
     * 是否有交易密码
     */
    public void isExitPayPwd()
    {
        HttpConnect.post(this, "member_is_password_pay", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    String value = data.getJSONArray("data").getJSONObject(0).getString("value");
                    String password = data.getJSONArray("data").getJSONObject(0).getString("password");


                    if(!TextUtils.isEmpty(password)&&password.equals("1")) //存在支付密码  要修改支付密码
                    {
                        SecurityCenterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_login_pwd_is_setting.setText("修改");
                                iv_right_arror2.setVisibility(View.VISIBLE);

                                linearlayout_into_update_member_loginpwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SecurityCenterActivity.this,UpdateLoginPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else             //不存在需要设置
                    {
                        SecurityCenterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_login_pwd_is_setting.setText("设置");
                                iv_right_arror2.setVisibility(View.VISIBLE);
                                linearlayout_into_update_member_loginpwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SecurityCenterActivity.this,SettingLoginPasswordActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }

                    if(!TextUtils.isEmpty(value)&&value.equals("1")) //存在支付密码  要修改支付密码
                    {

                        SecurityCenterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSetting.setText("修改");
                                iv_right_arror.setVisibility(View.VISIBLE);
                                linearlayout_into_update_member_paypwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SecurityCenterActivity.this,UpdatePayPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else             //不存在需要设置
                    {
                        SecurityCenterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSetting.setText("设置");
                                iv_right_arror.setVisibility(View.VISIBLE);
                                linearlayout_into_update_member_paypwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SecurityCenterActivity.this,SettingPayPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                } else {
                    sToast(data.getString("msg"));
                }
                mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                mZProgressHUD.cancel();
                sToast("链接超时！");
            }
        });
    }
}
