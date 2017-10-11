package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.BillDetailAdapter;
import com.tohier.cartercoin.bean.BillDetail;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */

public class BillDetailsActivity extends MyBackBaseActivity implements AbsListView.OnScrollListener{

    private ListView gridView;
    private ImageView iv_back2;
    private NoDataView iv_isnull;
    private LoadingView gifImageView,loadingView;
    private NoLinkView noLinkView;
    private List<BillDetail> datas = new ArrayList<BillDetail>();
    private BillDetailAdapter adapter;

    private boolean isLastRow = false;
    private int page = 1;

    /**
     *  1-- 现金
     *  2-- α
     *  3-- 消费
     *  4-- 决战
     *  5---TAN
     *  6-- BTC
     *  7-- ETH
     *  8-- LTC
     */
    public static String type = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details_layout);

        initData();
        setUpView();

    }

    @Override
    public void initData() {
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        gifImageView = (LoadingView) this.findViewById(R.id.gif_loading);
        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);

        adapter = new BillDetailAdapter(this,datas);
        gridView = (ListView) this.findViewById(R.id.content_view);

        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        gridView.setAdapter(adapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        if (Tools.getAPNType(BillDetailsActivity.this) == true){
            noLinkView.setVisibility(View.GONE);

            getJsonDate(1);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }

    }


    private void setUpView() {
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setOnScrollListener(this);

        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(BillDetailsActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    getJsonDate(1);

                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //加载元素
            page++;
            getJsonDate(2);
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

    private void getJsonDate(final int isClear) {
        if (isClear == 1) {
            datas.clear();
            gifImageView.setVisibility(View.VISIBLE);

        }else{
            if (gridView.getFooterViewsCount()<=0){
                gridView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        Map<String, String> par = new HashMap<String, String>();
        par.put("page", page+"");

        if(!TextUtils.isEmpty(type))
        {
            if(type.equals("1"))
            {
                par.put("type", "1001");
            }else if(type.equals("2"))
            {
                par.put("type", "2001");
            }else if(type.equals("3"))
            {
                par.put("type", "4001");
            }else if(type.equals("4"))
            {
                par.put("type", "9001");
            }else if(type.equals("5"))

            {
                par.put("type", "11001");
            }else if(type.equals("6"))
            {
                par.put("type", "12001");
            }else if(type.equals("7"))
            {
                par.put("type", "13001");
            }else if(type.equals("8"))
            {
                par.put("type", "14001");
            }

        }
        HttpConnect.post(BillDetailsActivity.this, "member_wallet_water_list", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray jsonArray = data.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (jsonObject2 != null) {
                                final String money = jsonObject2.getString("money");
                                final String after = jsonObject2.getString("after");
                                final String date = jsonObject2.getString("date");
                                final String type = jsonObject2.getString("type");
                                final String paycount = jsonObject2.getString("paycount");
                                Handler dataHandler = new Handler(getMainLooper()
                                ) {

                                    @Override
                                    public void handleMessage(final Message msg) {
                                        gifImageView.setVisibility(View.GONE);
                                        iv_isnull.setVisibility(View.GONE);
                                        BillDetail billDetails = new BillDetail(money, after, date, type, paycount);
                                        datas.add(billDetails);
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        }
                    }

                }else
                {
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
                        gifImageView.setVisibility(View.GONE);
                        adapter.setDatas(datas);
                        adapter.notifyDataSetChanged();
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        gifImageView.setVisibility(View.GONE);
                        if (datas.size()>0){
                            iv_isnull.setVisibility(View.GONE);
                        }else{
                            iv_isnull.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

    }

}
