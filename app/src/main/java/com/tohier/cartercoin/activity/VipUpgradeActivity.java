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
import com.tohier.cartercoin.columnview.SlideSelectView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tohier.cartercoin.R.id.switch_alipay;
import static com.tohier.cartercoin.R.id.switch_price;
import static com.tohier.cartercoin.R.id.tv_crown;
import static com.tohier.cartercoin.R.id.tv_crown_price;
import static com.tohier.cartercoin.R.id.tv_diamond;
import static com.tohier.cartercoin.R.id.tv_diamond_price;
import static com.tohier.cartercoin.R.id.tv_platinum;
import static com.tohier.cartercoin.R.id.tv_platinum_price;
import static java.lang.Double.parseDouble;

public class VipUpgradeActivity extends MyBaseActivity implements View.OnClickListener {

    /***
     *
     * 优惠券列表
     */
    private  ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    /**
     * 会员等级
     */
    private ArrayList<String> listType = new ArrayList<String>();
    private String id = "0";

    private LinearLayout llZhuce,llPlatinum,llDamond,llCrown,llCoupon;
    private TextView tvZhuce,tvPlatinum,tvDamond,tvCrown,tvVipType,tvVipPrice,tvCouponPrice,tvPrice,tvTotalPrice,tvPay;
    private Button aSwitchPrice,aSwitchWechat,aSwitchAlipay;
    private TextView tvZhucePrice,tvPlatinumPrice,tvDamondPrice,tvCrownPrice,tvUpgradeRecord;
    private SlideSelectView slideSelectView;
    private String[] textStrings;
    private FrameLayout tvVipExplain;
    private ImageView iv_question_mark;
    /**
     * 资产包类型
     */
    private String type = "10";
    private String selType = "10";
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

    /**
     * 补完差价之后的价格
     */
    private String chaPrice;


    private AlipayUtils alipayUtils;
    private static final int SDK_PAY_FLAG = 1;

    private String pid;

    private View view,view1;
    PopupWindow window = null;
    PopupWindow window1 = null;

    /**
     * 月份
     */
    private String qty = "3";
    private TextView tv_month;


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
                        Toast.makeText(VipUpgradeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        LoginUser.getInstantiation(getApplicationContext()).getLoginUser().setType(type);
                        LoginUser.getInstantiation(getApplicationContext()).setType(type);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(VipUpgradeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(VipUpgradeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_vip_upgrade);
        MyApplication.activities.add(this);
        init();
        setUp();
    }

    private void setUp() {
        aSwitchPrice.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvVipExplain.setOnClickListener(this);

        tvUpgradeRecord.setOnClickListener(this);
        /**
         * 微信支付
         */
        aSwitchWechat.setOnClickListener(this);
        /**
         * 支付宝支付
         */
        aSwitchAlipay.setOnClickListener(this);

        tvVipType.setOnClickListener(this);

        slideSelectView.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                String month = "";
                if (index == 0){
                    month = "1个月";
                    qty = "1";
                }else if (index == 1){
                    month = "2个月";
                    qty = "2";
                }else if (index == 2){
                    month = "3个月";
                    qty = "3";
                }else if (index == 3){
                    month = "4个月";
                    qty = "4";
                }else if (index == 4){
                    month = "5个月";
                    qty = "5";
                }else if (index == 5){
                    month = "6个月";
                    qty = "6";
                }else if (index == 6){
                    month = "7个月";
                    qty = "7";
                }else if (index == 7){
                    month = "8个月";
                    qty = "8";
                }else if (index == 8){
                    month = "9个月";
                    qty = "9";
                }else if (index == 9){
                    month = "10个月";
                    qty = "10";
                }else if (index == 10){
                    month = "11个月";
                    qty = "11";
                }else if (index == 11){
                    month = "12个月";
                    qty = "12";
                }
                tv_month.setText(month);
                getFinalPrice(selType,qty);

            }
        });



    }

    private void init() {

        view = View.inflate(this,R.layout.activity_prompt,null);

        ((TextView)view.findViewById(R.id.tv_title)).setText("VIP升级");

        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);


        view1 = View.inflate(this,R.layout.activity_vip_type,null);
        window1 = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        llZhuce = (LinearLayout) findViewById(R.id.ll_zhuce);
        llPlatinum = (LinearLayout) findViewById(R.id.ll_platinum);
        llDamond = (LinearLayout) findViewById(R.id.ll_diamond);
        llCrown = (LinearLayout) findViewById(R.id.ll_crown);
        llCoupon = (LinearLayout) findViewById(R.id.ll_coupon);
        tvZhuce = (TextView) findViewById(R.id.tv_zhuce);
        tvPlatinum = (TextView) findViewById(tv_platinum);
        tvDamond = (TextView) findViewById(tv_diamond);
        tvCrown = (TextView) findViewById(tv_crown);

        tvZhucePrice = (TextView) findViewById(R.id.tv_zhuce_price);
        tvPlatinumPrice = (TextView) findViewById(tv_platinum_price);
        tvDamondPrice = (TextView) findViewById(tv_diamond_price);
        tvCrownPrice = (TextView) findViewById(tv_crown_price);

        tvVipType = (TextView) findViewById(R.id.tv_vip_type);
        tvVipExplain = (FrameLayout) findViewById(R.id.tv_vip_explain);
        iv_question_mark = (ImageView) findViewById(R.id.iv_question_mark);

        final RotateAnimation rotateAnimation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(2000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);

        tvVipPrice = (TextView) findViewById(R.id.tv_vip_price);
        tvCouponPrice = (TextView) findViewById(R.id.tv_coupon_price);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        aSwitchPrice = (Button) findViewById(switch_price);
        aSwitchWechat = (Button) findViewById(R.id.switch_wechat);
        aSwitchAlipay = (Button) findViewById(switch_alipay);
        tvUpgradeRecord = (TextView) findViewById(R.id.tv_upgrade_record);
        slideSelectView = (SlideSelectView) findViewById(R.id.slideSelectView);
        tv_month = (TextView) findViewById(R.id.tv_month);
        textStrings = getIntent().getExtras().getStringArray("textStrings");

        if (textStrings !=null){
            slideSelectView.setString(textStrings);
        }


        alipayUtils = new AlipayUtils();
        drawable = getResources().getDrawable(R.mipmap.dianed);
        drawable1 = getResources().getDrawable(R.mipmap.dian);

        getMoney();
        setVipType();

    }

    @Override
    public void onClick(View v) {
        String currentType = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getType();
        switch (v.getId()){
            case R.id.tv_vip_type:
                show1();
                break;
            case R.id.tv_upgrade_record:
                startActivity(new Intent(this,UpgradeRecordActivity.class).putExtra("type","2"));
                break;
            case R.id.tv_vip_explain:
                startActivity(new Intent(this,VipJurisdictionActivity.class));
                break;

            case R.id.ll_zhuce:
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvZhuce.setTextColor(0xfffeb531);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xff000000);
                tvCrown.setTextColor(0xff000000);

                tvZhucePrice.setTextColor(0xfffeb531);
                tvPlatinumPrice.setTextColor(0xff000000);
                tvDamondPrice.setTextColor(0xff000000);
                tvCrownPrice.setTextColor(0xff000000);

                selType = "10";

                break;

            case R.id.ll_platinum:
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);

                tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvZhuce.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xfffeb531);
                tvDamond.setTextColor(0xff000000);
                tvCrown.setTextColor(0xff000000);

                tvZhucePrice.setTextColor(0xff000000);
                tvPlatinumPrice.setTextColor(0xfffeb531);
                tvDamondPrice.setTextColor(0xff000000);
                tvCrownPrice.setTextColor(0xff000000);
                selType = "20";
                getFinalPrice(selType,qty);

                break;

            case R.id.ll_crown:
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

                tvZhuce.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xff000000);
                tvCrown.setTextColor(0xfffeb531);

                tvZhucePrice.setTextColor(0xff000000);
                tvPlatinumPrice.setTextColor(0xff000000);
                tvDamondPrice.setTextColor(0xff000000);
                tvCrownPrice.setTextColor(0xfffeb531);

                selType = "40";
                getFinalPrice(selType,qty);

                break;

            case R.id.ll_diamond:
                tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);

                tvZhuce.setTextColor(0xff000000);
                tvPlatinum.setTextColor(0xff000000);
                tvDamond.setTextColor(0xfffeb531);
                tvCrown.setTextColor(0xff000000);

                tvZhucePrice.setTextColor(0xff000000);
                tvPlatinumPrice.setTextColor(0xff000000);
                tvDamondPrice.setTextColor(0xfffeb531);
                tvCrownPrice.setTextColor(0xff000000);
                selType = "30";
                getFinalPrice(selType,qty);

                break;

            case R.id.ll_coupon:
                startActivityForResult(new Intent(this,CouponActivity.class).putExtra("type","2"),1);
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
                orderPrice = parseDouble(tvTotalPrice.getText().toString().substring(1,tvTotalPrice.getText().toString().length()));
                   if(isHave==false&&wxisSel==false&&alipayisSel==false)
                   {
                       sToast("请选择支付方式");
                       return;
                   }

                tvPay.setClickable(false);

                if(isHave)
                {
                    if(wxisSel==false&&alipayisSel==false)//余额支付
                    {
//                          sToast("余额支付");
                        show();

                    }else if(wxisSel)
                    {
                        sToast("余额与微信混合支付");

                    }
                }else
                {
                    if(wxisSel)
                    {
                        show();
                    }else if(alipayisSel) {//支付宝支付
                        show();
                    }

                }


                break;
        }
        orderPrice = parseDouble(tvTotalPrice.getText().toString().substring(1,tvTotalPrice.getText().toString().length()));
    }

    @Override
    public void initData() {

    }




    public void show1()
    {

        final TextView tv_vip01 = (TextView) view1.findViewById(R.id.tv_vip_02);
        final TextView tv_vip02 = (TextView) view1.findViewById(R.id.tv_vip_03);
        final TextView tv_vip03 = (TextView) view1.findViewById(R.id.tv_vip_04);
        ImageView iv_cencel = (ImageView) view1.findViewById(R.id.iv_cancel);

        HttpConnect.post(this, "member_had_buy_upgrade_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final String date1 = data.optJSONArray("data").optJSONObject(1).optString("duedate");
                    final String date2 = data.optJSONArray("data").optJSONObject(2).optString("duedate");
                    final String date3 = data.optJSONArray("data").optJSONObject(3).optString("duedate");
                    final String date11 = data.optJSONArray("data").optJSONObject(1).optString("begindate");
                    final String date22 = data.optJSONArray("data").optJSONObject(2).optString("begindate");
                    final String date33 = data.optJSONArray("data").optJSONObject(3).optString("begindate");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{

                                if (date1.contains("1900")){
                                    tv_vip01.setText("--");
                                }else{
                                    if (date2.contains("1900") && date3.contains("1900")){

                                        tv_vip01.setText(date11.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                    }else if (!date2.contains("1900") && date3.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date2.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }else if (!date3.contains("1900") && date2.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date3.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }else if (!date3.contains("1900") && !date2.contains("1900")){

                                        if (Long.parseLong(Tools.dateToStamp(date2))>Long.parseLong(Tools.dateToStamp(date1)) || Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date1))){
                                            tv_vip01.setText("--");
                                        }else{
                                            tv_vip01.setText(date2.replaceAll("/","-").split(" ")[0]+"~"+date1.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }

                                    if(tv_vip01.getText().toString().split("~")[0].equals(tv_vip01.getText().toString().split("~")[1])){
                                        tv_vip01.setText("--");
                                    }
                                }


                                if (date2.contains("1900")){
                                    tv_vip02.setText("--");
                                }else{
                                    if (date3.contains("1900")){
                                        tv_vip02.setText(date22.replaceAll("/","-").split(" ")[0]+"~"+date2.replaceAll("/","-").split(" ")[0]);
                                    }else{

                                        if (Long.parseLong(Tools.dateToStamp(date3))>Long.parseLong(Tools.dateToStamp(date2))){
                                            tv_vip02.setText("--");
                                        }else{
                                            tv_vip02.setText(date3.replaceAll("/","-").split(" ")[0]+"~"+date2.replaceAll("/","-").split(" ")[0]);
                                        }

                                    }

                                    if(tv_vip02.getText().toString().split("~")[0].equals(tv_vip02.getText().toString().split("~")[1])){
                                        tv_vip02.setText("--");
                                    }
                                }
                                if (date3.contains("1900")){
                                    tv_vip03.setText("--");
                                }else{
                                    tv_vip03.setText(date33.replaceAll("/","-").split(" ")[0]+"~"+date3.replaceAll("/","-").split(" ")[0]);
                                    if(tv_vip03.getText().toString().split("~")[0].equals(tv_vip03.getText().toString().split("~")[1])){
                                        tv_vip03.setText("--");
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else{
                    sToast(data.optString("msg"));
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                sToast(arg0.toString());
            }
        });


        iv_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window1.dismiss();
            }
        });


        if(!window1.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            window1.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            window1.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window1.setBackgroundDrawable(dw);

            // 在底部显示
            window1.showAtLocation(tvUpgradeRecord,
                    Gravity.BOTTOM, 0, 0);
        }
    }


    /**
     * 显示取消支付的提示框
     */
    public void show()
    {
        if(!TextUtils.isEmpty(type))
        {
            if(selType.equals("10"))
            {
               sToast("请选择VIP会员");
                tvPay.setClickable(true);
            }else
            {
                if(Integer.parseInt(selType)>Integer.parseInt(type))
                {
                    if(selType.equals("10"))
                    {
                        ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要升级VIP吗?");
                    }else if(selType.equals("20"))
                    {
                        ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要升级VIP吗?");
                    }else if(selType.equals("30"))
                    {
                        if(type.equals("10"))
                        {
                            ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要升级VIP吗?");
                        }else if(type.equals("20"))
                        {
                            ((TextView)view.findViewById(R.id.tv_prompt)).setText("您购买的钻石会员级别覆盖原有的铂金会员");
                        }
                    }else if(selType.equals("40"))
                    {
                        if(type.equals("10"))
                        {

                            ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要升级VIP吗?");
                        }else if(type.equals("20"))
                        {
                            ((TextView)view.findViewById(R.id.tv_prompt)).setText("您购买的皇冠会员级别覆盖原有的铂金会员");
                        }else if(type.equals("30"))
                        {
                            ((TextView)view.findViewById(R.id.tv_prompt)).setText("您购买的皇冠会员级别覆盖原有的钻石会员");
                        }
                    }
                }else
                {
                    ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要 吗?");
                }
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
                        if(isHave)
                        {
                            if(wxisSel==false&&alipayisSel==false)
                            {
                                buyAssets(selType,"0",orderPrice+"",id,qty);
                            }
                        }else
                        {
                            if(wxisSel)
                            {
                                final HashMap<String,String> map = new HashMap<String,String>();
                                map.put("type",selType);
                                map.put("paymode","2");
                                map.put("totalpay",orderPrice+"");
                                map.put("conponsid",id);
                                map.put("qty",qty);
                                HttpConnect.post(VipUpgradeActivity.this, "member_upgrade", map, new Callback() {
                                    @Override
                                    public void onResponse(Response arg0) throws IOException {
                                        String json = arg0.body().string();
                                        JSONObject data = JSONObject.fromObject(json);
                                        if (data.optString("status").equals("success")){
                                            pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
//                                       aliPay(pid,"2");
                                            getWechatPaySign(pid,"2");
                                        }else{
                                            sToast(data.optString("msg"));
                                            tvPay.setClickable(true);
                                        }

                                    }
                                    @Override
                                    public void onFailure(Request arg0, IOException arg1) {
                                        sToast(arg0.toString());
                                        tvPay.setClickable(true);
                                    }
                                });
                            }else if(alipayisSel)
                            {
                                final HashMap<String,String> map = new HashMap<String,String>();
                                map.put("type",selType);
                                map.put("paymode","1");
                                map.put("totalpay",orderPrice+"");
                                map.put("conponsid",id);
                                map.put("qty",qty);
                                HttpConnect.post(VipUpgradeActivity.this, "member_upgrade", map, new Callback() {
                                    @Override
                                    public void onResponse(Response arg0) throws IOException {
                                        String json = arg0.body().string();
                                        JSONObject data = JSONObject.fromObject(json);
                                        if (data.optString("status").equals("success")){
                                            pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
//                                       aliPay(pid,"2");
                                            Intent intent = new Intent(VipUpgradeActivity.this, ZhifubaozhifuActivity.class);
                                            tvPay.setClickable(true);
                                            startActivity(intent);
                                        }else{
                                            sToast(data.optString("msg"));
                                            tvPay.setClickable(true);
                                        }

                                    }
                                    @Override
                                    public void onFailure(Request arg0, IOException arg1) {
                                        sToast(arg0.toString());
                                        tvPay.setClickable(true);
                                    }
                                });
                            }
                        }


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
                    window.showAtLocation(tvUpgradeRecord,
                            Gravity.BOTTOM, 0, 0);
                }
            }
        }else
        {
//            sToast("请检查您的网络链接状态");
        }
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
                if (chaPrice != null){
                    tvTotalPrice.setText("￥"+(parseDouble(chaPrice) - parseDouble(data.getStringExtra("price"))));
                }
                id = data.getStringExtra("id");
                break;
        }
    }


    /**
     * 升级会员
     * @param type  会员类型  10 20 30 40
     * @param paymode   0---余额 1---支付宝 2---微信 01---支付宝余额混合支付 02---微信余额混合支付
     * @param totalpay  总金额
     */
    private void buyAssets(final String type, String paymode, String totalpay, String couponId,String qty){
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",type);
        map.put("paymode",paymode);
        map.put("totalpay",totalpay);
        map.put("conponsid",couponId);
        map.put("qty",qty);
        HttpConnect.post(this, "member_upgrade", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    getMoney1();
//                    LoginUser.getInstantiation(getApplicationContext()).getLoginUser().setType(type);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoginUser.getInstantiation(getApplicationContext()).setType(type);
                            sToast("支付成功");
                            id =  "0";
                            tvCouponPrice.setText("-￥0");
                            tvTotalPrice.setText("￥"+(parseDouble(chaPrice)));
                            tvPay.setClickable(true);
                            startActivity(new Intent(VipUpgradeActivity.this,UpgradeRecordActivity.class).putExtra("type","2"));
                        }
                    });

                }else{
                    sToast(data.optString("msg"));
                    tvPay.setClickable(true);
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                sToast(arg0.toString());
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
                            price = parseDouble( data.optJSONArray("data").optJSONObject(0).optString("money"));
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
//                                    alipayisSel  = false;
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
                            price = parseDouble( data.optJSONArray("data").optJSONObject(0).optString("money"));
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



    private void setVipType(){
        HttpConnect.post(this, "member_update_detail", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final JSONArray list1 = data.optJSONArray("data");
                    for (int i = 0;i<list1.size();i++){
                        listType.add(list1.optJSONObject(i).optString("name"));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvZhuce.setText(" "+list1.optJSONObject(0).optString("name"));
                            tvPlatinum.setText(" "+list1.optJSONObject(1).optString("name"));
                            tvDamond.setText(" "+list1.optJSONObject(2).optString("name"));
                            tvCrown.setText(" "+list1.optJSONObject(3).optString("name"));

                            tvZhucePrice.setText("("+list1.optJSONObject(0).optString("price")+"元)");
                            tvPlatinumPrice.setText("("+list1.optJSONObject(1).optString("price")+"元)");
                            tvDamondPrice.setText("("+list1.optJSONObject(2).optString("price")+"元)");
                            tvCrownPrice.setText("("+list1.optJSONObject(3).optString("price")+"元)");


                            HttpConnect.post(VipUpgradeActivity.this, "member_info", null, new Callback() {

                                @Override
                                public void onResponse(Response arg0) throws IOException {
                                    JSONObject data = JSONObject.fromObject(arg0.body().string());
                                    if (data.get("status").equals("success")) {
                                        type = data.getJSONArray("data").getJSONObject(0).getString("type");

                                        Handler dataHandler = new Handler(getContext()
                                                .getMainLooper()) {

                                            @Override
                                            public void handleMessage(final Message msg) {


                                                if(!TextUtils.isEmpty(type))
                                                {
                                                    if(type.equals("10"))
                                                    {
                                                        tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                                                        tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvZhuce.setTextColor(0xfffeb531);
                                                        tvPlatinum.setTextColor(0xff000000);
                                                        tvDamond.setTextColor(0xff000000);
                                                        tvCrown.setTextColor(0xff000000);

                                                        tvZhucePrice.setTextColor(0xfffeb531);
                                                        tvPlatinumPrice.setTextColor(0xff000000);
                                                        tvDamondPrice.setTextColor(0xff000000);
                                                        tvCrownPrice.setTextColor(0xff000000);

                                                        tvVipType.setText(listType.get(0));
                                                        llPlatinum.setOnClickListener(VipUpgradeActivity.this);
                                                        llDamond.setOnClickListener(VipUpgradeActivity.this);
                                                        llCrown.setOnClickListener(VipUpgradeActivity.this);
                                                    }else if(type.equals("20"))
                                                    {

                                                        tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                                                        tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvZhuce.setTextColor(0xff000000);
                                                        tvPlatinum.setTextColor(0xfffeb531);
                                                        tvDamond.setTextColor(0xff000000);
                                                        tvCrown.setTextColor(0xff000000);

                                                        tvZhucePrice.setTextColor(0xff000000);
                                                        tvPlatinumPrice.setTextColor(0xfffeb531);
                                                        tvDamondPrice.setTextColor(0xff000000);
                                                        tvCrownPrice.setTextColor(0xff000000);

                                                        tvVipType.setText(listType.get(1));
                                                        llPlatinum.setOnClickListener(VipUpgradeActivity.this);
                                                        llDamond.setOnClickListener(VipUpgradeActivity.this);
                                                        llCrown.setOnClickListener(VipUpgradeActivity.this);
                                                    }else if(type.equals("30"))
                                                    {
                                                        tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                                                        tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);

                                                        tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvZhuce.setTextColor(0xff000000);
                                                        tvPlatinum.setTextColor(0xff000000);
                                                        tvDamond.setTextColor(0xfffeb531);
                                                        tvCrown.setTextColor(0xff000000);

                                                        tvZhucePrice.setTextColor(0xff000000);
                                                        tvPlatinumPrice.setTextColor(0xff000000);
                                                        tvDamondPrice.setTextColor(0xfffeb531);
                                                        tvCrownPrice.setTextColor(0xff000000);
                                                        tvVipType.setText(listType.get(2));
                                                        llPlatinum.setOnClickListener(VipUpgradeActivity.this);
                                                        llDamond.setOnClickListener(VipUpgradeActivity.this);
                                                        llCrown.setOnClickListener(VipUpgradeActivity.this);
                                                    }else if(type.equals("40"))
                                                    {
                                                        tvZhuce.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvPlatinum.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvDamond.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                                                        tvCrown.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

                                                        tvZhuce.setTextColor(0xff000000);
                                                        tvPlatinum.setTextColor(0xff000000);

                                                        tvDamond.setTextColor(0xff000000);
                                                        tvCrown.setTextColor(0xfffeb531);

                                                        tvZhucePrice.setTextColor(0xff000000);
                                                        tvPlatinumPrice.setTextColor(0xff000000);
                                                        tvDamondPrice.setTextColor(0xff000000);
                                                        tvCrownPrice.setTextColor(0xfffeb531);
                                                        tvVipType.setText(listType.get(3));
                                                        llPlatinum.setOnClickListener(VipUpgradeActivity.this);
                                                        llDamond.setOnClickListener(VipUpgradeActivity.this);
                                                        llCrown.setOnClickListener(VipUpgradeActivity.this);
                                                    }

                                                    selType = type;

                                                    getFinalPrice(selType,qty);
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

                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }
                            });

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
     * 补完差价之后的钱数
     * @param newType
     */
    private void getFinalPrice( String newType,String qty){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("new",newType);
        map.put("qty",qty);
        HttpConnect.post(this, "member_upgrade_money_diff", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    String str = data.optJSONArray("data").optJSONObject(0).optString("perfect");
                    if (TextUtils.isEmpty(str)){
                        str = "0.00";
                   }

//                    if (str.contains(".00") || !str.contains(".")){
//
//                        String random_money = Tools.getRandomCount(99, 10);
//                        chaPrice =  str.substring(0,str.lastIndexOf("."))+"."+random_money;
//                    }else{
                        chaPrice = str;
//                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvVipPrice.setText("￥"+chaPrice);

                            double p = Double.parseDouble(chaPrice)-Double.parseDouble(tvCouponPrice.getText().toString().substring(2,(tvCouponPrice.getText().toString().length())));
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
            new AlertDialog.Builder(VipUpgradeActivity.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
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
                MyNetworkConnection.getNetworkConnection(VipUpgradeActivity.this).post("post", MeActivity.payUrl, map, new Callback() {
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
                                PayTask alipay = new PayTask(VipUpgradeActivity.this);
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
                    MyNetworkConnection.getNetworkConnection(VipUpgradeActivity.this).post("post", wxpPayUrl, map, new Callback() {
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
                                        PayPlugin.unifiedAppPay(VipUpgradeActivity.this, msg);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

}
