package com.tohier.cartercoin.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tohier.cartercoin.activity.GesturesPasswordActivity;
import com.tohier.cartercoin.activity.GesturesPasswordUpdateActivity;
import com.tohier.cartercoin.activity.LoginMainActivity;
import com.tohier.cartercoin.biz.SetGesturesPasswordBiz;
import com.tohier.cartercoin.columnview.LocusPassWordView;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.listener.SetGesturesPasswordListener;
import com.tohier.cartercoin.ui.GesturesPwdVerifyPassView;
import com.tohier.cartercoin.ui.LoadLoginPicView;

import java.util.Set;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class SetGesturesPasswordPresenter {

    private SetGesturesPasswordBiz setGesturesPasswordBiz;
    private LoadLoginPicView loadLoginPicView;
    /**
     * 上下文对象
     **/
    private Context context;
    private LocusPassWordView mPwdView;
    private int count = 5;
    private GesturesPwdVerifyPassView gesturesPwdVerifyPassView;

    public SetGesturesPasswordPresenter(Context context,LoadLoginPicView loadLoginPicView,LocusPassWordView mPwdView) {
        this.context = context;
        setGesturesPasswordBiz = new SetGesturesPasswordBiz(context);
        this.loadLoginPicView = loadLoginPicView;
        this.mPwdView = mPwdView;
    }

    public SetGesturesPasswordPresenter(Context context, GesturesPwdVerifyPassView gesturesPwdVerifyPassView, LocusPassWordView mPwdView) {
        this.context = context;
        setGesturesPasswordBiz = new SetGesturesPasswordBiz(context);
        this.gesturesPwdVerifyPassView = gesturesPwdVerifyPassView;
        this.mPwdView = mPwdView;
    }

    public void checkGesturesPassword(String gesturesPassword)
    {
        setGesturesPasswordBiz.checkGesturesPassword(gesturesPassword,new SetGesturesPasswordListener() {

            @Override
            public void loadSuccess(final String result) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {

                        if(!TextUtils.isEmpty(result)&&result.equals("1"))
                        {
                            loadLoginPicView.loadSuccess();
                        }else if(!TextUtils.isEmpty(result)&&result.equals("0"))
                        {
                            count = count - 1 ;
                            if(count<=0)
                            {
                                Intent intent = new Intent(context, LoginMainActivity.class);
                                context.startActivity(intent);
                                ((GesturesPasswordActivity)context).finish();
                                LoginUser.getInstantiation(context.getApplicationContext()).loginOut();
                            }else
                            {
                                ((GesturesPasswordActivity)context).sToast("手势错误,你还有"+count+"次机会");
                                mPwdView.markError();
                            }
                        }
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
                        loadLoginPicView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }
        });
    }

    /**
     * 修改验证手势密码
     * @param gesturesPassword
     */
    public void updateCheckGesturesPassword(String gesturesPassword)
    {
        setGesturesPasswordBiz.checkGesturesPassword(gesturesPassword,new SetGesturesPasswordListener() {

            @Override
            public void loadSuccess(final String result) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {

                        if(!TextUtils.isEmpty(result)&&result.equals("1"))
                        {
                            gesturesPwdVerifyPassView.VerifyPassSuccess();
                        }else if(!TextUtils.isEmpty(result)&&result.equals("0"))
                        {
                            count = count - 1 ;
                            if(count<=0)
                            {
                                Intent intent = new Intent(context, LoginMainActivity.class);
                                context.startActivity(intent);
//                                Set<String> keys2 = MyApplication.maps.keySet();
//                                if(keys2!=null&&keys2.size()>0)
//                                {
//                                    if(keys2.contains("IndexActivity"))
//                                    {
//                                        Activity activity = MyApplication.maps.get("IndexActivity");
//                                        activity.finish();
//                                    } ;
//                                }

                                Set<String> keys3 = MyApplication.maps.keySet();
                                if(keys3!=null&&keys3.size()>0)
                                {
                                    if(keys3.contains("SettingActivity"))
                                    {
                                        Activity activity = MyApplication.maps.get("SettingActivity");
                                        activity.finish();
                                    };
                                }
                                ((GesturesPasswordUpdateActivity)context).finish();
                                LoginUser.getInstantiation(context.getApplicationContext()).loginOut();
                            }else
                            {
                                ((GesturesPasswordUpdateActivity)context).sToast("手势错误,你还有"+count+"次机会");
                                mPwdView.markError();
                            }
                        }
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
                        gesturesPwdVerifyPassView.VerifyPassFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }
        });
    }


    public void setGesturesPassword(String gesturesPassword)
    {
        setGesturesPasswordBiz.setGesturesPassword(gesturesPassword,new SetGesturesPasswordListener() {

            @Override
            public void loadSuccess(String result) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadLoginPicView.loadSuccess();
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
                        loadLoginPicView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }
        });
    }


    public void verifyPassSetGesturesPassword(String gesturesPassword)
    {
        setGesturesPasswordBiz.setGesturesPassword(gesturesPassword,new SetGesturesPasswordListener() {

            @Override
            public void loadSuccess(String result) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        gesturesPwdVerifyPassView.setSuccess();
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
                        gesturesPwdVerifyPassView.VerifyPassFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(String msg) {

            }
        });
    }


}
