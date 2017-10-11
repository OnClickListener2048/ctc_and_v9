package com.tohier.cartercoin.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.FengYunRankingActivity;
import com.tohier.cartercoin.adapter.DianZanRankingAdapter;
import com.tohier.cartercoin.bean.ZanRanking;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class StudentsListener implements PullToRefreshLayout.OnRefreshListener {

	private int page = 1;
	private Context ctx;
	private ArrayList<ZanRanking> datas = new ArrayList<ZanRanking>();
	private DianZanRankingAdapter adapter;
	private GifImageView gifImageView;

	public StudentsListener(Context ctx, DianZanRankingAdapter adapter, GifImageView gifImageView) {
		this.ctx = ctx;
		this.adapter = adapter;
		this.gifImageView = gifImageView;
	}

	public List<ZanRanking> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<ZanRanking> datas) {
		this.datas = datas;
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			adapter.setList(datas);
			adapter.notifyDataSetChanged();
		};
	};


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
							((FengYunRankingActivity)ctx).runOnUiThread(new Runnable() {
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
					datas.clear();
				}
				getJsonDate(pullToRefreshLayout);
			}
		}.start();
	}

	
	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {
		((FengYunRankingActivity)ctx).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				gifImageView.setVisibility(View.VISIBLE);
			}
		});

		Map<String, String> par = new HashMap<String, String>();
		par.put("page",page+"");
		HttpConnect.post(((FengYunRankingActivity)ctx), "member_convergence_list", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body()
						.string());

				if (data.get("status").equals("success")) {
					  JSONArray dataArr = data.optJSONArray("data");

						   for (int i = 0; i < dataArr.size(); i++) {
							   JSONObject object2 = dataArr.optJSONObject(i);
							   if (object2 != null){
								   ZanRanking zanRanking = new ZanRanking();
								   zanRanking.setId(object2
										   .optString("id"));
								   zanRanking.setPic(object2
										   .optString("pic"));
								   zanRanking.setName(object2
										   .optString("name"));
								   zanRanking.setPraisenum(object2
										   .optString("praisenum"));
								   zanRanking.setLevel(object2.optString("level"));
								   zanRanking.setPraiseme(object2.optString("praiseme"));
								   zanRanking.setPower(object2.optString("power"));
								   zanRanking.setMobile(object2.optString("mobile"));
								   zanRanking.setLinkCode(object2.optString("linkcode"));
								   zanRanking.setType(object2.optString("type"));
								   datas.add(zanRanking);
							   }

						   }
				} else {
					((FengYunRankingActivity)ctx).runOnUiThread(new Runnable() {

								@Override
								public void run() {
									gifImageView.setVisibility(View.GONE);
								}
							});
				}

				((FengYunRankingActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.setList(datas);
						adapter.notifyDataSetChanged();
						gifImageView.setVisibility(View.GONE);
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						}
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

				((FengYunRankingActivity)ctx).runOnUiThread(new Runnable() {

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
