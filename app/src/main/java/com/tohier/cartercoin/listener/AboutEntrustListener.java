package com.tohier.cartercoin.listener;

import com.tohier.cartercoin.bean.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/12.
 */

public interface AboutEntrustListener {

    /**
     * 成功的回调
     */
    void success(String url);

    /**
     * 添加买卖交易成功时的回调
     */
    void success();


    /**
     *失败的回调
     */
    void fail(String msg);

    /**
     * 交易平台买卖实时的回调成功
     * @param list
     */
    void tradingSuccess(ArrayList<Transaction> list);

    /**
     * 返回资产信息和汇率的成功回调
     * @param map
     */
    void success(HashMap<String,String> map);

    /**
     * 是否完善个人信息
     * @param flag
     */
    void isUpdateInfo(String flag);

    /**
     * 卡特币的当前信息
     * @param map
     */
    void getCTCInfo(HashMap<String,String> map);

    /**
     * 获取交易价格范围  数量
     */
    void getPriceRangeAndCount(HashMap<String,String> map);
}
