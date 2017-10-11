package com.tohier.cartercoin.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.presenter.EntrustPresenter;
import com.tohier.cartercoin.transactionfragment.DealFragment;
import com.tohier.cartercoin.transactionfragment.DealRecordFragment;
import com.tohier.cartercoin.transactionfragment.NewEntrustFragment;
import com.tohier.cartercoin.transactionfragment.OldEntrustFragment;
import com.tohier.cartercoin.ui.EntrustView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class TransactionActivity extends MyBaseFragmentActivity implements EntrustView, View.OnClickListener{

    private TextView tvTransacion,tvCjTransaction,tvNewEntrust,tvOldEntrust;
    private ViewPager vpTransaction;
    private ImageView lineTransaction;
    private List<Fragment> vpFragment = new ArrayList<Fragment>();
    private TranslateAnimation animation;
    private DealFragment dealFragment;
    private DealRecordFragment dealRecordFragment;
    private NewEntrustFragment newEntrustFragment;
    private OldEntrustFragment oldEntrustFragment;

    private int offset;
    private int currIndex;
    private int one;
    private EntrustPresenter presenter;
    private TextView tvCurrentPrice,tvMaxPrice,tvMinPrice,tvVol;
    private NoLinkView ivNoLink;

    private SharedPreferences sharedPreferences;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x111){
                presenter.getCTCInfo();
                handler.postDelayed(thread,DealFragment.time*1000);
            }
        }
    };

    Thread thread = new Thread(){
        @Override
        public void run(){
            handler.sendEmptyMessage(0x111);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_transaction);
        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);

        if (Tools.getAPNType(TransactionActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            init();
            setUp();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(TransactionActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    init();
                    setUp();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setUp() {
        tvTransacion.setOnClickListener(this);
        tvNewEntrust.setOnClickListener(this);
        tvOldEntrust.setOnClickListener(this);
        tvCjTransaction.setOnClickListener(this);

        vpTransaction.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){
                    tvTransacion.setTextColor(0xfffeb831);
                    tvCjTransaction.setTextColor(0xff000000);
                    tvNewEntrust.setTextColor(0xff000000);
                    tvOldEntrust.setTextColor(0xff000000);
                }else if (position == 1){
                    tvTransacion.setTextColor(0xff000000);
                    tvCjTransaction.setTextColor(0xfffeb831);
                    tvNewEntrust.setTextColor(0xff000000);
                    tvOldEntrust.setTextColor(0xff000000);
                }else if (position == 2){
                    tvNewEntrust.setTextColor(0xfffeb831);
                    tvCjTransaction.setTextColor(0xff000000);
                    tvTransacion.setTextColor(0xff000000);
                    tvOldEntrust.setTextColor(0xff000000);
                }else{
                    tvNewEntrust.setTextColor(0xff000000);
                    tvCjTransaction.setTextColor(0xff000000);
                    tvTransacion.setTextColor(0xff000000);
                    tvOldEntrust.setTextColor(0xfffeb831);
                }

                Animation animation = new TranslateAnimation(one*currIndex, one*position, 0,0);//显然这个比较简洁，只有一行代码。
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                lineTransaction.startAnimation(animation);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        tvTransacion = (TextView) findViewById(R.id.tv_transaction);
        tvCjTransaction = (TextView) findViewById(R.id.tv_cj_transaction);
        tvNewEntrust = (TextView) findViewById(R.id.tv_new_entrust);
        tvOldEntrust = (TextView) findViewById(R.id.tv_old_entrust);
        vpTransaction = (ViewPager) findViewById(R.id.vp_transaction);
        lineTransaction = (ImageView) findViewById(R.id.iv_transaction_line);

        tvCurrentPrice = (TextView) findViewById(R.id.tv_current_price);
        tvMaxPrice = (TextView) findViewById(R.id.tv_max_price);
        tvMinPrice = (TextView) findViewById(R.id.tv_min_price);
        tvVol = (TextView) findViewById(R.id.tv_deal_vol);

        sharedPreferences = getSharedPreferences("loading",0);


        dealFragment = new DealFragment();
        dealRecordFragment = new DealRecordFragment();
        newEntrustFragment = new NewEntrustFragment();
        oldEntrustFragment = new OldEntrustFragment();

        vpFragment.add(dealFragment);
        vpFragment.add(dealRecordFragment);
        vpFragment.add(newEntrustFragment);
        vpFragment.add(oldEntrustFragment);

        vpTransaction.setAdapter(new TransactionAdapter(getSupportFragmentManager(),vpFragment));


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = lineTransaction.getLayoutParams();
        params.width = width / 4;
        lineTransaction.setLayoutParams(params);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 4 - lineTransaction.getWidth()) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        lineTransaction.setImageMatrix(matrix);
        // 设置动画初始位置

        one = offset * 2 + lineTransaction.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;

        presenter = new EntrustPresenter(this,this);
        handler.post(thread);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_transaction:
                currIndex = 0;
                vpTransaction.setCurrentItem(currIndex);
                break;

            case R.id.tv_cj_transaction:
                currIndex = 1;
                vpTransaction.setCurrentItem(currIndex);

                break;

            case R.id.tv_new_entrust:
                currIndex = 2;
                vpTransaction.setCurrentItem(currIndex);

                break;
            case R.id.tv_old_entrust:
                currIndex = 3;
                vpTransaction.setCurrentItem(currIndex);
                break;
        }
    }

    /**
     *
     * 导航栏底部线的动画
     * @param tv
     */
    private void translateRedLine(TextView tv)
    {
        float x = tv.getX();
        TranslateAnimation animation = new TranslateAnimation(lineTransaction.getX(), x, 0, 0) ;
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        lineTransaction.setX(x);
    }

    @Override
    public void getKLineInfo(String url) {

    }

    @Override
    public void fail(String msg) {

    }

    @Override
    public void getTradingSuccess() {

    }

    @Override
    public void getTrading(ArrayList<Transaction> list) {

    }

    @Override
    public void getIngoAndRate(HashMap<String, String> map) {

    }

    @Override
    public void isUpdateInfo(String flag) {

    }

    @Override
    public void getCTCInfo(HashMap<String, String> map) {

        tvMaxPrice.setText(map.get("maxprice"));
        tvMinPrice.setText(map.get("minprice"));
        tvCurrentPrice.setText(map.get("newprice"));

        long vol = Long.parseLong(map.get("volume"));
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
        if (vol>9999){

            if (vol>99999999){
                float size = (float)vol/100000000;
                String filesize = df.format(size);
                tvVol.setText(filesize+"亿");
            }else{

                float size = (float)vol/10000;
                String filesize = df.format(size);//返回的是String类型的
                tvVol.setText(filesize+"万");
            }
        }else{
            tvVol.setText(vol+"");
        }

    }

    @Override
    public void getPriceRangeAndCount(HashMap<String, String> map) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(thread);
    }

    public void back(View view){
        finish();
        sharedPreferences.edit().putInt("loading",0).commit();
    }

    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        finish();
        sharedPreferences.edit().putInt("loading",0).commit();
    }
}
