package com.tohier.cartercoin.listener;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface GetMemberInfoListener {



    /**
     * 获取成功的回调
     **/
    void loadSuccess();

    /**
     * 获取失败的回调
     **/
    void loadFail();

    void showMsg(String msg);

    void loadRelationshipInfoSuccess(String share,String tongzhuo,String tongzu,String tongban);

    void loadExchangerateInfoSuccess(String price);

}
