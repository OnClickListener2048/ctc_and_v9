package com.tohier.cartercoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.accountinfo.AccountSmallExchequerFragment;
import com.tohier.cartercoin.accountinfo.NewAccountWalletFragment;
import com.tohier.cartercoin.accountinfo.QuotationFragment;
import com.tohier.cartercoin.adapter.BaseViewPagerAdapter;
import com.tohier.cartercoin.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class RevisionAccountInfoActivity2 extends MyBaseFragmentActivity {

    private ImageView iv_back2,iv_line;
    private TextView tv_all_assets,tv_qianbao,tv_xiaojinku,tv_hangqing;
    private ViewPager vp_account_info;
    private TabLayout tablayout;

    private List<Fragment> vpFragment = new ArrayList<Fragment>();
    private ArrayList<String> titles = new ArrayList<String>();
    private NewAccountWalletFragment walletFragment;
    private AccountSmallExchequerFragment accountSmallExchequerFragment;
    private QuotationFragment quotationFragment;
    private BaseViewPagerAdapter mPagerAdapter;

    private int offset;
    private int currIndex;
    private int one;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {

                case  R.id.tv_qianbao:
                    currIndex = 0;
                    vp_account_info.setCurrentItem(currIndex);
                    break;

                case  R.id.tv_xiaojinku:
                    currIndex = 1;
                    vp_account_info.setCurrentItem(currIndex);
                    break;

                case  R.id.tv_hangqing:
                    currIndex = 2;
                    vp_account_info.setCurrentItem(currIndex);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revision_account_layout2);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        super.initData();

        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        iv_line = (ImageView) this.findViewById(R.id.iv_line);
        tv_all_assets = (TextView) this.findViewById(R.id.tv_all_assets);
        tv_qianbao = (TextView) this.findViewById(R.id.tv_qianbao);
        tv_xiaojinku = (TextView) this.findViewById(R.id.tv_xiaojinku);
        tv_hangqing = (TextView) this.findViewById(R.id.tv_hangqing);
        vp_account_info = (ViewPager) this.findViewById(R.id.vp_account_info);
        tablayout = (TabLayout) this.findViewById(R.id.tablayout);

        titles.add("钱包");
        titles.add("小金库");
        titles.add("行情");

        walletFragment = new NewAccountWalletFragment();
        accountSmallExchequerFragment = new AccountSmallExchequerFragment();
        quotationFragment = new QuotationFragment();
        vpFragment.add(walletFragment);
        vpFragment.add(accountSmallExchequerFragment);
        vpFragment.add(quotationFragment);
        vp_account_info.setOffscreenPageLimit(3);

        vp_account_info.setAdapter(new TransactionAdapter(getSupportFragmentManager(),vpFragment));

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = iv_line.getLayoutParams();
        params.width = width / 3;
        iv_line.setLayoutParams(params);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - iv_line.getWidth()) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_line.setImageMatrix(matrix);
        // 设置动画初始位置

        one = offset * 2 + iv_line.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;
//        tablayout.setupWithViewPager(vp_account_info);

        tv_qianbao.setOnClickListener(onClickListener);
        tv_hangqing.setOnClickListener(onClickListener);
        tv_xiaojinku.setOnClickListener(onClickListener);
    }

    public void setUpView()
    {
        boolean flag = getIntent().getBooleanExtra("ismain",true);
        if(flag)
        {
            iv_back2.setVisibility(View.GONE);
        }

        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        vp_account_info.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){
                    tv_qianbao.setTextColor(0xfffeb831);
                    tv_xiaojinku.setTextColor(0xff000000);
                    tv_hangqing.setTextColor(0xff000000);


                }else if (position == 1){
                    tv_xiaojinku.setTextColor(0xfffeb831);
                    tv_qianbao.setTextColor(0xff000000);
                    tv_hangqing.setTextColor(0xff000000);
                }else
                {
                    tv_hangqing.setTextColor(0xfffeb831);
                    tv_qianbao.setTextColor(0xff000000);
                    tv_xiaojinku.setTextColor(0xff000000);
                }

                Animation animation = new TranslateAnimation(one*currIndex, one*position, 0,0);//显然这个比较简洁，只有一行代码。
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                iv_line.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setAllAssets(String allMoney) {
        if(!TextUtils.isEmpty(allMoney))
        {
            tv_all_assets.setText(allMoney);
        }else
        {
            tv_all_assets.setText("0.00");
        }
    }

        /**
     * 双击退出程序
     */
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = getIntent().getBooleanExtra("ismain",true);
        if(flag)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    sToast("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();

                } else {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
                return true;
            }
        }else
        {
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
