package com.tohier.cartercoin.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.shape.BaseLightShape;

/**
 * Created by Administrator on 2016/11/9.
 */
public class TwoMActivity extends MyBaseActivity {
    ;
    private SharedPreferences sharedpreferences;
    private int x = 1;

    private HighLight mHightLight;

    /**
     *  用户头像
     */
    private CircleImageView circleImageView;

    /**
     *  用户昵称
     */
    private TextView tv_nickname;

    /**
     *  会员级别图标
     */
    private ImageView iv_member_type_icon;

    /**
     *  会员级别
     */
    private TextView tv_member_level;
    /**
     *  进入设置
     */
    private TextView tv_setting;
    /**
     *  显示会员总资产
     */
    private TextView tv_all_assets;
    /**
     *  显示α资产当前价格
     */
    private TextView tv_ctc_current_price;

    //进入我的账单
    private LinearLayout linearLayout_into_mybill;
    //进入我的订单
    private LinearLayout linearLayout_into_myorder;
    //进入我的银行卡
    private LinearLayout linearLayout_into_bandcard;
    //进入收货地址
    private LinearLayout linearLayout_into_addaddress;
    //进入安全中心
    private LinearLayout linearLayout_into_security_center;
    //进入实名认证
    private LinearLayout linearLayout_authentication;
    //进入我的宝粉
    private LinearLayout linearLayout_into_myfans;
    //进入个人中心
    private LinearLayout linearLayout_into_myinfo;
    //进入资产信息
    private LinearLayout linearLayout_into_account_info;
    //进入矿产包
    private LinearLayout linearLayout_into_assetspackage;
    //进入VIP
    private LinearLayout linearLayout_into_vipupgrade;
    //进入交易中心
    private LinearLayout linearLayout_into_translate;
    //进入VIP特权
    private LinearLayout linearLayout_into_member_privilege;
    //进入提现
    private LinearLayout linearLayout_into_tixian;

    //福利
    private LinearLayout linearLayout_into_share_bonus;
    //新手指引
    private LinearLayout linearLayout_into_xin;

    private LoadingView load_view;

    private View member_privilege_divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_twome_layout);
        initData();
        setUpView();
        //第一次进来onHiddenChanged 不执行
//        if (sharedpreferences.getInt("isload",1) != 1){
//           load_view.setGravity(View.VISIBLE);
//        }else{
//            sharedpreferences.edit().putInt("isload",0).commit();
//        }

        loadMemberInfo();
        getMemberTotalAssets();
        getMemberExchangerate();
    }


    @Override
    public void initData() {
        sharedpreferences = getSharedPreferences("isShowWelcomeLayout", Context.MODE_PRIVATE);

        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        iv_member_type_icon = (ImageView) findViewById(R.id.iv_member_type_icon);
        tv_member_level = (TextView) findViewById(R.id.tv_member_level);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_all_assets = (TextView) findViewById(R.id.tv_all_assets);
        tv_ctc_current_price = (TextView) findViewById(R.id.tv_ctc_current_price);
        linearLayout_into_mybill = (LinearLayout) findViewById(R.id.linearLayout_into_mybill);
        linearLayout_into_myorder = (LinearLayout) findViewById(R.id.linearLayout_into_myorder);
        linearLayout_into_bandcard = (LinearLayout) findViewById(R.id.linearLayout_into_bandcard);
        linearLayout_into_addaddress = (LinearLayout) findViewById(R.id.linearLayout_into_addaddress);
        linearLayout_into_security_center = (LinearLayout) findViewById(R.id.linearLayout_into_security_center);
        linearLayout_authentication = (LinearLayout) findViewById(R.id.linearLayout_authentication);
        linearLayout_into_myfans = (LinearLayout) findViewById(R.id.linearLayout_into_myfans);
        linearLayout_into_myinfo = (LinearLayout) findViewById(R.id.linearLayout_into_myinfo);
        linearLayout_into_account_info = (LinearLayout) findViewById(R.id.linearLayout_into_account_info);
        linearLayout_into_assetspackage = (LinearLayout) findViewById(R.id.linearLayout_into_assetspackage);
        linearLayout_into_vipupgrade = (LinearLayout) findViewById(R.id.linearLayout_into_vipupgrade);
        linearLayout_into_translate = (LinearLayout) findViewById(R.id.linearLayout_into_translate);
        linearLayout_into_member_privilege = (LinearLayout) findViewById(R.id.linearLayout_into_member_privilege);
        linearLayout_into_tixian = (LinearLayout) findViewById(R.id.linearLayout_into_tixian);
        linearLayout_into_share_bonus = (LinearLayout) findViewById(R.id.linearLayout_into_share_bonus);
        linearLayout_into_xin = (LinearLayout) findViewById(R.id.linearLayout_into_xin);
        load_view = (LoadingView) findViewById(R.id.load_view);
        load_view.setGravity(View.VISIBLE);

        member_privilege_divider = findViewById(R.id.member_privilege_divider);
    }

    private void setUpView() {
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, NewSettingActivity.class));

            }
        });

        linearLayout_into_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnect.post(TwoMActivity.this, "member_is_password_pay", null, new Callback() {

                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        JSONObject data = JSONObject.fromObject(arg0.body().string());
                        if (data.get("status").equals("success")) {
                            String value = data.getJSONArray("data").getJSONObject(0).getString("value");

                            if(!TextUtils.isEmpty(value)&&value.equals("1")) //存在支付密码  要修改支付密码
                            {

                                TwoMActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(TwoMActivity.this, TiXianActivity.class));
                                    }
                                });
                            }else             //不存在需要设置
                            {
                                TwoMActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sToast("请设置交易密码");
                                        startActivity(new Intent(TwoMActivity.this, SettingPayPwdActivity.class));
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {

                    }
                });
            }
        });


        HttpConnect.post(TwoMActivity.this, "member_hide_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String assets = data.getJSONArray("data").getJSONObject(0).getString("assets");
                    final String news = data.getJSONArray("data").getJSONObject(0).getString("new");
                    final String bao = data.getJSONArray("data").getJSONObject(0).getString("bao");
                    final String upgrade = data.getJSONArray("data").getJSONObject(0).getString("upgrade");
                    String options = data.getJSONArray("data").getJSONObject(0).getString("options");


                    TwoMActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(assets)&&assets.equals("0"))
                            {
                                linearLayout_into_assetspackage.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(bao)&&bao.equals("0"))
                            {
                                linearLayout_into_myfans.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(upgrade)&&upgrade.equals("0"))
                            {
                                linearLayout_into_vipupgrade.setVisibility(View.VISIBLE);
                                linearLayout_into_member_privilege.setVisibility(View.VISIBLE);
                                member_privilege_divider.setVisibility(View.VISIBLE);
                            }
                            if(!TextUtils.isEmpty(news)&&news.equals("0"))
                            {
                                linearLayout_into_xin.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        /**
         * 福利
         */
        linearLayout_into_share_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, ShareBonusActivity.class));
            }
        });

        /**
         * 福利
         */
        linearLayout_into_xin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this,NoviceActivity .class));
            }
        });

        linearLayout_into_mybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, BillListActivity.class));
            }
        });

        linearLayout_into_myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, MyOrderActivity.class));
            }
        });

        linearLayout_into_bandcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, BankListActivity.class));
            }
        });

        linearLayout_into_addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, ShouHuoAddressActivity.class));
            }
        });

        linearLayout_into_security_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, SecurityCenterActivity.class));
            }
        });
        linearLayout_into_account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, RevisionAccountInfoActivity2.class).putExtra("ismain",false));
            }
        });

        linearLayout_into_assetspackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, RevisionBuyAssetsActivity.class));
            }
        });
        linearLayout_into_vipupgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnect.post(TwoMActivity.this, "member_buy_upgrade_count", null, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")){

                            TwoMActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  String[] textStrings = new String[]{data.optJSONArray("data").optJSONObject(0).optString("one"),
                                            data.optJSONArray("data").optJSONObject(0).optString("two"),
                                            data.optJSONArray("data").optJSONObject(0).optString("three"),
                                            data.optJSONArray("data").optJSONObject(0).optString("four"),
                                            data.optJSONArray("data").optJSONObject(0).optString("five"),
                                            data.optJSONArray("data").optJSONObject(0).optString("six"),
                                            data.optJSONArray("data").optJSONObject(0).optString("severn"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eight"),
                                            data.optJSONArray("data").optJSONObject(0).optString("nine"),
                                            data.optJSONArray("data").optJSONObject(0).optString("ten"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eleven"),
                                            data.optJSONArray("data").optJSONObject(0).optString("twelve"),};
                                    Intent intent = new Intent(TwoMActivity.this, VipUpgradeActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putStringArray("textStrings", textStrings);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                    }
                });
            }
        });

        linearLayout_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_authentication.setClickable(false);
                HttpConnect.post(TwoMActivity.this, "member_info", null, new Callback() {

                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        JSONObject data = JSONObject.fromObject(arg0.body().string());
                        if (data.get("status").equals("success")) {
                            final String name = data.getJSONArray("data").getJSONObject(0).getString("name");
                            final String idnumber = data.getJSONArray("data").getJSONObject(0).getString("idnumber");
                            final String picup = data.optJSONArray("data").optJSONObject(0).optString("picup");
                            final String picdown = data.optJSONArray("data").optJSONObject(0).optString("picdown");

                            Handler dataHandler = new Handler(getContext()
                                    .getMainLooper()) {

                                @Override
                                public void handleMessage(final Message msg) {
                                    linearLayout_authentication.setClickable(true);
                                    if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(idnumber)&&!TextUtils.isEmpty(picup)&&!TextUtils.isEmpty(picdown))
                                    {
                                        startActivity(new Intent(TwoMActivity.this,RealNameAuthenticationActivity2.class));
                                    }else
                                    {
                                        startActivity(new Intent(TwoMActivity.this,RealNameAuthenticationActivity.class));
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
                                    if(!TextUtils.isEmpty(msg8))
                                    {
                                        if(!Tools.isPhonticName(msg8))
                                        {
                                            sToast(msg8);
                                        }
                                    }
                                    linearLayout_authentication.setClickable(true);
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
                                linearLayout_authentication.setClickable(true);
                                sToast("网络质量不佳，请检查网络！");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                });
            }
        });

        linearLayout_into_myfans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, StudentExchangeContributionActivity.class));
            }
        });

        linearLayout_into_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, MeInfoActivity.class));
            }
        });

        linearLayout_into_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, NewTransactionActivity.class));
            }
        });

        linearLayout_into_member_privilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TwoMActivity.this, MemberPrivilegeActivity.class));
            }
        });



        ViewTreeObserver vto = findViewById(R.id.ll_me1).getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (x == 1 && sharedpreferences.getBoolean("isShow2",true)==true){
//                    showNextKnownTipView();

                    x = 2;

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //需要刷新的地方
        //会员信息
        loadMemberInfo();
        //资产资产 总资产
        getMemberTotalAssets();
        //α资产当日价格
        getMemberExchangerate();
    }

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        HttpConnect.post(TwoMActivity.this, "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String pic = data.getJSONArray("data").getJSONObject(0).getString("pic");
                    final String nickname = data.getJSONArray("data").getJSONObject(0).getString("nickname");
                    final String type = data.getJSONArray("data").getJSONObject(0).getString("type");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            LoginUser.getInstantiation(TwoMActivity.this.getApplicationContext()).setName1(nickname);
                            LoginUser.getInstantiation(TwoMActivity.this.getApplicationContext()).setHeadUrl(pic);
                            LoginUser.getInstantiation(TwoMActivity.this.getApplicationContext()).setType1(type);

                            if (pic!=null&&!pic.equals("")){
                                Glide.with(TwoMActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(circleImageView) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(TwoMActivity.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        circleImageView.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                            }else
                            {
                                circleImageView.setImageResource(R.mipmap.iv_member_default_head_img);
                            }

                            if(!TextUtils.isEmpty(nickname)&&!nickname.equals(""))
                            {
                                tv_nickname.setText(nickname);
                            }else
                            {
                                tv_nickname.setText("无名英雄");
                            }

                            if(!TextUtils.isEmpty(type))
                            {
                                if(type.equals("10"))
                                {
                                    tv_member_level.setText("注册会员");
                                    iv_member_type_icon.setImageResource(R.mipmap.iv_putong_memeber);
//                                    linearLayout_two_level.setVisibility(View.GONE);
//                                    linearLayout_three_level.setVisibility(View.GONE);
                                }else if(type.equals("20"))
                                {
                                    tv_member_level.setText("铂金VIP会员");
                                    iv_member_type_icon.setImageResource(R.mipmap.iv_baijin_member);
//                                    linearLayout_two_level.setVisibility(View.VISIBLE);
//                                    linearLayout_three_level.setVisibility(View.GONE);
                                }else if(type.equals("30"))
                                {
                                    tv_member_level.setText("钻石VIP会员");
                                    iv_member_type_icon.setImageResource(R.mipmap.iv_zhuanshi_member);
//                                    linearLayout_two_level.setVisibility(View.VISIBLE);
//                                    linearLayout_three_level.setVisibility(View.VISIBLE);
                                }else if(type.equals("40"))
                                {
                                    tv_member_level.setText("皇冠VIP会员");
                                    iv_member_type_icon.setImageResource(R.mipmap.iv_huangguan_member);
//                                    linearLayout_two_level.setVisibility(View.VISIBLE);
//                                    linearLayout_three_level.setVisibility(View.VISIBLE);
                               }

                                load_view.setGravity(View.GONE);
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
                            load_view.setGravity(View.GONE);

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
                        load_view.setGravity(View.GONE);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 获取当前会员的资产信息
     */
    public void getMemberTotalAssets() {
        HttpConnect.post(TwoMActivity.this, "member_count_detial", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String allssets = data.optJSONArray("data").optJSONObject(0).optString("allmoney");
                    TwoMActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_all_assets.setText(allssets);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                TwoMActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        sToast("请检查您的网络连接状态");
                    }
                });
            }
        });
    }

    /**
     * 获取α资产当日价格
     */
    public void getMemberExchangerate() {
        HttpConnect.post(TwoMActivity.this, "member_rate", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String rate = data.getJSONArray("data").getJSONObject(0).getString("rate");
                    TwoMActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_ctc_current_price.setText(rate);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                TwoMActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                      sToast("请检查您的网络连接状态");
                    }
                });
            }
        });
    }


    public  void showNextKnownTipView()
    {
        MainActivity.tabWidget.setVisibility(View.GONE);
        MainActivity.view.setVisibility(View.GONE);
            mHightLight = new HighLight(TwoMActivity.this)//
                    .anchor(findViewById(R.id.ll_me1))//如果是Activity上增加引导层，不需要设置anchor
                    .addHighLight(R.id.linearLayout_into_account_info,R.layout.info_known_total_asstes,new OnRightPosCallback(),new BaseLightShape(5,5) {
                        @Override
                        protected void resetRectF4Shape(RectF viewPosInfoRectF, float dx, float dy) {
                            //缩小高亮控件范围
                            viewPosInfoRectF.inset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dx,getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dy,getResources().getDisplayMetrics()));
                        }

                        @Override
                        protected void drawShape(Bitmap bitmap, HighLight.ViewPosInfo viewPosInfo) {
                            //custom your hight light shape 自定义高亮形状
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setDither(true);
                            paint.setAntiAlias(true);
                            paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
                            RectF rectF = viewPosInfo.rectF;
                            canvas.drawOval(rectF, paint);
                        }
                    })
                    .autoRemove(false)
                    .enableNext()
                    .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
                        @Override
                        public void onRemove() {
                            MainActivity.tabWidget.setVisibility(View.VISIBLE);
                            MainActivity.view.setVisibility(View.VISIBLE);
                            sharedpreferences.edit().putBoolean("isShow2",false).commit();
                        }
                    })
                    .setClickCallback(new HighLight.OnClickCallback() {
                        @Override
                        public void onClick() {
                            mHightLight.next();
                        }
                    });
            mHightLight.show();


    }



    /**
     * 双击退出程序
     */
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                sToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();

            } else {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
