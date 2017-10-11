package com.tohier.cartercoin.presenter;

import com.tohier.android.config.IContext;
import com.tohier.cartercoin.activity.MyBaseFragmentActivity;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.biz.EntrustBiz;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.listener.AboutEntrustListener;
import com.tohier.cartercoin.ui.EntrustView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/12.
 */

public class EntrustPresenter {

    private EntrustBiz entrustBiz;
    private EntrustView entrustView;
    private IContext context;

    public EntrustPresenter( EntrustView entrustView, IContext context) {
        this.context = context;
        entrustBiz = new EntrustBiz(context);
        this.entrustView = entrustView;
    }

    public void getData(){
        entrustBiz.getEntrustData( new AboutEntrustListener() {
            @Override
            public void success(final String url) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getKLineInfo(url);
                    }
                });

            }

            @Override
            public void success() {
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });

            }

            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {
            }

            @Override
            public void success(HashMap<String, String> map) {
            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }

        });
    }


    public void getTradingNow(){
        entrustBiz.getTradingNow( new AboutEntrustListener() {


            @Override
            public void success(String url) {

            }

            @Override
            public void success() {
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });
            }

            @Override
            public void tradingSuccess(final ArrayList<Transaction> list) {

                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getTrading(list);
                    }
                });


            }

            @Override
            public void success(HashMap<String, String> map) {

            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }


        });
    }

    /**
     * 添加买卖交易
     * @param type
     * @param count
     * @param price
     */
    public void getTradingAdd(String type,String count,String price){
        entrustBiz.getTradingAdd(type, count, price, new AboutEntrustListener() {


            @Override
            public void success(String url) {

            }

            @Override
            public void success() {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getTradingSuccess();

                    }
                });
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         boolean flag = Tools.isPhonticName(msg);
                        if(!flag)
                        {
                            ((MyBaseFragmentActivity)context).sToast(msg);
                            entrustView.fail(msg);
                        }
                    }
                });

            }


            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {
            }

            @Override
            public void success(HashMap<String, String> map) {
            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }


        });
    }


    /**
     * 获取资产信息和汇率
     */
    public void getInfoAndRate(){
        entrustBiz.getInfoAndRate(new AboutEntrustListener() {

            @Override
            public void success(String url) {

            }

            @Override
            public void success() {
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });
            }

            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {
            }

            @Override
            public void success(final HashMap<String, String> map) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getIngoAndRate(map);
                    }
                });

            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }

        });
    }



    /**
     * 是否完善个人信息
     *
     */
    public void isUpdateInfo(){
        entrustBiz.updateInfo(new AboutEntrustListener() {
            @Override
            public void success(String url) {
            }

            @Override
            public void success() {
            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });

            }

            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {
            }

            @Override
            public void success(HashMap<String, String> map) {
            }

            @Override
            public void isUpdateInfo(String flag) {
                entrustView.isUpdateInfo(flag);
            }

            @Override
            public void getCTCInfo(final HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }


        });

    }


    public void getCTCInfo(){
        entrustBiz.getCTCInfo(new AboutEntrustListener() {
            @Override
            public void success(String url) {

            }

            @Override
            public void success() {

            }

            @Override
            public void fail(final String msg) {

                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });
            }

            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {

            }

            @Override
            public void success(HashMap<String, String> map) {

            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(final HashMap<String, String> map) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getCTCInfo(map);
                    }
                });
            }

            @Override
            public void getPriceRangeAndCount(HashMap<String, String> map) {

            }
        });
    }


    public void getPriceRangeAndCount(){
        entrustBiz.getPriceRangeAndCount(new AboutEntrustListener() {
            @Override
            public void success(String url) {

            }

            @Override
            public void success() {

            }

            @Override
            public void fail(final String msg) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.fail(msg);
                    }
                });
            }

            @Override
            public void tradingSuccess(ArrayList<Transaction> list) {

            }

            @Override
            public void success(HashMap<String, String> map) {

            }

            @Override
            public void isUpdateInfo(String flag) {

            }

            @Override
            public void getCTCInfo(HashMap<String, String> map) {

            }

            @Override
            public void getPriceRangeAndCount(final HashMap<String, String> map) {
                ((MyBaseFragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustView.getPriceRangeAndCount(map);
                    }
                });
            }
        });
    }

}
