package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.NoviceAdapter;
import com.tohier.cartercoin.bean.Novice;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;


public class NoviceActivity extends MyBackBaseActivity {

    private ListView lv_novice;
    private ProgressBar progressBar_mining_comprehensive_value;
    private TextView tv_mining_comprehensive_value;
    private ArrayList<Novice> list = new ArrayList<Novice>();
    private NoviceAdapter adapter;
    private String[] textStrings;

    private int iCount = 0; //未完成数



    private  int PERCENT_TOTAL = 0;

    int percentFluency  = 0;

    private LoadingView gifLoading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;


    final Runnable mUpdateProgressFluency = new Runnable() {
        final int everyTimeAddF = 4;
        final int delay = 1000 / (100 / everyTimeAddF);

        @Override
        public void run()
        {
            percentFluency += everyTimeAddF;

            if (percentFluency > PERCENT_TOTAL)
            {
                mHandler.removeCallbacks(mUpdateProgressFluency);
            }
            else
            {
                Message msg = mHandler.obtainMessage();
                msg.arg1 = percentFluency;
                msg.what = What.FLUENCY;
                mHandler.sendMessageDelayed(msg, 0);
            }
        }
    };

    static class What
    {
        final static int    FLUENCY = 1;
    }

    final Handler mHandler  = new Handler() {

        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case What.FLUENCY:
                    progressBar_mining_comprehensive_value.setProgress(msg.arg1);
                    mHandler.post(mUpdateProgressFluency);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice);
        init();

        setUp();
    }

    private void setUp() {
        lv_novice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(list.get(position).getState().equals("0")){
                    if (position == 0){
                        MainActivity.mTabHost.setCurrentTab(1);
                        finish();
                    }else if (position == 1){
                        startActivity(new Intent(NoviceActivity.this, AddContactActivity.class));
                    }else if (position == 2){
                        startActivity(new Intent(NoviceActivity.this, NewShareActivity.class));
                    }else if (position == 3){
                        startActivity(new Intent(NoviceActivity.this,RevisionBuyAssetsActivity .class));
                    }else if (position == 4){
                        Bundle bundle=new Bundle();
                        bundle.putStringArray("textStrings", textStrings);
                        startActivity(new Intent(NoviceActivity.this, VipUpgradeActivity.class).putExtras(bundle));
                    }else if (position == 5){
                        startActivity(new Intent(NoviceActivity.this, NewTransactionActivity.class));
                    }
                }


            }
        });

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(NoviceActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    getDatas();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void init() {
        lv_novice = (ListView) findViewById(R.id.lv_novice);
        progressBar_mining_comprehensive_value = (ProgressBar) findViewById(R.id.progressBar_mining_comprehensive_value);
        tv_mining_comprehensive_value = (TextView) findViewById(R.id.tv_mining_comprehensive_value);

        gifLoading = (LoadingView) findViewById(R.id.gif_loading);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        adapter = new NoviceAdapter(this,list);
        lv_novice.setAdapter(adapter);

//        getDatas();

        /**
         * vip升级提示
         */
        HttpConnect.post(this, "member_buy_upgrade_count", null, new Callback() {
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
        finish();
    }

    private void getDatas(){
        gifLoading.setVisibility(View.VISIBLE);
        list.clear();
        iCount = 0;
        HttpConnect.post(this, "member_task_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    Novice novice = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        JSONObject obj = array.optJSONObject(i);
                        String title = obj.optString("name");
                        String desc = obj.optString("introduce");
                        String strength = obj.optString("strength");
                        String state = obj.optString("status");
                        if (state.equals("0")){
                            iCount++;
                        }

                        novice = new Novice(title,desc,strength,state);
                        list.add(novice);
                    }

                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

//                        adapter.setDatas(list);
//                        adapter.notifyDataSetChanged();
//                        BigDecimal b1   =   new BigDecimal(Double.toString(list.size()-iCount));
//                        BigDecimal b2   =   new   BigDecimal(Double.toString(list.size()));
//                        PERCENT_TOTAL = (int) (b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
//                        mHandler.post(mUpdateProgressFluency);
//                        tv_mining_comprehensive_value.setText(PERCENT_TOTAL+"%");
                        ArrayList<Novice> list1 = new ArrayList<Novice>();
                        list1.addAll(list);
                        adapter.setDatas(list1);
                        adapter.notifyDataSetChanged();
                        BigDecimal b1   =   new BigDecimal(Double.toString(list1.size()-iCount));
                        BigDecimal b2   =   new   BigDecimal(Double.toString(list1.size()));
                        PERCENT_TOTAL = (int) (b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
                        mHandler.post(mUpdateProgressFluency);
                        tv_mining_comprehensive_value.setText(PERCENT_TOTAL+"%");
                        gifLoading.setVisibility(View.GONE);
                        if (list1.size()>0){
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
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Tools.getAPNType(NoviceActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            getDatas();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }
    }
}
