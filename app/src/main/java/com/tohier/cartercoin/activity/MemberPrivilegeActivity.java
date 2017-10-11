package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2016/12/31.
 */

public class MemberPrivilegeActivity extends MyBackBaseActivity {

    private WebView web_update_apk;
    private ImageView iv_back;
    private Button btn_into_member_upgrade;
    private String[] textStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_privilege_layout);

        init();


    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.title_back);
        btn_into_member_upgrade = (Button) findViewById(R.id.btn_into_member_upgrade);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_into_member_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_into_member_upgrade.setClickable(false);
                HttpConnect.post(MemberPrivilegeActivity.this, "member_buy_upgrade_count", null, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    textStrings = new String[]{data.optJSONArray("data").optJSONObject(0).optString("one"),
                                            data.optJSONArray("data").optJSONObject(0).optString("two"),
                                            data.optJSONArray("data").optJSONObject(0).optString("three"),
                                            data.optJSONArray("data").optJSONObject(0).optString("four"),
                                            data.optJSONArray("data").optJSONObject(0).optString("five"),
                                            data.optJSONArray("data").optJSONObject(0).optString("six"),
                                            data.optJSONArray("data").optJSONObject(0).optString("severn"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eight"),
                                            data.optJSONArray("data").optJSONObject(0).optString("nine"),
                                            data.optJSONArray("data").optJSONObject(0).optString("ten"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eleven"),
                                            data.optJSONArray("data").optJSONObject(0).optString("twelve"),};
                                    Intent intent = new Intent(MemberPrivilegeActivity.this, VipUpgradeActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putStringArray("textStrings", textStrings);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    btn_into_member_upgrade.setClickable(true);
                                }
                            });
                        }else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_into_member_upgrade.setClickable(true);
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_into_member_upgrade.setClickable(true);
                            }
                        });
                    }
                });
            }
        });


        final TextView tv_vip01 = (TextView) findViewById(R.id.tv_vip_02);
        final TextView tv_vip02 = (TextView) findViewById(R.id.tv_vip_03);
        final TextView tv_vip03 = (TextView) findViewById(R.id.tv_vip_04);

        HttpConnect.post(this, "member_had_buy_upgrade_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final String date1 = data.optJSONArray("data").optJSONObject(1).optString("duedate");
                    final String date2 = data.optJSONArray("data").optJSONObject(2).optString("duedate");
                    final String date3 = data.optJSONArray("data").optJSONObject(3).optString("duedate");
                    final String date11 = data.optJSONArray("data").optJSONObject(1).optString("begindate");
                    final String date22 = data.optJSONArray("data").optJSONObject(2).optString("begindate");
                    final String date33 = data.optJSONArray("data").optJSONObject(3).optString("begindate");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try{

                                if (date1.contains("1900")){
                                    tv_vip01.setText("--");
                                }else{
                                    if (date2.contains("1900") && date3.contains("1900")){

                                        tv_vip01.setText(date11.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                    }else if (!date2.contains("1900") && date3.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date2.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }else if (!date3.contains("1900") && date2.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date3.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }else if (!date3.contains("1900") && !date2.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date2.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }


                                    if(tv_vip01.getText().toString().split("~")[0].equals(tv_vip01.getText().toString().split("~")[1])){
                                        tv_vip01.setText("--");
                                    }

                                }


                                if (date2.contains("1900")){
                                    tv_vip02.setText("--");
                                }else{
                                    if (date3.contains("1900")){
                                        tv_vip02.setText(date22.replaceAll("/","-").split(" ")[0]+"~"+date2.replaceAll("/","-").split(" ")[0]);
                                    }else{

                                        if (Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date2))){
                                            tv_vip02.setText("--");
                                        }else{
                                            tv_vip02.setText(date3.replaceAll("/","-").split(" ")[0]+"~"+date2.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }


                                    if(tv_vip02.getText().toString().split("~")[0].equals(tv_vip02.getText().toString().split("~")[1])){
                                        tv_vip02.setText("--");
                                    }
                                }
                                if (date3.contains("1900")){
                                    tv_vip03.setText("--");
                                }else{
                                    tv_vip03.setText(date33.replaceAll("/","-").split(" ")[0]+"~"+date3.replaceAll("/","-").split(" ")[0]);

                                    if(tv_vip03.getText().toString().split("~")[0].equals(tv_vip03.getText().toString().split("~")[1])){
                                        tv_vip03.setText("--");
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else{
                    sToast(data.optString("msg"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                sToast(arg0.toString());
            }
        });

        getUrl();

    }

    private void getUrl()
    {
        //mZProgressHUD.show();
        HttpConnect.post(this, "member_help_member_type", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String url = data.getJSONArray("data").getJSONObject(0).getString("value");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            web_update_apk = (WebView)findViewById(R.id.web);
                            web_update_apk.getSettings().setJavaScriptEnabled(true);

                            web_update_apk.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // TODO Auto-generated method stub
                                    view.loadUrl(url);
                                    return false;
                                }
                            });

                            web_update_apk.loadUrl(url);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
//						    AssetsExplainActivity.this.sToast("暂无数据");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                    //sToast(data.getString("msg"));
                }
                //mZProgressHUD.cancel();
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                //mZProgressHUD.cancel();
                Handler dataHandler = new Handler(getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
//						AssetsExplainActivity.this.sToast("请检查您的网络链接");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }
}
