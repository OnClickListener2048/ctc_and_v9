package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface MiningInfoListener {



    /**
     * 挖矿信息加载成功的回调  or 挖矿开始的回调
     **/
    void loadSuccess();

    /**
     * 挖矿信息获取失败的回调  or 挖矿开始的回调
     **/
    void  loadFail();

    /**
     * 显示msg
     */
    void showMsg();



}
