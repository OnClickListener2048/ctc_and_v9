package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface SetGesturesPasswordListener {

    /**
     * 设置手势密码成功的回调
     **/
    void loadSuccess(String result);

    /**
     * 设置手势密码失败的回调
     **/
    void  loadFail();

    /**
     * 显示msg
     */
    void showMsg(String msg);



}
