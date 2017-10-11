package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.DataManagerTools;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.presenter.GetMemberInfoPresenter;
import com.tohier.cartercoin.presenter.PhoneLoginPresenter;
import com.tohier.cartercoin.ui.GetMemberInfoView;
import com.tohier.cartercoin.ui.PhoneLoginView;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class PhoneLoginActivity extends BaseActivity implements PhoneLoginView,View.OnClickListener,GetMemberInfoView {


    /**
     *用于手机号登录 负责完成View于Model间的交互
     **/
    private PhoneLoginPresenter phoneLoginPresenter;
    private EditText et_name,et_password;
    private Button btn_phoneLogin;
    private ImageView iv_pwd_isShow,iv_back;
    private TextView tv_forget_pwd;
    private GifImageView gif_loading;

    /**
     * 获取用户信息
     */
     private GetMemberInfoPresenter getMemberInfoPresenter;

    PopupWindow window = null;
    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        et_name = (EditText) this.findViewById(R.id.et_name);
        gif_loading = (GifImageView) this.findViewById(R.id.gif_loading);
        et_password = (EditText) this.findViewById(R.id.et_password);
        btn_phoneLogin = (Button) this.findViewById(R.id.btn_phoneLogin);
        iv_pwd_isShow = (ImageView) this.findViewById(R.id.iv_pwd_isShow);
        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        tv_forget_pwd = (TextView) this.findViewById(R.id.tv_forget_pwd);

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        view1 = View.inflate(this,R.layout.popupwindow_weihu_update,null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        //获取会员信息的presenter
        getMemberInfoPresenter = new GetMemberInfoPresenter(this,this,"phoneLogin");
        //登录presenter
        phoneLoginPresenter = new PhoneLoginPresenter(this,this,getMemberInfoPresenter);
    }

    private void setUpView() {
        btn_phoneLogin.setOnClickListener(this);
        iv_pwd_isShow.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
    }


    @Override
    public String getusername() {
        return et_name.getText().toString().trim();
    }

    @Override
    public String getpassword() {
        return et_password.getText().toString().trim();
    }


    /**
     * 登录成功要直接获取会员的信息   只有获取会员信息成功之后才算是登录成功
     */
    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFail() {
//          sToast("请检查您的网络链接状态");
    }

    @Override
    public void showProgress() {
//        mZProgressHUD.show();
        gif_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        mZProgressHUD.cancel();
        gif_loading.setVisibility(View.GONE);
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_phoneLogin:
                isMaintain();
                break;

            case R.id.iv_pwd_isShow:
                DataManagerTools.TextVisiblePassword(iv_pwd_isShow,et_password);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this,ForgetLoginPassword.class));
                break;
        }
    }

    /**
     * 查看当前app是否处于维护状态
     */
    private void isMaintain(){
        MyNetworkConnection.getNetworkConnection(PhoneLoginActivity.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
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
                                phoneLoginPresenter.phoneLogin();
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
            window.showAtLocation(et_name,
                    Gravity.BOTTOM, 0, 0);
        }
    }


    @Override
    public void loadMemberInfoSuccess() {
        hideProgress();

        MyApplication.deleteActivity("LoginMainActivity");

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
        finish();
//        Toast.makeText(this, "获取会员信息成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadMemberInfoFail() {
        hideProgress();
//        Toast.makeText(this, "链接失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMsg(String msg) {

    }
}
