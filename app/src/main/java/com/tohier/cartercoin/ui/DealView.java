package com.tohier.cartercoin.ui;

import com.tohier.cartercoin.bean.DealRecord;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface DealView {

    void failure(String msg);

    /**
     * 获取交易信息
     * @param list
     */
    void getDeals(ArrayList<DealRecord> list);

}
