package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2017/6/20.
 */

public class CurrentCurrencyDetailActivity extends MyBackBaseActivity {

    private TextView tv_shouyi_account,tv_private_mining_account,tv_available_shouyi_account,tv_locking_private_minning;
    private ImageView iv_back2;
    private LinearLayout linearLayout_into_aerfa_flowing_water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_currency_detail);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        super.initData();

        tv_shouyi_account = (TextView) this.findViewById(R.id.tv_shouyi_account);
        tv_private_mining_account = (TextView) this.findViewById(R.id.tv_private_mining_account);
        tv_available_shouyi_account = (TextView) this.findViewById(R.id.tv_available_shouyi_account);
        tv_locking_private_minning = (TextView) this.findViewById(R.id.tv_locking_private_minning);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        linearLayout_into_aerfa_flowing_water = (LinearLayout) this.findViewById(R.id.linearLayout_into_aerfa_flowing_water);

        String privateMinning = getIntent().getStringExtra("privateMinning");
        String available = getIntent().getStringExtra("shouyi");

        if(!TextUtils.isEmpty(privateMinning))
        {
            tv_private_mining_account.setText(privateMinning);
            tv_locking_private_minning.setText("锁定:"+privateMinning);
        }

        if(!TextUtils.isEmpty(available))
        {
            tv_shouyi_account.setText(available);
            tv_available_shouyi_account.setText("可用:"+available);
        }
    }


    private void setUpView() {
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayout_into_aerfa_flowing_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillDetailsActivity.type = "2";
                startActivity(new Intent(CurrentCurrencyDetailActivity.this,BillDetailsActivity.class));
            }
        });
    }




}
