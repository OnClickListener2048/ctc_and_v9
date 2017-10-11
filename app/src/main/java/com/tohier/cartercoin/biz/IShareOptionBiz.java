package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.ShareOptionListener;

/**
 * Created by Administrator on 2016/11/21.
 */

public interface IShareOptionBiz {

    /**
     * 获取比特币k线图的数据
     * @param listener
     */
    void getBtcKLineUrl(String time,String coinType,ShareOptionListener listener);



    /**
     * 获取比特币实时价格
     * @param listener
     */
    void getBtcPrice(ShareOptionListener listener);


    /**
     * 获取期权时间段
     * @param listener
     */
    void getTimes(String coinType,ShareOptionListener listener);

    /**
     * 获取币种
     * @param listener
     */
    void getCoinType(ShareOptionListener listener);

    /**
     *
     * 获取期权动态
     * @param type
     * @param listener
     */
    void getTrends(String type,ShareOptionListener listener);

    /**
     * 获取实盘、虚拟盘的ctc数量
     * 0---实盘   1---虚拟盘
     * @param type
     * @param listener
     */
    void getCTC(String type,ShareOptionListener listener);

    /**
     * 期权交易
     * @param payAccount  交易类型
     * @param shareOptionType  实盘  虚拟盘
     * @param type   时间段  1 5  15
     * @param state  买入方向
     * @param money  下注金额
     * @param price   当前比特币价格
     * @param cointype  币种
     * @param listener
     */
    void optionDeal(String time,String payAccount,String shareOptionType,String type,String state,String money,String price,String cointype,ShareOptionListener listener);
}
