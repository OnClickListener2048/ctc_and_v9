package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.DealListener;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface IDealBiz {

    /**
     * 获取下注记录
     * @param type
     * @param pager
     * @param listener
     */
  void getDealRecord(String type, String interfaces ,String pager, DealListener listener);
}
