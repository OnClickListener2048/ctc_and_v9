package com.tohier.cartercoin.miningranking;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.presenter.LoadMiningRankingListDataPresenter;
import com.tohier.cartercoin.ui.LoadMiningRankingListDataView;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ActiveFragment extends BaseFragment implements LoadMiningRankingListDataView {
    private View view;
    private LoadMiningRankingListDataPresenter loadMiningRankingListDataPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  View.inflate(getActivity(), R.layout.fragment_mall,null);

        initData();
        return view;
    }

    @Override
    public void initData() {
        loadMiningRankingListDataPresenter = new LoadMiningRankingListDataPresenter(getActivity(),this,"active");
        loadMiningRankingListDataPresenter.loadMiningRankingListData();
    }

    @Override
    public void loadFail() {
//        sToast("总排行：链接失败");
    }

    @Override
    public void showMsg(String msg) {
//        sToast("总排行："+msg);
    }

    @Override
    public void showProgress() {
        mZProgressHUD.show();
    }

    @Override
    public void hideProgress() {
        mZProgressHUD.hide();
    }

    @Override
    public TextView getActiveTextView() {
        return null;
    }

    @Override
    public ViewPager getMiningViewPager() {
        return null;
    }

    @Override
    public void setIndividualRanking(RankingData rankingData) {

    }

    @Override
    public void setTextColor(String type) {

    }
}
