package com.tohier.cartercoin.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.BillAdapter;
import com.tohier.cartercoin.bean.BillData;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BillListActivity extends MyBackBaseActivity {

    private ImageView iv_back2;
    private ListView lvBill;
    private BillAdapter billAdapter;
    private List<BillData> datas = new ArrayList<BillData>();
    private LoadingView gif_loading;
    private NoLinkView ivNoLink;
    private NoDataView iv_isnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list_layout);

        initData();
        setUpView();
        if (Tools.getAPNType(BillListActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            loadBillList();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        gif_loading = (LoadingView) this.findViewById(R.id.gif_loading);
        iv_isnull = (NoDataView) this.findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
        lvBill = (ListView) this.findViewById(R.id.lv_bill);

        billAdapter = new BillAdapter(this,datas);
        lvBill.setAdapter(billAdapter);
        lvBill.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setUpView() {
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(BillListActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    loadBillList();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void loadBillList()
    {
        gif_loading.setVisibility(View.VISIBLE);
        HttpConnect.post(this, "member_pay_collection_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            JSONArray array = data
                                    .optJSONArray("data");
                            if (array.size() != 0) {

                                for(int i = 0 ; i < array.size() ; i ++)
                                {
                                    final String name = array.getJSONObject(i).getString("nickname");
                                    final String createdate = array.getJSONObject(i).getString("createdate");
                                    final String pic = array.getJSONObject(i).getString("pic");
                                    final String qty = array.getJSONObject(i).getString("qty");
                                    BillData billData = new BillData(name, pic, qty, createdate);
                                    billData.setMoneyType(array.getJSONObject(i).getString("payment"));
                                    datas.add(billData);
                                }
                                billAdapter.setDatas(datas);
                                billAdapter.notifyDataSetChanged();
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            gif_loading.setVisibility(View.GONE);
                            if (datas.size()>0){
                                iv_isnull.setVisibility(View.GONE);
                            }else{
                                iv_isnull.setVisibility(View.VISIBLE);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        gif_loading.setVisibility(View.GONE);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


}
