package com.tohier.cartercoin.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.RecordActivity;
import com.tohier.cartercoin.adapter.RecordAdapter;
import com.tohier.cartercoin.bean.Record;
import com.tohier.cartercoin.bean.Transaction;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class RecordListener implements PullToRefreshLayout.OnRefreshListener {

	private int page = 1;
	private Context ctx;
	private ArrayList<Record> datas = new ArrayList<Record>();
	private RecordAdapter adapter;
	private GifImageView gifLoading;
	private ImageView ivNodata;


	public ArrayList<Record> getDatas() {
		return datas;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setDatas(ArrayList<Record> datas) {
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

	public RecordListener(Context ctx, RecordAdapter adapter, GifImageView gifLoading, ImageView ivNodata) {
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
							((RecordActivity)ctx).runOnUiThread(new Runnable() {
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


	/**
	 * 解析数据
	 * @param pullToRefreshLayout
     */
	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("page", page+"");
		((RecordActivity)ctx).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				gifLoading.setVisibility(View.VISIBLE);
			}
		});
		HttpConnect.post(((RecordActivity)ctx), "member_cash_in_list", par, new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body().string());
				
				if (data.get("status").equals("success")) {
					JSONArray dataArr = data.optJSONArray("data");
					Transaction transaction;
					   for(int i = 0 ; i < dataArr.size() ; i ++)
					   {
						   JSONObject jsonObjNews = dataArr.optJSONObject(i);
						   if(jsonObjNews!=null)
						   {
							   String rownum = jsonObjNews.optString("rownum");
							   String id = jsonObjNews.optString("id");
							   String paymode = jsonObjNews.optString("paymode");
							   String price = jsonObjNews.optString("qty");
							   String date = jsonObjNews.optString("date");
							   String status = jsonObjNews.optString("status");
							   Record record = new Record(date,price,paymode,status);
							   record.setId(id);
							   datas.add(record);

						   }
					   }
				} else {
					((RecordActivity)ctx).runOnUiThread(new Runnable() {

								@Override
								public void run() {
//									((IndexActivity)ctx).sToast("暂无数据");

								}
							});
				}
				
				((RecordActivity)ctx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter.setList(datas);
						adapter.notifyDataSetChanged();
						gifLoading.setVisibility(View.GONE);
						if (datas.size()>0){
							ivNodata.setVisibility(View.GONE);
						}else{
							ivNodata.setVisibility(View.VISIBLE);
						}
						if(pullToRefreshLayout!=null)
						{
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						}
					}
				});
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
				((RecordActivity)ctx).runOnUiThread(new Runnable() {

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
