package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/31.
 */

public class AddAlipayActivity extends MyBackBaseActivity {

    private ImageView ivBack;
    private EditText etName;
    private EditText etAcount;
    private Button btnaddok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_alipay);
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
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        etName = (EditText) this.findViewById(R.id.et_username);
        etAcount = (EditText) this.findViewById(R.id.et_kahao);
        btnaddok = (Button) this.findViewById(R.id.btnaddok);

        btnaddok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = AddAlipayActivity.this.getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
                if(TextUtils.isEmpty(name))
                {
                    sToast("请先实名认证");
                }else
                {
                if(TextUtils.isEmpty(etName.getText().toString().trim()))
                {
                    sToast("请填写姓名");
                    return;
                }else if(TextUtils.isEmpty(etAcount.getText().toString().trim()))
                {
                    sToast("请填写支付宝账号");
                    return;
                }else if(name!=null&&!name.equals(etName.getText().toString().trim()))
                {
                    sToast("支付宝信息必须与实名认证信息一致");
                    return;
                }else
                {
                    Map<String, String> par = new HashMap<String, String>();

                    par.put("account", etAcount.getText().toString().trim());

                    par.put("name", etName.getText().toString().trim());

                    HttpConnect.post(AddAlipayActivity.this, "member_add_bankaccount_or_alipay", par, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {

                            final JSONObject data = JSONObject.fromObject(arg0.body().string());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(data.getString("status").equals("success"))
                                    {
                                        Set<String> keys3 = MyApplication.maps.keySet();
                                        if(keys3!=null&&keys3.size()>0)
                                        {
                                            if(keys3.contains("AlipayListActivity"))
                                            {
                                                Activity activity = MyApplication.maps.get("AlipayListActivity");
                                                activity.finish();
                                            };
                                        }

                                        Intent intent = new Intent(AddAlipayActivity.this,AlipayListActivity.class);
                                        startActivity(intent);
                                        MyApplication.deleteActivity("AlipayListActivity");
                                        finish();
                                    }else
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(!TextUtils.isEmpty(data.getString("msg")))
                                                {
                                                    boolean flag = Tools.isPhonticName(data.getString("msg"));
                                                    if(!flag)
                                                    {
                                                        sToast(data.getString("msg"));
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            // TODO Auto-generated method stub
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    sToast("请检查您的网络状态");
                                }
                            });

                        }
                    });
                }

                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
