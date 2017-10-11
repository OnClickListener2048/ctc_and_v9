package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.ShareOptionRankingListener;

/**
 * Created by Administrator on 2016/12/8.
 */

public interface IShareOptionRankingBiz {

    /**
     * 全部排行
     * @param code
     * @param listener
     */
    void getAllRanking(String code, ShareOptionRankingListener listener);

    /**
     * 个人排行
     * @param code
     * @param listener
     */
    void getSelfRanking(String code, ShareOptionRankingListener listener);
}
