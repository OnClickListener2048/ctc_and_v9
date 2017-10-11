package com.tohier.cartercoin.listener;

import com.tohier.cartercoin.bean.News;

import java.util.List;

/**
 * Created by 武文锴 on 2016/11/4.
 */

public interface LoadGongGaoListener {



    /**
     * 公告列表信息加载成功的回调
     **/
    void loadSuccess(List<News> datas);

    /**
     * 公告列表信息获取失败的回调
     **/
    void  loadFail();

    /**
     * 显示msg
     */
    void showMsg(String msg);



}
