package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 */

public class DialogBandPhoneActivity extends MyBaseActivity {

    private EditText et_phonenum;
    private EditText et_yzm;
    private Button btn_bandphone;
    private Button btn_getyzm;
    private ImageView iv_kefu;
    private TextView tv_return_login;

    private int count = 120;
    Handler bandPhonehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btn_getyzm.setText("获取短信验证码   ");
                    btn_getyzm.setClickable(true);
                    count = 120;
                    bandPhonehandler.removeCallbacks(bandPhoneThread);
                }else{
                    btn_getyzm.setText(count+"秒后再获取");
                    btn_getyzm.setClickable(false);
                    postDelayed(bandPhoneThread,1000);
                }
            }
        }
    };
    Thread bandPhoneThread = new Thread(){
        @Override
        public void run(){
            bandPhonehandler.sendEmptyMessage(0x111);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupwindow_bandphone);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.48);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.78);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(p);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        tv_return_login = (TextView) this.findViewById(R.id.tv_return_login);
        tv_return_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        et_phonenum = (EditText) this.findViewById(R.id.et_phonenum);
        et_yzm = (EditText) this.findViewById(R.id.et_yzm);
        btn_getyzm = (Button) this.findViewById(R.id.btn_getyzm);
        btn_bandphone = (Button) this.findViewById(R.id.btn_bandphone);
        iv_kefu = (ImageView) this.findViewById(R.id.iv_kefu);

    }

    private void setUpView() {

        tv_return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUser.getInstantiation(getApplicationContext()).isLogin()){

                    Map<String, String> par = new HashMap<String, String>();
                    HttpConnect.post(DialogBandPhoneActivity.this, "member_mine_end", par, new Callback() {

                        @Override
                        public void onFailure(Request arg0, IOException arg1) {
                            DialogBandPhoneActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sToast("网络质量不佳，请检查网络！");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            final JSONObject data = JSONObject.fromObject(arg0.body().string());
                            if (data.get("status").equals("success")) {

                                DialogBandPhoneActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        IYWLoginService loginService = mIMKit.getIMCore().getLoginService();
                                        loginService.logout(new IWxCallback() {

                                            @Override
                                            public void onSuccess(Object... arg0) {
                                                //登出成功
                                                Intent intent5 = new Intent(DialogBandPhoneActivity.this, LoginMainActivity.class);
                                                startActivity(intent5);
                                                finish();
                                                LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                            }

                                            @Override
                                            public void onProgress(int arg0) {
                                                // TODO Auto-generated method stub
                                            }

                                            @Override
                                            public void onError(int errCode, String description) {
                                                //登出失败，errCode为错误码,description是错误的具体描述信息
//                                                Toast.makeText(NewSettingActivity.this,description,Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                });

                            } else {
                                DialogBandPhoneActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(data.optString("msg")!=null&&!data.optString("msg").equals(""))
                                        {
                                            if(!Tools.isPhonticName(data.optString("msg")))
                                            {
                                                sToast(data.optString("msg"));
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    });


                    //startActivity(new Intent(this,LoginMainActivity.class));
                }
            }
        });

        iv_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                //的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent1 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent1);
            }
        });

        btn_bandphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_phonenum.getText().toString().trim()))
                {
                    sToast("请填写手机号码");
                }else if(et_phonenum.getText().toString().trim().length()!=11)
                {
                    sToast("手机号码输入不合法");
                }else if(TextUtils.isEmpty(et_yzm.getText().toString().trim()))
                {
                    sToast("请填写手机验证码");
                }else
                {
                    bandphone();
                }
            }
        });

        btn_getyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_phonenum.getText().toString().trim()))
                {
                    sToast("请填写手机号码");
                }else if(et_phonenum.getText().toString().trim().length()!=11)
                {
                    sToast("手机号码输入不合法");
                }else
                {
                    Map<String, String> par = new HashMap<String, String>();
                    par.put("mobile",  et_phonenum.getText().toString().trim());
                    par.put("code","member_mobile_upgrade");
                    HttpConnect.post(DialogBandPhoneActivity.this, "sms_member_code", par, new Callback() {

                        @Override
                        public void onResponse(Response arg0) throws IOException {
                            final JSONObject data = JSONObject.fromObject(arg0.body().string());
                            if (data.get("status").equals("success")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bandPhonehandler.post(bandPhoneThread);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(data.optString("msg")!=null)
                                        {
                                            if(!Tools.isPhonticName(data.optString("msg")))
                                            {
                                                sToast(data.getString("msg"));
                                            }
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
//                                    sToast("请检查您的网络链接状态");
                                }
                            });
                        }
                    });
                }

            }
        });
    }

    /**
     * 验证手机号是否能绑定
     */
    public void bandphone()
    {
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("newmobile",et_phonenum.getText().toString().trim() );
        par1.put("newsms",et_yzm.getText().toString().trim() );
        par1.put("code","member_mobile_upgrade" );
        HttpConnect.post(this, "member_mobile_upgrade", par1, new Callback() {

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        sToast("请检查您的网络链接状态");
                        btn_bandphone.setClickable(true);
                    }
                });
            }

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject object = JSONObject.fromObject(arg0.body().string());
                if (object.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            MainActivity.isflag = true;
                            btn_bandphone.setClickable(true);
                            LoginUser.getInstantiation(getApplicationContext()).setPhoneNum(et_phonenum.getText().toString().trim());
                            finish();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);


                }else{
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            btn_bandphone.setClickable(true);
                            if(!TextUtils.isEmpty(object.getString("msg")))
                            {
                                boolean flag = Tools.isPhonticName(object.getString("msg"));
                                if(!flag)
                                {
                                    sToast(object.getString("msg"));
                                }
                            }else
                            {
                                sToast("绑定失败，请重试");
                            }

                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }
        });
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
