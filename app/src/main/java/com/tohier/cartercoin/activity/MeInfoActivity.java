package com.tohier.cartercoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.android.util.BitmapUtil;
import com.tohier.android.util.FileUtils;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.FastBlurUtil;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.wxapi.WXEntryActivity;

import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MeInfoActivity extends MyBaseActivity {

    private boolean isUpdate = false;
    private ImageView iv_back2,iv_sex;
    private String unionid;
    //qq
    private static final String QQ_APP_ID = "1105547483";
    public Tencent mTencent;

    private String QQopenID;

    //  解除微信为1
    public static int REMOVEBANDWECHAT = 1;
    //   解除qq为2
    public static int REMOVEBANDQQ = 2;
    // 解除微信Code
    public static String REMOVEBANDWECHATCODE = "member_unbundling_weichat";
    // 解除qqCode
    public static String REMOVEBANDQQCODE = "member_unbundling_qqid";

    //进入修改昵称的LinearLayout
    private LinearLayout linearlayout_into_update_member_nickname;
    //进入修改性别的LinearLayout
    private LinearLayout linearlayout_into_update_member_sex;
    //进入绑定手机号的LinearLayout
    private LinearLayout linearlayout_into_update_member_bandphone;
    //微信绑定的LinearLayout
    private LinearLayout linearlayout_into_update_member_bandwx;
    public IWXAPI api;
    //微信
    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    private TextView tv_sex,tv_wx_num,tv_qq_num,tv_phonenum;

    private CircleImageView iv_member_head_img;

    private View view;
    private PopupWindow removeBandWin = null;

    //修改头像的view
    private View view1;

    private Button btn_tuku, btn_camera, btn_quxiao;
    private PopupWindow window;

    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String TAG = "RegisterInfoActivity";
    private Uri uritempFile;

    private Bitmap photo;

    private TextView tv_nickname;

    private ImageView iv_info_bg;

    private TextView tv_autograph;

    //进入会员升级的LinearLayout
    private LinearLayout linearlayout_into_update_member_bandqq;
    private LinearLayout linearlayout_into_update_member_autograph;
    private LinearLayout linearLayout_into_qrcode;

    String replaceBg;

    private TextView tv_linkcode,tv_location,tv_member_level,tv_mining_level;

    private LoadingView loadview;

    private LinearLayout linearLayout_minning_level;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.linearlayout_into_update_member_nickname:
                    startActivity(new Intent(MeInfoActivity.this,UpdateNickNameActivity.class));
                    break;
                case R.id.linearlayout_into_update_member_sex:
                    startActivity(new Intent(MeInfoActivity.this,UpdateSexActivity.class));
                    break;
                case R.id.linearlayout_into_update_member_bandphone:
                    Intent intent = null;
                    String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
                    if(TextUtils.isEmpty(phoneNum)&&phoneNum.equals("")) {
                        intent = new Intent(MeInfoActivity.this, BandPhoneActivity.class);
                    }else
                    {
                        intent = new Intent(MeInfoActivity.this, UpdateBandPhoneActivity.class);
                    }
                    if(intent!=null)
                    {
                        startActivity(intent);
                    }
                    break;
                case R.id.linearlayout_into_update_member_bandwx:
                    if(!TextUtils.isEmpty(tv_wx_num.getText().toString().trim()))
                    {
                        if(tv_wx_num.getText().toString().trim().equals("绑定"))
                        {
                            myProgressDialog.show();
                            getSharedPreferences("weixinlogin", Context.MODE_APPEND).edit().putString("login","bandphone").commit();

                            api = WXAPIFactory.createWXAPI(getApplicationContext(), WX_APP_ID, true);
                            api.registerApp(WX_APP_ID);
                            final SendAuth.Req req = new SendAuth.Req();
                            req.scope = "snsapi_userinfo";
                            req.state = "wechat_sdk_demo_test";
                            if (!api.sendReq(req)) {
                                sToast("请您安装微信");
                            }
                        }else if(tv_wx_num.getText().toString().trim().equals("解绑"))
                        {
                            showRemoveBandWin(REMOVEBANDWECHAT);
                        }
                    }
                    break;
                case R.id.linearlayout_into_update_member_bandqq:
                    if(!TextUtils.isEmpty(tv_wx_num.getText().toString().trim())) {
                        if(tv_qq_num.getText().toString().trim().equals("绑定")){
                            myProgressDialog.show();
                            mTencent = Tencent.createInstance(QQ_APP_ID, getApplicationContext());
                            if (!mTencent.isSessionValid()) {
                                mTencent.login(MeInfoActivity.this, "get_simple_userinfo", listener);
                            } else {
                                mTencent.logout(getApplicationContext());
                                mTencent.login(MeInfoActivity.this, "get_simple_userinfo", listener);
                            }
                        }else if(tv_qq_num.getText().toString().trim().equals("解绑")){
                            showRemoveBandWin(REMOVEBANDQQ);
                        }
                    }
                    break;
                case R.id.iv_member_head_img:
                    replaceBg = "headImg";
                    window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);

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

                    // 设置popWindow的显示和消失动画
                    window.setAnimationStyle(R.style.Mypopwindow_anim_style);
                    // 在底部显示
                    window.showAtLocation(findViewById(R.id.iv_back2),
                            Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.iv_back2:
                    finish();
                    break;
                case R.id.linearlayout_into_update_member_autograph:
                    startActivity(new Intent(MeInfoActivity.this,UpdateAutographActivity.class));
                    break;
                case R.id.linearLayout_into_qrcode:
                    startActivity(new Intent(MeInfoActivity.this,MyQrCode.class));
                    break;
                case R.id.iv_info_bg:
                    replaceBg = "infobgimg";
                    window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);

                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp58 = getWindow().getAttributes();
                    lp58.alpha = 0.5f;
                    getWindow().setAttributes(lp58);
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
                    ColorDrawable dw2 = new ColorDrawable(0x00ffffff);
                    window.setBackgroundDrawable(dw2);

                    // 设置popWindow的显示和消失动画
                    window.setAnimationStyle(R.style.Mypopwindow_anim_style);
                    // 在底部显示
                    window.showAtLocation(findViewById(R.id.iv_back2),
                            Gravity.BOTTOM, 0, 0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info_layout);
        initData();
        setUpView();
        loadMemberInfo();

        WXEntryActivity.setBandSuccess(new WXEntryActivity.BandSuccess() {
            @Override
            public void sueesss() {
                loadMemberInfo();
            }
        });

    }


    @Override
    public void initData() {
        myProgressDialog.setTitle("正在加载...");
        view = View.inflate(this,R.layout.activity_prompt,null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("解绑");
        ((TextView)view.findViewById(R.id.tv_cancel_pay)).setText("确定即解绑");
        removeBandWin = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        view1 = View.inflate(this, R.layout.photo_popupwindow_item, null);
        btn_tuku = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_photo);
        btn_camera = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_camera);
        btn_quxiao = (Button) view1
                .findViewById(R.id.photo_popupwindow_item_look);

        linearLayout_minning_level = (LinearLayout) this.findViewById(R.id.linearLayout_minning_level);

        /**
         * 头像处理
         */
        btn_tuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                }else{
                    MeInfoActivity.this.sToast("请先插入SD卡");
                }
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }else{
                    MeInfoActivity.this.sToast("请先插入SD卡");
                }
            }
        });

        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.checkSDCard()){
                    window.dismiss();
                }
            }
        });

        tv_nickname = (TextView) this.findViewById(R.id.tv_nickname);
        tv_autograph = (TextView) this.findViewById(R.id.tv_autograph);
        tv_linkcode = (TextView) this.findViewById(R.id.tv_linkcode);
        tv_location = (TextView) this.findViewById(R.id.tv_location);
        tv_member_level = (TextView) this.findViewById(R.id.tv_member_level);
        tv_mining_level = (TextView) this.findViewById(R.id.tv_mining_level);
        iv_info_bg = (ImageView) this.findViewById(R.id.iv_info_bg);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);

        loadview = (LoadingView) findViewById(R.id.loadview);

        linearlayout_into_update_member_bandqq = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandqq);
        linearlayout_into_update_member_nickname = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_nickname);
        linearlayout_into_update_member_sex = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_sex);
        linearlayout_into_update_member_bandphone = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandphone);
        linearlayout_into_update_member_autograph = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_autograph);
        linearLayout_into_qrcode = (LinearLayout) this.findViewById(R.id.linearLayout_into_qrcode);
        tv_phonenum = (TextView) this.findViewById(R.id.tv_phonenum);
        linearlayout_into_update_member_bandwx = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_bandwx);
        iv_member_head_img = (CircleImageView) this.findViewById(R.id.iv_member_head_img);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        //链接码
//        tv_linkcode = (TextView) this.findViewById(R.id.tv_linkcode);
        //性别
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        //显示微信是否绑定
        tv_wx_num = (TextView) this.findViewById(R.id.tv_wx_num);
        //显示qq是否绑定
        tv_qq_num = (TextView) this.findViewById(R.id.tv_qq_num);
        tv_phonenum = (TextView) this.findViewById(R.id.tv_phonenum);

        HttpConnect.post(this, "member_hide_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String min = data.getJSONArray("data").getJSONObject(0).getString("minning");

                    MeInfoActivity.this.runOnUiThread(new Runnable() {
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
    }

    private void setUpView() {
        iv_member_head_img.setOnClickListener(onClickListener);
        iv_info_bg.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandqq.setOnClickListener(onClickListener);
        linearlayout_into_update_member_nickname.setOnClickListener(onClickListener);
        linearlayout_into_update_member_sex.setOnClickListener(onClickListener);
        linearLayout_into_qrcode.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandphone.setOnClickListener(onClickListener);
        linearlayout_into_update_member_autograph.setOnClickListener(onClickListener);
        linearlayout_into_update_member_bandwx.setOnClickListener(onClickListener);
        iv_back2.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isUpdate==false)
        {
            loadMemberInfo();
        }
        String phoneNum = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getPhoneNum();
        if(TextUtils.isEmpty(phoneNum)||phoneNum.equals(""))
        {
            tv_phonenum.setText("绑定");
        }else
        {
            tv_phonenum.setText(phoneNum.substring(0,3)+"****"+phoneNum.substring(7,11));
        }

        myProgressDialog.cancel();
    }


    private String mobile;
    private String idnumber;
    private String qqopenid;
    private String wechatopenid;
    private String agent;
    private String introducermobile;
    private String sex;
    private String introducerid;
    private String name;
    private String gesturepassword;
    private String linkcode;
    private String type;
    private String nickname;
    private String pic;
    private String birthday;
    private String id;
    private String oneautograph;
    private String location;
    private String miningLevel;
    private String backgroundpic;


    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {
            sToast("您取消了QQ登录");
            myProgressDialog.cancel();
        }

        @Override
        public void onComplete(Object arg0) {
            sToast("进入授权");
            org.json.JSONObject jo = (org.json.JSONObject) arg0;

            initLoginID(jo);
            myProgressDialog.cancel();
        }

        @Override
        public void onError(UiError arg0) {
            sToast("QQ登录出错");
            myProgressDialog.cancel();
        }
    };

    private void initLoginID(org.json.JSONObject jsonObject) {
        try {
            if (jsonObject.getString("ret").equals("0")) {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                QQopenID = jsonObject.getString(Constants.PARAM_OPEN_ID);
                getQQUnionid(token,expires);
//                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
//                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
//                QQopenID = jsonObject.getString(Constants.PARAM_OPEN_ID);
//                mTencent.setOpenId(QQopenID);
//                mTencent.setAccessToken(token, expires);
//                getuserInfo();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    /**
     * 获取QQ的unionid
     */
    private void getQQUnionid(final String token, final String expires) {
        MyNetworkConnection.getNetworkConnection(MeInfoActivity.this).post("get", "https://graph.qq.com/oauth2.0/me?access_token="+ token+"&unionid=1", null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        sToast("请检查您的网络链接状态");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                byte[] data = response.body().bytes();
                String s = new String(data);

                String[] split = s.split(":");
                s = split[split.length - 1];
                split = s.split("\"");
                s = split[1];
                unionid = s;
                mTencent.setOpenId(QQopenID);
                mTencent.setAccessToken(token, expires);
                getuserInfo();
            }
        });
    }

    private void getuserInfo() {
        UserInfo qqInfo = new UserInfo(this, mTencent.getQQToken());
        qqInfo.getUserInfo(getQQinfoListener);
    }

    private IUiListener getQQinfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                org.json.JSONObject jsonObject = (org.json.JSONObject) response;

                int ret = jsonObject.getInt("ret");

                String headImgUrl = jsonObject.getString("figureurl_qq_2");
                String nickname = jsonObject.getString("nickname");

                Map<String, String> par1 = new HashMap<String, String>();
                par1.put("id", LoginUser.getInstantiation(getApplicationContext())
                        .getLoginUser().getUserId());
                par1.put("openid", unionid);

                HttpConnect.post(MeInfoActivity.this, "member_modify_qq", par1, new Callback() {

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        Handler dataHandler = new Handler(
                                getContext().getMainLooper()) {
                            @Override
                            public void handleMessage(
                                    final Message msg)
                            {
//                                sToast("请检查你的网络状态");
                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        JSONObject object = JSONObject.fromObject(arg0.body().string());
                        if (object.get("status").equals("success")) {
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {
                                @Override
                                public void handleMessage(
                                        final Message msg)
                                {
                                    sToast("QQ已绑定成功");
                                    tv_qq_num.setText("解绑");
                                    qqopenid = unionid;
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }else
                        {
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {
                                @Override
                                public void handleMessage(
                                        final Message msg)
                                {
                                    sToast("该QQ已有用户绑定");
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    }
                });
                //处理自己需要的信息
            } catch (Exception e) {

            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        loadview.setVisibility(View.VISIBLE);
        HttpConnect.post(MeInfoActivity.this, "member_info", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    id = data.getJSONArray("data").getJSONObject(0).getString("ID");
                    birthday = data.getJSONArray("data").getJSONObject(0).getString("birthday");
                    pic = data.getJSONArray("data").getJSONObject(0).getString("pic");
                    nickname = data.getJSONArray("data").getJSONObject(0).getString("nickname");
                    type = data.getJSONArray("data").getJSONObject(0).getString("type");
                    linkcode = data.getJSONArray("data").getJSONObject(0).getString("linkcode");
                    gesturepassword = data.getJSONArray("data").getJSONObject(0).getString("gesturespassword");
                    name = data.getJSONArray("data").getJSONObject(0).getString("name");
                    introducerid = data.getJSONArray("data").getJSONObject(0).getString("introducerid");
                    sex = data.getJSONArray("data").getJSONObject(0).getString("sex");
                    introducermobile = data.getJSONArray("data").getJSONObject(0).getString("introducermobile");
                    agent = data.getJSONArray("data").getJSONObject(0).getString("agent");
                    wechatopenid = data.getJSONArray("data").getJSONObject(0).getString("wechatopenid");
                    qqopenid = data.getJSONArray("data").getJSONObject(0).getString("qqopenid");
                    idnumber = data.getJSONArray("data").getJSONObject(0).getString("idnumber");
                    mobile = data.getJSONArray("data").getJSONObject(0).getString("mobile");
                    oneautograph = data.getJSONArray("data").getJSONObject(0).getString("oneautograph");

                    location = data.getJSONArray("data").getJSONObject(0).getString("Address");

                    miningLevel = data.getJSONArray("data").getJSONObject(0).getString("grade");
                    backgroundpic = data.getJSONArray("data").getJSONObject(0).getString("backgroundpic");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void handleMessage(final Message msg) {
//                            gif_loading.setVisibility(View.GONE);
//                            loadInfoComplete();
                            /**
                             * 设置头像  以及昵称
                             */
                            if (pic!=null&&!pic.equals("")){
                                if(!MeInfoActivity.this.isDestroyed())
                                {
                                    Glide.with(MeInfoActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_head_img) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });

                                    if(backgroundpic.equals("")||backgroundpic.equals(pic))
                                    {
                                        new Thread(new Thread()
                                        {
                                            @Override
                                            public void run() {
                                                super.run();
                                                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(pic,2);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        iv_info_bg.setImageBitmap(blurBitmap2);
                                                        loadview.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        }).start();
                                    }else
                                    {
                                        Glide.with(MeInfoActivity.this).load(backgroundpic).placeholder(null)
                                                .error(null).into( iv_info_bg);
                                        loadview.setVisibility(View.GONE);
                                    }
                                }

                            }else
                            {
                                iv_member_head_img.setImageResource(R.mipmap.iv_member_default_head_img);
                                iv_info_bg.setImageResource(R.mipmap.iv_member_info_bg);
//                                iv_info_bg.setImageResource(R.mipmap.iv_member_default_head_img);
                                loadview.setVisibility(View.GONE);
                            }

                            if(!TextUtils.isEmpty(linkcode))
                            {
                                tv_linkcode.setText(linkcode);
                            }


                           if(!TextUtils.isEmpty(nickname))
                            {
                                tv_nickname.setText(nickname);
                            }

                            if(!TextUtils.isEmpty(oneautograph))
                            {
                                tv_autograph.setText(oneautograph);
                            }

                            if(!TextUtils.isEmpty(miningLevel))
                            {
                                tv_mining_level.setText(miningLevel);
                            }

                            if(!TextUtils.isEmpty(location))
                            {
                                tv_location.setText(location);
                            }

                            if(!TextUtils.isEmpty(sex))
                            {
                                if(sex.equals("保密"))
                                {
                                    iv_sex.setVisibility(View.GONE);
                                    tv_sex.setText("保密");
                                }else if(sex.equals("美女"))
                                {
                                    tv_sex.setText("美女");
                                    iv_sex.setVisibility(View.VISIBLE);
                                    iv_sex.setImageResource(R.mipmap.girl);
                                }else if(sex.equals("帅哥"))
                                {
                                    tv_sex.setText("帅哥");
                                    iv_sex.setVisibility(View.VISIBLE);
                                    iv_sex.setImageResource(R.mipmap.boy);
                                }
                            }

                            if(!TextUtils.isEmpty(wechatopenid))
                            {
                                tv_wx_num.setText("解绑");
                            }else
                            {
                                tv_wx_num.setText("绑定");
                            }

                            if(!TextUtils.isEmpty(qqopenid))
                            {
                                tv_qq_num.setText("解绑");
                            }else
                            {
                                tv_qq_num.setText("绑定");
                            }

                            if(!TextUtils.isEmpty(type))
                            {
                                if(type.equals("10"))
                                {
                                    tv_member_level.setText("注册会员");
                                }else if(type.equals("20"))
                                {
                                    tv_member_level.setText("铂金VIP会员");
                                }else if(type.equals("30"))
                                {
                                    tv_member_level.setText("钻石VIP会员");
                                }else if(type.equals("40"))
                                {
                                    tv_member_level.setText("皇冠VIP会员");
                                }
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
                            loadview.setVisibility(View.GONE);
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
                        loadview.setVisibility(View.GONE);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 显示解除绑定的提示框
     */
    public void showRemoveBandWin(final int type)
    {
        if(type==REMOVEBANDQQ)
        {
            ((TextView)view.findViewById(R.id.tv_prompt)).setText("是否要解绑QQ");
        }else if(type==REMOVEBANDWECHAT)
        {
            ((TextView)view.findViewById(R.id.tv_prompt)).setText("是否要解绑微信");
        }

        Button btnCencel1 = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit1 = (Button) view.findViewById(R.id.btn_commit);

        btnCencel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBandWin.dismiss();
            }
        });

        btnCommit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==REMOVEBANDQQ){
                    removeBand(REMOVEBANDQQCODE,removeBandWin);
                }else if(type==REMOVEBANDWECHAT){
                    removeBand(REMOVEBANDWECHATCODE,removeBandWin);
                }
            }
        });

        if(!removeBandWin.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = getWindow().getAttributes();
            lp5.alpha = 0.5f;
            getWindow().setAttributes(lp5);
            removeBandWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = getWindow().getAttributes();
                    lp3.alpha = 1f;
                    getWindow().setAttributes(lp3);
                }
            });

            removeBandWin.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            removeBandWin.setBackgroundDrawable(dw);

            // 在底部显示
            removeBandWin.showAtLocation(findViewById(R.id.iv_back2),
                    Gravity.BOTTOM, 0, 0);
        }
    }

    public void removeBand(final String code, final PopupWindow popupWindow){
        Map<String,String> par = new HashMap();
        if(code.equals(REMOVEBANDQQCODE))
        {
            par.put("qqid",qqopenid);
        }else if(code.equals(REMOVEBANDWECHATCODE))
        {
            par.put("wechatid",wechatopenid);
        }
        HttpConnect.post(MeInfoActivity.this, code, par, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String value = data.getJSONArray("data").getJSONObject(0).getString("value");
                            if(!TextUtils.isEmpty(value)&&value.equals("1"))
                            {
                                if(code.equals(REMOVEBANDQQCODE))
                                {
                                    tv_qq_num.setText("绑定");
                                }else if(code.equals(REMOVEBANDWECHATCODE))
                                {
                                    tv_wx_num.setText("绑定");
                                }
                            }
                            popupWindow.dismiss();
                        }
                    });

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(data.optString("msg")!=null&&!TextUtils.isEmpty(data.optString("msg")))
                            {
                                sToast(data.optString("msg"));
                            }
                            window.dismiss();
                        }
                    });
                }

            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        window.dismiss();
//                        sToast("请检查您的网络链接状态");
                    }
                });
            }
        });

    }

    /**
     * 裁剪图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size*0.2+size);
        intent.putExtra("outputY", size*0.8);

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        // intent.putExtra("return-data", true);

        // uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/"
                + Environment.getExternalStorageDirectory().getPath() + "/"
                + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CROP_REQUEST_CODE);
    }


    public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 照相必备1
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
            return;
        }

        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i(TAG, "相册，开始裁剪");
                Log.i(TAG, "相册 [ " + data + " ]");
                if (data == null) {
                    return;
                }
                startPhotoZoom(data.getData(), 100);
                break;
            case CAMERA_REQUEST_CODE:
                Log.i(TAG, "相机, 开始裁剪");
                File picture = new File(Environment.getExternalStorageDirectory()+ "/temp.jpg");
                startPhotoZoom(Uri.fromFile(picture), 100);
                break;
            case CROP_REQUEST_CODE:
                Log.i(TAG, "相册裁剪成功");
                Log.i(TAG, "裁剪以后 [ " + data + " ]");
                //获取目标控件的大小
                int targetW = iv_info_bg.getWidth();
                int targetH = iv_info_bg.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                try {
                    //inJustDecodeBounds为true，可以加载源图片的尺寸大小，decodeStream方法返回的bitmap为null
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(uritempFile), null, bmOptions);
                    // 得到源图片的尺寸
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;
                    //通过比较获取较小的缩放比列
                    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                    // 将inJustDecodeBounds置为false，设置bitmap的缩放比列
                    bmOptions.inJustDecodeBounds = false;
                    if (scaleFactor != 1){
                        if (scaleFactor>=4){

                            if (scaleFactor>=5){
                                if (scaleFactor>=6){
                                    if (scaleFactor>=6){

                                        bmOptions.inSampleSize = scaleFactor+10;
                                    }else{
                                        bmOptions.inSampleSize = scaleFactor+8;
                                    }
                                }else{
                                    bmOptions.inSampleSize = scaleFactor+7;
                                }
                            }else{
                                bmOptions.inSampleSize = scaleFactor+6;
                            }

                        }else{
                            bmOptions.inSampleSize = scaleFactor+5;
                        }

                    }else{
                        bmOptions.inSampleSize = scaleFactor+2;
                    }

                    bmOptions.inPurgeable = true;
                    //再次decode获取bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(uritempFile), null, bmOptions);
                    photo = bitmap;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                photo = zoomImage(photo,450,450);

                if(replaceBg.equals("headImg"))
                {
                    if(backgroundpic.equals("")||backgroundpic.equals(pic))
                    {
                        iv_member_head_img.setImageBitmap(photo);
                        iv_info_bg.setImageBitmap(photo);
                    }else
                    {
                        iv_member_head_img.setImageBitmap(photo);
                    }
                    updateMemberInfo();
                }else if(replaceBg.equals("infobgimg"))
                {
                    isUpdate = true;
                    setBackGroundPic();
                }
                break;
        }

    }

    public void updateMemberInfo()
    {
//        gif_loading.setVisibility(View.VISIBLE);
        Map<String, String> par1 = new HashMap<String, String>();
        if (photo == null) {
            par1.put("pic", pic);
        } else {
            par1.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
        }
        par1.put("nickname", nickname);
        par1.put("name", name);
        par1.put("birthday", birthday);

        String s = sex;
        if (s.equals("保密")){
            par1.put("sex", "-1");
        }else if (s.equals("帅哥")){
            par1.put("sex", "1");
        }else if (s.equals("美女")){
            par1.put("sex", "0");
        }
        par1.put("qqopenid", qqopenid);
        par1.put("wechatopenid", wechatopenid);
        par1.put("idnumber", idnumber);
        par1.put("phonenunber",mobile );

        HttpConnect.post(this, "member_update_information", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
//                            gif_loading.setVisibility(View.GONE);
                            MeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (photo == null) {
                                        Glide.with(MeInfoActivity.this).load(pic).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_head_img) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                iv_member_head_img.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                        Glide.with(MeInfoActivity.this).load(pic).placeholder(null)
                                                .error(null).into( iv_info_bg);
                                    } else {
                                        if(backgroundpic.equals("")||backgroundpic.equals(pic)) {
                                            iv_member_head_img.setImageBitmap(photo);
                                            iv_info_bg.setImageBitmap(photo);
                                        }else
                                        {
                                            iv_member_head_img.setImageBitmap(photo);
                                        }
                                    }
                                    sToast("修改成功");
                                    mIMKit.getContactService().clearContactInfoCache(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getLinkCode(), MyApplication.APP_KEY);

                                }
                            });

                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
//                            gif_loading.setVisibility(View.GONE);
                            sToast("修改失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }


    public void setBackGroundPic()
    {
//        gif_loading.setVisibility(View.VISIBLE);
        Map<String, String> par1 = new HashMap<String, String>();
        if (photo == null) {
            par1.put("backgroundpic", backgroundpic);
        } else {
            par1.put("upfile:pic", BitmapUtil.bitmapToBase64(photo));
        }

        HttpConnect.post(this, "member_background_pic", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {
                        isUpdate = false;
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
//                            gif_loading.setVisibility(View.GONE);
                            isUpdate = false;
                            MeInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_info_bg.setImageBitmap(photo);
                                }
                            });

                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            isUpdate = false;
//                            gif_loading.setVisibility(View.GONE);
                            sToast("修改失败");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
    }

}
