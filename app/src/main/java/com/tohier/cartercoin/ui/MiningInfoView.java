package com.tohier.cartercoin.ui;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tohier.cartercoin.columnview.RunningTextView;

/**
 * Created by 武文锴 on 2016/11/5.
 */

public interface MiningInfoView {

    void loadFail();
    void showMsg(String msg);

    ProgressBar getProgressBar_power_mining();
    ProgressBar getProgressBar_share_mining();
    ProgressBar getProgressBar_active_mining();
    ProgressBar getProgressBar_task_mining();

    TextView getTv_task_completion_percentage();
    TextView getTv_share_completion_percentage();
    TextView getTv_active_completion_percentage();
    TextView getTv_power_completion_percentage();
    TextView getTv_mining_me_calculating();
    TextView getTv_mining_all_calculating();

    TextView getTv_mining_time();
    TextView getTv_mining_count();
    TextView getIv_mining_icon();

    void showProgress();
    void hideProgress();

    void setMiningStartGifAnim(boolean isStart);

}
