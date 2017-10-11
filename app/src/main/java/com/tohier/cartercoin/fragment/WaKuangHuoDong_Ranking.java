package com.tohier.cartercoin.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tohier.cartercoin.activity.WaKuangRankingActivity;
import com.tohier.cartercoin.adapter.WaKuangApapter;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.config.DateDistance;
import com.tohier.cartercoin.config.HttpConnect;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaKuangHuoDong_Ranking extends BaseFragment {

	private View view;
	private ListView lv;
	private WaKuangApapter adapter;
	private ArrayList<RankingData> list_data = new ArrayList<RankingData>();
	private String request_title;
	private String msg;
	private TextView daojishi,tv;
	private boolean is_start = false;

	public ListView getLv() {
		return lv;
	}

	public ArrayList<RankingData> getList_data() {
		return list_data;
	}

	public void setList_data(ArrayList<RankingData> list_data) {
		this.list_data = list_data;
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			Date date = new Date();
			SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于
			String time = myFmt2.format(date);
			try {
				if(WaKuangRankingActivity.startdate!=null)
				{
					String riqi = DateDistance.getDistanceTime(time, WaKuangRankingActivity.startdate,null);
					daojishi.setText(riqi);
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_wakuang_rangking, null);
		init();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(tr);
	}
	
	private void init() {
		tv = (TextView) view.findViewById(R.id.tv);
		daojishi = (TextView) view.findViewById(R.id.daojishi);
		lv = (ListView) view.findViewById(R.id.alv_ranking);
		adapter = new WaKuangApapter(getContext(), list_data);
		lv.setAdapter(adapter);
		list_data.clear();
		tv.setText("");
		onRank();
	}

	@Override
	public void initData() {
		
	}

	public void onRank() {
		request_title = "member_mine_ranking_list_activity";
	try {
		HttpConnect.post(this,request_title , null,
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
										RankingData rankingData = new RankingData();
										rankingData.setImgUrl(object2
												.getString("pic"));
										rankingData.setName(object2
												.getString("name"));
										rankingData.setPrice(object2
												.getString("bonusrebatetotal"));
										list_data.add(rankingData);
									}
									Handler dataHandler = new Handler(
											getContext().getMainLooper()) {

										@Override
										public void handleMessage(
												final Message msg) {
												if(list_data.size()==0)
													{  //2009-01-01 12:00:00
													   
													    if(is_start!=true)
													    {
													    	handler.post(tr);
													    }
														
														lv.setVisibility(View.GONE);
														tv.setText("活动开始倒计时");
														 is_start = true;
													}else
													{
														daojishi.setVisibility(View.GONE);
														tv.setVisibility(View.GONE);
														tv.setText("");
														
														ArrayList<RankingData> list_data2 = new ArrayList<RankingData>();
													    list_data2.addAll(list_data);
														adapter.setList(list_data2);
														adapter.notifyDataSetChanged();
													}
										}
									};
									dataHandler.sendEmptyMessage(0);
								}else
								{
									Handler dataHandler = new Handler(
											getContext().getMainLooper()) {

										@Override
										public void handleMessage(
												final Message msg) {
												if(list_data.size()==0)
													{  //2009-01-01 12:00:00
													    
													    if(is_start!=true)
													    {
													    	handler.post(tr);
													    }
														lv.setVisibility(View.GONE);
														tv.setText("活动开始倒计时");
														is_start = true;
													}else
													{
														daojishi.setVisibility(View.GONE);
														tv.setVisibility(View.GONE);
														tv.setText("");
														ArrayList<RankingData> list_data2 = new ArrayList<RankingData>();
													    list_data2.addAll(list_data);
														adapter.setList(list_data2);
														adapter.notifyDataSetChanged();
													}
										}
									};
									dataHandler.sendEmptyMessage(0);
								}
							} else {
								Handler dataHandler = new Handler(
										getContext().getMainLooper()) {

									@Override
									public void handleMessage(
											final Message msg) {
											if(list_data.size()==0)
												{  //2009-01-01 12:00:00
												    if(is_start!=true)
												    {
												    	handler.post(tr);
												    }
													handler.post(tr);
													lv.setVisibility(View.GONE);
													tv.setText("活动开始倒计时");
													 is_start = true;
												}else
												{
													daojishi.setVisibility(View.GONE);
													tv.setVisibility(View.GONE);
													tv.setText("");
													ArrayList<RankingData> list_data2 = new ArrayList<RankingData>();
												    list_data2.addAll(list_data);
													adapter.setList(list_data2);
													adapter.notifyDataSetChanged();
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
								sToast("链接超时！");
							}
						};
						dataHandler.sendEmptyMessage(0);
					}
				});
	} catch (Exception e) {
	}
} 
	

}
