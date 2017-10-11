package com.tohier.cartercoin.ui;

import com.tohier.cartercoin.bean.News;

import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public interface LoadGongGaoListView {

    void showMsg(String msg);
    void loadSuccess(List<News> news);
    void loadFail();

}
