package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.DealAdapter;
import com.tohier.cartercoin.bean.DealRecord;
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

public class MoreDealRecordActivity extends MyBackBaseActivity implements AbsListView.OnScrollListener{


    private ListView gridView;
    private ArrayList<DealRecord> datas = new ArrayList<DealRecord>();
    private DealAdapter adapter;
    private Spinner spinnerTime;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter1;
    private LoadingView gifLoading,loadingView;
    private NoDataView ivNodata;
    private NoLinkView noLinkView;
    private TextView tv_α,tv_rmb,tvDummy;
    private String type;

    private boolean isLastRow = false;
    private int page = 1;



    /**
     * 0---当天
     * 1---一周
     * 2---一月
     * 3---三月
     */
    private String time = "0";


    public ListView getGridView() {
        return gridView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_deal_record);

       init();



    }

    private void init() {
        list.add("当天");
        list.add("一周");
        list.add("一月");
        list.add("三月");

        spinnerTime = (Spinner) findViewById(R.id.spinner_time);
        gridView = (ListView) findViewById(R.id.content_view);
        gifLoading = (LoadingView) findViewById(R.id.cif_loading);
        ivNodata  = (NoDataView) findViewById(R.id.iv_nodata);
        tv_rmb = (TextView) findViewById(R.id.tv_rmb);
        tv_α = (TextView) findViewById(R.id.tv_α);
        tvDummy = (TextView) findViewById(R.id.tv_dummy);

        noLinkView = (NoLinkView) findViewById(R.id.iv_no_link);

        loadingView = new LoadingView(this);
        loadingView.loadMore();
        loadingView.setGravity(Gravity.CENTER);

        type = getIntent().getStringExtra("type");

        if (type.equals("0")){
            tvDummy.setText("切换模拟盘");
        }else{
            tvDummy.setText("切换实盘");
        }


        adapter = new DealAdapter(this,datas,0);
        gridView.setAdapter(adapter);

//        /**
//         * 解析数据
//         */

        if (Tools.getAPNType(MoreDealRecordActivity.this) == true){
            noLinkView.setVisibility(View.GONE);
            getJsonDate(1);
            getStream(type,time);
        }else{
            noLinkView.setVisibility(View.VISIBLE);
        }


        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinnerTime.setAdapter(adapter1);


        spinnerTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                gridView.removeFooterView(loadingView);
                if (Tools.getAPNType(MoreDealRecordActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    if (arg2==0){
                        time = "0";
                        page = 1;
                        getJsonDate(1);
                        getStream(type,time);
                    }else if(arg2==1) {
                        time = "1";
                        page = 1;
                        getJsonDate(1);
                        getStream(type,time);
                    }else if(arg2==2) {
                        time = "2";
                        page = 1;
                        getJsonDate(1);
                        getStream(type,time);
                    }else if(arg2==3) {
                        time = "3";
                        page = 1;
                        getJsonDate(1);
                        getStream(type,time);
                    }
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }


            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        noLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(MoreDealRecordActivity.this) == true){
                    noLinkView.setVisibility(View.GONE);
                    getJsonDate(1);
                    getStream(type,time);
                }else{
                    noLinkView.setVisibility(View.VISIBLE);
                }
            }
        });
        /**
         * 点击事件
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DealRecord dealRecord = datas.get(position);

                Intent intent = new Intent();
                intent.setClass(MoreDealRecordActivity.this, DealDetialsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DealRecord", dealRecord);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        tvDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDummy.getText().toString().equals("切换模拟盘")){

                    HttpConnect.post(MoreDealRecordActivity.this, "member_reality_cash_ctc_in", null, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            final JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvDummy.setText("切换实盘");
                                        type = "1";
                                        page = 1;
                                        getStream(type,time);
                                        getJsonDate(1);
                                    }
                                });
                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                        }
                    });


                }else{
                    tvDummy.setText("切换模拟盘");
                    type = "0";
                    page = 1;
                    getStream(type,time);
                    getJsonDate(1);
                }
            }
        });

        gridView.setOnScrollListener(this);
    }

    @Override
    public void initData() {

    }


    private void getStream(String reality,String date){
        Map<String, String> par = new HashMap<String, String>();
        par.put("reality",reality);
        par.put("date",date);
        HttpConnect.post(this, "member_fistory_options_all", par, new Callback() {

            @Override
            public void onResponse(final Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            JSONArray array = data.optJSONArray("data");

                            if (array ==null){
                                tv_α.setText("0");
                                tv_rmb.setText("0");
                            }else{
                                JSONObject obj = array.optJSONObject(0);
                                if (obj ==null){
                                    tv_α.setText("0");
                                    tv_rmb.setText("0");
                                }else{
                                    final String ctc = obj.optString("ctc");
                                    final String money = obj.optString("money");
                                    if (!TextUtils.isEmpty(ctc)){
                                        tv_α.setText(ctc);
                                    }else {
                                        tv_α.setText("0");
                                    }

                                    if (!TextUtils.isEmpty(money)){
                                        tv_rmb.setText(money);
                                    }else {
                                        tv_rmb.setText("0");
                                    }
                                }
                            }

                        }
                    });


                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    public void back(View view){
        datas.clear();
        finish();
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
            gifLoading.setVisibility(View.VISIBLE);

        }else{
            if (gridView.getFooterViewsCount()<=0){
                gridView.addFooterView(loadingView);
            }
            loadingView.setVisibility(View.VISIBLE);
        }
        Map<String, String> par = new HashMap<String, String>();
        par.put("reality",type);
        par.put("page",page+"");
        par.put("date",time);
        HttpConnect.post(this, "member_profit_selcet_by_date", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    DealRecord dealRecord = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        dealRecord = new DealRecord();
                        JSONObject obj = array.optJSONObject(i);
                        dealRecord.setMoney(obj.optString("qty"));
                        dealRecord.setProfit(obj.optString("profit"));
                        dealRecord.setOrderNum(obj.optString("code"));
                        dealRecord.setType(obj.optString("cointype"));
                        dealRecord.setState(obj.optString("rise"));
                        dealRecord.setCycle(obj.optString("type"));
                        dealRecord.setOrderTime(obj.optString("createdate"));
                        dealRecord.setOrderPrice(obj.optString("beginprice"));
                        dealRecord.setExpireTime(obj.optString("dealtime"));
                        dealRecord.setExpirePrice(obj.optString("endprice"));
                        String s = obj.optString("profitorloss");
                        dealRecord.setResult(obj.optString("profitorloss"));
                        dealRecord.setPayAccount(obj.getString("payaccont"));
                        datas.add(dealRecord);
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
                        adapter.setList(datas);
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

               runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        gifLoading.setVisibility(View.GONE);
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
