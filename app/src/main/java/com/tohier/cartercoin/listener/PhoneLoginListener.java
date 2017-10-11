package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface PhoneLoginListener {



    /**
     * 登录成功的回调
     **/
    void loadSuccess();

    /**
     * 登录失败的回调
     **/
    void loadFail();

    /**
     * 登录失败的回调
     **/
    void showLoginMsg(String msg);

}
