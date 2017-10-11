package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationCreater;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.HeadZoomScrollView;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.FastBlurUtil;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/14.
 */

public class FriendsInfoActivity extends MyBaseActivity implements View.OnClickListener{

    private TextView tvName,tvOneua,tvLinkCode,tvArea,tvType,tvLevel,tvQinmi;
    private ImageView ivBg,ivSex,iv_dianzan_pic;
    private CircleImageView ivTouXiang;
    private Button btnAdd,btnDel,btnTransfer,btnSend,btnAgree,btnJujue;
    private LinearLayout ll_test,ll_test1;
    private FrameLayout rl;

    private ZanRanking zanRanking;
    private int isFrend = 0; // 0---好友  1---陌生人

    private Intent intent3;
    private SharedPreferences sharedPreferences;

    private View view;
    private PopupWindow window = null;
    private int isdel;

    private HeadZoomScrollView headZoomScrollView;

    private LoadingView loadview;

    private LinearLayout linearLayout_minning_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_info_layout);

        init();
        setUp();

    }



    private void init() {
        linearLayout_minning_level = (LinearLayout) this.findViewById(R.id.linearLayout_minning_level);

        sharedPreferences = getSharedPreferences("contact",0);
        intent3 = getIntent();

        isFrend = intent3.getIntExtra("isFrend",0);
        isdel = intent3.getIntExtra("isdel",1);

        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("删除好友");
        ((TextView)view.findViewById(R.id.tv_prompt)).setText("确定要删除吗?");
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvOneua = (TextView) findViewById(R.id.tv_oneua);
        tvLinkCode = (TextView) findViewById(R.id.tv_linkcode);
        tvArea = (TextView) findViewById(R.id.tv_area);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvQinmi = (TextView) findViewById(R.id.tv_qinmi);
        rl = (FrameLayout) findViewById(R.id.fl);

        loadview = (LoadingView) findViewById(R.id.loadview);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        ivTouXiang = (CircleImageView) findViewById(R.id.iv_touxaing);
        ivSex = (ImageView) findViewById(R.id.iv_sex);
        iv_dianzan_pic = (ImageView) findViewById(R.id.iv_dianzan_pic);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnTransfer = (Button) findViewById(R.id.btn_transfer);

        ll_test = (LinearLayout) findViewById(R.id.ll_test);
        ll_test1 = (LinearLayout) findViewById(R.id.ll_test1);
        btnAgree = (Button) findViewById(R.id.btn_agree);
        btnJujue = (Button) findViewById(R.id.btn_jujue);
        if (isdel == 1){
            if (isFrend == 0){
                ll_test.setVisibility(View.VISIBLE);
                btnDel.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                ll_test1.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
            }else if (isFrend == 1){
                ll_test.setVisibility(View.GONE);
                btnDel.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                ll_test1.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
            }else if (isFrend == 2){
                ll_test.setVisibility(View.GONE);
                btnDel.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                ll_test1.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
            }

        }

        HttpConnect.post(this, "member_hide_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String min = data.getJSONArray("data").getJSONObject(0).getString("minning");

                    FriendsInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(min)&&min.equals("0"))
                            {
                                linearLayout_minning_level.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        loadview.setVisibility(View.VISIBLE);
        if (intent3.getIntExtra("isq",0) == 1){
            Map<String, String> map = new HashMap<String, String>();
            map.put("number",intent3.getStringExtra("userid"));
            HttpConnect.post(this,"member_friends_search" , map,
                    new Callback() {

                        @Override
                        public void onResponse(Response arg0)
                                throws IOException {

                            final JSONObject object = JSONObject.fromObject(arg0
                                    .body().string());
                            if (object.get("status").equals("success")) {

                                JSONArray array = object
                                        .optJSONArray("data");
                                if (array.size() != 0) {
                                    JSONObject object2 = array
                                            .getJSONObject(0);
                                    zanRanking = new ZanRanking();
                                    String id = object2.optString("id");
                                    String name = object2.optString("name");
                                    String pic =  object2.optString("pic");
                                    String lk = object2.optString("lk");
                                    String type = object2.optString("type");
                                    String grade =  object2.optString("grade");
                                    String inti =   object2.optString("inti");
                                    String ip =  object2.optString("ip");
                                    String mobile =  object2.optString("mobile");
                                    String remark = object2.optString("remark");
                                    String oneau = object2.optString("oneau");
                                    String sex = object2.optString("sex");
                                    String sta = object2.optString("sta");
                                    String bgpic = object2.optString("backgroundpic");
                                    String paymentcode = object2.optString("paymentcode");

                                    String pra = object2.optString("pra");
                                    String bepra = object2.optString("bepra");
                                    String zanType = "";

                                    if (pra.equals("0") && bepra.equals("1")){
                                        zanType = "1";
                                    }else if (pra.equals("1") && bepra.equals("0")){
                                        zanType = "0";
                                    }else if (pra.equals("1") && bepra.equals("1")){
                                        zanType = "2";
                                    }else if (pra.equals("0") && bepra.equals("0")){
                                        zanType = "3";
                                    }

                                    zanRanking.setZanType(zanType);
                                    zanRanking.setPic(pic);
                                    zanRanking.setArea(ip);
                                    zanRanking.setCount(inti);
                                    zanRanking.setType(type);
                                    zanRanking.setLinkCode(lk);
                                    zanRanking.setId(id);
                                    zanRanking.setName(name);
                                    zanRanking.setLevel1(grade);
                                    zanRanking.setMobile(mobile);
                                    zanRanking.setOneau(oneau);
                                    zanRanking.setRemark(remark);
                                    zanRanking.setSex(sex);
                                    zanRanking.setSta(sta);
                                    zanRanking.setBackgroundpic(bgpic);
                                    zanRanking.setPaymentCode(paymentcode);

                                    Handler dataHandler = new Handler(
                                            getContext().getMainLooper()) {

                                        @Override
                                        public void handleMessage(
                                                final Message msg) {
                                            if (zanRanking.getSex().equals("美女")){
                                                ivSex.setVisibility(View.VISIBLE);
                                                ivSex.setImageResource(R.mipmap.girl);
                                            }else if (zanRanking.getSex().equals("帅哥")){
                                                ivSex.setImageResource(R.mipmap.boy);
                                                ivSex.setVisibility(View.VISIBLE);
                                            }else {
                                                ivSex.setVisibility(View.GONE);
                                            }

                                            if (isdel == 0){
                                                if (zanRanking.getSta().equals("0")){
                                                    ll_test.setVisibility(View.GONE);
                                                    btnDel.setVisibility(View.GONE);
                                                    btnAdd.setVisibility(View.VISIBLE);
                                                    ll_test1.setVisibility(View.GONE);
                                                    rl.setVisibility(View.GONE);
                                                }else{
                                                    ll_test.setVisibility(View.VISIBLE);
                                                    btnDel.setVisibility(View.VISIBLE);
                                                    btnAdd.setVisibility(View.GONE);
                                                    ll_test1.setVisibility(View.GONE);
                                                    rl.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            tvName.setText(zanRanking.getName());
                                            tvOneua.setText(zanRanking.getOneau());
                                            tvLinkCode.setText(zanRanking.getLinkCode());
                                            tvArea.setText(zanRanking.getArea());
                                            tvType.setText(zanRanking.getType());
                                            tvLevel.setText(zanRanking.getLevel1());
                                            tvQinmi.setText(zanRanking.getCount());

                                            if(zanRanking.getPic()!=null)
                                            {
                                                if(!zanRanking.getBackgroundpic().equals("")&&!zanRanking.getBackgroundpic().equals(zanRanking.getPic()))
                                                {
                                                    Glide.with(FriendsInfoActivity.this).load(zanRanking.getBackgroundpic()).placeholder(null)
                                                            .error(null).into(ivBg);
                                                }else
                                                {
                                                    new Thread(new Thread()
                                                    {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(zanRanking.getPic(),2);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ivBg.setImageBitmap(blurBitmap2);
                                                                }
                                                            });
                                                        }
                                                    }).start();
                                                }
                                                Glide.with(FriendsInfoActivity.this).load(zanRanking.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivTouXiang) {
                                                    @Override
                                                    protected void setResource(Bitmap resource) {
                                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                        circularBitmapDrawable.setCircular(true);
                                                        ivTouXiang.setImageDrawable(circularBitmapDrawable);
                                                    }
                                                });

                                                String zanType = zanRanking.getZanType();

                                                if (zanType.equals("0")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan0);
                                                }else if (zanType.equals("1")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan1);
                                                }else if (zanType.equals("2")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                }else if (zanType.equals("3")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan3);
                                                }
                                            }else
                                            {
                                                ivTouXiang.setImageResource(R.mipmap.iv_member_default_head_img);
                                                ivBg.setImageResource(R.mipmap.iv_member_info_bg);
                                            }
                                            MyOnClickListener myOnClickListener = new MyOnClickListener(zanRanking.getId(),zanRanking.getLinkCode());
                                            btnAgree.setOnClickListener(myOnClickListener);
                                            btnJujue.setOnClickListener(myOnClickListener);

                                            loadview.setVisibility(View.GONE);
                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);

                                }
                            }else{
                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
                                        loadview.setVisibility(View.GONE);
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }

                        }


                        @Override
                        public void onFailure(Request arg0, IOException arg1) {


                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
                                    loadview.setVisibility(View.GONE);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    });
        }else{
            zanRanking = (ZanRanking) intent3.getSerializableExtra("zanRanking");
            if (zanRanking.getSex().equals("美女")){
                ivSex.setVisibility(View.VISIBLE);
                ivSex.setImageResource(R.mipmap.girl);
            }else if (zanRanking.getSex().equals("帅哥")){
                ivSex.setImageResource(R.mipmap.boy);
                ivSex.setVisibility(View.VISIBLE);
            }else {
                ivSex.setVisibility(View.GONE);
            }
            tvName.setText(zanRanking.getName());
            tvOneua.setText(zanRanking.getOneau());
            tvLinkCode.setText(zanRanking.getLinkCode());
            tvArea.setText(zanRanking.getArea());
            tvType.setText(zanRanking.getType());
            tvLevel.setText(zanRanking.getLevel1());
            tvQinmi.setText(zanRanking.getCount());

            if(zanRanking.getPic()!=null)
            {
                if(!zanRanking.getBackgroundpic().equals("")&&!zanRanking.getBackgroundpic().equals(zanRanking.getPic()))
                {
                    Glide.with(FriendsInfoActivity.this).load(zanRanking.getBackgroundpic()).placeholder(null)
                            .error(null).into(ivBg);
                }else
                {
                    new Thread(new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(zanRanking.getPic(),2);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivBg.setImageBitmap(blurBitmap2);
                                }
                            });
                        }
                    }).start();
                }
                Glide.with(FriendsInfoActivity.this).load(zanRanking.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivTouXiang) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivTouXiang.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }else
            {
                ivTouXiang.setImageResource(R.mipmap.iv_member_default_head_img);
                ivBg.setImageResource(R.mipmap.iv_member_info_bg);
            }



            MyOnClickListener myOnClickListener = new MyOnClickListener(zanRanking.getId(),zanRanking.getLinkCode());
            btnAgree.setOnClickListener(myOnClickListener);
            btnJujue.setOnClickListener(myOnClickListener);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadview.setVisibility(View.GONE);
                }
            });

        }

    }

    private void setUp() {
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        iv_dianzan_pic.setOnClickListener(this);

    }




    public void back(View view){
        finish();
    }

    String msg = "";
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_dianzan_pic:
                Map<String, String> map = new HashMap<String, String>();
                map.put("zid",zanRanking.getId());
                HttpConnect.post(this,"member_friends_be_praise" , map,
                        new Callback() {
                            @Override
                            public void onResponse(Response arg0)
                                    throws IOException {

                                if (arg0 != null || !arg0.equals("")) {

                                    final JSONObject object = JSONObject.fromObject(arg0.body().string());
                                    msg = object.getString("msg");

                                    if (object.get("status").equals("success")) {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg) {

                                                String zanType = zanRanking.getZanType();

                                                if (zanType.equals("0")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                    zanRanking.setZanType("2");
                                                }else if (zanType.equals("1")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                    zanRanking.setZanType("2");
                                                }else if (zanType.equals("2")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan2);
                                                    zanRanking.setZanType("2");
                                                }else if (zanType.equals("3")){
                                                    iv_dianzan_pic.setImageResource(R.mipmap.frend_dianzan0);
                                                    zanRanking.setZanType("0");
                                                }
                                                tvQinmi.setText(Integer.parseInt(zanRanking.getCount())+1+"");
                                                ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(FriendsInfoActivity.this, R.anim.scale);
                                                iv_dianzan_pic.setAnimation(animation);

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }else {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg1) {
                                                if (!TextUtils.isEmpty(msg)){
//                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

                                Handler dataHandler = new Handler(
                                        getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
//                                            ((ContactListActivity)context).sToast("请检查网络");
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        });
                break;
            case R.id.btn_add://加宝友
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("tomemberid",zanRanking.getId());
                HttpConnect.post(this,"member_add_friends" , map1,
                        new Callback() {
                            @Override
                            public void onResponse(Response arg0)
                                    throws IOException {

                                if (arg0 != null || !arg0.equals("")) {

                                    final JSONObject object = JSONObject.fromObject(arg0.body().string());
                                    msg = object.getString("msg");

                                    if (object.get("status").equals("success")) {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg) {
                                                sToast("申请已发送");

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }else {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg1) {
                                                if (!TextUtils.isEmpty(msg)){
                                                    Toast.makeText(FriendsInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

                                Handler dataHandler = new Handler(
                                        getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
//                                        sToast("请检查网络");
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        });
                break;
            case R.id.btn_send://发送消息

                String userid = zanRanking.getLinkCode();
                final Intent intent = mIMKit.getChattingActivityIntent(userid, MyApplication.APP_KEY);
                startActivity(intent);
                break;
            case R.id.btn_del://删除宝友
                show();
                break;
            case R.id.btn_transfer://转账
                if(null!=zanRanking)
                {
                    String pic = zanRanking.getPic();
                    String name = zanRanking.getName();
                    String paymentCode = zanRanking.getPaymentCode();
                    Intent intent1 = new Intent(this,PayMoneyActivity.class);
                    intent1.putExtra("pic",pic);
                    intent1.putExtra("nickname",name);
                    intent1.putExtra("linkcode",paymentCode);
                    startActivity(intent1);
                }else
                {
                    Intent intent1 = new Intent(this,PayMoneyActivity.class);
                    intent1.putExtra("pic","");
                    intent1.putExtra("nickname","");
                    intent1.putExtra("linkcode","");
                    startActivity(intent1);
                }



                break;
        }
    }


    /**
     * 显示确定删除的提示框
     */
    public void show()
    {

        Button btnCencel1 = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit1 = (Button) view.findViewById(R.id.btn_commit);

        btnCencel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        btnCommit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("fmid",zanRanking.getId());
                HttpConnect.post(FriendsInfoActivity.this,"member_friends_delete" , map1,
                        new Callback() {
                            @Override
                            public void onResponse(Response arg0)
                                    throws IOException {

                                if (arg0 != null || !arg0.equals("")) {

                                    final JSONObject object = JSONObject.fromObject(arg0.body().string());
                                    msg = object.getString("msg");

                                    if (object.get("status").equals("success")) {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg) {

                                                //获取与某个聊天对象的会话记录,userId:表示聊天对象id
                                                YWConversation conversation = mIMKit.getIMCore().getConversationService()
                                                        .getConversation(zanRanking.getLinkCode());
                                                //获取会话管理类
                                                IYWConversationService conversationService = mIMKit.getIMCore().getConversationService();
                                                conversationService.deleteConversation(conversation);

                                                Set<String> keys3 = MyApplication.maps.keySet();
                                                if(keys3!=null&&keys3.size()>0)
                                                {
                                                    if(keys3.contains("ContactListActivity"))
                                                    {
                                                        Activity activity = MyApplication.maps.get("ContactListActivity");
                                                        activity.finish();
                                                        startActivity(new Intent(FriendsInfoActivity.this,ContactListActivity.class));
                                                    };
                                                }
                                                finish();

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }else {
                                        Handler dataHandler = new Handler(
                                                getMainLooper()) {
                                            @Override
                                            public void handleMessage(
                                                    final Message msg1) {
                                                if (!TextUtils.isEmpty(msg)){
                                                    Toast.makeText(FriendsInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        };
                                        dataHandler.sendEmptyMessage(0);
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Request arg0, IOException arg1) {

                                Handler dataHandler = new Handler(
                                        getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {
//                                        sToast("请检查网络");
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        });

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
            window.showAtLocation(findViewById(R.id.iv_back2),
                    Gravity.BOTTOM, 0, 0);
        }
    }


    /**
     * 好友审批
     */
    private class MyOnClickListener implements View.OnClickListener{

        private String id ;
        private String lk;

        public MyOnClickListener(String id, String lk) {
            this.id = id;
            this.lk = lk;
        }
        private String status;

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btn_agree){
                status = "1";
            }else if (v.getId() == R.id.btn_jujue){
                status = "2";
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("FromMemberID",id);
            map.put("status",status);
            HttpConnect.post(FriendsInfoActivity.this,"member_friends_list_do" , map,
                    new Callback() {
                        @Override
                        public void onResponse(Response arg0)
                                throws IOException {

                            if (arg0 != null || !arg0.equals("")) {

                                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                                msg = object.getString("msg");

                                if (object.get("status").equals("success")) {
                                    Handler dataHandler = new Handler(
                                            getMainLooper()) {
                                        @Override
                                        public void handleMessage(
                                                final Message msg) {

                                            getSharedPreferences("contact",0).edit().putString("isFresh","2").commit();
                                            if (status.equals("1")){
                                                sToast("添加成功");
                                                //创建一条文本或者表情消息
                                                YWMessage message = YWMessageChannel.createTextMessage("我们已经是宝友了，现在可以开始聊天了。");
                                                YWIMCore imCore = YWAPI.createIMCore(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode(), MyApplication.APP_KEY);
                                                //创建一个与消息接收者的聊天会话，userId：表示聊天对象id
                                                final YWConversationCreater conversationCreater = imCore.getConversationService().getConversationCreater();
                                                YWConversation conversation = conversationCreater.createConversationIfNotExist(lk);
                                                //将消息发送给对方
                                                conversation.getMessageSender().sendMessage(message, 120, null);
                                                Set<String> keys3 = MyApplication.maps.keySet();
                                                if(keys3!=null&&keys3.size()>0)
                                                {
                                                    if(keys3.contains("ContactListActivity"))
                                                    {
                                                        Activity activity = MyApplication.maps.get("ContactListActivity");
                                                        activity.finish();
                                                        startActivity(new Intent(FriendsInfoActivity.this,ContactListActivity.class));
                                                    };
                                                }
                                                finish();
                                            }else if (status.equals("2")){
                                                sToast("已拒绝添加");
                                                MyApplication.deleteActivity("ContactListActivity");
                                                startActivity(new Intent(FriendsInfoActivity.this,ContactListActivity.class));
                                                finish();
                                            }

                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }else {
                                    Handler dataHandler = new Handler(
                                            getMainLooper()) {
                                        @Override
                                        public void handleMessage(
                                                final Message msg1) {
                                            if (!TextUtils.isEmpty(msg)){
                                                Toast.makeText(FriendsInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    };
                                    dataHandler.sendEmptyMessage(0);
                                }
                            }
                        }


                        @Override
                        public void onFailure(Request arg0, IOException arg1) {

                            Handler dataHandler = new Handler(
                                    getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
//                                    sToast("请检查网络");
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    });

        }
    }
}
