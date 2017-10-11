package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.MiningInfoListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface IMningInfoBiz {

    void loadMiningInfo(MiningInfoListener miningInfoListener);

    void startMining(MiningInfoListener miningInfoListener);

}
