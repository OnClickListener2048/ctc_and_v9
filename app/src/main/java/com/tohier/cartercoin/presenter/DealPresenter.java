package com.tohier.cartercoin.presenter;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.bean.DealRecord;
import com.tohier.cartercoin.biz.DealBiz;
import com.tohier.cartercoin.listener.DealListener;
import com.tohier.cartercoin.ui.DealView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */

public class DealPresenter {

    private DealBiz dealBiz;
    private DealView dealView;
    private IContext context;

    public DealPresenter( DealView dealView, IContext context) {
        this.dealBiz = new DealBiz(context);
        this.dealView = dealView;
        this.context = context;
    }


    public void getDealRecord(String type, String interfaces ,String pager){
        dealBiz.getDealRecord(type, interfaces, pager, new DealListener() {
            @Override
            public void success(final ArrayList<DealRecord> list) {

                  dealView.getDeals(list);

            }

            @Override
            public void failure(final String msg) {

                dealView.failure(msg);
            }


        });
    }

}
