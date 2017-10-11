package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface WxLoginListener {

    /**
     * 登录成功的回调
     **/
    void success();

    /**
     * 登录失败的回调
     **/
    void fail();

}
