package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.AlipayListAdapter;
import com.tohier.cartercoin.bean.AlipayData;
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
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/31.
 */
public class AlipayListActivity extends MyBaseActivity{

    private ImageView iv_back;
    private ListView lv;
    private Button btn_add;
    private List<AlipayData> datas = new ArrayList<AlipayData>();
    private AlipayListAdapter adapter;

    private LoadingView cif_loading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipaylist);

        MyApplication.maps.put("AlipayListActivity",this);
        initData();

    }

    @Override
    public void initData() {
        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        lv = (ListView) this.findViewById(R.id.lv);
        btn_add = (Button) this.findViewById(R.id.btn_add);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        cif_loading  = (LoadingView) findViewById(R.id.cif_loading);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        if (Tools.getAPNType(AlipayListActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getData();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Set<String> keys3 = MyApplication.maps.keySet();
                if(keys3!=null&&keys3.size()>0)
                {
                    if(keys3.contains("TiXianActivity"))
                    {
                        Activity activity = MyApplication.maps.get("TiXianActivity");
                        activity.finish();
                    };
                }

                if(keys3!=null&&keys3.size()>0)
                {
                    if(keys3.contains("PresentationModeActivity"))
                    {
                        Activity activity = MyApplication.maps.get("PresentationModeActivity");
                        activity.finish();
                    };
                }
                Intent intent = new Intent(AlipayListActivity.this,TiXianActivity.class);
                intent.putExtra("account", datas.get(position).getAccount());
                intent.putExtra("id", datas.get(position).getId());
                intent.putExtra("mode", "alipay");
                startActivity(intent);
                finish();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlipayListActivity.this,AddAlipayActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(AlipayListActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getData();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getData()
    {
        cif_loading.setVisibility(View.VISIBLE);
        HttpConnect.post(this, "member_alipay_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject jsonObject = JSONObject.fromObject(arg0.body().string());
                if (jsonObject.getString("status").equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (jsonObject2 != null) {
                                String card_id = jsonObject2.getString("BankCard_ID");

                                String name = jsonObject2.getString("Name");

                                String account = jsonObject2.getString("Account");
                                AlipayData alipayData = new AlipayData(card_id,name,account);
                                datas.add(alipayData);
                            }
                        }
                    }
                }
                AlipayListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter = new AlipayListAdapter(datas,AlipayListActivity.this);
                        lv.setAdapter(adapter);
                        cif_loading.setVisibility(View.GONE);
                        if (datas.size()>0){
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
    };
}
