package com.tohier.cartercoin.shareoptionrankingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.ShareOptionRankingActivity;
import com.tohier.cartercoin.adapter.ShareOptionRankingAdapter;
import com.tohier.cartercoin.bean.UserShareOptionRanking;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.presenter.ShareOptionRankingPresenter;
import com.tohier.cartercoin.ui.ShareOptionRankingView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ShareOptionMoneyRankingFragment extends BaseFragment implements ShareOptionRankingView {

    private View view;
    private ListView lvMoney;
    private ShareOptionRankingAdapter adapter;
    private ArrayList<UserShareOptionRanking> list = new ArrayList<UserShareOptionRanking>();
    private ShareOptionRankingPresenter presenter;
    private LoadingView gifLoading;
    private NoDataView ivNodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_money_ranking,container,false);

        init();
        setup();

        return view;
    }

    private void setup() {

    }

    private void init() {
        lvMoney = (ListView) view.findViewById(R.id.lv_money);
        ivNodata = (NoDataView) view.findViewById(R.id.iv_nodata);
        gifLoading = (LoadingView) view.findViewById(R.id.cif_loading);

        adapter = new ShareOptionRankingAdapter(getActivity(),list,1);
        lvMoney.setAdapter(adapter);
        gifLoading.setVisibility(View.VISIBLE);
        presenter = new ShareOptionRankingPresenter((ShareOptionRankingActivity)getActivity(),this);
        presenter.getRanking("member_option_all");
    }


    @Override
    public void initData() {

    }

    @Override
    public void successRanking(ArrayList<UserShareOptionRanking> list) {
        if (list.size()>0){
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            ivNodata.setVisibility(View.GONE);
            gifLoading.setVisibility(View.GONE);
        }else{
            gifLoading.setVisibility(View.GONE);
            ivNodata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void fail(String msg) {
        gifLoading.setVisibility(View.GONE);
        ivNodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void successSelfRanking(UserShareOptionRanking userShareOptionRanking) {

    }
}
