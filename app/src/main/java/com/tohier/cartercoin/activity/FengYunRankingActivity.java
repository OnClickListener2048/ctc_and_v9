package com.tohier.cartercoin.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;
import com.tohier.cartercoin.config.Tools;
import com.tohier.cartercoin.dianzanfragment.DianZanAll_Ranking;
import com.tohier.cartercoin.dianzanfragment.TongXueHui_Ranking;
import com.tohier.cartercoin.miningrankingfragment.WaKuangZong_Ranking;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FengYunRankingActivity extends MyBaseFragmentActivity implements View.OnClickListener{

	private CircleImageView pv_touxiang;
	private TextView tv_name, tv_ranking, tv_zan_count;
	private String id ;
    private ViewPager vp;
	private List<Fragment> datas = new ArrayList<Fragment>();
    private TextView tv_dianzan_ranking,tv_tongxuehui;
	private List<TextView> tv_datas = new ArrayList<TextView>();
	private TextView view,tv_title;
	private DianZanAll_Ranking dianZanAll_Ranking;
	private TongXueHui_Ranking tongXueHui_Ranking;
    private String startDate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fengyun_ranking);
		id = LoginUser.getInstantiation(getApplicationContext()).getLoginUser().getUserId();
		init();
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
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
					switch (arg0) {
					case 0:
						translateRedLine(tv_tongxuehui);
						setTextViewBGColor(tv_tongxuehui);
						break;
					case 1:
						translateRedLine(tv_dianzan_ranking);
						setTextViewBGColor(tv_dianzan_ranking);
						dianZanAll_Ranking.onRank();
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

	private void init() {
		view = (TextView) findViewById(R.id.tv_red_line);
		tv_dianzan_ranking = (TextView) findViewById(R.id.tv_dianzan_ranking);
		tv_tongxuehui= (TextView) findViewById(R.id.tv_tongxuehui);
		tv_dianzan_ranking.setOnClickListener(this);
		tv_tongxuehui.setOnClickListener(this);
		
		vp = (ViewPager) findViewById(R.id.vp_wakuang_ranking);
		pv_touxiang = (CircleImageView) findViewById(R.id.pv_touxiang);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_ranking = (TextView) findViewById(R.id.tv_ranking);
		tv_zan_count = (TextView) findViewById(R.id.tv_zan_count);
		tv_title = (TextView) findViewById(R.id.tv_title);

		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		LayoutParams params = view.getLayoutParams();
		params.width = width/2;
		view.setLayoutParams(params);
		 //个人排行在点赞排行中的排名
		gerenPanking("member_convergence_praise_me");

			tv_datas.add(tv_tongxuehui);
		    tv_datas.add(tv_dianzan_ranking);

			dianZanAll_Ranking = new DianZanAll_Ranking();
		     tongXueHui_Ranking = new TongXueHui_Ranking();

			datas.add(tongXueHui_Ranking);
			datas.add(dianZanAll_Ranking);

			vp.setAdapter(new TransactionAdapter(getSupportFragmentManager(),datas));
		tv_title.setOnClickListener(this);



		HttpConnect.post(this, "member_info", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					final String name = data.getJSONArray("data").getJSONObject(0).getString("nickname");
					final String pic = data.getJSONArray("data").getJSONObject(0).getString("pic");

					Handler dataHandler = new Handler(getContext()
							.getMainLooper()) {

						@Override
						public void handleMessage(final Message msg) {

							Glide.with(FengYunRankingActivity.this)
									.load(pic)
									.placeholder(R.mipmap.iv_member_default_head_img)
									.error(R.mipmap.iv_member_default_head_img)
									.into(pv_touxiang);
							tv_name.setText(name);

						}
					};
					dataHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public  void onFailure(Request arg0, IOException arg1) {

			}
		});




	}

	public void back(View view) {
		finish();
	}
	@Override
	public void initData() {
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tongxuehui:
			vp.setCurrentItem(0);

			translateRedLine(tv_tongxuehui);
			setTextViewBGColor(tv_tongxuehui);
			break;
		case R.id.tv_dianzan_ranking:
			vp.setCurrentItem(1);
			translateRedLine(tv_dianzan_ranking);
			setTextViewBGColor(tv_dianzan_ranking);
			break;
		//双击置顶
		case R.id.tv_title:
			if (vp.getCurrentItem() == 0) {
				Tools.shuangji(tongXueHui_Ranking.getLv());
			}else if (vp.getCurrentItem() == 1){
				Tools.shuangji(dianZanAll_Ranking.getLv());
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

	
	private void gerenPanking(String request_ranking) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);

		HttpConnect.post(this, request_ranking, map, new Callback() {

			private ZanRanking zanRanking;

			@Override
			public void onResponse(Response arg0) throws IOException {
				
				JSONObject object = JSONObject.fromObject(arg0.body().string());
				
					if (object.get("status").equals("success")) {
						JSONArray array = object.optJSONArray("data");
						if(array != null){
							if (array.size() != 0) {
								JSONObject object2 = array.optJSONObject(0);
								zanRanking = new ZanRanking();
								zanRanking.setPic(object2.optString("pic"));
								zanRanking.setName(object2.optString("name"));
								zanRanking.setPraisenum(object2
										.optString("praisenum"));
								zanRanking.setRanking(object2.optString("ranking"));
								
								Handler dataHandler = new Handler(FengYunRankingActivity.this
										.getMainLooper()) {

									@Override
									public void handleMessage(final Message msg) {
										
										tv_ranking.setText("第"
												+ zanRanking.getRanking() + "名");
										tv_zan_count.setText(zanRanking.getPraisenum());
									}
								};
								dataHandler.sendEmptyMessage(0);
							}else
							{
								Handler dataHandler = new Handler(FengYunRankingActivity.this
										.getMainLooper()) {

									@Override
									public void handleMessage(final Message msg) {
										
										tv_ranking.setText("暂无排名");
										tv_zan_count.setText("00.00");
									}
								};
								dataHandler.sendEmptyMessage(0);
							}
						}else
						{
							Handler dataHandler = new Handler(FengYunRankingActivity.this
									.getMainLooper()) {

								@Override
								public void handleMessage(final Message msg) {
									
									tv_ranking.setText("暂无排名");
									tv_zan_count.setText("00.00");
								}
							};
							dataHandler.sendEmptyMessage(0);
						}
					}else
					{
						Handler dataHandler = new Handler(FengYunRankingActivity.this
								.getMainLooper()) {

							@Override
							public void handleMessage(final Message msg) {
								
								tv_ranking.setText("暂无排名");
								tv_zan_count.setText("00.00");
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

	public int getHuoDongData()
	{
		 ArrayList<RankingData> list_data = new ArrayList<RankingData>();
		 list_data = ((WaKuangZong_Ranking)datas.get(0)).getList_data();
		return list_data.size();
	}

	public TextView getTv_dianzan_ranking() {
		return tv_dianzan_ranking;
	}

	public void setTv_dianzan_ranking(TextView tv_dianzan_ranking) {
		this.tv_dianzan_ranking = tv_dianzan_ranking;
	}
}
