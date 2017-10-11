package com.tohier.cartercoin.presenter;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.MyBaseFragmentActivity;
import com.tohier.cartercoin.bean.UserShareOptionRanking;
import com.tohier.cartercoin.biz.ShareOptionRankingBiz;
import com.tohier.cartercoin.listener.ShareOptionRankingListener;
import com.tohier.cartercoin.ui.ShareOptionRankingView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ShareOptionRankingPresenter {
    private IContext context;
    private ShareOptionRankingBiz shareOptionRankingBiz;
    private ShareOptionRankingView shareOptionRankingView;

    public ShareOptionRankingPresenter(IContext context, ShareOptionRankingView shareOptionRankingView) {
        this.shareOptionRankingBiz = new ShareOptionRankingBiz(context);
        this.context = context;
        this.shareOptionRankingView = shareOptionRankingView;
    }


    public void getRanking(final String code){
        shareOptionRankingBiz.getAllRanking(code, new ShareOptionRankingListener() {
            @Override
            public void successRanking(final ArrayList<UserShareOptionRanking> list) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionRankingView.successRanking(list);
                    }
                });
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionRankingView.fail(msg+"1");
                    }
                });
            }

            @Override
            public void successSelfRanking(UserShareOptionRanking userShareOptionRanking) {

            }
        });
    }

    public void getSelfRanking(String code){
        shareOptionRankingBiz.getSelfRanking(code, new ShareOptionRankingListener() {
            @Override
            public void successRanking(final ArrayList<UserShareOptionRanking> list) {

            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionRankingView.fail(msg+"1");
                    }
                });
            }

            @Override
            public void successSelfRanking(final UserShareOptionRanking userShareOptionRanking) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionRankingView.successSelfRanking(userShareOptionRanking);
                    }
                });
            }
        });
    }



}
