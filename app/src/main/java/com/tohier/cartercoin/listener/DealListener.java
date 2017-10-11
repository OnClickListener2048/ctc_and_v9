package com.tohier.cartercoin.listener;

import com.tohier.cartercoin.bean.DealRecord;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */
public interface DealListener {

    /**
     * 获取下注记录成功的回调
     **/
    void success(ArrayList<DealRecord> list);

    /**
     * 获取下注记录失败的回调
     **/
    void failure(String msg);
}
