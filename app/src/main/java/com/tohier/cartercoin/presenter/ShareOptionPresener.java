package com.tohier.cartercoin.presenter;

import com.tohier.android.activity.base.BaseFragmentActivity;
import com.tohier.android.config.IContext;
import com.tohier.cartercoin.biz.ShareOptionBiz;
import com.tohier.cartercoin.listener.ShareOptionListener;
import com.tohier.cartercoin.ui.ShareOptionView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ShareOptionPresener {

    private ShareOptionBiz shareOptionBiz;
    private ShareOptionView shareOptionView;
    private IContext context;

    public ShareOptionPresener( ShareOptionView shareOptionView, IContext context) {
        this.shareOptionBiz = new ShareOptionBiz(context);
        this.shareOptionView = shareOptionView;
        this.context = context;
    }

    public void getBtcKLineUrl(String time,String coinType){
        shareOptionBiz.getBtcKLineUrl(time,coinType,new ShareOptionListener() {


            @Override
            public void successKLineUrl(final String url) {

                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getBtcKLineUrl(url);
                    }
                });

            }

            @Override
            public void fail(final String msg) {

                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });

            }

            @Override
            public void successPrice(double price, String stamp) {

            }


            @Override
            public void successTimes(ArrayList<HashMap<String,String>> times) {

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(String ctc,String monry, String ctcoption) {

            }

            @Override
            public void successOptionDeal() {

            }
        });
    }



    public void getBtcPrice(){
        shareOptionBiz.getBtcPrice( new ShareOptionListener() {


            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });
            }


            @Override
            public void successPrice(final double price, final String stamp) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getBtcPrice(price,stamp);
                    }
                });
            }

            @Override
            public void successTimes(ArrayList<HashMap<String,String>> times) {

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(String ctc,String monry, String ctcoption) {

            }

            @Override
            public void successOptionDeal() {

            }
        });
    }

    public void getTimes(String coinType){
        shareOptionBiz.getTimes(coinType,new ShareOptionListener() {

            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });
            }

            @Override
            public void successPrice(double price, String stamp) {

            }


            @Override
            public void successTimes(final ArrayList<HashMap<String,String>> times) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getTimes(times);
                    }
                });

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(String ctc, String money, String ctcoption) {

            }


            @Override
            public void successOptionDeal() {

            }
        });
    }

    public void getCoinTypes(){
        shareOptionBiz.getCoinType(new ShareOptionListener() {

            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });
            }

            @Override
            public void successPrice(double price, String stamp) {

            }


            @Override
            public void successTimes(ArrayList<HashMap<String,String>> times) {
            }

            @Override
            public void successCoinType(final ArrayList<HashMap<String,String>> coinTypes) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getCoinTypes(coinTypes);
                    }
                });

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(String ctc,String monry, String ctcoption) {

            }

            @Override
            public void successOptionDeal() {

            }
        });
    }



    public void getTrends(String type){
        shareOptionBiz.getTrends(type, new ShareOptionListener() {


            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });
            }

            @Override
            public void successPrice(double price, String stamp) {

            }


            @Override
            public void successTimes(ArrayList<HashMap<String, String>> times) {

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(final ArrayList<HashMap<String, String>> trends) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getTrends(trends);
                    }
                });

            }

            @Override
            public void success(String ctc,String monry, String ctcoption) {

            }

            @Override
            public void successOptionDeal() {

            }
        });
    }

    public void getCTC(String type){
        shareOptionBiz.getCTC(type, new ShareOptionListener() {

            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.fail(msg);
                    }
                });
            }

            @Override
            public void successPrice(double price, String stamp) {

            }

            @Override
            public void successTimes(ArrayList<HashMap<String, String>> times) {

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(final String ctc, final String monry, final String ctcoption) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.getCTC(ctc,monry,ctcoption);
                    }
                });
            }

            @Override
            public void successOptionDeal() {

            }
        });
    }


    public void optionDeal(String time,String payAccount,String shareOptionType,String type,String state,String money,  String price,String cointype){
        shareOptionBiz.optionDeal(time,payAccount,shareOptionType, type, state, money, price,cointype,new ShareOptionListener() {


            @Override
            public void successKLineUrl(String url) {

            }

            @Override
            public void fail(final String msg) {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.equalsIgnoreCase("timeout") || msg.equals("")){

                        }else{
                            ((BaseFragmentActivity)context).sToast(msg);
                        }
                    }
                });

            }

            @Override
            public void successPrice(double price, String stamp) {

            }


            @Override
            public void successTimes(ArrayList<HashMap<String, String>> times) {

            }

            @Override
            public void successCoinType(ArrayList<HashMap<String,String>> coinTypes) {

            }

            @Override
            public void successTrends(ArrayList<HashMap<String, String>> trends) {

            }

            @Override
            public void success(String ctc ,String monry, String ctcoption) {

            }

            public void successOptionDeal() {
                ((BaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareOptionView.optionDeal();
                    }
                });

            }


        });
    }
}
