package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
 * Created by Administrator on 2016/12/29.
 */

public class MyAccountInfoActivity extends MyBaseActivity{

    private ImageView ivBack,iv_question_mark;
    private TextView tvRMBCount,tv_bill,tv_consumption_count,tv_droiyan_count,tv_private_mining,tvRichCount,tvAαCount,tv_all_assets;
    private PopupWindow window = null;
    private View view;
    private FrameLayout tv_into_account_desc;
    private LinearLayout linearLayout_into_tixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_account_layout);

        initData();
        setUpView();
        loadAccountInfo();

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

        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("复投");
        ((TextView)view.findViewById(R.id.tv_prompt)).setText("我要复投");
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        ivBack = (ImageView) this.findViewById(R.id.iv_back2);
        iv_question_mark = (ImageView) this.findViewById(R.id.iv_question_mark);
        tvRMBCount = (TextView) this.findViewById(R.id.tv_rmb_count);
        tv_into_account_desc = (FrameLayout) this.findViewById(R.id.tv_into_account_desc);
        tv_bill = (TextView) this.findViewById(R.id.tv_bill);
        tvRichCount = (TextView) this.findViewById(R.id.tv_rmbrich_count);
        tvAαCount = (TextView) this.findViewById(R.id.tv_α_count);
//        tv_α_assets_count = (TextView) this.findViewById(R.id.tv_α_assets_count);  //投资账户
        tv_consumption_count = (TextView) this.findViewById(R.id.tv_consumption_count);
        tv_droiyan_count = (TextView) this.findViewById(R.id.tv_droiyan_count);
        tv_private_mining = (TextView) this.findViewById(R.id.tv_private_mining);
        tv_all_assets = (TextView) this.findViewById(R.id.tv_all_assets);

        linearLayout_into_tixian = (LinearLayout) this.findViewById(R.id.linearLayout_into_tixian);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpView() {

        final RotateAnimation rotateAnimation =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(2000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);


        tv_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountInfoActivity.this,BillDetailsActivity.class));
            }
        });

        tv_into_account_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountInfoActivity.this,AccountShuoMingActivity.class));
            }
        });

        linearLayout_into_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountInfoActivity.this,TiXianActivity.class));
            }
        });
    }

    private void loadAccountInfo()
    {
        HttpConnect.post(this, "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String available = data.getJSONArray("data").getJSONObject(0).getString("ctc");//α资产账户
//                    final String rich = data.getJSONArray("data").getJSONObject(0).getString("ctcrecat");    //复投账户
                    final String money = data.getJSONArray("data").getJSONObject(0).getString("money");  //现金账户
//                    final String field = data.getJSONArray("data").getJSONObject(0).getString("field") ;  //投资账户
                    final String ctcoption = data.getJSONArray("data").getJSONObject(0).getString("ctcoption");  //决战账户
                    final String pool = data.getJSONArray("data").getJSONObject(0).getString("pool");  //私有矿池
                    final String ctccous = data.getJSONArray("data").getJSONObject(0).getString("ctccous");  //消费账户
                    final String allmoney = data.getJSONArray("data").getJSONObject(0).getString("allmoney");  //总资产
//                    final String ctcpro = data.getJSONArray("data").getJSONObject(0).getString("fieldctcpro");  //投资百分比
//                    final String richpro = data.getJSONArray("data").getJSONObject(0).getString("ctcrecatpro");   //复投百分比
//                    final String moneypro = data.getJSONArray("data").getJSONObject(0).getString("moneypro");  //现金百分比
//                    final String avaictcpro = data.getJSONArray("data").getJSONObject(0).getString("ctcpro"); //α资产账户百分比
//                    final String ctccouspro = data.getJSONArray("data").getJSONObject(0).getString("ctccouspro"); //消费账户百分比
//                    final String ctcoptionpro = data.getJSONArray("data").getJSONObject(0).getString("ctcoptionpro"); //决战账户百分比
//                    final String poolpro = data.getJSONArray("data").getJSONObject(0).getString("poolpro"); //决战账户百分比

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            if(!TextUtils.isEmpty(available))
                            {
                                tvAαCount.setText(available+" α");
                            }else{
                                tvAαCount.setTextColor(0xff000000);
                                tvAαCount.setText("0 α");
                            }

                            if(!TextUtils.isEmpty(allmoney))
                            {
                                tv_all_assets.setText("¥ "+allmoney);
                            }else{
                                tv_all_assets.setText("¥ 0");
                            }

                            if(!TextUtils.isEmpty(pool))
                            {
                                tv_private_mining.setText(pool+" α");
                            }else{
                                tv_private_mining.setTextColor(0xffff0001);
                                tv_private_mining.setText("0α");
                            }

                            //消费
                            if(!TextUtils.isEmpty(ctccous))
                            {
                                tv_consumption_count.setText(ctccous+" 个");
                            }else{
                                tv_consumption_count.setText("0个");
                            }
                            //决战
                            if(!TextUtils.isEmpty(ctcoption))
                            {
                                tv_droiyan_count.setText(ctcoption+" 个");
                            }else{
                                tv_droiyan_count.setText("0个");
                            }

                            if(!TextUtils.isEmpty(money)) //TODO
                            {
                                tvRMBCount.setText("¥ "+money);
                            }else{
                                tvRMBCount.setText("0 RMB");
                            }

//                            if(!TextUtils.isEmpty(rich))
//                            {
//                                tvRichCount.setText(rich+" α");
//                            }else{
//                                tvRichCount.setText("0 RMB");
//                            }
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

                            sToast(msg8);
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

//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAccountInfo();
    }

    /**
     * 百分比可能会有小数 此方法去除小数
     * @param str
     * @return
     */
    public String  removeDecimalPoint(String str)
    {
        if(str.contains("."))
        {
            int count = str.indexOf(".");
            return str.substring(0,count);
        }
        return str;
    }



    /**
     * 显示取消支付的提示框
     */
    public void show()
    {

        ((TextView)view.findViewById(R.id.tv_prompt)).setText("我要复投");
        ((TextView)view.findViewById(R.id.tv_prompt)).setTextSize(18f);
        Button btnCencel1 = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit1 = (Button) view.findViewById(R.id.btn_commit);

        btnCencel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(false);
                    }
                });

                HttpConnect.post(MyAccountInfoActivity.this, "member_exchange_pool", null, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sToast("兑换成功");
                                    loadAccountInfo();
                                    window.dismiss();
                                    v.setClickable(true);
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sToast(data.optString("msg"));
                                    window.dismiss();
                                    v.setClickable(true);
                                }
                            });
                        }

                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                window.dismiss();
                                v.setClickable(true);
                            }
                        });
                    }
                });

            }
        });

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 在底部显示
            window.showAtLocation(tv_bill,
                    Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 显示取消支付的提示框
     */
    public void showTransferAccountsWin(String value)
    {
        ((TextView)view.findViewById(R.id.tv_prompt)).setText("您还需要将收益账户中的"+value+"个α转至复投账户中才能进行复投");
        ((TextView)view.findViewById(R.id.tv_prompt)).setTextSize(14f);
        Button btnCencel1 = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit1 = (Button) view.findViewById(R.id.btn_commit);

        btnCencel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountInfoActivity.this,TransferAccountsActivity.class));;

                window.dismiss();
            }
        });


        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 在底部显示
            window.showAtLocation(tv_bill,
                    Gravity.BOTTOM, 0, 0);
        }
    }


    public void transferAccount(View view){
        startActivity(new Intent(this,TransferAccountsActivity.class));
    }

    public void myFuTou(final View view){

        view.setClickable(false);
        HttpConnect.post(MyAccountInfoActivity.this, "member_can_disable_pool", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = data.optJSONArray("data");
                            if(jsonArray!=null)
                            {
                                JSONObject jsonObj = jsonArray.optJSONObject(0);
                                if(jsonObj.optString("value")!=null)
                                {
                                    if(jsonObj.optString("value").equals("0"))
                                    {
                                        show();
                                    }else
                                    {
                                        showTransferAccountsWin(jsonObj.optString("value"));
                                    }
                                }
                            }
                            view.setClickable(true);
                        }
                    });

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        sToast("请检查您的网络链接状态");
                        view.setEnabled(true);
                    }
                });
            }
        });
    }
}
