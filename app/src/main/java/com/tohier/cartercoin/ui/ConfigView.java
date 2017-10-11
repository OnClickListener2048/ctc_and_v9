package com.tohier.cartercoin.ui;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface ConfigView {

    void fail(String msg);

    /**
     * 获取期权交易信息
     **/
    void success(HashMap<String , String> ininfo);
}
