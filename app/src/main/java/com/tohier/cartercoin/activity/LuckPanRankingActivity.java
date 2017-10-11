package com.tohier.cartercoin.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.luckpanranking.LuckPanActiveRuleFragment;
import com.tohier.cartercoin.luckpanranking.LuckPanRankingFragment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LuckPanRankingActivity extends MyBaseFragmentActivity implements View.OnClickListener {

    private TextView tvMoneyRanking,tvIntegralRanking,tvName,tvRanking,tvCount;
    private ViewPager vpShareOption;
    private ImageView line;
    private CircleImageView civTouxiang;
    private List<Fragment> vpFragment = new ArrayList<Fragment>();
    private LuckPanActiveRuleFragment luckPanActiveRuleFragment;
    private LuckPanRankingFragment luckPanRankingFragment;
    private int offset;
    private int currIndex;
    private int one;

    private NoLinkView ivNoLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_option_ranking);


        ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
        if (Tools.getAPNType(LuckPanRankingActivity.this) == true){
            ivNoLink.setVisibility(View.GONE);
            init();
            setUp();
            loadMeRanking();
        }else{
            ivNoLink.setVisibility(View.VISIBLE);
        }

        ivNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.getAPNType(LuckPanRankingActivity.this) == true){
                    ivNoLink.setVisibility(View.GONE);
                    init();
                    setUp();
                    loadMeRanking();
                }else{
                    ivNoLink.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void init() {
        vpShareOption = (ViewPager) findViewById(R.id.vp_option_ranking);
        tvMoneyRanking = (TextView) findViewById(R.id.tv_money_ranking);
        tvIntegralRanking = (TextView) findViewById(R.id.tv_jifen_ranking);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvRanking = (TextView) findViewById(R.id.tv_ranking);
        tvCount = (TextView) findViewById(R.id.tv_count);
        line = (ImageView) findViewById(R.id.iv_line);
        civTouxiang = (CircleImageView) findViewById(R.id.circleImageView_touxiang);

        ((TextView) findViewById(R.id.tv_title)).setText("抽奖排行榜");
        tvMoneyRanking.setText("次数排行");
        tvIntegralRanking.setText("活动规则");

        luckPanRankingFragment = new LuckPanRankingFragment();
        luckPanActiveRuleFragment = new LuckPanActiveRuleFragment();

        vpFragment.add(luckPanRankingFragment);
        vpFragment.add(luckPanActiveRuleFragment);


        vpShareOption.setAdapter(new TransactionAdapter(getSupportFragmentManager(),vpFragment));


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = line.getLayoutParams();
        params.width = width / 2;
        line.setLayoutParams(params);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - line.getWidth()) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        line.setImageMatrix(matrix);
        // 设置动画初始位置

        one = offset * 2 + line.getWidth();// 页卡1 -> 页卡2 偏移量  或者int one=screenW / 3;

    }

    private void setUp() {
        tvMoneyRanking.setOnClickListener(this);
        tvIntegralRanking.setOnClickListener(this);

        vpShareOption.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){
                    tvMoneyRanking.setTextColor(0xffF7BD2B);
                    tvIntegralRanking.setTextColor(0xff000000);


                }else if (position == 1){
                    tvIntegralRanking.setTextColor(0xffF7BD2B);
                    tvMoneyRanking.setTextColor(0xff000000);

                }

                Animation animation = new TranslateAnimation(one*currIndex, one*position, 0,0);//显然这个比较简洁，只有一行代码。
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                line.startAnimation(animation);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_money_ranking:
                currIndex = 0;
                vpShareOption.setCurrentItem(currIndex);
                break;
            case R.id.tv_jifen_ranking:
                currIndex = 1;
                vpShareOption.setCurrentItem(currIndex);
                break;
        }

    }

    public void back(View view){
        finish();
    }

    /**
     * 个人排行
     *
     */
    public void loadMeRanking()
    {
        if(!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName()))
        {
            tvName.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
        }

        if(!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl()))
        {
            Glide.with(LuckPanRankingActivity.this).load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(civTouxiang) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(LuckPanRankingActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    civTouxiang.setImageDrawable(circularBitmapDrawable);
                }
            });
        }



        HttpConnect.post(LuckPanRankingActivity.this, "member_roulette_count_list_myself", null, new Callback() {
            @Override
            public void onResponse(Response arg0) throws IOException {
                String json = arg0.body().string();
                JSONObject data = JSONObject.fromObject(json);
                if (data.optString("status").equals("success")){
                    JSONArray array = data.optJSONArray("data");
                    if (array!=null){
                        final JSONObject obj = array.optJSONObject(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(obj.optString("Nickname")))
                                {
                                    tvName.setText(obj.optString("Nickname"));
                                }

                                if(!TextUtils.isEmpty(obj.optString("Pic")))
                                {
                                    Glide.with(LuckPanRankingActivity.this).load(obj.optString("Pic")).asBitmap().centerCrop().into(new BitmapImageViewTarget(civTouxiang) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(LuckPanRankingActivity.this.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            civTouxiang.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }

                                if(!TextUtils.isEmpty(obj.optString("co")))
                                {
                                    tvCount.setText(obj.optString("co")+" 次");
                                }

                                if(!TextUtils.isEmpty(obj.optString("rownum")))
                                {
                                    tvRanking.setText("第"+obj.optString("rownum")+"名");
                                }
                            }
                        });
                    }

                }else{

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

}
