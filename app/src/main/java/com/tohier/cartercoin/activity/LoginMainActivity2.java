package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.biz.LoadLoginPicBiz;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.WxLoginPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.WxLoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 武文锴 on 2016/11/4.
 * This is 登录页面
 */

public class LoginMainActivity2 extends BaseActivity implements WxLoginView,GetMemberInfoView{

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

    private ImageView checkbox_agreement;
    private TextView tv_into_agreement;

    private LinearLayout linearLayout_qq_login,linearLayout_weixin_login;

    PopupWindow window = null;
    private View view1;

    //   wechat    and    qq
    private String loginType = "";
    private String unionid;

    private TextView tv_forget_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //将IndexActivity加入销毁队列中  防止从本页面进入手机登录页面成功进行登录 要及时关闭改页面
        MyApplication.maps.put("LoginMainActivity2",this);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        checkbox_agreement = (ImageView) this.findViewById(R.id.checkbox_agreement);
        checkbox_agreement.setImageResource(R.mipmap.iv_agreement_no_checked);
        wxLoginPresenter = new WxLoginPresenter(this,this);
        btn_into_phone_register = (Button) this.findViewById(R.id.btn_phone_register);
//        tv_qq_login = (TextView) this.findViewById(R.id.tv_qq_login);
        tv_into_agreement = (TextView) this.findViewById(R.id.tv_into_agreement);
        tv_forget_pwd = (TextView) this.findViewById(R.id.tv_forget_pwd);
        linearLayout_weixin_login = (LinearLayout) this.findViewById(R.id.linearLayout_weixin_login);
        linearLayout_qq_login = (LinearLayout) this.findViewById(R.id.linearLayout_qq_login);


//        qqIsHide();
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


        tv_into_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMainActivity2.this,LoginXieyiActivity.class));
            }
        });

        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMainActivity2.this,ForgetLoginPassword.class));
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

//        btn_into_phone_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(agreementIsChecked)
//                {
//                    loginType = "login";
//                    isMaintain();
//                }else
//                {
//                    sToast("请查看条款与协议");
//                }
//             }
//        });

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
//                    wxLoginPresenter.login();
                    isMaintain();
                }else
                {
                    sToast("请查看条款与协议");
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
            Toast.makeText(LoginMainActivity2.this, "你取消了QQ登录", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object arg0) {
            JSONObject jo = (JSONObject) arg0;

            initLoginID(jo);
        }

        @Override
        public void onError(UiError arg0) {
            Toast.makeText(LoginMainActivity2.this, "QQ登录出错", Toast.LENGTH_SHORT).show();
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
        MyNetworkConnection.getNetworkConnection(LoginMainActivity2.this).post("get", "https://graph.qq.com/oauth2.0/me?access_token="+ token+"&unionid=1", null, new Callback() {
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
        UserInfo qqInfo = new UserInfo(LoginMainActivity2.this, mTencent.getQQToken());
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

                isFirstLogin(openID,headImgUrl,nickname);


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

    private void isFirstLogin(final String openId, final String headimgurl, final String nickname) {
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

                    final String token = data.optJSONArray("data").optJSONObject(0).optString("token");
                    LoginUser.getInstantiation(LoginMainActivity2.this.getApplicationContext()).setToken(token);
                    Handler dataHandler = new Handler(
                            LoginMainActivity2.this.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            Handler dataHandler = new Handler(
                                    LoginMainActivity2.this.getMainLooper()) {

                                @Override
                                public void handleMessage(
                                        final Message msg) {
                                    LoginUser.getInstantiation(LoginMainActivity2.this.getApplicationContext()).setToken(token);
                                    getMemberInfoPresenter = new GetMemberInfoPresenter(LoginMainActivity2.this,LoginMainActivity2.this,"qqLogin");
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
                            LoginMainActivity2.this.getMainLooper()) {

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
                                        Intent intent = new Intent(LoginMainActivity2.this,PhoneRegisterActivity.class);
                                        intent.putExtra("nickname",nickname);
                                        intent.putExtra("pic",headimgurl);
                                        intent.putExtra("type","qq");
                                        intent.putExtra("openid",openId);
                                        startActivity(intent);
                                    }else
                                    {
                                        if (!"".equals(data.getString("msg"))){
                                            sToast(data.getString("msg"));
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
                        LoginMainActivity2.this.getMainLooper()) {

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
        MyNetworkConnection.getNetworkConnection(LoginMainActivity2.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
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
            public void onResponse(Response response) throws IOException {
                String json = response.body().string().trim();
                final net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
                final net.sf.json.JSONObject jsonObject = jsonArray.optJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           String enable = jsonObject.optString("enable");
                            if(enable!=null&&!TextUtils.isEmpty(enable))
                            {
                                if(enable.equals("false"))
                                {
                                    if(loginType.equals("wechat"))
                                    {
                                        wxLoginPresenter.login();
                                    }else if(loginType.equals("qq"))
                                    {
                                        mTencent = Tencent.createInstance(QQ_APP_ID, LoginMainActivity2.this.getApplicationContext());
                                        if (!mTencent.isSessionValid()) {
                                            mTencent.login(LoginMainActivity2.this, "get_simple_userinfo", listener);
                                        } else {
                                            mTencent.logout(LoginMainActivity2.this.getApplicationContext());
                                            mTencent.login(LoginMainActivity2.this, "get_simple_userinfo", listener);
                                        }
                                    }else if(loginType.equals("register"))
                                    {
                                        Intent intent = new Intent(LoginMainActivity2.this,PhoneRegisterActivity.class);
                                        intent.putExtra("type","mobile");
                                        startActivity(intent);
                                    }else if(loginType.equals("login"))
                                    {
                                        startActivity(new Intent(LoginMainActivity2.this,PhoneLoginActivity.class));
                                    }
                                }else
                                {
                                    String begindate = jsonObject.optString("begindate");
                                    String enddate = jsonObject.optString("enddate");
                                    show(begindate,enddate);
                                }
                            }
                        }
                    });
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
        SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
        String gesturepassword = sharedPreferences.getString("gesturepassword","");
        if(!TextUtils.isEmpty(gesturepassword)&&gesturepassword.equals("有"))
        {
            startActivity(new Intent(this,MainActivity.class));
        }else
        {
            Intent intent =  new Intent(this,GesturesPasswordActivity.class);
            intent.putExtra("isSetting","true");
            startActivity(intent);
        }
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
    public void loginFail() {
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
                LoginMainActivity2.this.getMainLooper()) {

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
                LoginMainActivity2.this.getMainLooper()) {

            @Override
            public void handleMessage(
                    final Message msg) {
//                Toast.makeText(LoginMainActivity.this, "链接超时！", Toast.LENGTH_SHORT).show();
            }
        };
        dataHandler.sendEmptyMessage(0);
    }

    @Override
    public void showMsg(String msg) {

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

    public String getOpenID() {
        return openID;
    }
}
