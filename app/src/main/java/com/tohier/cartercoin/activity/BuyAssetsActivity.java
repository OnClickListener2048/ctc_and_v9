package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.alipayconfig.AlipayUtils;
import com.tohier.cartercoin.alipayconfig.PayResult;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tohier.cartercoin.R.id.switch_alipay;
import static com.tohier.cartercoin.R.id.switch_price;
import static com.tohier.cartercoin.R.id.tv_diamond;
import static com.tohier.cartercoin.R.id.tv_gold;
import static com.tohier.cartercoin.R.id.tv_platinum;

public class BuyAssetsActivity extends MyBaseActivity implements View.OnClickListener {


    private static final int SDK_PAY_FLAG = 1;
    PopupWindow window = null;
    /***
     * 优惠券列表
     */
    private  ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private String id = "0";
    private LinearLayout llZhuce,llGold,llPlatinum,llDamond,llCrown,llCoupon,ll;
    private TextView tv,tvRisk,tvZhuce,tvGold,tvPlatinum,tvDamond,tvVipType,tvVipPrice,tvCouponPrice,tvPrice,tvTotalPrice,tvPay;
    private Button aSwitchPrice,aSwitchWechat,aSwitchAlipay;
    private TextView tvAssetsRecord;
    private View view;
    private FrameLayout tvVipExplain;
    private ImageView iv_question_mark;
    /**
     * 资产包类型
     */
    private String type = "100";
    /**
     * 是否使用余额支付
     */
    private boolean isHave = false;
    /**
     * 可用余额
     */
    private double price = 0;
    /**
     * 订单那总金额
     */
    private double orderPrice = 0;
    private boolean wxisSel = false;
    private boolean alipayisSel =false;
    private Drawable drawable1,drawable;
    private TextView tvVipActivity;
    private boolean isBuyexPerience = false;
    private AlipayUtils alipayUtils;
    private String pid;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(BuyAssetsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(BuyAssetsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(BuyAssetsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_assets);

        init();
        setUp();

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

    private void setUp() {
        aSwitchPrice.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvVipExplain.setOnClickListener(this);
        tvRisk.setOnClickListener(this);
        tvAssetsRecord.setOnClickListener(this);
        /**
         * 微信支付
         */
        aSwitchWechat.setOnClickListener(this);
        /**
         * 支付宝支付
         */
        aSwitchAlipay.setOnClickListener(this);

        llZhuce.setOnClickListener(this);
        llDamond.setOnClickListener(this);
        llGold.setOnClickListener(this);
        llPlatinum.setOnClickListener(this);
        ll.setOnClickListener(this);

    }

    private void init() {

        isBuyexPerience();
        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_prompt)).setText("购买矿产包");
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        llZhuce = (LinearLayout) findViewById(R.id.ll_zhuce);
        llGold = (LinearLayout) findViewById(R.id.ll_gold);
        llPlatinum = (LinearLayout) findViewById(R.id.ll_platinum);
        llDamond = (LinearLayout) findViewById(R.id.ll_diamond);
        llCrown = (LinearLayout) findViewById(R.id.ll_crown);
        llCoupon = (LinearLayout) findViewById(R.id.ll_coupon);
        ll = (LinearLayout) findViewById(R.id.ll);

        tv = (TextView) findViewById(R.id.tv);
        tvZhuce = (TextView) findViewById(R.id.tv_zhuce);
        tvGold = (TextView) findViewById(tv_gold);
        tvPlatinum = (TextView) findViewById(tv_platinum);
        tvDamond = (TextView) findViewById(tv_diamond);
        tvVipActivity = (TextView) findViewById(R.id.tv_vip_activity);
        tvVipExplain = (FrameLayout) findViewById(R.id.tv_vip_explain);
        iv_question_mark = (ImageView) findViewById(R.id.iv_question_mark);

        final RotateAnimation rotateAnimation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(2000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);

        tvRisk = (TextView) findViewById(R.id.tv_risk);
        tvVipPrice = (TextView) findViewById(R.id.tv_vip_price);
        tvCouponPrice = (TextView) findViewById(R.id.tv_coupon_price);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        aSwitchPrice = (Button) findViewById(switch_price);
        aSwitchWechat = (Button) findViewById(R.id.switch_wechat);
        aSwitchAlipay = (Button) findViewById(switch_alipay);
        tvAssetsRecord = (TextView) findViewById(R.id.tv_asstes_record);


        HttpConnect.post(this, "member_assets_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(" "+data.optJSONArray("data").optJSONObject(0).optString("name"));
                            tvZhuce.setText(" "+data.optJSONArray("data").optJSONObject(1).optString("name"));
                            tvGold.setText(" "+data.optJSONArray("data").optJSONObject(2).optString("name"));
                            tvPlatinum.setText(" "+data.optJSONArray("data").optJSONObject(3).optString("name"));
//                            tvDamond.setText(" "+data.optJSONArray("data").optJSONObject(3).optString("name"));
                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });


        alipayUtils =  new AlipayUtils();

        drawable = getResources().getDrawable(R.mipmap.dianed);
        drawable1 = getResources().getDrawable(R.mipmap.dian);
        assetsDetials("100");
//        getCoupon();
        getMoney();

    }


    /**
     * 显示取消支付的提示框
     */
    public void show()
    {

        Button btnCencel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) view.findViewById(R.id.btn_commit);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                tvPay.setClickable(true);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyAssets(type,"0",orderPrice+"",id);
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
            window.showAtLocation(tvAssetsRecord,
                    Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_asstes_record:
                startActivity(new Intent(this,BuyAssetsRecordActivity.class).putExtra("type","3"));
                break;
            case R.id.tv_vip_explain:
                startActivity(new Intent(this,AssetsExplainActivity.class));
                break;
            case R.id.tv_risk:
                startActivity(new Intent(this,RiskActivity.class));
                break;

            case R.id.ll:

                tvGold.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tv.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);

                tv.setTextColor(0xfffeb531);
                tvZhuce.setTextColor(0xff000000);
                tvGold.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xff000000);
                type = "100";
                assetsDetials(type);

                break;

            case R.id.ll_zhuce:
                tv.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvGold.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                    tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                    tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                    tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                    tv.setTextColor(0xff000000);
                    tvZhuce.setTextColor(0xfffeb531);
                    tvGold.setTextColor(0xff000000);
                    tvPlatinum.setTextColor(0xff000000);
                    tvDamond.setTextColor(0xff000000);
                    type = "200";
                    assetsDetials(type);

                break;
            case R.id.ll_gold:
                tv.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvGold.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvZhuce.setTextColor(0xff000000);
                tv.setTextColor(0xff000000);
                tvGold.setTextColor(0xfffeb531);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xff000000);

                type = "300";
                assetsDetials(type);
                break;

            case R.id.ll_platinum:
                tv.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvGold.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);

                tv.setTextColor(0xff000000);
                tvZhuce.setTextColor(0xff000000);
                tvGold.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xfffeb531);
                tvDamond.setTextColor(0xff000000);

                type = "400";
                assetsDetials(type);
                break;

            case R.id.ll_diamond:
                tvGold.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

                tvZhuce.setTextColor(0xff000000);
                tvGold.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xfffeb531);

                type = "400";
                assetsDetials(type);
                break;

            case R.id.ll_coupon:
                startActivityForResult(new Intent(this,CouponActivity.class).putExtra("type","5"),1);
                break;
            case switch_price:

                if(isHave)
                {
                    isHave = false;
                    aSwitchPrice.setBackgroundResource(R.drawable.switch_off);
                }else
                {
                    isHave = true;
                    aSwitchPrice.setBackgroundResource(R.drawable.switch_no);
                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_off);
                    aSwitchWechat.setBackgroundResource(R.drawable.switch_off);
                    alipayisSel = false;
                    wxisSel = false;


                }

                break;
            case switch_alipay:
                if(alipayisSel)
                {
                    alipayisSel = false;
                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_off);
                }else
                {
                    alipayisSel = true;
                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_no);
                    wxisSel = false;
                    aSwitchWechat.setBackgroundResource(R.drawable.switch_off);
                    isHave = false;
                    aSwitchPrice.setBackgroundResource(R.drawable.switch_off);
                }


                break;
            case R.id.switch_wechat:
                if(wxisSel)
                {
                    wxisSel = false;
                    aSwitchWechat.setBackgroundResource(R.drawable.switch_off);
                }else
                {
                    wxisSel = true;
                    aSwitchWechat.setBackgroundResource(R.drawable.switch_no);
                    alipayisSel = false;
                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_off);
                    isHave = false;
                    aSwitchPrice.setBackgroundResource(R.drawable.switch_off);
                }

                break;
            case R.id.tv_pay:
                orderPrice = Double.parseDouble(tvTotalPrice.getText().toString().substring(1,tvTotalPrice.getText().toString().length()));


                if(isHave==false&&wxisSel==false&&alipayisSel==false)
                   {
                       sToast("请选择支付方式");
                       return;
                   }


                tvPay.setClickable(false);



                if(isHave)
                {
                    if(wxisSel==false&&alipayisSel==false)
                    {
//
                        show();

                    }else if(wxisSel)
                    {
                        sToast("余额与微信混合支付");
                    }

                }else
                {
                    if(wxisSel)
                    {
                        final HashMap<String,String> map = new HashMap<String,String>();
                        map.put("type",type);
                        map.put("paymode","2");
                        map.put("totalpay",orderPrice+"");
                        map.put("conponsid",id);
                        HttpConnect.post(BuyAssetsActivity.this, "member_assets", map, new Callback() {
                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                String json = arg0.body().string();
                                JSONObject data = JSONObject.fromObject(json);
                                if (data.optString("status").equals("success")){
                                    pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
//                                       aliPay(pid,"3");

                                    //获取签名 并完成支付
                                    getWechatPaySign(pid,"3");
                                }else{
                                    if (!"".equals(data.getString("msg"))){
                                        sToast(data.getString("msg"));
                                    };
                                    tvPay.setClickable(true);
                                }

                            }
                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

                                if (!"".equals(arg0.toString())){
                                    sToast(arg0.toString());
                                }
                                tvPay.setClickable(true);
                            }
                        });
                    }else if(alipayisSel){
                        final HashMap<String,String> map = new HashMap<String,String>();
                        map.put("type",type);
                        map.put("paymode","1");
                        map.put("totalpay",orderPrice+"");
                        map.put("conponsid",id);
                        HttpConnect.post(BuyAssetsActivity.this, "member_assets", map, new Callback() {
                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                String json = arg0.body().string();
                                JSONObject data = JSONObject.fromObject(json);
                                if (data.optString("status").equals("success")){
                                    pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
//                                       aliPay(pid,"3");

                                    Intent intent = new Intent(BuyAssetsActivity.this, ZhifubaozhifuActivity.class);
                                    tvPay.setClickable(true);
                                    startActivity(intent);
                                }else{

                                    if (!"".equals(data.optString("msg"))){
                                        sToast(data.optString("msg"));
                                    }

                                    tvPay.setClickable(true);
                                }

                            }
                            @Override
                            public void onFailure(Request arg0, IOException arg1) {
                                if (!"".equals(arg0.toString())){
                                    sToast(arg0.toString());
                                }
                                tvPay.setClickable(true);
                            }
                        });


                    }

                }


                break;
        }
        orderPrice = Double.parseDouble(tvTotalPrice.getText().toString().substring(1,tvTotalPrice.getText().toString().length()));
    }

    @Override
    public void initData() {

    }

    public void back(View view){
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                tvCouponPrice.setText("-￥"+data.getStringExtra("price"));
                tvTotalPrice.setText("￥"+(Double.parseDouble(tvVipPrice.getText().toString()) -Double.parseDouble(data.getStringExtra("price"))));
                id = data.getStringExtra("id");

                break;
        }
    }



    /**
     * 升级资产包
     * @param type  会员类型  10 20 30 40 50
     * @param paymode   0---余额 1---支付宝 2---微信 01---支付宝余额混合支付 02---微信余额混合支付
     * @param totalpay  总金额
     */
    private void buyAssets(final String type, String paymode, String totalpay, String couponId){
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",type);
        map.put("paymode",paymode);
        map.put("totalpay",totalpay);
        map.put("conponsid",couponId);
        HttpConnect.post(this, "member_assets", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    getMoney1();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("购买资产包成功");
                            tvPay.setClickable(true);
                            id =  "0";
                            tvCouponPrice.setText("-￥0");
                            tvTotalPrice.setText("￥"+(Double.parseDouble(tvVipPrice.getText().toString())));
                            startActivity(new Intent(BuyAssetsActivity.this,BuyAssetsRecordActivity.class).putExtra("type","3"));
                        }
                    });

                }else{

                    if (!"".equals(data.getString("msg"))){
                        sToast(data.getString("msg"));
                    }
                    tvPay.setClickable(true);
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

                if (!"".equals(arg0.toString())){
                    sToast(arg0.toString());
                }

            }
        });

    }



    private void getMoney(){
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            price = Double.parseDouble( data.optJSONArray("data").optJSONObject(0).optString("money"));
                            tvPrice.setText(price+"");

//                            if(price>0)
//                            {
//                                orderPrice = Double.parseDouble(tvTotalPrice.getText().toString().substring(1,tvTotalPrice.getText().toString().length()));
//                                if(price>=orderPrice)
//                                {
//                                    isHave = true;
//                                    aSwitchPrice.setBackgroundResource(R.drawable.switch_no);
//                                    aSwitchWechat.setBackgroundResource(R.drawable.switch_off);
//                                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_off);
//
//                               alipayisSel  = false;
//                                    wxisSel = false;
//                                }else
//                                {
//                                    aSwitchPrice.setBackgroundResource(R.drawable.switch_off);
//                                    aSwitchAlipay.setBackgroundResource(R.drawable.switch_no);
//                                    isHave = false;
//                                    alipayisSel = true;
//                                }
//                            }else
//                            {
//                                aSwitchAlipay.setBackgroundResource(R.drawable.switch_no);
//                                aSwitchWechat.setBackgroundResource(R.drawable.switch_off);
//                                alipayisSel = true;
//                            }

                            aSwitchPrice.setBackgroundResource(R.drawable.switch_off);
                            aSwitchAlipay.setBackgroundResource(R.drawable.switch_off);
                            aSwitchWechat.setBackgroundResource(R.drawable.switch_no);
                            wxisSel = true;
                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    private void getMoney1(){
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            price = Double.parseDouble( data.optJSONArray("data").optJSONObject(0).optString("money"));
                            tvPrice.setText(price+"");

                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }


    /**
     * 是否购买过5000体验包
     * str == 0  可以购买
     * str == 1  不可以购买
     */
    private void isBuyexPerience(){
        HttpConnect.post(this, "member_is_buy_one", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = data.optJSONArray("data").optJSONObject(0).optString("value");

                            if (str.equals("0")){
                                isBuyexPerience = true;
                            }else if(str.equals("1")){
                                isBuyexPerience = false;
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


    /**
     * 资产包详情
     * @param type
     */
    private void assetsDetials(String type){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("id",type);
        HttpConnect.post(this, "member_assets_introduce", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvVipActivity.setText(data.optJSONArray("data").optJSONObject(0).optString("introduce"));
                            String price = data.optJSONArray("data").optJSONObject(0).optString("price");

//                            String random_money = Tools.getRandomCount(99, 10);

                            tvVipPrice.setText(price);
                            double p =Double.parseDouble(tvVipPrice.getText().toString())-Double.parseDouble(tvCouponPrice.getText().toString().substring(2,(tvCouponPrice.getText().toString().length())));
                            String s1 = String.format("%.2f", p );
                            tvTotalPrice.setText("￥"+s1);


                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }


    /**
     * 支付宝支付
     * @param pid
     * @param type
     */
    private void aliPay(final String pid, final String type){
        if (TextUtils.isEmpty(MyApplication.PARTNER) || TextUtils.isEmpty(MyApplication.RSA_PRIVATE) || TextUtils.isEmpty(MyApplication.SELLER)) {
            new AlertDialog.Builder(BuyAssetsActivity.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String > map = new HashMap<String, String>();
                map.put("pid",pid);
                map.put("type",type);
                MyNetworkConnection.getNetworkConnection(BuyAssetsActivity.this).post("post", MeActivity.payUrl, map, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String json = response.body().string();
                        JSONObject data = JSONObject.fromObject(json);
                        String sign = data.optJSONArray("data").optJSONObject(0).optString("sign");
                        final String sign1 = sign.replace('$','"');
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(BuyAssetsActivity.this);
                                // 调用支付接口，获取支付结果
                                String result = alipay.pay(sign1, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                    }
                });
            }
        });
    }


    private void getWechatPaySign(final String pid, final String type){
        HttpConnect.post(this, "member_wft_sign", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    String wxpPayUrl = data.optJSONArray("data").optJSONObject(0).optString("url");

                    HashMap<String, String > map = new HashMap<String, String>();
                    map.put("pid",pid);
                    map.put("type",type);
                    MyNetworkConnection.getNetworkConnection(BuyAssetsActivity.this).post("post", wxpPayUrl, map, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvPay.setClickable(true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String json = response.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if(data.getString("status").equals("success"))
                            {

                                final String tokenid = data.optJSONArray("data").optJSONObject(0).optString("tokenid");
                                final String services = data.optJSONArray("data").optJSONObject(0).optString("services");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvPay.setClickable(true);
                                        RequestMsg msg = new RequestMsg();
                                        msg.setTokenId(tokenid);
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId("wx7ad749f6cba84064");
                                        PayPlugin.unifiedAppPay(BuyAssetsActivity.this, msg);
                                    }
                                });
                            }else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvPay.setClickable(true);
                                    }
                                });
                            }
                        }
                    });
                }else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPay.setClickable(true);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvPay.setClickable(true);
                    }
                });
            }
        });
    }

}
