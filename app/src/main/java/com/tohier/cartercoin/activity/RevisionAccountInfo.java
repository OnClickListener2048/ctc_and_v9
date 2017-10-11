package com.tohier.cartercoin.activity;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
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

/**
 * Created by Administrator on 2017/6/19.
 */

public class RevisionAccountInfo extends MyBaseActivity {

    private ImageView iv_back2,iv_line;
    private TextView tv_all_assets,tv_chongzhi_tab,tv_tixian_tab;
    private ViewPager vp_account_info;

    private int offset;
    private int currIndex;
    private int one;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              switch (v.getId())
              {
                  case  R.id.iv_back2:
                       finish();
                      break;

                  case  R.id.tv_chongzhi_tab:

                      break;

                  case  R.id.tv_tixian_tab:

                      break;
              }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revision_account_layout);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        super.initData();

        iv_back2 = (ImageView) this.findViewById(R.id.iv_back2);
        tv_all_assets = (TextView) this.findViewById(R.id.tv_all_assets);
        tv_chongzhi_tab = (TextView) this.findViewById(R.id.tv_chongzhi_tab);
        tv_tixian_tab = (TextView) this.findViewById(R.id.tv_tixian_tab);
        vp_account_info = (ViewPager) this.findViewById(R.id.vp_account_info);
        iv_line = (ImageView) this.findViewById(R.id.iv_line);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = iv_line.getLayoutParams();
        params.width = width / 2;
        iv_line.setLayoutParams(params);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - iv_line.getWidth()) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_line.setImageMatrix(matrix);
        // 设置动画初始位置

        one = offset * 2 + iv_line.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;
    }

    public void setUpView()
    {
        iv_back2.setOnClickListener(onClickListener);
        tv_chongzhi_tab.setOnClickListener(onClickListener);
        tv_tixian_tab.setOnClickListener(onClickListener);


        vp_account_info.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){
                    tv_chongzhi_tab.setTextColor(0xfffeb831);
                    tv_tixian_tab.setTextColor(0xff4d4d4d);


                }else if (position == 1){
                    tv_tixian_tab.setTextColor(0xfffeb831);
                    tv_chongzhi_tab.setTextColor(0xff4d4d4d);
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

}
