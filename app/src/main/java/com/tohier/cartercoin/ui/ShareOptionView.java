package com.tohier.cartercoin.ui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/21.
 */

public interface ShareOptionView {
    /**
     * 获取比特币k线图的数据
     */
    void getBtcKLineUrl(String url);


    /**
     * 失败
     * @param msg
     */
    void fail(String msg);

    /**
     * 比特币当前价格和时间戳
     * @param price
     */
    void getBtcPrice(double price,String time);

    /**
     * 期权时间段
     */
    void getTimes(ArrayList<HashMap<String,String>> times);
    /**
     * 期权币种
     */
    void getCoinTypes(ArrayList<HashMap<String,String>> coinTypes);

    /**
     * 期权动态
     */
    void getTrends(ArrayList<HashMap<String,String>> trends);

    /**
     * 获取ctc账户资产
     * @param ctc
     */
    void getCTC(String ctc,String money,String ctcoption);

    /**
     * 期权交易
     */
    void optionDeal();

}
