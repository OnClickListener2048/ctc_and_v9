package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.DealRecord;

public class DealDetialsActivity extends MyBackBaseActivity {

    private TextView  tvPrice,tvProfit,tvOrderNum,tvMoney,tvRise,tvCycle,tvOrderTime,tvExpireTime,tvOrderPrice,tvExpirePrice,tvResult;
    private Intent intent;
    private DealRecord dealRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detials);

        init();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    private void init() {
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvProfit = (TextView) findViewById(R.id.tv_profit);
        tvOrderNum = (TextView) findViewById(R.id.tv_ordernum);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvRise = (TextView) findViewById(R.id.tv_rise);
        tvCycle = (TextView) findViewById(R.id.tv_cycle);
        tvOrderTime = (TextView) findViewById(R.id.tv_ordertime);
        tvExpireTime = (TextView) findViewById(R.id.tv_expiretime);
        tvOrderPrice = (TextView) findViewById(R.id.tv_orderprice);
        tvExpirePrice = (TextView) findViewById(R.id.tv_expireprice);
        tvResult = (TextView) findViewById(R.id.tv_result);

        intent = getIntent();

        dealRecord=(DealRecord)intent.getSerializableExtra("DealRecord");

        tvPrice.setText(dealRecord.getMoney());
        tvProfit.setText(dealRecord.getProfit());
        tvOrderNum.setText(dealRecord.getOrderNum());
        tvMoney.setText(dealRecord.getType());
        tvRise.setText(dealRecord.getState());
        tvCycle.setText(dealRecord.getCycle());
        tvOrderTime.setText(dealRecord.getOrderTime());
        tvExpireTime.setText(dealRecord.getExpireTime());
        tvOrderPrice.setText(dealRecord.getOrderPrice());
        tvExpirePrice.setText(dealRecord.getExpirePrice());
        tvResult.setText(dealRecord.getResult());



    }

    @Override
    public void initData() {

    }

    public void back(View view){
        finish();
    }
}
