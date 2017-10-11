package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.biz.LoadLoginPicBiz;
import com.tohier.cartercoin.columnview.MyProgressDialog;
import com.tohier.cartercoin.config.DataManagerTools;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.IPUtil;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.PhoneLoginPresenter;
import com.tohier.cartercoin.presenter.WxLoginPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.PhoneLoginView;
import com.tohier.cartercoin.ui.WxLoginView;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 武文锴 on 2016/11/4.
 * This is 登录页面
 */

public class LoginMainActivity extends MyBaseActivity implements WxLoginView,GetMemberInfoView,PhoneLoginView {

    /**
     * qq登录的appId
     */
    public static final String QQ_APP_ID = "1105547483";
    private Tencent mTencent;
    /**
     * qq登录得到的openId
     **/
    private String openID;


    /**
     *用于微信登录 负责完成View于Model间的交互
     **/
    private WxLoginPresenter wxLoginPresenter;
    /**
     *  登录页面的背景logo
     **/
//    private ImageView login_ctc_icon;

    /**
     *  点击进入手机登录的button组件
     **/
    private Button btn_into_phone_register;

    /**
     *  点击进入QQ登录的textview组件
     */
//    private TextView tv_qq_login;

    /**
     * 获取会员信息的presenter
     */
    private GetMemberInfoPresenter getMemberInfoPresenter;

    /**
     * 同意协议的变量值
     * @param savedInstanceState
     *
     */
    private boolean agreementIsChecked = false;

    private ImageView checkbox_agreement,iv_pwd_isShow;
    private TextView tv_into_agreement;

    private LinearLayout linearLayout_qq_login,linearLayout_weixin_login;

    PopupWindow window = null;
    private View view1;

    //   wechat    and    qq
    private String loginType = "";
    private String unionid;

    private TextView tv_forget_pwd,tv_code;
    private EditText et_name,et_password,et_code;
    private Button btn_code;
    private LinearLayout ll_code,ll_pwd;
    /**
     *用于手机号登录 负责完成View于Model间的交互
     **/
    private PhoneLoginPresenter phoneLoginPresenter;
    /**
     * 登录按钮
     */
    private Button btn_phoneLogin;
    private View view;


    private int count = 120;

    private int timeOut = 0;


    private String type = "mobile";

    public static MyProgressDialog myProgressDialog1;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btn_code.setText("获取短信验证码");
                    btn_code.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
                    btn_code.setText("验证码已发送（"+count+"s）");
                    btn_code.setClickable(false);
                    postDelayed(thread,1000);
                }
            }else  if(msg.what == 0x112){
                myProgressDialog.cancel();
                sToast("网络质量不佳，请检查网络！");
            }else  if(msg.what == 0x113){
                timeOut++;
                if (timeOut >= 7){
                    myProgressDialog.cancel();
                    timeOut = 0;
                }else {
                    postDelayed(thread2,1000);
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
    Thread thread1 = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x112);
        }
    };

    Thread thread2 = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x113);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //将IndexActivity加入销毁队列中  防止从本页面进入手机登录页面成功进行登录 要及时关闭改页面
        MyApplication.maps.put("LoginMainActivity",this);

        initData();
        setUpView();
    }

    @Override
    public void initData() {

        myProgressDialog.setTitle("正在登录...");
        this.myProgressDialog1 = myProgressDialog;

        checkbox_agreement = (ImageView) this.findViewById(R.id.checkbox_agreement);
        view = this.findViewById(R.id.view);

        checkbox_agreement.setImageResource(R.mipmap.iv_agreement_no_checked);
        wxLoginPresenter = new WxLoginPresenter(this,this);
        btn_into_phone_register = (Button) this.findViewById(R.id.btn_phone_register);
//        tv_qq_login = (TextView) this.findViewById(R.id.tv_qq_login);
        tv_into_agreement = (TextView) this.findViewById(R.id.tv_into_agreement);
        tv_forget_pwd = (TextView) this.findViewById(R.id.tv_forget_pwd);
        linearLayout_weixin_login = (LinearLayout) this.findViewById(R.id.linearLayout_weixin_login);
        linearLayout_qq_login = (LinearLayout) this.findViewById(R.id.linearLayout_qq_login);

        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        et_name = (EditText) this.findViewById(R.id.et_name);
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_code = (EditText) this.findViewById(R.id.et_code);

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btn_phoneLogin = (Button) this.findViewById(R.id.btn_phoneLogin);
        btn_code = (Button) this.findViewById(R.id.btn_code);

        tv_code = (TextView) this.findViewById(R.id.tv_code);
        ll_code = (LinearLayout) this.findViewById(R.id.ll_code);
        ll_pwd = (LinearLayout) this.findViewById(R.id.ll_pwd);

        try{
            String s = Tools.getPhoneNumber(this);
            if (s.toString().contains(" ")) {
                String[] str = s.toString().split(" ");
                String str1 = "";
                for (int i = 0; i < str.length; i++) {
                    str1 += str[i];
                }
                et_name.setText(str1);
            }
        }catch(Exception e){

        }
        //获取会员信息的presenter
        getMemberInfoPresenter = new GetMemberInfoPresenter(this,this,"phoneLogin");
        phoneLoginPresenter = new PhoneLoginPresenter(this,this,getMemberInfoPresenter);
        /**
         * 查看登录页面logo是否更新 更新的话 从本地获取 设置上去
         */
//        Bitmap button_pic = LoadLoginPicBiz.convertToBitmap(LoadLoginPicBiz.ALBUM_PATH+LoadLoginPicBiz.BUTTON_PIC);
//        if(button_pic!=null)
//        {
//            iv_login_wx_icon_bg.setBackgroundDrawable(Drawable.createFromPath(LoadLoginPicBiz.ALBUM_PATH+LoadLoginPicBiz.BUTTON_PIC));
//        }
        Bitmap background_pic = LoadLoginPicBiz.convertToBitmap(LoadLoginPicBiz.ALBUM_PATH+LoadLoginPicBiz.BACKGROUND_PIC);
        if(background_pic!=null)
        {
//            login_ctc_icon.setImageBitmap(background_pic);
        }

        view1 = View.inflate(this,R.layout.popupwindow_weihu_update,null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        agreementIsChecked = true;
        checkbox_agreement.setImageResource(R.mipmap.iv_agreement_checked);
    }

    private void setUpView() {

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    et_name.setText(str1);
                    et_name.setSelection(start);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = "login";
                isMaintain();
            }
        });

        iv_pwd_isShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManagerTools.TextVisiblePassword(iv_pwd_isShow,et_password);
            }
        });

        tv_into_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMainActivity.this,LoginXieyiActivity.class));
            }
        });

        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = "forgetLoginPwd";
                isMaintain();
            }
        });

        checkbox_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreementIsChecked==false)
                {
                    agreementIsChecked = true;
                    checkbox_agreement.setImageResource(R.mipmap.iv_agreement_checked);
                }else
                {
                    agreementIsChecked = false;
                    checkbox_agreement.setImageResource(R.mipmap.iv_agreement_no_checked);
                }
            }
        });

        btn_into_phone_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreementIsChecked)
                {
                    loginType = "register";
                    isMaintain();
                }else
                {
                    sToast("请查看条款与协议");
                }
            }
        });

        linearLayout_qq_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreementIsChecked)
                {
                    loginType = "qq";
                    isMaintain();
                }else
                {
                    sToast("请查看条款与协议");
                }
            }
        });

        linearLayout_weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreementIsChecked)
                {
                    loginType = "wechat";
                    isMaintain();
                }else
                {
                    sToast("请查看条款与协议");
                }
            }
        });

        tv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_code.getText().toString().equals("验证码登录")){
                    tv_code.setText("密码登录");
                    ll_pwd.setVisibility(View.GONE);
                    ll_code.setVisibility(View.VISIBLE);
                    type = "sms";
                }else if (tv_code.getText().toString().equals("密码登录")){
                    tv_code.setText("验证码登录");
                    ll_pwd.setVisibility(View.VISIBLE);
                    ll_code.setVisibility(View.GONE);
                    type = "mobile";
                }
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_name.getText().toString().trim()))
                {
                    sToast("请输入手机号");
                    return;
                }
                if(et_name.getText().toString().trim().length()!=11)
                {
                    sToast("手机号输入不合法");
                    return;
                }
                String phone = et_name.getText().toString();
                if (StringUtils.isNotBlank(phone) && NumberUtils.isNumber(phone) && phone.length() == 11) {
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("mobile", phone);
                    par.put("code","member_login_v3");
                    HttpConnect.post(LoginMainActivity.this, "sms_member_code", par, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            JSONObject data = null;
                            try {
                                data = new JSONObject(arg0.body().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (data.optString("status").equals("success")) {
                                handler.post(thread);
                            } else {
                                sToast(data.optString("msg"));
                            }

                        }

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                        }
                    });
                } else {
                    sToast("请填入正确的手机号!");
                }
            }
        });
    }


    /**
     * qq登录的监听器
     */
    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginMainActivity.this, "你取消了QQ登录", Toast.LENGTH_SHORT).show();
                    myProgressDialog.cancel();
                }
            });
        }

        @Override
        public void onComplete(Object arg0) {
           JSONObject jo = (org.json.JSONObject) arg0;
            initLoginID(jo);
            myProgressDialog.cancel();
            myProgressDialog.setTitle("正在加载...");
            myProgressDialog.show();
        }

        @Override
        public void onError(UiError arg0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginMainActivity.this, "QQ登录出错", Toast.LENGTH_SHORT).show();
                    myProgressDialog.cancel();
                }
            });

        }
    };

    private void initLoginID(JSONObject jsonObject) {
            try {
                if (jsonObject.getString("ret").equals("0")) {
                    String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                    String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                    openID = jsonObject.getString(Constants.PARAM_OPEN_ID);
                    getQQUnionid(token,expires);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }



    /**
     * 获取QQ的unionid
     */
    private void getQQUnionid(final String token, final String expires) {
        MyNetworkConnection.getNetworkConnection(LoginMainActivity.this).post("get", "https://graph.qq.com/oauth2.0/me?access_token="+ token+"&unionid=1", null, new Callback() {
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
                 mTencent.setOpenId(openID);
                mTencent.setAccessToken(token, expires);
                getuserInfo();
            }
        });
    }

    private void getuserInfo() {
        UserInfo qqInfo = new UserInfo(LoginMainActivity.this, mTencent.getQQToken());
        qqInfo.getUserInfo(getQQinfoListener);
    }

    private IUiListener getQQinfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                JSONObject jsonObject = (JSONObject) response;

                int ret = jsonObject.getInt("ret");

                String headImgUrl = jsonObject.getString("figureurl_qq_2");
                String nickname = jsonObject.getString("nickname");
                isFirstLogin(headImgUrl,nickname);


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

    private void isFirstLogin(final String headimgurl, final String nickname) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("code", unionid);
        par.put("password", "");
        par.put("type", "qq");
        par.put("pic", headimgurl);
        par.put("nickname", nickname);
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
        HttpConnect.post(this, "member_login_v3", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                final net.sf.json.JSONObject data = net.sf.json.JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {
                    getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
                    final String token = data.optJSONArray("data").optJSONObject(0).optString("token");
                    LoginUser.getInstantiation(LoginMainActivity.this.getApplicationContext()).setToken(token);
                    Handler dataHandler = new Handler(
                            LoginMainActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            Handler dataHandler = new Handler(
                                    LoginMainActivity.this.getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
                                    LoginUser.getInstantiation(LoginMainActivity.this.getApplicationContext()).setToken(token);
                                    getMemberInfoPresenter = new GetMemberInfoPresenter(LoginMainActivity.this,LoginMainActivity.this,"qqLogin");
                                    getMemberInfoPresenter.getMemberInfo();
//                                    ModifyMemberInfoPresenter modifyMemberInfoPresenter = new ModifyMemberInfoPresenter(LoginMainActivity.this,getMemberInfoPresenter);
//                                    modifyMemberInfoPresenter.modifyMemberInfo(headimgurl,nickname);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }else
                {
                    Handler dataHandler = new Handler(
                            LoginMainActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            if(data.getString("msg")!=null)
                            {
                                boolean flag = Tools.isPhonticName(data.getString("msg"));
                                if(!flag)
                                {
                                    if(data.getString("msg").equals("0"))//跳注册
                                    {
                                        Intent intent = new Intent(LoginMainActivity.this,PhoneRegisterActivity.class);
                                        intent.putExtra("nickname",nickname);
                                        intent.putExtra("pic",headimgurl);
                                        intent.putExtra("type","qq");
                                        intent.putExtra("openid",unionid);
                                        startActivity(intent);
                                    }else
                                    {
                                        if(data.optString("msg")!=null)
                                        {
                                            if(!Tools.isPhonticName(data.optString("msg")))
                                            {
                                                sToast(data.getString("msg"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        LoginMainActivity.this.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
//                        Toast.makeText(LoginMainActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 查看当前app是否处于维护状态
     */
    private void isMaintain(){

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                final String ipLine = IPUtil.getNetIp();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if ( !loginType.equals("forgetLoginPwd") || !loginType.equals("register")) {
                            myProgressDialog.setTitle("正在登录...");
                            myProgressDialog.show();
                            handler.postDelayed(thread2,1000);
                        }

                        MyNetworkConnection.getNetworkConnection(LoginMainActivity.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                      handler.postDelayed(thread1,2000);
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String json = response.body().string().trim();
                                final net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
                                final net.sf.json.JSONObject jsonObject = jsonArray.optJSONObject(0);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String enable = jsonObject.optString("enable");
                                        String ip = jsonObject.optString("ip");
                                        if(enable!=null&&!TextUtils.isEmpty(enable))
                                        {
                                            if(enable.equals("false"))
                                            {
                                                if(loginType.equals("wechat"))
                                                {
                                                    wxLoginPresenter.login();
                                                }else if(loginType.equals("qq"))
                                                {
                                                    mTencent = Tencent.createInstance(QQ_APP_ID, LoginMainActivity.this.getApplicationContext());
                                                    if (!mTencent.isSessionValid()) {
                                                        mTencent.login(LoginMainActivity.this, "get_simple_userinfo", listener);
                                                    } else {
                                                        mTencent.logout(LoginMainActivity.this.getApplicationContext());
                                                        mTencent.login(LoginMainActivity.this, "get_simple_userinfo", listener);
                                                    }

                                                }else if(loginType.equals("register"))
                                                {
                                                    Intent intent = new Intent(LoginMainActivity.this,PhoneRegisterActivity.class);
                                                    intent.putExtra("type","mobile");
                                                    startActivity(intent);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            myProgressDialog.cancel();
                                                        }
                                                    });

                                                }else if(loginType.equals("login"))
                                                {
                                                    if(type.equals("mobile"))
                                                    {
                                                        phoneLoginPresenter.phoneLogin();
                                                    }else if(type.equals("sms"))
                                                    {
                                                        if(TextUtils.isEmpty(et_name.getText().toString().trim()))
                                                        {
                                                            sToast("请输入手机号");
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    myProgressDialog.cancel();
                                                                }
                                                            });
                                                            return;
                                                        }else if(TextUtils.isEmpty(et_code.getText().toString().trim()))
                                                        {
                                                            sToast("请输入验证码");
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    myProgressDialog.cancel();
                                                                }
                                                            });
                                                            return;
                                                        }else
                                                        {
                                                             isSmsLogin(et_name.getText().toString().trim(),getSms(),"","");
                                                        }
                                                    }
                                                }else if(loginType.equals("forgetLoginPwd"))
                                                {
                                                    startActivity(new Intent(LoginMainActivity.this,ForgetLoginPassword.class));
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            myProgressDialog.cancel();
                                                        }
                                                    });
                                                }
                                            }else
                                            {
                                                if(!TextUtils.isEmpty(ip)&&ip.equals(ipLine))
                                                {
                                                    if(loginType.equals("wechat"))
                                                    {
                                                        wxLoginPresenter.login();
                                                    }else if(loginType.equals("qq"))
                                                    {
                                                        mTencent = Tencent.createInstance(QQ_APP_ID, LoginMainActivity.this.getApplicationContext());
                                                        if (!mTencent.isSessionValid()) {
                                                            mTencent.login(LoginMainActivity.this, "get_simple_userinfo", listener);
                                                        } else {
                                                            mTencent.logout(LoginMainActivity.this.getApplicationContext());
                                                            mTencent.login(LoginMainActivity.this, "get_simple_userinfo", listener);
                                                        }
                                                    }else if(loginType.equals("register"))
                                                    {
                                                        Intent intent = new Intent(LoginMainActivity.this,PhoneRegisterActivity.class);
                                                        intent.putExtra("type","mobile");
                                                        startActivity(intent);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                myProgressDialog.cancel();
                                                            }
                                                        });
                                                    }else if(loginType.equals("login"))
                                                    {
                                                        phoneLoginPresenter.phoneLogin();
                                                    }else if(loginType.equals("forgetLoginPwd"))
                                                    {
                                                        startActivity(new Intent(LoginMainActivity.this,ForgetLoginPassword.class));
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                myProgressDialog.cancel();
                                                            }
                                                        });
                                                    }
                                                }else
                                                {
                                                    String title = jsonObject.optString("title");
                                                    String content = jsonObject.optString(
                                                            "content");
                                                    startActivity(new Intent(LoginMainActivity.this,MaintainDialogActivity.class).putExtra("title",title).putExtra("content",content));
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            myProgressDialog.cancel();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }


    private void isSmsLogin(final String mobile,final String sms, final String headimgurl, final String nickname) {
        Map<String, String> par = new HashMap<String, String>();
        par.put("code", mobile);
        par.put("password", sms);
        par.put("type", "sms");
        par.put("pic", headimgurl);
        par.put("nickname", nickname);
        par.put("uuid", GuideActivity.PHONE_ID);
        par.put("geographic", GuideActivity.LONGITUDE+","+GuideActivity.LATITUDE );
        par.put("geographicBac",GuideActivity.ADDRESS );
        par.put("browserSystem", GuideActivity.PHONE_TYPE);
        HttpConnect.post(this, "member_login_v3", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

                final net.sf.json.JSONObject data = net.sf.json.JSONObject.fromObject(arg0.body().string());

                if (data.get("status").equals("success")) {
                    getSharedPreferences("superPwd", Context.MODE_PRIVATE).edit().putString("sp", "").commit();
                    final String token = data.optJSONArray("data").optJSONObject(0).optString("token");
                    LoginUser.getInstantiation(LoginMainActivity.this.getApplicationContext()).setToken(token);
                    Handler dataHandler = new Handler(
                            LoginMainActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            Handler dataHandler = new Handler(
                                    LoginMainActivity.this.getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
//
                                    myProgressDialog.cancel();
                                    LoginUser.getInstantiation(LoginMainActivity.this.getApplicationContext()).setToken(token);
                                    getMemberInfoPresenter = new GetMemberInfoPresenter(LoginMainActivity.this,LoginMainActivity.this,"sms");
                                    getMemberInfoPresenter.getMemberInfo();
//                                    ModifyMemberInfoPresenter modifyMemberInfoPresenter = new ModifyMemberInfoPresenter(LoginMainActivity.this,getMemberInfoPresenter);
//                                    modifyMemberInfoPresenter.modifyMemberInfo(headimgurl,nickname);
                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }else
                {
                    Handler dataHandler = new Handler(
                            LoginMainActivity.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                                        if(data.optString("msg")!=null)
                                        {
                                            if(!Tools.isPhonticName(data.optString("msg")))
                                            {
                                                sToast(data.getString("msg"));
                                            }
                                        }
                            myProgressDialog.cancel();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(
                        LoginMainActivity.this.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        myProgressDialog.cancel();
//                        Toast.makeText(LoginMainActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 显示实名认证的提示框
     */
    public void show(String startDate,String endDate)
    {
        ImageView iv_cancel_authentication = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);
        TextView tv_start_time = (TextView) view1.findViewById(R.id.tv_start_time);
        TextView tv_end_time = (TextView) view1.findViewById(R.id.tv_end_time);

        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        tv_start_time.setText("开始时间:"+startDate);
        tv_end_time.setText("结束时间:"+endDate);

        if(!window.isShowing())
        {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 =getWindow().getAttributes();
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
            window.showAtLocation(linearLayout_weixin_login,
                    Gravity.BOTTOM, 0, 0);
        }
    }



    private void sendBroadcastRegisterDone() {
//        SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
//        String gesturepassword = sharedPreferences.getString("gesturepassword","");
//        if(!TextUtils.isEmpty(gesturepassword)&&gesturepassword.equals("有"))
//        {
            startActivity(new Intent(this,MainActivity.class));
//        }else
//        {
//            Intent intent =  new Intent(this,GesturesPasswordActivity.class);
//            intent.putExtra("isSetting","true");
//            startActivity(intent);
//        }
        myProgressDialog.cancel();
//        view.setVisibility(View.GONE);
        finish();

    }

    public void onWxLogin(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
    }

    @Override
    public String getusername() {
        return et_name.getText().toString().trim();
    }

    public String getSms()
    {
            return et_code.getText().toString().trim();
    }


    @Override
    public String getpassword() {
        if (tv_code.getText().toString().equals("验证码登录")){
            return et_password.getText().toString().trim();
        }else if (tv_code.getText().toString().equals("密码登录")){

            return et_code.getText().toString().trim();
        }
       return "";
    }


    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFail() {
        myProgressDialog.cancel();
        Toast.makeText(this,"请安装微信客户端",Toast.LENGTH_SHORT).show();
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

    @Override
    public void loadMemberInfoSuccess() {
        Handler dataHandler = new Handler(
                LoginMainActivity.this.getMainLooper()) {

            @Override
            public void handleMessage(
                    final Message msg) {
//                Toast.makeText(LoginMainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                sendBroadcastRegisterDone();
            }
        };
        dataHandler.sendEmptyMessage(0);
    }

    @Override
    public void loadMemberInfoFail() {
        Handler dataHandler = new Handler(
                LoginMainActivity.this.getMainLooper()) {

            @Override
            public void handleMessage(
                    final Message msg) {
                hideProgress();
                sToast("网络质量不佳，请检查网络！");
            }
        };
        dataHandler.sendEmptyMessage(0);
    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showProgress() {
        myProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        myProgressDialog.cancel();
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

    public String getOpenID() {
        return openID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myProgressDialog.cancel();
    }


}
