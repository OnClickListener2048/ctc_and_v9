package com.tohier.cartercoin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.mobileim.conversation.EServiceContact;
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
import java.util.HashMap;

import static com.tohier.cartercoin.R.id.tv_money;
import static com.tohier.cartercoin.config.MyNetworkConnection.getNetworkConnection;


public class RechargeRMBActivity extends MyBackBaseActivity implements View.OnClickListener{

    private String rechrageMoney = "0";
    private TextView tvMoney,tvmax,tvKefu;
    private ImageView ivKefu;
    private EditText etMoney;
    private Button btnCharge,btnChargeRecord;
    private ToggleButton btnWX,btnAlipay;
    private String payType = "wechat";
    private TextView tvPrice;
    /**
     * 订单id
     */
    private  String pid;

    /**
     * 0---期权
     * 1---中心
     */
    private String type;

    /**
     * 支付宝工具类
     */
    private AlipayUtils alipayUtils;

    private View view;
    PopupWindow window = null;

    private View view1;
    PopupWindow window1 = null;

    private String min;


    private static final int SDK_PAY_FLAG = 1;


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
                        Toast.makeText(RechargeRMBActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeRMBActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(RechargeRMBActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_recharge_rmb);

        init();

        setUp();
    }

    private void setUp() {

        tvKefu.setOnClickListener(this);
        ivKefu.setOnClickListener(this);
        btnCharge.setOnClickListener(this);
        btnChargeRecord.setOnClickListener(this);

        btnWX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payType = "wechat";
                     btnAlipay.setChecked(false);
                }else{
                    payType = "alipay";
                    btnAlipay.setChecked(true);
                }
            }
        });

        btnAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payType = "alipay";
                    btnWX.setChecked(false);
                }else{
                    payType = "wechat";
                    btnWX.setChecked(true);
                }
            }
        });

    }

    private void init() {

        view = View.inflate(this,R.layout.activity_con_firm_pay,null);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        view1 = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view1.findViewById(R.id.tv_title)).setText("充值");
        ((TextView)view1.findViewById(R.id.tv_prompt)).setText("现金充值");
        window1 = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        type = getIntent().getStringExtra("type");
        tvMoney = (TextView) findViewById(tv_money);
        tvmax = (TextView) findViewById(R.id.tv_max);
        tvKefu = (TextView) findViewById(R.id.tv_kefu);
        etMoney = (EditText) findViewById(R.id.et_money);
        btnWX = (ToggleButton) findViewById(R.id.btn_wx);
        btnAlipay = (ToggleButton) findViewById(R.id.btn_alipay);
        btnCharge = (Button) findViewById(R.id.btn_charge);
        ivKefu = (ImageView) findViewById(R.id.iv_kefu);
        btnChargeRecord = (Button) findViewById(R.id.btn_charge_record);
        tvPrice = (TextView) findViewById(R.id.tv_vip_price);

        alipayUtils =  new AlipayUtils();


        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final String money = data.optJSONArray("data").optJSONObject(0).optString("money");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (money.equals("0.00") || money.equals("0.0000") ){
                                tvMoney.setText("0RMB");
                            }else{
                                tvMoney.setText(money+"RMB");
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        HttpConnect.post(this, "member_rechrage_cash", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    final String max = data.optJSONArray("data").optJSONObject(0).optString("max");
                    min = data.optJSONArray("data").optJSONObject(0).optString("min");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvmax.setText("本次可充值"+min+"~"+max+"RMB");
                            etMoney.setHint("充值金额不能低于"+min+"元");
                        }
                    });
                }

            }
            @Override


            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        /**
         *
         * 随机生成小数的充值金额
         */

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString())){
//                    String random_money = Tools.getRandomCount(99, 10);
//                    rechrageMoney = s.toString().trim()+"."+random_money;
                    rechrageMoney = s.toString().trim();
                    tvPrice.setText(rechrageMoney);
                }else{
                    tvPrice.setText("0.00");
                }
            }
        });
    }


    /**
     * 显示取消支付的提示框
     */
    public void showWxPayDialog()
    {
        Button btnCencel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) view.findViewById(R.id.btn_commit);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取签名 并完成支付
                getWechatPaySign(pid,type);
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
            window.showAtLocation(btnChargeRecord,
                    Gravity.BOTTOM, 0, 0);
        }
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
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPay(pid,type);
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
            window.showAtLocation(btnChargeRecord,
                    Gravity.BOTTOM, 0, 0);
        }
    }


    public void back(View view){
        finish();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        //intent = new Intent(getActivity(),AddWXKeFu.class);
        //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
        EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
        //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
        //的setNeedByPass方法，参数为false。
        //contact.setNeedByPass(false);
        Intent intent1;
        switch (v.getId()){

            case R.id.iv_kefu:
                intent1 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent1);

                break;

            case R.id.btn_charge_record:
                startActivity(new Intent(this,RecordActivity.class).putExtra("type",type));
                break;

            case R.id.btn_charge:

                if (TextUtils.isEmpty(etMoney.getText().toString())){
                    sToast("请输入充值金额");
                    return;
                }

                if(Integer.parseInt(etMoney.getText().toString())<Integer.parseInt(min))
                {
                    sToast("单笔充值不能少于"+min+"人民币");
                    return;
                }

                btnCharge.setClickable(false);
                /**
                 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
                 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
                 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
                 *
                 * orderInfo的获取必须来自服务端；
                 */
                if (payType.equals("wechat")){

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("qty", rechrageMoney);
                    map.put("paymode","1");
                    map.put("type",type);
                    HttpConnect.post(RechargeRMBActivity.this, "member_cash_in", map, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                pid = data.optJSONArray("data").optJSONObject(0).optString("ID");

                                //获取签名 并完成支付
                                getWechatPaySign(pid,type);
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnCharge.setClickable(true);
                                    }
                                });
                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnCharge.setClickable(true);
                                }
                            });
                        }
                    });

                }else if (payType.equals("alipay")){

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("qty", rechrageMoney);
                    map.put("paymode","0");
                    map.put("type",type);
                    HttpConnect.post(RechargeRMBActivity.this, "member_cash_in", map, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                pid = data.optJSONArray("data").optJSONObject(0).optString("ID");

//                                aliPay(pid,type);

                                Intent intent = new Intent(RechargeRMBActivity.this, ZhifubaozhifuActivity.class);
                                btnCharge.setClickable(true);
                                startActivity(intent);
                            }else{
                                btnCharge.setClickable(true);
                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            btnCharge.setClickable(true);
                        }
                    });

                }


                break;
        }
    }



    private void aliPay(final String pid, final String type){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String > map = new HashMap<String, String>();
                map.put("pid",pid);
                map.put("type",type);
                getNetworkConnection(RechargeRMBActivity.this).post("post", MeActivity.payUrl, map, new Callback() {
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
                                PayTask alipay = new PayTask(RechargeRMBActivity.this);
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
                    MyNetworkConnection.getNetworkConnection(RechargeRMBActivity.this).post("post", wxpPayUrl, map, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnCharge.setClickable(true);
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
                                        btnCharge.setClickable(true);
                                        RequestMsg msg = new RequestMsg();
                                        msg.setTokenId(tokenid);
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId("wx7ad749f6cba84064");
                                        PayPlugin.unifiedAppPay(RechargeRMBActivity.this, msg);
                                    }
                                });
                            }else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnCharge.setClickable(true);
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
                            btnCharge.setClickable(true);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnCharge.setClickable(true);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

                Toast.makeText(getApplicationContext(), "pay_result-->" + data.getStringExtra("pay_result"), Toast.LENGTH_SHORT).show();
    }

}
