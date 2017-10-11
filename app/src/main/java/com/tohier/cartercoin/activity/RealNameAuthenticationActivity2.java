package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/12/30.
 */

public class RealNameAuthenticationActivity2 extends MyBackBaseActivity {

    private TextView et_name,et_id_number;
    private GifImageView gif_loading;
    private ImageView iv_back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_real_name_authentication2);

        initData();
        loadMemberInfo();

    }

    @Override
    public void initData() {
        et_name = (TextView) this.findViewById(R.id.et_name);
        et_id_number = (TextView) this.findViewById(R.id.et_id_number);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);

        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String idnumber;
    private String name;

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        myProgressDialog.setTitle("加载中...");
        myProgressDialog.show();
        HttpConnect.post(RealNameAuthenticationActivity2.this, "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    name = data.getJSONArray("data").getJSONObject(0).getString("name");
                    idnumber = data.getJSONArray("data").getJSONObject(0).getString("idnumber");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            myProgressDialog.cancel();
                            if(!TextUtils.isEmpty(idnumber))
                            {
                                et_id_number.setText(idnumber.substring(0,3)+"***********"+idnumber.substring(14,18));
                            }

                            if(!TextUtils.isEmpty(name))
                            {
                                et_name.setText(name);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            myProgressDialog.cancel();
                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        sToast("网络质量不佳，请检查网络！");
                        myProgressDialog.cancel();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }
}
