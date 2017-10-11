package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.findfragment.HuoDongFragment;
import com.tohier.cartercoin.findfragment.XinWenFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class GongGaoActivity extends MyBaseFragmentActivity
{

    private ViewPager viewPager;
    private TextView tvNews,tvGongGao;
    private ImageView ivBack;
    private List<Fragment> datas = new ArrayList<Fragment>();

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.iv_back2:
                    finish();
                    break;
                case R.id.tv_news:
                    viewPager.setCurrentItem(0);
                    tvNews.setTextColor(0xfff7bd2b);
                    tvGongGao.setTextColor(0xff4d4d4d);
                    break;

                case R.id.tv_gonggao:
                    viewPager.setCurrentItem(1);
                    tvGongGao.setTextColor(0xfff7bd2b);
                    tvNews.setTextColor(0xff4d4d4d);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonggao_layout);

        initData();
        setUpView();
    }

    @Override
    public void initData() {
        viewPager = (ViewPager) this.findViewById(R.id.viewpager_gonggao_list);
        tvNews = (TextView) this.findViewById(R.id.tv_news);
        tvGongGao = (TextView) this.findViewById(R.id.tv_gonggao);
        ivBack = (ImageView) this.findViewById(R.id.iv_back2);

        datas.add(new XinWenFragment());
        datas.add(new HuoDongFragment());
        viewPager.setAdapter(new TransactionAdapter(getSupportFragmentManager(),datas));
    }

    private void setUpView() {
        tvNews.setOnClickListener(onClickListener);
        tvGongGao.setOnClickListener(onClickListener);
        ivBack.setOnClickListener(onClickListener);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  if(position==0)
                  {
                      tvNews.setTextColor(0xfff7bd2b);
                      tvGongGao.setTextColor(0xff4d4d4d);
                  }else
                  {
                      tvGongGao.setTextColor(0xfff7bd2b);
                      tvNews.setTextColor(0xff4d4d4d);
                  }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
