package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/17.
 */

public class UpdateBandPhoneActivity extends MyBackBaseActivity {

    private ImageView ivBack;
    private EditText et_xian_phone,et_xian_yzm;
    private Button btn_get_xian_yanzhengma,btn_commit_update_bandphone;

    private int xianCount = 120;
    private TextView tv_yuan_phone;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x222){
                xianCount--;
                if (xianCount<=0){
                    btn_get_xian_yanzhengma.setText("获取短信验证码   ");
                    btn_get_xian_yanzhengma.setClickable(true);
                    xianCount = 120;
                    handler.removeCallbacks(xianthread);
                }else{
                    btn_get_xian_yanzhengma.setText("验证码已发送 （"+xianCount+"s）   ");
                    btn_get_xian_yanzhengma.setClickable(false);
                    postDelayed(xianthread,1000);
                }
            }
        }
    };

    Thread xianthread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x222);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_get_xian_yanzhengma:
                    onValidateCode(false);
                    break;

                case R.id.btn_commit_update_nickname:
                    verificationMobileIsBand();
                    break;

                case R.id.iv_back2:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_bandphone_layout);

        initData();
        setUpView();

    }

    @Override
    public void initData() {
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        tv_yuan_phone = (TextView) this.findViewById(R.id.et_nickname);
        et_xian_phone = (EditText) this.findViewById(R.id.et_xian_phone);
        et_xian_yzm = (EditText) this.findViewById(R.id.et_xian_yzm);
        btn_get_xian_yanzhengma = (Button) this.findViewById(R.id.btn_get_xian_yanzhengma);
        btn_commit_update_bandphone = (Button) this.findViewById(R.id.btn_commit_update_nickname);

        String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
        if(!TextUtils.isEmpty(phoneNum)&&!phoneNum.equals(""))
        {
            tv_yuan_phone.setText(phoneNum);
        }
    }

    private void setUpView() {
            btn_get_xian_yanzhengma.setOnClickListener(onClickListener);
            btn_commit_update_bandphone.setOnClickListener(onClickListener);
            ivBack.setOnClickListener(onClickListener);
    }

    public void onValidateCode(final boolean isyuan) {
        String phone = "";
            if(et_xian_phone.getText().toString().trim().length()!=11) {
                sToast("现手机号输入不合法");
                return;
            }
        phone = et_xian_phone.getText().toString().trim();
        if (StringUtils.isNotBlank(phone) && NumberUtils.isNumber(phone) && phone.length() == 11) {
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", phone);
            par.put("code","member_mobile_upgrade");
            HttpConnect.post(this, "sms_member_code", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                            handler.post(xianthread);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                sToast(data.getString("msg"));
                            }
                        });
                    }
                }


                @Override
                public void onFailure(Request arg0, IOException arg1) {

                }
            });
        } else {
            sToast("请填入正确的手机号!");
        }
    }


    /**
     * 验证手机号是否能绑定
     */
    public void verificationMobileIsBand()
    {
        if(TextUtils.isEmpty(et_xian_phone.getText().toString().trim()))
        {
            sToast("请输入要绑定的手机号码");
            return;
        }if(TextUtils.isEmpty(et_xian_yzm.getText().toString().trim())) {
            sToast("请输入要绑定手机号码的验证码");
            return;
        }else
        {
            myProgressDialog.setTitle("正在加载...");
            myProgressDialog.show();
            Map<String, String> par1 = new HashMap<String, String>();
            par1.put("oldmobile",LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum() );
            par1.put("newmobile",et_xian_phone.getText().toString().trim() );
            par1.put("newsms",et_xian_yzm.getText().toString().trim() );
            par1.put("code","member_mobile_upgrade" );
            par1.put("type","0" );

            HttpConnect.post(this, "member_mobile_upgrade", par1, new Callback() {

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("网络质量不佳，请检查网络");
                            myProgressDialog.cancel();
                        }
                    });
                }

                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject object = JSONObject.fromObject(arg0.body().string());
                    if (object.get("status").equals("success")) {
                        Handler dataHandler = new Handler(getContext()
                                .getMainLooper()) {

                            @Override
                            public void handleMessage(final Message msg) {
                                myProgressDialog.cancel();
                                LoginUser.getInstantiation(getApplicationContext()).setPhoneNum(et_xian_phone.getText().toString().trim());
                                finish();
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }else{
                        Handler dataHandler = new Handler(getContext()
                                .getMainLooper()) {

                            @Override
                            public void handleMessage(final Message msg) {
                                myProgressDialog.cancel();
                                boolean flag = Tools.isPhonticName(object.getString("msg"));
                                if(!flag)
                                {
                                    sToast(object.getString("msg"));
                                }
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                }
            });
        }
    }
    
}
