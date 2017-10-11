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
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.bean.UserShareOptionRanking;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.fragment.ContentFragment;
import com.tohier.cartercoin.presenter.ShareOptionRankingPresenter;
import com.tohier.cartercoin.shareoptionrankingfragment.ShareOptionIntegralRankingFragment;
import com.tohier.cartercoin.ui.ShareOptionRankingView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentActivity extends MyBaseFragmentActivity implements ShareOptionRankingView, View.OnClickListener {

    private TextView tvMoneyRanking,tvIntegralRanking,tvName,tvRanking,tvCount;
    private ViewPager vpShareOption;
    private ImageView line;
    private CircleImageView civTouxiang;
    private List<Fragment> vpFragment = new ArrayList<Fragment>();
    private ContentFragment contentFragment;
    private ShareOptionIntegralRankingFragment shareOptionIntegralRankingFragment;
    private int offset;
    private int currIndex;
    private int one;
    private ShareOptionRankingPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_option_ranking);

        init();
        setUp();


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

    private void init() {

        tvMoneyRanking = (TextView) findViewById(R.id.tv_money_ranking);
        tvIntegralRanking = (TextView) findViewById(R.id.tv_jifen_ranking);
        vpShareOption = (ViewPager) findViewById(R.id.vp_option_ranking);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvRanking = (TextView) findViewById(R.id.tv_ranking);
        tvCount = (TextView) findViewById(R.id.tv_count);
        line = (ImageView) findViewById(R.id.iv_line);
        civTouxiang = (CircleImageView) findViewById(R.id.circleImageView_touxiang);


        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("title"));
        tvMoneyRanking.setText("积分排行");
        tvIntegralRanking.setText("活动规则");

        shareOptionIntegralRankingFragment = new ShareOptionIntegralRankingFragment();
        shareOptionIntegralRankingFragment.setCode(getIntent().getStringExtra("code"));
        contentFragment = new ContentFragment();
        contentFragment.setUrl(getIntent().getStringExtra("url"));

        vpFragment.add(shareOptionIntegralRankingFragment);
        vpFragment.add(contentFragment);


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

        presenter = new ShareOptionRankingPresenter(this,this);

        presenter.getSelfRanking(getIntent().getStringExtra("code1"));
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


    @Override
    public void successRanking(ArrayList<UserShareOptionRanking> list1) {
    }

    @Override
    public void fail(String msg) {
        if (msg.equals("nodata1")){
            if (!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())){
                Glide.with(this).load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl()).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(civTouxiang) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        civTouxiang.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }else{
                civTouxiang.setImageResource(R.mipmap.iv_member_default_head_img);
            }
            tvName.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
            tvRanking.setText("暂无排行");
            tvCount.setText("0 分");

        }

    }

    @Override
    public void successSelfRanking(UserShareOptionRanking userShareOptionRanking) {
        if (!TextUtils.isEmpty(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())){
            Glide.with(this).load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl()).asBitmap().centerCrop().error(R.mipmap.iv_member_default_head_img).into(new BitmapImageViewTarget(civTouxiang) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    civTouxiang.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else{
            civTouxiang.setImageResource(R.mipmap.iv_member_default_head_img);
        }
        tvName.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());

        if (TextUtils.isEmpty(userShareOptionRanking.getRanking())){
            tvRanking.setText("暂无排行");
        }else{
            tvRanking.setText("第"+userShareOptionRanking.getRanking()+"名");
        }

        if (TextUtils.isEmpty(userShareOptionRanking.getCount())){
            tvCount.setText("0 分");
        }else{
            tvCount.setText(userShareOptionRanking.getCount()+" 分");
        }


    }
}
