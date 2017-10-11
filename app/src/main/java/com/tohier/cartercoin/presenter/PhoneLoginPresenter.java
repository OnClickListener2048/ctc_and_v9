package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.LoginMainActivity;
import com.tohier.cartercoin.activity.MyBaseActivity;
import com.tohier.cartercoin.biz.PhoneLoginBiz;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.listener.PhoneLoginListener;
import com.tohier.cartercoin.ui.PhoneLoginView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class PhoneLoginPresenter {

    private PhoneLoginBiz phoneLoginBiz;
    private PhoneLoginView phoneLoginView;
    /**
     * 上下文对象
     **/
    private Context context;

    private GetMemberInfoPresenter getMemberInfoPresenter;

    public PhoneLoginPresenter(Context context, PhoneLoginView phoneLoginView, GetMemberInfoPresenter getMemberInfoPresenter) {
        this.context = context;
        phoneLoginBiz = new PhoneLoginBiz(context);
        this.phoneLoginView = phoneLoginView;
        this.getMemberInfoPresenter = getMemberInfoPresenter;
    }

    public void VerificationTokenPhoneLogin()
    {
        if(TextUtils.isEmpty(phoneLoginView.getusername()))
        {
            phoneLoginView.hideProgress();
            ((IContext)context).sToast("请输入手机号码");
        }else if(TextUtils.isEmpty(phoneLoginView.getpassword()))
        {
            phoneLoginView.hideProgress();
            ((IContext)context).sToast("请输入登录密码");
        }else
        {
            phoneLoginBiz.login(phoneLoginView.getusername(), phoneLoginView.getpassword(), new PhoneLoginListener() {
                @Override
                public void loadSuccess() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            getMemberInfoPresenter.VerificationTokenGetMemberInfo();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }

                @Override
                public void loadFail() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {

                            phoneLoginView.hideProgress();
                            phoneLoginView.loginFail();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }

                @Override
                public void showLoginMsg(final String msg5) {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            phoneLoginView.hideProgress();
                            if(!TextUtils.isEmpty(msg5))
                            {
                                if(msg5.equals("登录失败"))
                                {
                                    Intent intent5 = new Intent(context, LoginMainActivity.class);
                                    context.startActivity(intent5);
                                    ((MyBaseActivity)context).finish();
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            });
        }
    }

    public void phoneLogin()
    {
        phoneLoginView.showProgress();
        if(TextUtils.isEmpty(phoneLoginView.getusername()))
        {
            phoneLoginView.hideProgress();
            ((LoginMainActivity)context).sToast("请输入手机号码");
        }else if(TextUtils.isEmpty(phoneLoginView.getpassword()))
        {
            phoneLoginView.hideProgress();
            ((LoginMainActivity)context).sToast("请输入登录密码");
        }else
        {
            phoneLoginBiz.login(phoneLoginView.getusername(), phoneLoginView.getpassword(), new PhoneLoginListener() {
                @Override
                public void loadSuccess() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            getMemberInfoPresenter.getMemberInfo();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);

                }

                @Override
                public void loadFail() {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            phoneLoginView.hideProgress();
//                            phoneLoginView.loginFail();
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }

                @Override
                public void showLoginMsg(final String msg8) {
                    Handler dataHandler = new Handler(
                            context.getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
                            phoneLoginView.hideProgress();
                            ((LoginMainActivity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean flag = Tools.isPhonticName(msg8);
                                    if(!flag)
                                    {
                                        ((LoginMainActivity)context).sToast(msg8);
                                    }
                                }
                            });
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            });
        }
    }



}
