package com.tohier.cartercoin.listener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tohier.cartercoin.activity.YouHuiJuanActivity;
import com.tohier.cartercoin.adapter.YouHuiJuan_Adapter;
import com.tohier.cartercoin.bean.YouHuiQuan;
import com.tohier.cartercoin.columnview.PullToRefreshLayout;
import com.tohier.cartercoin.config.HttpConnect;
import com.tohier.cartercoin.config.LoginUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class YiShiYongListener implements PullToRefreshLayout.OnRefreshListener {

//	private int page = 1;
	private Context ctx;
	private List<YouHuiQuan> datas = new ArrayList<YouHuiQuan>();
	private YouHuiJuan_Adapter adapter;
	private ImageView iv_isnull;
	private GifImageView gif_loading;

//	public void setPage(int page) {
//		this.page = page;
//	}

	public List<YouHuiQuan> getDatas() {
		return datas;
	}

	public void setDatas(List<YouHuiQuan> datas) {
		this.datas = datas;
	}

	public YiShiYongListener(Context ctx, YouHuiJuan_Adapter adapter, ImageView iv_isnull, GifImageView gif_loading) {
		this.ctx = ctx;
		this.adapter = adapter;
		this.iv_isnull = iv_isnull;
		this.gif_loading = gif_loading;
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		// 下拉刷新操作
		// 千万别忘了告诉控件刷新完毕了哦！
//				new Thread()
//				{
//					@Override
//					public void run() {
//						page = 1;
//						datas.clear();
//						getJsonDate(pullToRefreshLayout);
//					}
//				}.start();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
//		// 加载操作
//		// 千万别忘了告诉控件刷新完毕了哦！
//		new Thread()
//		{
//			@Override
//			public void run() {
//				try {
//					sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				page = page + 1;
//				getJsonDate(pullToRefreshLayout);
//			}
//		}.start();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	}
	
	public void getJsonDate(final PullToRefreshLayout pullToRefreshLayout) {
		Map<String, String> par = new HashMap<String, String>();
		par.put("id", LoginUser.getInstantiation(ctx.getApplicationContext()).getLoginUser().getUserId());
		par.put("status", 1+"");
		// mZProgressHUD.show();
		HttpConnect.post(((YouHuiJuanActivity)ctx), "member_coupon_list", par, new Callback() {

			private String msg;

			@Override
			public void onResponse(Response arg0) throws IOException {
				JSONObject data = JSONObject.fromObject(arg0.body()
						.string().trim());

				msg = data.getString("msg");

				if (data.get("status").equals("success")) {
					JSONArray dataArr = data.getJSONArray("data");
					for(int i = 0 ; i < dataArr.size() ; i ++)
					{
						JSONObject jsonObjNews = dataArr.optJSONObject(i);
						if(jsonObjNews!=null)
						{
							String id = jsonObjNews.getString("id");
							String name = jsonObjNews.getString("name");
							String money = jsonObjNews.getString("money");
							String status = jsonObjNews.getString("status");
							String createdate = jsonObjNews.getString("createdate");
							String enddate = jsonObjNews.getString("enddate");
							String remark = jsonObjNews.getString("remark");

							YouHuiQuan youHuiQuan = new YouHuiQuan(id,name,money,status,createdate,enddate,remark);

							datas.add(youHuiQuan);
						}
					}


					((YouHuiJuanActivity)ctx).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							iv_isnull.setVisibility(View.GONE);
							gif_loading.setVisibility(View.GONE);
							adapter.setItems(datas);
							adapter.notifyDataSetChanged();
							if(pullToRefreshLayout!=null)
							{
								pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
							}
						}
					});
				} else {
					((YouHuiJuanActivity)ctx).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							gif_loading.setVisibility(View.GONE);
							if(datas.size()==0)
							{
								iv_isnull.setVisibility(View.VISIBLE);
							}
//							((YouHuiJuanActivity)ctx).sToast("暂无数据");
						}
					});
				}

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

				((YouHuiJuanActivity)ctx).runOnUiThread(new Runnable() {

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
