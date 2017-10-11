package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ConFirmActivity extends MyBaseActivity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private ImageView ivCancel;
    private Button btnCommit;
    private EditText etPayPwd;
    private TextView tvDealPer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_firm);

        init();
        setup();


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

    private void setup() {

        btnCommit.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }

    private void init() {
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        etPayPwd = (EditText) findViewById(R.id.et_pay_pwd);
        tvDealPer = (TextView) findViewById(R.id.tv_deal_par);
        tvDealPer.setText(getIntent().getStringExtra("price"));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_commit:
                if (!TextUtils.isEmpty(etPayPwd.getText().toString())){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnCommit.setClickable(false);
                        }
                    });

                    HashMap<String,String> par = new HashMap<String,String>();
                    par.put("pid",getIntent().getStringExtra("pid"));
                    par.put("passwordpay",etPayPwd.getText().toString());
                    HttpConnect.post(this, "member_products_pay", par, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            final JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnCommit.setClickable(true);
                                    }
                                });
                                startActivity(new Intent(ConFirmActivity.this,MyOrderActivity.class));
                                sToast("支付成功");
                                finish();
                            }else{
                                boolean flag = Tools.isPhonticName(data.optString("msg"));
                                if(!flag)
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnCommit.setClickable(true);
                                            sToast(data.optString("msg"));
                                        }
                                    });

                                }
                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnCommit.setClickable(true);
                                }
                            });
                        }
                    });

                }else{
                    sToast("请输入交易密码");
                }
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }

    }




}
