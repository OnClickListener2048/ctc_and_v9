package com.tohier.cartercoin.activity;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.youhuijuanfragment.WeiShiYongFragment;
import com.tohier.cartercoin.youhuijuanfragment.YiGuoQiFragment;
import com.tohier.cartercoin.youhuijuanfragment.YiShiYongFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/19.
 */

public class YouHuiJuanActivity extends MyBaseFragmentActivity implements View.OnClickListener {

    private TextView tv_weishiyong, tv_yishiyong, tv_yiguoqi;
    private ViewPager viewPager;
    private ImageView view_line;
    private WeiShiYongFragment weiShiYongFragment;
    private YiShiYongFragment yiShiYongFragment;
    private YiGuoQiFragment yiGuoQiFragment;
    private List<Fragment> vp_data = new ArrayList<Fragment>();
    private float x,view_line_x;
    private int page;
    private TranslateAnimation animation;
    private int offset;
    private int currIndex;
    private int one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youhuijuan);

        initData();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = view_line.getLayoutParams();
        params.width = width / 3;
        view_line.setLayoutParams(params);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenW = dm.widthPixels;// 获取分辨率宽度
            offset = (screenW / 3 - view_line.getWidth()) / 2;// 计算偏移量
            Matrix matrix = new Matrix();
            matrix.postTranslate(offset, 0);
            view_line.setImageMatrix(matrix);
        // 设置动画初始位置


           one = offset * 2 + view_line.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;
        setUpView();

//        SliderConfig mConfig = new SliderConfig.Builder()
//                .primaryColor(Color.TRANSPARENT)
//                .secondaryColor(Color.TRANSPARENT)
//                .position(SliderPosition.LEFT)
//                .edge(false)
//                .build();
//
//        ISlider iSlider = SliderUtils.attachActivity(this, mConfig);
//        mConfig.setPosition(SliderPosition.LEFT);
//        iSlider.setConfig(mConfig);
    }

    @Override
    public void initData() {
        
        tv_weishiyong = (TextView) this.findViewById(R.id.tv_weishiyong);
        tv_yishiyong = (TextView) this.findViewById(R.id.tv_yishiyong);
        tv_yiguoqi = (TextView) this.findViewById(R.id.tv_yiguoqi);
        viewPager = (ViewPager) this.findViewById(R.id.activity_youhuijuan_viewpager);
        view_line = (ImageView) this.findViewById(R.id.activity_youhuijuan_line);


        tv_weishiyong.setOnClickListener(this);
        tv_yishiyong.setOnClickListener(this);
        tv_yiguoqi.setOnClickListener(this);

        weiShiYongFragment = new WeiShiYongFragment();
        yiShiYongFragment = new YiShiYongFragment();
        yiGuoQiFragment = new YiGuoQiFragment();

        vp_data.add(weiShiYongFragment);
        vp_data.add(yiShiYongFragment);
        vp_data.add(yiGuoQiFragment);
        viewPager.setAdapter(new TransactionAdapter(getSupportFragmentManager(), vp_data));
    }

    @SuppressWarnings("deprecation")
    private void setUpView() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          
            @Override
            public void onPageSelected(int arg0) {
                Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。
                currIndex = arg0;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                view_line.startAnimation(animation);
                switch (arg0)
                {
                    case 0:
                        tv_weishiyong.setTextColor(0xfffeb831);
                        tv_yishiyong.setTextColor(0xff4d4d4d);
                        tv_yiguoqi.setTextColor(0xff4d4d4d);
                        break;

                    case 1:
                        tv_yishiyong.setTextColor(0xfffeb831);
                        tv_weishiyong.setTextColor(0xff4d4d4d);
                        tv_yiguoqi.setTextColor(0xff4d4d4d);
                        break;

                    case 2:
                        tv_yiguoqi.setTextColor(0xfffeb831);
                        tv_yishiyong.setTextColor(0xff4d4d4d);
                        tv_weishiyong.setTextColor(0xff4d4d4d);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    @Override
    public void onClick(View v) {
           switch (v.getId())
           {
               case R.id.tv_weishiyong:
                   animation = new TranslateAnimation(one*currIndex, one*0, 0, 0);//显然这个比较简洁，只有一行代码。
                   currIndex = 0;
                   animation.setFillAfter(true);// True:图片停在动画结束位置
                   animation.setDuration(300);
                   view_line.startAnimation(animation);
                   viewPager.setCurrentItem(0);
                   tv_weishiyong.setTextColor(0xfffeb831);
                   tv_yishiyong.setTextColor(0xff4d4d4d);
                   tv_yiguoqi.setTextColor(0xff4d4d4d);
                   break;
               case R.id.tv_yishiyong:
                   animation  = new TranslateAnimation(one*currIndex, one*1, 0, 0);//显然这个比较简洁，只有一行代码。
                   currIndex = 1;
                   animation.setFillAfter(true);// True:图片停在动画结束位置
                   animation.setDuration(300);
                   view_line.startAnimation(animation);
                   viewPager.setCurrentItem(1);
                   tv_yishiyong.setTextColor(0xfffeb831);
                   tv_weishiyong.setTextColor(0xff4d4d4d);
                   tv_yiguoqi.setTextColor(0xff4d4d4d);
                   break;
               case R.id.tv_yiguoqi:
                   animation = new TranslateAnimation(one*currIndex, one*2, 0, 0);//显然这个比较简洁，只有一行代码。
                   currIndex = 2;
                   animation.setFillAfter(true);// True:图片停在动画结束位置
                   animation.setDuration(300);
                   view_line.startAnimation(animation);
                   viewPager.setCurrentItem(2);
                   tv_yiguoqi.setTextColor(0xfffeb831);
                   tv_yishiyong.setTextColor(0xff4d4d4d);
                   tv_weishiyong.setTextColor(0xff4d4d4d);
                   break;
           }
    }

    public void back(View view){
        finish();
    }
}
