package com.tohier.cartercoin.listener;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.MeActivity;
import com.tohier.cartercoin.adapter.GongGaoListAdapter;
import com.tohier.cartercoin.bean.Entrust;
import com.tohier.cartercoin.bean.News;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GongGaoListListener implements PullToRefreshLayout.OnRefreshListener {

	private int page = 1;
	private Context ctx;
	private List<News> datas = new ArrayList<News>();
	private GongGaoListAdapter adapter;

	public GongGaoListListener(Context ctx, GongGaoListAdapter adapter) {
		super();
		this.ctx = ctx;
		this.adapter = adapter;
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
							((MeActivity)ctx).runOnUiThread(new Runnable() {
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
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				page = page + 1;
				if(page==1)
				{
					datas.clear();
				}
				getJsonDate(pullToRefreshLayout);
			}
		}.start();
	}


	/**
	 * 解析数据
	 * @param pullToRefreshLayout
     */
	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {

		Map<String, String> par = new HashMap<String, String>();
		par.put("page", page+"");

		// mZProgressHUD.show();
		HttpConnect.post(((MeActivity)ctx), "article_list", par, new Callback() {

			private String msg;

			@Override
			public void onResponse(Response arg0) throws IOException {
				final JSONObject data = JSONObject.fromObject(arg0.body()
						.string().trim());
				
				if (data.get("status").equals("success")) {
					JSONArray dataArr = data.optJSONArray("data");
					Entrust entrust;
					   for(int i = 0 ; i < dataArr.size() ; i ++)
					   {
						   JSONObject jsonObjNews = dataArr.optJSONObject(i);
						   if(jsonObjNews!=null)
						   {
							   String id = jsonObjNews.getString("id");
							   String title = jsonObjNews.getString("title");
							   String pic = jsonObjNews.getString("pic");
							   String clicks = jsonObjNews.getString("clicks");
							   String likes = jsonObjNews.getString("likes");
							   String createdate = jsonObjNews.getString("createdate");
							   String url = jsonObjNews.getString("url");
							   String desc = jsonObjNews.getString("description");
							   News news = new News(id, title, pic, clicks, likes, createdate,desc,url);
							   datas.add(news);
						   }
					   }
				} else {
					((MeActivity)ctx).runOnUiThread(new Runnable() {

								@Override
								public void run() {
//									((IndexActivity)ctx).sToast("暂无数据");
								}
							});
				}
				
				((MeActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.setList(datas);
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
				
				((MeActivity)ctx).runOnUiThread(new Runnable() {

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
