package com.tohier.cartercoin.biz;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tohier.cartercoin.listener.WxLoginListener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public class WxLoginBiz implements IWxLoginBiz {

    /**
     * 用于微信登录的appId
     **/
    private static final String WX_APP_ID = "wx7ad749f6cba84064";
    public IWXAPI api;

    /**
     * 上下文对象
     **/
    private Context context;

    public WxLoginBiz(Context context) {
        this.context = context;
    }

    @Override
    public void login(WxLoginListener wxLoginListener) {
        api = WXAPIFactory.createWXAPI(context, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        if (!api.sendReq(req)) {
           wxLoginListener.fail();
        }
    }
}
