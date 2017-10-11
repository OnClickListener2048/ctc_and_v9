package com.tohier.cartercoin.ui;

import android.support.v4.view.ViewPager;
import android.widget.TextView;
import com.tohier.cartercoin.bean.RankingData;
/**
 * Created by 武文锴 on 2016/11/5.
 */

public interface LoadMiningRankingListDataView {

    void loadFail();
    void showMsg(String msg);
    void showProgress();
    void hideProgress();
    TextView getActiveTextView();
    ViewPager getMiningViewPager();

    void setIndividualRanking(RankingData rankingData);

    void setTextColor(String type);

}
