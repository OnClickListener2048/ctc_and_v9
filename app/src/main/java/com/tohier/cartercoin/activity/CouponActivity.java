package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.Coupon_list_Adapter;
import com.tohier.cartercoin.bean.YouHuiQuan;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends MyBaseActivity {

    private ListView gridView;
    private ArrayList<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
    private Coupon_list_Adapter adapter;
    private String type;

    private LoadingView gifLoading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        init();
        if (Tools.getAPNType(CouponActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getData();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }


    private void init() {
        type = getIntent().getStringExtra("type");

        sharedPreferences = getSharedPreferences("contact",0);
        sharedPreferences.edit().putString("isresume","1").commit();

        gridView = (ListView) findViewById(R.id.listview);
        gifLoading = (LoadingView) findViewById(R.id.gif_loading);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        adapter = new Coupon_list_Adapter(datas,this);
        gridView.setAdapter(adapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = null;
                if (type.equals("2")){
                    intent = new Intent(CouponActivity.this, VipUpgradeActivity.class);
                }else if (type.equals("5")){
                    intent = new Intent(CouponActivity.this, BuyAssetsActivity.class);
                }else{
                    intent = new Intent(CouponActivity.this, BuildOrderActivity.class);
                }

                intent.putExtra("id",datas.get(position).getId())
                        .putExtra("price",datas.get(position).getMoney())
                        .putExtra("min",datas.get(position).getMin()
                        );
                setResult(RESULT_OK,intent);
                finish();


            }
        });


        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(CouponActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getData();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getData(){

        gifLoading.setVisibility(View.VISIBLE);
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
        par.put("status", 0+"");
        par.put("type",type);
        HttpConnect.post(this, "member_assets_upgrade_coupon", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                if (data.get("status").equals("success")) {
                    final JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String name = jsonObjNews.optString("name");
                            String money = jsonObjNews.optString("money");
                            String status = jsonObjNews.optString("status");
                            String createdate = jsonObjNews.optString("createdate");
                            String enddate = jsonObjNews.optString("enddate");
                            String remark = jsonObjNews.optString("limit");
                            String min = jsonObjNews.optString("min");
                            String sta = jsonObjNews.optString("sta");
                            String mon = jsonObjNews.optString("mon");
                            String type = jsonObjNews.optString("type");
                            YouHuiQuan youHuiQuan = new YouHuiQuan(id,name,money,status,createdate,enddate,remark,min,sta,mon,type);
                            datas.add(youHuiQuan);
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(datas);
                        adapter.notifyDataSetChanged();
                        gifLoading.setVisibility(View.GONE);
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
                gifLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    public void back(View view){
        Intent intent = null;
        if (type.equals("2")){
            intent = new Intent(CouponActivity.this, VipUpgradeActivity.class);
        }else if (type.equals("5")){
            intent = new Intent(CouponActivity.this, BuyAssetsActivity.class);
        }else if(type.equals("1")){
            intent = new Intent(CouponActivity.this, BuildOrderActivity.class);
        }
        intent.putExtra("id","0")
                .putExtra("price","0");
        setResult(RESULT_OK,intent);
        finish();
    }

}
