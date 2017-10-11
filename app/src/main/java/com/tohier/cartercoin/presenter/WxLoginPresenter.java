package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tohier.cartercoin.biz.WxLoginBiz;
import com.tohier.cartercoin.listener.WxLoginListener;
import com.tohier.cartercoin.ui.WxLoginView;

/**
 * Created by Administrator on 2016/11/4.
 */

public class WxLoginPresenter {

    /**
     * 上下文对象
     **/
    private Context context;
    private WxLoginBiz wxLoginBiz;
    private WxLoginView wxLoginView;



    public WxLoginPresenter(Context context,WxLoginView wxLoginView) {
        this.context = context;
        wxLoginBiz = new WxLoginBiz(context);
        this.wxLoginView = wxLoginView;
    }

    public void login()
    {
           wxLoginBiz.login(new WxLoginListener() {
               @Override
               public void success() {

               }

               @Override
               public void fail() {
                   wxLoginView.loginFail();
               }
           });
    }

}
