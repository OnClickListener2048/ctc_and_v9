package com.tohier.cartercoin.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.BaseViewPagerAdapter;
import com.tohier.cartercoin.columnview.TabPagerIndicator;
import com.tohier.cartercoin.orderfragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends MyBaseFragmentActivity {

    private TabPagerIndicator mPagerIndicator;
    private ViewPager vpMall;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<TextView> textViews = new ArrayList<TextView>();
    private ArrayList<String> listType = new ArrayList<String>();
    private ArrayList<String> listTypeState = new ArrayList<String>();
    private List<Fragment> datas2 = new ArrayList<Fragment>();

    private BaseViewPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        init();

        setUp();

    }



    private void init() {

        listType.clear();
        listTypeState.clear();
        fragments.clear();
        textViews.clear();

        listType.add("全部");
        listType.add("待付款");
        listType.add("待发货");
        listType.add("已发货");
        listTypeState.add("0");
        listTypeState.add("1");
        listTypeState.add("2");
        listTypeState.add("3");

        mPagerIndicator = (TabPagerIndicator)findViewById(R.id.pagerIndicator);
        vpMall = (ViewPager)findViewById(R.id.vp_goods);
        vpMall.setOffscreenPageLimit(4);
        for (int i=0;i<listType.size();i++){

            TextView textView = new TextView(this);
            textView.setText(listType.get(i));
            textView.setTextColor(0xff000000);
            textView.setPadding(20,10,20,10);

            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(16f);
            textViews.add(textView);

            OrderFragment orderFragment = new OrderFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type",listTypeState.get(i));
            orderFragment.setArguments(bundle);
            fragments.add(orderFragment);
        }

        mPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments, listType);
        vpMall.setAdapter(mPagerAdapter);
        mPagerIndicator.setViewPager(vpMall);
        mPagerIndicator.setIndicatorMode(TabPagerIndicator.IndicatorMode.MODE_WEIGHT_EXPAND_NOSAME,
                true);


    }


    private void setUp() {
        /**
         * 设置监听事件
         *
         */
        for (int i = 0 ; i < textViews.size() ; i ++){
            final TextView textView = textViews.get(i);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0 ; j < textViews.size() ; j ++){

                        if (textView.getText().toString().equals(textViews.get(j).getText().toString())){
                            vpMall.setCurrentItem(finalI);
                            textViews.get(j).setTextColor(0xffffb831);

                        }else{
                            textViews.get(j).setTextColor(0xff000000);
                        }
                    }
                }
            });
        }


        vpMall.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0 ; i < textViews.size() ; i ++){
                    if (i == position){
                        textViews.get(i).setTextColor(0xffffb831);

                    }else{
                        textViews.get(i).setTextColor(0xff000000);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initData() {


    }


    public void back(View view){
        finish();
    }
}
