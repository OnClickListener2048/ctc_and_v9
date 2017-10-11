package com.tohier.cartercoin.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public interface NewMiningInfoView {

//    void loadFail();
//    void showMsg(String msg);
//
//    ProgressBar getProgressBar_power_mining();
//    ProgressBar getProgressBar_share_mining();
//    ProgressBar getProgressBar_active_mining();
//    ProgressBar getProgressBar_task_mining();
//
//    TextView getTv_task_completion_percentage();
//    TextView getTv_share_completion_percentage();
//    TextView getTv_active_completion_percentage();
//    TextView getTv_power_completion_percentage();
//    TextView getTv_mining_me_calculating();
//    TextView getTv_mining_all_calculating();
//
//    TextView getTv_mining_time();
//    TextView getTv_mining_count();
//    TextView getIv_mining_icon();
//
//    void showProgress();
//    void hideProgress();
//
//    void setMiningStartGifAnim(boolean isStart);

    /**
     * 添加小球
     */
    void addView(View view);

    /**
     * 获取当前屏幕小球的数量
     */

    int getQiuCount();

    /**
     * 移除view
     */
    void removeView(View view);

    /**
     * 设置消费账户
     */
    void setXiaoFei(Double xiaofei);

    /**
     * 设置α资产
     */
    void setShouYi(Double shouYi);

    /**
     * 设置决战账户
     */
    void setJueZhan(Double jueZhan);

    /**
     * 设置复投账户
     */
    void setFuTou(Double fuTou);

    /**
     * show小手
     */
    void showHand();

    /**
     * hide小手
     */
    void hideHand();

    /**
     * 设置是否可以点击按钮挖矿
     */
    void setStartEnable(boolean isFlag);

    /**
     * 体力值减少
     */
    void jian();

    /**
     * 小人动画开始
     */
    void startAnim();

    /**
     * 小人动画结束
     */
    void stopAnim();

    /**
     * 显示分享PopupWindow
     */
    void showShareWin();

    /**
     * 显示当前可收矿产的数量
     */
    void showCollectCount(int count);

    /**
     * 显示当前可收矿产的数量
     */
    void setChengShuMiningCount(int count);

    /**
     * 显示本次挖矿结束时间的TextView
     */
    void showThisEndTime(String time);

    /**
     * 获取一键收矿的LinearLayout
     * @return
     */
    LinearLayout getOneKeyShouMiningLinearLayout();


    /**
     * 显示正在加载状态的Gif
     */
    void showLoadGif();

    /**
     * 隐藏正在加载状态的Gif
     */
    void hideLoadGif();


    int getViewWidth();

    ImageView getStartMiningImg();

}
