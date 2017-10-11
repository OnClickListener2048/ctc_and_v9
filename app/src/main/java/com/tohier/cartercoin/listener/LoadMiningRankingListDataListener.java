package com.tohier.cartercoin.listener;

import com.tohier.cartercoin.bean.MiningInfo;
import com.tohier.cartercoin.bean.RankingData;

import java.util.List;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface LoadMiningRankingListDataListener {



    /**
     * 登录成功的回调
     **/
    void loadSuccess();

    /**
     * 登录失败的回调
     **/
    void loadFail();

    /**
     * 显示msg
     */
    void showMsg();

    void loadIndividualRankingSuccess(RankingData datas);


}
