package com.tohier.cartercoin.miningrankingfragment;

import android.graphics.Color;
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
import com.tohier.cartercoin.adapter.WaKuangApapter;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.columnview.LoadingView;
import com.tohier.cartercoin.columnview.NoDataView;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WaKuangWeek_Ranking extends BaseFragment {

	private View view;
	private ListView lv;
	private WaKuangApapter adapter;
	private ArrayList<RankingData> list_data = new ArrayList<RankingData>();
	private String request_title;
	private String msg;
	private TextView daojishi,tv;
	private LoadingView gifLoading;
	private NoDataView ivNodata;

	public ListView getLv() {
		return lv;
	}
	
	public ArrayList<RankingData> getList_data() {
		return list_data;
	}

	public void setList_data(ArrayList<RankingData> list_data) {
		this.list_data = list_data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_wakuang_rangking, null);
		init();
		return view;
	}
	
	private void init() {
		tv = (TextView) view.findViewById(R.id.tv);
		daojishi = (TextView) view.findViewById(R.id.daojishi);
		lv = (ListView) view.findViewById(R.id.alv_ranking);
		gifLoading = (LoadingView) view.findViewById(R.id.cif_loading);
		ivNodata = (NoDataView) view.findViewById(R.id.iv_nodata);

		adapter = new WaKuangApapter(getContext(), list_data);
		lv.setAdapter(adapter);
		lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		list_data.clear();
		tv.setText("");
		onRank();
	}

	@Override
	public void initData() {
		
	}

	public void onRank() {
			request_title = "member_mine_ranking_list_week";

		gifLoading.setVisibility(View.VISIBLE);
		try {
			HttpConnect.post(this,request_title , null,new Callback() {

						@Override
						public void onResponse(Response arg0)
								throws IOException {

							if (arg0 != null || !arg0.equals("")) {

								final JSONObject object = JSONObject.fromObject(arg0
										.body().string());

								msg = object.getString("msg");
								if (object.get("status").equals("success")) {
									

									
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
												ArrayList<RankingData> list_data2 = new ArrayList<RankingData>();
											    list_data2.addAll(list_data);

												if (list_data2.size()>0){
													adapter.setList(list_data2);
													adapter.notifyDataSetChanged();
													ivNodata.setVisibility(View.GONE);
													gifLoading.setVisibility(View.GONE);
												}else{
													gifLoading.setVisibility(View.GONE);
													ivNodata.setVisibility(View.VISIBLE);
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
												final Message msg) 
										{

											if(!TextUtils.isEmpty(WaKuangWeek_Ranking.this.msg))
											{
												if(WaKuangWeek_Ranking.this.msg.equals("nodata"))
												{
													gifLoading.setVisibility(View.GONE);
													ivNodata.setVisibility(View.VISIBLE);
												}
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
	
	
	

}