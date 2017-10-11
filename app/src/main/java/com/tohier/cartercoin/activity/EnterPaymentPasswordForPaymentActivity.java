package com.tohier.cartercoin.activity;

import android.app.Activity;
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
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/22.
 */

public class EnterPaymentPasswordForPaymentActivity extends MyBaseActivity {

    private ImageView ivCancel;
    private TextView tv_deal_par;
    private EditText et_pay_pwd;
    private Button btn_commit;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.iv_cancel:
                    finish();
                    break;

                case R.id.btn_commit:
                    confirmPayMoney();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypwd_for_payment_layout);

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
        ivCancel = (ImageView) this.findViewById(R.id.iv_cancel);
        tv_deal_par = (TextView) this.findViewById(R.id.tv_deal_par);
        et_pay_pwd = (EditText) this.findViewById(R.id.et_pay_pwd);
        btn_commit = (Button) this.findViewById(R.id.btn_commit);

        ivCancel.setOnClickListener(onClickListener);
        btn_commit.setOnClickListener(onClickListener);


        if(!TextUtils.isEmpty(getIntent().getStringExtra("paymoney")))
        {
            tv_deal_par.setText(getIntent().getStringExtra("paymoney"));
        }
    }

    public void confirmPayMoney()
    {
        btn_commit.setClickable(false);
        if(TextUtils.isEmpty(et_pay_pwd.getText().toString().trim()))
        {
               sToast("请输入交易密码");
               btn_commit.setClickable(true);
               return;
        }

        Map<String, String> par = new HashMap<String, String>();
        par.put("linkcode", getIntent().getStringExtra("linkcode"));
        par.put("passwordpay", et_pay_pwd.getText().toString().trim());
        par.put("money",tv_deal_par.getText().toString().trim());
        par.put("PayType",getIntent().getStringExtra("paytype"));
        HttpConnect.post(this, "member_paymend_qr_new", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success"))
                {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(
                                final Message msg)
                        {
                            Set<String> keys3 = MyApplication.maps.keySet();
                            if(keys3!=null&&keys3.size()>0)
                            {
                                if(keys3.contains("PayMoneyActivity"))
                                {
                                    Activity activity = MyApplication.maps.get("PayMoneyActivity");
                                    activity.finish();
                                    sToast("转账成功");
                                };
                            }
                            finish();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else
                {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(
                                final Message msg)
                        {
                            boolean flag = Tools.isPhonticName(data.getString("msg"));
                            if(!flag)
                            {
                                sToast(data.getString("msg"));
                                btn_commit.setClickable(true);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1)
            {
                Handler dataHandler = new Handler(
                        getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(
                            final Message msg)
                    {
                        sToast("链接超时！");
                        btn_commit.setClickable(true);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

}
