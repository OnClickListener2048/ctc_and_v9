package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tohier.cartercoin.bean.News;
import com.tohier.cartercoin.biz.LoadGongGaoList1Biz;
import com.tohier.cartercoin.biz.LoadLoginPicBiz;
import com.tohier.cartercoin.listener.LoadGongGaoListener;
import com.tohier.cartercoin.listener.LoadLoginPicListener;
import com.tohier.cartercoin.ui.LoadGongGaoListView;
import com.tohier.cartercoin.ui.LoadLoginPicView;

import java.util.List;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public class LoadGongGaoListPresenter {

    private LoadGongGaoList1Biz loadGongGaoList1Biz;
    private LoadGongGaoListView loadGongGaoListView;

    private boolean isWanCheng;
    /**
     * 上下文对象
     **/
    private Context context;

    public LoadGongGaoListPresenter(Context context, LoadGongGaoListView loadGongGaoListView,boolean isWanCheng) {
        this.context = context;
        loadGongGaoList1Biz = new LoadGongGaoList1Biz(context);
        this.loadGongGaoListView = loadGongGaoListView;
        this.isWanCheng = isWanCheng;
    }

    public void loadGongGaoList1()
    {
        isWanCheng = false;
        loadGongGaoList1Biz.loadGongGao1(new LoadGongGaoListener() {
            @Override
            public void loadSuccess(final List<News> datas) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.loadSuccess(datas);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg8) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.showMsg(msg8);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public void loadGongGaoList2()
    {
        isWanCheng = false;
        loadGongGaoList1Biz.loadGongGao2(new LoadGongGaoListener() {
            @Override
            public void loadSuccess(final List<News> datas) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.loadSuccess(datas);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg(final String msg8) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadGongGaoListView.showMsg(msg8);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }
}
