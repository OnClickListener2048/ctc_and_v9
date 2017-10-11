package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.presenter.ConfigPresenter;
import com.tohier.cartercoin.ui.ConfigView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import static com.tohier.cartercoin.config.LoginUser.getInstantiation;

public class ShareOptionConfigActivity extends MyBackBaseActivity implements ConfigView,View.OnClickListener {

    private TextView tvRmbCount;
    private TextView tvCtcCount;
    private TextView tvCtcCount1;
    private TextView tvDayDeal;
    private TextView tvDayProfit;
    private TextView tvTotalDeal;
    private TextView tvTotalProfit;
    private TextView tvRecharge;
    private TextView tvRecord;
    private TextView tvCount;
    private TextView tvOptionTotalProfit;
    private RelativeLayout llInvitation;
    private RelativeLayout llZhiBo;
    private RelativeLayout llRanking;
    private RelativeLayout llMarket;
    private RelativeLayout llSchool;
    private TextView tvDummy;

    private ConfigPresenter presenter;
    private Intent intent;
    private String type;
//    private String tribeid,masterid;
//    private YWTribe mTribe;
//    IYWTribeService tribeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_option_config);
        init();
        setUp();

    }

    private void setUp() {
        tvRecharge.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        llInvitation.setOnClickListener(this);
        llZhiBo.setOnClickListener(this);
        llRanking.setOnClickListener(this);
        llMarket.setOnClickListener(this);
        llSchool.setOnClickListener(this);
        tvDummy.setOnClickListener(this);
    }

    private void init() {

//        tribeService = mIMKit.getTribeService();


        tvRmbCount = (TextView) findViewById(R.id.tv_rmb_count);
        tvCtcCount = (TextView) findViewById(R.id.tv_ctc_count);
        tvCtcCount1 = (TextView) findViewById(R.id.tv_ctc_count1);
        tvDayDeal = (TextView) findViewById(R.id.tv_day_deal);
        tvDayProfit = (TextView) findViewById(R.id.tv_day_profit);
        tvTotalDeal = (TextView) findViewById(R.id.tv_total_deal);
        tvTotalProfit = (TextView) findViewById(R.id.tv_total_profit);
        tvRecharge = (TextView) findViewById(R.id.tv_recharge);
        tvRecord = (TextView) findViewById(R.id.tv_deal_record);
        llInvitation = (RelativeLayout) findViewById(R.id.ll_invitation);
        llZhiBo = (RelativeLayout) findViewById(R.id.ll_zhibo);
        llRanking = (RelativeLayout) findViewById(R.id.ll_ranking);
        llMarket = (RelativeLayout) findViewById(R.id.ll_market);
        llSchool = (RelativeLayout) findViewById(R.id.ll_school);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvDummy = (TextView) findViewById(R.id.tv_dummy);
        tvOptionTotalProfit = (TextView) findViewById(R.id.tv_option_total_profit);

        intent = getIntent();
        type = intent.getStringExtra("type");
//        tribeid = intent.getStringExtra("tribeid");
//        masterid = intent.getStringExtra("masterid");
//
//        /**
//         * 将群消息设置为接受不提醒
//         */
//        mTribe = tribeService.getTribe(Long.parseLong(tribeid));
//        tribeService.receiveNotAlertTribeMsg(mTribe, null);

        if (type.equals("0")){
            tvDummy.setText("切换模拟盘");
        }else{
            tvDummy.setText("切换实盘");
        }
        getCTC(type);

        presenter = new ConfigPresenter(this,this);
        presenter.getDealInfo(type);


//       tribeService.getMembersFromServer(new IWxCallback() {
//            @Override
//            public void onSuccess(Object... result) {
//
//                final List<YWTribeMember> list = (List<YWTribeMember>) result[0];
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvCount.setText("在线"+list.size()+"人");
//
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onError(int code, String info) {
//                // TODO Auto-generated method stub
//
//            }
//        }, Long.parseLong(tribeid));
//
    }

    @Override
    public void initData() {

    }


    public void back(View view){
//        if (!LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode().equals(masterid)){
//            tribeService.exitFromTribe(new IWxCallback() {
//                @Override
//                public void onSuccess(Object... objects) {
//                    finish();
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Toast.makeText(ShareOptionConfigActivity.this, i+" "+s, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onProgress(int i) {
//
//                }
//            }, Long.parseLong(tribeid));
//        }
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dummy:

                if (tvDummy.getText().toString().equals("切换模拟盘")){

                    HttpConnect.post(this, "member_reality_cash_ctc_in", null, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvDummy.setText("切换实盘");
                                        type = "1";
                                        getCTC(type);
                                        presenter.getDealInfo(type);
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
                    getCTC(type);
                    presenter.getDealInfo(type);

                }

                break;
            case R.id.tv_recharge:
                startActivity(new Intent(this,RechargeRMBActivity.class).putExtra("type","0"));
                break;
            case R.id.tv_deal_record:
                startActivity(new Intent(this,MoreDealRecordActivity.class).putExtra("type",type));
                break;
            case R.id.ll_invitation:
                startActivity(new Intent(this,NewShareActivity.class));
                break;
//            case R.id.ll_zhibo:
//                Intent intent = mIMKit.getTribeChattingActivityIntent(Long.parseLong(tribeid));
//                startActivity(intent);
//
//                break;
            case R.id.ll_ranking:
                startActivity(new Intent(this,ShareOptionRankingActivity.class));
                break;
            case R.id.ll_market:
                startActivity(new Intent(this,GongGaoActivity.class));
                break;
            case R.id.ll_school:
                startActivity(new Intent(this,NewSchoolActivity.class));
                break;

        }
    }

    @Override
    public void fail(String msg) {
//        sToast(msg);
    }

    @Override
    public void success(HashMap<String, String> ininfo) {
        tvDayDeal.setText("今日交易: "+ininfo.get("count")+"笔");
        tvTotalDeal.setText("累计交易: "+ininfo.get("allcount")+"笔");
        tvDayProfit.setText("今日盈利: "+ininfo.get("profit"));
        tvTotalProfit.setText("累计流水: "+ininfo.get("accumulated"));
        tvOptionTotalProfit.setText("决战总流水: "+ininfo.get("ctccount"));
    }



    public void getCTC(String type) {
        String path = "";
        String id = "";
        try{
            id = getInstantiation(getApplicationContext()).getLoginUser().getUserId();
        }catch(Exception e){
            id = "0";
        }

        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        if (type.equals("0")){
            path = "member_count_detial";
        }else{
            path = "member_wallet_total_simulation";
        }

        HttpConnect.post(this, path, map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    double lock = 0;
                    if(data.optJSONArray("data").optJSONObject(0).optString("lock").equals("")){
                        lock = 0;
                    }else{
                        lock = Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("lock"));
                    }
                    double ctc = Double.parseDouble(data.optJSONArray("data").optJSONObject(0).optString("ctc"));
                    double c = ctc-lock;
                    if (c<=0){
                        c = 0;
                    }

                    final double finalC = c;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCtcCount.setText(new DecimalFormat("#0.0000").format(Double.parseDouble(finalC +""))+"   α");
                            tvRmbCount.setText(data.optJSONArray("data").optJSONObject(0).optString("money")+"   RMB");
                            tvCtcCount1.setText(data.optJSONArray("data").optJSONObject(0).optString("ctcoption")+"   ");
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCtcCount.setText(new DecimalFormat("#0.0000").format(Double.parseDouble(0.0000 +""))+"   ");
                            tvRmbCount.setText("0.0000   α");
                            tvRmbCount.setText("0.00   RMB");
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

}
