package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;

/**
 * Created by Administrator on 2016/12/26.
 */

public class AboutUsActivity extends MyBackBaseActivity {

    private ImageView ivBack;
    private TextView tv_verson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_layout);

        initData();
    }

    @Override
    public void initData() {
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        tv_verson = (TextView) this.findViewById(R.id.tv_verson);

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

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获取服务器当前版本
     */
    public void loadServiceVersion()
    {

    }



}
