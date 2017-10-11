package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.MiningNotice;
import com.tohier.cartercoin.bean.PrizeBean;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.LuckPanLayout;
import com.tohier.cartercoin.config.RotatePan;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.config.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewSpanLayoutActivity2 extends MyBaseActivity implements RotatePan.AnimationEndListener,View.OnClickListener {

    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;

    private String[] strs = {"妹子一只1", "妹子一只2", "妹子一只3", "妹子一只4", "妹子一只5", "妹子一只6", "妹子一只7", "妹子一只8", "妹子一只9", "妹子一只10", "妹子一只11", "妹子一只12", "妹子一只13", "妹子一只14", "妹子一只15", "妹子一只16", "妹子一只17", "妹子一只18"};

    public static LinearLayout linearLayout_select_shouyiaccount, linearLayout_select_xiaofeiaccount, linearLayout_select_juezhanaccount;
    private LinearLayout linearLayout_draw_fivecount, linearLayout_draw_tencount, linearLayout_draw_fifteencount, linearLayout_liankai_console;

    private ImageView iv_fivecount_icon, iv_tencount_icon, iv_fifteencount_icon;

    private TextView tv_one_showdrawcount, tv_two_showdrawcount, tv_three_showdrawcount;

    private TextView tv_xiaofei_account, tv_shoyi_account, tv_juezhan_account;


    private String type = "";

    /**
     * 显示消耗品
     */
    private TextView tv_consume_detail;

    private static String ONEDRAWCOUNT = "5";
    private static String TWODRAWCOUNT = "10";
    private static String THREEDRAWCOUNT = "15";

    private String strength = "";
    private String ctc = "";
    // 金豆
    private String jinDou = "";
    // 银豆
    private String yinDou = "";

    //抽奖次数
    private String selDrawCount = "1";

    //开始按钮
    private TextView mIvStart;

    /**
     * 进入中奖记录
     */
    private TextView tv_into_winningrecord;

    private ImageView iv_cancel_luckpan;

    //奖项的名称
    private String[] mPrizeName2 = null;
    private String[] mPrizeDesc = null;
    private String[] mPrizeIcon2 = null;
    private int[] mPrizeColor2 = null;
    private List<Bitmap> mImgIconBitmap = null;
    /**
     * 大转盘列表数据
     */
    private List<PrizeBean> datas = new ArrayList<PrizeBean>();

    private String currentColor = "";

    int i = 0;
    int count = 0;

    //指针停下来那一项
    private static int SELECTION = -1;

    public int requestCode = 0;

    //弹幕
    private LinearLayout linearLayout_barrage_prompt;
    private CircleImageView iv_barrage_headimg;
    private TextView tv_barrage_content;
    private ImageView iv_notice_start_bg, iv_notice_end_bg;

    private ArrayList<MiningNotice> notices = new ArrayList<MiningNotice>();

    public static int SLEEPTIME = 6;
    public static int SHOWTIME = 3;
    public int requestTime = 0;

    private ImageView iv_into_span_ranking, iv_into_span_active;
    private int sleepTime = 500;

    /**
     * 1.收益账户
     * 2.决战账户
     * 3.消费账户
     */
    private String mode = "2";

    private String activeUrl = "";
    private String names[] = null;

    private LoadingView load_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspanlayout);

        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);

        mIvStart = (TextView) findViewById(R.id.iv_start);

        tv_into_winningrecord = (TextView) this.findViewById(R.id.tv_into_winningrecord);

        linearLayout_select_shouyiaccount = (LinearLayout) findViewById(R.id.linearLayout_select_shouyiaccount);
        linearLayout_select_juezhanaccount = (LinearLayout) findViewById(R.id.linearLayout_select_juezhanaccount);
        linearLayout_select_xiaofeiaccount = (LinearLayout) findViewById(R.id.linearLayout_select_xiaofeiaccount);
        linearLayout_liankai_console = (LinearLayout) findViewById(R.id.linearLayout_liankai_console);

        linearLayout_draw_fivecount = (LinearLayout) findViewById(R.id.linearLayout_draw_fivecount);
        linearLayout_draw_tencount = (LinearLayout) findViewById(R.id.linearLayout_draw_tencount);
        linearLayout_draw_fifteencount = (LinearLayout) findViewById(R.id.linearLayout_draw_fifteencount);

        iv_fifteencount_icon = (ImageView) findViewById(R.id.iv_fifteencount_icon);
        iv_tencount_icon = (ImageView) findViewById(R.id.iv_tencount_icon);
        iv_fivecount_icon = (ImageView) findViewById(R.id.iv_fivecount_icon);
        iv_cancel_luckpan = (ImageView) findViewById(R.id.iv_cancel_luckpan);

        tv_one_showdrawcount = (TextView) findViewById(R.id.tv_one_showdrawcount);
        tv_two_showdrawcount = (TextView) findViewById(R.id.tv_two_showdrawcount);
        tv_three_showdrawcount = (TextView) findViewById(R.id.tv_three_showdrawcount);

        tv_xiaofei_account = (TextView) findViewById(R.id.tv_xiaofei_account);
        tv_shoyi_account = (TextView) findViewById(R.id.tv_shouyi_account);
        tv_juezhan_account = (TextView) findViewById(R.id.tv_juezhan_account);
        tv_consume_detail = (TextView) findViewById(R.id.tv_consume_detail);

        //弹幕
        linearLayout_barrage_prompt = (LinearLayout) findViewById(R.id.linearLayout_barrage_prompt);
        iv_barrage_headimg = (CircleImageView) findViewById(R.id.iv_barrage_headimg);
        tv_barrage_content = (TextView) findViewById(R.id.tv_barrage_content);
        iv_notice_start_bg = (ImageView) findViewById(R.id.iv_notice_start_bg);
        iv_notice_end_bg = (ImageView) findViewById(R.id.iv_notice_end_bg);

        iv_into_span_ranking = (ImageView) this.findViewById(R.id.iv_into_span_ranking);
        iv_into_span_active = (ImageView) this.findViewById(R.id.iv_into_span_active);

        load_view = (LoadingView) findViewById(R.id.load_view);

        Animation anim = AnimationUtils.loadAnimation(NewSpanLayoutActivity2.this, R.anim.mining_activeicon_anim);
        iv_into_span_ranking.startAnimation(anim);
        iv_into_span_active.startAnimation(anim);

        linearLayout_draw_fivecount.setOnClickListener(this);
        linearLayout_draw_tencount.setOnClickListener(this);
        linearLayout_draw_fifteencount.setOnClickListener(this);

        linearLayout_select_juezhanaccount.setOnClickListener(this);
        linearLayout_select_xiaofeiaccount.setOnClickListener(this);
        linearLayout_select_shouyiaccount.setOnClickListener(this);

        tv_into_winningrecord.setOnClickListener(this);
        iv_cancel_luckpan.setOnClickListener(this);
        mIvStart.setOnClickListener(this);
        iv_into_span_ranking.setOnClickListener(this);
        iv_into_span_active.setOnClickListener(this);

        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
            if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                //默认是男生的
                if (sex.equals("美女")) {
                    mIvStart.setBackgroundResource(R.drawable.luckpan_gril_btn_bg_shape_e90dc7);
                    linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                    iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    goBtn.setImageResource(R.mipmap.point);
                } else if (sex.equals("帅哥")) {
                    goBtn.setImageResource(R.mipmap.point);
                } else {
                    mIvStart.setBackgroundResource(R.drawable.luckpan_secrecy_btn_bg_shape_d45800);
                    linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                    iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    goBtn.setImageResource(R.mipmap.iv_secrecy_point);
                }
            }
        }

        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;

                int MinValue = Math.min(width, height);
                MinValue -= Util.dip2px(NewSpanLayoutActivity2.this, 10) * 2;
                backHeight = MinValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;

                luckPanLayout.setLayoutParams(lp);

                MinValue -= Util.dip2px(NewSpanLayoutActivity2.this, 28) * 2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = MinValue;
                lp.width = MinValue;

                rotatePan.setLayoutParams(lp);

                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2 + 45);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();
            }
        });
        load_view.setVisibility(View.VISIBLE);
        loadLuckListData(mode);
        miningNotice();
        drawCountConsole();
//        isShowContinuityOpen();1`
        isExitSpanActive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountInfo();
        surplus_active_time();
    }

    @Override
    public void endAnimation(int position) {
        luckPanLayout.setDelayTime(500);
        if (getType.equals("one")) {
            startActivityForResult(new Intent(NewSpanLayoutActivity2.this, PrizeActivity.class).putExtra("prizeName", mPrizeName2[SELECTION]).putExtra("prizeUrl", mPrizeIcon2[SELECTION]), requestCode);
        } else {
            startActivityForResult(new Intent(NewSpanLayoutActivity2.this, PrizeListActivity.class).putExtra("names", names), requestCode);
        }
    }

    boolean isSelOne = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout_draw_fivecount:
                if (mIvStart.isClickable()) {
                    if (!TextUtils.isEmpty(ONEDRAWCOUNT)) {
                        if (ONEDRAWCOUNT.equals("1")) {
                            if (isSelOne) {
                                if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                        //默认是男生的
                                        if (sex.equals("美女")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                        } else if (sex.equals("帅哥")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                        } else {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                        }
                                        isSelOne = false;
                                    }
                                }
                                /**
                                 * 1.收益账户
                                 * 2.决战账户
                                 * 3.消费账户
                                 */
                                if (mode.equals("1")) {
                                    tv_consume_detail.setText("消耗: " + ctc + "α + " + strength + "体力");
                                } else if (mode.equals("2")) {
                                    tv_consume_detail.setText("消耗: " + yinDou + "个 + " + strength + "体力");
                                } else if (mode.equals("3")) {
                                    tv_consume_detail.setText("消耗: " + jinDou + "个 + " + strength + "体力");
                                }
                                selDrawCount = "1";
                            } else {
                                selDrawCount = ONEDRAWCOUNT;
                                int sel = Integer.parseInt(selDrawCount);
                                int mCtc = Integer.parseInt(ctc);
                                int mJinDou = Integer.parseInt(jinDou);
                                int mYinDou = Integer.parseInt(yinDou);
                                int mStrength = Integer.parseInt(strength);
                                /**
                                 * 1.收益账户
                                 * 2.决战账户
                                 * 3.消费账户
                                 */
                                if (mode.equals("1")) {
                                    tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                                } else if (mode.equals("2")) {
                                    tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                                } else if (mode.equals("3")) {
                                    tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                                }
                                if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                        //默认是男生的
                                        if (sex.equals("美女")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                        } else if (sex.equals("帅哥")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                        } else {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                        }
                                        isSelOne = true;
                                    }
                                }
                            }
                        } else {
                            if (!TextUtils.isEmpty(selDrawCount) && selDrawCount.equals(ONEDRAWCOUNT)) {
                                if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                        //默认是男生的
                                        if (sex.equals("美女")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                        } else if (sex.equals("帅哥")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                        } else {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                        }
                                        isSelOne = false;
                                    }
                                }
                                /**
                                 * 1.收益账户
                                 * 2.决战账户
                                 * 3.消费账户
                                 */
                                if (mode.equals("1")) {
                                    tv_consume_detail.setText("消耗: " + ctc + "α + " + strength + "体力");
                                } else if (mode.equals("2")) {
                                    tv_consume_detail.setText("消耗: " + yinDou + "个 + " + strength + "体力");
                                } else if (mode.equals("3")) {
                                    tv_consume_detail.setText("消耗: " + jinDou + "个 + " + strength + "体力");
                                }
                                selDrawCount = "1";
                            } else {
                                selDrawCount = ONEDRAWCOUNT;
                                int sel = Integer.parseInt(selDrawCount);
                                double mCtc = Double.parseDouble(ctc);
                                double mJinDou = Double.parseDouble(jinDou);
                                double mYinDou = Double.parseDouble(yinDou);
                                double mStrength = Double.parseDouble(strength);
                                /**
                                 * 1.收益账户
                                 * 2.决战账户
                                 * 3.消费账户
                                 */
                                if (mode.equals("1")) {
                                    tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                                } else if (mode.equals("2")) {
                                    tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                                } else if (mode.equals("3")) {
                                    tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                                }
                                if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                        //默认是男生的
                                        if (sex.equals("美女")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                        } else if (sex.equals("帅哥")) {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                        } else {
                                            iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                            iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                            iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                        }
                                        isSelOne = true;
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case R.id.linearLayout_draw_tencount:
                if (mIvStart.isClickable()) {
                    isSelOne = false;
                    if (!TextUtils.isEmpty(selDrawCount) && selDrawCount.equals(TWODRAWCOUNT)) {
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                            if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                //默认是男生的
                                if (sex.equals("美女")) {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                } else if (sex.equals("帅哥")) {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                } else {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                }
                            }
                        }
                        /**
                         * 1.收益账户
                         * 2.决战账户
                         * 3.消费账户
                         */
                        if (mode.equals("1")) {
                            tv_consume_detail.setText("消耗: " + ctc + "α + " + strength + "体力");
                        } else if (mode.equals("2")) {
                            tv_consume_detail.setText("消耗: " + yinDou + "个 + " + strength + "体力");
                        } else if (mode.equals("3")) {
                            tv_consume_detail.setText("消耗: " + jinDou + "个 + " + strength + "体力");
                        }
                        selDrawCount = "1";
                    } else {
                        selDrawCount = TWODRAWCOUNT;
                        int sel = Integer.parseInt(selDrawCount);
                        double mCtc = Double.parseDouble(ctc);
                        double mJinDou = Double.parseDouble(jinDou);
                        double mYinDou = Double.parseDouble(yinDou);
                        double mStrength = Double.parseDouble(strength);
                        /**
                         * 1.收益账户
                         * 2.决战账户
                         * 3.消费账户
                         */
                        if (mode.equals("1")) {
                            tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                        } else if (mode.equals("2")) {
                            tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                        } else if (mode.equals("3")) {
                            tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                        }

                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                            if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                //默认是男生的
                                if (sex.equals("美女")) {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                } else if (sex.equals("帅哥")) {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                } else {
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.linearLayout_draw_fifteencount:
                if (mIvStart.isClickable()) {
                    isSelOne = false;
                    if (!TextUtils.isEmpty(selDrawCount) && selDrawCount.equals(THREEDRAWCOUNT)) {
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                            if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                //默认是男生的
                                if (sex.equals("美女")) {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                } else if (sex.equals("帅哥")) {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                } else {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                }
                            }
                        }
                        /**
                         * 1.收益账户
                         * 2.决战账户
                         * 3.消费账户
                         */
                        if (mode.equals("1")) {
                            tv_consume_detail.setText("消耗: " + ctc + "α + " + strength + "体力");
                        } else if (mode.equals("2")) {
                            tv_consume_detail.setText("消耗: " + yinDou + "个 + " + strength + "体力");
                        } else if (mode.equals("3")) {
                            tv_consume_detail.setText("消耗: " + jinDou + "个 + " + strength + "体力");
                        }
                        selDrawCount = "1";
                    } else {
                        selDrawCount = THREEDRAWCOUNT;
                        int sel = Integer.parseInt(selDrawCount);
                        double mCtc = Double.parseDouble(ctc);
                        double mJinDou = Double.parseDouble(jinDou);
                        double mYinDou = Double.parseDouble(yinDou);
                        double mStrength = Double.parseDouble(strength);
                        /**
                         * 1.收益账户
                         * 2.决战账户
                         * 3.消费账户
                         */
                        if (mode.equals("1")) {
                            tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                        } else if (mode.equals("2")) {
                            tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                        } else if (mode.equals("3")) {
                            tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                        }
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                            if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                //默认是男生的
                                if (sex.equals("美女")) {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                } else if (sex.equals("帅哥")) {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                } else {
                                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                    iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                    iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.linearLayout_select_shouyiaccount:
                if (mIvStart.isClickable()) {
                    setClickFalse();
                    mode = "1";
                    int sel = Integer.parseInt(selDrawCount);
                    double mCtc = Double.parseDouble(ctc);
                    double mJinDou = Double.parseDouble(jinDou);
                    double mYinDou = Double.parseDouble(yinDou);
                    double mStrength = Double.parseDouble(strength);
                    /**
                     * 1.收益账户
                     * 2.决战账户
                     * 3.消费账户
                     */
                    if (mode.equals("1")) {
                        tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                    } else if (mode.equals("2")) {
                        tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                    } else if (mode.equals("3")) {
                        tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                    }
                    loadLuckListData(mode);
                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if (sex.equals("美女")) {
                                linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                            } else if (sex.equals("帅哥")) {
                                linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                            } else {
                                linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                            }
                        }
                    }
                    linearLayout_select_juezhanaccount.setBackgroundColor(0xffffffff);
                    linearLayout_select_xiaofeiaccount.setBackgroundColor(0xffffffff);
                }

                break;

            case R.id.linearLayout_select_juezhanaccount:
                if (mIvStart.isClickable()) {
                    setClickFalse();
                    mode = "2";
                    int sel = Integer.parseInt(selDrawCount);
                    double mCtc = Double.parseDouble(ctc);
                    double mJinDou = Double.parseDouble(jinDou);
                    double mYinDou = Double.parseDouble(yinDou);
                    double mStrength = Double.parseDouble(strength);
                    /**
                     * 1.收益账户
                     * 2.决战账户
                     * 3.消费账户
                     */
                    if (mode.equals("1")) {
                        tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                    } else if (mode.equals("2")) {
                        tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                    } else if (mode.equals("3")) {
                        tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                    }
                    loadLuckListData(mode);
                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if (sex.equals("美女")) {
                                linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                            } else if (sex.equals("帅哥")) {
                                linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                            } else {
                                linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                            }
                        }
                    }
                    linearLayout_select_shouyiaccount.setBackgroundColor(0xffffffff);
                    linearLayout_select_xiaofeiaccount.setBackgroundColor(0xffffffff);
                }

                break;

            case R.id.linearLayout_select_xiaofeiaccount:
                if (mIvStart.isClickable()) {
                    setClickFalse();
                    mode = "3";
                    int sel = Integer.parseInt(selDrawCount);
                    double mCtc = Double.parseDouble(ctc);
                    double mJinDou = Double.parseDouble(jinDou);
                    double mYinDou = Double.parseDouble(yinDou);
                    double mStrength = Double.parseDouble(strength);
                    /**
                     * 1.收益账户
                     * 2.决战账户
                     * 3.消费账户
                     */
                    if (mode.equals("1")) {
                        tv_consume_detail.setText("消耗: " + mCtc * sel + "α + " + mStrength * sel + "体力");
                    } else if (mode.equals("2")) {
                        tv_consume_detail.setText("消耗: " + mYinDou * sel + "个 + " + mStrength * sel + "体力");
                    } else if (mode.equals("3")) {
                        tv_consume_detail.setText("消耗: " + mJinDou * sel + "个 + " + mStrength * sel + "体力");
                    }
                    loadLuckListData(mode);
                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if (sex.equals("美女")) {
                                linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                            } else if (sex.equals("帅哥")) {
                                linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                            } else {
                                linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                            }
                        }
                    }
                    linearLayout_select_shouyiaccount.setBackgroundColor(0xffffffff);
                    linearLayout_select_juezhanaccount.setBackgroundColor(0xffffffff);
                }
                break;
            case R.id.tv_into_winningrecord:
                if (mIvStart.isClickable()) {
                    startActivity(new Intent(NewSpanLayoutActivity2.this, WinningRecordActivity.class));
                }
                break;
            case R.id.iv_cancel_luckpan:
                if (mIvStart.isClickable()) {
                    finish();
                }
                break;
            case R.id.iv_start:
                mIvStart.setClickable(false);
                clickToDraw();
                break;

            case R.id.iv_into_span_ranking:
                if (mIvStart.isClickable()) {
                    startActivity(new Intent(NewSpanLayoutActivity2.this, LuckPanRankingActivity.class));
                }
                break;

            case R.id.iv_into_span_active:
                if (mIvStart.isClickable()) {
                    startActivity(new Intent(NewSpanLayoutActivity2.this, LuckPanActiveShuoMingActivity.class).putExtra("activeUrl", activeUrl));
                }
                break;
        }
    }


    /**
     * one  一次
     * more 多次
     */
    private String getType = "";

    /**
     * 点击进行抽奖
     */
    public void clickToDraw() {
        mIvStart.setClickable(false);
        if (TextUtils.isEmpty(selDrawCount)) {
            mIvStart.setClickable(true);
            sToast("请选择抽奖次数");
        } else {
            if (selDrawCount.equals("1")) {
                getType = "one";
                drawOneCount(mode);
            } else if (selDrawCount.equals(ONEDRAWCOUNT)) {
                getType = "more";
                drawManyTimes(mode, ONEDRAWCOUNT);
            } else if (selDrawCount.equals(TWODRAWCOUNT)) {
                getType = "more";
                drawManyTimes(mode, TWODRAWCOUNT);
            } else if (selDrawCount.equals(THREEDRAWCOUNT)) {
                getType = "more";
                drawManyTimes(mode, THREEDRAWCOUNT);
            }
        }
    }

    private boolean matching = false;

    private void drawOneCount(String mode) {
        matching = false;
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("type", mode);
        par1.put("groupid", mode);
        par1.put("number", "1");
        HttpConnect.post(this, "member_roulette_v2", par1, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String value = data.getJSONArray("data").getJSONObject(0).optString("value");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            if (!TextUtils.isEmpty(value)) {
                                if (value.equals("余额不足")) {
                                    sToast(value);
                                    mIvStart.setClickable(true);
                                } else if (value.equals("体力不足")) {
                                    sToast(value);
                                    mIvStart.setClickable(true);
                                } else {
                                    for (int i = 0; i < datas.size(); i++) {
                                        if (value.equals(datas.get(i).getId())) {
                                            matching = true;
                                            SELECTION = i;
                                            //生成0到11之间的随机数
                                            //转盘是从-1开始 10结束 所以要减一
                                            //传入的参数由后台返回指定中哪个奖项
                                            rotatePan.startRotate(SELECTION);
                                            luckPanLayout.setDelayTime(100);
                                        }
                                    }
                                    if (!matching) {
                                        mIvStart.setClickable(true);
                                    }
                                }
                            }else
                            {
                                mIvStart.setClickable(true);
                                sToast("余额不足");
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            mIvStart.setClickable(true);
                            if (!TextUtils.isEmpty(data.optString("msg")) && !Tools.isPhonticName(data.getString("msg"))) {
                                sToast(data.optString("msg"));
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
                        mIvStart.setClickable(true);
                        sToast("请检查您的网络连接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }


    private boolean flag = false;

    /**
     * 抽奖多次
     */
    private void drawManyTimes(String mode, String count) {
        matching = false;
        flag = false;
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("type", mode);
        par1.put("groupid", mode);
        par1.put("number", count);
        HttpConnect.post(this, "member_roulette_v2", par1, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String value = data.getJSONArray("data").getJSONObject(0).optString("value");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            if (!TextUtils.isEmpty(value)) {
                                if (value.equals("余额不足")) {
                                    sToast(value);
                                    mIvStart.setClickable(true);
                                } else if (value.equals("体力不足")) {
                                    sToast(value);
                                    mIvStart.setClickable(true);
                                } else {
                                    String positions[] = value.split(",");
                                    names = new String[positions.length];
                                    for (int i = 0; i < datas.size(); i++) {
                                        String id = datas.get(i).getId();
                                        for (int p = 0; p < positions.length; p++) {
                                            if (positions[p].equals(id)) {
                                                matching = true;
                                                if (!flag) {
                                                    SELECTION = i;
                                                    //生成0到11之间的随机数
                                                    //转盘是从-1开始 10结束 所以要减一
                                                    //传入的参数由后台返回指定中哪个奖项
                                                    rotatePan.startRotate(SELECTION);
                                                    luckPanLayout.setDelayTime(100);
                                                    flag = true;
                                                }
                                                names[p] = datas.get(i).getName();
                                            }
                                        }
                                    }
                                    if (!matching) {
                                        mIvStart.setClickable(true);
                                    }
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            mIvStart.setClickable(true);
                            if (!TextUtils.isEmpty(data.optString("msg")) && !Tools.isPhonticName(data.getString("msg"))) {
                                sToast(data.optString("msg"));
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
                        mIvStart.setClickable(true);
                        sToast("请检查您的网络连接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    public void loadLuckListData(String value) {
        datas.clear();
        HashMap<String, String> par = new HashMap<>();
        par.put("group", value + "");
        par.put("type", value + "");
        HttpConnect.post(this, "member_roulette_list", par, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            JSONArray array = data
                                    .optJSONArray("data");
                            if (array.size() != 0) {
                                mPrizeName2 = new String[array.size()];
                                mPrizeDesc = new String[array.size()];
                                mPrizeIcon2 = new String[array.size()];
                                mPrizeColor2 = new int[array.size()];
                                mImgIconBitmap = new ArrayList<Bitmap>();
                                for (int i = 0; i < array.size(); i++) {
                                    final String id = array.getJSONObject(i).getString("id");
                                    String name = array.getJSONObject(i).getString("name");
                                    final String money = array.getJSONObject(i).getString("money");
                                    final String pic = array.getJSONObject(i).getString("pic");
                                    final String type = array.getJSONObject(i).getString("type");
                                    PrizeBean prizeBean = new PrizeBean(id, name, money, pic, type);
                                    datas.add(prizeBean);

                                    mPrizeIcon2[i] = pic;

                                    String desc = "";
                                    if (name.contains("(")) {
                                        desc = name.substring(name.indexOf("("), name.length());
                                        name = name.substring(0, name.indexOf("("));
                                    }
                                    mPrizeDesc[i] = desc;
                                    mPrizeName2[i] = name;

                                    if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser()) {
                                        if (null != LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex()) {
                                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                                            //默认是男生的
                                            if (sex.equals("美女")) {
                                                if (currentColor.equals("")) {
                                                    mPrizeColor2[i] = 0Xfff986c1;
                                                    currentColor = "0Xfff986c1";
                                                } else if (currentColor.equals("0XFFe90dc7")) {
                                                    mPrizeColor2[i] = 0Xfff986c1;
                                                    currentColor = "0Xfff986c1";
                                                } else if (currentColor.equals("0Xfff986c1")) {
                                                    mPrizeColor2[i] = 0XFFf668bd;
                                                    currentColor = "0XFFf668bd";
                                                } else if (currentColor.equals("0XFFf668bd")) {
                                                    mPrizeColor2[i] = 0XFFe90dc7;
                                                    currentColor = "0XFFe90dc7";
                                                }
                                            } else if (sex.equals("帅哥")) {
                                                if (currentColor.equals("")) {
                                                    mPrizeColor2[i] = 0XFFe4f5d5;
                                                    currentColor = "0XFFe4f5d5";
                                                } else if (currentColor.equals("0XFFa9e7e6")) {
                                                    mPrizeColor2[i] = 0XFFe4f5d5;
                                                    currentColor = "0XFFe4f5d5";
                                                } else if (currentColor.equals("0XFFe4f5d5")) {
                                                    mPrizeColor2[i] = 0XFF88b8b7;
                                                    currentColor = "0XFF88b8b7";
                                                } else if (currentColor.equals("0XFF88b8b7")) {
                                                    mPrizeColor2[i] = 0XFFa9e7e6;
                                                    currentColor = "0XFFa9e7e6";
                                                }
                                            } else {
                                                if (currentColor.equals("")) {
                                                    mPrizeColor2[i] = 0XFF6d428d;
                                                    currentColor = "0XFF6d428d";
                                                } else if (currentColor.equals("0XFFeb6100")) {
                                                    mPrizeColor2[i] = 0XFF6d428d;
                                                    currentColor = "0XFF6d428d";
                                                } else if (currentColor.equals("0XFF6d428d")) {
                                                    mPrizeColor2[i] = 0XFFbc4e00;
                                                    currentColor = "0XFFbc4e00";
                                                } else if (currentColor.equals("0XFFbc4e00")) {
                                                    mPrizeColor2[i] = 0XFFeb6100;
                                                    currentColor = "0XFFeb6100";
                                                }
                                            }
                                        }
                                    } else {
                                        if (currentColor.equals("")) {
                                            mPrizeColor2[i] = 0XFFe4f5d5;
                                            currentColor = "0XFFe4f5d5";
                                        } else if (currentColor.equals("0XFFa9e7e6")) {
                                            mPrizeColor2[i] = 0XFFe4f5d5;
                                            currentColor = "0XFFe4f5d5";
                                        } else if (currentColor.equals("0XFFe4f5d5")) {
                                            mPrizeColor2[i] = 0XFF88b8b7;
                                            currentColor = "0XFF88b8b7";
                                        } else if (currentColor.equals("0XFF88b8b7")) {
                                            mPrizeColor2[i] = 0XFFa9e7e6;
                                            currentColor = "0XFFa9e7e6";
                                        }
                                    }
                                }
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        for (int i = 0; i < mPrizeIcon2.length; i++) {
                                            String picUrl = mPrizeIcon2[i];
                                            if (null != picUrl && !picUrl.equals("")) {
                                                mImgIconBitmap.add(returnBitmap(picUrl));
                                            }
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                load_view.setVisibility(View.GONE);
                                                rotatePan.setData(mPrizeName2.length, mPrizeName2, mPrizeDesc, mPrizeColor2, mImgIconBitmap);
                                                rotatePan.refresh();
//                                                rotatePan.setStr(mPrizeName2);
                                            }
                                        });
                                    }
                                };
                                thread.start();
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
                            if (null != msg8 && !Tools.isPhonticName(msg8)) {
                                load_view.setVisibility(View.GONE);
                                sToast(msg8);
                            }
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
                        load_view.setVisibility(View.GONE);
                        sToast("请检查您的网络链接状态");
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

                    Handler dataHandler = new Handler(getContext()
                            .getMainLooper()) {

                        @Override
                        public void handleMessage(final Message msg) {
                            tv_shoyi_account.setText(available + "α");
                            tv_xiaofei_account.setText(ctccous + "个");
                            tv_juezhan_account.setText(ctcoption + "个");
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
     * 抽奖次数限制
     */
    private void drawCountConsole() {
        mIvStart.setClickable(false);
        HttpConnect.post(this, "member_roulette_count", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String jsonStr = arg0.body().string();
                final JSONObject data = JSONObject.fromObject(jsonStr);
                if (data.get("status").equals("success")) {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            JSONArray valueObj = data.optJSONArray("data");
                            if (null != valueObj) {
                                JSONObject oneObj = valueObj.optJSONObject(0);
                                if (null != oneObj) {
                                    mIvStart.setClickable(true);
                                    ONEDRAWCOUNT = oneObj.optString("one");
                                    TWODRAWCOUNT = oneObj.optString("two");
                                    THREEDRAWCOUNT = oneObj.optString("three");
                                    strength = oneObj.optString("strength");
                                    ctc = oneObj.optString("ctc");
                                    jinDou = oneObj.optString("gold"); // 金豆
                                    yinDou = oneObj.optString("silver");  // 银豆
                                    tv_three_showdrawcount.setText(THREEDRAWCOUNT + "连开");
                                    tv_two_showdrawcount.setText(TWODRAWCOUNT + "连开");
                                    tv_one_showdrawcount.setText(ONEDRAWCOUNT + "连开");
                                    tv_consume_detail.setText("消耗: " + yinDou + "个 + " + strength + "体力");
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            mIvStart.setClickable(false);
                            if (!TextUtils.isEmpty(data.optString("msg")) && !Tools.isPhonticName(data.getString("msg"))) {
                                sToast(data.optString("msg"));
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Handler dataHandler = new Handler(getContext().getMainLooper()) {
                    @Override
                    public void handleMessage(final Message msg) {
                        mIvStart.setClickable(false);
                        sToast("请检查您的网络连接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

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
                                                final MiningNotice miningNotice = notices.get(showCount);
                                                showCount = showCount + 1;

                                                if (miningNotice.getPic() != null && !miningNotice.getPic().equals("")) {
                                                    if (!isDestroyed()) {
                                                        Glide.with(NewSpanLayoutActivity2.this).load(miningNotice.getPic()).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_barrage_headimg) {
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

    /**
     * 抽奖弹幕
     */
    private void miningNotice() {
        notices.clear();
        HttpConnect.post(this, "member_roulette_notice", null, new Callback() {

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
                                        String pic = jsonObj.optString("pic");
                                        String contents = jsonObj.optString("value");
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
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(50000);
            conn.connect();
            InputStream is = conn.getInputStream();
            distoryBitmap(bitmap);
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeStream(is, null, options);
            } catch (Exception e) {

            } finally {
                is.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    public void distoryBitmap(Bitmap bmb) {
        if (null != bmb && !bmb.isRecycled())
        {
            bmb.recycle();
            bmb = null;
        }
        System.gc();
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==NewSpanLayoutActivity2.this.requestCode)
        {
                mIvStart.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mIvStart.isClickable())
        {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(mIvStart.isClickable())
            {
                finish();
            }
            return true;
        }else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 剩余活动时间
     */
    public void surplus_active_time()
    {
        HttpConnect.post(NewSpanLayoutActivity2.this, "member_roulette_date_is", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String dao = obj.optString("value");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null!=dao&&!TextUtils.isEmpty(dao))
                            {
                                if(dao.equals("1"))
                                {
                                    iv_into_span_ranking.setVisibility(View.VISIBLE);
                                }else
                                {
                                    iv_into_span_ranking.setVisibility(View.GONE);
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
     * 是否显示连开
     */
    public void isShowContinuityOpen()
    {
        HttpConnect.post(NewSpanLayoutActivity2.this, "member_is_white_list", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    final String dao = obj.optString("value");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(null!=dao&&!TextUtils.isEmpty(dao))
                            {
                                if(dao.equals("1"))
                                {
                                    linearLayout_liankai_console.setVisibility(View.VISIBLE);
                                }else
                                {
                                    linearLayout_liankai_console.setVisibility(View.GONE);
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
     * 是否有大转盘活动
     */
    public void isExitSpanActive()
    {
        HttpConnect.post(NewSpanLayoutActivity2.this, "member_roulette_activity_by_time", null, new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject active = JSONObject.fromObject(arg0.body().string());

                if (active.get("status").equals("success")) {

                    JSONObject obj = active.getJSONArray("data").getJSONObject(0);
                    String count = obj.optString("count");
                    if(!TextUtils.isEmpty(count))
                    {
                       activeUrl = obj.optString("value");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_into_span_active.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }else
                {
                    if(!TextUtils.isEmpty(active.getString("msg")))
                    {
                        if(active.getString("msg").contains("nodat"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_into_span_active.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }


    @Override
    public void initData() {

    }

   public void setClickFalse()
    {
        linearLayout_select_juezhanaccount.setClickable(false);
        linearLayout_select_xiaofeiaccount.setClickable(false);
        linearLayout_select_shouyiaccount.setClickable(false);
    }

    public static void setClickTrue()
    {
        linearLayout_select_juezhanaccount.setClickable(true);
        linearLayout_select_xiaofeiaccount.setClickable(true);
        linearLayout_select_shouyiaccount.setClickable(true);
    }

}
