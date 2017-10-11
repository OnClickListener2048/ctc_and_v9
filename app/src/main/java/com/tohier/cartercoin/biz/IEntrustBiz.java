package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.AboutEntrustListener;

/**
 * Created by Administrator on 2016/11/12.
 */

public interface IEntrustBiz {

    /**
     * 获取交易k线图的数据
     * @param listener
     */
    void getEntrustData(AboutEntrustListener listener);


    /**
     * 实时买卖交易
     * @param listener
     */
    void getTradingNow(AboutEntrustListener listener);


    /**
     * 添加买卖交易
     * @param type
     * @param count
     * @param price
     */
    void getTradingAdd(String type,String count,String price,AboutEntrustListener listener);


    /**
     * 会员资产和汇率
     * @param listener
     */
    void getInfoAndRate(AboutEntrustListener listener);


    /**
     * 完善会员信息
     * @param listener
     */
    void updateInfo(AboutEntrustListener listener);

    void getCTCInfo(AboutEntrustListener listener);

    /**
     * 获取交易价格范围  数量
     * @param listener
     */
    void getPriceRangeAndCount(AboutEntrustListener listener);


}
