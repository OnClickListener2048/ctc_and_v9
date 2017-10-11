package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.
 */

public class UpdatePayPwdActivity extends MyBackBaseActivity {

    private EditText etOriginalPassword,etNewPassword,etNewPassword2;
    private Button btnCommitUpdate;
    private TextView tv_forget_pwd;
    private ImageView ivBack,iv_pwd_isShow,iv_pwd_isShow2,iv_pwd_isShow3;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_commit_update:
                    if(TextUtils.isEmpty(etOriginalPassword.getText().toString().trim()))
                    {
                        sToast("原密码不能为空");
                        return;
                    }else if(TextUtils.isEmpty(etNewPassword.getText().toString().trim())||TextUtils.isEmpty(etNewPassword2.getText().toString().trim()))
                    {
                        sToast("新密码不能为空");
                        return;
                    }else if(!etNewPassword.getText().toString().trim().equals(etNewPassword2.getText().toString().trim()))
                    {
                        sToast("两次密码输入不一致");
                        return;
                    }else
                    {
                        myProgressDialog.setTitle("正在加载...");
                        myProgressDialog.show();
                        Map<String, String> par = new HashMap<String, String>();
                        par.put("oldpassword", etOriginalPassword.getText().toString().trim());
                        par.put("newpassword", etNewPassword.getText().toString().trim());
                        HttpConnect.post(UpdatePayPwdActivity.this, "member_modify_passwordpay", par, new Callback() {

                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                JSONObject data = JSONObject.fromObject(arg0.body().string());
                                if (data.get("status").equals("success")) {
                                    sToast("交易密码修改成功");
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sToast("原密码错误");
                                        }
                                    });
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myProgressDialog.cancel();
                                    }
                                });
                                sToast("网络质量不佳，请检查网络！");
                            }
                        });
                    }
                    break;
                case R.id.tv_forget_pwd:
                     startActivity(new Intent(UpdatePayPwdActivity.this,ForgetPayPassword.class));
                    break;
                case R.id.iv_back2:
                     finish();
                    break;
                case R.id.iv_pwd_isShow:
                    if(etOriginalPassword.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                    {
                        //显示
                        etOriginalPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_pwd_isShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                    }else
                    {
                        etOriginalPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_pwd_isShow.setImageResource(R.mipmap.iv_pwd_no_show);
                    }
                    break;
                case R.id.iv_pwd_isShow2:
                    if(etNewPassword.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                    {
                        //显示
                        etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_pwd_isShow2.setImageResource(R.mipmap.iv_pay_pwd_show);
                    }else
                    {
                        etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_pwd_isShow2.setImageResource(R.mipmap.iv_pwd_no_show);
                    }
                    break;
                case R.id.iv_pwd_isShow3:
                    if(etNewPassword2.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                    {
                        //显示
                        etNewPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        iv_pwd_isShow3.setImageResource(R.mipmap.iv_pay_pwd_show);
                    }else
                    {
                        etNewPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        iv_pwd_isShow3.setImageResource(R.mipmap.iv_pwd_no_show);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_paypwd);

        initData();
        setUpView();

    }

    @Override
    public void initData() {
        etOriginalPassword = (EditText) this.findViewById(R.id.et_originalpassword);
        etNewPassword = (EditText) this.findViewById(R.id.et_new_pwd);
        etNewPassword2 = (EditText) this.findViewById(R.id.et_new_pwd2);
        btnCommitUpdate = (Button) this.findViewById(R.id.btn_commit_update);
        tv_forget_pwd = (TextView) this.findViewById(R.id.tv_forget_pwd);
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        iv_pwd_isShow2 = (ImageView) this.findViewById(R.id.iv_pwd_isShow2);
        iv_pwd_isShow3 = (ImageView) this.findViewById(R.id.iv_pwd_isShow3);

        etOriginalPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void setUpView() {
        btnCommitUpdate.setOnClickListener(onClickListener);
        tv_forget_pwd.setOnClickListener(onClickListener);
        ivBack.setOnClickListener(onClickListener);
        iv_pwd_isShow.setOnClickListener(onClickListener);
        iv_pwd_isShow2.setOnClickListener(onClickListener);
        iv_pwd_isShow3.setOnClickListener(onClickListener);
    }
}
