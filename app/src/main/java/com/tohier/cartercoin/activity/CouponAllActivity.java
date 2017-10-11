package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8.
 */

public class CouponAllActivity extends MyBackBaseActivity {

    private ListView lv_coupon;
    private TextView tv_into_invalid_coupon,tv_coupon_money;
    private List<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
    private Coupon_list_Adapter adapter;
    private LoadingView gif_loading;
    private NoLinkView ivNoLink;
    private  NoDataView iv_isnull;

    private String[] textStrings;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponall_layout);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        lv_coupon = (ListView) this.findViewById(R.id.lv_coupon);
        tv_coupon_money = (TextView) this.findViewById(R.id.tv_coupon_money);
        gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_isnull);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
        tv_into_invalid_coupon = (TextView) this.findViewById(R.id.tv_into_invalid_coupon);

        lv_coupon.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new Coupon_list_Adapter(datas, this);
        lv_coupon.setAdapter(adapter);
    }

    private void setUpView() {
        tv_into_invalid_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(CouponAllActivity.this,InvalidCouponActivity.class));
            }
        });

        lv_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                String type = datas.get(position).getType();
                if(!TextUtils.isEmpty(type))
                {
                    if(type.equals("1"))   //商城
                    {
                        intent = new Intent(CouponAllActivity.this,MallActivity.class);
                        startActivity(intent);
                    }else if(type.equals("2")) //会员升级
                    {
                        lv_coupon.setClickable(false);
                        HttpConnect.post(CouponAllActivity.this, "member_buy_upgrade_count", null, new Callback() {
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
                                            Intent intent = new Intent(CouponAllActivity.this, VipUpgradeActivity.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putStringArray("textStrings", textStrings);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                }else
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lv_coupon.setClickable(true);
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFailure(Request arg0, IOException arg1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lv_coupon.setClickable(true);
                                    }
                                });
                            }
                        });
                    }else if(type.equals("5"))  //矿产包
                    {
                        intent = new Intent(CouponAllActivity.this,RevisionBuyAssetsActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(CouponAllActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    loadData(1);
                    loadAllCouponMoney();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    int ii = 0;
    @Override
    protected void onResume() {
        super.onResume();
        if (ii == 0){
            if (Tools.getAPNType(CouponAllActivity.this) == true){
                i++;
                ivNoLink.setVisibility(View.GONE);
                loadData(i);
                loadAllCouponMoney();
            }else{
                ivNoLink.setVisibility(View.VISIBLE);
            }
            ii++;
        }


    }

    private void loadData(int i) {
        datas.clear();
        if (i == 1){
            gif_loading.setVisibility(View.VISIBLE);
        }

        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
        par.put("status", 0+"");
        HttpConnect.post(CouponAllActivity.this, "member_coupon_list", par, new Callback() {

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
                            setListViewHeightBasedOnChildren(lv_coupon);
                            tv_into_invalid_coupon.setVisibility(View.VISIBLE);
                            gif_loading.setVisibility(View.GONE);
                            iv_isnull.setVisibility(View.GONE);
//                            if(pullToRefreshLayout!=null)
//                            {
//                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                            }
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
                            tv_into_invalid_coupon.setVisibility(View.VISIBLE);
                            gif_loading.setVisibility(View.GONE);

                            iv_isnull.setVisibility(View.VISIBLE);

                        }
                    });
                }


            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        gif_loading.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    private void loadAllCouponMoney() {
        Map<String, String> par = new HashMap<String, String>();
        par.put("Status","0");
        HttpConnect.post(CouponAllActivity.this, "member_coupon_money_all", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                        JSONObject jsonObjNews = dataArr.optJSONObject(0);
                        if(jsonObjNews!=null)
                        {
                            final String al = jsonObjNews.getString("al");
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    tv_coupon_money.setText(al+"元");
                                }
                            });
                        }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(!TextUtils.isEmpty(msg)&&!Tools.isPhonticName(msg))
                            {
                                sToast(msg);
                            }
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
                    }
                });

            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight = totalHeight+listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }


    public void back(View v)
    {
        finish();
    }
}
