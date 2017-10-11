package com.tohier.cartercoin.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TodayMiningRankingAdapter;
import com.tohier.cartercoin.bean.MiningNotice;
import com.tohier.cartercoin.bean.TodayMiningRankingBean;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.RunningTextView;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.IPUtil;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyNetworkConnection;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.NewMiningInfoPresenter;
import com.tohier.cartercoin.ui.NewMiningInfoView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.CircleLightShape;

/**
 * Created by Administrator on 2016/11/9.
 */

public class NewMiningActivity extends MyBaseActivity implements NewMiningInfoView {

    private View view;
    private PopupWindow window1;

    private ImageView iv_into_luckpan;
    private boolean taskIconLoadSuccess = false;
    private ImageView iv_mining_active;
    private TextView tv_surplus_task_time, tv_onekey_time;
    private TextView tv_message_count;
    public static int SLEEPTIME = 6;
    public static int SHOWTIME = 3;
    public int requestTime = 0;

    private SharedPreferences sharedpreferences;

    private HighLight mHightLight;
    //分享view
    private View view1;
    PopupWindow window = null;

    private LoadingView gifImageView;

    /**
     * 这是分享图片所用的名称
     */
    public static final String SHARE_PIC = "share8.jpg";

    /**
     * 显示本次挖矿结束时间
     */
    public TextView tv_this_end_time;
    int endTime = 0;


    /**
     * 图片保存的路劲
     **/
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory() + "/download_test/";

    private static final String WX_APP_ID = "wx7ad749f6cba84064";

    public IWXAPI api;

    /**
     * 头像
     */
    private CircleImageView iv_mining_member_head_img;

    /**
     * 私有矿池
     */
    private TextView tv_mining_pool;

    /**
     * 当前可收矿包的数量
     */
    private int chengShuCount;

    /**
     * 挖矿说明
     */
    private ImageView iv_shuoming;

    /**
     * 提示开始挖矿的小手
     */
    private ImageView iv_start_mining_prompt_hand;

    /**
     * 点击侧滑的显示私有矿池 我的算力
     */
    private RelativeLayout relativeLayout_is_show_private_pool, relativeLayout_mining_bg, relativeLayout_check_mining_level, relativeLayout_is_show_assets, relativeLayout_mines;

    /**
     * 挖出的矿显示的position
     */
    private LinearLayout linearLayout_show_share_win, linearLayout_onekey_shou_mining;

    /**
     * 开始挖矿的ImageView
     */
    private ImageView iv_start_mining, iv_question_mark;

    private NewMiningInfoPresenter newMiningInfoPresenter;

    private ProgressBar progressBar_mining_comprehensive_value;
    private TextView tv_mining_comprehensive_value;
    private RunningTextView tv_xiaofei_account, tv_shoyi_account, tv_juezhan_account, tv_share, tv_active, tv_vip;
    private TextView tv_mining_level;

    private boolean privateMiningIsShow = false;
    private boolean assetsIsShow = false;

    private LinearLayout linearLayout_geren_suanli_and_mining, learLayout_into_share, learLayout_into_active, learLayout_into_vip, linearLayout_into_assets, linearLayout_accunt;

    private TextView iv_mining_strength, tv_mining_count;

    private int PERCENT_TOTAL = 0;

    int percentFluency = 0;

    private ListView listview_mining;
    private TextView tv_more_ranking;

    //弹幕
    private LinearLayout linearLayout_barrage_prompt;
    private CircleImageView iv_barrage_headimg;
    private TextView tv_barrage_content;
    private ImageView iv_notice_start_bg, iv_notice_end_bg;

    private int qiuViewWidth = 0;

    final Runnable mUpdateProgressFluency = new Runnable() {
        final int everyTimeAddF = 1;
        final int delay = 1000 / (100 / everyTimeAddF);

        @Override
        public void run() {
            percentFluency += everyTimeAddF;

            if (percentFluency > PERCENT_TOTAL) {
                mHandler.removeCallbacks(mUpdateProgressFluency);
            } else {
                Message msg = mHandler.obtainMessage();
                msg.arg1 = percentFluency;
                msg.what = What.FLUENCY;
                mHandler.sendMessageDelayed(msg, delay);
            }
        }
    };
    private TodayMiningRankingAdapter adapter;
    //体力值
    private String strengthvalue;
    private int tili;
    private AnimatorSet btnSexAnimatorSet;
    private String[] textStrings;
    private Tencent mTencent;
    private QzoneShare qzoneTencent;
    private int x = 1;
    private ArrayList<MiningNotice> notices = new ArrayList<MiningNotice>();
    private Timer startActiveTimer;
    private TimerTask startActivetimerTask;
    private String isStart;
    private TimerTask taskSurplusTimeTimerTask, onekeySurplusTimeTimerTask;
    private int surplus_task_time, surplus_onekey_time;
    private Timer taskSurplusTimeTimer, onekeySurplusTimeTimer;

    static class What {
        final static int FLUENCY = 1;
    }

    final Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case What.FLUENCY:
                    progressBar_mining_comprehensive_value.setProgress(msg.arg1);
                    mHandler.post(mUpdateProgressFluency);
                    break;
            }
        }
    };

    int intoCount = 0;
    int showCount = 0;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isEnd = true;

    final Handler noticeHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x111:
                    if (intoCount <= requestTime) {

                        if (isEnd) {
                            isEnd = false;
                            showCount = 0;
                            if (timer != null) {
                                timer.cancel();
                            }

                            if (timerTask != null) {
                                timerTask.cancel();
                            }

                            timer = new Timer();

                            timerTask = new TimerTask() {
                                @Override
                                public void run() {

                                    if (showCount + 1 <= notices.size()) {
                                        runOnUiThread(new Runnable() {
                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                            @Override
                                            public void run() {
                                                if (showCount >= notices.size()) {
                                                    return;
                                                }
                                                final MiningNotice miningNotice = notices.get(showCount);
                                                showCount = showCount + 1;

                                                if (miningNotice.getPic() != null && !miningNotice.getPic().equals("")) {
                                                    if (!isDestroyed()) {
                                                        Glide.with(NewMiningActivity.this).load(miningNotice.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_barrage_headimg) {
                                                            @Override
                                                            protected void setResource(Bitmap resource) {
                                                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                                circularBitmapDrawable.setCircular(true);
                                                                linearLayout_barrage_prompt.setVisibility(View.VISIBLE);
                                                                iv_barrage_headimg.setVisibility(View.VISIBLE);
                                                                iv_barrage_headimg.setImageDrawable(circularBitmapDrawable);
                                                                if (miningNotice.getContents() != null && !miningNotice.getContents().equals("")) {
                                                                    tv_barrage_content.setText(miningNotice.getContents());
                                                                } else {
                                                                    tv_barrage_content.setText("");
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    linearLayout_barrage_prompt.setVisibility(View.VISIBLE);
                                                    iv_barrage_headimg.setVisibility(View.VISIBLE);
                                                    iv_barrage_headimg.setImageResource(R.mipmap.iv_member_default_head_img);
                                                    if (miningNotice.getContents() != null && !miningNotice.getContents().equals("")) {
                                                        tv_barrage_content.setText(miningNotice.getContents());
                                                    } else {
                                                        tv_barrage_content.setText("");
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                linearLayout_barrage_prompt.setVisibility(View.INVISIBLE);
                                                iv_barrage_headimg.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }

                                    try {
                                        Thread.sleep(SHOWTIME * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            linearLayout_barrage_prompt.setVisibility(View.INVISIBLE);
                                            iv_barrage_headimg.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                }
                            };
                            timer.schedule(timerTask, 0, SHOWTIME + SLEEPTIME * 1000);
                        }

                        intoCount = intoCount + 1;
                        noticeHandler.postDelayed(noticeThread, 1000);
                    } else {
                        intoCount = 0;
                        isEnd = true;
                        noticeHandler.removeCallbacks(noticeThread);
                        miningNotice();
                        getMemberState();
                        isMaintain();
                    }
                    break;

                case 0x112:

                    if (!window1.isShowing()) {
                        // 设置背景颜色变暗
                        WindowManager.LayoutParams lp5 = getWindow().getAttributes();
                        lp5.alpha = 0.5f;
                        getWindow().setAttributes(lp5);
                        window1.setOnDismissListener(new PopupWindow.OnDismissListener() {

                            @Override
                            public void onDismiss() {
                                WindowManager.LayoutParams lp3 = MainActivity.mainActivity.getWindow().getAttributes();
                                lp3.alpha = 1f;
                                MainActivity.mainActivity.getWindow().setAttributes(lp3);
                            }
                        });

                        window1.setOutsideTouchable(true);

                        // 实例化一个ColorDrawable颜色为半透明
                        ColorDrawable dw = new ColorDrawable(0x00ffffff);
                        window1.setBackgroundDrawable(dw);
                        // 在底部显示
                        window1.showAtLocation(tv_shoyi_account,
                                Gravity.BOTTOM, 0, 0);

                    }

                    if (window1 != null && window1.isShowing()) {
                        // 设置背景颜色变暗
                        WindowManager.LayoutParams lp5 = MainActivity.mainActivity.getWindow().getAttributes();
                        lp5.alpha = 0.5f;
                        MainActivity.mainActivity.getWindow().setAttributes(lp5);
                    }


                    break;
            }
        }
    };

    final Thread noticeThread = new Thread() {
        @Override
        public void run() {
            super.run();
            noticeHandler.sendEmptyMessage(0x111);
        }
    };

    final Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            noticeHandler.sendEmptyMessage(0x112);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_newmining_layout);

        initData();
        setUpView();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Date d = new Date();
                //用d.getHour()可以获取当前小时数。
                final int hour = d.getHours();
                if (hour >= 6 && hour < 18) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout_mining_bg.setBackgroundResource(R.mipmap.iv_mining_day_bg);
                            iv_notice_start_bg.setBackgroundResource(R.mipmap.day_bg1);
                            tv_barrage_content.setBackgroundResource(R.mipmap.day_bg2);
                            iv_notice_end_bg.setBackgroundResource(R.mipmap.day_bg3);
                            tv_surplus_task_time.setTextColor(0xffff0000);
                        }
                    });
                } else if (hour >= 18 || hour < 6) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout_mining_bg.setBackgroundResource(R.mipmap.iv_mining_night_bg);
                            iv_notice_start_bg.setBackgroundResource(R.mipmap.day_bg1);
                            tv_barrage_content.setBackgroundResource(R.mipmap.day_bg2);
                            iv_notice_end_bg.setBackgroundResource(R.mipmap.day_bg3);
                            tv_surplus_task_time.setTextColor(0xffffffff);
                        }
                    });
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);

    }

    @Override
    public void initData() {
        view = View.inflate(this, R.layout.activity_prompt_vip, null);
        window1 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        iv_mining_member_head_img = (CircleImageView) findViewById(R.id.iv_mining_member_head_img);
        iv_barrage_headimg = (CircleImageView) findViewById(R.id.iv_barrage_headimg);
        tv_mining_pool = (TextView) findViewById(R.id.tv_mining_pool);
        tv_barrage_content = (TextView) findViewById(R.id.tv_barrage_content);
        iv_shuoming = (ImageView) findViewById(R.id.iv_shuoming);
        iv_into_luckpan = (ImageView) findViewById(R.id.iv_into_luckpan);
        iv_mining_active = (ImageView) findViewById(R.id.iv_mining_active);
        tv_surplus_task_time = (TextView) findViewById(R.id.tv_surplus_task_time);
        tv_onekey_time = (TextView) findViewById(R.id.tv_onekey_time);
        tv_message_count = (TextView) findViewById(R.id.tv_message_count);
        relativeLayout_is_show_private_pool = (RelativeLayout) findViewById(R.id.relativeLayout_is_show_private_pool);
        relativeLayout_mining_bg = (RelativeLayout) findViewById(R.id.relativeLayout_mining_bg);
        relativeLayout_check_mining_level = (RelativeLayout) findViewById(R.id.relativeLayout_check_mining_level);
        relativeLayout_is_show_assets = (RelativeLayout) findViewById(R.id.relativeLayout_is_show_assets);
        iv_start_mining_prompt_hand = (ImageView) findViewById(R.id.iv_start_mining_prompt_hand);
        iv_start_mining = (ImageView) findViewById(R.id.iv_start_mining);
        iv_question_mark = (ImageView) findViewById(R.id.iv_question_mark);
        iv_notice_start_bg = (ImageView) findViewById(R.id.iv_notice_start_bg);
        iv_notice_end_bg = (ImageView) findViewById(R.id.iv_notice_end_bg);
        relativeLayout_mines = (RelativeLayout) findViewById(R.id.relativeLayout_mines);
        learLayout_into_share = (LinearLayout) findViewById(R.id.learLayout_into_share);
        learLayout_into_active = (LinearLayout) findViewById(R.id.learLayout_into_active);
        learLayout_into_vip = (LinearLayout) findViewById(R.id.learLayout_into_vip);
        linearLayout_accunt = (LinearLayout) findViewById(R.id.linearLayout_accunt);
        linearLayout_into_assets = (LinearLayout) findViewById(R.id.linearLayout_into_assets);
        linearLayout_show_share_win = (LinearLayout) findViewById(R.id.linearLayout_show_share_win);
        linearLayout_onekey_shou_mining = (LinearLayout) findViewById(R.id.linearLayout_onekey_shou_mining);
        linearLayout_barrage_prompt = (LinearLayout) findViewById(R.id.linearLayout_barrage_prompt);
        progressBar_mining_comprehensive_value = (ProgressBar) findViewById(R.id.progressBar_mining_comprehensive_value);
        tv_mining_comprehensive_value = (TextView) findViewById(R.id.tv_mining_comprehensive_value);
        tv_share = (RunningTextView) findViewById(R.id.tv_share);
        tv_active = (RunningTextView) findViewById(R.id.tv_active);
        tv_vip = (RunningTextView) findViewById(R.id.tv_vip);
        tv_mining_level = (TextView) findViewById(R.id.tv_mining_level);
        tv_xiaofei_account = (RunningTextView) findViewById(R.id.tv_xiaofei_account);
        tv_shoyi_account = (RunningTextView) findViewById(R.id.tv_shouyi_account);
        tv_juezhan_account = (RunningTextView) findViewById(R.id.tv_juezhan_account);
        listview_mining = (ListView) findViewById(R.id.listview_mining);
        tv_more_ranking = (TextView) findViewById(R.id.tv_more_ranking);
        tv_mining_count = (TextView) findViewById(R.id.tv_mining_count);
        tv_this_end_time = (TextView) findViewById(R.id.tv_this_end_time);
        gifImageView = (LoadingView) findViewById(R.id.gif_loading);

        tv_xiaofei_account.setFormat("0.0000");
        tv_shoyi_account.setFormat("0.0000");
        tv_juezhan_account.setFormat("0.0000");

        tv_share.setFormat("0");
        tv_active.setFormat("0");
        tv_vip.setFormat("0");

        listview_mining.setSelector(new ColorDrawable(Color.TRANSPARENT));

        iv_mining_strength = (TextView) findViewById(R.id.iv_mining_strength);
        linearLayout_geren_suanli_and_mining = (LinearLayout) findViewById(R.id.linearLayout_geren_suanli_and_mining);

        //加载分享PopupWindow
        view1 = View.inflate(this, R.layout.popupwindow_share, null);

        window = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

//        final View view = View.inflate(this, R.layout.fragment_mining_qiuqiu_layout,null);
//        view.measure(0,0);//通知系统主动测量
        ;
        qiuViewWidth = getWindowManager().getDefaultDisplay().getWidth() / 6;

        getTaskIcon();
        showLoadGif();
        taskIsComplete();
        newMiningInfoPresenter = new NewMiningInfoPresenter(NewMiningActivity.this, this);
        newMiningInfoPresenter.loadMiningInfo();
        miningNotice();

        iv_into_luckpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewMiningActivity.this, NewSpanLayoutActivity2.class));
            }
        });

        linearLayout_onekey_shou_mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifImageView.setVisibility(View.VISIBLE);
                linearLayout_onekey_shou_mining.setClickable(false);
                if (relativeLayout_mines.getChildCount() != 0) {
                    if (newMiningInfoPresenter.isOver || newMiningInfoPresenter.isOver == false && relativeLayout_mines.getChildCount() > 1) {
                        //调用一键收取的接口
                        HttpConnect.post(NewMiningActivity.this, "member_mine_collect_all", null,
                                new Callback() {

                                    @Override
                                    public void onResponse(Response arg0)
                                            throws IOException {
                                        String json = arg0.body().string();
                                        final JSONObject data = JSONObject.fromObject(json);
                                        if (data.get("status").equals("success")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String value = data.getJSONArray("data").optJSONObject(0).optString("value");
                                                    if (!TextUtils.isEmpty(value)) {
                                                        if (!value.equals("0")) {
                                                            if (newMiningInfoPresenter.isOver) {
                                                                int count = newMiningInfoPresenter.chengshuData.size() + relativeLayout_mines.getChildCount();
                                                                if (tili - count >= 0) {
                                                                    //计时器要停掉
                                                                    //timer.cancel();
                                                                    //体力足够 一键就可以收取完毕 挖矿信息获取失败 需要
                                                                    if (newMiningInfoPresenter.isOver) {
                                                                        if (tili - newMiningInfoPresenter.chengshuData.size() + relativeLayout_mines.getChildCount() >= 0) {
                                                                            tv_mining_count.setText(" x " + 0);
                                                                        }
                                                                    }
                                                                    newMiningInfoPresenter.chengshuData.clear();
                                                                    newMiningInfoPresenter.getList().clear();
                                                                    newMiningInfoPresenter.weichengshuData.clear();
                                                                    //清除当前屏幕所有小球
                                                                    relativeLayout_mines.removeAllViews();
                                                                    //重新加载小球的数据
                                                                    //重新获取体力值与账户信息  and refresh privateMining
                                                                    loadAccountInfo();
                                                                    loadMemberPrivateMiningPool();
                                                                    newMiningInfoPresenter.loadMiningInfo();
                                                                } else {
                                                                    showShareWin();
                                                                }
                                                            } else {
                                                                int count = newMiningInfoPresenter.chengshuData.size() + relativeLayout_mines.getChildCount() - 1;
                                                                if (tili - count >= 0) {
                                                                    //计时器要停掉
                                                                    //timer.cancel();
                                                                    //体力足够 一键就可以收取完毕 挖矿信息获取失败 需要
                                                                    if (newMiningInfoPresenter.isOver) {
                                                                        if (tili - newMiningInfoPresenter.chengshuData.size() + relativeLayout_mines.getChildCount() >= 0) {
                                                                            tv_mining_count.setText(" x " + 0);
                                                                        }
                                                                    }
                                                                    newMiningInfoPresenter.chengshuData.clear();
                                                                    newMiningInfoPresenter.getList().clear();
                                                                    newMiningInfoPresenter.weichengshuData.clear();
                                                                    //清除当前屏幕所有小球
                                                                    relativeLayout_mines.removeAllViews();
                                                                    //重新加载小球的数据
                                                                    //重新获取体力值与账户信息  and refresh privateMining
                                                                    loadAccountInfo();
                                                                    loadMemberPrivateMiningPool();
                                                                    newMiningInfoPresenter.loadMiningInfo();
                                                                } else {
                                                                    showShareWin();
                                                                }
                                                            }

                                                            gifImageView.setVisibility(View.GONE);
                                                            linearLayout_onekey_shou_mining.setClickable(true);
                                                        } else {
                                                            linearLayout_onekey_shou_mining.setVisibility(View.GONE);
                                                            sToast("一键收矿已失效");
                                                            gifImageView.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!TextUtils.isEmpty(data.getString("msg"))) {
                                                        if (!Tools.isPhonticName(data.getString("msg"))) {
                                                            sToast(data.getString("msg"));
                                                        }
                                                    }

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Request arg0, IOException arg1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                gifImageView.setVisibility(View.GONE);
//                                                    sToast("请检查您的网络链接状态");
                                                linearLayout_onekey_shou_mining.setClickable(true);
                                            }
                                        });
                                    }
                                });
                    } else {
                        gifImageView.setVisibility(View.GONE);
                        sToast("当前没有可以收取的矿");
                        linearLayout_onekey_shou_mining.setClickable(true);
                    }
                } else {
                    gifImageView.setVisibility(View.GONE);
                    sToast("当前没有可以收取的矿");
                    linearLayout_onekey_shou_mining.setClickable(true);
                }
            }
        });
    }

    private void setUpView() {
        /**
         * 进入挖矿排行
         */
        tv_more_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewMiningActivity.this, WaKuangRankingActivity.class));
            }
        });
        /**
         * 进入挖矿说明
         */
        iv_shuoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMiningActivity.this, MiningShuoMingActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 进入购买资产包页面
         */
        linearLayout_into_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMiningActivity.this, BuyAssetsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 进入活动页面
         */
        iv_mining_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_mining_active.clearAnimation();
                Intent intent = new Intent(NewMiningActivity.this, MiningActiveActivity.class);
                startActivity(intent);
            }
        });

        relativeLayout_is_show_assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assetsIsShow) {
                    assetsIsShow = true;
                    linearLayout_into_assets.setVisibility(View.VISIBLE);
                } else {
                    assetsIsShow = false;
                    linearLayout_into_assets.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 查看会员等级一览表
         */
        relativeLayout_check_mining_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(NewMiningActivity.this, VipListExplainActivity.class);
//                startActivity(intent);
            }
        });

        /**
         * 点击侧滑显示私有矿池
         */
        relativeLayout_is_show_private_pool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privateMiningIsShow) {
                    privateMiningIsShow = false;
                    linearLayout_geren_suanli_and_mining.setVisibility(View.VISIBLE);
                } else {
                    privateMiningIsShow = true;
                    linearLayout_geren_suanli_and_mining.setVisibility(View.GONE);
                }
            }
        });

        linearLayout_geren_suanli_and_mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewMiningActivity.this, PrivateMiningExplainActivity.class));
            }
        });

        /**
         * 提示开始挖矿的小手 使用属性动画 左右移动
         */
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_start_mining_prompt_hand, "translationX", 0.0f, 20, 0f, 0f);
        animator.setDuration(1500);//动画时间
        animator.setInterpolator(new BounceInterpolator());//实现反复移动的效果
        animator.setRepeatCount(-1);//设置动画重复次数
        animator.start();//启动动画

        /**
         * 开始挖矿
         */
        iv_start_mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMiningInfoPresenter.startMining();
            }
        });

        /**
         * 进入分享
         */
        learLayout_into_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewMiningActivity.this, NewShareActivity.class));
            }
        });

        /**
         * 进入点赞排行榜
         */
        learLayout_into_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMiningActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 进入购买会员页面
         */
        learLayout_into_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpConnect.post(NewMiningActivity.this, "member_buy_upgrade_count", null, new Callback() {
                    @Override
                    public void onResponse(Response arg0) throws IOException {
                        String json = arg0.body().string();
                        final JSONObject data = JSONObject.fromObject(json);
                        if (data.optString("status").equals("success")) {
                            NewMiningActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    textStrings = new String[]{data.optJSONArray("data").optJSONObject(0).optString("one"),
                                            data.optJSONArray("data").optJSONObject(0).optString("two"),
                                            data.optJSONArray("data").optJSONObject(0).optString("three"),
                                            data.optJSONArray("data").optJSONObject(0).optString("four"),
                                            data.optJSONArray("data").optJSONObject(0).optString("five"),
                                            data.optJSONArray("data").optJSONObject(0).optString("six"),
                                            data.optJSONArray("data").optJSONObject(0).optString("severn"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eight"),
                                            data.optJSONArray("data").optJSONObject(0).optString("nine"),
                                            data.optJSONArray("data").optJSONObject(0).optString("ten"),
                                            data.optJSONArray("data").optJSONObject(0).optString("eleven"),
                                            data.optJSONArray("data").optJSONObject(0).optString("twelve"),};

                                    Intent intent = new Intent(NewMiningActivity.this, VipUpgradeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("textStrings", textStrings);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });

                        }


                    }

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                    }
                });
            }
        });

        linearLayout_show_share_win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMiningActivity.this, SignedActivity.class);
                startActivity(intent);
            }
        });


        sharedpreferences = getSharedPreferences("isShowWelcomeLayout", Context.MODE_PRIVATE);

        ViewTreeObserver vto = findViewById(R.id.ll_mining).getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if (x == 1 && sharedpreferences.getBoolean("isShow1", true) == true) {
//                    showNextKnownTipView();
                    x = 2;
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        miningActiveIsStart();
        surplus_onekey_time();
        if (null != isStart && isStart.equals("活动中")) {
            taskIsComplete();
        }
        refresh();
        loadMemberPrivateMiningPool();
        loadMemberActiveCoefficient();
        loadAccountInfo();
        onRank();
//        isShowOneKeyShowMining();

        final RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(1000);
        rotateAnimation.setRepeatCount(2);
        iv_question_mark.setAnimation(rotateAnimation);

        Animation anim = AnimationUtils.loadAnimation(NewMiningActivity.this, R.anim.mining_activeicon_anim);
        if (taskIconLoadSuccess) {
            iv_mining_active.startAnimation(anim);
        }

        Animation anim2 = AnimationUtils.loadAnimation(NewMiningActivity.this, R.anim.mining_activeicon_anim);
        iv_shuoming.setAnimation(anim2);

        HttpConnect.post(NewMiningActivity.this, "member_hide_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String assets = data.getJSONArray("data").getJSONObject(0).getString("assets");
                    final String upgrade = data.getJSONArray("data").getJSONObject(0).getString("upgrade");

                    NewMiningActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(upgrade)) {
                                if (upgrade.equals("0")) {
                                    learLayout_into_vip.setVisibility(View.VISIBLE);
                                    /**
                                     *
                                     * 会员到期提醒
                                     */
//                                    if (sharedpreferences.getString("is1","0").equals("1")){
                                    //记录当天是否提示过，提示过则今天不在提示，明天在记录新的日期
                                    final String date = Tools.stampToDate(System.currentTimeMillis() / 1000 + "").split(" ")[0];
                                    if (!sharedpreferences.getString("dateVip", "2017-01-01").equals(date)) {
                                        HttpConnect.post(NewMiningActivity.this, "member_upgrade_duedate_message", null, new Callback() {
                                            @Override
                                            public void onResponse(Response arg0) throws IOException {

                                                String str = arg0.body().string();
                                                JSONObject data = JSONObject.fromObject(str);
                                                if (data.getString("status").equals("success")) {
                                                    String value = data.optJSONArray("data").optJSONObject(0).optString("value");
                                                    if (!value.equals("0")) {
                                                        showVipMsg(value);
                                                        sharedpreferences.edit().putString("dateVip", date).commit();
                                                    }
                                                } else {
                                                }

                                            }

                                            @Override
                                            public void onFailure(Request arg0, IOException arg1) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                    }
                                                });
                                            }
                                        });

                                    }

//                                    }
                                } else {
                                    learLayout_into_share.setGravity(Gravity.CENTER);
                                }
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });

    }


    /**
     * 需要刷新的地方
     */
    public void refresh() {
        /**
         * 设置头像  以及昵称
         **/
        final String headUrl = LoginUser.getInstantiation(NewMiningActivity.this.getApplicationContext()).getLoginUser().getHeadUrl();
        if (headUrl != null && !headUrl.equals("")) {
            Glide.with(NewMiningActivity.this).load(headUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_mining_member_head_img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(NewMiningActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_mining_member_head_img.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            iv_mining_member_head_img.setImageResource(R.mipmap.iv_member_default_head_img);
        }
    }

    /**
     * 获取会员私有矿池的信息
     */
    public void loadMemberPrivateMiningPool() {
        HttpConnect.post(NewMiningActivity.this, "member_personal_pool_pravite", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String value = data.getJSONArray("data").getJSONObject(0).optString("value");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            /**
                             * 设置头像  以及昵称
                             */
                            if (!TextUtils.isEmpty(value) && !value.equals("")) {
                                tv_mining_pool.setText(" : " + removeDecimalPoint(value));
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

//                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 获取会员分享 活跃 vip等系数
     */
    public void loadMemberActiveCoefficient() {
        HttpConnect.post(NewMiningActivity.this, "member_mine_factor", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String share = data.getJSONArray("data").getJSONObject(0).optString("share");
                    final String active = data.getJSONArray("data").getJSONObject(0).optString("active");
                    final String energy = data.getJSONArray("data").getJSONObject(0).optString("energy");
                    final String per = data.getJSONArray("data").getJSONObject(0).optString("per");
                    final String grade = data.getJSONArray("data").getJSONObject(0).optString("grade");
                    final String power = data.getJSONArray("data").getJSONObject(0).optString("power");
                    final String pervalue = data.getJSONArray("data").getJSONObject(0).optString("pervalue");

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

                            tv_share.playNumber(Double.parseDouble(share), 0);
                            tv_active.playNumber(Double.parseDouble(active), 0);
                            tv_vip.playNumber(Double.parseDouble(energy), 0);
                            tv_mining_level.setText(grade);
                            PERCENT_TOTAL = (int) (Float.parseFloat(per) * 100);
                            percentFluency = 0;
                            if (Double.parseDouble(per) * 100 > 0 && Double.parseDouble(per) * 100 < 1) {
                                progressBar_mining_comprehensive_value.setProgress(1);
                            } else {
                                if (PERCENT_TOTAL == 0) {
                                    progressBar_mining_comprehensive_value.setProgress(PERCENT_TOTAL);
                                } else {
                                    mHandler.post(mUpdateProgressFluency);
                                }
                            }
                            tv_mining_comprehensive_value.setText(removeDecimalPoint(Double.parseDouble(pervalue) * 100 + "") + "%");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

//                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

//                        sToast("链接超时");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public void loadAccountInfo() {
        HttpConnect.post(this, "member_count_detial", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String available = data.getJSONArray("data").getJSONObject(0).getString("ctc");//收益
                    final String ctccous = data.getJSONArray("data").getJSONObject(0).getString("ctccous");  //消费账户
                    final String ctcoption = data.getJSONArray("data").getJSONObject(0).getString("ctcoption");  //决战账户
                    strengthvalue = data.getJSONArray("data").getJSONObject(0).getString("strengthvalue");  //决战账户

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            tv_shoyi_account.playNumber(Double.parseDouble(available), Double.parseDouble(tv_shoyi_account.getText().toString()));
                            tv_xiaofei_account.playNumber(Double.parseDouble(ctccous), Double.parseDouble(tv_xiaofei_account.getText().toString()));
                            tv_juezhan_account.playNumber(Double.parseDouble(ctcoption), Double.parseDouble(tv_juezhan_account.getText().toString()));
                            if (!TextUtils.isEmpty(strengthvalue)) {
                                if (Integer.parseInt(strengthvalue) <= 0) {
                                    iv_mining_strength.setText("【" + 0 + "】");
                                } else {
                                    iv_mining_strength.setText("【" + strengthvalue + "】");
                                }
                            } else {
                                iv_mining_strength.setText("【" + 0 + "】");
                            }
                            if (!TextUtils.isEmpty(strengthvalue) && !strengthvalue.equals("") && !strengthvalue.equals("0")) {
                                tili = Integer.parseInt(strengthvalue);
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

//                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

//                        sToast("");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 当日排行
     */
    public void onRank() {
        final List<TodayMiningRankingBean> datas = new ArrayList<TodayMiningRankingBean>();
        adapter = new TodayMiningRankingAdapter(datas, NewMiningActivity.this);
        listview_mining.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listview_mining);
        try {
            HttpConnect.post(this, "member_minning_today", null, new Callback() {

                @Override
                public void onResponse(Response arg0)
                        throws IOException {

                    if (arg0 != null || !arg0.equals("")) {

                        final JSONObject object = JSONObject.fromObject(arg0
                                .body().string());
                        if (object.get("status").equals("success")) {

                            String msg = object.getString("msg");

                            JSONArray array = object
                                    .optJSONArray("data");
                            if (array.size() != 0) {
                                if (array.size() >= 15) {
                                    for (int i = 0; i < 15; i++) {
                                        JSONObject object2 = array
                                                .getJSONObject(i);
                                        TodayMiningRankingBean rankingData = new TodayMiningRankingBean(object2
                                                .getString("pic"), object2
                                                .getString("name"), object2
                                                .getString("bonusrebatetotal"));
                                        datas.add(rankingData);
                                    }
                                } else {
                                    for (int i = 0; i < array.size(); i++) {
                                        JSONObject object2 = array
                                                .getJSONObject(i);
                                        TodayMiningRankingBean rankingData = new TodayMiningRankingBean(object2
                                                .getString("pic"), object2
                                                .getString("name"), object2
                                                .getString("bonusrebatetotal"));
                                        datas.add(rankingData);
                                    }
                                }

                                Handler dataHandler = new Handler(
                                        getContext().getMainLooper()) {

                                    @Override
                                    public void handleMessage(
                                            final Message msg) {

                                        adapter.setDatas(datas);
                                        adapter.notifyDataSetChanged();
                                        setListViewHeightBasedOnChildren(listview_mining);

                                        linearLayout_accunt.setFocusable(true);
                                        linearLayout_accunt.setFocusableInTouchMode(true);
                                        linearLayout_accunt.requestFocus();
                                    }
                                };
                                dataHandler.sendEmptyMessage(0);
                            }
                        } else {
                            Handler dataHandler = new Handler(
                                    getContext().getMainLooper()) {
                                @Override
                                public void handleMessage(
                                        final Message msg) {

                                }
                            };
                            dataHandler.sendEmptyMessage(0);
                        }
                    }

                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    Handler dataHandler = new Handler(
                            getContext().getMainLooper()) {

                        @Override
                        public void handleMessage(
                                final Message msg) {
//									sToast("链接超时！");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            });
        } catch (Exception e) {

        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight = totalHeight + listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    String shareType = "";

    /**
     * 显示分享的提示框
     */
    public void show() {

        ImageView iv_share_wx = (ImageView) view1.findViewById(R.id.iv_share_wx);
        ImageView iv_cancel_authentication = (ImageView) view1.findViewById(R.id.iv_cancel_authentication);
        ImageView iv_share_pengyouquan = (ImageView) view1.findViewById(R.id.iv_share_pengyouquan);
        ImageView iv_share_qq = (ImageView) view1.findViewById(R.id.iv_share_qq);
        ImageView iv_share_qzone = (ImageView) view1.findViewById(R.id.iv_share_qzone);


        iv_cancel_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        //分享到微信
        iv_share_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "微信";
                loadPic();
                window.dismiss();
            }
        });

        //qq
        iv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "qq";
                loadPic();
                window.dismiss();
            }
        });

        //qzone
        iv_share_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "qzone";
                loadPic();
                window.dismiss();
            }
        });

        //分享到微信
        iv_share_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareType = "朋友圈";
                loadPic();
                window.dismiss();
            }
        });

        if (!window.isShowing()) {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp5 = MainActivity.mainActivity.getWindow().getAttributes();
            lp5.alpha = 0.5f;
            MainActivity.mainActivity.getWindow().setAttributes(lp5);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp3 = MainActivity.mainActivity.getWindow().getAttributes();
                    lp3.alpha = 1f;
                    MainActivity.mainActivity.getWindow().setAttributes(lp3);
                }
            });

            window.setOutsideTouchable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            window.setBackgroundDrawable(dw);

            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.Mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(tv_shoyi_account,
                    Gravity.BOTTOM, 0, 0);
        }
    }


    private IUiListener listener = new IUiListener() {

        @Override
        public void onCancel() {
            sToast("取消分享");
        }

        @Override
        public void onComplete(Object arg0) {
            sToast("QQ分享成功");
        }

        @Override
        public void onError(UiError arg0) {
//            sToast("QQ分享失败"+arg0.toString());
        }
    };

    /**
     * 调用接口合成图片 并且获取其url 缓存到本地
     */
    public void loadPic() {
        //TODO
        myProgressDialog.setTitle("正在加载");
        myProgressDialog.show();
        HttpConnect.post(NewMiningActivity.this, "member_share_pic_url", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String sharePicUrl = data.optJSONArray("data").optJSONObject(0).optString("url");

                    saveFile(sharePicUrl, SHARE_PIC);

                    Bitmap bitmap = convertToBitmap(ALBUM_PATH + SHARE_PIC);
                    if (bitmap != null) {
                        if (shareType.equals("微信") || shareType.equals("朋友圈")) {
                            bitmap = zoomImage(bitmap, 380, 675.5);
                        }

                        final Bitmap finalBitmap = bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myProgressDialog.cancel();
                                if (shareType.equals("微信")) {
                                    api = WXAPIFactory.createWXAPI(NewMiningActivity.this, WX_APP_ID, true);
                                    api.registerApp(WX_APP_ID);
                                    if (api.isWXAppInstalled()) {
                                        WXImageObject imageObject = new WXImageObject(finalBitmap);
                                        final WXMediaMessage msg = new WXMediaMessage();

                                        msg.mediaObject = imageObject;
                                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bStream);
                                        msg.thumbData = bStream.toByteArray();

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = "image";
                                        req.message = msg;
                                        req.scene = SendMessageToWX.Req.WXSceneSession;
                                        api.sendReq(req);
                                    } else {
                                        sToast("请安装微信客户端");
                                    }
                                } else if (shareType.equals("朋友圈")) {
                                    api = WXAPIFactory.createWXAPI(NewMiningActivity.this, WX_APP_ID, true);
                                    api.registerApp(WX_APP_ID);
                                    if (api.isWXAppInstalled()) {
                                        WXImageObject imageObject = new WXImageObject(finalBitmap);
                                        final WXMediaMessage msg = new WXMediaMessage();

                                        msg.mediaObject = imageObject;

                                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bStream);
                                        msg.thumbData = bStream.toByteArray();

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = "image";
                                        req.message = msg;
                                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                        api.sendReq(req);
                                    } else {
                                        sToast("请安装微信客户端");
                                    }
                                } else if (shareType.equals("qq")) {
                                    mTencent = Tencent.createInstance(LoginMainActivity.QQ_APP_ID, NewMiningActivity.this.getApplicationContext());
//                                    if (!mTencent.isSessionValid()) {
//                                        mTencent.login(NewMiningActivity.this, "get_user_info", listener);
//                                    }
                                    Bundle params = new Bundle();
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ALBUM_PATH + SHARE_PIC);
                                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                                    mTencent.shareToQQ(NewMiningActivity.this, params, listener);
                                } else if (shareType.equals("qzone")) {
                                    mTencent = Tencent.createInstance(LoginMainActivity.QQ_APP_ID, NewMiningActivity.this.getApplicationContext());
//                                    if (!mTencent.isSessionValid()) {
//                                        mTencent.login(NewMiningActivity.this, "get_user_info", listener);
//                                    }
                                    Bundle params = new Bundle();
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ALBUM_PATH + SHARE_PIC);
                                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "分乐宝");
                                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                                    mTencent.shareToQQ(NewMiningActivity.this, params, listener);
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                           sToast("请检查您的网络连接状态");
                    }
                });
            }
        });
    }


    /**
     * 百分比可能会有小数 此方法去除小数
     *
     * @param str
     * @return
     */
    public String removeDecimalPoint(String str) {
        if (str.contains(".")) {
            int count = str.indexOf(".");
            return str.substring(0, count);
        }
        return str;
    }


    /**
     * 从网络获取图片
     *
     * @param path 加载图片的url
     */
    protected InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 将输入流转换为bitmap 保存到内存中
     */
    protected void saveFile(String url, String fileName) {
        // 取得的是inputstream，直接从inputstream生成bitmap
        try {
            InputStream inputStream = getImageStream(url);
            if (inputStream != null) {
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                saveFile(mBitmap, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * 保存文件
    */
    protected void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        bos.flush();
        bos.close();
    }


    /**
     * 读取sd卡下图片，由图片路劲转换为bitmap
     *
     * @param path 图片路径
     * @return 返回值为bitmap
     */
    public static Bitmap convertToBitmap(String path) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        // Calculate inSampleSize
//        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(path);
    }

    /***
     * 图片压缩方法二
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    private void floatAnim(View view, int delay) {
        List<Animator> animators = new ArrayList<Animator>();
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", -3.0f, 3.0f, -3.0f);
        translationYAnim.setDuration(1000);
        translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
//        translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
        translationYAnim.start();
        animators.add(translationYAnim);

        btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(delay);
        btnSexAnimatorSet.start();
    }

    @Override
    public void addView(View view) {
        if (relativeLayout_mines != null) {
            relativeLayout_mines.addView(view);
        }
    }

    @Override
    public int getQiuCount() {
        return relativeLayout_mines.getChildCount();
    }

    @Override
    public void removeView(View view) {
        relativeLayout_mines.removeView(view);
    }

    @Override
    public void setXiaoFei(Double xiaofei) {
        tv_xiaofei_account.playNumber(Double.parseDouble(tv_xiaofei_account.getText().toString()) + xiaofei, Double.parseDouble(tv_xiaofei_account.getText().toString()));
    }

    @Override
    public void setShouYi(Double shouYi) {
        tv_shoyi_account.playNumber(Double.parseDouble(tv_shoyi_account.getText().toString()) + shouYi, Double.parseDouble(tv_shoyi_account.getText().toString()));
    }

    @Override
    public void setJueZhan(Double jueZhan) {
        tv_juezhan_account.playNumber(Double.parseDouble(tv_juezhan_account.getText().toString()) + jueZhan, Double.parseDouble(tv_juezhan_account.getText().toString()));
    }

    @Override
    public void setFuTou(Double fuTou) {
//        tv_futou_account.playNumber(Double.parseDouble(tv_futou_account.getText().toString())+fuTou,Double.parseDouble(tv_futou_account.getText().toString()));
    }

    @Override
    public void showHand() {
        iv_start_mining_prompt_hand.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHand() {
        iv_start_mining_prompt_hand.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setStartEnable(boolean isFlag) {
        if (isFlag == true) {
            iv_start_mining.setClickable(true);
        } else {
            iv_start_mining.setClickable(false);
        }
    }

    @Override
    public void jian() {
        tili = tili - 1;
        iv_mining_strength.setText("【" + tili + "】");
    }

    @Override
    public void startAnim() {
//        floatAnim(iv_start_mining,0);
    }

    @Override
    public void stopAnim() {
//        btnSexAnimatorSet.end();
    }

    @Override
    public void showShareWin() {
        show();
    }

    @Override
    public void showCollectCount(int count) {
        tv_mining_count.setText(" x " + count);
    }

    @Override
    public void setChengShuMiningCount(int count) {
        this.chengShuCount = count;
    }

    @Override
    public void showThisEndTime(String time) {
        if (endTime == 0) {
            if (!TextUtils.isEmpty(time)) {
                endTime = Integer.parseInt(time);
            }

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (endTime > 0) {
                                endTime = endTime - 1;
                                tv_this_end_time.setVisibility(View.VISIBLE);
                                tv_this_end_time.setText("倒计时：" + DateUtil.formatDate(new java.sql.Date(endTime * 1000), "HH:mm:ss"));
                            } else {
                                tv_this_end_time.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            };

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        }
    }

    @Override
    public LinearLayout getOneKeyShouMiningLinearLayout() {
        return linearLayout_onekey_shou_mining;
    }

    @Override
    public void showLoadGif() {
        gifImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadGif() {
        gifImageView.setVisibility(View.GONE);
    }

    @Override
    public int getViewWidth() {
        return qiuViewWidth;
    }

    @Override
    public ImageView getStartMiningImg() {
        return iv_start_mining;
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        if (hasFocus)
//        {
//            showNextKnownTipView();
//        }
//    }

    public void showNextKnownTipView() {
//            MainActivity.view.setVisibility(View.GONE);
//            MainActivity.tabWidget.setVisibility(View.GONE);
        mHightLight = new HighLight(this)//
                .anchor(findViewById(R.id.ll_mining))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.iv_start_mining, R.layout.info_known_person, new OnBottomPosCallback(10), new CircleLightShape())
                .addHighLight(R.id.iv_into_luckpan, R.layout.info_known_info, new OnLeftPosCallback(10), new CircleLightShape())
                .autoRemove(false)
                .enableNext()
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
                    @Override
                    public void onRemove() {
//                            MainActivity.view.setVisibility(View.VISIBLE);
//                            MainActivity.tabWidget.setVisibility(View.VISIBLE);
                        sharedpreferences.edit().putBoolean("isShow1", false).commit();
                        sharedpreferences.edit().putString("is1", "1").commit();
                    }
                })
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        mHightLight.next();
                    }
                });
        mHightLight.show();


    }


//    /**
//     * 双击退出程序
//     */
//    private long mExitTime;
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                sToast("再按一次退出程序");
//                mExitTime = System.currentTimeMillis();
//
//            } else {
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(startMain);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//      Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, listener);
            }
        }
    }


    private void miningNotice() {
        notices.clear();
        HttpConnect.post(this, "member_minnig_notice", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {


                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            JSONArray dataArr = data.optJSONArray("data");
                            if (dataArr != null) {
                                for (int i = 0; i < dataArr.size(); i++) {
                                    JSONObject jsonObj = dataArr.optJSONObject(i);
                                    if (jsonObj != null) {
                                        String pic = jsonObj.optString("Pic");
                                        String contents = jsonObj.optString("Contents");
                                        MiningNotice miningNotice = new MiningNotice(pic, contents);
                                        notices.add(miningNotice);
                                    }
                                }
                                if (notices.size() != 0) {
                                    int time = notices.size() * (SHOWTIME + SLEEPTIME);
                                    if (time >= 60) {
                                        requestTime = time;
                                    } else {
                                        requestTime = 60;
                                    }
                                    noticeHandler.post(noticeThread);
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    final String msg8 = data.getString("msg");
                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {

//                            sToast(msg8);
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext()
                        .getMainLooper()) {

                    @Override
                    public void handleMessage(final Message msg) {

//                        sToast("");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 获取用户状态
     */
    private void getMemberState() {
        HttpConnect.post(NewMiningActivity.this, "member_status_exit", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    JSONArray dataArr = data.getJSONArray("data");
                    if (dataArr != null) {
                        JSONObject jsonObj = dataArr.optJSONObject(0);
                        if (jsonObj != null) {
                            final String value = jsonObj.optString("value");
                            if (value != null) {
                                if (value.equals("3")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent5 = new Intent(NewMiningActivity.this, LoginMainActivity.class);
                                            startActivity(intent5);
                                            finish();
                                            LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }


    /**
     * 查看当前app是否处于维护状态
     */
    private void isMaintain() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                final String ipLine = IPUtil.getNetIp();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyNetworkConnection.getNetworkConnection(NewMiningActivity.this).post("post", "http://www.blacoin.cc/app/fenlebao.ashx", null, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        sToast("请检查您的网络链接状态");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String json = response.body().string().trim();
                                final net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(json);
                                final net.sf.json.JSONObject jsonObject = jsonArray.optJSONObject(0);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String enable = jsonObject.optString("enable");
                                        String ip = jsonObject.optString("ip");
                                        if (enable != null && !TextUtils.isEmpty(enable)) {
                                            if (enable.equals("false")) {
                                                //正常状态

                                            } else {
                                                if (!TextUtils.isEmpty(ip) && ip.equals(ipLine)) {
                                                    //正常状态
                                                } else {
                                                    //非正常状态
                                                    Intent intent5 = new Intent(NewMiningActivity.this, LoginMainActivity.class);
                                                    startActivity(intent5);
                                                    finish();
                                                    LoginUser.getInstantiation(getApplicationContext()).loginOut();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }


    int startActiveSecond = 0;

    private void miningActiveIsStart() {
        HttpConnect.post(NewMiningActivity.this, "member_activity_min_options_listtwo", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String startDate = obj.optString("startdate");
                    final String endDate = obj.optString("enddate");
//                    final String dao = obj.optString("dao");

                    if (null != startDate && null != endDate) {
                        if (null != startActiveTimer) {
                            startActiveTimer.cancel();
                        }
                        if (null != startActivetimerTask) {
                            startActivetimerTask.cancel();
                        }
                        startActiveTimer = new Timer();
                        startActivetimerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Date date = new Date();
                                SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于
                                String currentTime = myFmt2.format(date);
                                isStart = DateDistance.getDistanceTime2(currentTime, startDate, endDate);
                                if (null != isStart) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isStart.equals("未开始")) {
                                                tv_surplus_task_time.setVisibility(View.GONE);
                                                iv_mining_active.setVisibility(View.GONE);
                                                tv_message_count.setVisibility(View.GONE);
                                            } else if (isStart.equals("活动中")) {
                                                iv_mining_active.setVisibility(View.VISIBLE);
                                                tv_surplus_task_time.setVisibility(View.VISIBLE);
                                                startActiveSecond = startActiveSecond + 1;
                                                if (startActiveSecond == 1) {
                                                    taskIsComplete();
                                                    getTaskIcon();
                                                }
                                                surplus_task_time();
                                            } else if (isStart.equals("已结束")) {
                                                tv_surplus_task_time.setVisibility(View.GONE);
                                                iv_mining_active.setVisibility(View.GONE);
                                                tv_message_count.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                            }
                        };
                        startActiveTimer.scheduleAtFixedRate(startActivetimerTask, 0, 1000);
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    /**
     * 剩余任务时间
     */
    public void surplus_task_time() {
        HttpConnect.post(NewMiningActivity.this, "member_activity_min_options_listtwo", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String dao = obj.optString("dao");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != dao && !TextUtils.isEmpty(dao)) {
                                boolean isAllNum = isNumericFirst(dao);
                                if (isAllNum) {
                                    surplus_task_time = Integer.parseInt(dao);
                                    if (null != taskSurplusTimeTimer) {
                                        taskSurplusTimeTimer.cancel();
                                    }

                                    if (null != taskSurplusTimeTimerTask) {
                                        taskSurplusTimeTimerTask.cancel();
                                    }

                                    taskSurplusTimeTimerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (surplus_task_time > 0) {
                                                        surplus_task_time = surplus_task_time - 1;
                                                        tv_surplus_task_time.setVisibility(View.VISIBLE);
                                                        tv_surplus_task_time.setText(DateUtil.formatDate(new java.sql.Date(surplus_task_time * 1000), "HH:mm:ss"));
                                                    } else {
                                                        tv_surplus_task_time.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            });
                                        }
                                    };

                                    taskSurplusTimeTimer = new Timer();
                                    taskSurplusTimeTimer.scheduleAtFixedRate(taskSurplusTimeTimerTask, 1000, 1000);
                                } else {
                                    tv_surplus_task_time.setText(dao);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    /**
     * 剩余一键挖矿时间
     */
    public void surplus_onekey_time() {
        HttpConnect.post(NewMiningActivity.this, "member_minning_duedate", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String dao = obj.optString("dao");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != dao && !TextUtils.isEmpty(dao)) {
                                boolean isAllNum = isNumericFirst(dao);
                                if (isAllNum) {
                                    surplus_onekey_time = Integer.parseInt(dao);
                                    if (null != onekeySurplusTimeTimer) {
                                        onekeySurplusTimeTimer.cancel();
                                    }

                                    if (null != onekeySurplusTimeTimerTask) {
                                        onekeySurplusTimeTimerTask.cancel();
                                    }

                                    onekeySurplusTimeTimerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (surplus_onekey_time > 0) {
                                                        surplus_onekey_time = surplus_onekey_time - 1;
                                                        if (surplus_onekey_time <= 0) {
                                                            linearLayout_onekey_shou_mining.setVisibility(View.GONE);
                                                        } else {
                                                            linearLayout_onekey_shou_mining.setVisibility(View.VISIBLE);
                                                            tv_onekey_time.setText(DateUtil.formatDate(new java.sql.Date(surplus_onekey_time * 1000), "HH:mm:ss"));
                                                        }
                                                    } else {
                                                        linearLayout_onekey_shou_mining.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                    };
                                    onekeySurplusTimeTimer = new Timer();
                                    onekeySurplusTimeTimer.scheduleAtFixedRate(onekeySurplusTimeTimerTask, 1000, 1000);
                                } else {
                                    if (dao.contains("-")) {
                                        linearLayout_onekey_shou_mining.setVisibility(View.GONE);
                                    } else {
                                        linearLayout_onekey_shou_mining.setVisibility(View.VISIBLE);
                                        tv_onekey_time.setText(dao);
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    public static boolean isNumericFirst(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 查看任务是否完成
     */
    private void taskIsComplete() {
        HttpConnect.post(NewMiningActivity.this, "member_activity_is_perfect", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String value = obj.optString("value");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != value && value.equals("1"))  //有可以领取的任务
                            {
                                if (iv_mining_active.getVisibility() == View.VISIBLE) {
                                    tv_message_count.setVisibility(View.VISIBLE);
                                }
                            } else if (null != value && value.equals("0"))//没有可以领取的任务
                            {
                                tv_message_count.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }


    /**
     * 获取任务图标
     */
    private void getTaskIcon() {
        HttpConnect.post(NewMiningActivity.this, "member_mining_task_pic", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String value = obj.optString("value");

                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void run() {
                            if (value != null && !TextUtils.isEmpty(value)) {
                                if (!isDestroyed()) {
                                    Glide.with(NewMiningActivity.this).load(value).animate(R.anim.mining_activeicon_anim).placeholder(null)
                                            .error(null).into(iv_mining_active);
                                    taskIconLoadSuccess = true;
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    /**
     * 是否显示一键收矿
     */
    private void isShowOneKeyShowMining() {
        HttpConnect.post(NewMiningActivity.this, "member_is_or_no_onekey_minning", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String value = obj.optString("value");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(value) && !value.equals("0")) {
                                linearLayout_onekey_shou_mining.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    private void showVipMsg(String msg) {
        Button btnCencel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnCommit = (Button) view.findViewById(R.id.btn_commit);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);

        btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window1.dismiss();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewMiningActivity.this, VipUpgradeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("textStrings", MeActivity.textStrings);
                intent.putExtras(bundle);
                startActivity(intent);
                window1.dismiss();
            }
        });

        noticeHandler.postDelayed(thread, 100);
    }

    public void back(View view) {
        finish();
    }

}
