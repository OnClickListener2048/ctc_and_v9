package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.Address;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/21.
 */

public class BuildOrderActivity extends MyBaseActivity implements View.OnClickListener{

    public static boolean isFirst = true;

    /**
     * 优惠券的id
     */
    private String id = "0";

    private SharedPreferences sp;

    /***
     * 优惠券
     */
   private String couponPrice = "0";
    private LinearLayout llAddress,llGoods,llCoupon,llInfo,llAddress1;
    private TextView tvReceiptPerson,tvReceiptNum,tvReceiptAddress,
            tvInfo,tvOrderNum,tvGoodsName,tvGoodsPrice,tvGoodsCount,tvCouponPrice,tvBuyCount,tvTotal,tvImmediatelyBuy;

    private ImageView ivGoodsPic,ivReduce,ivAdd;
    private int buyCount = 1;

    private Intent intent;
    private String pic,name,price,desc,goodsId;

    private ToggleButton btnYuE,btnXiaoFei;
    private TextView tvYuE,tvXiaoFei;
    private String xiaofei;
    private String payType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order);

        init();
        setUp();

    }



    private void init() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        pic = intent.getStringExtra("pic");
        price = intent.getStringExtra("ctcprice");
        buyCount = Integer.parseInt(intent.getStringExtra("count"));


        desc = intent.getStringExtra("desc");
        goodsId  = intent.getStringExtra("id");


        sp = getSharedPreferences("address",0);

        btnYuE = (ToggleButton) findViewById(R.id.btn_yue);
        btnXiaoFei = (ToggleButton) findViewById(R.id.btn_xiaofei);
        tvYuE = (TextView) findViewById(R.id.tv_yue);
        tvXiaoFei = (TextView) findViewById(R.id.tv_xiaofei);
        llAddress1 = (LinearLayout) findViewById(R.id.ll_address1);
        llAddress = (LinearLayout) findViewById(R.id.ll_address);
        llGoods = (LinearLayout) findViewById(R.id.ll_goods);
        llCoupon = (LinearLayout) findViewById(R.id.ll_coupon);
        tvReceiptPerson = (TextView) findViewById(R.id.tv_shouhuoName);
        tvReceiptNum = (TextView) findViewById(R.id.tv_shouhuoPhone);
        tvReceiptAddress = (TextView) findViewById(R.id.tv_shouhuoAddress);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvOrderNum = (TextView) findViewById(R.id.tv_ordernum);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
        tvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
        tvCouponPrice = (TextView) findViewById(R.id.tv_coupon_price);
        tvBuyCount = (TextView) findViewById(R.id.tv_buy_count);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvImmediatelyBuy = (TextView) findViewById(R.id.tv_immediately_buy);
        ivGoodsPic = (ImageView) findViewById(R.id.iv_goods_pic);
        ivReduce = (ImageView) findViewById(R.id.iv_reduce);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        llInfo = (LinearLayout) findViewById(R.id.ll_info);


        Glide.with(this).load(pic).placeholder(null)
                .error(null).into(ivGoodsPic);
        tvOrderNum.setText(name);
        tvGoodsName.setText(name);
        tvGoodsPrice.setText(price);
        tvGoodsCount.setText("x"+buyCount);
        tvBuyCount.setText(buyCount+"");
        tvTotal.setText(intent.getStringExtra("price"));


        HttpConnect.post(this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equalsIgnoreCase("success")) {
                    final String yue = data.optJSONArray("data").optJSONObject(0).optString("ctc");
                    xiaofei = data.optJSONArray("data").optJSONObject(0).optString("ctccous");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tvYuE.setText(yue+"α");
                            tvXiaoFei.setText(xiaofei+"个");
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvYuE.setText(0.0000+"α");
                            tvXiaoFei.setText(0.0000+"α");
                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        getJsonAddressData();


    }


    private void setUp() {
        llAddress.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llGoods.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivReduce.setOnClickListener(this);
        tvImmediatelyBuy.setOnClickListener(this);

        btnYuE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payType = "0";
                    btnXiaoFei.setChecked(false);
                }else{
                    payType = "1";
                    btnXiaoFei.setChecked(true);
                }
            }
        });

        btnXiaoFei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    payType = "1";
                    btnYuE.setChecked(false);
                }else{
                    payType = "0";
                    btnYuE.setChecked(true);
                }
            }
        });
    }

    @Override
    public void initData() {

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirst == true){
            getJsonAddressData();
        }else {
            try {
                if (sp.getString("name1", "").equals("")) {
                    llInfo.setVisibility(View.GONE);
//                                tvReceiptAddress.setVisibility(View.GONE);
                    llAddress1.setVisibility(View.GONE);
                    tvInfo.setVisibility(View.VISIBLE);
                } else {
                    llInfo.setVisibility(View.VISIBLE);
                    llAddress1.setVisibility(View.VISIBLE);
                    tvInfo.setVisibility(View.GONE);
                    tvReceiptPerson.setText("收货人: " + sp.getString("name1", ""));
                    tvReceiptNum.setText(sp.getString("phone", ""));
                    tvReceiptAddress.setText("收货地址: " + sp.getString("address", ""));
                }

            } catch (Exception e) {
                llInfo.setVisibility(View.GONE);
//                                tvReceiptAddress.setVisibility(View.GONE);
                llAddress1.setVisibility(View.GONE);
                tvInfo.setVisibility(View.VISIBLE);
            }
        }

    }



    public void back (View view){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_address:
                startActivity(new Intent(this,ShouHuoAddressActivity.class).putExtra("shopType","1"));
                isFirst = false;
                break;
            case R.id.ll_goods:
                startActivity(new Intent(this, GoodsDetialsActivity.class).putExtra("id",goodsId));
                break;
            case R.id.ll_coupon:

                BigDecimal bd5 = new BigDecimal(tvGoodsPrice.getText().toString().substring(0,tvGoodsPrice.getText().toString().length()));
                BigDecimal bd6 = new BigDecimal(tvGoodsCount.getText().toString().substring(1,tvGoodsCount.getText().toString().length()));

                tvTotal.setText(bd5.multiply(bd6)+"");

                startActivityForResult(new Intent(this,CouponActivity.class).putExtra("type","1"),1);
                break;
            case R.id.iv_add:
                buyCount+=1;
                tvBuyCount.setText(buyCount+"");
                tvGoodsCount.setText("x"+buyCount);

                BigDecimal bd1 = new BigDecimal(tvGoodsPrice.getText().toString().substring(0,tvGoodsPrice.getText().toString().length()));
                BigDecimal bd2 = new BigDecimal(buyCount);
                BigDecimal couponprice = new BigDecimal(couponPrice);

                tvTotal.setText(bd1.multiply(bd2).subtract(couponprice)+"");
                break;
            case R.id.iv_reduce:
                if (buyCount>1){
                    buyCount-=1;
                }else{
                    sToast("商品数量不能再少了");
                }

                tvBuyCount.setText(buyCount+"");
                tvGoodsCount.setText("x"+buyCount);
                BigDecimal bd3 = new BigDecimal(tvGoodsPrice.getText().toString().substring(0,tvGoodsPrice.getText().toString().length()));
                BigDecimal bd4 = new BigDecimal(buyCount);
                BigDecimal couponprice1 = new BigDecimal(couponPrice);
                tvTotal.setText(bd3.multiply(bd4).subtract(couponprice1)+"");
                break;
            case R.id.tv_immediately_buy:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvImmediatelyBuy.setClickable(false);
                    }
                });
                if (TextUtils.isEmpty(tvReceiptPerson.getText().toString())){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("请选择地址");
                            tvImmediatelyBuy.setClickable(true);
                        }
                    });
                    return;
                }

                if (payType.equals("1")){
                    if (Double.parseDouble(xiaofei)<Double.parseDouble(tvTotal.getText().toString())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast("金豆不足");
                                tvImmediatelyBuy.setClickable(true);
                            }
                        });

                        return;
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("请选择支付方式");
                            tvImmediatelyBuy.setClickable(true);
                        }
                    });

                    return;
                }

                HashMap<String,String> par = new HashMap<String,String>();
                par.put("pid",goodsId);
                par.put("qty",buyCount+"");
                par.put("name",tvReceiptPerson.getText().toString().split(" ")[1]);
                try{
                    par.put("address",tvReceiptAddress.getText().toString().split(" ")[1]);
                }catch (Exception e){
                    par.put("address","");
                }
                par.put("phonenumber",tvReceiptNum.getText().toString());
                par.put("paymode",payType);
                par.put("id",id);
                HttpConnect.post(this, "member_order_buy", par, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvImmediatelyBuy.setClickable(true);
                                }
                            });
                            startActivity(new Intent(BuildOrderActivity.this,ConFirmActivity.class)
                            .putExtra("pid",data.optJSONArray("data").optJSONObject(0).optString("ID"))
                                    .putExtra("price",tvTotal.getText().toString()));
                            finish();

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvImmediatelyBuy.setClickable(true);
                                    sToast(data.optString("msg"));
                                }
                            });

                        }

                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvImmediatelyBuy.setClickable(true);
                            }
                        });
                    }
                });

                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                if (requestCode == 0){
//                    llInfo.setVisibility(View.VISIBLE);
//                    llAddress1.setVisibility(View.VISIBLE);
//                    tvInfo.setVisibility(View.GONE);
//                    tvReceiptPerson.setText("收货人: "+data.getStringExtra("name"));
//                    tvReceiptNum.setText(data.getStringExtra("phone"));
//                    tvReceiptAddress.setText("收货地址: "+data.getStringExtra("address"));
                }else if (requestCode == 1){
                    couponPrice = data.getStringExtra("price");
                    tvCouponPrice.setText(couponPrice+" 金豆");

                    BigDecimal bd3 = new BigDecimal(tvTotal.getText().toString());
                    BigDecimal couponprice1 = new BigDecimal(couponPrice);
                    if (bd3.subtract(couponprice1).intValue() >=0){
                        tvTotal.setText(bd3.subtract(couponprice1)+"");
                    }else{
                        tvTotal.setText("0.00");
                    }


                    id = data.getStringExtra("id");
                }

                break;
        }
    }


    public void getJsonAddressData() {
        // TODO Auto-generated method stub
//		Map<String, String> par = new HashMap<String, String>();
//
//		String id = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId();
//		par.put("id", id);

//		mZProgressHUD.show();

        HttpConnect.post(this, "member_address_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final ArrayList<Address> list = new ArrayList<Address>();

                JSONObject jsonObject = JSONObject.fromObject(arg0.body()
                        .string());
                if (jsonObject.getString("status").equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (jsonObject2 != null) {
                                final String card_id = jsonObject2.getString("id");
                                final String consignee = jsonObject2.getString("consignee");
                                final String address = jsonObject2.getString("address");
                                final String weChat = jsonObject2.getString("wechat");
                                final String mobile = jsonObject2.getString("mobile");
                                final String province = jsonObject2.getString("province");
                                final String city = jsonObject2.getString("city");
                                final String default_address = jsonObject2.getString("default");
                                Address addressObj = new Address(consignee,address,province,city,weChat,mobile,card_id,default_address);
                                list.add(addressObj);
                            }
                        }
                        BuildOrderActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
							if (list.size()>0){
                                llInfo.setVisibility(View.VISIBLE);
//                                tvReceiptAddress.setVisibility(View.VISIBLE);
                                llAddress1.setVisibility(View.VISIBLE);
                                tvInfo.setVisibility(View.GONE);
                                tvReceiptPerson.setText("收货人: "+list.get(0).getConsignee());
                                tvReceiptNum.setText(list.get(0).getMobile());
                                tvReceiptAddress.setText("收货地址: "+list.get(0).getAddress());
                            }else{
                                llInfo.setVisibility(View.GONE);
//                                tvReceiptAddress.setVisibility(View.GONE);
                                llAddress1.setVisibility(View.GONE);
                                tvInfo.setVisibility(View.VISIBLE);
                            }
                            }
                        });
                    }
                }else{
                    BuildOrderActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            llInfo.setVisibility(View.GONE);
                            llAddress1.setVisibility(View.GONE);
                            tvInfo.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

    }
}
