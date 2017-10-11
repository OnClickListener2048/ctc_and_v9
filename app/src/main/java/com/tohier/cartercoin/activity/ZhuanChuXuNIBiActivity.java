package com.tohier.cartercoin.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class ZhuanChuXuNIBiActivity extends MyBackBaseActivity implements
        OnClickListener{

    private ImageView iv_back, iv_erweima;
    private EditText et_qianbaoaddress, et_zhuanchucount, et_jiaoyimima,
            et_pleaseshuruyzm;
    private TextView tv_keyongkatebi_count, tv_dongjiekatebi_count,tv_keyongkatebi;
    private Button btn_confirm, btn_zhuanchujilu, btn_getyzm;
    private String qianbaodizhi, zhuanchucount, jiaoyimima, plaseshuruyzm;
    private float ctc;
    private TextView tv_zhuanchu;
    private String msg;
    private String ctcoutfee,value;
    private int count = 120;
    private View dialog_view;
    private Button btn_begin_ws_info,btn_quxiao;
    private AlertDialog dialog;
    private String shouxifei;
    private GifImageView gif_loading;
    /**
     * 币种
     */
    private String name ;

    /**
     * 0---α
     * 1---TAN
     * 2---BTC
     * 3---ETH
     * 4---LTC
     */
    private String type;

    /**
     * 钱包地址
     */
    private String address;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
//                    btn_getyzm.setBackgroundResource(R.drawable.button_shape_yellow);
                    btn_getyzm.setText("获取短信验证码");
                    btn_getyzm.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
//                    btn_getyzm.setBackgroundResource(R.drawable.button_shape_gary);
                    btn_getyzm.setText("验证码已发送 （"+count+"s）");
                    btn_getyzm.setClickable(false);
                    postDelayed(thread,1000);
                }
            }
        }
    };
    Thread thread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x111);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zhuanchuxunibi);

        init();
        setUpView();

    }

    private void init() {
        iv_back = (ImageView) this.findViewById(R.id.title_back);
        iv_erweima = (ImageView) this.findViewById(R.id.iv_erweima);
        et_qianbaoaddress = (EditText) this
                .findViewById(R.id.et_qianbaoaddress);
        et_zhuanchucount = (EditText) this.findViewById(R.id.et_zhuanchucount);
        et_jiaoyimima = (EditText) this.findViewById(R.id.et_jiaoyimima);
        et_pleaseshuruyzm = (EditText) this
                .findViewById(R.id.et_pleaseshuruyzm);
        tv_keyongkatebi_count = (TextView) this
                .findViewById(R.id.tv_keyongkatebi_count);
        tv_keyongkatebi = (TextView) this
                .findViewById(R.id.tv_keyongkatebi);
        btn_confirm = (Button) this.findViewById(R.id.btn_comfirm);
        btn_zhuanchujilu = (Button) this.findViewById(R.id.btn_zhuanchujilu);
        btn_getyzm = (Button) this.findViewById(R.id.btn_getyzm);
        tv_zhuanchu = (TextView) this.findViewById(R.id.tv_zhuanchu);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);


        setPricePoint(et_zhuanchucount);

        name = getIntent().getStringExtra("name");
        tv_keyongkatebi.setText("可转出的"+name.split(" ")[0]+" ：");

        if (name.equals("α 阿尔法")){
          type = "0";
        }else if (name.equals("TAN 唐")){
            type = "1";
        }else if (name.equals("BTC 比特币")){
            type = "2";
        }else if (name.equals("ETH 以太坊")){
            type = "3";
        }else if (name.equals("LTC 莱特币")){
            type = "4";
        }

        loadWalletAddress();

        xunibitixian();
        loadWodezichan();

        dialog_view = View.inflate(this, R.layout.alertdialog_wanshanginfo, null);
        btn_begin_ws_info = (Button) dialog_view.findViewById(R.id.btn_begin_ws_info);
        btn_quxiao = (Button) dialog_view.findViewById(R.id.btn_quxiao);

        btn_begin_ws_info.setOnClickListener(this);
        btn_quxiao.setOnClickListener(this);
    }


    private void setUpView() {

        jiaoyimima = (String) et_jiaoyimima.getHint();
        qianbaodizhi = (String) et_qianbaoaddress.getHint();
        plaseshuruyzm = (String) et_pleaseshuruyzm.getHint();
        zhuanchucount = (String) et_zhuanchucount.getHint();

        setTextSize(et_jiaoyimima.getHint().toString().trim(), et_jiaoyimima);
        setTextSize(et_pleaseshuruyzm.getHint().toString().trim(),
                et_pleaseshuruyzm);
        setTextSize(et_qianbaoaddress.getHint().toString().trim(),
                et_qianbaoaddress);
        setTextSize(et_zhuanchucount.getHint().toString().trim(),
                et_zhuanchucount);

        iv_back.setOnClickListener(this);
        iv_erweima.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_zhuanchujilu.setOnClickListener(this);
        btn_getyzm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;

            case R.id.iv_erweima:
                startActivityForResult(new Intent(ZhuanChuXuNIBiActivity.this, CaptureActivity.class), 0);
                break;

            case R.id.btn_comfirm:
                confirm_zhuanchu();
                break;

            case R.id.btn_getyzm:
                onValidateCode();
                break;

            case R.id.btn_zhuanchujilu:
                Intent intent2 = null;
                intent2 = new Intent(ZhuanChuXuNIBiActivity.this,
                        ZhuanChuJiLuActivity.class);
                intent2.putExtra("type",type);
                startActivity(intent2);
                break;

            case R.id.btn_begin_ws_info:
//            	Intent intent5 = new Intent(ZhuanChuXuNIBiActivity.this,BandPhoneActivity.class);
//            	startActivity(intent5);
                break;

            case R.id.btn_quxiao:
                dialog.dismiss();
                break;
        }
    }

    private void confirm_zhuanchu() {
        String code = "";

        btn_confirm.setClickable(false);
        if (TextUtils.isEmpty(et_qianbaoaddress.getText().toString().trim())) {
            btn_confirm.setClickable(true);
            sToast("请提供钱包地址");
            return;
        }

        if (TextUtils.isEmpty(et_zhuanchucount.getText().toString().trim())) {
            btn_confirm.setClickable(true);
            sToast("请输入转出数量");
            return;
        }

        String zhuanchuxunibicount = et_zhuanchucount.getText().toString()
                .trim();
        Boolean strResult = zhuanchuxunibicount.matches("[0-9]+.*[0-9]*");
        if (!strResult) {
            btn_confirm.setClickable(true);
            sToast("输入的转出数量不合法");
            return;
        }

        float count = 0;
        if (zhuanchuxunibicount != null) {
            count = Float.parseFloat(zhuanchuxunibicount);
        }

        if(!TextUtils.isEmpty(value))
        {
            if(count<Integer.parseInt(value))
            {
                btn_confirm.setClickable(true);
                sToast("输入的转出数量少于"+value);
                return;
            }
        }

//
//        if (TextUtils.isEmpty(et_pleaseshuruyzm.getText().toString().trim())) {
//            btn_confirm.setClickable(true);
//            sToast("请输入验证密码");
//            return;
//        }

        if ( TextUtils.isEmpty(et_jiaoyimima.getText().toString().trim())) {
            btn_confirm.setClickable(true);
            sToast("请输入交易密码");
            return;
        }
        if (count > ctc) {
            btn_confirm.setClickable(true);
            sToast("可供转出的虚拟币不足:" + count);
            return;
        }

        if (address.equals(et_qianbaoaddress.getText().toString().trim())){
            btn_confirm.setClickable(true);
            sToast("不能给在自己转币，请重新输入钱包地址！" +
                    "");
            return;
        }
        //TODO
        myProgressDialog.setTitle("正在加载");
        myProgressDialog.show();
        // 在访问网络为果之前 先显示进度条
        Map<String, String> par = new HashMap<String, String>();
        par.put("passwordpay", et_jiaoyimima.getText().toString().trim());
        par.put("address", et_qianbaoaddress.getText().toString().trim());
        par.put("qty", et_zhuanchucount.getText().toString().trim());
//        par.put("sms", et_pleaseshuruyzm.getText().toString().trim());

        if (name.equals("α 阿尔法")){
            code = "member_ctc_out";
        }else if (name.equals("TAN 唐")){
            code = "member_tan_out";
        }else if (name.equals("BTC 比特币")){
            code = "member_btc_out";
        }else if (name.equals("ETH 以太坊")){
            code = "member_eth_out";
        }else if (name.equals("LTC 莱特币")){
            code = "member_ltc_out";
        }

        HttpConnect.post(this, code, par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");
                if (data.get("status").equals("success")) {
                    ZhuanChuXuNIBiActivity.this
                            .runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    btn_confirm.setClickable(true);
                                    Intent intent = new Intent(ZhuanChuXuNIBiActivity.this,
                                            ZhuanChuJiLuActivity.class);
                                    intent.putExtra("type",type);
                                    startActivity(intent);
                                    finish();
                                }
                            });
//
                } else {
                    ZhuanChuXuNIBiActivity.this
                            .runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    btn_confirm.setClickable(true);
                                    sToast(msg);
                                }
                            });
                }
                ZhuanChuXuNIBiActivity.this
                        .runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                myProgressDialog.cancel();
                            }
                        });
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myProgressDialog.cancel();
                        btn_confirm.setClickable(true);
                    }
                });
                // mZProgressHUD.cancel();
            }
        });

    }

    private void xunibitixian() {

        HttpConnect.post(this, "member_ctc_out_fee", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub
                JSONObject json = JSONObject.fromObject(arg0.body().string());

                if (json.get("status").equals("success")) {
                    ctcoutfee = json.getJSONArray("data").getJSONObject(0).getString("ctcoutfee");
                    ZhuanChuXuNIBiActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_zhuanchu.setText("3.转出币的服务费为每次转出总额的" + ctcoutfee + "。");
                        }
                    });

                } else {

                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                // TODO Auto-generated method stub

            }
        });

        HttpConnect.post(this, "member_ctc_out_limit", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub
                JSONObject json = JSONObject.fromObject(arg0.body().string());

                if (json.get("status").equals("success")) {
                    value = json.getJSONArray("data").getJSONObject(0).getString("value");
                    ZhuanChuXuNIBiActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et_zhuanchucount.setHint("转出的数量不得少于"+value);
                        }
                    });

                } else {

                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void onValidateCode() {

        if (TextUtils.isEmpty(et_qianbaoaddress.getText().toString().trim())) {
            sToast("请提供钱包地址");
            return;
        }

        if (TextUtils.isEmpty(et_zhuanchucount.getText().toString().trim())) {
            sToast("请输入转出数量");
            return;
        }

        if ( TextUtils.isEmpty(et_jiaoyimima.getText().toString().trim())) {
            sToast("请输入交易密码");
            return;
        }


        if(TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext())
                .getLoginUser().getPhoneNum()))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setView(dialog_view);
            dialog = alertDialog.create();
            dialog.show();
        }else{
            gif_loading.setVisibility(View.VISIBLE);
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", LoginUser.getInstantiation(getApplicationContext())
                    .getLoginUser().getPhoneNum());
            par.put("code","member_ctc_out");
            HttpConnect.post(this, "sms_member_code", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    JSONObject data = JSONObject.fromObject(arg0.body().string());

                    if (data.get("status").equals("success")) {

                        handler.post(thread);

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gif_loading.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    sToast("链接超时！");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gif_loading.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadWodezichan();

    }



    String ctc1 = "";
    private void loadWodezichan() {
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    if (data.getJSONArray("data").getJSONObject(0)
                            .getString("ctc") != null) {


                        if (name.equals("α 阿尔法")){
                            ctc1 = data.getJSONArray("data").getJSONObject(0).getString("ctc");
                            ctc = Float.parseFloat(ctc1);
                        }else if (name.equals("TAN 唐")){
                            ctc1 = data.getJSONArray("data").getJSONObject(0).getString("tanmoney");
                            ctc =  Float.parseFloat(ctc1);  //现金账户
                        }else if (name.equals("BTC 比特币")){
                            ctc1 = data.getJSONArray("data").getJSONObject(0).getString("btcmoney");
                            ctc =  Float.parseFloat(ctc1);
                        }else if (name.equals("ETH 以太坊")){
                            ctc1 = data.getJSONArray("data").getJSONObject(0).getString("ethmoney");
                            ctc =  Float.parseFloat(ctc1);
                        }else if (name.equals("LTC 莱特币")){
                            ctc1 = data.getJSONArray("data").getJSONObject(0).getString("ltcmoney");
                            ctc =  Float.parseFloat(ctc1);
                        }


                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {
                            @Override
                            public void handleMessage(
                                    final Message msg)
                            {

                                if (ctc1.equals("0.00") || ctc1.equals("0.0000") ){
                                    tv_keyongkatebi_count.setText("0");
                                }else{
                                    tv_keyongkatebi_count.setText(ctc1+"");
                                }
                            }
                        };
                        dataHandler.sendEmptyMessage(0);

                        Log.e("wwkone", "ctc====" + ctc);
                    }
                } else {
                    // sToast(data.getString("msg"));
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                sToast("链接超时！");
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                String scanResult = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                if (scanResult != null && scanResult.length() > 0) {
                    if (scanResult.contains("http")) {
                        sToast("请提供正确的二维码以供扫描");
                    } else {
                        et_qianbaoaddress.setText(scanResult);
                    }
                }
            }
        }
    }

    // 设置edittext hint 字体大小
    public void setTextSize(String text, EditText editText) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(text);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null ){
            handler.removeCallbacks(thread);
        }
    }

    public void kefu(View view){
        //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
        EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
        //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
        //的setNeedByPass方法，参数为false。
        //contact.setNeedByPass(false);
        Intent intent1 = mIMKit.getChattingActivityIntent(contact);
        startActivity(intent1);
    }


    private void loadWalletAddress() {
        String code = "";
        if (name.equals("α 阿尔法")){
            code = "member_ctc_wallet_address";
        }else if (name.equals("TAN 唐")){
            code = "member_tan_wallet_address";
        }else if (name.equals("BTC 比特币")){
            code = "member_btc_wallet_address";
        }else if (name.equals("ETH 以太坊")){
            code = "member_eth_wallet_address";
        }else if (name.equals("LTC 莱特币")){
            code = "member_ltc_wallet_address";
        }

        HttpConnect.post(this, code , null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub
                JSONObject json = JSONObject.fromObject(arg0.body().string());

                if (json.get("status").equals("success")) {

                    address = json.optJSONArray("data").optJSONObject(0).optString("address");
                } else {
                    ZhuanChuXuNIBiActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                ZhuanChuXuNIBiActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
            }
        });
    }


    private  void  setPricePoint(final  EditText editText) {

        editText.addTextChangedListener(new

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

}
