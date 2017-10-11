package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.DataCleanManager;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/14.
 */

public class SettingActivity extends MyBaseActivity implements View.OnClickListener{

    private LinearLayout linearlayout_into_update_member_loginpwd,linearlayout_into_update_member_paypwd,linearlayout_clearcache,linearlayout_into_edit_opinion,linearlayout_into_helpcenter,
                                 linearlayout_into_contact_service,linearlayout_into_aboutapp,linearlayout_into_update_member_gesturespwd;
    private Button btn_logout;
    private ImageView iv_back2,iv_right_arror,iv_right_arror2;
    private TextView tv_cache_count,tv_is_start_gesturespwd;

    private Button btn_commit_clearcache;
    private Button btn_cancel_clearcache;
    private View clearCaChePopupWindowView;
    private PopupWindow clearCaChePopupWindow;
    private TextView tvSetting,tv_login_pwd_is_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        MyApplication.maps.put("SettingActivity",this);

        initData();
        setUpView();

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
//        linearlayout_into_update_member_loginpwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_loginpwd);
//        linearlayout_into_update_member_paypwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_paypwd);
        linearlayout_clearcache = (LinearLayout) this.findViewById(R.id.linearlayout_clearcache);
        linearlayout_into_edit_opinion = (LinearLayout) this.findViewById(R.id.linearlayout_into_edit_opinion);
        linearlayout_into_helpcenter = (LinearLayout) this.findViewById(R.id.linearlayout_into_helpcenter);
        linearlayout_into_contact_service = (LinearLayout) this.findViewById(R.id.linearlayout_into_contact_service);
        linearlayout_into_aboutapp = (LinearLayout) this.findViewById(R.id.linearlayout_into_aboutapp);
//        linearlayout_into_update_member_gesturespwd = (LinearLayout) this.findViewById(R.id.linearlayout_into_update_member_gesturespwd);
        btn_logout = (Button) this.findViewById(R.id.btn_logout);
        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        tv_cache_count = (TextView) this.findViewById(R.id.tv_cache_count);
//        tv_is_start_gesturespwd = (TextView) this.findViewById(R.id.tv_is_start_gesturespwd);
//        tvSetting = (TextView) this.findViewById(R.id.tv_isSetting);
//        tv_login_pwd_is_setting = (TextView) this.findViewById(R.id.tv_login_pwd_is_setting);
//        iv_right_arror = (ImageView) this.findViewById(R.id.iv_right_arror);
//        iv_right_arror2 = (ImageView) this.findViewById(R.id.iv_right_arror2);

        clearCaChePopupWindowView = View.inflate(this, R.layout.clearcache_popupwindow_item, null);
        btn_commit_clearcache = (Button) clearCaChePopupWindowView
                .findViewById(R.id.btn_commit_clearcache);
        btn_cancel_clearcache = (Button) clearCaChePopupWindowView
                .findViewById(R.id.btn_cancel_clearcache);
    }

    private void setUpView() {
        linearlayout_clearcache.setOnClickListener(this);
        linearlayout_into_edit_opinion.setOnClickListener(this);
        linearlayout_into_helpcenter.setOnClickListener(this);
        linearlayout_into_contact_service.setOnClickListener(this);
        linearlayout_into_update_member_gesturespwd.setOnClickListener(this);
        linearlayout_into_aboutapp.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        iv_back2.setOnClickListener(this);
        btn_commit_clearcache.setOnClickListener(this);
        btn_cancel_clearcache.setOnClickListener(this);


        /**
         * 设置文本内容 缓存量
         */
        try {
           String cacheCount =  DataCleanManager.getTotalCacheSize(this);
            tv_cache_count.setText(cacheCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.linearlayout_clearcache:
                clearCaChePopupWindow = new PopupWindow(clearCaChePopupWindowView, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                clearCaChePopupWindow.setOutsideTouchable(true);

                // 实例化一个ColorDrawable颜色为半透明
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                clearCaChePopupWindow.setBackgroundDrawable(dw);

                // 设置popWindow的显示和消失动画
                clearCaChePopupWindow.setAnimationStyle(R.style.Mypopwindow_anim_style);
                // 在底部显示
                clearCaChePopupWindow.showAtLocation(findViewById(R.id.iv_back2),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.linearlayout_into_edit_opinion:

                break;
            case R.id.linearlayout_into_helpcenter:
                startActivity(new Intent(SettingActivity.this,HelperCenterActivity.class));
                break;
            case R.id.linearlayout_into_contact_service:
                //intent = new Intent(getActivity(),AddWXKeFu.class);
                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                EServiceContact contact = new EServiceContact(MyApplication.KEFU, 0);
                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                //的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent1 = mIMKit.getChattingActivityIntent(contact);
                startActivity(intent1);
                break;
            case R.id.linearlayout_into_aboutapp:
                startActivity(new Intent(SettingActivity.this,HelperCenterActivity.class).putExtra("mark","关于分乐宝"));
                break;
            case R.id.btn_logout:
                intent = new Intent(this, LoginMainActivity.class);
                startActivity(intent);
                finish();
                LoginUser.getInstantiation(getApplicationContext()).loginOut();
                break;
            case R.id.iv_back2:
                finish();
                break;
            case R.id.btn_commit_clearcache:
               boolean delete = DataCleanManager.clearAllCache(this);
                if(delete)
                {
                    try {
                        String cacheCount =  DataCleanManager.getTotalCacheSize(this);
                        tv_cache_count.setText(cacheCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                {

                }
                clearCaChePopupWindow.dismiss();
                break;
            case R.id.btn_cancel_clearcache:
                 clearCaChePopupWindow.dismiss();
                break;
//            case R.id.linearlayout_into_update_member_gesturespwd:
//                intent = new Intent(SettingActivity.this,GesturesPasswordUpdateActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("isExitGesturesPassword", Context.MODE_PRIVATE);
        String opengestures = sharedPreferences.getString("opengestures","");
        if(!TextUtils.isEmpty(opengestures)&&opengestures.equals("True"))   //开启状态
        {
            tv_is_start_gesturespwd.setText("已开启");
        }else                                                            //未开启状态
        {
            tv_is_start_gesturespwd.setText("未开启");
        }

        isExitPayPwd();
    }

    /**
     * 是否有交易密码
     */
    public void isExitPayPwd()
    {
        HttpConnect.post(SettingActivity.this, "member_is_password_pay", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                 if (data.get("status").equals("success")) {
                    String value = data.getJSONArray("data").getJSONObject(0).getString("value");
                    String password = data.getJSONArray("data").getJSONObject(0).getString("password");


                     if(!TextUtils.isEmpty(password)&&password.equals("1")) //存在支付密码  要修改支付密码
                     {
                         SettingActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 tv_login_pwd_is_setting.setText("修改");
                                 iv_right_arror2.setVisibility(View.VISIBLE);

                                 linearlayout_into_update_member_loginpwd.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intent = new Intent(SettingActivity.this,UpdateLoginPwdActivity.class);
                                         startActivity(intent);
                                     }
                                 });
                             }
                         });


                     }else             //不存在需要设置
                     {
                         SettingActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 tv_login_pwd_is_setting.setText("设置");
                                 iv_right_arror2.setVisibility(View.VISIBLE);
                                 linearlayout_into_update_member_loginpwd.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intent = new Intent(SettingActivity.this,SettingLoginPasswordActivity.class);
                                         startActivity(intent);
                                     }
                                 });
                             }
                         });
                     }

                    if(!TextUtils.isEmpty(value)&&value.equals("1")) //存在支付密码  要修改支付密码
                    {

                        SettingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSetting.setText("修改");
                                iv_right_arror.setVisibility(View.VISIBLE);
                                linearlayout_into_update_member_paypwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SettingActivity.this,UpdatePayPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });


                    }else             //不存在需要设置
                    {
                        SettingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSetting.setText("设置");
                                iv_right_arror.setVisibility(View.VISIBLE);
                                linearlayout_into_update_member_paypwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SettingActivity.this,SettingPayPwdActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                } else {
                     if (!"".equals(data.getString("msg"))){
                         sToast(data.getString("msg"));
                     }
                }
                mZProgressHUD.cancel();
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                mZProgressHUD.cancel();
//                sToast("链接超时！");
            }
        });
    }

}
