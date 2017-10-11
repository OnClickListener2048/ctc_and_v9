package com.tohier.cartercoin.transactionfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.DealExplainActivity;
import com.tohier.cartercoin.activity.RechargeRMBActivity;
import com.tohier.cartercoin.activity.TransactionActivity;
import com.tohier.cartercoin.adapter.MyTransactionAdapter;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.MyDealListView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.presenter.EntrustPresenter;
import com.tohier.cartercoin.ui.EntrustView;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

import static com.tohier.cartercoin.R.id.cif_loading1;


public class DealFragment extends BaseFragment implements EntrustView , View.OnClickListener{

//    private int count = 8;
    public static int time = 1;
    float sum = 0;
    Thread thread2 = new Thread(){
        @Override
        public void run(){
            handler1.sendEmptyMessage(0x113);
        }
    };
    private View view;
    private TextView tvCNY,tvCTC,tvPrive;
    private MyDealListView listView_transaction;
    private ScrollView scrollView;
    private LoadingView gifLoading,gifLoading1; //加载圈
    private WebView webView;
    private TextView tvAccount,tvBuy,tvSellTotal,tvCommit,tvTotal;
    private EditText tvPrice;
    private TextView tvCurrentPrice;
    private ImageView ivType,iv;
    private FrameLayout tvDealExplain;
    private ImageView iv_question_mark;
    private GifImageView gifUp,ivXia;
    private FrameLayout ivUpOrDown;
    private EditText etPrice,etCount;
    private LinearLayout llHide,ll;
    private AnimationSet mShowAnim,HiddenAmin;
    private String price;
    private String count1;

    private SharedPreferences sharedPreferences;

    /**
     * 当前价格
     */
    private String newprice;
    /**
     * 实时交易数据
     */
    private ArrayList<Transaction> data = new ArrayList<Transaction>();
    private MyTransactionAdapter adapter;
    private EntrustPresenter presenter;
    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 0x113){
                presenter.getTradingNow();
                presenter.getCTCInfo();
                handler1.postDelayed(thread2,time*1000);
            }
        }
    };
    /**
     * 交易数据
     */
    private ArrayList<Transaction> list = new ArrayList<Transaction>();
    /**
     * true  显示操作区
     * false  隐藏
     */
//    private boolean isVisible = false;
    /**
     * 1 --- 买
     * 2---卖
     */
    private int type = 1;
    /**
     * 复投账户余额
     */
    private String rich = "0";
    /**
     * 人民币账户
     */
    private String cny = "0";
    /**
     * α账户账户
     */
    private String ctc = "0";
    /**
     * 可卖出数量
     */
    private String oncecansell = "0";
    private String cansell = "0";
    /**
     * 卖一价
     */
    private String sellprice = "0";
    /**
     * 买一价
     */
    private String buyprice = "0";
    /**
     * 可买入的数量
     */
    private double min,max;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deal, container, false);
        init();
        setup();
        adapter = new MyTransactionAdapter(((TransactionActivity)getActivity()),data,0);
        listView_transaction.setAdapter(adapter);

        presenter = new EntrustPresenter(this,(TransactionActivity)getActivity());
        presenter.getData();
        presenter.getInfoAndRate();
        presenter.getPriceRangeAndCount();
        if (sharedPreferences.getInt("loading",0) == 0){
            gifLoading1.setVisibility(View.VISIBLE);
            sharedPreferences.edit().putInt("loading",1).commit();
        }
        handler1.post(thread2);

        return view;
    }

    private void setup() {

        ivUpOrDown.setOnClickListener(this);
        ivType.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        tvDealExplain.setOnClickListener(this);

        listView_transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    String state = data.get(position).getState().substring(0,1);
                    if (state.equals("买")){
                       type = 2;
                        tvPrice.setText(data.get(position).getPrice());
                    }else if (state.equals("卖")){
                        type = 1;
                        etPrice.setText(data.get(position).getPrice());
                    }

                    setInitView(type);
                }

            }
        });

        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    DecimalFormat df   = new DecimalFormat("#0.00");

                    if (TextUtils.isEmpty(s.toString())){
                        s = "0";
                    }

                    String buyPrice1 = "";

                    if (type == 1){
                        if (TextUtils.isEmpty(etPrice.getText().toString())){
                            buyPrice1 = "0";
                        }else{
                            buyPrice1 = etPrice.getText().toString();
                        }
                        tvSellTotal.setText(df.format(Double.parseDouble(s.toString())*Double.parseDouble(buyPrice1))+"");
                    }else{

                        if (TextUtils.isEmpty(tvPrice.getText().toString())){
                            buyPrice1 = "0";
                        }else{
                            buyPrice1 = tvPrice.getText().toString();
                        }
                        tvSellTotal.setText(df.format(Double.parseDouble(s.toString())*Double.parseDouble(buyPrice1))+"");
                    }
                }catch (Exception e){
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DecimalFormat df   = new DecimalFormat("#0.00");

                try{
                    if (TextUtils.isEmpty(s.toString())){
                        s = "0";
                        etCount.setHint("");
                    }else{
                        if (s.toString().equals("0") || s.toString().equals("0.") ){
                            etCount.setHint("");
                        }else{
                            if (type == 1){
                                etCount.setHint(df.format( Double.parseDouble(rich)/Double.parseDouble(s.toString())));
                            }else{
                                etCount.setHint("");
                            }
                        }
                    }
                }catch (Exception e){

                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /**
         * 只能输入四位小数
         */
        etPrice.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4)
                {
                    edt.delete(posDot + 5, posDot + 6);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });

      /**
         * 只能输入四位小数
         */
        tvPrice.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4)
                {
                    edt.delete(posDot + 5, posDot + 6);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });

    }

    /**
     *
     * 初始化
     */
    private void init() {
        sharedPreferences = getActivity().getSharedPreferences("loading",0);
        HttpConnect.post((TransactionActivity) getActivity(), "member_time", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    time = Integer.parseInt(data.optJSONArray("data").optJSONObject(0).optString("time"));
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });


        getData();


        etPrice = (EditText) view.findViewById(R.id.et_price1);
        etCount = (EditText) view.findViewById(R.id.et_count);
        tvAccount = (TextView) view.findViewById(R.id.tv_account);
        tvBuy = (TextView) view.findViewById(R.id.tv_buy);
        tvCommit = (TextView) view.findViewById(R.id.tv_commit);
        tvCurrentPrice = (TextView) view.findViewById(R.id.tv_current_price);
        tvSellTotal = (TextView) view.findViewById(R.id.tv_sell_total);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        iv = (ImageView) view.findViewById(R.id.iv);
        tvDealExplain = (FrameLayout) view.findViewById(R.id.tv_deal_explain);
        iv_question_mark = (ImageView) view.findViewById(R.id.iv_question_mark);

        final RotateAnimation rotateAnimation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(2000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);

        ivType = (ImageView) view.findViewById(R.id.iv_type);
        ivUpOrDown = (FrameLayout) view.findViewById(R.id.iv_upordown);
        llHide = (LinearLayout) view.findViewById(R.id.ll_hide);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        ivXia = (GifImageView) view.findViewById(R.id.iv_xia);
        gifUp = (GifImageView) view.findViewById(R.id.gif_up);
        tvPrice = (EditText) view.findViewById(R.id.tv_price);


        webView = (WebView) view.findViewById(R.id.webview);
        gifLoading = (LoadingView) view.findViewById(R.id.cif_loading);
        gifLoading1 = (LoadingView) view.findViewById(cif_loading1);
        tvCNY = (TextView) view.findViewById(R.id.tv_cny);
        tvCTC = (TextView) view.findViewById(R.id.tv_ctc);
        tvPrive = (TextView) view.findViewById(R.id.tv_new_price);
        listView_transaction = (MyDealListView) view.findViewById(R.id.lv_deal);

        scrollView = (ScrollView) view.findViewById(R.id.slv_deal);


        //控件隐藏的动画
        mShowAnim = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.up_out);


//        //控件显示的动画
//         HiddenAmin= (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.down_in);
//
//        HiddenAmin.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                llHide.setVisibility(View.GONE);
//                ivXia.setVisibility(View.GONE);
//                gifUp.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });


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


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler1.removeCallbacks(thread2);
    }

    @Override
    public void initData() {

    }
    

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.tv_deal_explain:
                startActivity(new Intent(getActivity(), DealExplainActivity.class));
                break;
            case R.id.tv_commit:

                if(type == 1){
                    if (TextUtils.isEmpty(etPrice.getText().toString())){
                        sToast("请输入买入价格");
                        return;
                    }
                    if (etPrice.getText().toString().indexOf(".") == 0){
                        sToast("输入价格不合法");
                        return;
                    }
                }else if (type == 2) {
                    if (TextUtils.isEmpty(tvPrice.getText().toString())) {
                        sToast("请输入卖出价格");
                        return;
                    }
                    if (etPrice.getText().toString().indexOf(".") == 0) {
                        sToast("输入价格不合法");
                        return;
                    }
                }

                if (TextUtils.isEmpty(etCount.getText().toString())){
                    sToast("请输入数量");
                    return;
                }

                if (etCount.getText().toString().indexOf(".") == 0) {
                    sToast("输入数量不合法");
                    return;
                }
                gifLoading1.setVisibility(View.VISIBLE);
                tvCommit.setClickable(false);
                if (type == 1){
                    presenter.getTradingAdd("0",etCount.getText().toString(),etPrice.getText().toString());
                }else if (type == 2){
                    presenter.getTradingAdd("1",etCount.getText().toString(),tvPrice.getText().toString());
                }

                break;

            case R.id.tv_buy:
                startActivity(new Intent(getActivity(), RechargeRMBActivity.class).putExtra("type","1"));
                break;

            case R.id.iv_upordown:
//                if (isVisible==false){
//                    ll.startAnimation(mShowAnim);
//                    llHide.setVisibility(View.VISIBLE);
//                    isVisible = true;
//                    ivXia.setVisibility(View.VISIBLE);
//                    gifUp.setVisibility(View.GONE);
//                }else{
//                    ll.startAnimation(HiddenAmin);
////                    llHide.setVisibility(View.GONE);
//                    isVisible = false;
////                    ivXia.setVisibility(View.GONE);
////                    gifUp.setVisibility(View.VISIBLE);
//                }
                getData();

                break;
            case R.id.iv_type:
                if (type == 1){
                    type = 2;
                    ivType.setImageResource(R.mipmap.maichu);
                }else if (type == 2){
                    type = 1;
                    ivType.setImageResource(R.mipmap.mairu);
                }
//                if (isVisible==false){
//                    ll.startAnimation(mShowAnim);
//                }
//                isVisible = true;
//                llHide.setVisibility(View.VISIBLE);
//                ivXia.setVisibility(View.VISIBLE);
//                gifUp.setVisibility(View.GONE);
                presenter.getInfoAndRate();
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        listView_transaction.setFocusable(false);
        scrollView.smoothScrollTo(0,0);
        presenter.getTradingNow();
//        if (isVisible==true){
//            llHide.setVisibility(View.VISIBLE);
//            ivXia.setVisibility(View.VISIBLE);
//            gifUp.setVisibility(View.GONE);
//        }else{
//            llHide.setVisibility(View.GONE);
//            ivXia.setVisibility(View.GONE);
//            gifUp.setVisibility(View.VISIBLE);
//        }
        getData();
    }


    @Override
    public void getKLineInfo(String url) {
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

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
    public void fail(String msg) {
//        sToast(msg);

        gifLoading1.setVisibility(View.GONE);
        tvCommit.setClickable(true);

    }
    @Override
    public void getTradingSuccess() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sToast("挂单成功");
                presenter.getInfoAndRate();
                etCount.setText("");
                etCount.setHint("");
                tvSellTotal.setText("");
                gifLoading1.setVisibility(View.GONE);
                tvCommit.setClickable(true);

            }
        });

    }


    @Override
    public void getTrading(ArrayList<Transaction> list) {
        data.clear();
        data.addAll(list);
        adapter.notifyDataSetChanged();
        gifLoading1.setVisibility(View.GONE);

    }

    @Override
    public void getIngoAndRate(HashMap<String, String> map) {
        String rate = map.get("rate");
        ctc = map.get("ctc");
        cny = map.get("money");
        rich = map.get("rich");

        tvCTC.setText("α："+ctc);
        tvCNY.setText("￥："+cny);
        tvPrive.setText("当前价格："+rate+"CNY");

        getData();
    }

    @Override
    public void isUpdateInfo(final String flag) {

    }

    @Override
    public void getCTCInfo(HashMap<String, String> map) {
        newprice = map.get("newprice");
        tvCurrentPrice.setText("当前价:"+map.get("newprice"));
    }

    @Override
    public void getPriceRangeAndCount(HashMap<String, String> map) {
        min = Double.parseDouble(map.get("min"));
        max = Double.parseDouble(map.get("max"));

    }

    private void getData(){

        HttpConnect.post((TransactionActivity) getActivity(), "member_trade_now_price_sell", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    oncecansell = data.optJSONArray("data").optJSONObject(0).optString("oncecansell");
                    cansell = data.optJSONArray("data").optJSONObject(0).optString("cansell");
                    sellprice = data.optJSONArray("data").optJSONObject(0).optString("sellprice");
                    buyprice = data.optJSONArray("data").optJSONObject(0).optString("buyprice");

                    try{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setInitView(type);
                            }
                        });
                    }catch (Exception e){}


                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    private void setInitView(int type){

        if (type == 1){//买入
            ivType.setImageResource(R.mipmap.mairu);
            tvAccount.setText("现金账户："+cny);
            iv.setVisibility(View.VISIBLE);
            tvBuy.setVisibility(View.GONE);
            tvTotal.setText("买入总价：");
            etPrice.setVisibility(View.VISIBLE);
            etCount.setText("");
            etCount.setHint("");
            tvPrice.setVisibility(View.GONE);
            tvCommit.setText("买入");
        }else if (type == 2){//卖出
            ivType.setImageResource(R.mipmap.maichu);
            tvAccount.setText("可卖α资产："+cansell);
            iv.setVisibility(View.GONE);
            tvBuy.setVisibility(View.GONE);
//            etCount.setHint("单次最多可卖"+oncecansell);
            tvCommit.setText("卖出");
            tvTotal.setText("卖出总价：");
//            tvPrice.setText(sellprice);
            etCount.setText("");
            etPrice.setVisibility(View.GONE);
            tvPrice.setVisibility(View.VISIBLE);

        }
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

}
