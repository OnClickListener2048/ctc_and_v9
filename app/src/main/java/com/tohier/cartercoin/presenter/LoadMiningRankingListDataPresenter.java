package com.tohier.cartercoin.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import com.tohier.cartercoin.activity.MiningRankingListActivity;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.biz.LoadMiningRankingListDataBiz;
import com.tohier.cartercoin.listener.LoadMiningRankingListDataListener;
import com.tohier.cartercoin.miningranking.ActiveFragment;
import com.tohier.cartercoin.miningranking.AllFragment;
import com.tohier.cartercoin.miningranking.MonthFragment;
import com.tohier.cartercoin.miningranking.WeekFragment;
import com.tohier.cartercoin.ui.LoadMiningRankingListDataView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */

public class LoadMiningRankingListDataPresenter {

    private LoadMiningRankingListDataBiz loadMiningRankingListDataBiz;
    private LoadMiningRankingListDataView loadMiningRankingListDataView;
    /**
     * 上下文对象
     **/
    private Context context;

    /**
     * 访问网络时 返回msg
     */
    private String msg;

    /**
     * 是否存在活动的标识
     */
    private String isExitActive;
    private String activeNmae;
    private String activeStartDate;


    /**
     * 用于存放排行榜数据
     */
    private List<RankingData> listData = new ArrayList<>();

    /**
     * 存放挖矿排行碎片的数据源
     */
    private List<Fragment> listFrag = new ArrayList<Fragment>();

    public void setListData(List<RankingData> listData) {
        this.listData = listData;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setIsExitActive(String isExitActive) {
        this.isExitActive = isExitActive;
    }

    public void setActiveNmae(String activeNmae) {
        this.activeNmae = activeNmae;
    }

    public void setActiveStartDate(String activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public String getActiveNmae() {
        return activeNmae;
    }

    public LoadMiningRankingListDataPresenter(Context context, LoadMiningRankingListDataView loadMiningRankingListDataView) {
        this.context = context;
        loadMiningRankingListDataBiz = new LoadMiningRankingListDataBiz(context,this);
        this.loadMiningRankingListDataView = loadMiningRankingListDataView;
    }

    public LoadMiningRankingListDataPresenter(Context context, LoadMiningRankingListDataView loadMiningRankingListDataView,String type) {
        this.context = context;
        loadMiningRankingListDataBiz = new LoadMiningRankingListDataBiz(context,this,type);
        this.loadMiningRankingListDataView = loadMiningRankingListDataView;
    }


    /**
     * 请求排行数据
     */
    public void loadMiningRankingListData()
    {
        loadMiningRankingListDataView.showProgress();
        loadMiningRankingListDataBiz.loadData(new LoadMiningRankingListDataListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.hideProgress();
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
                        loadMiningRankingListDataView.hideProgress();
                        loadMiningRankingListDataView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.hideProgress();
                        loadMiningRankingListDataView.showMsg(LoadMiningRankingListDataPresenter.this.msg);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadIndividualRankingSuccess(RankingData datas) {

            }
        });
    }


    /**
     * 请求个人挖矿排行数据
     */
    public void loadPersonalMining(String type)
    {
//        loadMiningRankingListDataView.showProgress();
        loadMiningRankingListDataBiz.loadPersonalMining(new LoadMiningRankingListDataListener() {
            @Override
            public void loadSuccess() {

            }

            @Override
            public void loadFail() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.showMsg(LoadMiningRankingListDataPresenter.this.msg);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadIndividualRankingSuccess(final RankingData datas) {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.setIndividualRanking(datas);
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        },type);
    }



    /**
     * 查看当前排行 是否有活动存在
     */
    public void isExitActive()
    {
        loadMiningRankingListDataView.showProgress();
        loadMiningRankingListDataBiz.isExitActive(new LoadMiningRankingListDataListener() {
            @Override
            public void loadSuccess() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                            loadMiningRankingListDataView.hideProgress();
//                           ((MiningRankingListActivity)context).sToast("有活动");
                            loadMiningRankingListDataView.getActiveTextView().setVisibility(View.VISIBLE);
                            loadMiningRankingListDataView.getActiveTextView().setText(activeNmae);
                            listFrag.add(new ActiveFragment());
                            listFrag.add(new WeekFragment());
                            listFrag.add(new MonthFragment());
                            listFrag.add(new AllFragment());
                            loadPersonalMining("active");
                            loadMiningRankingListDataView.setTextColor("有活动");
                            loadMiningRankingListDataView.getMiningViewPager().setAdapter(new TransactionAdapter(((MiningRankingListActivity)context).getSupportFragmentManager(),listFrag));
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
                        loadMiningRankingListDataView.hideProgress();
                        loadMiningRankingListDataView.loadFail();
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void showMsg() {
                Handler dataHandler = new Handler(
                        context.getMainLooper()) {

                    @Override
                    public void handleMessage(
                            final Message msg) {
                        loadMiningRankingListDataView.hideProgress();
                        listFrag.add(new WeekFragment());
                        listFrag.add(new MonthFragment());
                        listFrag.add(new AllFragment());
                        loadPersonalMining("week");
                        loadMiningRankingListDataView.setTextColor("无活动");
                        loadMiningRankingListDataView.getMiningViewPager().setAdapter(new TransactionAdapter(((MiningRankingListActivity)context).getSupportFragmentManager(),listFrag));

                    }
                };
                dataHandler.sendEmptyMessage(0);
            }

            @Override
            public void loadIndividualRankingSuccess(RankingData datas) {

            }
        });
    }

}
