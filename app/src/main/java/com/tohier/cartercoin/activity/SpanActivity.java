package com.tohier.cartercoin.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.activity.base.BaseFragmentActivity;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.PrizeBean;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.MyApplication;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.spanfragment.JueZhanSpanFragment;
import com.tohier.cartercoin.spanfragment.ShouYiSpanFragment;
import com.tohier.cartercoin.spanfragment.XiaoFeiSpanFragment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从-1开始
 */
public class SpanActivity extends BaseFragmentActivity implements View.OnClickListener {

    private Button mIvStart;
    private boolean   mIsClickStart; //默认为false避免还没点击开始转动就会提示

    //奖项的名称
    private String[] mPrizeName2 = null;
    private String[] mPrizeDesc = null;
    private String []    mPrizeIcon2 = new String[50];
    private int []    mPrizeColor2 = new int[50];
    //指针停下来那一项
    private static int SELECTION = -1;

    private List<PrizeBean> datas = new ArrayList<PrizeBean>();

    private ImageView iv_cancel_luckpan;

    private TextView tv_xiaofei_account,tv_shoyi_account,tv_juezhan_account;
    private LinearLayout linearLayout_select_shouyiaccount,linearLayout_select_xiaofeiaccount,linearLayout_select_juezhanaccount;
    private LinearLayout linearLayout_draw_fivecount,linearLayout_draw_tencount,linearLayout_draw_fifteencount;
    /**
     * 0    收益账户
     * 1    决战账户
     * 2    消费账户
     */
    private String selDrawAccount = "1";
    //抽奖次数
    private String selDrawCount = "1";

    private ImageView iv_fivecount_icon,iv_tencount_icon,iv_fifteencount_icon;


    private static String ONEDRAWCOUNT = "5";
    private static String TWODRAWCOUNT = "10";
    private static String THREEDRAWCOUNT = "15";

    public boolean isSuccess = true;

    int i = 0;
    int count = 0;
    public int requestCode = 0;

    private TextView  tv_one_showdrawcount,tv_two_showdrawcount,tv_three_showdrawcount;

    /**
     * 显示消耗品
     */
    private TextView tv_consume_detail;

    /**
     * 进入中奖记录
     */
    private TextView tv_into_winningrecord;

    private String currentColor = "";
    private String strength = "";
    private String ctc = "";
    private XiaoFeiSpanFragment xiaoFeiSpanFragment;
    private ShouYiSpanFragment shouYiSpanFragment;
    private JueZhanSpanFragment jueZhanSpanFragment;
    private FragmentTransaction beginTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span_layout);
        MyApplication.maps.put("LuckPanActivity",this);
        initViews();
        drawCountConsole();
//        loadLuckListData();
    }

    private void initViews() {
        beginTransaction = getFragmentManager().beginTransaction();
        xiaoFeiSpanFragment = new XiaoFeiSpanFragment();
        shouYiSpanFragment = new ShouYiSpanFragment();
        jueZhanSpanFragment = new JueZhanSpanFragment();

        beginTransaction.add(R.id.relativeLayout,xiaoFeiSpanFragment).add(R.id.relativeLayout,shouYiSpanFragment).add(R.id.relativeLayout,jueZhanSpanFragment).show(jueZhanSpanFragment).hide(shouYiSpanFragment).hide(xiaoFeiSpanFragment).commit();

        tv_into_winningrecord = (TextView) this.findViewById(R.id.tv_into_winningrecord);

        tv_xiaofei_account = (TextView) findViewById(R.id.tv_xiaofei_account);
        tv_shoyi_account = (TextView) findViewById(R.id.tv_shouyi_account);
        tv_juezhan_account = (TextView) findViewById(R.id.tv_juezhan_account);
        tv_consume_detail = (TextView) findViewById(R.id.tv_consume_detail);

        tv_one_showdrawcount = (TextView) findViewById(R.id.tv_one_showdrawcount);
        tv_two_showdrawcount = (TextView) findViewById(R.id.tv_two_showdrawcount);
        tv_three_showdrawcount = (TextView) findViewById(R.id.tv_three_showdrawcount);

        linearLayout_select_shouyiaccount = (LinearLayout) findViewById(R.id.linearLayout_select_shouyiaccount);
        linearLayout_select_juezhanaccount = (LinearLayout) findViewById(R.id.linearLayout_select_juezhanaccount);
        linearLayout_select_xiaofeiaccount = (LinearLayout) findViewById(R.id.linearLayout_select_xiaofeiaccount);

        linearLayout_draw_fivecount = (LinearLayout) findViewById(R.id.linearLayout_draw_fivecount);
        linearLayout_draw_tencount = (LinearLayout) findViewById(R.id.linearLayout_draw_tencount);
        linearLayout_draw_fifteencount = (LinearLayout) findViewById(R.id.linearLayout_draw_fifteencount);

        iv_cancel_luckpan = (ImageView) findViewById(R.id.iv_cancel_luckpan);
        mIvStart = (Button) findViewById(R.id.iv_start);

        iv_fifteencount_icon = (ImageView) findViewById(R.id.iv_fifteencount_icon);
        iv_tencount_icon = (ImageView) findViewById(R.id.iv_tencount_icon);
        iv_fivecount_icon = (ImageView) findViewById(R.id.iv_fivecount_icon);

        mIvStart.setOnClickListener(this);
        tv_into_winningrecord.setOnClickListener(this);
        iv_cancel_luckpan.setOnClickListener(this);
        linearLayout_select_shouyiaccount.setOnClickListener(this);
        linearLayout_select_juezhanaccount.setOnClickListener(this);
        linearLayout_select_xiaofeiaccount.setOnClickListener(this);

        linearLayout_draw_fivecount.setOnClickListener(this);
        linearLayout_draw_tencount.setOnClickListener(this);
        linearLayout_draw_fifteencount.setOnClickListener(this);

//        mLuckSpan.setOnSpanRollListener(new LuckSpan.SpanRollListener() {
//            @Override
//            public void onSpanRollListener(double speed) {
//                if (0 == speed) {
//                    //已经停止下来了 提示中奖名并释放按钮
//                    if (mIsClickStart) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mIsClickStart = false;
//                                startActivityForResult(new Intent(SpanActivity.this,PrizeActivity.class).putExtra("prizeName",mPrizeName2[SELECTION+1]).putExtra("prizeUrl",mPrizeIcon2[SELECTION+1]),requestCode);
//                            }
//                        });
//                    }
//                }
//            }
//        });

        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
        {
            if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
            {
                String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                //默认是男生的
                if(sex.equals("美女"))
                {
                    mIvStart.setBackgroundResource(R.drawable.luckpan_gril_btn_bg_shape_e90dc7);
                    iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                    linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                }else if(sex.equals("帅哥"))
                {

                }else
                {
                    mIvStart.setBackgroundResource(R.drawable.luckpan_secrecy_btn_bg_shape_d45800);
                    iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                    linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountInfo();
    }


    public void clear(Canvas aCanvas)
    {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        aCanvas.drawPaint(paint);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_start:
                clickToDraw();
                break;

            case R.id.tv_into_winningrecord:
                startActivity(new Intent(SpanActivity.this,WinningRecordActivity.class));
//                mLuckSpan.stop();
                finish();
                break;

            case R.id.iv_cancel_luckpan:
                mIvStart.setVisibility(View.INVISIBLE);
//                 mLuckSpan.stop();
                 finish();
                break;

            case R.id.linearLayout_select_shouyiaccount:
                beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.show(shouYiSpanFragment).hide(xiaoFeiSpanFragment).hide(jueZhanSpanFragment).commit();
                selDrawAccount = "0";
                if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                    {
                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                        //默认是男生的
                        if(sex.equals("美女"))
                        {
                            linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                        }else if(sex.equals("帅哥"))
                        {
                            linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                        }else
                        {
                            linearLayout_select_shouyiaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                        }
                    }
                }
                linearLayout_select_juezhanaccount.setBackgroundColor(0xffffffff);
                linearLayout_select_xiaofeiaccount.setBackgroundColor(0xffffffff);
                break;

            case R.id.linearLayout_select_juezhanaccount:
                beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.show(jueZhanSpanFragment).hide(shouYiSpanFragment).hide(xiaoFeiSpanFragment).commit();
                selDrawAccount = "1";
                if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                    {
                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                        //默认是男生的
                        if(sex.equals("美女"))
                        {
                            linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                        }else if(sex.equals("帅哥"))
                        {
                            linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                        }else
                        {
                            linearLayout_select_juezhanaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                        }
                    }
                }
                linearLayout_select_shouyiaccount.setBackgroundColor(0xffffffff);
                linearLayout_select_xiaofeiaccount.setBackgroundColor(0xffffffff);
                break;

            case R.id.linearLayout_select_xiaofeiaccount:
                beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.show(xiaoFeiSpanFragment).hide(shouYiSpanFragment).hide(jueZhanSpanFragment).commit();
                selDrawAccount = "2";
                if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                    {
                        String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                        //默认是男生的
                        if(sex.equals("美女"))
                        {
                            linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_girl_sel_account_bg);
                        }else if(sex.equals("帅哥"))
                        {
                            linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_sel_account_bg);
                        }else
                        {
                            linearLayout_select_xiaofeiaccount.setBackgroundResource(R.mipmap.iv_secrecy_sel_account_bg);
                        }
                    }
                }
                linearLayout_select_shouyiaccount.setBackgroundColor(0xffffffff);
                linearLayout_select_juezhanaccount.setBackgroundColor(0xffffffff);
                break;

            case R.id.linearLayout_draw_fivecount:
                if(!TextUtils.isEmpty(selDrawCount)&&selDrawCount.equals(ONEDRAWCOUNT))
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                    tv_consume_detail.setText("消耗: "+ctc+"α + "+strength+"体力");
                    selDrawCount = "1";
                }else
                {
                    selDrawCount = ONEDRAWCOUNT;
                   int sel =  Integer.parseInt(selDrawCount);
                   int mCtc =  Integer.parseInt(ctc);
                   int mStrength =  Integer.parseInt(strength);
                    tv_consume_detail.setText("消耗: "+mCtc*sel+"α + "+mStrength*sel+"体力");
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                }
                break;

            case R.id.linearLayout_draw_tencount:
                if(!TextUtils.isEmpty(selDrawCount)&&selDrawCount.equals(TWODRAWCOUNT))
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                    tv_consume_detail.setText("消耗: "+ctc+"α + "+strength+"体力");
                    selDrawCount = "1";
                }else
                {
                    selDrawCount = TWODRAWCOUNT;
                    int sel =  Integer.parseInt(selDrawCount);
                    int mCtc =  Integer.parseInt(ctc);
                    int mStrength =  Integer.parseInt(strength);
                    tv_consume_detail.setText("消耗: "+mCtc*sel+"α + "+mStrength*sel+"体力");
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                }
                break;

            case R.id.linearLayout_draw_fifteencount:
                if(!TextUtils.isEmpty(selDrawCount)&&selDrawCount.equals(THREEDRAWCOUNT))
                {
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                    tv_consume_detail.setText("消耗: "+ctc+"α + "+strength+"体力");
                    selDrawCount = "1";
                }else
                {
                    selDrawCount = THREEDRAWCOUNT;
                    int sel =  Integer.parseInt(selDrawCount);
                    int mCtc =  Integer.parseInt(ctc);
                    int mStrength =  Integer.parseInt(strength);
                    tv_consume_detail.setText("消耗: "+mCtc*sel+"α + "+mStrength*sel+"体力");
                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
                    {
                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
                        {
                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
                            //默认是男生的
                            if(sex.equals("美女"))
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_girl_sel_draw_count);
                            }else if(sex.equals("帅哥"))
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_sel_draw_count);
                            }else
                            {
                                iv_fifteencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count_pre);
                                iv_tencount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                                iv_fivecount_icon.setImageResource(R.mipmap.iv_secrecy_sel_draw_count);
                            }
                        }
                    }
                }
                break;
        }
    }

//    public void loadLuckListData()
//    {
//        HashMap<String,String> par = new HashMap<>();
////        Random random = new Random();
////        // 0-4 的随机数 不包括5
////        group = random.nextInt(5);
////        // 1- 5 的随机数
////        group = group + 1 ;
//        par.put("group",0+"");
//        HttpConnect.post(this, "member_roulette_list", par, new Callback() {
//
//            @Override
//            public void onResponse(Response arg0) throws IOException {
//                final JSONObject data = JSONObject.fromObject(arg0.body().string());
//                if (data.get("status").equals("success")) {
//
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            JSONArray array = data
//                                    .optJSONArray("data");
//                            if (array.size() != 0) {
//                                mPrizeName2 = new String[array.size()];
//                                mPrizeDesc = new String[array.size()];
//                                for(int i = 0 ; i < array.size() ; i ++)
//                                {
//                                    final String id = array.getJSONObject(i).getString("id");
//                                    String name = array.getJSONObject(i).getString("name");
//                                    final String money = array.getJSONObject(i).getString("money");
//                                    final String pic = array.getJSONObject(i).getString("pic");
//                                    final String type = array.getJSONObject(i).getString("type");
//                                    PrizeBean prizeBean = new PrizeBean(id,name,money,pic,type);
//                                    datas.add(prizeBean);
//
//                                    mPrizeIcon2[i] = pic;
//
//                                    String desc = "";
//                                    if(name.contains("("))
//                                    {
//                                        desc = name.substring(name.indexOf("("),name.length());
//                                        name = name.substring(0,name.indexOf("("));
//                                    }
//                                    mPrizeDesc[i] = desc;
//                                    mPrizeName2[i] = name;
//
//                                    if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser())
//                                    {
//                                        if(null!=LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex())
//                                        {
//                                            String sex = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getSex();
//                                            //默认是男生的
//                                            if(sex.equals("美女"))
//                                            {
//                                                if(currentColor.equals(""))
//                                                {
//                                                    mPrizeColor2[i] = 0Xfff986c1;
//                                                    currentColor = "0Xfff986c1";
//                                                }else if(currentColor.equals("0XFFe90dc7"))
//                                                {
//                                                    mPrizeColor2[i] = 0Xfff986c1;
//                                                    currentColor = "0Xfff986c1";
//                                                }else if(currentColor.equals("0Xfff986c1"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFf668bd;
//                                                    currentColor = "0XFFf668bd";
//                                                }else if(currentColor.equals("0XFFf668bd"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFe90dc7;
//                                                    currentColor = "0XFFe90dc7";
//                                                }
//                                            }else if(sex.equals("帅哥"))
//                                            {
//                                                if(currentColor.equals(""))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFe4f5d5;
//                                                    currentColor = "0XFFe4f5d5";
//                                                }else if(currentColor.equals("0XFFa9e7e6"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFe4f5d5;
//                                                    currentColor = "0XFFe4f5d5";
//                                                }else if(currentColor.equals("0XFFe4f5d5"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFF88b8b7;
//                                                    currentColor = "0XFF88b8b7";
//                                                }else if(currentColor.equals("0XFF88b8b7"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFa9e7e6;
//                                                    currentColor = "0XFFa9e7e6";
//                                                }
//                                            }else
//                                            {
//                                                if(currentColor.equals(""))
//                                                {
//                                                    mPrizeColor2[i] = 0XFF6d428d;
//                                                    currentColor = "0XFF6d428d";
//                                                }else if(currentColor.equals("0XFFeb6100"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFF6d428d;
//                                                    currentColor = "0XFF6d428d";
//                                                }else if(currentColor.equals("0XFF6d428d"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFbc4e00;
//                                                    currentColor = "0XFFbc4e00";
//                                                }else if(currentColor.equals("0XFFbc4e00"))
//                                                {
//                                                    mPrizeColor2[i] = 0XFFeb6100;
//                                                    currentColor = "0XFFeb6100";
//                                                }
//                                            }
//                                        }
//                                    }else
//                                    {
//                                        if(currentColor.equals(""))
//                                        {
//                                            mPrizeColor2[i] = 0XFFe4f5d5;
//                                            currentColor = "0XFFe4f5d5";
//                                        }else if(currentColor.equals("0XFFa9e7e6"))
//                                        {
//                                            mPrizeColor2[i] = 0XFFe4f5d5;
//                                            currentColor = "0XFFe4f5d5";
//                                        }else if(currentColor.equals("0XFFe4f5d5"))
//                                        {
//                                            mPrizeColor2[i] = 0XFF88b8b7;
//                                            currentColor = "0XFF88b8b7";
//                                        }else if(currentColor.equals("0XFF88b8b7"))
//                                        {
//                                            mPrizeColor2[i] = 0XFFa9e7e6;
//                                            currentColor = "0XFFa9e7e6";
//                                        }
//                                    }
//                                }
//                                mLuckSpan.setmPrizeName(mPrizeName2);
//                                mLuckSpan.setmPrizeDesc(mPrizeDesc);
//                                mLuckSpan.setmPrizeIcon2(mPrizeIcon2);
//                                mLuckSpan.setmSpanColor(mPrizeColor2);
//                                mLuckSpan.setmSpanCount(mPrizeName2.length);
//                                mLuckSpan.startLoadBitmap();
//                                mLuckSpan.startCanvas();
//                                mIvStart.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//                }else
//                {
//                    final String msg8 = data.getString("msg");
//                    Handler dataHandler = new Handler(getContext()
//                            .getMainLooper()) {
//
//                        @Override
//                        public void handleMessage(final Message msg) {
//                            if(null!=msg8&&!Tools.isPhonticName(msg8))
//                            {
//                                sToast(msg8);
//                            }
//                        }
//                    };
//                    dataHandler.sendEmptyMessage(0);
//                }
//            }
//
//            @Override
//            public  void onFailure(Request arg0, IOException arg1) {
//                Handler dataHandler = new Handler(getContext()
//                        .getMainLooper()) {
//
//                    @Override
//                    public void handleMessage(final Message msg) {
//                        sToast("请检查您的网络链接状态");
//                    }
//                };
//                dataHandler.sendEmptyMessage(0);
//            }
//        });
//    }

    /**
     * 点击进行抽奖
     */
    public void clickToDraw()
    {
        mIvStart.setClickable(false);
        if(TextUtils.isEmpty(selDrawCount))
        {
            mIvStart.setClickable(true);
            sToast("请选择抽奖次数");
        }else
        {
            if(selDrawCount.equals("1"))
            {
                drawOneCount();
            }else if(selDrawCount.equals(ONEDRAWCOUNT))
            {
               i = 0;
               count = Integer.parseInt(ONEDRAWCOUNT);
               Thread thread = new Thread()
               {
                   @Override
                   public void run() {
                       super.run();
                       while (true)
                       {
                           SystemClock.sleep(200);
                           if(isSuccess)
                           {
                               isSuccess = false;
                               i = i+1;
                               if(i==1)
                               {
                                   drawOneCount();
                               }else if(i>1&&i<=count)
                               {
                                   SystemClock.sleep(200);
                                   drawManyTimes();
                               }else
                               {
                                   mIvStart.setClickable(true);
                                   isSuccess = true;
                                   return;
                               }
                           }
                       }
                   }
               };
                thread.start();
            }else if(selDrawCount.equals(TWODRAWCOUNT))
            {
                i = 0;
                count = Integer.parseInt(TWODRAWCOUNT);
                Thread thread = new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        while (true)
                        {
                            SystemClock.sleep(200);
                            if(isSuccess) {
                                isSuccess = false;
                                i = i+1;
                                if(i==1)
                                {
                                    drawOneCount();
                                }else if(i>1&&i<=count)
                                {
                                    SystemClock.sleep(200);
                                    drawManyTimes();
                                }else
                                {
                                    mIvStart.setClickable(true);
                                    isSuccess = true;
                                    return;
                                }
                            }
                        }
                    }
                };
                thread.start();
            }else if(selDrawCount.equals(THREEDRAWCOUNT))
            {
                i = 0;
                count = Integer.parseInt(THREEDRAWCOUNT);
                Thread thread = new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        while (true)
                        {
                            SystemClock.sleep(200);
                            if(isSuccess) {
                                isSuccess = false;
                                i = i+1;
                                if(i==1)
                                {
                                    drawOneCount();
                                }else if(i>1&&i<=count)
                                {
                                    SystemClock.sleep(200);
                                    drawManyTimes();
                                }else
                                {
                                    mIvStart.setClickable(true);
                                    isSuccess = true;
                                    return;
                                }
                            }
                        }
                    }
                };
                thread.start();
            }
        }
    }

    public void loadAccountInfo()
    {
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
                        public void handleMessage(final Message msg)
                        {
                            tv_shoyi_account.setText(available+"α");
                            tv_xiaofei_account.setText(ctccous+"α");
                            tv_juezhan_account.setText(ctcoption+"α");
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                }else
                {
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
            public  void onFailure(Request arg0, IOException arg1) {
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

    private void drawOneCount()
    {
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("type",selDrawAccount);
        par1.put("groupid",selDrawAccount+"");
        HttpConnect.post(this, "member_roulette", par1, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String value = data.getJSONArray("data").getJSONObject(0).optString("token");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            if(!TextUtils.isEmpty(value))
                            {
                                for(int i = 0 ; i < datas.size() ; i ++)
                                {
                                    if(value.equals(datas.get(i).getId()))
                                    {
                                        SELECTION = i - 1;
                                        mIsClickStart = true;
                                        //生成0到11之间的随机数
                                        //转盘是从-1开始 10结束 所以要减一
                                        //传入的参数由后台返回指定中哪个奖项
//                                        mLuckSpan.luckyStart(SELECTION);
                                        //模拟请求网络
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //网络请求6秒
                                                SystemClock.sleep(4000);
                                                //逐渐停止转盘
//                                                mLuckSpan.luckStop();
                                            }
                                        }).start();
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
                            if(!TextUtils.isEmpty(data.optString("msg"))&&!Tools.isPhonticName(data.getString("msg")))
                            {
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

    /**
     * 抽奖多次
     */
    private void drawManyTimes()
    {
        Map<String, String> par1 = new HashMap<String, String>();
        par1.put("type",selDrawAccount);
        par1.put("groupid",selDrawAccount+"");
        HttpConnect.post(this, "member_roulette", par1, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                final JSONObject data = JSONObject.fromObject(arg0.body().string());
                if (data.get("status").equals("success")) {
                    final String value = data.getJSONArray("data").getJSONObject(0).optString("token");
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            if(!TextUtils.isEmpty(value))
                            {
                                for(int i = 0 ; i < datas.size() ; i ++)
                                {
                                    if(value.equals(datas.get(i).getId()))
                                    {
                                        SELECTION = i - 1;
                                        mIsClickStart = true;
                                        startActivity(new Intent(SpanActivity.this,PrizeActivity.class).putExtra("prizeName",mPrizeName2[SELECTION+1]).putExtra("prizeUrl",mPrizeIcon2[SELECTION+1]));
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
                            if(!TextUtils.isEmpty(data.optString("msg"))&&!Tools.isPhonticName(data.getString("msg")))
                            {
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

    /**
     * 抽奖次数限制
     */
    private void drawCountConsole()
    {
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
                            if(null!=valueObj)
                            {
                                JSONObject oneObj = valueObj.optJSONObject(0);
                                if(null!=oneObj)
                                {
                                    ONEDRAWCOUNT = oneObj.optString("one");
                                    TWODRAWCOUNT = oneObj.optString("two");
                                    THREEDRAWCOUNT = oneObj.optString("three");
                                    strength = oneObj.optString("strength");
                                    ctc = oneObj.optString("ctc");
                                    tv_three_showdrawcount.setText(THREEDRAWCOUNT+"连开");
                                    tv_two_showdrawcount.setText(TWODRAWCOUNT+"连开");
                                    tv_one_showdrawcount.setText(ONEDRAWCOUNT+"连开");
                                    tv_consume_detail.setText("消耗: "+ctc+"α + "+strength+"体力");
                                }
                            }
                        }
                    };
                    dataHandler.sendEmptyMessage(0);
                } else {
                    Handler dataHandler = new Handler(getContext().getMainLooper()) {
                        @Override
                        public void handleMessage(final Message msg) {
                            if(!TextUtils.isEmpty(data.optString("msg"))&&!Tools.isPhonticName(data.getString("msg")))
                            {
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
                        sToast("请检查您的网络连接状态");
                    }
                };
                dataHandler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mLuckSpan.stop();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==SpanActivity.this.requestCode)
        {
            if(selDrawCount.equals("1"))
            {
                mIvStart.setClickable(true);
            }
            isSuccess = true;
        }
    }
}
