package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.Invalid_Coupon_Adapter;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8.
 */

public class InvalidCouponActivity extends MyBackBaseActivity {

    private ListView lv_coupon;
    private List<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
    private Invalid_Coupon_Adapter adapter;
    private LoadingView gif_loading;
    private NoLinkView ivNoLink;
    private NoDataView iv_isnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_coupon_layout);

        initData();
        if (Tools.getAPNType(InvalidCouponActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            loadData();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        lv_coupon = (ListView) this.findViewById(R.id.lv_coupon);
        gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_isnull);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
        lv_coupon.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new Invalid_Coupon_Adapter(datas, this);
        adapter.setCouponStatus("已使用and已过期");
        lv_coupon.setAdapter(adapter);

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(InvalidCouponActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    loadData();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadData() {
        gif_loading.setVisibility(View.VISIBLE);
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
        par.put("status", 1+"");
        HttpConnect.post(InvalidCouponActivity.this, "member_coupon_list", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
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
//                            YouHuiQuan youHuiQuan = new YouHuiQuan(id,name,money,status,createdate,enddate,remark,sta,mon);
                            YouHuiQuan youHuiQuan = new YouHuiQuan(id,name,money,status,createdate,enddate,remark,min,sta,mon,type);

                            datas.add(youHuiQuan);
                        }
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.setItems(datas);
                            adapter.notifyDataSetChanged();
                            gif_loading.setVisibility(View.GONE);
                            if (datas.size()>0){
                                iv_isnull.setVisibility(View.GONE);
                            }else{
                                iv_isnull.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(!TextUtils.isEmpty(msg)&&!Tools.isPhonticName(msg))
                            {
                                sToast(msg);
                            }else
                            {
//                                sToast("暂无数据");
                            }
                            adapter.setItems(datas);
                            adapter.notifyDataSetChanged();
                            gif_loading.setVisibility(View.GONE);
                            if (datas.size()>0){
                                iv_isnull.setVisibility(View.GONE);
                            }else{
                                iv_isnull.setVisibility(View.VISIBLE);
                            }
//                            if(datas.size()==0)
//                            {
//                                iv_isnull.setVisibility(View.VISIBLE);
//                            }
//									((YouHuiJuanActivity)ctx).sToast("暂无数据");
                        }
                    });
                }


            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

//                        sToast("请检查您的网络链接状态");
                        gif_loading.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    public void back(View v)
    {
        finish();
    }


}
