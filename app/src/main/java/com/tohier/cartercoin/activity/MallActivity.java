package com.tohier.cartercoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.design.widget.TabLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.BaseViewPagerAdapter;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.columnview.TabPagerIndicator;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.mallfragment.MyMallFragment;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2016/11/9.
 */

public class MallActivity extends MyBaseFragmentActivity implements View.OnClickListener{


//    private TabPagerIndicator mPagerIndicator;
    private TabLayout tablayout;
    private Button btn_into_jifen,btn_erweima_saomiao;
    private ImageView tvOrder;
    private TextView tv_hah;
    private ViewPager vpMall;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<TextView> textViews = new ArrayList<TextView>();
    private ArrayList<String> listType = new ArrayList<String>();
    private ArrayList<String> listTypeId = new ArrayList<String>();

    private List<Fragment> datas2 = new ArrayList<Fragment>();

    private BaseViewPagerAdapter mPagerAdapter;
    private boolean first;

    private NoLinkView iv_no_link;
    private LoadingView loadview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mall);

        iv_no_link = (NoLinkView) findViewById(R.id.iv_no_link);
        if (Tools.getAPNType(MallActivity.this) == true){
            iv_no_link.setVisibility(View.GONE);
            initData();
        }else{
            iv_no_link.setVisibility(View.VISIBLE);
        }

       iv_no_link.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Tools.getAPNType(MallActivity.this) == true){
                   iv_no_link.setVisibility(View.GONE);
                   initData();
               }else{
                   iv_no_link.setVisibility(View.VISIBLE);
               }

           }
       });

    }

    @Override
    public void initData() {
//        mPagerIndicator = (TabPagerIndicator) findViewById(R.id.pagerIndicator);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        vpMall = (ViewPager) findViewById(R.id.vp_goods);
        btn_erweima_saomiao = (Button) findViewById(R.id.btn_erweima_saomiao);
        btn_into_jifen = (Button) findViewById(R.id.btn_into_jifen);
        tvOrder = (ImageView) findViewById(R.id.tv_order);
        loadview = (LoadingView) findViewById(R.id.loadview);


        loadview.setVisibility(View.VISIBLE);
        HttpConnect.post(this, "products_kind_list", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                listType.clear();
                listTypeId.clear();
                fragments.clear();
                textViews.clear();
                String str = arg0.body().string();
                JSONObject data = JSONObject.fromObject(str);
                if (data.getString( "status").equals("success")) {
                    JSONArray list = data.getJSONArray("data");

                    for (int i = 0; i<list.size(); i++){
                        listTypeId.add(list.getJSONObject(i).getString("id"));
                        listType.add(list.getJSONObject(i).getString("name"));
                    }

                    MallActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i=0;i<listType.size();i++){
                                tablayout.addTab(tablayout.newTab().setText(listType.get(i)));

                                MyMallFragment myMallFragment = new MyMallFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("type",listTypeId.get(i));
                                myMallFragment.setArguments(bundle);
                                fragments.add(myMallFragment);
                            }
                            vpMall.setOffscreenPageLimit(listType.size());
                            mPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments, listType);
                            vpMall.setAdapter(mPagerAdapter);
//                            mPagerIndicator.setViewPager(vpMall);
//                            mPagerIndicator.setIndicatorMode(TabPagerIndicator.IndicatorMode.MODE_WEIGHT_EXPAND_NOSAME,
//                                    true);

                            tablayout.setupWithViewPager(vpMall);

                            if(tablayout.getTabCount()>3)
                            {
                                tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                            }

                            LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);
                            linearLayout.setPadding(0,30,0,30);
                            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                            linearLayout.setDividerDrawable(ContextCompat.getDrawable(MallActivity.this,
                                    R.drawable.layout_divider_vertical));

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
                                                textViews.get(j).setTextColor(0xffff0000);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadview.setVisibility(View.GONE);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadview.setVisibility(View.GONE);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadview.setVisibility(View.GONE);
                    }
                });
            }
        });





        btn_into_jifen.setOnClickListener(this);
        btn_erweima_saomiao.setOnClickListener(this);
        tvOrder.setOnClickListener(this);



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
    public void onClick(View v) {
        if(v.getId()==R.id.btn_erweima_saomiao)
        {
            Intent intent = new Intent(MallActivity.this, GongGaoActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.tv_order){

            startActivity(new Intent(MallActivity.this, MyOrderActivity.class));
        }
    }


    public void back(View view){
        finish();
    }

}
