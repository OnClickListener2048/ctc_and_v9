package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.GetVersionCodeListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface ILoadVersionCodeBiz {

    void getVersionCode(GetVersionCodeListener getVersionCodeListener);



    void getUrl(GetVersionCodeListener getVersionCodeListener);

}
