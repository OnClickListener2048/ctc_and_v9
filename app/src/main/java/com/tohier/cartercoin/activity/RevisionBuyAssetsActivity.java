package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.RevisionAssetsAdapter;
import com.tohier.cartercoin.bean.RevisionAssetsBean;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.MyDealListView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.listener.MyItemOnclick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RevisionBuyAssetsActivity extends MyBaseActivity implements View.OnClickListener {

    private RelativeLayout ll_wx,ll_alipay,ll_money;
    private LinearLayout ll_coupon,ll_money1;
    private ImageView iv_ed,iv_ed1,iv_ed2;
    private TextView tv_total_price,tv_asstes_record,tv_coupon_price;
    private MyDealListView lv_data1,lv_data2;
    private RevisionAssetsAdapter adapter;
    //    private Assets1Adapter adapter1;
    PopupWindow window = null;
    private View view;

    private LoadingView gifLoading;
    private NoDataView ivNodata;
    private NoLinkView ivNoLink;


    private ArrayList<RevisionAssetsBean> list = new ArrayList<RevisionAssetsBean>();
    //    private ArrayList<Assets> list1 = new ArrayList<Assets>();
    private SharedPreferences sharedPreferences;

    private int index = 0;

    /**
     * 资产包类型
     */
    private String type;
    /**
     * 可用余额
     */
    private double price = 0;

    /**
     * 优惠券的id
     */
    private String id = "0";

    /**
     * 支付类型   0---余额 1---支付宝 2---微信
     */
    private String payModel = "2";
    private String pid;

    private TextView tv_notice;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1) {
                adapter.notifyDataSetChanged();
                //每隔1毫秒更新一次界面，如果只需要精确到秒的倒计时此处改成1000即可
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_buyassets_layout);

        init();
        setUp();
        getNotice();
    }


    private void init() {
        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("购买矿产包");
        ((TextView)view.findViewById(R.id.tv_prompt)).setVisibility(View.GONE);
        ((ImageView)view.findViewById(R.id.iv_prompt)).setVisibility(View.VISIBLE);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        sharedPreferences = getSharedPreferences("contact",0);

        ll_wx = (RelativeLayout) findViewById(R.id.ll_wx);

        ll_wx.setFocusable(true);
        ll_wx.setFocusableInTouchMode(true);
        ll_wx.requestFocus();
        ll_wx.requestFocusFromTouch();

        ll_alipay = (RelativeLayout) findViewById(R.id.ll_alipay);
        ll_money = (RelativeLayout) findViewById(R.id.ll_money);
        ll_coupon = (LinearLayout) findViewById(R.id.ll_coupon);
        ll_money1 = (LinearLayout) findViewById(R.id.ll_money1);
        iv_ed = (ImageView) findViewById(R.id.iv_ed);
        iv_ed1 = (ImageView) findViewById(R.id.iv_ed1);
        iv_ed2 = (ImageView) findViewById(R.id.iv_ed2);

        tv_asstes_record = (TextView) findViewById(R.id.tv_asstes_record);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_price = (TextView) findViewById(R.id.tv_coupon_price);
        lv_data1 = (MyDealListView) findViewById(R.id.lv_data1);
        lv_data2 = (MyDealListView) findViewById(R.id.lv_data2);

        gifLoading = (LoadingView) findViewById(R.id.cif_loading);
        ivNodata = (NoDataView) findViewById(R.id.iv_nodata);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);


        lv_data1.setSelector(new ColorDrawable(Color.TRANSPARENT));

        tv_notice = (TextView) this.findViewById(R.id.tv_notice);
//        adapter1 = new Assets1Adapter(this,list1);
//        lv_data2.setAdapter(adapter1);

        adapter = new RevisionAssetsAdapter(RevisionBuyAssetsActivity.this,list);
        lv_data1.setAdapter(adapter);
        adapter.setMyItemOnClick(new MyItemOnclick() {
            @Override
            public void myItemOnClick(int position,View v) {
                show(position, list,id);
            }
        });

        getMoney1();

    }

    private void setUp() {
        ll_wx.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_coupon.setOnClickListener(this);
        tv_asstes_record.setOnClickListener(this);

//        adapter1.setMyItemOnClick(new MyItemOnclick() {
//            @Override
//            public void myItemOnClick(int position) {
//                if (payModel.equalsIgnoreCase("0")){
//                    show(position,list1,"0");
//                }else  if (payModel.equalsIgnoreCase("1")){
//                    alipayAssets(list1.get(position).getId(),"0");
//                }else  if (payModel.equalsIgnoreCase("2")){
//                    wechatAssets(list1.get(position).getId(),"0");
//                }
//
//            }
//        });
//        adapter1.setMyItemOnClick(new MyItemOnclick() {
//            @Override
//            public void myItemOnClick(int position) {
//                if (payModel.equalsIgnoreCase("0")){
//                    show(position,list1,"0");
//                }else  if (payModel.equalsIgnoreCase("1")){
//                    alipayAssets(list1.get(position).getId(),"0");
//                }else  if (payModel.equalsIgnoreCase("2")){
//                    wechatAssets(list1.get(position).getId(),"0");
//                }
//
//            }
//        });


        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(RevisionBuyAssetsActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    if (sharedPreferences.getString("isresume","0").equals("0")){
//                        getData1();
                        if (payModel.equals("2") && index != 0){
                            getData(0);
                        }else{
                            getData(1);
                            index = 1;
                        }

                    }else{
                        sharedPreferences.edit().putString("isresume","0").commit();
                    }
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 升级资产包
     * @param type  会员类型
     * @param paymode   0---余额 1---支付宝 2---微信
     */
    private void buyAssets(final String type, String paymode, String couponId){
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",type);
        map.put("paymode",paymode);
        map.put("conponsid",couponId);
        HttpConnect.post(this, "member_assets", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    getMoney1();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("购买资产包成功");
                            id =  "0";
                            tv_coupon_price.setText("-￥0");
                            getData(0);
//                            getData1();
                            startActivity(new Intent(RevisionBuyAssetsActivity.this,BuyAssetsRecordActivity.class).putExtra("type","3"));
                        }
                    });

                }else{
                    if (!TextUtils.isEmpty(data.optString("msg"))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast(data.optString("msg"));
                            }
                        });

                    }

                }

            }
            @Override
            public void onFailure(final Request arg0, IOException arg1) {
                if (!"".equals(arg0.toString())){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast(arg0.toString());
                        }
                    });
                }
            }
        });

    }

    /**
     * 支付宝支付
     * @param type
     */
    private void alipayAssets(final String type , final String id, final View v){
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",type);
        map.put("paymode",payModel);
        map.put("conponsid",id);
        HttpConnect.post(RevisionBuyAssetsActivity.this, "member_assets", map, new Callback() {
            @Override
            public void onResponse(final Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
                    Intent intent = new Intent(RevisionBuyAssetsActivity.this, ZhifubaozhifuActivity.class);
                    startActivity(intent);
                }else{
                    if (!"".equals(data.optString("msg"))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast(data.optString("msg"));
                            }
                        });

                    }

                }

            }
            @Override
            public void onFailure(final Request arg0, IOException arg1) {
                if (!"".equals(arg0.toString())){


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast(arg0.toString());
                        }
                    });
                }

            }
        });
    }

    /**
     * 微信支付
     * @param type
     */
    private void wechatAssets(final String type , final String id, final View v){
        final HashMap<String,String> map = new HashMap<String,String>();
        map.put("type",type);
        map.put("paymode",payModel);
        map.put("conponsid",id);
        HttpConnect.post(RevisionBuyAssetsActivity.this, "member_assets", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    pid = data.optJSONArray("data").optJSONObject(0).optString("ID");
                    //获取签名 并完成支付
                    getWechatPaySign(pid,"3");
                }else{
                    if (!"".equals(data.optString("msg"))){
                        sToast(data.optString("msg"));
                    }
                }

            }
            @Override
            public void onFailure(final Request arg0, IOException arg1) {
                if (!"".equals(arg0.toString())){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast(arg0.toString());
                        }
                    });

                }
            }
        });
    }

    /**
     * 显示取消支付的提示框
     *
     */
    public void show(final int position, final ArrayList<RevisionAssetsBean> list , final String id1)
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

                try{
                    if (payModel.equalsIgnoreCase("0")){
                        buyAssets(list.get(position).getId(),payModel,id1);
                    }else  if (payModel.equalsIgnoreCase("1")){
                        alipayAssets(list.get(position).getId(),id1,v);
                    }else  if (payModel.equalsIgnoreCase("2")){
                        wechatAssets(list.get(position).getId(),id1,v);
                    }
                    tv_coupon_price.setText("-￥0");
                    id = "0";
                    getData(0);
                    window.dismiss();

                }catch (Exception e){
                    sToast("购买资产包失败，请重试！");
                    window.dismiss();
                }
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
            window.showAtLocation(lv_data1,
                    Gravity.BOTTOM, 0, 0);
        }
    }

    private void getMoney1(){
        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    price = Double.parseDouble( data.optJSONArray("data").optJSONObject(0).optString("money"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv_total_price.setText("￥"+price);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_total_price.setText("￥0.00");
                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    private void getNotice(){
        HttpConnect.post(this, "member_assets_pay_time", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                   final String title = data.optJSONArray("data").optJSONObject(0).optString("title");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                          if(!TextUtils.isEmpty(title))
                          {
                              tv_notice.setText(title);
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

    @Override
    public void initData() {

    }

    public void back(View view){
        finish();
    }


    private void getData(final int i){
        list.clear();
        if (i == 1){
            gifLoading.setVisibility(View.VISIBLE);
        }

        HttpConnect.post(this, "member_assets_list_v2", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONArray array = data.optJSONArray("data");
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject object2 = array.getJSONObject(i);
                                String Name = object2.optString("Name");
                                String pic = object2.optString("pic");
                                String id = object2.optString("id");
                                String Type  = object2.optString("Type");   // 0 秒杀 1 限购
                                String Gold  =  object2.optString("Gold");
                                String Silver  = object2.optString("Silver");
                                String Price = object2.optString("Price");
                                String ReductionPrice = object2.optString("ReductionPrice");
                                String Qty = object2.optString("Qty");   //一共多少
                                String BeginDate = object2.optString("BeginDate");
                                String EndDate = object2.optString("EndDate");
                                String status = object2.optString("status");
                                String cou = object2.optString("cou");    //买了多少
                                String pre = object2.optString("pre");   //剩余多少
                                String pro = object2.optString("pro");   //剩余多少
                                Date date = new Date();
                                SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//等价于
                                String currentTime = myFmt2.format(date);
                                long isStart = DateDistance.getBuyAssetsTimeIsStart(currentTime,BeginDate);
                                RevisionAssetsBean assets = new RevisionAssetsBean(id,Name,Type,Gold,Silver,Price,ReductionPrice,Qty,BeginDate,EndDate,status,cou,pre,pic,pro,false,isStart);
                                list.add(assets);
                            }

                            ArrayList<RevisionAssetsBean> list1 = new ArrayList<RevisionAssetsBean>();
                            list1.addAll(list);
                                    adapter.setList(list1);
                                    adapter.notifyDataSetChanged();
                            if(i==1 && !payModel.equals("1"))
                            {
                                adapter.start();//开始倒计时
                                handler.sendEmptyMessageDelayed(1,1000);
                            }
                            gifLoading.setVisibility(View.GONE);
                            if (list1.size()>0){
                                ivNodata.setVisibility(View.GONE);
                            }else{
                                ivNodata.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            gifLoading.setVisibility(View.GONE);
                            if (list.size()>0){
                                ivNodata.setVisibility(View.GONE);
                            }else{
                                ivNodata.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gifLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
//    private void getData1(){
//        list1.clear();
//        HttpConnect.post(this, "member_assets_type_one", null, new Callback() {
//            @Override
//            public void onResponse(Response arg0) throws IOException {
//                String json = arg0.body().string();
//                final JSONObject data = JSONObject.fromObject(json);
//                if (data.optString("status").equals("success")){
//                    final JSONArray array = data.optJSONArray("data");
//                        JSONObject object2 = array.getJSONObject(0);
//                        String id1 = object2.optString("id");
//                        String introduce  = object2.optString("introduce");
//                        String name  =  object2.optString("name");
//                        String value  = object2.optString("value");
//                        String price = object2.optString("price");
//                        Assets assets = new Assets(id1,introduce,name,value,price);
//                        assets.setDesc(object2.optString("intro"));
//                        list1.add(assets);
//
//
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (list1.get(0).getValue().equalsIgnoreCase("0")){
//                                list1.clear();
//                            }
//                            adapter1.setList(list1);
//                            adapter1.notifyDataSetChanged();
//
//                        }
//                    });
//                }
//
//            }
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//            }
//        });
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_asstes_record:
                startActivity(new Intent(this,BuyAssetsRecordActivity.class).putExtra("type","3"));
                break;
            case R.id.ll_wx:
                payModel = "2";
                iv_ed.setVisibility(View.VISIBLE);
                iv_ed1.setVisibility(View.GONE);
                iv_ed2.setVisibility(View.GONE);
                ll_money1.setVisibility(View.GONE);
                break;
            case R.id.ll_alipay:
                payModel = "1";
                iv_ed.setVisibility(View.GONE);
                iv_ed1.setVisibility(View.VISIBLE);
                iv_ed2.setVisibility(View.GONE);
                ll_money1.setVisibility(View.GONE);
                break;
            case R.id.ll_money:
                payModel = "0";
                iv_ed.setVisibility(View.GONE);
                iv_ed1.setVisibility(View.GONE);
                iv_ed2.setVisibility(View.VISIBLE);
                ll_money1.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_coupon:
                startActivityForResult(new Intent(this,CouponActivity.class).putExtra("type","5"),1);
                id = "0";
                getData(0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                tv_coupon_price.setText("-￥"+data.getStringExtra("price"));
//                tvTotalPrice.setText("￥"+(Double.parseDouble(tvVipPrice.getText().toString()) -Double.parseDouble(data.getStringExtra("price"))));
                id = data.getStringExtra("id");
                if (!id.equalsIgnoreCase("0")){
                    double min = Double.parseDouble(data.getStringExtra("min"));
                    for (int i = 0;i < list.size();i++){
                        if (Double.parseDouble(list.get(i).getReductionPrice()) >= min ){

                            BigDecimal bd1 = new BigDecimal(list.get(i).getReductionPrice());
                            BigDecimal bd2 = new BigDecimal(data.getStringExtra("price"));

                            list.get(i).setReductionPrice(bd1.subtract(bd2)+"");
                        }
                    }

                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }else{
                    getData(0);
                }
                break;
        }
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
                    MyNetworkConnection.getNetworkConnection(RevisionBuyAssetsActivity.this).post("post", wxpPayUrl, map, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

                                        RequestMsg msg = new RequestMsg();
                                        msg.setTokenId(tokenid);
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId("wx7ad749f6cba84064");
                                        PayPlugin.unifiedAppPay(RevisionBuyAssetsActivity.this, msg);
                                    }
                                });
                            }else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

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

                        }
                    });
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null!=adapter)
        {
            List<Handler> handlerList= adapter.getHandlerList();
            List<Thread> threadList= adapter.getThreadList();
            for(int i = 0 ; i < threadList.size() ; i ++)
            {
               Handler handler = handlerList.get(i);
               handler.removeCallbacks(threadList.get(i));
            }
            adapter.getThreadList().clear();
            adapter.getHandlerList().clear();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Tools.getAPNType(RevisionBuyAssetsActivity.this) == true) {
            ivNoLink.setVisibility(View.GONE);
            if (sharedPreferences.getString("isresume", "0").equals("0")) {
//                getData1();
                if (payModel.equals("2") && index != 0) {
                    getData(0);
                } else {
                    getData(1);
                    index = 1;
                }

            } else {
                sharedPreferences.edit().putString("isresume", "0").commit();
            }
        } else {
            ivNoLink.setVisibility(View.VISIBLE);
        }

    }
}