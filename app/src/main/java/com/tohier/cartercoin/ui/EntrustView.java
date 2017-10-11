package com.tohier.cartercoin.ui;

import com.tohier.cartercoin.bean.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/12.
 */

public interface EntrustView {
    /**
     *
     * 获取K线图url
     * @param url
     */
    void getKLineInfo(String url);
    void fail(String msg);

    void getTradingSuccess();

    /**
     * 获取交易信息
     * @param list
     */
    void getTrading(ArrayList<Transaction> list);

    /**
     * 获取会员资产和汇率
     * @param map
     */
    void getIngoAndRate(HashMap<String,String> map);



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
