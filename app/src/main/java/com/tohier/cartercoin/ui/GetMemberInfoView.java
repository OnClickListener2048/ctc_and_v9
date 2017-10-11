package com.tohier.cartercoin.ui;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public interface GetMemberInfoView {


    void loadMemberInfoSuccess();
    void loadMemberInfoFail();
    void showMsg(String msg);
    void showProgress();
    void hideProgress();


    /**
     * 显示人民币的TextView
     * @param rmbTextView
     */
    void setRMBTextView(String rmbTextView);

    /**
     * 显示消费账户的TextView
     * @param rmbTextView
     */
    void setXiaoFeiAccountTextView(String rmbTextView);

    /**
     * 显示决战账户的TextView
     * @param rmbTextView
     */
    void setJueZhanTextView(String rmbTextView);

    /**
     * 显示定期卡特币的TextView
     * @param terminalCtcTextView
     */
    void setTerminalCtcTextView(String terminalCtcTextView);
    /**
     * 显示活期卡特币的TextView
     * @param demandTextView
     */
    void setDemandTextView(String demandTextView);


    /**
     * 显示同班的TextView
     * @param rmbTextView
     */
    TextView setTongBanTextView(String rmbTextView);

    /**
     * 显示同系的TextView
     * @param terminalCtcTextView
     */
    TextView setTongXiTextView(String terminalCtcTextView);
    /**
     * 显示同校的TextView
     * @param demandTextView
     */
    TextView setTongXiaoTextView(String demandTextView);
    /**
     * 显示分享人数的TextView
     * @param demandTextView
     */
    void setShareTextView(String demandTextView);

    /**
     * 显示当前卡特币的TextView
     * @param demandTextView
     */
    void setCurrentCTCPriceTextView(String demandTextView);

    /**
     * 显示总资产的TextView
     * @param
     */
    void setallSsetsTextView(String allssets);


    LinearLayout getTwoLevel();
    LinearLayout getThreeLevel();

}
