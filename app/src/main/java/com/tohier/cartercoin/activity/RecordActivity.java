package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.RecordAdapter;
import com.tohier.cartercoin.bean.Record;
import com.tohier.cartercoin.bean.Transaction;
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
import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends MyBackBaseActivity implements AbsListView.OnScrollListener{
    private ListView gridView;
    private ArrayList<Record> datas = new ArrayList<Record>();
    private RecordAdapter adapter;
    private LoadingView cif_loading,loadingView;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;
    private ImageView ivBack;
//    private WXPaySuccessReceiver wxPaySuccessReceiver;

    private boolean isLastRow = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        MyApplication.activities.add(this);
        init();

    }

    private void init() {
        gridView = (ListView) findViewById(R.id.lv_upgrade);
        cif_loading = (LoadingView) findViewById(R.id.cif_loading);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);


        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        ivBack = (ImageView) findViewById(R.id.title_back);
        adapter = new RecordAdapter(this,datas,getIntent().getStringExtra("type"),ivBack);
        gridView.setAdapter(adapter);

//
//        wxPaySuccessReceiver = new WXPaySuccessReceiver("人民币充值");
//        IntentFilter filter = new IntentFilter("paysuccess");
//        registerReceiver(wxPaySuccessReceiver, filter);
        if (Tools.getAPNType(this) == true){
            ivNoLink.setVisibility(View.GONE);
            getData(1);
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(RecordActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getData(1);
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });

        gridView.setOnScrollListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(wxPaySuccessReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void initData() {

    }
    public void back(View view){
        finish();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
            page++;
            getData(2);
            isLastRow = false;
        }
    }



    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //如果当前列表的数量等于查询的总数量,则不做任何操作


        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }

    }

    private void getData(final int isClear) {
        if (isClear == 1) {
            datas.clear();
            cif_loading.setVisibility(View.VISIBLE);

        }else{
            if (gridView.getFooterViewsCount()<=0){
                gridView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");
        HttpConnect.post(this, "member_cash_in_list", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.optJSONArray("data");
                    Transaction transaction;
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String rownum = jsonObjNews.optString("rownum");
                            String id = jsonObjNews.optString("id");
                            String paymode = jsonObjNews.optString("paymode");
                            String price = jsonObjNews.optString("qty");
                            String date = jsonObjNews.optString("date");
                            String status = jsonObjNews.optString("status");
                            Record record = new Record(date,price,paymode,status);
                            record.setId(id);
                            datas.add(record);

                        }
                    }
                }else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (isClear == 1) {
//                                ivNodata.setVisibility(View.VISIBLE);
                            }else {
                                loadingView.noMoreData("没有更多数据了");
                            }
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setList(datas);
                                adapter.notifyDataSetChanged();
                                cif_loading.setVisibility(View.GONE);
                                if (datas.size()>0){
                                    ivNodata.setVisibility(View.GONE);
                                }else{
                                    ivNodata.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                });

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cif_loading.setVisibility(View.GONE);
                        if (datas.size()>0){
                            ivNodata.setVisibility(View.GONE);
                        }else{
                            ivNodata.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
}
