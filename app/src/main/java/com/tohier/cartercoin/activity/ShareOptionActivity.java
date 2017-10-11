package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.tribe.IYWTribeService;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.MyViewPager;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.presenter.ShareOptionPresener;
import com.tohier.cartercoin.shareoptionfragment.CurrentDealFragment;
import com.tohier.cartercoin.shareoptionfragment.DealRecordFragment;
import com.tohier.cartercoin.shareoptionfragment.OrderFragment;
import com.tohier.cartercoin.ui.ShareOptionView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ShareOptionActivity extends MyBackBaseFragmentActivity implements View.OnClickListener,ShareOptionView{


    private LoadingView gifLoading,gifLoading1; //加载圈
    private TextView tvAdd100,tvAdd200,tvAdd500;
    private Button btnBuyUp,btnBuyDown,btnDeal;
    private LinearLayout llCoinType;
    private TextView tvK1m,tvK5m,tvK15m;
    private TextView tv1,tv15,tv5,tv30;
    private ImageView ivMore;
    private VerticalTextview tvInfo;
    private WebView webView;
    private ArrayAdapter<String> adapter;

    private TextView tvCurrentCtc,tvProfit,tvCurrentDeal,tvOrder,tvRecord;
    private MyViewPager vp;
    private EditText etMoney;
    private int money = 0;
    private FrameLayout tvExplain;
    private ImageView iv_question_mark;

    private ShareOptionPresener presener ;


    private double currentPrice;
    private String stamp;
    private String coinType = "btc";
    private ImageView ivLine;

    private String type = "1";  //时间，
    private String state ="3"; //涨跌状态
    private String typeCoin = "0";

    /**
     * 1---第一个时间段
     * 2---第二个时间段
     * 3---第三个时间段
     */
    private String type1 = "1";
    /**
     * 0---实盘
     * 1---虚拟盘
     */
    private  TextView tvDummy;
    private String shareOptionType = "0";

    /**
     * 卡特期权下注记录
     */
    private List<Fragment> vpFragment = new ArrayList<Fragment>();
    private CurrentDealFragment currentDealFragment;
    private DealRecordFragment dealRecordFragment;
    private OrderFragment orderFragment;
    private int offset;
    private int currIndex;
    private int one;

    /**
     * 下注成功之后的相关字段
     */
    private double price; //下注时的btc的价格
    private int ctcPrice;//下注的数量
    ArrayList<HashMap<String,String>> list_times = new ArrayList<HashMap<String,String>>();


    /**
     * 头部币种导航
     */
    private ArrayList<TextView> textViews = new ArrayList<TextView>();
    private ArrayList<HashMap<String,String>> coinType1 = new ArrayList<HashMap<String,String>>();

    float p = 0;
    String str = "";

    private String ctc1,rmb1;
    /**
     * 交易类型
     * 0---决战
     * 1---人民币
     * 2---α资产
     */
    private String payAccount;

    /**
     * 实盘ctc ，rmb数量
     *
     */
    private String ctc,rmb,ctcoption;


    private String code  =  "btc";


    IYWTribeService tribeService;


    /**
     * 币种
     */
    private String coinTypes = "比特币";


    private int time = 30;

    TextView tv;

    /**
     * 下注金额的几个定值
     * 三个：α资产
     */
    private ArrayList<String> money1 = new ArrayList<String>();

    /**
     * k线的类型
     */
    private ArrayList<String> kType = new ArrayList<String>();


    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x113){
                presener.getBtcPrice();
                presener.getCTC(shareOptionType);
                handler1.postDelayed(thread,1000);
            }else if (msg.what == 0x114){
                presener.getTrends(shareOptionType);
                handler1.postDelayed(thread1,time*1000);

            }else if (msg.what == 0x115){

                try{
                    long finalTime = 0;
                    String time = stampToDate(stamp);
                    long t = Long.parseLong(time.substring(time.length()-2,time.length()));

                    long t1 = 60*Integer.parseInt(type);

                    /**
                     * 三十秒之前，当前分钟结算，三十秒后，下一秒结算
                     */
//                if (t>=30){
//                    finalTime = t1-t+60;
//                }else{
//                    finalTime = t1-t;
//                }

                    /**
                     * 当前分钟剩余秒数+下一分钟
                     */
                    finalTime = t1 + 60 - t;
                    tv.setText((secToTime(finalTime)));
                    handler1.postDelayed(thread2,1000);
                }catch (Exception e){

                }

            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run(){
            handler1.sendEmptyMessage(0x113);
        }
    };
    Thread thread1 = new Thread(){
        @Override
        public void run(){
            handler1.sendEmptyMessage(0x114);
        }
    };
    Thread thread2 = new Thread(){
        @Override
        public void run(){
            handler1.sendEmptyMessage(0x115);
        }
    };

    public static String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;

//                java.text.DecimalFormat df = new java.text.DecimalFormat();
//                df.applyPattern("00");
//                timeStr = df.format(unitFormat(hour)) + ":" + df.format(unitFormat(minute)) + ":" + df.format(unitFormat(second));

                timeStr =unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_shareoption);
        presener = new ShareOptionPresener(this, this);
        init();
        setup();
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


    private void setup() {

        tvAdd100.setOnClickListener(this);
        tvAdd200.setOnClickListener(this);
        tvAdd500.setOnClickListener(this);
        btnBuyUp.setOnClickListener(this);
        btnBuyDown.setOnClickListener(this);
        tvK1m.setOnClickListener(this);
        tvK5m.setOnClickListener(this);
        tvK15m.setOnClickListener(this);
        tvCurrentDeal.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        btnDeal.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv15.setOnClickListener(this);
        tv30.setOnClickListener(this);
        tvDummy.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        tvExplain.setOnClickListener(this);






        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){
                    tvCurrentDeal.setTextColor(0xfffeb831);
                    tvOrder.setTextColor(0xff000000);
                    tvRecord.setTextColor(0xff000000);
                }else if (position == 1){
                    tvCurrentDeal.setTextColor(0xff000000);
                    tvOrder.setTextColor(0xfffeb831);
                    tvRecord.setTextColor(0xff000000);
                }else if (position == 2){
                    tvCurrentDeal.setTextColor(0xff000000);
                    tvOrder.setTextColor(0xff000000);
                    tvRecord.setTextColor(0xfffeb831);
                }
                Animation animation = new TranslateAnimation(one*currIndex, one*position, 0, 0);//显然这个比较简洁，只有一行代码。
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                ivLine.startAnimation(animation);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /**
         * 文本框变化的监听
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
                int price = 0;

                if (TextUtils.isEmpty(s.toString())){
                    price = Integer.parseInt("0");
                }else{
                    price = Integer.parseInt(s.toString());
                }

                BigDecimal bd5 = new BigDecimal(price+"");
                BigDecimal bd6 = new BigDecimal(p+"");
                BigDecimal bigDecimal2 = bd5.multiply(bd6).add(bd5);

                str ="+"+bigDecimal2+" 银豆";
                setTextViewStyle(tvProfit,str,str.length()-2);


            }
        });


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                webView.setVisibility(View.GONE);
                gifLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.setVisibility(View.GONE);
                gifLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {


        HttpConnect.post(this, "member_options_down_count", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    money1.add(data.optJSONArray("data").optJSONObject(0).optString("two"));
                    money1.add(data.optJSONArray("data").optJSONObject(0).optString("three"));
                    money1.add(data.optJSONArray("data").optJSONObject(0).optString("four"));
                    money1.add(data.optJSONArray("data").optJSONObject(0).optString("one"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 设置字体样式
                             */
                            setTextViewStyle(tvProfit,"0 银豆",1);
                            setTextViewStyle(tvAdd100,money1.get(0),money1.get(0).length()-2);
                            setTextViewStyle(tvAdd200,money1.get(1),money1.get(1).length()-2);
                            setTextViewStyle(tvAdd500,money1.get(2),money1.get(2).length()-2);
                            etMoney.setHint(money1.get(3).substring(0,money1.get(3).length()-2)+"~"+money1.get(2).substring(0,money1.get(2).length()-2));
                        }
                    });

                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });



        HttpConnect.post(this, "member_options_time_one", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    time = Integer.parseInt(data.optJSONArray("data").optJSONObject(0).optString("value"));
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });





        tv = (TextView) findViewById(R.id.tvtv);

        ivMore = (ImageView) findViewById(R.id.iv_more);
        gifLoading = (LoadingView) findViewById(R.id.cif_loading);
        gifLoading1 = (LoadingView) findViewById(R.id.cif_loading1);
        tvCurrentDeal = (TextView) findViewById(R.id.tv_current_deal);
        tvExplain = (FrameLayout) findViewById(R.id.tv_explain);
        iv_question_mark = (ImageView) findViewById(R.id.iv_question_mark);

        final RotateAnimation rotateAnimation =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(2000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);

        tvOrder = (TextView) findViewById(R.id.tv_order);
        tvRecord = (TextView) findViewById(R.id.tv_deal_record);
        tvAdd100 = (TextView) findViewById(R.id.tv_price_100);
        tvAdd200 = (TextView) findViewById(R.id.tv_price_200);
        tvAdd500 = (TextView) findViewById(R.id.tv_price_500);
        tvK1m = (TextView) findViewById(R.id.tv_1m);
        tvK5m = (TextView) findViewById(R.id.tv_5m);
        tvK15m = (TextView) findViewById(R.id.tv_15m);
        llCoinType = (LinearLayout) findViewById(R.id.ll_cointype);
        tvCurrentCtc = (TextView) findViewById(R.id.tv_current_ctc);
        tvProfit = (TextView) findViewById(R.id.tv_profit);
        btnBuyUp = (Button) findViewById(R.id.btn_buyup);
        btnBuyDown = (Button) findViewById(R.id.btn_buydown);
        btnDeal = (Button) findViewById(R.id.btn_deal);
        etMoney = (EditText) findViewById(R.id.et_price);
        vp = (MyViewPager) findViewById(R.id.vp_option);
        ivLine = (ImageView)findViewById(R.id.iv_line);
        tv1 = (TextView) findViewById(R.id.tv_1m_k);
        tv5 = (TextView) findViewById(R.id.tv_5m_k);
        tv15 = (TextView) findViewById(R.id.tv_15m_k);
        tv30 = (TextView) findViewById(R.id.tv_30m_k);
        tvInfo = (VerticalTextview) findViewById(R.id.tv_info);
        tvDummy = (TextView) findViewById(R.id.tv_dummy);
        webView = (WebView) findViewById(R.id.webview);

        ((HorizontalScrollView)findViewById(R.id.hsl_cointype)).setHorizontalScrollBarEnabled(false);

        tribeService = mIMKit.getTribeService();


        tvInfo.setText(12, 5, Color.GRAY);//设置属性,具体跟踪源码
        tvInfo.setTextStillTime(4000);//设置停留时长间隔
        tvInfo.setAnimTime(600);//设置进入和退出的时间间隔

        tvInfo.startAutoScroll();

        /**
         * 解决scrollview嵌套listview，listview不能滑动的问题
         */
        currentDealFragment = new CurrentDealFragment();
        currentDealFragment.setType(shareOptionType);
        currentDealFragment.setViewPager(vp);
        currentDealFragment.setTime(time);

        orderFragment = new OrderFragment();
        orderFragment.setType(shareOptionType);
        orderFragment.setViewPager(vp);
        orderFragment.setTime(time);

        dealRecordFragment = new DealRecordFragment();
        dealRecordFragment.setType(shareOptionType);
        dealRecordFragment.setViewPager(vp);
        dealRecordFragment.setTime(time);

        vpFragment.add(currentDealFragment);
        vpFragment.add(orderFragment);
        vpFragment.add(dealRecordFragment);


        vp.setNoScroll(true);
        vp.setAdapter(new TransactionAdapter(getSupportFragmentManager(),vpFragment));


        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = ivLine.getLayoutParams();
        params.width = width / 3;
        ivLine.setLayoutParams(params);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - ivLine.getWidth()) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        ivLine.setImageMatrix(matrix);
        // 设置动画初始位置

        one = offset * 2 + ivLine.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;

        setTextViews();

    }

    @Override
    public void initData() {

    }

    /**
     * 0---跌
     * 1---涨
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (TextUtils.isEmpty(etMoney.getText().toString())){
            money = 0;
        }else{
            money = Integer.parseInt(etMoney.getText().toString());
        }

        switch(v.getId()){
            case R.id.tv_explain:
                    startActivity(new Intent(getContext(), PalyExplainActivity.class));
                break;
            case R.id.tv_dummy:
                if (tvDummy.getText().toString().equals("切换模拟盘")){

                    HttpConnect.post(this, "member_reality_cash_ctc_in", null, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvDummy.setText("切换实盘");
                                        btnDeal.setBackgroundResource(R.mipmap.simulation);
                                        btnBuyDown.setBackgroundResource(R.mipmap.down);
                                        btnBuyUp.setBackgroundResource(R.mipmap.up);
                                        btnDeal.setEnabled(false);
                                        shareOptionType = "1";
                                        presener.getCTC(shareOptionType);
                                        currentDealFragment.setType(shareOptionType);
                                        dealRecordFragment.setType(shareOptionType);
                                        orderFragment.setType(shareOptionType);
                                        handler1.removeCallbacks(thread2);
                                        tv.setText("00:00");

                                    }
                                });
                            }

                        }
                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                        }
                    });


                }else{
                    tvDummy.setText("切换模拟盘");
                    btnDeal.setBackgroundResource(R.mipmap.reality);
                    btnBuyDown.setBackgroundResource(R.mipmap.down);
                    btnBuyUp.setBackgroundResource(R.mipmap.up);
                    btnDeal.setEnabled(false);
                    shareOptionType = "0";
                    presener.getCTC(shareOptionType);
                    currentDealFragment.setType(shareOptionType);
                    dealRecordFragment.setType(shareOptionType);
                    orderFragment.setType(shareOptionType);
                    handler1.removeCallbacks(thread2);
                    tv.setText("00:00");
                }

                break;
            case R.id.tv_price_100:
                tvAdd100.setTextColor(0xfffeb831);
                tvAdd200.setTextColor(0xff000000);
                tvAdd500.setTextColor(0xff000000);
                if (money1.size()>0){
                    setTextViewStyle1(tvAdd100,money1.get(0),money1.get(0).length()-2);
                    setTextViewStyle(tvAdd200,money1.get(1),money1.get(1).length()-2);
                    setTextViewStyle(tvAdd500,money1.get(2),money1.get(2).length()-2);
                    money=Integer.parseInt(money1.get(0).substring(0,money1.get(0).length()-2));
                    etMoney.setText(money+"");
                }

                break;
            case R.id.tv_price_200:
                tvAdd100.setTextColor(0xff000000);
                tvAdd200.setTextColor(0xfffeb831);
                tvAdd500.setTextColor(0xff000000);
                if (money1.size()>0){
                    setTextViewStyle(tvAdd100,money1.get(0),money1.get(0).length()-2);
                    setTextViewStyle1(tvAdd200,money1.get(1),money1.get(1).length()-2);
                    setTextViewStyle(tvAdd500,money1.get(2),money1.get(2).length()-2);
                    money=Integer.parseInt(money1.get(1).substring(0,money1.get(1).length()-2));
                    etMoney.setText(money+"");
                }

                break;
            case R.id.tv_price_500:

                tvAdd100.setTextColor(0xff000000);
                tvAdd200.setTextColor(0xff000000);
                tvAdd500.setTextColor(0xfffeb831);
                if (money1.size()>0){

                    setTextViewStyle(tvAdd100,money1.get(0),money1.get(0).length()-2);
                    setTextViewStyle(tvAdd200,money1.get(1),money1.get(1).length()-2);
                    setTextViewStyle1(tvAdd500,money1.get(2),money1.get(2).length()-2);
                    money=Integer.parseInt(money1.get(2).substring(0,money1.get(2).length()-2));
                    etMoney.setText(money+"");
                }

                break;
            case R.id.tv_1m_k:
                tv1.setTextColor(0xfffeb831);
                tv5.setTextColor(0xff000000);
                tv15.setTextColor(0xff000000);
                tv30.setTextColor(0xff000000);
                if(kType.size()>0){
                    presener.getBtcKLineUrl(kType.get(0),code);
                }

                break;
            case R.id.tv_5m_k:
                tv1.setTextColor(0xff000000);
                tv5.setTextColor(0xfffeb831);
                tv15.setTextColor(0xff000000);
                tv30.setTextColor(0xff000000);
                if(kType.size()>0){
                    presener.getBtcKLineUrl(kType.get(1),code);
                }
                break;
            case R.id.tv_15m_k:
                tv1.setTextColor(0xff000000);
                tv5.setTextColor(0xff000000);
                tv15.setTextColor(0xfffeb831);
                tv30.setTextColor(0xff000000);
                if(kType.size()>0){
                    presener.getBtcKLineUrl(kType.get(2),code);
                }
                break;
            case R.id.tv_30m_k:
                tv1.setTextColor(0xff000000);
                tv5.setTextColor(0xff000000);
                tv15.setTextColor(0xff000000);
                tv30.setTextColor(0xfffeb831);
                if(kType.size()>0){
                    presener.getBtcKLineUrl(kType.get(3),code);
                }
                break;
            case R.id.btn_buyup:
                btnBuyUp.setBackgroundResource(R.mipmap.uped);
                btnBuyDown.setBackgroundResource(R.mipmap.down);

                if (shareOptionType.equals("0")){
                    btnDeal.setBackgroundResource(R.mipmap.realityed);
                }else{
                    btnDeal.setBackgroundResource(R.mipmap.simulationed);
                }
                btnDeal.setEnabled(true);
                state = "1";

                handler1.removeCallbacks(thread2);
                handler1.post(thread2);
                break;
            case R.id.btn_buydown:
                btnBuyUp.setBackgroundResource(R.mipmap.up);
                btnBuyDown.setBackgroundResource(R.mipmap.downed);
                if (shareOptionType.equals("0")){
                    btnDeal.setBackgroundResource(R.mipmap.realityed);
                }else{
                    btnDeal.setBackgroundResource(R.mipmap.simulationed);
                }
                btnDeal.setEnabled(true);
                state = "0";

                handler1.removeCallbacks(thread2);
                handler1.post(thread2);
                break;
            case R.id.tv_1m:
                tvK1m.setBackgroundResource(R.mipmap.type_bg_ed);
                tvK5m.setBackgroundResource(R.mipmap.type_bg);
                tvK15m.setBackgroundResource(R.mipmap.type_bg);
                tvK1m.setTextColor(0xffffffff);
                tvK5m.setTextColor(0xff000000);
                tvK15m.setTextColor(0xff000000);

                if (list_times.size()>0){
                    type = list_times.get(0).get("type");
                    type1 = "1";
                    p = Float.parseFloat(list_times.get(0).get("value"));

                    BigDecimal bd1 = new BigDecimal(money+"");
                    BigDecimal bd2 = new BigDecimal(p+"");
                    BigDecimal bigDecimal = bd1.multiply(bd2).add(bd1);

                    str ="+"+bigDecimal+" 银豆";
                    setTextViewStyle(tvProfit,str,str.length()-2);
                }

                break;
            case R.id.tv_5m:
                tvK1m.setBackgroundResource(R.mipmap.type_bg);
                tvK5m.setBackgroundResource(R.mipmap.type_bg_ed);
                tvK15m.setBackgroundResource(R.mipmap.type_bg);
                tvK1m.setTextColor(0xff000000);
                tvK5m.setTextColor(0xffffffff);
                tvK15m.setTextColor(0xff000000);

                if (list_times.size()>0){
                    type1 = "2";
                    type = list_times.get(1).get("type");
                    p = Float.parseFloat(list_times.get(1).get("value"));

                    BigDecimal bd3 = new BigDecimal(money+"");
                    BigDecimal bd4 = new BigDecimal(p+"");
                    BigDecimal bigDecimal1 = bd3.multiply(bd4).add(bd3);

                    str ="+"+bigDecimal1+" 银豆";
                    setTextViewStyle(tvProfit,str,str.length()-2);
                }



                break;
            case R.id.tv_15m:
                tvK1m.setBackgroundResource(R.mipmap.type_bg);
                tvK5m.setBackgroundResource(R.mipmap.type_bg);
                tvK15m.setBackgroundResource(R.mipmap.type_bg_ed);
                tvK1m.setTextColor(0xff000000);
                tvK5m.setTextColor(0xff000000);
                tvK15m.setTextColor(0xffffffff);
                if (list_times.size()>0){
                    type1 = "3";
                    type = list_times.get(2).get("type");
                    p = Float.parseFloat(list_times.get(2).get("value"));

                    BigDecimal bd5 = new BigDecimal(money+"");
                    BigDecimal bd6 = new BigDecimal(p+"");
                    BigDecimal bigDecimal2 = bd5.multiply(bd6).add(bd5);

                    str ="+"+bigDecimal2+" 银豆";
                    setTextViewStyle(tvProfit,str,str.length()-2);
                }


                break;
            case R.id.tv_current_deal:
                currIndex = 0;
                vp.setCurrentItem(currIndex);
                orderFragment.setType(shareOptionType);
                currentDealFragment.setListViewHeightBasedOnChildren(currentDealFragment.getLvDeal());
                break;
            case R.id.tv_order:
                currIndex = 1;
                vp.setCurrentItem(currIndex);
                orderFragment.setType(shareOptionType);
                orderFragment.setListViewHeightBasedOnChildren(orderFragment.getLvDeal());
                break;
            case R.id.tv_deal_record:
                currIndex = 2;
                vp.setCurrentItem(currIndex);
                orderFragment.setType(shareOptionType);
                dealRecordFragment.getHandler().post(dealRecordFragment.getThread());
//                dealRecordFragment.setListViewHeightBasedOnChildren(dealRecordFragment.getLvDeal());
                break;

            case R.id.btn_deal:

                if (TextUtils.isEmpty(etMoney.getText().toString())){
                    sToast("请输入金额");
                    return;
                }
                if (state.equals("3")){
                    sToast("请选择买入方向");

                }else{
                    payAccount = "0";
                    btnDeal.setClickable(false);
                    presener.optionDeal(stampToDate(stamp),payAccount,shareOptionType,type1,state,etMoney.getText().toString(),currentPrice+"",coinTypes);
                }
                break;
            case R.id.iv_more:

                startActivity(new Intent(ShareOptionActivity.this, ShareOptionConfigActivity.class).putExtra("type",shareOptionType));
//                gifLoading1.setVisibility(View.VISIBLE);
//                ivMore.setClickable(false);
//                HttpConnect.post(this, "member_im_tribe", null, new Callback() {
//                    @Override
//                    public void onResponse(Response arg0) throws IOException {
//                        String json = arg0.body().string();
//                        JSONObject data = JSONObject.fromObject(json);
//                        if (data.optString("status").equals("success")){
//                            final String tribeid = data.optJSONArray("data").optJSONObject(0).optString("tribeid");
//                            final String masterid = data.optJSONArray("data").optJSONObject(0).optString("masterid");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    if (!LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum().equals(masterid)){
//                                        tribeService.joinTribe(new IWxCallback() {
//                                            @Override
//                                            public void onSuccess(Object... objects) {
//                                                startActivity(
//                                                        new Intent(ShareOptionActivity.this, ShareOptionConfigActivity.class)
//                                                                .putExtra("tribeid",tribeid)
//                                                                .putExtra("masterid",masterid)
//                                                                .putExtra("type",shareOptionType)
//                                                                .putExtra("ctc",ctc1)
//                                                                .putExtra("rmb",rmb1));
//
//                                            }
//
//                                            @Override
//                                            public void onError(int i, String s) {
//                                                if (i ==6)
//                                                {
//                                                   startActivity(
//                                                            new Intent(ShareOptionActivity.this, ShareOptionConfigActivity.class)
//                                                                    .putExtra("tribeid",tribeid)
//                                                                    .putExtra("masterid",masterid)
//                                                                    .putExtra("type",shareOptionType)
//                                                                    .putExtra("ctc",ctc1)
//                                                                    .putExtra("rmb",rmb1));
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onProgress(int i) {
////                                                sToast("加入中");
//                                            }
//                                        }, Long.parseLong(tribeid));
//                                    }
////                                    else{
////                                        getActivity().startActivity(
////                                                new Intent(getActivity(), ShareOptionConfigActivity.class)
////                                                        .putExtra("tribeid",tribeid)
////                                                        .putExtra("masterid",masterid)
////                                                        .putExtra("type",shareOptionType)
////                                                        .putExtra("ctc",ctc1)
////                                                        .putExtra("rmb",rmb1));
////
////                                    }
//
//                                    gifLoading1.setVisibility(View.GONE);
//                                }
//                            });
//
//                        }else{
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ivMore.setClickable(true);
//                                    gifLoading1.setVisibility(View.GONE);
//                                }
//                            });
//
//                        }
//
//                    }
//                    @Override
//                    public void onFailure(Request arg0, IOException arg1) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ivMore.setClickable(true);
//                                gifLoading1.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                });

                break;

        }
    }




    @Override
    public void onResume() {
        super.onResume();
        ivMore.setClickable(true);
        presener.getTimes(typeCoin);
        presener.getCoinTypes();
        presener.getCTC(shareOptionType);

        handler1.post(thread);
        handler1.post(thread1);


    }


    @Override
    public void onStart() {
        super.onStart();
//        currentDealFragment.setListViewHeightBasedOnChildren(currentDealFragment.getLvDeal());
    }

    @Override
    public void onPause() {
        super.onPause();
//        handler1.removeCallbacks(thread);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tvInfo.stopAutoScroll();
        handler1.removeCallbacks(thread1);
        handler1.removeCallbacks(thread);
        handler1.removeCallbacks(thread2);
    }


    @Override
    public void getBtcKLineUrl(String url) {

        Log.i("url",url);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(
                new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
        gifLoading.setVisibility(View.GONE);
    }


    @Override
    public void fail( String msg) {
//        sToast(msg);

        btnDeal.setClickable(true);
    }

    @Override
    public void getBtcPrice(double price ,String stamp1) {
//        sToast(price+"");
        currentPrice = price;
        stamp = stamp1;
    }

    @Override
    public void getTimes(ArrayList<HashMap<String,String>> times) {
        list_times.clear();
        list_times.addAll(times);
        type = list_times.get(0).get("type");

        tvK1m.setText(times.get(0).get("time")+"   "+removeDecimalPoint(Double.parseDouble(times.get(0).get("value"))));
        tvK5m.setText(times.get(1).get("time")+"   "+removeDecimalPoint(Double.parseDouble(times.get(1).get("value"))));
        tvK15m.setText(times.get(2).get("time")+"   "+removeDecimalPoint(Double.parseDouble(times.get(2).get("value"))));

        if (type.equals(list_times.get(0).get("type"))){
            p = Float.parseFloat(list_times.get(0).get("value"));
        }else if (type.equals(list_times.get(1).get("type"))){
            p = Float.parseFloat(list_times.get(1).get("value"));
        }else if (type.equals(list_times.get(2).get("type"))){
            p = Float.parseFloat(list_times.get(2).get("value"));
        }




    }


    public void setTextViews(){
        HttpConnect.post(this, "member_option_k_type", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                   final JSONArray array = data.optJSONArray("data");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (array.size()>0){

                                tv1.setText(array.optJSONObject(0).optString("name"));
                                tv5.setText(array.optJSONObject(1).optString("name"));
                                tv15.setText(array.optJSONObject(2).optString("name"));

                                kType.add(array.optJSONObject(0).optString("value"));
                                kType.add(array.optJSONObject(1).optString("value"));
                                kType.add(array.optJSONObject(2).optString("value"));

                                if (array.size() == 3)


                                {
                                    tv30.setVisibility(View.GONE);
                                }else if (array.size() == 4){
                                    tv30.setVisibility(View.VISIBLE);
                                    tv30.setText(array.optJSONObject(3).optString("name"));
                                    kType.add(array.optJSONObject(3).optString("value"));
                                }

                                presener.getBtcKLineUrl(kType.get( 0),code);
                            }

                        }

                    });

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivMore.setClickable(true);
                            gifLoading1.setVisibility(View.GONE);
                        }
                    });

                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivMore.setClickable(true);
                        gifLoading1.setVisibility(View.GONE);
                    }
                });
            }
        });
    }


    @Override
    public void getCoinTypes(ArrayList<HashMap<String,String>> coinTypes1) {
        coinType1.clear();
        textViews.clear();
        llCoinType.removeAllViews();
        coinType1.addAll(coinTypes1);
        for (int i=0;i<coinTypes1.size();i++){
            Log.i("coinType"+i,coinTypes1.get(i).get("code"));
            TextView tv = new TextView(this);
            tv.setText(coinTypes1.get(i).get("cointype"));
            tv.setTextColor(0xff000000);
            tv.setPadding(10,10,20,10);

            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(16f);
            textViews.add(tv);
            llCoinType.addView(tv);
        }

        textViews.get(0).setTextColor(0xfffda135);

        for (int k = 0;k<coinType1.size();k++){
            final int finalK = k;
            textViews.get(k).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0;i<coinType1.size();i++){
                        if (coinType1.get(i).get("cointype").equals(textViews.get(finalK).getText().toString())){
                            textViews.get(i).setTextColor(0xfffeb831);
                            code = coinType1.get(i).get("code");
                            presener.getBtcKLineUrl("1",code);
                            coinTypes = textViews.get(i).getText().toString();
                        }else{
                            textViews.get(i).setTextColor(0xff000000);
                        }
                    }


                }
            });
        }
    }

    @Override
    public void getTrends(ArrayList<HashMap<String, String>> trends) {

        ArrayList<String> list_trends = new ArrayList<String>();
        String str = "";
        for (int i = 0; i<trends.size() ; i++){
            str = trends.get(i).get("time")+"  "+trends.get(i).get("name")+"盈利"+trends.get(i).get("profit");
            list_trends.add(str);
        }

        tvInfo.setTextList(list_trends);//加入显示内容,集合类型
    }

    @Override
    public void getCTC(String ctc2 ,String monry2,String ctcoption2) {

        ctc = ctc2;
        rmb  = monry2;
        ctcoption = ctcoption2;

        String str = "";

        if (TextUtils.isEmpty(ctcoption2)){
            str = "0 银豆";
        }else{
            str = ctcoption+" 银豆";
        }
        setTextViewStyle(tvCurrentCtc,str,str.length()-2);

        if (shareOptionType.equals("0")){
            ctc1 = ctc2;
            rmb1 = monry2;
        }

    }

    @Override
    public void optionDeal() {
        sToast("下单成功");

        presener.getCTC(shareOptionType);
        currentDealFragment.setType(shareOptionType);
        dealRecordFragment.setType(shareOptionType);
        orderFragment.setType(shareOptionType);

        orderFragment.setIsFrist(1);
        currentDealFragment.setIsFrist(1);
        dealRecordFragment.setIsFrist(1);
        price = currentPrice;
        ctcPrice = Integer.parseInt(etMoney.getText().toString());

        if (shareOptionType.equals("0")){
            btnDeal.setBackgroundResource(R.mipmap.reality);
        }else{
            btnDeal.setBackgroundResource(R.mipmap.simulation);
        }
        //失去焦点
        btnDeal.setEnabled(false);
        btnDeal.setClickable(true);
        btnBuyDown.setBackgroundResource(R.mipmap.down);
        btnBuyUp.setBackgroundResource(R.mipmap.up);

        String rise = "";
        if (state == "1"){
            rise = "up";
        }else{
            rise = "down";
        }

        stamp = Long.parseLong(stamp)+(Integer.parseInt(type)*60)+"";

        orderFragment.getHandler().post(orderFragment.getThread());

        webView.loadUrl("JavaScript:addmarkLinedata("+currentPrice+",'"+stampToDate(stamp)+"','"+rise+"')");

        handler1.removeCallbacks(thread2);
        tv.setText("00:00");
    }

    private void setTextViewStyle(TextView mTextView , String str , int i){
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style0), 0, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style1), i, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextView.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    private void setTextViewStyle1(TextView mTextView , String str , int i){
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style2), 0, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style3), i, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextView.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    /**
     * 分数转换百分数
     * @param str
     * @return
     */
    public String  removeDecimalPoint(double str)
    {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(0);
        String str1 = nf.format(str);

        return str1;
    }


    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s){
        String res = "";
        if (!TextUtils.isEmpty(s)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s);
            Date date = new Date(lt*1000);
            res = simpleDateFormat.format(date);
        }

        return res;
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                gifLoading.setVisibility(View.GONE);
            } else {
                if (gifLoading.getVisibility() == View.GONE)
                    gifLoading.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }

    }


    public void back(View view){
        finish();
    }
}
