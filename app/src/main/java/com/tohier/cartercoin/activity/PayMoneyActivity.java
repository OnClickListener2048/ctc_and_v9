package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class PayMoneyActivity extends MyBaseActivity {

    private ImageView iv_back2;
    private CircleImageView circleImageView;
    private TextView tvNickName;
    private Button btn_comfirm;
    private int i = 0;
    private PopupWindow window;
    private View view1;
    private EditText tv_money_count,tv_money_count1;
    private String newprice;
    private TextView tv_tan,tv_btc,tv_eth,tv_ltc,tv_type;
    private LinearLayout tv_α;
    private  TextView tv_current_currency_count;

    /**
     * 0---α
     * 1---tan
     * 2---Btc
     * 3---ETH
     * 4---Ltc
     */
    private String payType = "0";

    /**
     * α当前价格
     */
    private double rate;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             if(v.getId()== R.id.iv_back2)
             {
                 finish();
             }else if(v.getId()== R.id.btn_comfirm)
             {
                 String name = getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
                 if(TextUtils.isEmpty(name)&&name.equals("")) {
                     show();
                 }else
                 {
                     if(i!=0)
                     {
                         if(i==2)  //存在
                         {
                             if(TextUtils.isEmpty(tv_money_count.getText().toString().trim()))
                             {
                                 sToast("请输入付款金额");
                                 return;
                             }
                             Intent intent = new Intent(PayMoneyActivity.this,EnterPaymentPasswordForPaymentActivity.class);
                             intent.putExtra("paymoney", tv_money_count.getText().toString().trim());
                             intent.putExtra("linkcode",getIntent().getStringExtra("linkcode"));
                             intent.putExtra("paytype",payType);
                             startActivity(intent);
                         }else if(i==3)  //不存在
                         {
                             sToast("请设置交易密码");
                             startActivity(new Intent(PayMoneyActivity.this, SettingPayPwdActivity.class));
                             finish();
                         }
                     }else
                     {
//                         sToast("请检查网络链接状态");
                     }
                 }
             } else  {
                 if(v.getId()== R.id.tv_α) {
                     getPrice("ctc");
                     tv_type.setText("α");
                     payType = "0";
                     loadAccountInfo();
                     tv_α.setBackgroundResource(R.drawable.bg_shape_fb6262);
                     tv_tan.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_btc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_eth.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_ltc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                 }else  if(v.getId()== R.id.tv_tan) {
                     getPrice("tan");
                     payType = "1";
                     tv_type.setText("TAN");
                     loadAccountInfo();
                     tv_α.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_tan.setBackgroundResource(R.drawable.bg_shape_fb6262);
                     tv_btc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_eth.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_ltc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                 }else  if(v.getId()== R.id.tv_btc) {
                     getPrice3("btc");
                     payType = "2";
                     tv_type.setText("BTC");
                     loadAccountInfo();
                     tv_α.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_tan.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_btc.setBackgroundResource(R.drawable.bg_shape_fb6262);
                     tv_eth.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_ltc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                 }else  if(v.getId()== R.id.tv_eth) {
                     getPrice3("eth");
                     payType = "3";
                     tv_type.setText("ETH");
                     loadAccountInfo();
                     tv_α.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_tan.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_btc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_eth.setBackgroundResource(R.drawable.bg_shape_fb6262);
                     tv_ltc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                 }else  if(v.getId()== R.id.tv_ltc) {
                     getPrice3("ltc");
                     payType = "4";
                     tv_type.setText("LTC");
                     loadAccountInfo();
                     tv_α.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_tan.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_btc.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_eth.setBackgroundResource(R.drawable.bg_shape_d1d1d1);
                     tv_ltc.setBackgroundResource(R.drawable.bg_shape_fb6262);
                 }

             }
        }
    };

   private  void  setPricePoint(final  EditText editText) {  editText.addTextChangedListener(new

                                                TextWatcher() {
                                                    @Override
                                                    public  void onTextChanged(CharSequence s, int  start, int  before,  int  count) {

                                                        if (s.toString().contains("."))
                                                        {
                                                          if (s.length() - 1 - s.toString().indexOf(".") > 4)
                                                            {
                                                              s  = s.toString().subSequence(0,  s.toString().indexOf(".") + 5);

                                                                editText.setText(s);

                                                                editText.setSelection(s.length());

                                                            }

                                                        }

                                                        if (s.toString().trim().substring(0).equals("."))
                                                        {

                                                            s = "0" + s;

                                                            editText.setText(s);

                                                            editText.setSelection(2);

                                                        }



                                                        if(s.toString().startsWith("0") && s.toString().trim().length() > 1)
                                                        {
                                                            if (!s.toString().substring(1, 2).equals("."))
                                                            {
                                                                 editText.setText(s.subSequence(0, 1));

                                                                editText.setSelection(1);

                                                                return;

                                                            }

                                                        }

                                                    }



                                                    @Override
                                                     public  void beforeTextChanged(CharSequence s, int start, int count, int after) {



                                                    }



                                                    @Override
                                                     public  void  afterTextChanged(Editable s) {

                                                        //



                                                    }



                                                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymoney_layout);

        MyApplication.maps.put("PayMoneyActivity",this);

       view1 = View.inflate(this, R.layout.popupwindow_prompt_authentication,null);

        ImageView iv_into_authentication = (ImageView) view1.findViewById(R.id.iv_into_authentication);

        ImageView iv_cancel_authentication = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        iv_into_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayMoneyActivity.this,RealNameAuthenticationActivity.class));
                window.dismiss();
            }
        });

        initData();
        setUpView();
        isExitPayPwd();
        loadAccountInfo();
    }

    @Override
    public void initData() {
        circleImageView = (CircleImageView) this.findViewById(R.id.iv_head_img);
        tvNickName = (TextView) this.findViewById(R.id.tv_nickname);
        tv_current_currency_count = (TextView) this.findViewById(R.id.tv_current_currency_count);
        tv_money_count = (EditText) this.findViewById(R.id.tv_money_count);
        tv_money_count1 = (EditText) this.findViewById(R.id.tv_money_count1);
        btn_comfirm = (Button) this.findViewById(R.id.btn_comfirm);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        tv_α = (LinearLayout) this.findViewById(R.id.tv_α);
        tv_tan = (TextView) this.findViewById(R.id.tv_tan);
        tv_btc = (TextView) this.findViewById(R.id.tv_btc);
        tv_eth = (TextView) this.findViewById(R.id.tv_eth);
        tv_ltc = (TextView) this.findViewById(R.id.tv_ltc);
        tv_type = (TextView) this.findViewById(R.id.tv_type);
        iv_back2.setOnClickListener(onClickListener);
        btn_comfirm.setOnClickListener(onClickListener);
        tv_α.setOnClickListener(onClickListener);
        tv_tan.setOnClickListener(onClickListener);
        tv_btc.setOnClickListener(onClickListener);
        tv_eth.setOnClickListener(onClickListener);
        tv_ltc.setOnClickListener(onClickListener);

        setPricePoint(tv_money_count);


        HttpConnect.post(this, "member_price_max_min", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {


                    String json = arg0.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    if (data.optString("status").equals("success")){
                    newprice = data.optJSONArray("data").optJSONObject(0).optString("newprice");
                    }


            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });

    }

    private void setUpView() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("pic")))
        {
            Glide.with(this).load(getIntent().getStringExtra("pic")).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circleImageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("nickname")))
        {
              tvNickName.setText(getIntent().getStringExtra("nickname"));
        }
    }

    /**
     * 是否有交易密码
     */
    public void isExitPayPwd()
    {
        HttpConnect.post(this, "member_is_password_pay", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    String value = data.getJSONArray("data").getJSONObject(0).getString("value");

                    if(!TextUtils.isEmpty(value)&&value.equals("1")) //存在支付密码  要修改支付密码
                    {

                        PayMoneyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                i = 2;
                            }
                        });

                    }else             //不存在需要设置
                    {
                        PayMoneyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               i = 3;
                            }
                        });
                    }
                } else {
                    sToast(data.getString("msg"));
                }
                mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                mZProgressHUD.cancel();
//                sToast("链接超时！");
            }
        });


        tv_money_count1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    BigDecimal bd5 = new BigDecimal(Double.valueOf(s.toString()));
                    BigDecimal bd6 = new BigDecimal(Double.valueOf(newprice));

                    BigDecimal b = bd5.divide(bd6,4,BigDecimal.ROUND_HALF_UP);
                    tv_money_count.setText(b+"");

                }else{
                    tv_money_count.setText("");
                }
            }
        });
//
//        tv_money_count.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(s)){
//
//                    tv_money_count1.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//        });
    }

    /**
     * 显示实名认证的提示框
     */
    public void show()
    {
        if(!window.isShowing()) {

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

            window.setOutsideTouchable(false);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(iv_back2,
                    Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     *
     * @param type
     * type == btc 比特币
     * type == eth 以太坊
     * type == ltc 莱特币
     *
     */
    private  void getPrice3(String type)
    {
        MyNetworkConnection.getNetworkConnection(PayMoneyActivity.this).post("post", "https://www.okcoin.cn/api/v1/ticker.do?symbol="+type+"_cny", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                int i = 0;
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    String json = response.body().string();
                    JSONObject data = JSONObject.fromObject(json);
                    newprice = data.optJSONObject("ticker").optString("last");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(tv_money_count1.getText().toString())){
                                BigDecimal bd5 = new BigDecimal(Double.valueOf(tv_money_count1.getText().toString()));
                                BigDecimal bd6 = new BigDecimal(Double.valueOf(newprice));

                                BigDecimal b = bd5.divide(bd6,4,BigDecimal.ROUND_HALF_UP);
                                tv_money_count.setText(b+"");

                            }else{
                                tv_money_count.setText("");
                            }
                        }
                    });

                }catch (Exception e){
                    int i = 0;
                }
            }
        });
    }

    /**
     * α和TAN的当前价格
     * @param type
     */
    private  void getPrice(final String type)
    {
        String code  = "";

        if (type.equals("ctc")){
            code = "member_price_max_min";
        }else  if (type.equals("tan")){
            code = "member_tan_pirce";
        }
        HttpConnect.post(this, code, null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {


                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    if (type.equals("ctc")){
                        newprice = data.optJSONArray("data").optJSONObject(0).optString("newprice");
                    }else  if (type.equals("tan")){
                        newprice = data.optJSONArray("data").optJSONObject(0).optString("price");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(tv_money_count1.getText().toString())){
                                BigDecimal bd5 = new BigDecimal(Double.valueOf(tv_money_count1.getText().toString()));
                                BigDecimal bd6 = new BigDecimal(Double.valueOf(newprice));

                                BigDecimal b = bd5.divide(bd6,4,BigDecimal.ROUND_HALF_UP);
                                tv_money_count.setText(b+"");

                            }else{
                                tv_money_count.setText("");
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

    private void loadAccountInfo(){
        HttpConnect.post(this, "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String αMoney = data.getJSONArray("data").getJSONObject(0).optString("ctc");//α资产账户 -
                    final String TanMoney = data.getJSONArray("data").getJSONObject(0).getString("tanmoney") ;  //TAN账户 -
                    final String btcMoney = data.getJSONArray("data").getJSONObject(0).optString("btcmoney");  //BTC账户 -
                    final String ethMoney = data.getJSONArray("data").getJSONObject(0).getString("ethmoney");  //ETH账户 -
                    final String ltcMoney = data.getJSONArray("data").getJSONObject(0).optString("ltcmoney");  //LTC账户 -

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            /**
                             * 0---α
                             * 1---tan
                             * 2---Btc
                             * 3---ETH
                             * 4---Ltc
                             */
                            if(!TextUtils.isEmpty(payType))
                            {
                                if(payType.equals("0"))  //α
                                {

                                    if(!TextUtils.isEmpty(αMoney))
                                    {
                                        if (αMoney.equals("0.00") || αMoney.equals("0.0000") ){
                                            tv_current_currency_count.setText("0");
                                        }else{
                                            tv_current_currency_count.setText(αMoney);
                                        }
                                    }
                                }else if(payType.equals("1")) //糖
                                {
                                    if(!TextUtils.isEmpty(TanMoney))
                                    {
                                        if (TanMoney.equals("0.00") || TanMoney.equals("0.0000") ){
                                            tv_current_currency_count.setText("0");
                                        }else{
                                            tv_current_currency_count.setText(TanMoney);
                                        }
                                    }
                                }else if(payType.equals("2")) // 比特币
                                {
                                    if(!TextUtils.isEmpty(btcMoney))
                                    {
                                        if (btcMoney.equals("0.00") || btcMoney.equals("0.0000") ){
                                            tv_current_currency_count.setText("0");
                                        }else{
                                            tv_current_currency_count.setText(btcMoney);
                                        }
                                    }
                                }else if(payType.equals("3")) //以太坊
                                {
                                    if(!TextUtils.isEmpty(ethMoney))
                                    {
                                        if (ethMoney.equals("0.00") || ethMoney.equals("0.0000") ){
                                            tv_current_currency_count.setText("0");
                                        }else{
                                            tv_current_currency_count.setText(ethMoney);
                                        }
                                    }
                                }else if(payType.equals("4")) //莱特币
                                {
                                    if(!TextUtils.isEmpty(ltcMoney))
                                    {
                                        if (ltcMoney.equals("0.00") || ltcMoney.equals("0.0000") ){
                                            tv_current_currency_count.setText("0");
                                        }else{
                                            tv_current_currency_count.setText(ltcMoney);
                                        }
                                    }
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    final String msg8 = data.optString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            if(!TextUtils.isEmpty(msg8))
                            {
                                if(!Tools.isPhonticName(msg8))
                                {
                                    sToast(msg8);
                                }
                            }
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!window.isShowing()) {
            finish();
        }else
        {
            window.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(!window.isShowing()) {
                finish();
            }else
            {
                window.dismiss();
            }
            return  false;
        }
        return super.onKeyDown(keyCode, event);

    }

}
