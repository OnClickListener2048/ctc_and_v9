package com.tohier.cartercoin.activity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.UpgradeRecordAdapter;
import com.tohier.cartercoin.bean.UpgradeRecprd;
import com.tohier.cartercoin.broadcastreceiver.WXPaySuccessReceiver;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class BuyAssetsRecordActivity extends MyBackBaseActivity {


    private ListView lvUpgradeReCord;
    private LoadingView cif_loading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;
    private ArrayList<UpgradeRecprd> list = new ArrayList<UpgradeRecprd>();
    private UpgradeRecordAdapter adapter;
    private WXPaySuccessReceiver wxPaySuccessReceiver;
    private SharedPreferences sharedPreferences;

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyassets_record);
        MyApplication.activities.add(this);
        init();

    }

    private void init() {

        sharedPreferences = getSharedPreferences("contact",0);
        sharedPreferences.edit().putString("isresume","1").commit();

        lvUpgradeReCord = (ListView) findViewById(R.id.lv_upgrade);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivBack = (ImageView) findViewById(R.id.title_back);

        cif_loading  = (LoadingView) findViewById(R.id.cif_loading);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        adapter = new UpgradeRecordAdapter(this,list,1,getIntent().getStringExtra("type"),ivBack);
        lvUpgradeReCord.setAdapter(adapter);


        wxPaySuccessReceiver = new WXPaySuccessReceiver("购买资产包");
        IntentFilter filter = new IntentFilter("paysuccess");
        registerReceiver(wxPaySuccessReceiver, filter);

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(BuyAssetsRecordActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getData();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wxPaySuccessReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Tools.getAPNType(this) == true){
            ivNoLink.setVisibility(View.GONE);
            getData();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }


    private void getData() {
        list.clear();
        cif_loading.setVisibility(View.VISIBLE);
        HttpConnect.post(this, "member_assets_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    UpgradeRecprd upgradeRecprd = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        upgradeRecprd = new UpgradeRecprd();
                        JSONObject obj = array.optJSONObject(i);
                        upgradeRecprd.setState(obj.optString("status"));
                        upgradeRecprd.setAlipayPrice(obj.optString("ali"));
                        upgradeRecprd.setLevel(obj.optString("name"));
                        upgradeRecprd.setPayMode(obj.optString("paymode"));
                        upgradeRecprd.setPrice(obj.optString("money"));
                        upgradeRecprd.setTime(obj.optString("date"));
                        upgradeRecprd.setWeChatPrice(obj.optString("wechat"));
                        upgradeRecprd.setId(obj.optString("id"));
                        list.add(upgradeRecprd);
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateNotify(list);
                        cif_loading.setVisibility(View.GONE);
                        if (list.size()>0){
                            ivNodata.setVisibility(View.GONE);
                        }else{
                            ivNodata.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cif_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }


    public void back(View view){
        finish();
    }


}
