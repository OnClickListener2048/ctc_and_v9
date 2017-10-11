package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.MyApplication;

/**
 * Created by Administrator on 2017/3/31.
 */

public class PresentationModeActivity extends MyBackBaseActivity {

    private ImageView ivBack;
    private LinearLayout linearLayoutIntoAlipayPresentation;
    private LinearLayout linearLayoutIntoBankCardPresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_presentation_mode_layout);
        MyApplication.maps.put("PresentationModeActivity",this);

        initData();

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

    @Override
    public void initData() {
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        linearLayoutIntoBankCardPresentation = (LinearLayout) this.findViewById(R.id.linearLayout_into_bankcard_tixian);
        linearLayoutIntoAlipayPresentation = (LinearLayout) this.findViewById(R.id.linearLayout_into_alipay_tixian);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        linearLayoutIntoAlipayPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(PresentationModeActivity.this,AlipayListActivity.class));
            }
        });

        linearLayoutIntoBankCardPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresentationModeActivity.this,BankListActivity.class));
            }
        });
    }
}
