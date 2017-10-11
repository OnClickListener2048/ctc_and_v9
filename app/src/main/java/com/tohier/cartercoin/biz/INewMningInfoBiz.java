package com.tohier.cartercoin.biz;

import com.tohier.cartercoin.listener.MiningInfoListener;
import com.tohier.cartercoin.listener.NewMiningInfoListener;

/**
 * Created by 武文锴 on 2016/11/5.
 *
 */

public interface INewMningInfoBiz {

    void loadMiningInfo(NewMiningInfoListener miningInfoListener);

    void startMining(NewMiningInfoListener miningInfoListener);

}
