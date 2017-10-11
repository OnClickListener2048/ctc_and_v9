package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.GongGaoListAdapter;
import com.tohier.cartercoin.adapter.ReceiveCouponAdapter;
import com.tohier.cartercoin.bean.MyActivity;
import com.tohier.cartercoin.bean.News;
import com.tohier.cartercoin.bean.Product;
import com.tohier.cartercoin.bean.YouHuiQuan;
import com.tohier.cartercoin.columnview.AutoScrollView;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.FastBlurUtil;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.IPUtil;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.loader.GlideImageLoader;
import com.tohier.cartercoin.mefragmentgonggaolist.MeFragmentGongGaoList1;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.GetVersionCodePresenter;
import com.tohier.cartercoin.presenter.LoadGongGaoListPresenter;
import com.tohier.cartercoin.presenter.LoadLoginPicPresenter;
import com.tohier.cartercoin.presenter.PhoneLoginPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.LoadGongGaoListView;
import com.tohier.cartercoin.ui.LoadLoginPicView;
import com.tohier.cartercoin.ui.PhoneLoginView;
import com.tohier.cartercoin.ui.UpdateDialogView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.shape.CircleLightShape;
import zhy.com.highlight.shape.RectLightShape;


/**
 * Created by Administrator on 2016/11/9.
 */
public class MeActivity extends MyBaseActivity implements View.OnClickListener,LoadGongGaoListView,LoadLoginPicView,PhoneLoginView,GetMemberInfoView ,UpdateDialogView {



    private SharedPreferences sharedpreferences;

    private HighLight mHightLight;
    public static String[] textStrings;

    final ArrayList<String> text = new ArrayList<String>();


    private TextView tv_message_count;
//    private ImageView iv_novice_new;
    private LoadingView cif_loading,loadingView;

    public static MeActivity activity;

    /**
     * 存放操作列表的List
     */
    private List<Fragment> datas = new ArrayList<Fragment>();

    private AutoScrollView scrollView;

    private MeFragmentGongGaoList1 meFragmentGongGaoList1;
//    private MeFragmentGongGaoList2 meFragmentGongGaoList2;
    /**
     *加载公告列表的presenter
     */
    private LoadGongGaoListPresenter loadGongGaoListPresenter;

    private boolean isWanCheng = true;


    /**
     * 进入公告列表的LinearLayout
     */
    private LinearLayout linearlayout_into_gonggao_activity,linearLayout_into_capture,linearLayout_into_bill_activity,linearLayout_into_share,
            linearLayout_into_coupon,linearLayout_into_signed,ll_contact,ll_tixian,ll_shoukuan,ll_zichanbao,ll_vip,ll_deal,ll_option,ll_baoyou,ll_kefu,ll_1,ll_2;

    private ListView gongGaolistView;
    private GongGaoListAdapter gongGaoListAdapter;
    private ArrayList<News> newDatas = new ArrayList<News>();

    private VerticalTextview verticalTextview;

    private Banner banner;

    private List<Product> binnerList = new ArrayList<Product>();
    private List<String> imageUrl = new ArrayList<String>();



    public static String payUrl,wxpPayUrl;

    /**
     *用于加载登录页面以及欢迎页面图片 负责完成View于Model间的交互
     **/
    private LoadLoginPicPresenter loadLoginPicPresenter;

    private GetMemberInfoPresenter getMemberInfoPresenter;
    private String username;
    private String password;


    public static final String SETTINGS_ACT = "settings-act";

    /**
     * 图片保存的路劲
     **/
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/download_test/";

    /**
     * 这是分享图片所用的名称
     */
    public static final String SHARE_PIC1 = "sharess.jpg"; //背景

    private View upgradeView;
    private PopupWindow upgradeWin;
    /**
     *用于是否提示更新的Presenter 负责完成View于Model间的交互
     **/
    private GetVersionCodePresenter getVersionCodePresenter;

    Handler  handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x001)
            {
                getMeNotice();
                getMemberState();
                isMaintain();
                handler.postDelayed(thread,30000);
            }
        }
    };

    Thread thread = new Thread()
    {
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(0x001);
        }
    };

    /**
     * 进入活动列表的图片按钮
     */
    private ImageView iv_into_authentication,iv_cancel_active;
    private View view1,renZhengView,receiveCouponView;
    private PopupWindow window,renZhengWin,receiveCouponWin;
    private ListView receiveCouponLv;
    private ImageView iv_cancel_receive_coupon;
    private List<YouHuiQuan> receiveCouponDatas = new ArrayList<YouHuiQuan>();
    private ReceiveCouponAdapter receiveCouponAdapter;
    private TextView tv_receive_coupon_money;
    private RelativeLayout relativeLayout;
    //活动的id
    private String activeId;

    private MyActivity myActivity;
    private String url,title,desc;
    private int x = 1;

    private Handler popupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(new Intent(MeActivity.this,DialogBandPhoneActivity.class));
                    break;

                case 1:
                    receiveCouponWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp3 = MainActivity.mainActivity.getWindow().getAttributes();
                            lp3.alpha = 1f;
                            MainActivity.mainActivity.getWindow().setAttributes(lp3);
                        }
                    });

                    receiveCouponWin.setOutsideTouchable(true);

                    // 实例化一个ColorDrawable颜色为半透明
                    ColorDrawable dw = new ColorDrawable(0x00ffffff);
                    receiveCouponWin.setBackgroundDrawable(dw);

                    // 设置popWindow的显示和消失动画
                    receiveCouponWin.setAnimationStyle(R.style.Mypopwindow_anim_style);
                    // 在底部显示
                    receiveCouponWin.showAtLocation(findViewById(R.id.linearLayout_into_share),
                            Gravity.BOTTOM, 0, 0);

                    if(receiveCouponWin!=null&&receiveCouponWin.isShowing())
                    {
                        // 设置背景颜色变暗
                        WindowManager.LayoutParams lp5 = MainActivity.mainActivity.getWindow().getAttributes();
                        lp5.alpha = 0.5f;
                        MainActivity.mainActivity.getWindow().setAttributes(lp5);
                    }
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_me1);
        loadGongGaoListPresenter = new LoadGongGaoListPresenter(this,this,isWanCheng);

        MyApplication.maps.put("IndexActivity",this);

        if (null == PushServiceFactory.getCloudPushService()) {
            Log.i("DeviceId:","SDk环境初始化失败");
        } else {
            Log.i("DeviceId:","DeviceId=="+ PushServiceFactory.getCloudPushService().getDeviceId());
        }


        String into = MainActivity.into;
        if (!TextUtils.isEmpty(into) && into.equals("GuideActivity")) {
            String type = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("type", "");
            if (type.equals("phoneLogin")) {
                username = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("username", "");
                password = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("password", "");
                getMemberInfoPresenter = new GetMemberInfoPresenter(this, this);
                PhoneLoginPresenter phoneLoginPresenter = new PhoneLoginPresenter(this, this, getMemberInfoPresenter);
                phoneLoginPresenter.VerificationTokenPhoneLogin();
            } else if (type.equals("wxLogin")) {
                String openId = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("openId", "");
                wxLogin(openId);
            } else if (type.equals("qqLogin")) {
                String openId = getSharedPreferences("login_mode", Context.MODE_PRIVATE).getString("openId", "");
                qqLogin(openId, null);
            }else if(type.equals("sms"))
            {
                Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                startActivity(intent5);
                finish();
            }
        }else
        {
            String phoneNum = "";
            try{
                phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode();
            }catch (Exception e){

            }

            if (!phoneNum.equals("") && null != phoneNum) {
                final String finalPhoneNum = phoneNum;
                PushServiceFactory.getCloudPushService().bindAccount(phoneNum, new CommonCallback() {
                    @Override
                    public void onSuccess(String response) {
//					Toast.makeText(getApplicationContext(), "赞! 账号绑定成功 ~",
//							Toast.LENGTH_SHORT).show();
                        Log.i(SETTINGS_ACT, "@用户绑定账号 ：" + finalPhoneNum + " 成功");
                    }

                    @Override
                    public void onFailed(String errorCode, String errorMessage) {
//					Toast.makeText(getApplicationContext(), "衰! 账号绑定失败 ~",
//							Toast.LENGTH_SHORT).show();
                        Log.i(SETTINGS_ACT, "@用户绑定账号 ：" + finalPhoneNum + " 失败，原因 ： " + errorMessage);
                    }
                });
            }
        }


        initData();
        setUpView();

        //调用获取未读消息数的方法
        getMsgCount();
        //调用活动控制的方法
//        activeControl();


        savePic();


    }

    public void savePic() {
        /**
         * 获取分享图片
         */
        HttpConnect.post(MeActivity.this, "member_share_pic_url_me", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {

                String str = arg0.body().string();
                JSONObject data = JSONObject.fromObject(str);
                if (data.getString( "status").equals("success")) {
                    String url1 = data.optJSONArray("data").optJSONObject(0).optString("url");
//                    String url2 = data.optJSONArray("data").optJSONObject(0).optString("url2");

                    saveFile(url1,SHARE_PIC1);
//                     bitmap1 = convertToBitmap(ALBUM_PATH+SHARE_PIC1);

//                     saveFile(url1,SHARE_PIC);
//                     bitmap2= convertToBitmap(ALBUM_PATH+SHARE_PIC);
                }else{
//                    sToast(data.getString("msg"));
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
    public void onResume() {
        super.onResume();
        getPic();
        handler.post(thread);
        getMsgCount();
        if(window!=null&&window.isShowing())
        {
             window.dismiss();
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            linearLayout_into_share.setFocusable(true);
            linearLayout_into_share.setFocusableInTouchMode(true);
            linearLayout_into_share.requestFocus();
        }

    }

    @Override
    public void initData() {
        myActivity = new MyActivity();

        sharedpreferences = getSharedPreferences("isShowWelcomeLayout", Context.MODE_PRIVATE);

        view1 = View.inflate(this, R.layout.popupwindow_active,null);
        iv_into_authentication = (ImageView) view1.findViewById(R.id.iv_into_authentication);
        iv_cancel_active = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);

        renZhengView = View.inflate(this, R.layout.popupwindow_prompt_authentication,null);
        renZhengWin = new PopupWindow(renZhengView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        receiveCouponView = View.inflate(this, R.layout.popupwindow_receive_coupon,null);
        receiveCouponLv = (ListView) receiveCouponView.findViewById(R.id.lv_receive_coupon);
        iv_cancel_receive_coupon = (ImageView) receiveCouponView.findViewById(R.id.iv_cancel_receive_coupon);
        tv_receive_coupon_money = (TextView) receiveCouponView.findViewById(R.id.tv_receive_coupon_money);
        relativeLayout = (RelativeLayout) receiveCouponView.findViewById(R.id.relativeLayout);

        receiveCouponAdapter = new ReceiveCouponAdapter(receiveCouponDatas, this);
        receiveCouponLv.setAdapter(receiveCouponAdapter);
        receiveCouponWin = new PopupWindow(receiveCouponView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        upgradeView = View.inflate(this, R.layout.activity_prompt_update, null);
        upgradeWin = new PopupWindow(upgradeView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        /**
         * 初始化 app是否提示更新的 Presenter
         */
        getVersionCodePresenter = new GetVersionCodePresenter(this, this);

        iv_cancel_receive_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=receiveCouponWin&&receiveCouponWin.isShowing())
                {
                    receiveCouponWin.dismiss();
                }
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this,CouponAllActivity.class));
                if(null!=receiveCouponWin&&receiveCouponWin.isShowing())
                {
                    receiveCouponWin.dismiss();
                }
            }
        });
        receiveCouponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MeActivity.this,CouponAllActivity.class));
                if(null!=receiveCouponWin&&receiveCouponWin.isShowing())
                {
                    receiveCouponWin.dismiss();
                }
            }
        });

        iv_into_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
//                theActiveMarkReader();
//                Intent intent = new Intent(getActivity(),NewSchoolActivity.class);
//                intent.putExtra("url",activeId);
//                startActivity(intent);
                getUrl(myActivity.getId(),0);
            }
        });

        iv_cancel_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
//                theActiveMarkReader();
            }
        });

        banner = (Banner) findViewById(R.id.banner);

        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);



        scrollView = (AutoScrollView) findViewById(R.id.scrollview);
        linearlayout_into_gonggao_activity = (LinearLayout) findViewById(R.id.linearlayout_into_gonggao_activity);
        linearLayout_into_capture = (LinearLayout) findViewById(R.id.linearLayout_into_capture);
        linearLayout_into_bill_activity = (LinearLayout) findViewById(R.id.linearLayout_into_bill_activity);
        linearLayout_into_share = (LinearLayout) findViewById(R.id.linearLayout_into_share);
        linearLayout_into_signed = (LinearLayout) findViewById(R.id.linearLayout_into_signed);
        linearLayout_into_coupon = (LinearLayout) findViewById(R.id.linearLayout_into_coupon);
//        iv_novice_new = (ImageView) findViewById(R.id.iv_novice_new);

//        if (sharedpreferences.getString("isHadden","0").equals("0")){
//            iv_novice_new.setVisibility(View.VISIBLE);
//        }else{
//            iv_novice_new.setVisibility(View.GONE);
//        }
//        Animation anim = AnimationUtils.loadAnimation(MeActivity.this,R.anim.novice_new_anim);
//        iv_novice_new.startAnimation(anim);



        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        ll_tixian = (LinearLayout) findViewById(R.id.ll_tixian);
        ll_shoukuan = (LinearLayout) findViewById(R.id.ll_chongzhi);
        ll_zichanbao = (LinearLayout) findViewById(R.id.ll_zichanbao);
        ll_vip = (LinearLayout) findViewById(R.id.ll_vip);
        ll_deal = (LinearLayout) findViewById(R.id.ll_deal);
        ll_option = (LinearLayout) findViewById(R.id.ll_option);
        ll_baoyou = (LinearLayout) findViewById(R.id.ll_baoyou);
        ll_kefu = (LinearLayout) findViewById(R.id.ll_kefu);
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        cif_loading = (LoadingView) findViewById(R.id.cif_loading);
        loadingView = (LoadingView) findViewById(R.id.loadview);

        tv_message_count = (TextView) findViewById(R.id.tv_message_count);
        verticalTextview = (VerticalTextview) findViewById(R.id.tv_info);

        verticalTextview.setText(11, 5, 0xffffffff);//设置属性,具体跟踪源码
        verticalTextview.setTextStillTime(4000);//设置停留时长间隔
        verticalTextview.setAnimTime(600);//设置进入和退出的时间间隔
        verticalTextview.setClickable(true);

        verticalTextview.setOnItemClickListener(new VerticalTextview.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(MeActivity.this, MessageCenterActivity.class);
                startActivity(intent);
            }
        });

        verticalTextview.startAutoScroll();


        gongGaolistView = (ListView) findViewById(R.id.listview_gonggao);
        gongGaoListAdapter = new GongGaoListAdapter(this,newDatas,"");
        gongGaolistView.setAdapter(gongGaoListAdapter);



        HttpConnect.post(this, "member_ali_sign", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    payUrl = data.optJSONArray("data").optJSONObject(0).optString("url");
                }else{
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

        HttpConnect.post(this, "member_wft_sign", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    wxpPayUrl = data.optJSONArray("data").optJSONObject(0).optString("url");
                }else{
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });


        /**
         * 初始化 加载登录页面与欢迎页面 Presenter
         */
        loadLoginPicPresenter = new LoadLoginPicPresenter(this,this);
        loadLoginPicPresenter.loadPic();

        //TODO 未开启更新提示
//        getActivePicInfo();


        activity = this;


    }

    public void setUpView()
    {
        /**
         * 为诸多组件设定 点击事件
         */
        ((RelativeLayout) findViewById(R.id.relativeLayout_into_new_info)).setOnClickListener(this);
        linearlayout_into_gonggao_activity.setOnClickListener(this);
        linearLayout_into_capture.setOnClickListener(this);
        linearLayout_into_bill_activity.setOnClickListener(this);
        linearLayout_into_share.setOnClickListener(this);
        linearLayout_into_coupon.setOnClickListener(this);
        linearLayout_into_signed.setOnClickListener(this);

        //功能块
        ll_baoyou.setOnClickListener(this);
        ll_deal.setOnClickListener(this);
        ll_kefu.setOnClickListener(this);
        ll_option.setOnClickListener(this);
        ll_shoukuan.setOnClickListener(this);
        ll_tixian.setOnClickListener(this);
        ll_vip.setOnClickListener(this);
        ll_zichanbao.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        /**
         * 因为刚开始 ScrolLView并不在顶部  用于回调屏幕顶端
         */
        linearLayout_into_share.setFocusable(true);
        linearLayout_into_share.setFocusableInTouchMode(true);
        linearLayout_into_share.requestFocus();

        loadingView.setVisibility(View.VISIBLE);
        loadGongGaoListPresenter.loadGongGaoList2();

        gongGaolistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (gongGaoListAdapter.getList().get(position).getStatus().equalsIgnoreCase("1")){
                    Intent intent = new Intent(MeActivity.this,GongGaoDetailActivity.class);
                    intent.putExtra("url", gongGaoListAdapter.getList().get(position).getUrl());
                    intent.putExtra("title",gongGaoListAdapter.getList().get(position).getTitle());
                    intent.putExtra("desc", gongGaoListAdapter.getList().get(position).getDesc());
                    gongGaoListAdapter.getList().get(position).setStatus("1");
                    gongGaoListAdapter.getList().get(position).setNumber(Integer.parseInt(gongGaoListAdapter.getList().get(position).getNumber())+1+"");
                    gongGaoListAdapter.setList(gongGaoListAdapter.getList());
                    gongGaoListAdapter.notifyDataSetChanged();
                    startActivity(intent);
                }else{
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("id",gongGaoListAdapter.getList().get(position).getId());
                    HttpConnect.post(MeActivity.this, "member_read_arcticle", map, new Callback() {
                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            String json = arg0.body().string();
                            JSONObject data = JSONObject.fromObject(json);
                            if (data.optString("status").equals("success")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MeActivity.this,GongGaoDetailActivity.class);
                                        intent.putExtra("url", gongGaoListAdapter.getList().get(position).getUrl());
                                        intent.putExtra("title",gongGaoListAdapter.getList().get(position).getTitle());
                                        intent.putExtra("desc", gongGaoListAdapter.getList().get(position).getDesc());
                                        gongGaoListAdapter.getList().get(position).setStatus("1");
                                        gongGaoListAdapter.getList().get(position).setNumber(Integer.parseInt(gongGaoListAdapter.getList().get(position).getNumber())+1+"");
                                        gongGaoListAdapter.setList(gongGaoListAdapter.getList());
                                        gongGaoListAdapter.notifyDataSetChanged();
                                        startActivity(intent);
                                    }
                                });

                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MeActivity.this,GongGaoDetailActivity.class);
                                        intent.putExtra("url", gongGaoListAdapter.getList().get(position).getUrl());
                                        intent.putExtra("title",gongGaoListAdapter.getList().get(position).getTitle());
                                        intent.putExtra("desc", gongGaoListAdapter.getList().get(position).getDesc());
                                        gongGaoListAdapter.getList().get(position).setStatus("1");
                                        gongGaoListAdapter.getList().get(position).setNumber(Integer.parseInt(gongGaoListAdapter.getList().get(position).getNumber())+1+"");
                                        gongGaoListAdapter.setList(gongGaoListAdapter.getList());
                                        gongGaoListAdapter.notifyDataSetChanged();
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

            }
        });



        HttpConnect.post(this, "member_trade_option_hide", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String isHidden = data.optJSONArray("data").optJSONObject(0).optString("value");

                            if (isHidden.equals("0")){//显示
                                ll_2.setVisibility(View.VISIBLE);
                            }else{//隐藏
                                ll_2.setVisibility(View.GONE);
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
         * vip升级提示
         */
        HttpConnect.post(this, "member_buy_upgrade_count", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textStrings = new String[]{data.optJSONArray("data").optJSONObject(0).optString("one"),
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

                        }
                    });
                    
                }


            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });



        ViewTreeObserver vto = findViewById(R.id.ll_me).getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (x == 1  && sharedpreferences.getBoolean("isShow3",true) == true){
//                    showNextKnownTipView();

                    x = 2;

                }

            }
        });

    }

    @Override
    public void showMsg(String msg) {
//        sToast(msg);
        isWanCheng = true;
    }


    /**
     * 登录图片以及欢迎页图片加载成功的回调
     **/
    @Override
    public void loadSuccess() {
//
    }

    @Override
    public void loadSuccess(List<News> news) {
        for(int i = 0 ; i < news.size() ; i ++)
        {
            if(!newDatas.contains(news.get(i).getTitle()))
            {
                newDatas.add(news.get(i));
            }
        }
        loadingView.setVisibility(View.GONE);
        gongGaoListAdapter.setList(newDatas);
        gongGaoListAdapter.notifyDataSetChanged();
        try {
            setListViewHeightBasedOnChildren(gongGaolistView);
        }catch (Exception e){

        }
        isWanCheng = true;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight = totalHeight+listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    @Override
    public void loadFail() {
//         sToast("链接失败");
        isWanCheng = true;
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
          switch (v.getId())
          {
              case R.id.relativeLayout_into_new_info:
                   intent = new Intent(MeActivity.this, MessageCenterActivity.class);
                  break;
              case R.id.ll_contact:
//                  intent = new Intent(MeActivity.this, ContactListActivity.class);
                  intent = new Intent(MeActivity.this, ShareBonusActivity.class);
//                  sharedpreferences.edit().putString("isHadden","1").commit();
//                  iv_novice_new.setVisibility(View.GONE);
                  break;
              case R.id.iv_setting:
                     intent = new Intent(MeActivity.this, NewSettingActivity.class);
                  break;
              case R.id.relativeLayout_into_memberInfo:
                     intent = new Intent(MeActivity.this, UpdateNewMemberInfoActivity.class);
                   break;
              case R.id.linearlayout_into_gonggao_activity:
                  intent = new Intent(MeActivity.this, GongGaoActivity.class);
                  break;

              case R.id.linearLayout_into_capture:
                  startActivityForResult(new Intent(MeActivity.this, CaptureActivity.class), 0);
                  break;
              case R.id.linearLayout_into_bill_activity:
                  intent = new Intent(MeActivity.this, ReceivablesActivity.class);
                  break;

              case R.id.linearLayout_into_share:
                  intent = new Intent(MeActivity.this,NewShareActivity.class);
//                  intent = new Intent(MeActivity.this, ZanRecordActivity.class);

                  break;
              case R.id.linearLayout_into_coupon:
                  intent = new Intent(MeActivity.this, CouponAllActivity.class);
                  break;
              case R.id.linearLayout_into_signed:
                  intent = new Intent(MeActivity.this, SignedActivity.class);
                  break;
              case R.id.ll_baoyou://商城
                  intent = new Intent(MeActivity.this, MallActivity.class);
                  break;
              case R.id.ll_tixian://提现
                  intent = new Intent(MeActivity.this, RechargeRMBActivity.class).putExtra("type","1");
                  break;
              case R.id.ll_deal://交易
                  intent = new Intent(MeActivity.this, NewTransactionActivity.class);
                  break;
              case R.id.ll_kefu://客服

                  EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                  //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                  //的setNeedByPass方法，参数为false。
                  //contact.setNeedByPass(false);

                  intent = MeActivity.this.mIMKit.getChattingActivityIntent(contact);
                  break;
              case R.id.ll_chongzhi://充值

//                  String name = MeActivity.this.getSharedPreferences("isExitsName", Context.MODE_PRIVATE).getString("name","");
//                  if(TextUtils.isEmpty(name)||name.equals(""))
//                  {
//                      show();
//                  }else
//                  {
//                      String phoneNum = LoginUser.getInstantiation(MeActivity.this.getApplicationContext()).getLoginUser().getPhoneNum();
//                      if(TextUtils.isEmpty(phoneNum))
//                      {
//                          show();
//                      }else
//                      {
//                          intent = new Intent(MeActivity.this, TiXianActivity.class);
//                      }
//                  }
                  intent = new Intent(MeActivity.this, HelperCenterActivity.class);
                  break;
              case R.id.ll_zichanbao://资产包
                  intent = new Intent(MeActivity.this, RechargeRMBActivity.class).putExtra("type","1");
                  break;
              case R.id.ll_option://期权
                  intent = new Intent(MeActivity.this, GameListActivity.class);
                  break;
              case R.id.ll_vip://vip
                  HttpConnect.post(MeActivity.this, "member_is_password_pay", null, new Callback() {

                      @Override
                      public void onResponse(Response arg0) throws IOException {
                          JSONObject data = JSONObject.fromObject(arg0.body().string());
                          if (data.get("status").equals("success")) {
                              String value = data.getJSONArray("data").getJSONObject(0).getString("value");

                              if(!TextUtils.isEmpty(value)&&value.equals("1")) //存在支付密码  要修改支付密码
                              {

                                  MeActivity.this.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          startActivity(new Intent(MeActivity.this, TiXianActivity.class));
                                      }
                                  });
                              }else             //不存在需要设置
                              {
                                  MeActivity.this.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          sToast("请设置交易密码");
                                          startActivity(new Intent(MeActivity.this, SettingPayPwdActivity.class));
                                      }
                                  });
                              }
                          }
                      }

                      @Override
                      public void onFailure(Request arg0, IOException arg1) {

                      }
                  });

                  break;

          }
        if(intent!=null)  startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        verticalTextview.stopAutoScroll();
        handler.removeCallbacks(thread);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MeActivity.this.RESULT_OK) {
            if (requestCode == 0) {
                String scanResult = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                if (scanResult != null && scanResult.length() > 0) {
                    thisPersonExist(scanResult);
                }
            }
        }
    }

    /**
     * 验证付款人是否存在
     */
    public void thisPersonExist(final String linkcode)
    {
        Map<String, String> par = new HashMap<String, String>();
        par.put("linkcode", linkcode);
        HttpConnect.post(this, "member_payment_val", par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success"))
                {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(
                                final Message msg)
                        {
                            final String pic = data.getJSONArray("data").getJSONObject(0).getString("Pic");
                            final String name = data.getJSONArray("data").getJSONObject(0).getString("Name");
                            final String nickname = data.getJSONArray("data").getJSONObject(0).getString("Nickname");
                            Intent intent = new Intent(MeActivity.this,PayMoneyActivity.class);
                            intent.putExtra("pic",pic);
                            intent.putExtra("nickname",nickname);
                            intent.putExtra("linkcode",linkcode);
                            startActivity(intent);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else
                {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(
                                final Message msg)
                        {
                            sToast("请提供正确的二维码以便扫描");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1)
            {
//                sToast("链接超时！");
            }
        });
    }

       private void getMeNotice()
       {
           final ArrayList<String> datas = new ArrayList<String>();
           HttpConnect.post(MeActivity.this, "member_my_notice", null, new Callback() {

               @Override
               public void onResponse(Response arg0) throws IOException {
                   JSONObject data = JSONObject.fromObject(arg0.body().string());
                   if (data.get("status").equals("success")) {
                       JSONArray dataArr = data.getJSONArray("data");
                       if(dataArr!=null)
                       {
                           for(int i = 0 ; i < dataArr.size() ; i ++)
                           {
                              final String value = dataArr.getJSONObject(i).getString("value");
                               if (!TextUtils.isEmpty(value))
                               {

                                           datas.add(value);
                               }
                           }
                           MeActivity.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   text.addAll(datas);
                                   verticalTextview.setTextList(datas);
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

    /**
     * 获取用户状态
     */
    private void getMemberState()
    {
        HttpConnect.post(MeActivity.this, "member_status_exit", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    if(dataArr!=null)
                    {
                        JSONObject jsonObj = dataArr.optJSONObject(0);
                        if(jsonObj!=null)
                        {
                           final String value  = jsonObj.optString("value");
                           if(value!=null)
                           {
                               if(value.equals("3"))
                               {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                                           startActivity(intent5);
                                           finish();
                                           LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                       }
                                   });
                               }
                           }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    /**
     * 查看当前app是否处于维护状态
     */
    private void isMaintain() {
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                final String ipLine = IPUtil.getNetIp();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyNetworkConnection.getNetworkConnection(MeActivity.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        sToast("请检查您的网络链接状态");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                try {
                                    String json = response.body().string().trim();
                                    final net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
                                    final net.sf.json.JSONObject jsonObject = jsonArray.optJSONObject(0);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String enable = jsonObject.optString("enable");
                                            String ip = jsonObject.optString("ip");
                                            if (enable != null && !TextUtils.isEmpty(enable)) {
                                                if (enable.equals("false")) {
                                                    //正常状态

                                                } else {
                                                    if (!TextUtils.isEmpty(ip) && ip.equals(ipLine)) {
                                                        //正常状态
                                                    } else {
                                                        //非正常状态
                                                        Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                                                        startActivity(intent5);
                                                        finish();
                                                        LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }catch (Exception e){

                                }
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }



    /**
     * 获取未读消息的数量
     */
    public void getMsgCount()
    {
        HttpConnect.post(MeActivity.this, "member_message_corner", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    MeActivity.this.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if(!TextUtils.isEmpty(data.getJSONArray("data").getJSONObject(0).getString("count")))
                           {
                                if(data.getJSONArray("data").getJSONObject(0).getString("count").equals("0"))
                                {
                                    tv_message_count.setVisibility(View.GONE);
                                }else
                                {
                                    tv_message_count.setVisibility(View.VISIBLE);
//                                    tv_message_count.setText(data.getJSONArray("data").getJSONObject(0).getString("count"));
                                }
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

    private int one = 0;

    @Override
    public void onStart() {
        super.onStart();

        one ++;
        if(one==1)
        {
            getVersionCodePresenter.getVersionCode();
        }
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }



    int i = 0;
    /**
     * 绑定手机号的窗体
     */
    public void showBandPhoneWindow()
    {
        popupHandler.sendEmptyMessageDelayed(0,500);
    }

    public void showReceiveCouponWindow()
    {
        Map<String, String> par = new HashMap<String, String>();
        par.put("id", LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId());
        par.put("status", 0+"");
        HttpConnect.post(MeActivity.this, "member_coupon_batch", par, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    for(int i = 0 ; i < dataArr.size() ; i ++)
                    {
                        JSONObject jsonObjNews = dataArr.optJSONObject(i);
                        if(jsonObjNews!=null)
                        {
                            String id = jsonObjNews.optString("id");
                            String name = jsonObjNews.optString("name");
                            String money = jsonObjNews.optString("money");
                            String status = jsonObjNews.optString("status");
                            String createdate = jsonObjNews.optString("createdate");
                            String enddate = jsonObjNews.optString("enddate");
                            String remark = jsonObjNews.optString("limit");
                            String min = jsonObjNews.optString("min");
                            String sta = jsonObjNews.optString("sta");
                            String mon = jsonObjNews.optString("mon");
                            String type = jsonObjNews.optString("type");
                            YouHuiQuan youHuiQuan = new YouHuiQuan(id,name,money,status,createdate,enddate,remark,min,sta,mon,type);
                            receiveCouponDatas.add(youHuiQuan);
                        }
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            MainActivity.isflag = true;
                            popupHandler.sendEmptyMessageDelayed(1,500);
                            receiveCouponAdapter.setItems(receiveCouponDatas);
                            receiveCouponAdapter.notifyDataSetChanged();

                            HttpConnect.post(MeActivity.this, "member_coupon_batch_insert", null, new Callback() {
                                @Override
                                public void onResponse(Response arg0) throws IOException {
                                    JSONObject data = JSONObject.fromObject(arg0.body().string());
                                    if (data.get("status").equals("success")) {

                                    }
                                }
                                @Override
                                public void onFailure(Request arg0, IOException arg1) {
                                    MeActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                      sToast("请检查您的网络连接状态");
                                        }
                                    });
                                }
                            });

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(!TextUtils.isEmpty(msg)&&!Tools.isPhonticName(msg))
                            {
                                sToast(msg);
                            }else
                            {
//                                sToast("暂无数据");
                            }
                            activeControl();
                        }
                    });
                }


            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                               MainActivity.isflag = true;
                    }
                });

            }
        });

        HttpConnect.post(MeActivity.this, "member_coupon_batch_all", null, new Callback() {

            private String msg;

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body()
                        .string().trim());

                msg = data.getString("msg");

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    JSONObject jsonObjNews = dataArr.optJSONObject(0);
                    if(jsonObjNews!=null)
                    {
                        final String al = jsonObjNews.optString("al");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(al))
                                {
                                    tv_receive_coupon_money.setText(al);
                                }
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(!TextUtils.isEmpty(msg)&&!Tools.isPhonticName(msg))
                            {
                                sToast(msg);
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

                    }
                });

            }
        });
    }

    /**
     * 活动控制
     */
    public void activeControl()
    {
        HttpConnect.post(MeActivity.this, "member_pic_activity", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = data.optJSONArray("data");
                            if(jsonArray!=null)
                            {
                                if(!TextUtils.isEmpty(data.getJSONArray("data").getJSONObject(0).getString("value")))
                                {
                                    if(data.getJSONArray("data").getJSONObject(0).getString("value").equals("1"))
                                    {
                                        Map<String, String> par = new HashMap<String, String>();
                                        HttpConnect.post(MeActivity.this, "member_activity_min_options_listtwo", par, new Callback() {

                                            @Override
                                            public void onResponse(Response arg0) throws IOException {
                                                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                                                if (active.get("status").equals("success")) {
                                                    MainActivity.isflag = true;
                                                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                                                    myActivity.setType(obj.optString("type"));
                                                    myActivity.setId(obj.optString("id"));
                                                    myActivity.setImageUrl(obj.optString("pic"));
                                                    myActivity.setStartDate(obj.optString("startdate"));
                                                    myActivity.setEndDate(obj.optString("enddate"));
                                                    myActivity.setCode(obj.optString("code"));
                                                    myActivity.setCode1(obj.optString("selfcode"));
                                                    myActivity.setRanking(obj.optString("ranking"));
                                                    myActivity.setTitle(obj.optString("title"));

                                                    try {
                                                        MeActivity.this.runOnUiThread(new Runnable() {
                                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                                            @Override
                                                            public void run() {
                                                                window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                                                                        WindowManager.LayoutParams.MATCH_PARENT);

                                                                window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                                                    @Override
                                                                    public void onDismiss() {
                                                                        WindowManager.LayoutParams lp3 = MainActivity.mainActivity.getWindow().getAttributes();
                                                                        lp3.alpha = 1f;
                                                                        MainActivity.mainActivity.getWindow().setAttributes(lp3);
                                                                    }
                                                                });

                                                                window.setOutsideTouchable(false);

//                                                                // 实例化一个ColorDrawable颜色为半透明
//                                                                ColorDrawable dw = new ColorDrawable(0x00ffffff);
//                                                                window.setBackgroundDrawable(dw);

                                                                // 设置popWindow的显示和消失动画
                                                                window.setAnimationStyle(R.style.Mypopwindow_anim_style);

                                                                if(!TextUtils.isEmpty(data.getJSONArray("data").getJSONObject(0).getString("pic")))
                                                                {
                                                                    new Thread(new Thread()
                                                                    {
                                                                        @Override
                                                                        public void run() {
                                                                            super.run();
                                                                            final Bitmap blurBitmap2 = FastBlurUtil.getInternetPicture(data.getJSONArray("data").getJSONObject(0).getString("pic"));
                                                                            runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    if(null!=blurBitmap2)
                                                                                    {
                                                                                        iv_into_authentication.setImageBitmap(blurBitmap2);
                                                                                        iv_cancel_active.setVisibility(View.VISIBLE);

                                                                                        // 在底部显示
                                                                                        try {
                                                                                            if(!MeActivity.this.isDestroyed())
                                                                                            {
                                                                                                window.showAtLocation(findViewById(R.id.linearLayout_into_share),
                                                                                                        Gravity.BOTTOM, 0, 0);

                                                                                            }
                                                                                        }catch (Exception e)
                                                                                        {

                                                                                        }


                                                                                        if(window!=null&&window.isShowing())
                                                                                        {
                                                                                            // 设置背景颜色变暗
                                                                                            WindowManager.LayoutParams lp5 = MainActivity.mainActivity.getWindow().getAttributes();
                                                                                            lp5.alpha = 0.5f;
                                                                                            MainActivity.mainActivity.getWindow().setAttributes(lp5);
                                                                                        }
                                                                                    }else
                                                                                    {
                                                                                        MainActivity.isflag = true;
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }).start();
                                                                }
                                                                activeId = data.getJSONArray("data").getJSONObject(0).getString("url");
                                                            }
                                                        });

                                                    }catch (Exception e)
                                                    {
                                                        activeControl();
                                                    }

                                                }else
                                                {
                                                    MainActivity.isflag = true;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Request arg0, IOException arg1) {
                                                MainActivity.isflag = true;
                                            }
                                        });
                                    }else
                                    {
                                        MainActivity.isflag = true;
                                    }

                                }
                            }
                        }
                    });
                }else
                {
                    MainActivity.isflag = true;
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                MainActivity.isflag = true;
            }
        });

    }



    public void getUrl(String id , final int position){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        HttpConnect.post(this, "member_activity_url", map, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    JSONObject obj = array.optJSONObject(0);
                    url = obj.optString("url");
                    title = obj.optString("title");
                    desc = obj.optString("description");

                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String date =  myActivity.getStartDate();
                            String ranking = myActivity.getRanking();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式
                            String date1 = df.format(new Date());

                            try {
                                if (Long.parseLong(Tools.dateToStamp(date1))>=Long.parseLong(Tools.dateToStamp(date)) && ranking.equals("1")){

                                    if (myActivity.getType().equals("1")){
                                        startActivity(new Intent( MeActivity.this,ContentActivity.class)
                                                .putExtra("code",myActivity.getCode())
                                                .putExtra("code1",myActivity.getCode1())
                                                .putExtra("url",url));
                                    }else if (myActivity.getType().equals("0")){
                                        startActivity(new Intent( MeActivity.this,Content1Activity.class)
                                                .putExtra("code",myActivity.getCode())
                                                .putExtra("code1",myActivity.getCode1())
                                                .putExtra("url",url));
                                    }


                                }else{

                                    startActivity(new Intent( MeActivity.this,IntroduceActivity.class)
                                            .putExtra("type",myActivity.getType())
                                            .putExtra("url",url)
                                            .putExtra("code",myActivity.getCode())
                                            .putExtra("code1",myActivity.getCode1())
                                            .putExtra("ranking",ranking)
                                            .putExtra("title",title)
                                            .putExtra("desc",desc)
                                            .putExtra("date",date));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }else{
                }
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }


    private void getPic()
    {

        final Map<String, String> par = new HashMap<String, String>();
        HttpConnect.post(this, "member_home_shuffle", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body()
                        .string());

                binnerList.clear();

                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.optJSONArray("data");
                    if(dataArr!=null)
                    {
                        for(int i = 0 ; i < dataArr.size() ; i ++)
                        {
                            JSONObject jsonObjNews = dataArr.optJSONObject(i);
                            if(jsonObjNews!=null)
                            {
                                String picpath = jsonObjNews.getString("pic");
                                String title = jsonObjNews.getString("title");
                                String description = jsonObjNews.getString("description");
                                String url = jsonObjNews.getString("url");
                                Product product = new Product("",picpath,url);
                                product.setDesc(description);
                                product.setTitle(title);
                                product.setIsActivityList(jsonObjNews.optString("jump"));
                                binnerList.add(product);
                            }
                        }
                        if(binnerList!=null&&binnerList.size()>0)
                        {
                            imageUrl.clear();

                            for(int i = 0 ; i < binnerList.size() ; i ++)
                            {
                                imageUrl.add(binnerList.get(i).getPicpath());
                            }
                        }
                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {

                            @Override
                            public void handleMessage(
                                    final Message msg) {
                                if(imageUrl!=null&&imageUrl.size()>0)
                                {
                                    //简单使用
                                    if(imageUrl!=null){
                                        banner.setImages(imageUrl)
                                                .setImageLoader(new GlideImageLoader(cif_loading))
                                                .setOnBannerClickListener(new OnBannerClickListener() {
                                                    @Override
                                                    public void OnBannerClick(int position) {

                                                        if (binnerList.get(position-1).getIsActivityList().equalsIgnoreCase("0")){
                                                            Intent intent = new Intent( MeActivity.this,GongGaoDetailActivity.class);
                                                            intent.putExtra("url", binnerList.get(position-1).getUrl());
                                                            intent.putExtra("title",binnerList.get(position-1).getTitle());
                                                            intent.putExtra("desc", binnerList.get(position-1).getDesc());
                                                            startActivity(intent);
                                                        }else if (binnerList.get(position-1).getIsActivityList().equalsIgnoreCase("1")){
                                                            startActivity(new Intent( MeActivity.this, ListActivity.class));
                                                        }else if (binnerList.get(position-1).getIsActivityList().equalsIgnoreCase("2")){
                                                            startActivity(new Intent( MeActivity.this, NoviceActivity.class));
                                                        }

                                                    }
                                                });
                                        banner.start();

                                    }
                                }
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                }else {

                }

                // mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }



    /**
     * 显示实名认证的提示框
     */
    public void show()
    {
        ImageView iv_into_authentication = (ImageView) renZhengView.findViewById(R.id.iv_into_authentication);
        ImageView iv_cancel_authentication = (ImageView) renZhengView.findViewById(R.id.iv_cancel_authentication);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renZhengWin.dismiss();
            }
        });

        iv_into_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( MeActivity.this,RealNameAuthenticationActivity.class));
                renZhengWin.dismiss();
            }
        });

        if(!renZhengWin.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 =  MainActivity.mainActivity.getWindow().getAttributes();
            lp5.alpha = 0.5f;
            MainActivity.mainActivity.getWindow().setAttributes(lp5);
            renZhengWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 =  MainActivity.mainActivity.getWindow().getAttributes();
                    lp3.alpha = 1f;
                    MainActivity.mainActivity.getWindow().setAttributes(lp3);
                }
            });

            renZhengWin.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            renZhengWin.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            renZhengWin.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            renZhengWin.showAtLocation(linearLayout_into_share,
                    Gravity.BOTTOM, 0, 0);
        }
    }



    private  void showNextKnownTipView()
    {
        MainActivity.view.setVisibility(View.GONE);
        MainActivity.tabWidget.setVisibility(View.GONE);

        mHightLight = new HighLight( MeActivity.this)//
                .anchor(findViewById(R.id.ll_me))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.ll_zichanbao, R.layout.info_known_assets,new OnRightPosCallback(20),new CircleLightShape())
                .addHighLight(R.id.ll_contact, R.layout.info_known_share,new OnBottomPosCallback(30),new CircleLightShape())
                .addHighLight(R.id.ll_option, R.layout.info_known_signed,new OnRightPosCallback(10),new CircleLightShape())
                .addHighLight(R.id.banner, R.layout.info_known_vip,new OnBottomPosCallback(10),new RectLightShape())
                .autoRemove(false)
                .enableNext()
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
                    @Override
                    public void onRemove() {
                        MainActivity.isflag = true;
                        MainActivity.view.setVisibility(View.VISIBLE);
                        MainActivity.tabWidget.setVisibility(View.VISIBLE);
                        linearLayout_into_share.setFocusable(true);
                        linearLayout_into_share.setFocusableInTouchMode(true);
                        linearLayout_into_share.requestFocus();
                        sharedpreferences.edit().putBoolean("isShow3",false).commit();
                        sharedpreferences.edit().putString("is","1").commit();
//                        Animation anim = AnimationUtils.loadAnimation(MeActivity.this,R.anim.novice_new_anim);
//                        iv_novice_new.startAnimation(anim);
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

            if(window!=null&&window.isShowing())
            {
                window.dismiss();
            }else
            {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    sToast("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();

                } else {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public String getusername() {
        return username;
    }

    @Override
    public String getpassword() {
        return password;
    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFail() {
//             sToast("链接失败");
    }



    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setRMBTextView(String rmbTextView) {

    }

    @Override
    public void setXiaoFeiAccountTextView(String rmbTextView) {

    }

    @Override
    public void setJueZhanTextView(String rmbTextView) {

    }

    @Override
    public void setTerminalCtcTextView(String terminalCtcTextView) {

    }

    @Override
    public void setDemandTextView(String demandTextView) {

    }

    @Override
    public TextView setTongBanTextView(String rmbTextView) {
        return  null;
    }

    @Override
    public TextView setTongXiTextView(String terminalCtcTextView) {
        return  null;
    }

    @Override
    public TextView setTongXiaoTextView(String demandTextView) {
        return  null;
    }

    @Override
    public void setShareTextView(String demandTextView) {

    }

    @Override
    public void setCurrentCTCPriceTextView(String demandTextView) {

    }

    @Override
    public void setallSsetsTextView(String allssets) {

    }

    @Override
    public LinearLayout getTwoLevel() {
        return null;
    }

    @Override
    public LinearLayout getThreeLevel() {
        return null;
    }

    @Override
    public void loadMemberInfoSuccess() {
        String phoneNum = "";
        try{
            phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode();
        }catch (Exception e){

        }

        if (!phoneNum.equals("") && null != phoneNum) {
            final String finalPhoneNum = phoneNum;
            PushServiceFactory.getCloudPushService().bindAccount(phoneNum, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
//					Toast.makeText(getApplicationContext(), "赞! 账号绑定成功 ~",
//							Toast.LENGTH_SHORT).show();
                    Log.i(SETTINGS_ACT, "@用户绑定账号 ：" + finalPhoneNum + " 成功");
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
//					Toast.makeText(getApplicationContext(), "衰! 账号绑定失败 ~",
//							Toast.LENGTH_SHORT).show();
                    Log.i(SETTINGS_ACT, "@用户绑定账号 ：" + finalPhoneNum + " 失败，原因 ： " + errorMessage);
                }
            });
        }
    }

    @Override
    public void loadMemberInfoFail() {
//        sToast("链接失败");
    }



    private void wxLogin(final String openId) {

        Map<String, String> par = new HashMap<String, String>();
        par.put("code", openId);
        par.put("password", "");
        par.put("type", "wechat");
        par.put("pic", "");
        par.put("nickname", "");
        HttpConnect.post(this, "member_login_v3", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
                    final String token = data.getJSONArray("data").getJSONObject(0).getString("token");
                    LoginUser.getInstantiation(MeActivity.this.getApplicationContext()).setToken(token);

                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            getMemberInfoPresenter = new GetMemberInfoPresenter(MeActivity.this,MeActivity.this);
                            getMemberInfoPresenter.VerificationTokenGetMemberInfo();

                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            if(!TextUtils.isEmpty(data.getString("msg")))
                            {
                                if(data.getString("msg").equals("登录失败"))
                                {
                                    Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                                    startActivity(intent5);
                                    finish();
                                }
                            }
//                            sToast("数据接口请求失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        MeActivity.this.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
//                        sToast("链接超时！");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    private void qqLogin(final String openId, final String accessToken) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("code", openId);
        par.put("password", "");
        par.put("type", "qq");
        par.put("pic", "");
        par.put("nickname", "");
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
        HttpConnect.post(this, "member_login_v3", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                final JSONObject data = JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {
                    getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
                    final String token = data.getJSONArray("data").getJSONObject(0).getString("token");
                    LoginUser.getInstantiation(MeActivity.this.getApplicationContext()).setToken(token);

                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            getMemberInfoPresenter = new GetMemberInfoPresenter(MeActivity.this,MeActivity.this);
                            getMemberInfoPresenter.VerificationTokenGetMemberInfo();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }else
                {
                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            if(!TextUtils.isEmpty(data.getString("msg")))
                            {
                                if(data.getString("msg").equals("登录失败"))
                                {
                                    Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                                    startActivity(intent5);
                                    finish();
                                }
                            }
//                            Toast.makeText(MeActivity.this, "数据接口请求失败", Toast.LENGTH_SHORT).show();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        MeActivity.this.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
//                        Toast.makeText(MeActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    private void getActivePicInfo()
    {
        HttpConnect.post(this, "member_first_pic", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                final JSONObject data = JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {

                    final String value = data.getJSONArray("data").getJSONObject(0).getString("value");
                    final String numberStr = data.getJSONArray("data").getJSONObject(0).getString("number");

                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            if(value!=null&&!TextUtils.isEmpty(value))
                            {
                                if(value.equals("0"))  //da
                                {
                                        ViewGroup.LayoutParams layoutParams = iv_cancel_active.getLayoutParams();
                                        int width = 260;
                                        double number = Double.parseDouble(numberStr);
                                       int height = (int) (width / number);
                                        //设置图片的高
                                        layoutParams.height = height;
                                        iv_cancel_active.setLayoutParams(layoutParams);
                                    iv_cancel_active.invalidate();
                                }else if(value.equals("1"))  //xiao
                                {
                                    ViewGroup.LayoutParams layoutParams = iv_cancel_active.getLayoutParams();
                                    int width = 260;
                                    double number = Double.parseDouble(numberStr);
                                        int height = (int) (width * number);
                                        //设置图片的高
                                        layoutParams.height = height;
                                        iv_cancel_active.setLayoutParams(layoutParams);
                                    iv_cancel_active.invalidate();
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }else
                {
                    Handler dataHandler = new Handler(
                            MeActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            if(!TextUtils.isEmpty(data.getString("msg")))
                            {
                                if(data.getString("msg").equals("登录失败"))
                                {
                                    Intent intent5 = new Intent(MeActivity.this, LoginMainActivity.class);
                                    startActivity(intent5);
                                    finish();
                                }
                            }
//                            Toast.makeText(MeActivity.this, "数据接口请求失败", Toast.LENGTH_SHORT).show();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        MeActivity.this.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
//                        Toast.makeText(MeActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    /**
     * 将输入流转换为bitmap 保存到内存中
     */
    protected  void saveFile(String url,String fileName)
    {
        // 取得的是inputstream，直接从inputstream生成bitmap
        try {
            InputStream inputStream = getImageStream(url);
            if(inputStream!=null)
            {
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                saveFile(mBitmap,fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *读取sd卡下图片，由图片路劲转换为bitmap
     * @param path   图片路径
     * @return       返回值为bitmap
     */
    public static Bitmap convertToBitmap(String path) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        // Calculate inSampleSize
//        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 从网络获取图片
     * @param path 加载图片的url
     */
    protected InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }


    /*
    * 保存文件
    */
    protected void  saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    @Override
    public void showUpdateDialog(String updateMsg, final String url5) {
        Button btnCencel = (Button) upgradeView.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) upgradeView.findViewById(R.id.btn_commit);
        TextView tvMsg = (TextView) upgradeView.findViewById(R.id.tv_msg);
        tvMsg.setText(updateMsg);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = url5;
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                MeActivity.this.startActivity(intent); //启动浏览器
            }
        });

        if (!upgradeWin.isShowing()) {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = MainActivity.mainActivity.getWindow().getAttributes();
            lp5.alpha = 0.5f;
            MainActivity.mainActivity.getWindow().setAttributes(lp5);
            upgradeWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 =  MainActivity.mainActivity.getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            upgradeWin.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            upgradeWin.setBackgroundDrawable(dw);

            // 在底部显示
            upgradeWin.showAtLocation(findViewById(R.id.linearLayout_into_share),
                    Gravity.BOTTOM, 0, 0);
        }

    }

    @Override
    public void loadFail(String mark) {
        LoginUser.LoginUserEntity loginUser = LoginUser.getInstantiation(getApplicationContext()).getLoginUser();
        if(loginUser!=null)
        {
            String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
            if(!TextUtils.isEmpty(phoneNum))
            {
//                if (sharedpreferences.getString("is","0").equals("1")){

                    showReceiveCouponWindow();
//                        activeControl();
//                }
            }else
            {
//                if (sharedpreferences.getString("is","0").equals("1")){
                    showBandPhoneWindow();
//                }
            }
        }
    }



}
