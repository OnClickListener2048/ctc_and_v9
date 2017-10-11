package com.tohier.cartercoin.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.presenter.LoadMiningRankingListDataPresenter;
import com.tohier.cartercoin.ui.LoadMiningRankingListDataView;
/**
 * Created by Administrator on 2016/11/12.
 */

public class MiningRankingListActivity extends MyBaseFragmentActivity implements View.OnClickListener,LoadMiningRankingListDataView{

    private ViewPager viewPager;
    private LoadMiningRankingListDataPresenter loadMiningRankingListDataPresenter;
    private TextView tvName,tvRanking,tvCount;
    private ImageView iv_member_default_head_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mining_rangkinglist);

        loadMiningRankingListDataPresenter = new LoadMiningRankingListDataPresenter(this,this);
        loadMiningRankingListDataPresenter.isExitActive();
        initData();
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
        viewPager = (ViewPager) findViewById(R.id.viewpager_mining_rankingList);
        ((TextView)findViewById(R.id.tv_week_ranking)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_month_ranking)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_all_ranking)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.iv_back)).setOnClickListener(this);
        tvName = (TextView) this.findViewById(R.id.tv_name);
        tvRanking = (TextView) this.findViewById(R.id.tv_ranking);
        tvCount = (TextView) this.findViewById(R.id.tv_count);
        iv_member_default_head_img = (ImageView) this.findViewById(R.id.circleImageView_touxiang);
    }


    private void setUpView() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                     switch (position)
                     {
                         case 0:
                             if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae())) //周
                             {
                                 loadMiningRankingListDataPresenter.loadPersonalMining("week");
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xfffeb831);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                             }else                                                                        //活动
                             {
                                 loadMiningRankingListDataPresenter.loadPersonalMining("active");
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xfffeb831);
                             }
                             break;
                         case 1:
                             if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae()))   //月
                             {
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xfffeb831);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                                 loadMiningRankingListDataPresenter.loadPersonalMining("month");
                             }else                                                                    //周
                             {
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xfffeb831);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                                 loadMiningRankingListDataPresenter.loadPersonalMining("week");
                             }
                             break;
                         case 2:
                             if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae()))   //总
                             {
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xfffeb831);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                                 loadMiningRankingListDataPresenter.loadPersonalMining("all");
                             }else                                                                    //月
                             {
                                 ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xfffeb831);
                                 ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                                 ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                                 loadMiningRankingListDataPresenter.loadPersonalMining("month");
                             }
                             break;
                         case 3:  //总排行
                             ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                             ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                             ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xfffeb831);
                             ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                             loadMiningRankingListDataPresenter.loadPersonalMining("all");
                             break;
                     }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

        @Override
        public void onClick(View v) {
             switch (v.getId())
             {
                 case R.id.tv_active_ranking:
                     viewPager.setCurrentItem(0);
                     loadMiningRankingListDataPresenter.loadPersonalMining("active");
                     ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xfffeb831);
                     break;

                 case R.id.tv_week_ranking:
                     if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae()))
                     {
                         viewPager.setCurrentItem(0);
                     }else
                     {
                         viewPager.setCurrentItem(1);
                     }
                     ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xfffeb831);
                     ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                     loadMiningRankingListDataPresenter.loadPersonalMining("week");
                     break;

                 case R.id.tv_month_ranking:
                     if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae()))
                     {
                         viewPager.setCurrentItem(1);
                     }else
                     {
                         viewPager.setCurrentItem(2);
                     }
                     ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xfffeb831);
                     ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                     loadMiningRankingListDataPresenter.loadPersonalMining("month");
                     break;

                 case R.id.tv_all_ranking:
                     if(TextUtils.isEmpty(loadMiningRankingListDataPresenter.getActiveNmae()))
                     {
                         viewPager.setCurrentItem(2);
                     }else
                     {
                         viewPager.setCurrentItem(3);
                     }
                     ((TextView)findViewById(R.id.tv_month_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_all_ranking)).setTextColor(0xfffeb831);
                     ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
                     ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
                     loadMiningRankingListDataPresenter.loadPersonalMining("all");
                     break;
                 case R.id.iv_back:
                      finish();
                     break;
             }
        }

    @Override
    public void loadFail() {
//        sToast("链接失败");
    }

    @Override
    public void showMsg(String msg) {
//        sToast(msg);
    }

    @Override
    public void showProgress() {
        mZProgressHUD.show();
    }

    @Override
    public void hideProgress() {
             mZProgressHUD.hide();
    }

    @Override
    public TextView getActiveTextView() {
        return (TextView) findViewById(R.id.tv_active_ranking);
    }

    @Override
    public ViewPager getMiningViewPager() {
        return viewPager;
    }

    @Override
    public void setIndividualRanking(RankingData rankingData) {
        if (rankingData.getImgUrl() != null)
        {
            Glide.with(MiningRankingListActivity.this).load(rankingData.getImgUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_member_default_head_img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_member_default_head_img.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        if(rankingData.getName()!=null)
        {
           tvName.setText(rankingData.getName());
        }
        if(rankingData.getRanking()!=null)
        {
            tvRanking.setText(rankingData.getRanking());
        }
        if(rankingData.getPrice()!=null)
        {
            tvCount.setText(rankingData.getPrice());
        }
    }

    @Override
    public void setTextColor(String type) {
        if(!TextUtils.isEmpty(type)&&type.equals("有活动"))
        {
            ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xfffeb831);
            ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xff4d4d4d);
        }else
        {
            ((TextView)findViewById(R.id.tv_active_ranking)).setTextColor(0xff4d4d4d);
            ((TextView)findViewById(R.id.tv_week_ranking)).setTextColor(0xfffeb831);
        }
    }
}
