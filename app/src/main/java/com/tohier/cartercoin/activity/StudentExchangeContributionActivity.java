package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/18.
 */

public class StudentExchangeContributionActivity extends MyBackBaseActivity implements View.OnClickListener{

    private LinearLayout linearlayout_into_classmate_contribution, linearlayout_into_homologous_contribution, linearlayout_into_contribution_from_the_same_school,
            linearLayout_one_level,linearLayout_two_level,linearLayout_three_level;
    private TextView tv_classmate_count, tv_homologous_count, tv_the_same_school_count,tv_classmate_contribution_today,tv_classmate_allcontribution,tv_fellow_contribution_today,tv_fellow_allcontribution
                           ,tv_the_same_school_contribution_today,tv_the_same_school_allcontribution;
    private ImageView ivBack;

    private TextView tv_accelerated_recording;


    private TextView tv_classmate_active_today,tv_fellow_active_today,tv_the_same_school_active_today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentexchange_contribution_layout);

        initData();
        setUpView();
        getContributionData();

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
        tv_classmate_active_today = (TextView) this.findViewById(R.id.tv_classmate_active_today);
        tv_fellow_active_today = (TextView) this.findViewById(R.id.tv_fellow_active_today);
        tv_the_same_school_active_today = (TextView) this.findViewById(R.id.tv_the_same_school_active_today);

        linearlayout_into_classmate_contribution = (LinearLayout) this.findViewById(R.id.linearlayout_into_classmate_contribution);
        linearLayout_one_level = (LinearLayout) this.findViewById(R.id.linearLayout_one_level);
        linearLayout_two_level = (LinearLayout) this.findViewById(R.id.linearLayout_two_level);
        linearLayout_three_level = (LinearLayout) this.findViewById(R.id.linearLayout_three_level);

//        String type = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType();
//        if(!TextUtils.isEmpty(type))
//        {
//               if(type.equals("10"))
//               {
//                   linearLayout_two_level.setVisibility(View.GONE);
//                   linearLayout_three_level.setVisibility(View.GONE);
//               }else if(type.equals("20"))
//               {
//                   linearLayout_three_level.setVisibility(View.GONE);
//               }
//        }

        linearlayout_into_homologous_contribution = (LinearLayout) this.findViewById(R.id.linearlayout_into_homologous_contribution);
        linearlayout_into_contribution_from_the_same_school = (LinearLayout) this.findViewById(R.id.linearlayout_into_contribution_from_the_same_school);
        tv_classmate_count = (TextView) this.findViewById(R.id.tv_classmate_count);
        tv_homologous_count = (TextView) this.findViewById(R.id.tv_homologous_count);
        tv_the_same_school_count = (TextView) this.findViewById(R.id.tv_the_same_school_count);

        tv_classmate_contribution_today = (TextView) this.findViewById(R.id.tv_classmate_contribution_today);
        tv_classmate_allcontribution = (TextView) this.findViewById(R.id.tv_classmate_allcontribution);
        tv_fellow_contribution_today = (TextView) this.findViewById(R.id.tv_fellow_contribution_today);
        tv_fellow_allcontribution = (TextView) this.findViewById(R.id.tv_fellow_allcontribution);
        tv_the_same_school_contribution_today = (TextView) this.findViewById(R.id.tv_the_same_school_contribution_today);
        tv_the_same_school_allcontribution = (TextView) this.findViewById(R.id.tv_the_same_school_allcontribution);
        tv_accelerated_recording = (TextView) this.findViewById(R.id.tv_accelerated_recording);

        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
    }

    public void setUpView() {
        linearlayout_into_classmate_contribution.setOnClickListener(this);
        linearlayout_into_homologous_contribution.setOnClickListener(this);
        linearlayout_into_contribution_from_the_same_school.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tv_accelerated_recording.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.linearlayout_into_classmate_contribution:
                intent = new Intent(this,ContributionActivity.class);
                intent.putExtra("mark","同班");
                break;

            case R.id.linearlayout_into_homologous_contribution:
                intent = new Intent(this,ContributionActivity.class);
                intent.putExtra("mark","同系");
                break;

            case R.id.linearlayout_into_contribution_from_the_same_school:
                intent = new Intent(this,ContributionActivity.class);
                intent.putExtra("mark","同校");
                break;

            case R.id.tv_accelerated_recording:
                intent = new Intent(this,AcceleratedRecordingActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        } else {
            finish();
        }
    }


    public void getContributionData()
    {
        HttpConnect.post(this, "member_offline_earning", null, new Callback() {

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
                                    final String todaybouns = array.getJSONObject(i).getString("todaybouns");
                                    final String count = array.getJSONObject(i).getString("count");
                                    final String allbouns = array.getJSONObject(i).getString("allbouns");
                                    final String countcan = array.getJSONObject(i).getString("countcan");

                                    if(i==0)
                                    {
                                        tv_classmate_count.setText("①级宝粉"+count+"人");
                                        tv_classmate_contribution_today.setText(todaybouns);
                                        tv_classmate_allcontribution.setText(allbouns);
                                        tv_classmate_active_today.setText(countcan+"人");
                                    }

                                    if(i==1)
                                    {
                                        tv_homologous_count.setText("②级宝粉"+count+"人");
                                        tv_fellow_contribution_today.setText(todaybouns);
                                        tv_fellow_allcontribution.setText(allbouns);
                                        tv_fellow_active_today.setText(countcan+"人");
                                    }

                                    if(i==2)
                                    {
                                        tv_the_same_school_count.setText("③级宝粉"+count+"人");
                                        tv_the_same_school_contribution_today.setText(todaybouns);
                                        tv_the_same_school_allcontribution.setText(allbouns);
                                        tv_the_same_school_active_today.setText(countcan+"人");
                                    }
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

//                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public  void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

}
