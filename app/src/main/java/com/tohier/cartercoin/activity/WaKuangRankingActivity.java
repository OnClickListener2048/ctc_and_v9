package com.tohier.cartercoin.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.adapter.TransactionAdapter;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.columnview.NoLinkView;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.miningrankingfragment.WaKuangHuoDong_Ranking;
import com.tohier.cartercoin.miningrankingfragment.WaKuangMonth_Ranking;
import com.tohier.cartercoin.miningrankingfragment.WaKuangWeek_Ranking;
import com.tohier.cartercoin.miningrankingfragment.WaKuangZong_Ranking;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WaKuangRankingActivity extends MyBaseFragmentActivity implements View.OnClickListener{

	private CircleImageView iv_touxiang;
	private TextView tv_name, tv_ranking, tv_ctc;
	private String id ;
    private ViewPager vp;
	private List<Fragment> datas = new ArrayList<Fragment>();
    private TextView tv_all_ranking,tv_month_ranking,tv_zhou_ranking,tv_zhongqiu_ranking;
	private List<TextView> tv_datas = new ArrayList<TextView>();
    private String huoDongName;      
	private String msg;
    private int page_fragment;
	private TextView view,tv_wakuang;
	public static String startdate;
	private WaKuangHuoDong_Ranking waKuangHuoDong_Ranking;
	private WaKuangWeek_Ranking waKuangWeek_Ranking;
	private WaKuangMonth_Ranking waKuangMonth_Ranking;
	private WaKuangZong_Ranking waKuangZong_Ranking;

	private RankingData todaySelfRanking;
	private RankingData weekSelfRanking;
	private RankingData monthSelfRanking;

	private NoLinkView ivNoLink;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wakuang_ranking);
		id = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId();

		ivNoLink = (NoLinkView) findViewById(R.id.iv_no_link);
		vp = (ViewPager) findViewById(R.id.vp_wakuang_ranking);

		if (Tools.getAPNType(WaKuangRankingActivity.this) == true){
			ivNoLink.setVisibility(View.GONE);
			init();
			gerenTodayRanking("member_mining_today_self");
			gerenWeekRanking("member_mine_ranking_me_week");
			gerenMonthRanking("member_mine_ranking_me_month");
		}else{
			ivNoLink.setVisibility(View.VISIBLE);
		}

		setUpView();

	}

	
	private void translateRedLine(TextView tv)
	{
		float x = tv.getX();
	    TranslateAnimation animation = new TranslateAnimation(view.getX(), x, 0, 0) ;
	    animation.setDuration(400);
	    animation.setInterpolator(new LinearInterpolator());
	    animation.setFillAfter(true);
	    view.setX(x);
	}
	
	@SuppressWarnings("deprecation")
	private void setUpView() {
		ivNoLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Tools.getAPNType(WaKuangRankingActivity.this) == true){
					ivNoLink.setVisibility(View.GONE);
					init();
					gerenTodayRanking("member_mining_today_self");
					gerenWeekRanking("member_mine_ranking_me_week");
					gerenMonthRanking("member_mine_ranking_me_month");
				}else{
					ivNoLink.setVisibility(View.VISIBLE);
				}
			}
		});

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
			@Override
			public void onPageSelected(int arg0) {
				if(huoDongName==null)
				{
					switch (arg0) {
					case 1:
						translateRedLine(tv_zhou_ranking);
						setTextViewBGColor(tv_zhou_ranking);
						setWeekData("week");
						break;

	                case 2:
	                	translateRedLine(tv_month_ranking);
						setTextViewBGColor(tv_month_ranking);
						setWeekData("month");
						break;
						
	                case 0:
						translateRedLine(tv_all_ranking);
						setTextViewBGColor(tv_all_ranking);
						setWeekData("today");

	                	break;
					}
				}else
				{
					switch (arg0) {
					case 0:
						translateRedLine(tv_zhongqiu_ranking);
						setTextViewBGColor(tv_zhongqiu_ranking);

						
						break;

	                case 2:
						translateRedLine(tv_zhou_ranking);
						setTextViewBGColor(tv_zhou_ranking);
						setWeekData("week");
						break;
						
	                case 3:
	                	translateRedLine(tv_month_ranking);
						setTextViewBGColor(tv_month_ranking);
						setWeekData("month");
	                	break;
	                	
	                case 1:
						translateRedLine(tv_all_ranking);
						setTextViewBGColor(tv_all_ranking);
						setWeekData("today");
	                	break;
					}
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


	private void init() {
		view = (TextView) findViewById(R.id.tv_red_line);
		tv_wakuang = (TextView) findViewById(R.id.tv_wakuang);

		tv_all_ranking = (TextView) findViewById(R.id.tv_all_ranking);
		tv_month_ranking= (TextView) findViewById(R.id.tv_month_ranking);
		tv_zhou_ranking= (TextView) findViewById(R.id.tv_zhou_ranking);
		tv_zhongqiu_ranking = (TextView) findViewById(R.id.tv_zhongqiu_ranking);
		tv_all_ranking.setOnClickListener(this);
		tv_month_ranking.setOnClickListener(this);
		tv_zhou_ranking.setOnClickListener(this);
		tv_zhongqiu_ranking.setOnClickListener(this);
		

		iv_touxiang = (CircleImageView) findViewById(R.id.pv_touxiang);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_ranking = (TextView) findViewById(R.id.tv_ranking);
		tv_ctc = (TextView) findViewById(R.id.tv_ctc);
		
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		LayoutParams params = view.getLayoutParams();
		isYouHuoDong();
		if(huoDongName!=null)
		{
			tv_zhongqiu_ranking.setVisibility(View.VISIBLE);
			params.width = width/4;
			view.setLayoutParams(params);
		}else
		{
			tv_zhongqiu_ranking.setVisibility(View.GONE);
			params.width = width/3;
			view.setLayoutParams(params);
		}
		
		if(huoDongName!=null)
		{
			page_fragment = 4;
			tv_datas.add(tv_zhongqiu_ranking);
			waKuangHuoDong_Ranking = new WaKuangHuoDong_Ranking();
			datas.add(waKuangHuoDong_Ranking);
		}else
		{
			page_fragment = 3;
		}

		    tv_datas.add(tv_all_ranking);
		    tv_datas.add(tv_zhou_ranking);
		    tv_datas.add(tv_month_ranking);

		    waKuangZong_Ranking = new WaKuangZong_Ranking();
		    waKuangMonth_Ranking = new WaKuangMonth_Ranking();
		    waKuangWeek_Ranking = new WaKuangWeek_Ranking();

		    datas.add(waKuangZong_Ranking);
	    	datas.add(waKuangWeek_Ranking);
			datas.add(waKuangMonth_Ranking);

			vp.setAdapter(new TransactionAdapter(getSupportFragmentManager(),datas));
	    	vp.setOffscreenPageLimit(3);
			tv_wakuang.setOnClickListener(this);
	}

	public void back(View view) {
		finish();
	}


	@Override
	public void initData() {
		
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_zhou_ranking:
			if(huoDongName==null)
			{
				vp.setCurrentItem(1);
			}else
			{
				vp.setCurrentItem(2);
			}
			translateRedLine(tv_zhou_ranking);
			setTextViewBGColor(tv_zhou_ranking);
			setWeekData("week");
			break;


		case R.id.tv_month_ranking:
			if(huoDongName==null)
			{
				vp.setCurrentItem(2);
			}else
			{
				vp.setCurrentItem(3);
			}
			translateRedLine(tv_month_ranking);
			setTextViewBGColor(tv_month_ranking);
			setWeekData("month");
			break;
		case R.id.tv_all_ranking:
			if(huoDongName==null)
			{
				vp.setCurrentItem(0);
			}else
			{
				vp.setCurrentItem(1);
			}
			translateRedLine(tv_all_ranking);
			setTextViewBGColor(tv_all_ranking);
			setWeekData("today");
			break;

			
		case R.id.tv_zhongqiu_ranking:
				vp.setCurrentItem(0);
			translateRedLine(tv_zhongqiu_ranking);
			setTextViewBGColor(tv_zhongqiu_ranking);
			break;

		case R.id.tv_wakuang:
			int count = vp.getCurrentItem();
			if(huoDongName==null)
			{
				if (count == 0){
					Tools.shuangji(waKuangWeek_Ranking.getLv());
				}else if (count == 1){
					Tools.shuangji(waKuangMonth_Ranking.getLv());
				}else if (count == 2){
					Tools.shuangji(waKuangZong_Ranking.getLv());
				}
			}else
			{
				if (count == 0){
					Tools.shuangji(waKuangHuoDong_Ranking.getLv());
				}else if (count == 1){
					Tools.shuangji(waKuangWeek_Ranking.getLv());
				}else if (count == 2){
					Tools.shuangji(waKuangMonth_Ranking.getLv());
				}else if (count == 3){
					Tools.shuangji(waKuangZong_Ranking.getLv());
				}
			}

			break;
		}
	}



	private void setTextViewBGColor(TextView tv_sel)
	{
		for(int i = 0 ; i < tv_datas.size(); i ++)
		{
			if(tv_sel.getId()==tv_datas.get(i).getId())
			{
				((TextView)tv_datas.get(i)).setTextColor(0xffff9900);
			}else
			{
				((TextView)tv_datas.get(i)).setTextColor(0xff000000);
			}
		}
	}
	
	private void isYouHuoDong()
	{
		HttpConnect.post(this, "member_mine_ranking_list_activity_set", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				JSONObject object = JSONObject.fromObject(arg0.body().string());
				if (object.get("status").equals("success")) {
					
					msg = object.getString("msg");
					
					JSONArray array = object.optJSONArray("data");
					if (array.size() != 0) {
						JSONObject object2 = array.getJSONObject(0);
						String enable = object2.getString("enable");
						if(enable.equals("true"))
						{//2009-01-01 12:00:00
							huoDongName = object2.getString("title");
							startdate = object2.getString("startdate");
							getSharedPreferences("startDate", Context.MODE_APPEND).edit().putString("startdate", startdate).commit();
							Handler dataHandler = new Handler(WaKuangRankingActivity.this
									.getMainLooper()) {

								@Override
								public void handleMessage(final Message msg) {
									tv_zhongqiu_ranking.setVisibility(View.VISIBLE);
									tv_zhongqiu_ranking.setText(huoDongName);
									setTextViewBGColor(tv_zhongqiu_ranking);
								}
							};
							dataHandler.sendEmptyMessage(0);
						}else
						{
							Handler dataHandler = new Handler(WaKuangRankingActivity.this
									.getMainLooper()) {

								@Override
								public void handleMessage(final Message msg) {
									tv_zhongqiu_ranking.setVisibility(View.GONE);
									setTextViewBGColor(tv_all_ranking);
								}
							};
							dataHandler.sendEmptyMessage(0);
						}
					} else {
						Handler dataHandler = new Handler(WaKuangRankingActivity.this
								.getMainLooper()) {

							@Override
							public void handleMessage(final Message msg) {
                                 WaKuangRankingActivity.this.sToast( WaKuangRankingActivity.this.msg);
							}
						};
						dataHandler.sendEmptyMessage(0);
					}
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Handler dataHandler = new Handler(WaKuangRankingActivity.this
						.getMainLooper()) {

					@Override
					public void handleMessage(final Message msg) {
                         WaKuangRankingActivity.this.sToast("访问网络失败");
					}
				};
				dataHandler.sendEmptyMessage(0);
			}
		});
	}
	

	
	private void gerenTodayRanking(String request_ranking)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);

		HttpConnect.post(this, request_ranking, map, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				
				JSONObject object = JSONObject.fromObject(arg0.body().string());
				
					if (object.get("status").equals("success")) {
						JSONArray array = object.optJSONArray("data");
						if(array != null){
								JSONObject object2 = array.getJSONObject(0);
								todaySelfRanking = new RankingData();
								todaySelfRanking.setImgUrl(object2.getString("pic"));
								todaySelfRanking.setName(object2.getString("name"));
								todaySelfRanking.setPrice(object2.getString("bonusrebatetotal"));
								todaySelfRanking.setRanking(object2.getString("ranking"));
								
								Handler dataHandler = new Handler(WaKuangRankingActivity.this
										.getMainLooper()) {

									@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
									@Override
									public void handleMessage(final Message msg) {
										if(!WaKuangRankingActivity.this.isDestroyed())
										{
											Glide.with(WaKuangRankingActivity.this)
													.load(todaySelfRanking.getImgUrl())
													.placeholder(R.mipmap.iv_member_default_head_img)
													.error(R.mipmap.iv_member_default_head_img)
													.into(iv_touxiang);
											tv_name.setText(todaySelfRanking.getName());
											tv_ranking.setText("第"
													+ todaySelfRanking.getRanking() + "名");
											tv_ctc.setText(todaySelfRanking.getPrice());
										}
									}
								};
								dataHandler.sendEmptyMessage(0);
						}
					}else
					{
						Handler dataHandler = new Handler(WaKuangRankingActivity.this
								.getMainLooper()) {

							@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
							@Override
							public void handleMessage(final Message msg) {
                                if(!isDestroyed())
								{
									Glide.with(WaKuangRankingActivity.this)
											.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
											.placeholder(R.mipmap.iv_member_default_head_img)
											.error(R.mipmap.iv_member_default_head_img)
											.into(iv_touxiang);
									tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
									tv_ranking.setText("暂无排名");
									tv_ctc.setText("0");
								}
							}
						};
						dataHandler.sendEmptyMessage(0);
					}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
			}
		});
	}

	private void gerenWeekRanking(String request_ranking)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);

		HttpConnect.post(this, request_ranking, map, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				JSONObject object = JSONObject.fromObject(arg0.body().string());

				if (object.get("status").equals("success")) {
					JSONArray array = object.optJSONArray("data");
					if(array != null){
							JSONObject object2 = array.getJSONObject(0);
							weekSelfRanking = new RankingData();
							weekSelfRanking.setImgUrl(object2.getString("pic"));
							weekSelfRanking.setName(object2.getString("name"));
							weekSelfRanking.setPrice(object2
									.getString("bonusrebatetotal"));
							weekSelfRanking.setRanking(object2.getString("ranking"));

					}
				}else
				{
					Handler dataHandler = new Handler(WaKuangRankingActivity.this
							.getMainLooper()) {

						@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
						@Override
						public void handleMessage(final Message msg) {
							if (!WaKuangRankingActivity.this.isDestroyed()){
								Glide.with(WaKuangRankingActivity.this)
										.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
										.placeholder(R.mipmap.iv_member_default_head_img)
										.error(R.mipmap.iv_member_default_head_img)
										.into(iv_touxiang);
								tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
								tv_ranking.setText("暂无排名");
								tv_ctc.setText("0");
							}

						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

			}
		});
	}

	private void gerenMonthRanking(String request_ranking)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);

		HttpConnect.post(this, request_ranking, map, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

				JSONObject object = JSONObject.fromObject(arg0.body().string());

				if (object.get("status").equals("success")) {
					JSONArray array = object.optJSONArray("data");
					if(array != null){
						JSONObject object2 = array.getJSONObject(0);
						monthSelfRanking = new RankingData();
						monthSelfRanking.setImgUrl(object2.getString("pic"));
						monthSelfRanking.setName(object2.getString("name"));
						monthSelfRanking.setPrice(object2
								.getString("bonusrebatetotal"));
						monthSelfRanking.setRanking(object2.getString("ranking"));

					}
				}else
				{
					Handler dataHandler = new Handler(WaKuangRankingActivity.this
							.getMainLooper()) {

						@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
						@Override
						public void handleMessage(final Message msg) {
							if (!WaKuangRankingActivity.this.isDestroyed()){
								Glide.with(WaKuangRankingActivity.this)
										.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
										.placeholder(R.mipmap.iv_member_default_head_img)
										.error(R.mipmap.iv_member_default_head_img)
										.into(iv_touxiang);
								tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
								tv_ranking.setText("暂无排名");
								tv_ctc.setText("0");
							}

						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

			}
		});
	}


	public String getStartdate() {
		return startdate;
	}


	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public int getHuoDongData()
	{
		 ArrayList<RankingData> list_data = new ArrayList<RankingData>();
		 list_data = ((WaKuangZong_Ranking)datas.get(0)).getList_data();
		return list_data.size();
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void setWeekData(String time)
	{
		if(time.equals("today"))
		{
			if(null!=todaySelfRanking)
			{
				if(null!=todaySelfRanking.getRanking())
				{
					if(!isDestroyed())
					{
						Glide.with(WaKuangRankingActivity.this)
								.load(todaySelfRanking.getImgUrl())
								.placeholder(R.mipmap.iv_member_default_head_img)
								.error(R.mipmap.iv_member_default_head_img)
								.into(iv_touxiang);
						tv_name.setText(todaySelfRanking.getName());
						tv_ranking.setText("第"
								+ todaySelfRanking.getRanking() + "名");
						tv_ctc.setText(todaySelfRanking.getPrice());
					}

				}
			}else
			{
				if(!isDestroyed())
				{
					Glide.with(WaKuangRankingActivity.this)
							.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
							.placeholder(R.mipmap.iv_member_default_head_img)
							.error(R.mipmap.iv_member_default_head_img)
							.into(iv_touxiang);
					tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
					tv_ranking.setText("暂无排名");
					tv_ctc.setText("00.00");
				}
			}

		}else if(time.equals("week"))
		{
			if(null!=weekSelfRanking)
			{
				if(null!=weekSelfRanking.getRanking())
				{
					if(!isDestroyed())
					{
						Glide.with(WaKuangRankingActivity.this)
								.load(weekSelfRanking.getImgUrl())
								.placeholder(R.mipmap.iv_member_default_head_img)
								.error(R.mipmap.iv_member_default_head_img)
								.into(iv_touxiang);
						tv_name.setText(weekSelfRanking.getName());
						tv_ranking.setText("第"
								+ weekSelfRanking.getRanking() + "名");
						tv_ctc.setText(weekSelfRanking.getPrice());
					}

				}
			}else
			{
				if(!isDestroyed())
				{
					Glide.with(WaKuangRankingActivity.this)
							.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
							.placeholder(R.mipmap.iv_member_default_head_img)
							.error(R.mipmap.iv_member_default_head_img)
							.into(iv_touxiang);
					tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
					tv_ranking.setText("暂无排名");
					tv_ctc.setText("00.00");
				}
			}
		}else if(time.equals("month"))
		{
			if(null!=monthSelfRanking)
			{
				if(null!=monthSelfRanking.getRanking())
				{
					if(!isDestroyed())
					{
						Glide.with(WaKuangRankingActivity.this)
								.load(monthSelfRanking.getImgUrl())
								.placeholder(R.mipmap.iv_member_default_head_img)
								.error(R.mipmap.iv_member_default_head_img)
								.into(iv_touxiang);
						tv_name.setText(monthSelfRanking.getName());
						tv_ranking.setText("第"
								+ monthSelfRanking.getRanking() + "名");
						tv_ctc.setText(monthSelfRanking.getPrice());
					}

				}
			}else
			{
				if(!isDestroyed())
				{
					Glide.with(WaKuangRankingActivity.this)
							.load(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getHeadUrl())
							.placeholder(R.mipmap.iv_member_default_head_img)
							.error(R.mipmap.iv_member_default_head_img)
							.into(iv_touxiang);
					tv_name.setText(LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getNickName());
					tv_ranking.setText("暂无排名");
					tv_ctc.setText("00.00");
				}
			}
		}
	}
	
}
