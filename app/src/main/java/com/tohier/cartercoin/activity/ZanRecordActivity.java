package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.ZanAdapter;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ZanRecordActivity extends MyBackBaseActivity {

    private TextView tv_zan_count;
    private ListView lv_zan_record;
    private LoadingView gifLoading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;


    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private ZanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zan_record);
        init();
    }

    private void init() {
        tv_zan_count = (TextView) findViewById(R.id.tv_zan_count);
        lv_zan_record = (ListView) findViewById(R.id.lv_zan_record);
        gifLoading = (LoadingView) findViewById(R.id.cif_loading);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        adapter = new ZanAdapter(this,list);
        lv_zan_record.setAdapter(adapter);

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(ZanRecordActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getZanDatas();
                    HttpConnect.post(ZanRecordActivity.this, "member_prise_byday", null, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                final JSONArray array = data.optJSONArray("data");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_zan_count.setText("连续达标点赞"+array.getJSONObject(0).optString("value")+"天");
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_zan_count.setText("连续达标点赞0天");
                                    }
                                });
                            }
                        }

                        @Override

                        public void onFailure(Request arg0, IOException arg1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_zan_count.setText("连续达标点赞0天");
                                }
                            });
                        }
                    });
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Tools.getAPNType(ZanRecordActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getZanDatas();
            HttpConnect.post(ZanRecordActivity.this, "member_prise_byday", null, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.optString("status").equals("success")){
                        final JSONArray array = data.optJSONArray("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_zan_count.setText("连续达标点赞"+array.getJSONObject(0).optString("value")+"天");
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_zan_count.setText("连续达标点赞0天");
                            }
                        });
                    }
                }

                @Override

                public void onFailure(Request arg0, IOException arg1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_zan_count.setText("连续达标点赞0天");
                        }
                    });
                }
            });
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view){
        finish();
    }

    private void getZanDatas(){

        gifLoading.setVisibility(View.VISIBLE);

        HttpConnect.post(this, "member_thumbs_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    HashMap<String,String> map = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        map = new HashMap<String, String>();
                        JSONObject obj = array.optJSONObject(i);
                        map.put("time",obj.optString("date"));
                        map.put("count",obj.optString("number")+"/"+obj.optString("beprise"));

                        list.add(map);
                    }

                }


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.setDatas(list);
                        adapter.notifyDataSetChanged();
                        gifLoading.setVisibility(View.GONE);
                        if (list.size()>0){
                            ivNodata.setVisibility(View.GONE);
                        }else{
                            ivNodata.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public void onFailure(final Request arg0, final IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        gifLoading.setVisibility(View.GONE);

                    }
                });

            }
        });
    }
}
