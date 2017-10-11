package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface GetVersionCodeListener {



    /**
     * 获取版本号成功的回调
     **/
    void loadSuccess(String remark, int androidVersion);

     /**
     * 获取版本号失败的回调
     **/
    void loadFail();



}
