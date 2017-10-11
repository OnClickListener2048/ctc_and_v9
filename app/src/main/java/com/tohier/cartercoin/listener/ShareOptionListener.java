package com.tohier.cartercoin.listener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/21.
 */

public interface ShareOptionListener {

    void successKLineUrl(String url);
    void fail(String msg);

    void successPrice(double price,String stamp);
    void successTimes(ArrayList<HashMap<String,String>> times);
    void successCoinType(ArrayList<HashMap<String,String>> coinTypes);

    void successTrends(ArrayList<HashMap<String,String>> trends);
    void success(String ctc,String money,String ctcoption);

    /**
     * 二元期权
     */
    void successOptionDeal();
}
