package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */

public class BandPhoneActivity extends MyBackBaseActivity {

    private ImageView iv_back2;
    private Button btn_commit_update_nickname,btn_yanzhengma;
    private EditText et_nickname,et_yzm;
    private TextView tv_title2;
    private int i = 0;
    private int count = 120;
    private boolean loginBand;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                count--;
                if (count<=0){
                    btn_yanzhengma.setText("获取短信验证码   ");
                    btn_yanzhengma.setClickable(true);
                    count = 120;
                    handler.removeCallbacks(thread);
                }else{
                    btn_yanzhengma.setText("验证码已发送 （"+count+"s）   ");
                    btn_yanzhengma.setClickable(false);
                    postDelayed(thread,1000);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bandphone_layout);

        initData();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    @Override
    public void initData() {
        loginBand = getIntent().getBooleanExtra("loginBand",true);

                loadMemberInfo();
              iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
              tv_title2 = (TextView) this.findViewById(R.id.tv_title2);
              btn_commit_update_nickname = (Button) this.findViewById(R.id.btn_commit_update_nickname);
              btn_yanzhengma = (Button) this.findViewById(R.id.btn_yanzhengma);
              et_nickname = (EditText) this.findViewById(R.id.et_nickname);
             et_yzm = (EditText) this.findViewById(R.id.et_yzm);
              et_nickname.setHint("手机号码");
              tv_title2.setText("手机绑定");
              iv_back2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    finish();
                  }
             });

        btn_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onValidateCode();
            }
        });

        btn_commit_update_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_commit_update_nickname.setClickable(false);
                if(TextUtils.isEmpty(et_nickname.getText().toString().trim()))
                {
                    btn_commit_update_nickname.setClickable(true);
                          sToast("请填写手机号");
                          return;
                }else if(et_nickname.getText().toString().trim().length()<11)
                {
                    btn_commit_update_nickname.setClickable(true);
                          sToast("手机号输入不合法");
                          return;
                }else if(TextUtils.isEmpty(et_yzm.getText().toString().trim()))
                {
                    btn_commit_update_nickname.setClickable(true);
                    sToast("请填写手机验证码");
                    return;
                }else
                {
                    if(i==1)
                    {
                        bandphone();
                    }else
                    {
                        btn_commit_update_nickname.setClickable(true);
//                        sToast("请检查您的网络状态");
                    }
                }
            }
        });


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

    /**
     * 获取会员的信息
     */
    public void loadMemberInfo()
    {
        HttpConnect.post(BandPhoneActivity.this, "member_info", null, new Callback() {

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

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            i = 1;
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
                            sToast(msg8);
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
//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public void onValidateCode() {
        if(TextUtils.isEmpty(et_nickname.getText().toString().trim()))
        {
            sToast("请填写手机号");
            return;
        }else if(et_nickname.getText().toString().trim().length()!=11) {
            sToast("手机号输入不合法");
            return;
        }
        String phone = ((EditText) findViewById(R.id.et_nickname)).getText().toString();
        if (StringUtils.isNotBlank(phone) && NumberUtils.isNumber(phone) && phone.length() == 11) {
            Map<String, String> par = new HashMap<String, String>();
            par.put("mobile", phone);
            par.put("code","member_mobile_upgrade");
            HttpConnect.post(this, "sms_member_code", par, new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    final JSONObject data = JSONObject.fromObject(arg0.body().string());
                    if (data.get("status").equals("success")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        handler.post(thread);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sToast(data.getString("msg"));
                            }
                        });
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

//    /**
//     * 检查手机号的有效性
//     */
//    public void checkPhoneNumEffective()
//    {
//        gif_loading.setVisibility(View.VISIBLE);
//        Map<String, String> par1 = new HashMap<String, String>();
//        par1.put("mobile",et_nickname.getText().toString().trim() );
//
//        HttpConnect.post(this, "member_check_mobile", par1, new Callback() {
//
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn_commit_update_nickname.setClickable(true);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response arg0) throws IOException {
//                JSONObject object = JSONObject.fromObject(arg0.body().string());
//                if (object.get("status").equals("success")) {
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            verificationMobileIsBand();
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//
//
//                }else{
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            btn_commit_update_nickname.setClickable(true);
//                            gif_loading.setVisibility(View.GONE);
//                            sToast("请输入正确有效的手机号");
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//                }
//            }
//        });
//    }
//
//    /**
//     * 验证手机号是否能绑定
//     */
//    public void verificationMobileIsBand()
//    {
//        Map<String, String> par1 = new HashMap<String, String>();
//        par1.put("mobile",et_nickname.getText().toString().trim() );
//
//        HttpConnect.post(this, "member_bind_mobile", par1, new Callback() {
//
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn_commit_update_nickname.setClickable(true);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response arg0) throws IOException {
//                JSONObject object = JSONObject.fromObject(arg0.body().string());
//                if (object.get("status").equals("success")) {
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            bandphone();
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//
//
//                }else{
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            btn_commit_update_nickname.setClickable(true);
//                            gif_loading.setVisibility(View.GONE);
//                            sToast("该手机号已有用户绑定");
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//                }
//            }
//        });
//    }
    /**
     * 验证手机号是否能绑定
     */
    public void bandphone()
    {
            myProgressDialog.setTitle("正在加载...");
            myProgressDialog.show();
            Map<String, String> par1 = new HashMap<String, String>();
            par1.put("oldmobile",et_nickname.getText().toString().trim());
            par1.put("oldsms",et_yzm.getText().toString().trim() );
            par1.put("code","member_mobile_upgrade" );
            par1.put("type","0" );

            HttpConnect.post(this, "member_mobile_upgrade", par1, new Callback() {

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sToast("网络质量不佳，请检查网络！");
                            myProgressDialog.cancel();

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
                                myProgressDialog.cancel();
                                LoginUser.getInstantiation(getApplicationContext()).setPhoneNum(et_nickname.getText().toString().trim());
                                sToast("修改成功");
                                if(loginBand)
                                {
                                    startActivity(new Intent(BandPhoneActivity.this,MainActivity.class));
                                }
                                finish();
                            }
                        };
                        dataHandler.sendEmptyMessage(0);


                    }else{
                        Handler dataHandler = new Handler(getContext()
                                .getMainLooper()) {

                            @Override
                            public void handleMessage(final Message msg) {
                                myProgressDialog.cancel();

                                if(!TextUtils.isEmpty(object.getString("msg")))
                                {
                                    boolean flag = Tools.isPhonticName(object.getString("msg"));
                                    if(!flag)
                                    {
                                        sToast(object.getString("msg"));
                                    }
                                }else
                                {
                                    sToast("修改失败，请重试");
                                }

                            }
                        };
                        dataHandler.sendEmptyMessage(0);
                    }
                }
            });
    }
}
