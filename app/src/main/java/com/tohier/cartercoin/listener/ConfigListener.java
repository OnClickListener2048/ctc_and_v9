package com.tohier.cartercoin.listener;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface ConfigListener {

    /**
     * 获取期权交易信息
     **/
    void success(HashMap<String , String> ininfo);

    /**
     * 获取下注记录失败的回调
     **/
    void fail(String msg);
}
