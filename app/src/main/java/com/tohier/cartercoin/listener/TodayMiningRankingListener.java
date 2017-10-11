package com.tohier.cartercoin.listener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.GongGaoActivity;
import com.tohier.cartercoin.adapter.WaKuangApapter;
import com.tohier.cartercoin.bean.RankingData;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class TodayMiningRankingListener implements PullToRefreshLayout.OnRefreshListener {

	private int page = 1;
	private Context ctx;
	private ArrayList<RankingData> list_data = new ArrayList<RankingData>();
	private WaKuangApapter adapter;
	private GifImageView gifLoading;
	private ImageView ivNodata;

	public ArrayList<RankingData>  getDatas() {
		return list_data;
	}

	public void setDatas(ArrayList<RankingData> list_data) {
		this.list_data = list_data;
	}

	public TodayMiningRankingListener(Context ctx, WaKuangApapter adapter, GifImageView gifLoading, ImageView ivNodata) {
		super();
		this.ctx = ctx;
		this.adapter = adapter;
		this.gifLoading = gifLoading;
		this.ivNodata = ivNodata;
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		// 下拉刷新操作
		// 千万别忘了告诉控件刷新完毕了哦！
				new Thread()
				{
					@Override
					public void run() {
						page = page -1;
						if(page!=1)
						{
							getJsonDate(pullToRefreshLayout);
						}else
						{
							((GongGaoActivity)ctx).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if(pullToRefreshLayout!=null)
									{
										pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
									}
								}
							});
						}
					}
				}.start();
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		// 加载操作
		// 千万别忘了告诉控件刷新完毕了哦！
		new Thread()
		{
			@Override
			public void run() {
				page = page + 1;
				if(page==1)
				{
					list_data.clear();
				}
				getJsonDate(pullToRefreshLayout);
			}
		}.start();
	}
	
	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("page", page+"");
		// mZProgressHUD.show();
		HttpConnect.post(((GongGaoActivity)ctx), "member_minning_today", par, new Callback() {

			private String msg;

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body()
						.string().trim());

				msg = data.getString("msg");
				
				if (data.get("status").equals("success"))
				{
					JSONArray array = data
							.optJSONArray("data");
					if (array.size() != 0)
					{
						for (int i = 0; i < array.size(); i++)
						{
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
						((GongGaoActivity)ctx).runOnUiThread(new Runnable() {

							@Override
							public void run() {

								ivNodata.setVisibility(View.GONE);
								gifLoading.setVisibility(View.GONE);
							}
						});
					}

					} else {
					((GongGaoActivity)ctx).runOnUiThread(new Runnable() {

								@Override
								public void run() {
//									((GongGaoActivity)ctx).sToast("暂无数据");
								}
							});
				}
				
				((GongGaoActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.setList(list_data);
						adapter.notifyDataSetChanged();
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						}
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
				((GongGaoActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
						}
					}
				});
				
			}
		});
	}

}
