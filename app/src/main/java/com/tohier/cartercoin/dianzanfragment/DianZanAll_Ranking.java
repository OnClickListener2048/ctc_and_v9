package com.tohier.cartercoin.dianzanfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.android.fragment.base.BaseFragment;
import com.tohier.cartercoin.R;
import com.tohier.cartercoin.activity.FengYunRankingActivity;
import com.tohier.cartercoin.activity.GongGaoDetailActivity;
import com.tohier.cartercoin.adapter.DianZanRankingAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DianZanAll_Ranking extends BaseFragment {

	private View view;
	private ListView lv;
	private DianZanRankingAdapter adapter;
	private ArrayList<ZanRanking> list_data = new ArrayList<ZanRanking>();
	private String request_title;
	private String msg;
	private String id;
    private TextView tv_danzan_tishi,tv_dianzan_time,tv_href_gonggao;
	private String startDate;
	private String url;
	String enable;
	private GifImageView gifImageView;

	public ListView getLv() {
		return lv;
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			Date date = new Date();
			SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于
			String time = myFmt2.format(date);
			try {
				if(startDate!=null)
				{
					String riqi = DateDistance.getDistanceTime5(time,startDate);
					if(riqi.equals("开始"))
					{
						tv_danzan_tishi.setVisibility(View.GONE);
						tv_href_gonggao.setVisibility(View.GONE);
						tv_dianzan_time.setVisibility(View.GONE);
						lv.setVisibility(View.VISIBLE);
					}else
					{
						tv_dianzan_time.setText(riqi);
					}
				}
			} catch (Exception e) {
			}
			handler.postDelayed(tr, 1000);
		};
	};

	private Thread tr = new Thread()
	{
		public void run()
		{
			handler.sendEmptyMessage(0);
		};
	};
	private String title;
	private String urltitle;
	private String urldescription;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_dianzan_rangking1, null);
		init();
		setUpView();
		return view;
	}

	private void setUpView() {
		tv_href_gonggao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(enable.equals("true"))
				{
					Intent intent = new Intent(getActivity(),GongGaoDetailActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("title",urltitle);
					intent.putExtra("desc", urldescription);
					startActivity(intent);
				}
			}
		});
	}


	private void init() {
		id = LoginUser.getInstantiation(getActivity().getApplicationContext()).getLoginUser().getUserId();
		lv = (ListView) view.findViewById(R.id.alv_ranking);
		gifImageView = (GifImageView) view.findViewById(R.id.gif_loading);
		tv_dianzan_time = (TextView) view.findViewById(R.id.tv_dianzan_time);
		tv_danzan_tishi = (TextView) view.findViewById(R.id.tv_dianzan_tishi);
		tv_href_gonggao = (TextView) view.findViewById(R.id.tv_href_gonggao);
		tv_danzan_tishi.setText("");
		tv_href_gonggao.setText("");

		tv_href_gonggao.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		adapter = new DianZanRankingAdapter(getContext(), list_data,2,null);
		lv.setAdapter(adapter);
		lv.setSelector(new ColorDrawable(Color.TRANSPARENT));

		isYouHuoDong();
		onRank();
	}

	@Override
	public void initData() {
		
	}


	public void onRank() {
		list_data.clear();
			request_title = "member_convergence_praise_list";
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			HttpConnect.post(this,request_title , map,
					new Callback() {

						@Override
						public void onResponse(Response arg0)
								throws IOException {

							if (arg0 != null || !arg0.equals("")) {

								final JSONObject object = JSONObject.fromObject(arg0
										.body().string());
								if (object.get("status").equals("success")) {

									msg = object.getString("msg");

									JSONArray array = object
											.optJSONArray("data");
									if (array.size() != 0) {
										for (int i = 0; i < array.size(); i++) {
											JSONObject object2 = array
													.getJSONObject(i);
											ZanRanking zanRanking = new ZanRanking();
											zanRanking.setId(object2
													.getString("id"));
											zanRanking.setPic(object2
													.getString("pic"));


											zanRanking.setPraisenum(object2
													.getString("praisenum"));
											zanRanking.setLevel(object2.getString("level"));
											zanRanking.setPraiseme(object2.getString("praiseme"));
											zanRanking.setPower(object2.getString("power"));
											zanRanking.setType(object2.optString("type"));
											list_data.add(zanRanking);
										}

										Handler dataHandler = new Handler(
												getContext().getMainLooper()) {

											@Override
											public void handleMessage(
													final Message msg) {

												    gifImageView.setVisibility(View.GONE);

												    ArrayList<ZanRanking> list_data2 = new ArrayList<ZanRanking>();
												    list_data2.addAll(list_data);
													adapter.setList(list_data2);
													adapter.notifyDataSetChanged();
											}
										};
										dataHandler.sendEmptyMessage(0);
									}
								} else {
									Handler dataHandler = new Handler(
											getContext().getMainLooper()) {
										@Override
										public void handleMessage(
												final Message msg)
										{
											if(!TextUtils.isEmpty(DianZanAll_Ranking.this.msg))
											{
												sToast(DianZanAll_Ranking.this.msg);
											}
										}
									};
									dataHandler.sendEmptyMessage(0);
								}

							}

						}

						@Override
						public void onFailure(Request arg0, IOException arg1) {


							Handler dataHandler = new Handler(
									getContext().getMainLooper()) {

								@Override
								public void handleMessage(
										final Message msg) {
//									sToast("链接超时！");
								}
							};
							dataHandler.sendEmptyMessage(0);
						}
					});
		} catch (Exception e) {
		}
	}

	private void isYouHuoDong()

	{
		HttpConnect.post(this, "member_convergence_praise_list_set", null, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				if (data.get("status").equals("success")) {
					enable = data.optJSONArray("data").optJSONObject(0).optString("enable");
					if(enable!=null&&enable.equals("true")){
						title = data.optJSONArray("data").optJSONObject(0).optString("title");
						startDate = data.optJSONArray("data").optJSONObject(0).optString("startdate");
						url = data.optJSONArray("data").optJSONObject(0).optString("url");
						urltitle = data.optJSONArray("data").optJSONObject(0).optString("urltitle");
						urldescription = data.optJSONArray("data").optJSONObject(0).optString("urldescription");

						Handler dataHandler = new Handler(
								getContext().getMainLooper()) {
							@Override
							public void handleMessage(final Message msg)
							{
								if(!TextUtils.isEmpty(title))
								{
									((FengYunRankingActivity)getActivity()).getTv_dianzan_ranking().setText(title);
								}
								if(!TextUtils.isEmpty(startDate))
								{
                                        handler.post(tr);
								}
								lv.setVisibility(View.GONE);
								tv_href_gonggao.setVisibility(View.VISIBLE);
								tv_danzan_tishi.setText("距离点赞活动开始时间还剩");
								tv_href_gonggao.setText("查看活动细则");
							}
						};
						dataHandler.sendEmptyMessage(0);
					}else
					{
						Handler dataHandler = new Handler(
								getContext().getMainLooper()) {
							@Override
							public void handleMessage(final Message msg)
							{
								((FengYunRankingActivity)getActivity()).getTv_dianzan_ranking().setText("圈内人");
								tv_href_gonggao.setVisibility(View.GONE);
							}
						};
						dataHandler.sendEmptyMessage(0);
					}
				}
			}


			@Override
			public void onFailure(Request arg0, IOException arg1) {
				mZProgressHUD.cancel();
//				sToast("链接超时！");
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(tr);
	}
}
