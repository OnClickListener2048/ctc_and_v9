package com.tohier.cartercoin.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static java.lang.Integer.parseInt;

public class TiXianActivity extends MyBackBaseActivity implements OnClickListener{
    private TextView tv_keyongrmb;
    private ImageView iv_select;
    private EditText et_money;
    private EditText et_pwd, et_yanzhengma;
    private Button btnyanzhengma;
    private Button btntixian;
    private Button btntixianjilu;
    private ImageView iv_back;
    private TextView et_select_card;
    private TextView tv_shouxufei,tv_send_yzm_phone;
    // private List<CardData> huiyuanbankcardList = new ArrayList<CardData>();
    // private List<Map<String,String>> huiyuanbankcardList2 = new
    // ArrayList<Map<String,String>>(); //作为显示用 给spinner 进行数据的适配
    // private SimpleAdapter adapter;
    private int position;
    private String msg;
    private String money;
    private Float keyongmoney;
    private String id;
    private String HintMoney, HintPwd, HintCode;
    private String cashoutfee;
    private int count = 120;
    private View dialog_view;
    private Button btn_begin_ws_info,btn_quxiao;
	private AlertDialog dialog;
    private ImageView ivPwdisShow;
    private GifImageView gif_loading;
    private ImageView iv_banktype_icon;
    private TextView tt1;
    
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btnyanzhengma.setText("获取短信验证码   ");
                    btnyanzhengma.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
                    btnyanzhengma.setText("验证码已发送 （"+count+"s）   ");
                    btnyanzhengma.setClickable(false);
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
    private int min;
    private int max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        MyApplication.maps.put("TiXianActivity",TiXianActivity.this);
        setContentView(R.layout.activity_rmb_tixian);
        myProgressDialog.setTitle("正在加载...");

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        tv_keyongrmb = (TextView) findViewById(R.id.tv_keyongrmb);
        tt1 = (TextView) findViewById(R.id.tt1);
        tv_send_yzm_phone = (TextView) findViewById(R.id.tv_send_yzm_phone);
        iv_select = (ImageView) findViewById(R.id.iv_select);
        et_money = (EditText) findViewById(R.id.et_money);
        btnyanzhengma = (Button) findViewById(R.id.btn_yanzhengma);
        btntixian = (Button) findViewById(R.id.btn_tixian);
        btntixianjilu = (Button) findViewById(R.id.btn_tixianjilu);
        iv_back = (ImageView) findViewById(R.id.iv_back3);
        ivPwdisShow = (ImageView) findViewById(R.id.iv_pwd_isShow);
        iv_banktype_icon = (ImageView) findViewById(R.id.iv_banktype_icon);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_yanzhengma = (EditText) this.findViewById(R.id.et_yanzhengma);
        et_select_card = (TextView) this.findViewById(R.id.et_selecy_yinhangka);
        tv_shouxufei = (TextView) this.findViewById(R.id.tv_shouxufei);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);


        btnyanzhengma.setOnClickListener(this);
        btntixian.setOnClickListener(this);
        btntixianjilu.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_select.setOnClickListener(this);
        et_select_card.setOnClickListener(this);
        ivPwdisShow.setOnClickListener(this);

        String mode = getIntent().getStringExtra("mode");
        if(!TextUtils.isEmpty(mode)&&mode.equals("banklist"))
        {
            String str = getIntent().getStringExtra("account");
            if (str != null) {
                if (str.length() == 16) {
                    String start = str.substring(0, 4);
                    String end = str.substring(12, str.length());
                    et_select_card.setText(start + " **** **** " + end);
                } else {
                    String start = str.substring(0, 4);
                    String end = str.substring(15, str.length());
                    et_select_card.setText(start + " **** *** **** " + end);
                }
            }
            String iconurl = getIntent().getStringExtra("icon");
            if(iconurl!=null)
            {
                iv_banktype_icon.setVisibility(View.VISIBLE);
                Glide.with(TiXianActivity.this).load(iconurl).placeholder(null)
                        .error(null).into(iv_banktype_icon);
            }
            id = getIntent().getStringExtra("id");
        }else if(!TextUtils.isEmpty(mode)&&mode.equals("alipay"))
        {
            iv_banktype_icon.setVisibility(View.VISIBLE);
            String str = getIntent().getStringExtra("account");
            iv_banktype_icon.setImageResource(R.mipmap.iv_alipay_tixian_icon);
            et_select_card.setText(str);
            id = getIntent().getStringExtra("id");
        }else if(TextUtils.isEmpty(mode))   //首页进入
        {
            getDefaultTiXianCard();
        }

        String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
        if(TextUtils.isEmpty(phoneNum))
        {
            tv_send_yzm_phone.setText("请到安全中心绑定有效手机号");
        }else
        {
            tv_send_yzm_phone.setText("验证码将发送到您绑定的手机（"+phoneNum.substring(0,3)+"****"+phoneNum.substring(7,11)+"），请保持手机通讯畅通");
        }
        loadWodezichan();
        loadShouXuFei();
        loadTiXianFanWei();

        dialog_view = View.inflate(this, R.layout.alertdialog_wanshanginfo, null);
        btn_begin_ws_info = (Button) dialog_view.findViewById(R.id.btn_begin_ws_info);
        btn_quxiao = (Button) dialog_view.findViewById(R.id.btn_quxiao);
        
        btn_begin_ws_info.setOnClickListener(this);
        btn_quxiao.setOnClickListener(this);
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_tixian:

                if (TextUtils.isEmpty(id)) {
                    sToast("请选择提现方式");
                    return;
                }


                if (TextUtils.isEmpty(et_money.getText().toString().trim())) {
                    sToast("请输入提现金额");
                    return;
                }

                String zhuanchuxunibicount = et_money.getText().toString().trim();

                if(min==0||max==0)
                {
                    sToast("网络质量不佳，请检查网络！");
                    return;
                }

                 double money = Double.parseDouble(zhuanchuxunibicount);
                    if(money>=min&&money<=max)
                    {
//                        if (TextUtils.isEmpty(et_yanzhengma.getText().toString().trim())) {
//                            btntixian.setClickable(true);
//                            sToast("请输入验证密码");
//                        }

                        if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
                            sToast("请输入交易密码");
                            return;
                        }
                        myProgressDialog.show();
                        Map<String, String> par = new HashMap<String, String>();
                        par.put("passwordpay", et_pwd.getText().toString().trim());
                        par.put("bankcardid", id);
                        par.put("qty", et_money.getText().toString().trim());
                        par.put("sms",et_yanzhengma.getText().toString().trim());

                        HttpConnect.post(this, "member_cash_out", par, new Callback() {

                            @Override
                            public void onResponse(Response arg0) throws IOException {
                                // TODO Auto-generated method stub
                                JSONObject jsonObject = JSONObject.fromObject(arg0
                                        .body().string());
                                msg = jsonObject.getString("msg");
                                if (jsonObject.get("status").equals("success")) {
                                    TiXianActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            myProgressDialog.cancel();
                                            sToast("提交成功");
                                            startActivity(new Intent(TiXianActivity.this, TiXianJiLuActivity.class));
                                            MyApplication.deleteActivity("BankListActivity");
                                            finish();
                                        }
                                    });
                                } else {
                                    TiXianActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            myProgressDialog.cancel();
                                            sToast(msg);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Request arg0, IOException arg1) {
                                // TODO Auto-generated method stub
                                TiXianActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        sToast("网络质量不佳，请检查网络！");
                                        myProgressDialog.cancel();
                                    }
                                });
                            }
                        });
                    }else
                    {
                        sToast("提现金额在"+min+"~"+max+"之间");
                    }
                break;
            case R.id.btn_tixianjilu:
                Intent intent = new Intent(TiXianActivity.this,
                        TiXianJiLuActivity.class);
                startActivity(intent);
//                sToast("提现记录");
                break;

            case R.id.btn_yanzhengma:
                String tel = LoginUser.getInstantiation(getApplicationContext())
                        .getLoginUser().getPhoneNum();
                if(TextUtils.isEmpty(tel)||tel.equals(""))
                {
                    sToast("请到安全中心绑定有效手机号");
                }else{
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum());
                    map.put("code","member_cash_out");
                HttpConnect.post(TiXianActivity.this, "sms_member_code", map,
                        new Callback() {

                            @Override
                            public void onResponse(Response arg0)
                                    throws IOException {
                                JSONObject jsonStr;

                                jsonStr = JSONObject.fromObject(arg0.body()
                                        .string());
                                if (jsonStr.get("status").equals("success")) {
                                    handler.post(thread);
                                } else {
                                    sToast("数据获取失败");
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

//                                sToast("链接超时！");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        });
                }
                break;

            case R.id.iv_back3:
                finish();
                break;
            case R.id.iv_select:
                Intent intent3 = new Intent(TiXianActivity.this,
                        PresentationModeActivity.class);
                startActivity(intent3);
                break;
            case R.id.et_selecy_yinhangka:
                Intent intent4 = new Intent(TiXianActivity.this, PresentationModeActivity.class);
                startActivity(intent4);
                break;
                
            case R.id.btn_begin_ws_info:
//            	Intent intent5 = new Intent(TiXianActivity.this,BandPhoneActivity.class);
//            	startActivity(intent5);
                sToast("完善信息");
            	break;
            	
            case R.id.btn_quxiao:
            	dialog.dismiss();
            	break;

            case R.id.iv_pwd_isShow:
                if(et_pwd.getTransformationMethod()== PasswordTransformationMethod.getInstance())
                {
                    //显示
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPwdisShow.setImageResource(R.mipmap.iv_pay_pwd_show);
                }else
                {
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPwdisShow.setImageResource(R.mipmap.iv_pwd_no_show);
                }
                break;
        }

    }

    private void loadShouXuFei() {

        HttpConnect.post(this, "member_cash_out_fee", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub
                JSONObject json = JSONObject.fromObject(arg0.body().string());
                if (json.get("status").equals("success")) {
                    cashoutfee = json.getJSONArray("data").getJSONObject(0).getString("cashoutfee");
                    TiXianActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_shouxufei.setText("1.提现手续费为提现的" + cashoutfee + "。");
                            tt1.setText("2.提现手续费少于2元按2元计算，超过2元则正常按照提现的" + cashoutfee + "。");
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

    private void loadWodezichan() {
        //mZProgressHUD.show();
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext())
                .getLoginUser().getUserId());
        HttpConnect.post(this, "member_count_detial", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    money = data.getJSONArray("data").getJSONObject(0)
                            .getString("money");
                    keyongmoney = Float.parseFloat(money.toString().trim());

                    TiXianActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if (money.equals("0.00") || money.equals("0.0000") ){
                                tv_keyongrmb.setText("可用人民币: ￥0" );
                            }else{
                                tv_keyongrmb.setText("可用人民币: ￥" + money);
                            }
                        }
                    });

                    Log.e("qianqian", money);
                } else {
                    // sToast(data.getString("msg"));
                }
                //mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                //mZProgressHUD.cancel();
//                sToast("链接超时！");
            }
        });
    }


    private void loadTiXianFanWei() {
        Map<String, String> par = new HashMap<String, String>();
        HttpConnect.post(this, "member_cash_out_section", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    String minStr = data.getJSONArray("data").getJSONObject(0)
                            .getString("min");
                    String maxStr = data.getJSONArray("data").getJSONObject(0)
                            .getString("max");
                    if(!TextUtils.isEmpty(minStr))
                    {
                        min = parseInt(minStr);
                    }
                    if(!TextUtils.isEmpty(maxStr))
                    {
                        max = parseInt(maxStr);
                    }
                    TiXianActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            et_money.setHint("  请输入"+min+"的整倍数");
                        }
                    });

                } else {
                    // sToast(data.getString("msg"));
                }
                //mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                //mZProgressHUD.cancel();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
//                       sToast("请检查您的网络连接状态");
                   }
               });
            }
        });
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

    public void getDefaultTiXianCard()
    {
        HttpConnect.post(TiXianActivity.this, "member_default_bankcard", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        JSONObject jsonObject = data.getJSONArray("data").optJSONObject(0);
                            if(jsonObject!=null)
                            {
                                if(jsonObject.getString("type")!=null&&!TextUtils.isEmpty(jsonObject.getString("type"))&&jsonObject.getString("type").equals("支付宝"))
                                {
                                    String str = jsonObject.getString("account");
                                    String icon = jsonObject.getString("icon");
                                    id= jsonObject.getString("id");
                                    et_select_card.setText(str);
                                    iv_banktype_icon.setVisibility(View.VISIBLE);
                                    iv_banktype_icon.setImageResource(R.mipmap.iv_alipay_tixian_icon);
                                }else if(jsonObject.getString("type")!=null&&!TextUtils.isEmpty(jsonObject.getString("type")))
                                {
                                    String str = jsonObject.getString("account");
                                    id= jsonObject.getString("id");
                                    String icon = jsonObject.getString("icon");
                                    if (str != null) {
                                    if (str.length() == 16) {
                                        String start = str.substring(0, 4);
                                        String end = str.substring(12, str.length());
                                        et_select_card.setText(start + " **** **** " + end);
                                    } else if (str.length() == 19){
                                        String start = str.substring(0, 4);
                                        String end = str.substring(15, str.length());
                                        et_select_card.setText(start + " **** *** **** " + end);
                                    }else
                                    {
                                        et_select_card.setText(str);
                                    }
                                }
                                    iv_banktype_icon.setVisibility(View.VISIBLE);
                                    if(Util.isOnMainThread()) {
                                        Glide.with(TiXianActivity.this).load(icon).placeholder(null)
                                                .error(null).into(iv_banktype_icon);
                                    }

                                }
                            }
                        }
                    });

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(data.optString("msg")!=null&&!TextUtils.isEmpty(data.optString("msg")))
                            {
                                if(!Tools.isPhonticName(data.optString("msg")))
                                {
                                    sToast(data.optString("msg"));
                                }
                            }
                            et_select_card.setText("");
                            iv_banktype_icon.setVisibility(View.GONE);
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
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String mode = getIntent().getStringExtra("mode");
        if(TextUtils.isEmpty(mode))
        {
            getDefaultTiXianCard();
        }
    }
}
