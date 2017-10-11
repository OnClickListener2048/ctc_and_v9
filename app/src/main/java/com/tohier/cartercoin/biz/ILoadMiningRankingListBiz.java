package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.LoadMiningRankingListDataListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface ILoadMiningRankingListBiz {

    void loadData(LoadMiningRankingListDataListener loadMiningRankingListDataListener);

    void isExitActive(LoadMiningRankingListDataListener loadMiningRankingListDataListener);

    void loadPersonalMining(LoadMiningRankingListDataListener loadMiningRankingListDataListener, String type);
}
