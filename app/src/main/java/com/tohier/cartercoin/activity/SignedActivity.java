package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.signed.ResolutionUtil;
import com.tohier.cartercoin.signed.SignAdapter;
import com.tohier.cartercoin.signed.SignDialogFragment;
import com.tohier.cartercoin.signed.SignEntity;
import com.tohier.cartercoin.signed.SignView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignedActivity extends MyBackBaseFragmentActivity {
    private TextView tvSignDay;
    private TextView tvScore;
    private TextView tvYear;
    private TextView tvMonth;
    private SignView signView;
    private Button btnSign;
    private List<SignEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);

        initView();

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

    private void initView() {
        tvSignDay = (TextView) findViewById(R.id.activity_main_tv_main_day);
        tvScore = (TextView) findViewById(R.id.activity_main_tv_score);
        tvYear = (TextView) findViewById(R.id.activity_main_tv_year);
        tvMonth = (TextView) findViewById(R.id.activity_main_tv_month);
        signView = (SignView) findViewById(R.id.activity_main_cv);
        btnSign = (Button) findViewById(R.id.activity_main_btn_sign);
        if (signView != null) {
            signView.setOnTodayClickListener(onTodayClickListener);

        }
        if (btnSign != null) {
            //noinspection deprecation
            btnSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSign();
                }
            });
        }


        loadSignedInfo();


        //---------------------------------分辨率适配----------------------------------
        ResolutionUtil resolutionUtil = ResolutionUtil.getInstance();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvSignDay.setLayoutParams(layoutParams);
        tvSignDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(42));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvScore.setLayoutParams(layoutParams);
        tvScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(95));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(130));
        layoutParams.topMargin = resolutionUtil.formatVertical(54);
        View llDate = findViewById(R.id.activity_main_ll_date);
        if (llDate != null) {
            llDate.setLayoutParams(layoutParams);
        }

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(43);
        tvYear.setLayoutParams(layoutParams);
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(44);
        tvMonth.setLayoutParams(layoutParams);
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));

        /**
         * 距离太小  可能导致显示不全
         *
         */
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(980));
        signView.setLayoutParams(layoutParams);

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(142));
        layoutParams.topMargin = resolutionUtil.formatVertical(111);
        layoutParams.leftMargin = layoutParams.rightMargin = resolutionUtil.formatHorizontal(42);
//        if (btnSign != null) {
//            btnSign.setLayoutParams(layoutParams);
//            btnSign.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(54));
//        }


    }

    /**
     * 签到
     */
    private void onSign() {

        HttpConnect.post(this, "member_sign_everyday", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String value = data.optJSONArray("data").optJSONObject(0).optString("Value");

                            if (value.equals("")){
                                value = "0";
                            }

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            SignDialogFragment signDialogFragment = SignDialogFragment.newInstance(Integer.parseInt(value));
                            signDialogFragment.setOnConfirmListener(onConfirmListener);
                            fragmentManager.beginTransaction().add(signDialogFragment, SignDialogFragment.TAG).commitAllowingStateLoss();


//                            try{
//                                signDialogFragment.show(fragmentManager, SignDialogFragment.TAG);
//                            }catch (Exception e){
//                                sToast("签到失败，请稍候重试！");
//                            }

                            loadSignedInfo();
                        }
                    });
                }else{
                 sToast(data.getString("msg"));
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

    }


    private void signToday() {
        btnSign.setEnabled(false);
        btnSign.setText(R.string.have_signed);
        btnSign.setBackgroundResource(R.drawable.bg_shape_c4c4c4);
        loadSignedInfo();
    }

    private SignView.OnTodayClickListener onTodayClickListener = new SignView.OnTodayClickListener() {
        @Override
        public void onTodayClick() {
            onSign();
        }
    };



    private SignDialogFragment.OnConfirmListener onConfirmListener = new SignDialogFragment.OnConfirmListener() {
        @Override
        public void onConfirm() {
            signToday();
        }
    };





    private void loadSignedInfo(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        Calendar calendarToday = Calendar.getInstance();
        final int dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);

        /**
         * 累计签到天数
         */
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvMonth.setText(getResources().getStringArray(R.array.month_array)[month]);

        HttpConnect.post(this, "member_sign_alldays_by_month", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String cout = data.optJSONArray("data").optJSONObject(0).optString("count");
                            String strength  = data.optJSONArray("data").optJSONObject(0).optString("strength");
                            tvSignDay.setText(Html.fromHtml(String.format(getString(R.string.you_have_sign), "#999999", "#f26051", Integer.parseInt(cout))));
                            tvScore.setText(String.valueOf(strength));

                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        /**
         * 签到列表
         */
        HttpConnect.post(this, "member_sign_month_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data1 = JSONObject.fromObject(arg0.body().string());
                if (data1.get("status").equals("success")) {

                    JSONArray array = data1.optJSONArray("data");
                    data = new ArrayList<>();
                    SignEntity signEntity = null;
                    for (int i = 0 ; i < array.size() ; i++ ){
                        signEntity = new SignEntity();
                        JSONObject obj = array.optJSONObject(i);
                        signEntity.setDayType(Integer.parseInt(obj.optString("sign")));
                        data.add(signEntity);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (data.size()>0){
                                SignAdapter signAdapter = new SignAdapter(data);
                                signView.setAdapter(signAdapter);

                                if (data.get(dayOfMonthToday-1).getDayType() == 0){
                                    btnSign.setEnabled(false);
                                    btnSign.setText(R.string.have_signed);
                                    btnSign.setBackgroundResource(R.drawable.bg_shape_c4c4c4);
                                }else{
                                    btnSign.setEnabled(true);
                                    btnSign.setText(R.string.sign);
                                    btnSign.setBackgroundResource(R.drawable.bg_shape_feb831);
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
        finish();
    }
}
