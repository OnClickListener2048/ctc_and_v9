package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONObject;

import java.io.IOException;


public class ShareBonusActivity extends MyBackBaseActivity {

    private ImageView tv_back;
    private TextView tv_record,tv_α,tv_α_all,tv_α_all1,tv_α_rate,tv_profiot_all,tv_profiot_me,tv_info,tv_share_bouns_zige;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_bonus);
        init();
        setUp();
    }

    private void setUp() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareBonusActivity.this,ShareBonusRecordActivity.class));
            }
        });
    }

    private void init() {
        tv_back = (ImageView) findViewById(R.id.iv_back2);
        tv_record = (TextView) findViewById(R.id.tv_record);
        tv_α = (TextView) findViewById(R.id.tv_α);
        tv_α_all = (TextView) findViewById(R.id.tv_α_all);
        tv_α_all1 = (TextView) findViewById(R.id.tv_α_all1);
        tv_α_rate = (TextView) findViewById(R.id.tv_α_rate);
        tv_profiot_all = (TextView) findViewById(R.id.tv_profiot_all);
        tv_profiot_me = (TextView) findViewById(R.id.tv_profiot_me);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_share_bouns_zige = (TextView) findViewById(R.id.tv_share_bouns_zige);


        HttpConnect.post(this, "member_bouns_profit_intro", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String ctcpercen = data.getJSONArray("data").getJSONObject(0).optString("ctcpercen");//
                    final String selfprofit = data.getJSONArray("data").getJSONObject(0).optString("selfprofit");  //
                    final String  ctc = data.getJSONArray("data").getJSONObject(0).getString("ctc") ;  //
                    final String circtc  = data.getJSONArray("data").getJSONObject(0).optString("circtc");  //
                    final String allctc  = data.getJSONArray("data").getJSONObject(0).getString("allctc");  //
                    final String paltprofit  = data.getJSONArray("data").getJSONObject(0).optString("paltprofit");
                    final String intro  = data.getJSONArray("data").getJSONObject(0).optString("intro");
                    final String min  = data.getJSONArray("data").getJSONObject(0).optString("min");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_α.setText(ctc);
                            tv_α_all.setText(circtc);
                            tv_α_all1.setText(allctc);
                            tv_α_rate.setText(ctcpercen+"%");
                            tv_profiot_all.setText(paltprofit+" RMB");
                            tv_profiot_me.setText(selfprofit+" RMB");
                            tv_info.setText(intro);
                            tv_share_bouns_zige.setText(min+"α");
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_α.setText("0.0000");
                            tv_α_all.setText("0");
                            tv_α_all1.setText("0");
                            tv_α_rate.setText("0.00%");
                            tv_profiot_all.setText("0.00 RMB");
                            tv_profiot_me.setText("0.00 RMB");
                            tv_info.setText("");
                            tv_share_bouns_zige.setText("0α");
                        }
                    });

                }





            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_α.setText("0.0000");
                        tv_α_all.setText("0");
                        tv_α_all1.setText("0");
                        tv_α_rate.setText("0.00%");
                        tv_profiot_all.setText("0.00 RMB");
                        tv_profiot_me.setText("0.00 RMB");
                        tv_info.setText("");
                    }
                });

            }
        });
    }
}
