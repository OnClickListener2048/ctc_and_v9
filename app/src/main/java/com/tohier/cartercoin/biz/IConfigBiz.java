package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.ConfigListener;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface IConfigBiz {

    /**
     * 获取期权交易信息
     * @param listener
     */
    void getDealInfo(String reality , ConfigListener listener);

}
