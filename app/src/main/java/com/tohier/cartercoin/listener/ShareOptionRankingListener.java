package com.tohier.cartercoin.listener;

import com.tohier.cartercoin.bean.UserShareOptionRanking;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/8.
 */

public interface ShareOptionRankingListener {

    void successRanking(ArrayList<UserShareOptionRanking> list);
    void fail(String msg);
    void successSelfRanking(UserShareOptionRanking userShareOptionRanking);


}
